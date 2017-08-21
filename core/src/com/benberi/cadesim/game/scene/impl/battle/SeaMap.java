package com.benberi.cadesim.game.scene.impl.battle;

import com.benberi.cadesim.game.scene.impl.battle.tile.GameTile;
import com.benberi.cadesim.game.scene.impl.battle.tile.impl.Cell;
import com.benberi.cadesim.game.scene.impl.battle.tile.impl.SafeZone;

/**
 * Represents a battle arena map
 */
public class SeaMap {

    /**
     * Map dimension-x
     */
    public static final int MAP_WIDTH = 20;

    /**
     * Map dimension-y
     */
    public static final int MAP_HEIGHT = 36;

    /**
     * Map tiles
     */
    private GameTile[][] tiles = new GameTile[MAP_WIDTH][MAP_HEIGHT];

    public SeaMap() {
        Cell cell = new Cell(0);
        SafeZone safe = new SafeZone(0);
        for (int i = 0; i < tiles.length; i++) {
            for(int j = 0; j < tiles[i].length; j++) {
                if (j < 3) {
                    tiles[i][j] = safe;
                }
                else {
                    tiles[i][j] = cell;
                }

            }

        }
    }

    public GameTile[][] getTiles() {
        return this.tiles;
    }
}
