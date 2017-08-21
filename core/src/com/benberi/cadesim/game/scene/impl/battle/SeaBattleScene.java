package com.benberi.cadesim.game.scene.impl.battle;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.benberi.cadesim.GameContext;
import com.benberi.cadesim.game.scene.GameScene;
import com.benberi.cadesim.game.scene.impl.battle.tile.GameTile;
import com.benberi.cadesim.util.OrientationLocation;
import com.benberi.cadesim.util.PackedShipOrientation;
import com.benberi.cadesim.util.SimpleFileUtils;

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

    private TextureRegion r;
    private OrientationLocation location;

    @Override
    public void create() {
        this.batch = new SpriteBatch();
        map = new SeaMap();
        camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        Texture texture = new Texture("core/assets/vessel/wf/spritesheet.png");

        PackedShipOrientation o = context.getTools().getGson().fromJson(SimpleFileUtils.readStringFromFile("core/assets/vessel/wf/properties.json"), PackedShipOrientation.class);

        location = o.getOrienation(shipRotationIndex);

        r = new TextureRegion(texture, location.getX(), location.getY(), location.getWidth(), location.getHeight());
        local = new Vector2(0,0);
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

    private int shipRotationIndex = 14; // this is a temporary for testing, ship rotation


    private Vector2 local;
    private Vector2 target;

    private void renderEntities() {

        float x = getIsometricX(local.x, local.y, r);
        float y = getIsometricY(local.x, local.y, r);
        batch.draw(r, x+ location.getOffsetx(), y + location.getOffsety());
        local.y += 0.1f;
    }

    private void processShipRotation() {
        shipRotationIndex++;
        if (shipRotationIndex > 15) {
            shipRotationIndex = 0;
        }
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
