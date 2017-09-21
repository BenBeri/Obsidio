package com.benberi.cadesim.game.scene.impl.battle.map.tile.impl;

import com.badlogic.gdx.graphics.Texture;
import com.benberi.cadesim.GameContext;
import com.benberi.cadesim.game.scene.impl.battle.map.GameObject;
import com.benberi.cadesim.game.scene.impl.battle.map.tile.GameTile;
import com.benberi.cadesim.util.RandomUtils;

/**
 * A sea cell where ships can freely move on
 */
public class SmallRock extends GameObject {


    /**
     * Initializes the tile
     */
    public SmallRock(GameContext context, int x, int y) {
        super(context);
        set(x, y);
        setTexture(new Texture("core/assets/sea/rocks_small.png"));
        setPackedObjectOrientation("small_rock");
        setOrientation(RandomUtils.randInt(0, 3));
    }


}
