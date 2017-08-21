package com.benberi.cadesim.game.scene.impl.battle.entity;

import com.badlogic.gdx.graphics.g2d.Sprite;

/**
 * Entity stands for anything that can be on the sea battle board cell tiles (vessels, rocks, etc)
 */
public abstract class Entity {

    /**
     * The name of the entity
     */
    private String name;

    /**
     * The sprite of the entity
     */
    private Sprite sprite;

    /**
     * The x-location of the entity
     */
    private int x;

    /**
     * The y-location of the entity
     */
    private int y;

    public Entity(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Sprite getSprite() {
        return sprite;
    }

    public void setSprite(Sprite sprite) {
        this.sprite = sprite;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }
}
