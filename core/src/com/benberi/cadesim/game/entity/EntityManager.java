package com.benberi.cadesim.game.entity;

import com.benberi.cadesim.game.entity.vessel.Vessel;

import java.util.ArrayList;
import java.util.List;

public class EntityManager {

    /**
     * List of vessel entities in the game
     */
    private List<Vessel> entities = new ArrayList<Vessel>();

    /**
     * Adds an entity
     * @param entity The enity to add
     */
    public void addEntity(Vessel entity) {
        this.entities.add(entity);
    }

    public List<Vessel> listVesselEntities() {
        return entities;
    }
}
