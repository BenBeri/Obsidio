package com.benberi.cadesim.game.scene.impl.battle.tile.impl;

import com.badlogic.gdx.graphics.Texture;

public class SafeZone extends Cell {

    /**
     * Initializes the tile
     *
     * @param meta The action on step
     */
    public SafeZone(int meta) {
        super(meta);
        setTexture(new Texture("core/assets/sea/safezone.png"));
    }
}
