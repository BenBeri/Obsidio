package com.benberi.cadesim.game.entity.projectile;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.benberi.cadesim.game.entity.vessel.Vessel;

public class CannonBall extends Sprite {

    public static float VELOCITY = 0.08f;

    /**
     * The source location
     */
    private Vessel source;

    /**
     * The target location
     */
    private Vector2 target;

    private int initalDelay;

    private long start = System.currentTimeMillis();

    private boolean reached;

    protected CannonBall(Vessel source, Vector2 target, Texture t) {
        super(t);
        this.source = source;
        this.target = target;

        setPosition(source.getX(), source.getY());
    }

    public void setDelay(int delay) {
        this.initalDelay = delay;
    }

    public long getStart() {
        return start;
    }

    public int getInitalDelay() {
        return initalDelay;
    }

    public boolean reached() {
        return reached;
    }

    public void move() {
        if (System.currentTimeMillis() - start >= initalDelay) {
            if (source.getX() != target.x) {
                if (source.getX() > target.x) {
                    setX(getX() - VELOCITY);
                    if (getX() <= target.x) {
                        reached = true;
                    }
                } else {
                    setX(getX() + VELOCITY);
                    if (getX() >= target.x) {
                        reached = true;
                    }
                }
            } else if (source.getY() != target.y) {
                setY(getY() + VELOCITY);
            }
        }
    }
}
