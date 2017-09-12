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
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Bezier;
import com.badlogic.gdx.math.Vector2;
import com.benberi.cadesim.GameContext;
import com.benberi.cadesim.game.entity.projectile.CannonBall;
import com.benberi.cadesim.game.entity.vessel.Vessel;
import com.benberi.cadesim.game.entity.vessel.VesselMoveType;
import com.benberi.cadesim.game.entity.vessel.VesselMovementAnimation;
import com.benberi.cadesim.game.entity.vessel.move.MoveAnimationTurn;
import com.benberi.cadesim.game.entity.vessel.move.MovePhase;
import com.benberi.cadesim.game.scene.GameScene;
import com.benberi.cadesim.game.scene.impl.battle.tile.GameTile;

import java.util.Iterator;
import java.util.List;

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

    /**
     * The sea texture
     */
    private Texture sea;

    private ShapeRenderer renderer;
    /**
     * If the user can drag the map
     */
    private boolean canDragMap;

    private GameInformation information;

    private BitmapFont font;

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

        renderer = new ShapeRenderer();
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
            if (vessel.getMoveDelay() != -1) {
                vessel.tickMoveDelay();
            }
            if (!vessel.isMoving()) {
                if (currentSlot != -1) {
                    MoveAnimationTurn turn = vessel.getStructure().getTurn(currentSlot);
                    if (currentPhase == MovePhase.MOVE_TOKEN && vessel.getMovePhase() == null) {
                        if (turn.getAnimation() != VesselMovementAnimation.NO_ANIMATION && vessel.getMoveDelay() == -1) {
                            vessel.performMove(turn.getAnimation());
                            turn.setAnimation(VesselMovementAnimation.NO_ANIMATION);
                        }
                        else {
                            vessel.setMovePhase(MovePhase.getNext(vessel.getMovePhase()));
                        }
                    }
                    else if (currentPhase == MovePhase.ACTION_MOVE && vessel.getMovePhase() == MovePhase.MOVE_TOKEN && vessel.getMoveDelay() == -1) {
                        if (turn.getSubAnimation() != VesselMovementAnimation.NO_ANIMATION) {
                            vessel.performMove(turn.getSubAnimation());
                            turn.setSubAnimation(VesselMovementAnimation.NO_ANIMATION);
                        }
                        else {
                            vessel.setMovePhase(MovePhase.getNext(vessel.getMovePhase()));
                        }
                    }
                    else if (currentPhase == MovePhase.SHOOT && vessel.getMovePhase() == MovePhase.ACTION_MOVE  && vessel.getMoveDelay() == -1 && vessel.getCannonballs().size() == 0) {
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
            }
            else {
                VesselMovementAnimation move = vessel.getCurrentPerformingMove();

                Vector2 start = vessel.getAnimation().getStartPoint();
                Vector2 inbetween = vessel.getAnimation().getInbetweenPoint();
                Vector2 end = vessel.getAnimation().getEndPoint();
                Vector2 current = vessel.getAnimation().getCurrentAnimationLocation();

                // calculate step based on progress towards target (0 -> 1)
                // float step = 1 - (ship.getEndPoint().dst(ship.getLinearVector()) / ship.getDistanceToEndPoint());


                float velocityTurns = (0.011f * Gdx.graphics.getDeltaTime()) * 100; //Gdx.graphics.getDeltaTime();
                float velocityForward = (0.015f * Gdx.graphics.getDeltaTime()) * 100; //Gdx.graphics.getDeltaTime();

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
                        //current.x += (velocityForward * (float) add);
                    }
                    else {
                        add = move.getIncrementYForRotation(vessel.getRotationIndex());
                       // current.y += (velocityForward * (float) add);
                        current.y += (velocityForward * (float) add);
                    }
                   /// vessel.getAnimation().addStep(velocityForward);
                    vessel.getAnimation().addStep(velocityForward);
                }

                int result = (int) (vessel.getAnimation().getCurrentStep() * 100);
                vessel.getAnimation().tickAnimationTicks(velocityTurns * 100);
                //System.out.println(result);

                // check if the step is reached to the end, and dispose the movement
                if (result >= 100) {
                    vessel.setX(end.x);
                    vessel.setY(end.y);
                    vessel.setMoving(false);

                    if (move != VesselMovementAnimation.MOVE_FORWARD)
                        vessel.setRotationIndex(vessel.getRotationTargetIndex());

                    vessel.setMovePhase(MovePhase.getNext(vessel.getMovePhase()));
                    vessel.setMoveDelay();
                }
                else {
                    // process move
                    vessel.setX(current.x);
                    vessel.setY(current.y);
                }

                System.out.println(vessel.getAnimation().getAnimationTicks());

                // tick rotation of the ship image
                if (vessel.getAnimation().getAnimationTicks() >= 14) {
                    vessel.tickRotation();
                    vessel.getAnimation().resetAnimationTicks();
                }
            }
        }


        for (Vessel vessel : context.getEntities().listVesselEntities()) {
            if (vessel.isSmoking()) {
                vessel.tickSmoke();
            }
            Iterator<CannonBall> itr = vessel.getCannonballs().iterator();
            while (itr.hasNext()) {
                CannonBall c = itr.next();
                if (c.isReleased()) {
                    if (c.hasSubCannon()) {
                        if (c.canReleaseSubCannon()) {
                            c.getSubcannon().setReleased(true);
                        }
                    }
                    if (!c.reached()) {
                        c.move();
                    } else {
                        if (c.finnishedEndingAnimation()) {
                            itr.remove();
                        }
                        else {
                            c.tickEndingAnimation();
                        }
                    }
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

        List<Vessel> sortedVessels = context.getEntities().listVesselEntities();

        /*
         * Render vessels
         */
        for (Vessel vessel : context.getEntities().listCoordinateSortedVessels()) {

            // X position of the vessel
            float x = getIsometricX(vessel.getX(), vessel.getY(), vessel);

            // Y position of the vessel
            float y = getIsometricY(vessel.getX(), vessel.getY(), vessel);

            // draw vessel
            batch.draw(vessel, x + vessel.getOrientationLocation().getOffsetx(), y + vessel.getOrientationLocation().getOffsety());
        }

        /*
         * Render cannon balls
         */
        for (Vessel vessel : sortedVessels) {
            for (CannonBall c : vessel.getCannonballs()) {
                float cx = getIsometricX(c.getX(), c.getY(), c);
                float cy = getIsometricY(c.getX(), c.getY(), c);

                if (!c.reached()) {
                    batch.draw(c, cx, cy);
                }
                else {
                    cx = getIsometricX(c.getX(), c.getY(), c.getEndingAnimationRegion());
                    cy = getIsometricY(c.getX(), c.getY(), c.getEndingAnimationRegion());
                    batch.draw(c.getEndingAnimationRegion(), cx, cy);
                }
            }

            if (vessel.isSmoking()) {
                TextureRegion r = vessel.getShootSmoke();
                float cx = getIsometricX(vessel.getX(), vessel.getY(), r);
                float cy = getIsometricY(vessel.getX(), vessel.getY(), r);
                batch.draw(r, cx, cy);
            }
        }

        batch.end();
        renderer.setProjectionMatrix(camera.combined);

        /*
         * Render name & moves bar
         */
        for (Vessel vessel : sortedVessels) {
            renderer.begin(ShapeRenderer.ShapeType.Line);
            // X position of the vessel
            float x = getIsometricX(vessel.getX(), vessel.getY(), vessel);

            // Y position of the vessel
            float y = getIsometricY(vessel.getX(), vessel.getY(), vessel);

            int width = vessel.getMoveType().getBarWidth();
            renderer.setColor(Color.BLACK);
            renderer.rect(x + (vessel.getRegionWidth() / 2) - (width / 2), Math.round(y + vessel.getRegionHeight() * 1.35f), width, 7);
            renderer.end();
            renderer.begin(ShapeRenderer.ShapeType.Filled);
            renderer.setColor(Color.WHITE);

            int w = (width - 1) / 3;
            int fill = vessel.getNumberOfMoves() > 3 ? 3 : vessel.getNumberOfMoves();

            renderer.rect(x + (vessel.getRegionWidth() / 2) - (width / 2), Math.round(y + vessel.getRegionHeight() * 1.35f) + 1, fill * w, 6);
            renderer.setColor(Color.RED);
            if (vessel.getMoveType() == VesselMoveType.THREE_MOVES && vessel.getNumberOfMoves() > 3) {
                renderer.rect(x + (vessel.getRegionWidth() / 2) - (width / 2) + (width - 1) - 2, Math.round(y + vessel.getRegionHeight() * 1.35f) + 1, 10, 6);
            }
            renderer.end();

            batch.begin();
            GlyphLayout layout = new GlyphLayout(font, vessel.getName());
            font.draw(batch, vessel.getName(), x + (vessel.getRegionWidth() / 2) - (layout.width / 2), y + vessel.getRegionHeight() * 1.7f);
            batch.end();
        }

        batch.begin();
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
