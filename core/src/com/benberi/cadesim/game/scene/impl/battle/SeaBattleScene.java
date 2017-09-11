package com.benberi.cadesim.game.scene.impl.battle;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.math.Bezier;
import com.badlogic.gdx.math.Vector2;
import com.benberi.cadesim.GameContext;
import com.benberi.cadesim.game.entity.projectile.CannonBall;
import com.benberi.cadesim.game.entity.vessel.Vessel;
import com.benberi.cadesim.game.entity.vessel.VesselMovementAnimation;
import com.benberi.cadesim.game.entity.vessel.impl.WarFrigate;
import com.benberi.cadesim.game.entity.vessel.move.*;
import com.benberi.cadesim.game.scene.GameScene;
import com.benberi.cadesim.game.scene.impl.battle.tile.GameTile;

import java.util.Iterator;

public class SeaBattleScene implements GameScene {

    private static final float velocityTurns = 0.025f;
    private static final float velocityForward = 0.03f;

    /**
     * The main game context
     */
    private GameContext context;

    /**
     * The sprite batch renderer
     */
    private SpriteBatch batch;

    /**
     * Batch for some UI elements over the battle arena
     */
    private SpriteBatch uiBatch;

    /**
     * The battle map
     */
    private SeaMap map;

    /**
     * The camera view of the scene
     */
    private OrthographicCamera camera;

    private Vessel ship;

    /**
     * The sea texture
     */
    private Texture sea;

    /**
     * If the user can drag the map
     */
    private boolean canDragMap;

    private GameInformation information;

    private BitmapFont font;

    private long lastMove;

    private int currentSlot = -1;
    private MovePhase currentPhase;

    public SeaBattleScene(GameContext context) {
        this.context = context;
        information = new GameInformation(context, this);
    }

    public void createMap(int[][] tiles) {
        map = new SeaMap(tiles);
    }

