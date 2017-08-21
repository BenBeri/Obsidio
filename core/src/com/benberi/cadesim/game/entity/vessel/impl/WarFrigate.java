package com.benberi.cadesim.game.entity.vessel.impl;

import com.badlogic.gdx.graphics.Texture;
import com.benberi.cadesim.GameContext;
import com.benberi.cadesim.game.entity.vessel.Vessel;
import com.benberi.cadesim.util.PackedShipOrientation;
import com.benberi.cadesim.util.SimpleFileUtils;

public class WarFrigate extends Vessel {

    public WarFrigate(GameContext context) {
        super(context);
    }

    @Override
    public void create() {
        this.setTexture(new Texture("core/assets/vessel/wf/spritesheet.png"));
        this.setOrientationPack(getContext().getTools().getGson().fromJson(
                SimpleFileUtils.readStringFromFile("core/assets/vessel/wf/properties.json"),
                PackedShipOrientation.class));
        this.setRotationIndex(14);
    }

    @Override
    public float getMaxDamage() {
        return 0;
    }

    @Override
    public int getMaxBilge() {
        return 0;
    }

    @Override
    public int getMaxCannons() {
        return 0;
    }
}
