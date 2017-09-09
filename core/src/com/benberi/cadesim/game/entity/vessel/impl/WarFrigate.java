package com.benberi.cadesim.game.entity.vessel.impl;

import com.badlogic.gdx.Gdx;
import com.benberi.cadesim.GameContext;
import com.benberi.cadesim.game.entity.vessel.Vessel;
import com.benberi.cadesim.util.PackedShipOrientation;
import com.benberi.cadesim.util.RandomUtils;

public class WarFrigate extends Vessel {

    public WarFrigate(GameContext context, String name, int x, int y, int face) {
        super(context, name, x, y, face);
    }

    @Override
    public void create() {
        try {
            this.setTexture(getContext().getTextures().getVessel("warfrigate"));

            this.setOrientationPack(getContext().getTools().getGson().fromJson(
                    Gdx.files.internal("core/assets/vessel/wf/properties.json").readString(),
                    PackedShipOrientation.class));

            this.setRotationIndex(14);
        } catch (Exception e) {
            e.printStackTrace();
        }
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
