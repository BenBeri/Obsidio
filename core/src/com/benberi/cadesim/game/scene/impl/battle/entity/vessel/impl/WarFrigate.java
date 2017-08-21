package com.benberi.cadesim.game.scene.impl.battle.entity.vessel.impl;


import com.benberi.cadesim.game.scene.impl.battle.entity.vessel.Vessel;

public class WarFrigate extends Vessel {

    public WarFrigate(String name) {
        super(name);
    }

    @Override
    public double getMaxDamage() {
        return 0;
    }

    @Override
    public int getMaxCannons() {
        return 0;
    }

}
