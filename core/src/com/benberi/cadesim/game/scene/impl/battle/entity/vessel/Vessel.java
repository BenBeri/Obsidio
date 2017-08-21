package com.benberi.cadesim.game.scene.impl.battle.entity.vessel;

import com.benberi.cadesim.game.scene.impl.battle.entity.Entity;

public abstract class Vessel extends Entity {

    /**
     * The current damage of the ship, between 0 and {@link #getMaxDamage()}
     */
    private double damage;

    /**
     * The current bilge of the ship
     */
    private int bilge;

    /**
     * The amount of filled guns, between 0 and {@link #getMaxCannons()}
     */
    private int filledGuns;

    public Vessel(String name) {
        super(name);
    }

    /**
     * The maximum damage of the ship
     *
     * @return The max damage
     */
    public abstract double getMaxDamage();

    /**
     * The maximum amount of guns in the ship
     *
     * @return Number of guns
     */
    public abstract int getMaxCannons();

}
