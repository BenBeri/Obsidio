package com.benberi.cadesim.game.scene.impl.battle.tile;

import com.badlogic.gdx.graphics.Texture;

/**
 * Abstract design for a game tile
 */
public abstract class GameTile {

    /**
     * Tile dimensions WIDTH
     */
    public static final int TILE_WIDTH = 64;

    /**
     * Tile dimensions HEIGHT
     */
    public static final int TILE_HEIGHT = 48;


    /**
     * The texture of the tile
     */
    private Texture texture;

    /**
     * Meta ID used for actions when reached tile, etc
     */
    private int meta;

    public GameTile(int meta) {
        this.meta = meta;
    }

    /**
     * Get the meta ID
     * @return {@link #meta}
     */
    public int getMeta() {
        return this.meta;
    }

    public void setTexture(Texture texture) {
        this.texture = texture;
    }

    /**
     * Gets the tile texture
     * @return {@link #texture}
     */
    public Texture getTexture() {
        return this.texture;
    }

    /**
     * Checks if an entity can flow into this tile
     * @return <code>true</code> If the entity is allowed to move on, else <code>false</code>
     */
    public abstract boolean canFlowInto();
}
