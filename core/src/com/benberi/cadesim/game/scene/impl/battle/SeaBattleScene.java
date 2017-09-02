package com.benberi.cadesim.game.scene.impl.battle;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Bezier;
import com.badlogic.gdx.math.Vector2;
import com.benberi.cadesim.GameContext;
import com.benberi.cadesim.game.entity.vessel.Vessel;
import com.benberi.cadesim.game.entity.vessel.impl.WarFrigate;
import com.benberi.cadesim.game.entity.vessel.move.Move;
import com.benberi.cadesim.game.entity.vessel.move.MoveType;
import com.benberi.cadesim.game.entity.vessel.move.VesselMoveTurn;
import com.benberi.cadesim.game.scene.GameScene;
import com.benberi.cadesim.game.scene.impl.battle.tile.GameTile;

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
     * The battle map
     */
    private SeaMap map;

    /**
     * The camera view of the scene
     */
    private OrthographicCamera camera;

    public SeaBattleScene(GameContext context) {
        this.context = context;
    }

    private Vessel ship;

    /**
     * The sea texture
     */
    private Texture sea;

    /**
     * If the user can drag the map
     */
    private boolean canDragMap;

    @Override
    public void create() {
        this.batch = new SpriteBatch();
        sea = new Texture("core/assets/sea/sea1.png");
        sea.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);
        map = new SeaMap();
        camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight() - 200);
        this.ship = new WarFrigate(context);
        ship.create();
    }

    @Override
    public void update()
    {
        // update the camera
        camera.update();

        /*
         * Update vessels
         */
        for (Vessel vessel : context.getEntities().listVesselEntities()) {

        }

        if (!ship.isMoving() && !context.getProjectileManager().hasProjectiles()) {
            VesselMoveTurn turn = ship.getTurn();
            if (turn != null) {
                Move move = turn.getMove();
                if (move != null && move.getType() != MoveType.NONE) {
                    ship.performMove(move.getType());
                }
            }
        }

        if (ship.isMoving()){

            MoveType move = ship.getCurrentPerformingMove();

            Vector2 start = ship.getAnimation().getStartPoint();
            Vector2 inbetween = ship.getAnimation().getInbetweenPoint();
            Vector2 end = ship.getAnimation().getEndPoint();
            Vector2 current = ship.getAnimation().getCurrentAnimationLocation();

            // calculate step based on progress towards target (0 -> 1)
            // float step = 1 - (ship.getEndPoint().dst(ship.getLinearVector()) / ship.getDistanceToEndPoint());
            if (move != MoveType.FORWARD) {
                ship.getAnimation().addStep(velocityTurns);
                // step on curve (0 -> 1), first bezier point, second bezier point, third bezier point, temporary vector for calculations
                Bezier.quadratic(current, (float) ship.getAnimation().getCurrentStep(), start.cpy(),
                        inbetween.cpy(), end.cpy(), new Vector2());
            }
            else {
                // When ship moving forward, we may not want to use the curve
                int add = move.getIncrementXForRotation(ship.getRotationIndex());
                if (add == -1 || add == 1) {
                    current.x += (velocityForward * (float) add);
                }
                else {
                    add = move.getIncrementYForRotation(ship.getRotationIndex());
                    current.y += (velocityForward * (float) add);
                }
                ship.getAnimation().addStep(velocityForward);
            }

            int result = (int) (ship.getAnimation().getCurrentStep() * 100);

            // check if the step is reached to the end, and dispose the movement
            if (result >= 100) {
                ship.setX(end.x);
                ship.setY(end.y);
                ship.setMoving(false);
                if (move != MoveType.FORWARD)
                    ship.setRotationIndex(ship.getRotationTargetIndex());
                if (ship.getTurn().getMove().hasShoots()) {
                    ship.performShoot();
                }
            }
            else {
                // process move
                ship.setX(current.x);
                ship.setY(current.y);
            }

            // tick rotation of the ship image
            if (result % 25 == 0) {
                ship.tickRotation();
            }
        }
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

            // draw vessel
            batch.draw(ship, x + ship.getOrientationLocation().getOffsetx(), y + ship.getOrientationLocation().getOffsety());
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
    public boolean handleClick(float x, float y) {
        if (y < camera.viewportHeight) {
            this.canDragMap = true;
            return true;
        }
        this.canDragMap = false;
        return false;
    }

    @Override
    public boolean handleClickRelease(float x, float y) {
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
}
