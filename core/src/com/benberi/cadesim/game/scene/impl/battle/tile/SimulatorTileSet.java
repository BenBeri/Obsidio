package com.benberi.cadesim.game.scene.impl.battle.tile;

import com.benberi.cadesim.game.scene.impl.battle.tile.impl.Cell;

public enum SimulatorTileSet {

    /**
     * This is the regular sea battle cell we have
     */
    CELL(new Cell(-1)),

    CELL_SAFE(new Cell(-1));

    private GameTile tile;

    SimulatorTileSet(GameTile tile) {
        this.tile = tile;
    }

}
