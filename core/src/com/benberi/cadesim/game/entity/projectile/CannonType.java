package com.benberi.cadesim.game.entity.projectile;

import com.badlogic.gdx.math.Vector2;
import com.benberi.cadesim.game.entity.projectile.impl.LargeCannonball;
import com.benberi.cadesim.game.entity.vessel.Vessel;

public enum CannonType {
    MEDIUM,
    LARGE;

    public CannonBall create(Vessel source, Vector2 target) {
        switch (this) {
            case LARGE:
                return new LargeCannonball(source, target);
        }
        return null;
    }
}
