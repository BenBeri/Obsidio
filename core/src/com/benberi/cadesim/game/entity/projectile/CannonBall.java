package com.benberi.cadesim.game.entity.projectile;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.benberi.cadesim.game.entity.vessel.Vessel;

public class CannonBall extends Sprite {

    public static float VELOCITY = 3.5f;

    /**
     * The source location
     */
    private Vessel source;

    /**
     * The target location
     */
    private Vector2 target;

    /**
     * If the cannon reached its destination
     */
    private boolean reached;

    /**
     * If the cannon been shoot
     */
    private boolean released = true;

    private TextureRegion splash;

    /**
     * When a vessel shoots two cannons, the second cannon will be contained in the first cannon, so it can
     * time when to shoot it by distance between both
     */
    private CannonBall subcannon;

    private int splashTicks;

    private int regX;

    protected CannonBall(Vessel source, Vector2 target, Texture t, Texture splash) {
        super(t);
        this.source = source;
        this.target = target;
        setPosition(source.getX(), source.getY());
        this.splash = new TextureRegion(splash);
        this.splash.setRegionWidth(40);
        this.splash.setRegionHeight(30);
        this.splash.setRegionX(0);
        this.splash.setRegionY(0);
    }

    public boolean isFinishedSplashing() {
        return splash.getRegionX() >= splash.getTexture().getWidth();
    }

    public void tickSplash() {
        if (splashTicks >= 2) {
            splash.setRegion(regX, 0, 40, 30);
            System.out.println(splash.getRegionX());
            regX += 40;
            splashTicks = 0;
        }
        else {
            splashTicks += 100 * Gdx.graphics.getDeltaTime();
        }
    }

    public void setSubcannon(CannonBall c) {
        this.subcannon = c;
    }

    public TextureRegion getSplash() {
        return splash;
    }

    public CannonBall getSubcannon() {
        return subcannon;
    }

    public boolean isReleased() {
        return released;
    }

    public void setReleased(boolean flag) {
        this.released = flag;
    }

    public boolean reached() {
        return reached;
    }

    public boolean hasSubCannon() {
        return this.subcannon != null;
    }

    public boolean canReleaseSubCannon() {
        Vector2 v1 = new Vector2(getX(), getY());
        Vector2 v2 = new Vector2(subcannon.getX(), subcannon.getY());
        if (v1.dst(v2) >= 0.65) {
            return  true;
        }
        return false;
    }


    public void move() {
        if (source.getX() != target.x) {
            if (source.getX() > target.x) {
                setX(getX() - (VELOCITY * Gdx.graphics.getDeltaTime()) );
                if (getX() <= target.x) {
                    reached = true;
                }
            } else {
                setX(getX() + (VELOCITY * Gdx.graphics.getDeltaTime()));
                if (getX() >= target.x) {
                    reached = true;
                }
            }
        } else if (source.getY() != target.y) {
            setY(getY() + (VELOCITY * Gdx.graphics.getDeltaTime()));
        }
    }
}
