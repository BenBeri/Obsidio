package com.benberi.cadesim.game.entity.projectile;

import com.badlogic.gdx.math.Vector2;
import com.benberi.cadesim.game.entity.vessel.Vessel;

import java.util.ArrayList;
import java.util.List;

public class ProjectileManager {

    /**
     * The cannon balls
     */
    private List<CannonBall> projectiles = new ArrayList<CannonBall>();

    public void fireProjectile(Vessel source, Vector2 target, CannonType type) {
        projectiles.add(type.create(source, target));
    }

    public boolean hasProjectiles() {
        return !projectiles.isEmpty();
    }

}
