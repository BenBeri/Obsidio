package com.benberi.cadesim.game.entity.projectile;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.benberi.cadesim.game.entity.vessel.Vessel;

public class CannonBall extends Sprite {

    /**
     * The source location
     */
    private Vessel source;

    /**
     * The target location
     */
    private Vector2 target;

    protected CannonBall(Vessel source, Vector2 target, Texture t) {
        super(t);
        this.source = source;
        this.target = target;
    }
}
