package com.benberi.cadesim.game.entity;

import com.badlogic.gdx.Gdx;
import com.benberi.cadesim.GameContext;
import com.benberi.cadesim.game.entity.vessel.Vessel;
import com.benberi.cadesim.game.entity.vessel.impl.WarFrigate;

import java.util.ArrayList;
import java.util.List;

public class EntityManager {

    private GameContext context;

    /**
     * List of vessel entities in the game
     */
    private List<Vessel> entities = new ArrayList<Vessel>();

    public EntityManager(GameContext context) {
        this.context = context;
    }

    /**
     * Adds an entity
     */
    public void addEntity(String name, int x, int y, int face) {
        WarFrigate wf = new WarFrigate(context, name, x, y, face);
        wf.create();
        entities.add(wf);
    }

    public Vessel getVessel(int index) {
        return entities.get(index);
    }

    public List<Vessel> listVesselEntities() {
        return entities;
    }

    public Vessel getVesselByName(String name) {
        for (Vessel vessel : entities) {
            if (vessel.getName().equalsIgnoreCase(name)) {
                return vessel;
            }
        }

        return null;
    }
}
