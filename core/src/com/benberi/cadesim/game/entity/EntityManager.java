package com.benberi.cadesim.game.entity;

import com.benberi.cadesim.GameContext;
import com.benberi.cadesim.game.entity.vessel.Vessel;
import com.benberi.cadesim.game.entity.vessel.impl.WarFrigate;
import com.benberi.cadesim.game.entity.vessel.move.MovePhase;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Containing all kind of entities in the game
 */
public class EntityManager {

    private GameContext context;

    /**
     * List of vessel entities in the game
     */
    private List<Vessel> vessels = new ArrayList<Vessel>();

    public EntityManager(GameContext context) {
        this.context = context;
    }

    /**
     * Adds an entity
     */
    public void addEntity(String name, int x, int y, int face) {
        WarFrigate wf = new WarFrigate(context, name, x, y);
        wf.create();
        wf.setRotationIndex(face);
        vessels.add(wf);
    }

    public Vessel getVessel(int index) {
        return vessels.get(index);
    }

    public Vessel getVesselByPosition(float x, float y) {
        for (Vessel vessel : vessels) {
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
        return vessels;
    }

    public int countVsselsByPhase(MovePhase phase) {
        int count = 0;
        for (Vessel vessel : vessels) {
            if (vessel.getMovePhase() == phase) {
                System.out.println(vessel.getName());
                count++;
            }
        }

        return count;
    }

    public List<Vessel> listCoordinateSortedVessels() {
        List<Vessel> v = new ArrayList<>();
        v.addAll(vessels);
        v.sort((o1, o2) -> {
            int result = Float.compare(o1.getX(), o2.getX());
            if (result == 0) {
                return Float.compare(o1.getY(), o2.getY());
            }
            return result;
        });

        Collections.reverse(v);
        return v;
    }

    /**
     * Gets a vessel by name
     * @param name  The name
     * @return The vessel instance
     */
    public Vessel getVesselByName(String name) {
        for (Vessel vessel : vessels) {
            if (vessel.getName().equalsIgnoreCase(name)) {
                return vessel;
            }
        }

        return null;
    }

    /**
     * Counts vessels
     * @return  The number of vessels
     */
    public int count() {
        return vessels.size();
    }

    public void dispose() {
        vessels.clear();
    }
}
