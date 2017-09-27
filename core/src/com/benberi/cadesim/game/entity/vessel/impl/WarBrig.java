package com.benberi.cadesim.game.entity.vessel.impl;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.benberi.cadesim.GameContext;
import com.benberi.cadesim.game.entity.projectile.CannonBall;
import com.benberi.cadesim.game.entity.projectile.impl.LargeCannonball;
import com.benberi.cadesim.game.entity.projectile.impl.MediumCannonball;
import com.benberi.cadesim.game.entity.vessel.Vessel;
import com.benberi.cadesim.game.entity.vessel.VesselMoveType;
import com.benberi.cadesim.util.PackedObjectOrientation;

public class WarBrig extends Vessel {

    public WarBrig(GameContext context, String name, int x, int y) {
        super(context, name, x, y);
    }

    @Override
    public void create() {
        try {
            setDefaultTexture();
            this.shootSmoke = new TextureRegion(getContext().getTextures().getMisc("explode_medium"));
            shootSmoke.setRegion(0,0,40, 30);
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
        return new MediumCannonball(ctx, source, target, getContext().getTextures().getMisc("small_splash"),
                getContext().getTextures().getMisc("hit"));
    }

    @Override
    public VesselMoveType getMoveType() {
        return VesselMoveType.THREE_MOVES;
    }

    @Override
    public void setDefaultTexture() {
        this.setTexture(getContext().getTextures().getVessel("warbrig"));
        this.setOrientationPack(getContext().getTools().getGson().fromJson(
                Gdx.files.internal("core/assets/vessel/wb/properties.json").readString(),
                PackedObjectOrientation.class));
    }

    @Override
    public void setSinkingTexture() {
        this.setTexture(getContext().getTextures().getVessel("warbrig_sinking"));
        this.setOrientationPack(getContext().getTools().getGson().fromJson(
                Gdx.files.internal("core/assets/vessel/wb/sinking.json").readString(),
                PackedObjectOrientation.class));
    }
}
