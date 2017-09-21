package com.benberi.cadesim.game.scene.impl.battle.map.tile.impl;

import com.badlogic.gdx.graphics.Texture;
import com.benberi.cadesim.GameContext;
import com.benberi.cadesim.game.scene.impl.battle.map.GameObject;
import com.benberi.cadesim.game.scene.impl.battle.map.tile.GameTile;
import com.benberi.cadesim.util.RandomUtils;

/**
 * A sea cell where ships can freely move on
 */
public class BigRock extends GameObject {


    /**
     * Initializes the tile
     */
    public BigRock(GameContext context, int x, int y) {
        super(context);
        set(x, y);
        setTexture(new Texture("core/assets/sea/rocks_big.png"));
        setPackedObjectOrientation("big_rock");
        setOrientation(RandomUtils.randInt(0, 3));
    }


}
