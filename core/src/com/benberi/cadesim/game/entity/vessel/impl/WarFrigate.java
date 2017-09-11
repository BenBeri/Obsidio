package com.benberi.cadesim.game.entity.vessel.impl;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.benberi.cadesim.GameContext;
import com.benberi.cadesim.game.entity.projectile.CannonBall;
import com.benberi.cadesim.game.entity.projectile.impl.LargeCannonball;
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
    public int getMaxCannons() {
        return 0;
    }

    @Override
    public CannonBall createCannon(GameContext ctx, Vessel source, Vector2 target) {
        return new LargeCannonball(ctx, source, target);
    }
}
