package com.benberi.cadesim.game.scene;

import com.badlogic.gdx.graphics.Texture;
import com.benberi.cadesim.GameContext;

import javax.xml.soap.Text;
import java.util.HashMap;
import java.util.Map;

public class TextureCollection {

    /**
     * Sea tile textures
     */
    private Map<String, Texture> seaTiles = new HashMap<String, Texture>();

    /**
     * Vessel spritesheet textures
     */
    private Map<String, Texture> vessels = new HashMap<String, Texture>();

    private Map<String, Texture> misc = new HashMap<String, Texture>();

    private GameContext context;

    public TextureCollection(GameContext context) {
        this.context = context;
    }

    /**
     * Creates all textures
     */
    public void create() {
        createSeaTiles();
        createVessels();
        createMisc();
    }

    /**
     * Gets a sea tile
     * @param index The index
     * @return  A texture of the tile
     */
    public Texture getSeaTile(String index) {
        return seaTiles.get(index);
    }

    /**
     * Gets a vessel spritesheet texture
     * @param index The index
     * @return A texture of the vessel spritesheet
     */
    public Texture getVessel(String index) {
        return vessels.get(index);
    }

    /**
     * Gets misc spritesheet texture
     * @param index The index
     * @return A misc texture
     */
    public Texture getMisc(String index) {
        return misc.get(index);
    }

    /**
     * Creates vessel textures
     */
    private void createVessels() {
        vessels.put("warfrigate", new Texture("core/assets/vessel/wf/spritesheet.png"));
    }

    /**
     * Creates sea tiles textures
     */
    private void createSeaTiles() {
        seaTiles.put("cell", new Texture("core/assets/sea/cell.png"));
        seaTiles.put("safe", new Texture("core/assets/sea/safezone.png"));
    }

    private void createMisc() {
        misc.put("large_ball", new Texture("core/assets/projectile/cannonball_large.png"));
    }
}
