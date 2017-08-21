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
import com.benberi.cadesim.game.entity.vessel.move.MoveType;
import com.benberi.cadesim.game.scene.GameScene;
import com.benberi.cadesim.game.scene.impl.battle.tile.GameTile;

import java.util.Random;

public class SeaBattleScene implements GameScene {

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

    @Override
    public void create() {
        this.batch = new SpriteBatch();
        map = new SeaMap();
        camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        this.ship = new WarFrigate(context);
        ship.create();
    }

    @Override
    public void update() {
        camera.update();
    }

    @Override
    public void render() {
        batch.setProjectionMatrix(camera.combined);
        batch.begin();

        // Render the map
        renderSeaBattle();

        // Render ships
        renderEntities();

        batch.end();
    }

    @Override
    public void dispose() {

    }

    @Override
    public void handleDrag(float x, float y) {
        camera.translate(-x, y);
    }

    @Override
    public void handleClick(float x, float y) {
    }

    boolean m;
    boolean s;

    float step;

    private void renderEntities() {

//        if (move) {
//            Vector2 dir = target.cpy().sub(linear).nor();
//            linear.add(dir.scl(2 * Gdx.graphics.getDeltaTime()));
//            float step = 1 - (target.dst(linear) / distance);
//            System.out.println(step);
//            Bezier.quadratic(local, step, start.cpy(), middle.cpy(), target.cpy(), new Vector2());
//
//            if (step > 0.99f) {
//                local.x = 1;
//                local.y = 1;
//                move = false;
//            }
//        }
//


        if (!m) {
            Random r = new Random();
            //ship.performMove(MoveType.values()[r.nextInt(MoveType.values().length - 1)]);
            ship.performMove(MoveType.RIGHT);
            ship.setMoving(true);
            m=true;
            step = 0;
        }

        if (ship.isMoving()){
            // Normalized direction vector towards target
            Vector2 dir = ship.getEndPoint().cpy().sub(ship.getLinearVector()).nor();

            // Move towards target by adding direction vector multiplied by speed and delta time to linearVector
            ship.getLinearVector().add(dir.scl(2 * Gdx.graphics.getDeltaTime()));

            // calculate step based on progress towards target (0 -> 1)
           // float step = 1 - (ship.getEndPoint().dst(ship.getLinearVector()) / ship.getDistanceToEndPoint());
            step += 0.1f;
            if (ship.getCurrentPerformingMove() != MoveType.FORWARD) {
                // step on curve (0 -> 1), first bezier point, second bezier point, third bezier point, temporary vector for calculations
                Bezier.quadratic(ship.getCurrentAnimationLocation(), step, ship.getStartPoint().cpy(),
                        ship.getInbetweenPoint().cpy(), ship.getEndPoint().cpy(), new Vector2());
            }
            else {
                Bezier.quadratic(ship.getCurrentAnimationLocation(), step, ship.getStartPoint().cpy(),
                        new Vector2(ship.getStartPoint().x, ship.getEndPoint().y), ship.getEndPoint().cpy(), new Vector2());
            }

            // check if the step is reached to the end, and dispose the movement
            if (step == 1) {
                ship.setX(ship.getEndPoint().x);
                ship.setY(ship.getEndPoint().y);
                ship.setMoving(false);
                System.out.println("ENDED MOVE AT  "+ ship.getX() + " " + ship.getY());
            }
            else {
                // process move
                ship.setX(ship.getCurrentAnimationLocation().x);
                ship.setY(ship.getCurrentAnimationLocation().y);
            }

            // tick rotation of the ship image
            if (System.currentTimeMillis() - ship.getLastAnimationUpdate() >= Vessel.ROTATION_TICK_DELAY) {
                ship.tickRotation();
            }
        }

        float x = getIsometricX(ship.getX(), ship.getY(), ship);
        float y = getIsometricY(ship.getX(), ship.getY(), ship);
        batch.draw(ship, x + ship.getOrientationLocation().getOffsetx(), y + ship.getOrientationLocation().getOffsety());
    }


    public float getIsometricX(float x, float y, TextureRegion region) {
        return (x * GameTile.TILE_WIDTH / 2) - (y * GameTile.TILE_WIDTH / 2) - (region.getRegionWidth() / 2);
    }

    public float getIsometricY(float x, float y, TextureRegion region) {
        return (x * GameTile.TILE_HEIGHT / 2) + (y * GameTile.TILE_HEIGHT / 2) - (region.getRegionHeight() / 2);
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
