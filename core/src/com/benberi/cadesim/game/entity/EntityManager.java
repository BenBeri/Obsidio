package com.benberi.cadesim.game.entity;

import com.badlogic.gdx.Gdx;
import com.benberi.cadesim.GameContext;
import com.benberi.cadesim.game.entity.vessel.Vessel;
import com.benberi.cadesim.game.entity.vessel.impl.WarFrigate;
import com.benberi.cadesim.game.entity.vessel.move.MovePhase;

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

    public Vessel getVesselByPosition(float x, float y) {
        for (Vessel vessel : entities) {
            if (vessel.getX() == x && vessel.getY() == y) {
                return vessel;
            }
        }
        return null;
    }

    /**
     * Lists all vessels
     * @return  The list of vessels
     */
    public List<Vessel> listVesselEntities() {
        return entities;
    }

    public int countVsselsByPhase(MovePhase phase) {
        int count = 0;
        for (Vessel vessel : entities) {
            if (vessel.getMovePhase() == phase) {
                System.out.println(vessel.getName());
                count++;
            }
        }

        return count;
    }

    /**
     * Gets a vessel by name
     * @param name  The name
     * @return The vessel instance
     */
    public Vessel getVesselByName(String name) {
        for (Vessel vessel : entities) {
            if (vessel.getName().equalsIgnoreCase(name)) {
                return vessel;
            }
        }

        return null;
    }

    public int count() {
        return entities.size();
    }
}