    @Override
    public void create() {


        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("core/assets/font/FjallaOne-Regular.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 12;
        parameter.shadowColor = new Color(0, 0, 0, 0.8f);
        parameter.shadowOffsetY = 1;
        font = generator.generateFont(parameter);

        this.batch = new SpriteBatch();
        information.create();
        sea = new Texture("core/assets/sea/sea1.png");
        sea.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);
        camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight() - 200);
    }

    @Override
    public void update()
    {
        // update the camera
        camera.update();

        if (currentSlot > -1) {
            if (context.getEntities().countVsselsByPhase(currentPhase) == context.getEntities().count()) {
                MovePhase phase = MovePhase.getNext(currentPhase);
                if (phase == null) {
                    currentPhase = MovePhase.MOVE_TOKEN;
                    currentSlot++;

                    for (Vessel v : context.getEntities().listVesselEntities()) {
                        v.setMovePhase(null);
                    }

                    if (currentSlot > 3) {
                        currentSlot = -1;
                    }
                }
                else {
                    currentPhase = phase;
                }
            }
        }

        for (Vessel vessel : context.getEntities().listVesselEntities()) {
            if (!vessel.isMoving()) {
                if (currentSlot != -1) {
                    MoveAnimationTurn turn = vessel.getStructure().getTurn(currentSlot);
                    if (currentPhase == MovePhase.MOVE_TOKEN && vessel.getMovePhase() == null) {
                        if (turn.getAnimation() != VesselMovementAnimation.NO_ANIMATION) {
                            vessel.performMove(turn.getAnimation());
                            turn.setAnimation(VesselMovementAnimation.NO_ANIMATION);
                        }
                        else {
                            vessel.setMovePhase(MovePhase.getNext(vessel.getMovePhase()));
                        }
                    }
                    else if (currentPhase == MovePhase.ACTION_MOVE && vessel.getMovePhase() == MovePhase.MOVE_TOKEN) {
                        if (turn.getSubAnimation() != VesselMovementAnimation.NO_ANIMATION) {
                            vessel.performMove(turn.getSubAnimation());
                            turn.setSubAnimation(VesselMovementAnimation.NO_ANIMATION);
                        }
                        else {
                            vessel.setMovePhase(MovePhase.getNext(vessel.getMovePhase()));
                        }
                    }
                    else if (currentPhase == MovePhase.SHOOT && vessel.getMovePhase() == MovePhase.ACTION_MOVE) {
                        if (turn.getLeftShoots() == 0 && turn.getRightShoots() == 0) {
                            vessel.setMovePhase(MovePhase.getNext(vessel.getMovePhase()));
                        }
                        else {
                            if (turn.getLeftShoots() > 0) {
                                vessel.performLeftShoot(turn.getLeftShoots());
                                turn.setLeftShoots(0);
                            }
                            if (turn.getRightShoots() > 0) {
                                vessel.performRightShoot(turn.getRightShoots());
                                turn.setRightShoots(0);
                            }
                        }
                    }
                }

                Iterator<CannonBall> itr = vessel.getCannonballs().iterator();
                while (itr.hasNext()) {
                    CannonBall c = itr.next();
                    if (!c.reached()) {
                        c.move();
                    }
                    else {
                        itr.remove();
                    }
                }
            }
            else {
                VesselMovementAnimation move = vessel.getCurrentPerformingMove();

                Vector2 start = vessel.getAnimation().getStartPoint();
                Vector2 inbetween = vessel.getAnimation().getInbetweenPoint();
                Vector2 end = vessel.getAnimation().getEndPoint();
                Vector2 current = vessel.getAnimation().getCurrentAnimationLocation();

                // calculate step based on progress towards target (0 -> 1)
                // float step = 1 - (ship.getEndPoint().dst(ship.getLinearVector()) / ship.getDistanceToEndPoint());
                if (move != VesselMovementAnimation.MOVE_FORWARD) {
                    vessel.getAnimation().addStep(velocityTurns);
                    // step on curve (0 -> 1), first bezier point, second bezier point, third bezier point, temporary vector for calculations
                    Bezier.quadratic(current, (float) vessel.getAnimation().getCurrentStep(), start.cpy(),
                            inbetween.cpy(), end.cpy(), new Vector2());
                }
                else {
                    // When ship moving forward, we may not want to use the curve
                    int add = move.getIncrementXForRotation(vessel.getRotationIndex());
                    if (add == -1 || add == 1) {
                        current.x += (velocityForward * (float) add);
                    }
                    else {
                        add = move.getIncrementYForRotation(vessel.getRotationIndex());
                        current.y += (velocityForward * (float) add);
                    }
                    vessel.getAnimation().addStep(velocityForward);
                }

                int result = (int) (vessel.getAnimation().getCurrentStep() * 100);

                // check if the step is reached to the end, and dispose the movement
                if (result >= 100) {
                    vessel.setX(end.x);
                    vessel.setY(end.y);
                    vessel.setMoving(false);

                    if (move != VesselMovementAnimation.MOVE_FORWARD)
                        vessel.setRotationIndex(vessel.getRotationTargetIndex());

                    vessel.setMovePhase(MovePhase.getNext(vessel.getMovePhase()));
                }
                else {
                    // process move
                    vessel.setX(current.x);
                    vessel.setY(current.y);
                }

                // tick rotation of the ship image
                if (result % 25 == 0) {
                    vessel.tickRotation();
                }
            }
        }

        information.update();
    }

    @Override
    public void render() {

        batch.setProjectionMatrix(camera.combined);
        batch.begin();

        Gdx.gl.glViewport(0,200, Gdx.graphics.getWidth(), Gdx.graphics.getHeight() - 200);
        drawSea();

        // Render the map
        renderSeaBattle();

        // Render ships
        renderEntities();

        batch.end();

        information.render();
    }

    public GameInformation getInformation() {
        return information;
    }

    /**
     * Draws the sea background
     */
    private void drawSea() {
        batch.draw(sea, -5000, -5000, 0, 0, 10000, 10000);
    }

    /**
     * Renders all entities
     */
    private void renderEntities() {

        /*
         * Render vessels
         */
        for (Vessel vessel : context.getEntities().listVesselEntities()) {

            // X position of the vessel
            float x = getIsometricX(vessel.getX(), vessel.getY(), vessel);

            // Y position of the vessel
            float y = getIsometricY(vessel.getX(), vessel.getY(), vessel);

            String name = vessel.getName();

            // draw vessel
            batch.draw(vessel, x + vessel.getOrientationLocation().getOffsetx(), y + vessel.getOrientationLocation().getOffsety());

            GlyphLayout layout = new GlyphLayout(font, name);
            font.draw(batch, name, x + (vessel.getRegionWidth() / 2) - (layout.width / 2), y + vessel.getRegionHeight() * 1.6f);

            for (CannonBall c : vessel.getCannonballs()) {
                float cx = getIsometricX(c.getX(), c.getY(), c);
                float cy = getIsometricY(c.getX(), c.getY(), c);
                batch.draw(c, cx, cy);
            }
        }
    }


    public float getIsometricX(float x, float y, TextureRegion region) {
        return (x * GameTile.TILE_WIDTH / 2) - (y * GameTile.TILE_WIDTH / 2) - (region.getRegionWidth() / 2);
    }

    public float getIsometricY(float x, float y, TextureRegion region) {
        return (x * GameTile.TILE_HEIGHT / 2) + (y * GameTile.TILE_HEIGHT / 2) - (region.getRegionHeight() / 2);
    }

    @Override
    public void dispose() {

    }

    @Override
    public boolean handleDrag(float sx, float sy, float x, float y) {
        if (sy > camera.viewportHeight) {
            return false;
        }

        if (this.canDragMap) {
            camera.translate(-x, y);
        }

        return true;
    }

    @Override
    public boolean handleClick(float x, float y, int button) {
        if (y < camera.viewportHeight) {
            this.canDragMap = true;
            return true;
        }
        this.canDragMap = false;
        return false;
    }

    @Override
    public boolean handleClickRelease(float x, float y, int button) {
        if (y < camera.viewportHeight) {
            return true;
        }
        this.canDragMap = false;
        return false;
    }


    private void renderSeaBattle() {

        // The map tiles
        GameTile[][] tiles = map.getTiles();

        for (int i = 0; i < tiles.length; i++) {
            for(int j = 0; j < tiles[i].length; j++) {

                GameTile tile = tiles[i][j];
                Texture texture = tile.getTexture();

                int x = (i * GameTile.TILE_WIDTH / 2) - (j * GameTile.TILE_WIDTH / 2) -texture.getWidth() / 2;
                int y = (i * GameTile.TILE_HEIGHT / 2) + (j * GameTile.TILE_HEIGHT / 2) -texture.getHeight() / 2;

                batch.draw(texture, x, y);

            }
        }
    }

    public void setTurnExecute() {
        this.currentSlot = 0;
        this.currentPhase = MovePhase.MOVE_TOKEN;
        for (Vessel vessel : context.getEntities().listVesselEntities()) {
            vessel.setMovePhase(null);
        }
    }
}
