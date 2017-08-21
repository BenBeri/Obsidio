package com.benberi.cadesim.game.scene.impl.battle.tile.impl;

import com.badlogic.gdx.graphics.Texture;
import com.benberi.cadesim.game.scene.impl.battle.tile.GameTile;

/**
 * A sea cell where ships can freely move on
 */
public class Cell extends GameTile {


    /**
     * Initializes the tile
     */
    public Cell(int meta) {
        super(meta);
        setTexture(new Texture("core/assets/sea/cell.png"));
    }

    @Override
    public boolean canFlowInto() {
        return true;
    }

}
