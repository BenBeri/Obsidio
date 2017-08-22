package com.benberi.cadesim.game.entity.projectile.impl;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.benberi.cadesim.game.entity.projectile.CannonBall;
import com.benberi.cadesim.game.entity.vessel.Vessel;

public class LargeCannonball extends CannonBall {

    public LargeCannonball(Vessel source, Vector2 target) {
        super(source, target, new Texture("core/assets/projectile/cannonball_large.png"));
    }
}
