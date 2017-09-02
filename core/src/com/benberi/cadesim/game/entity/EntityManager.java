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
        Gdx.app.postRunnable(new Runnable(){
            @Override
            public void run() {
                WarFrigate wf = new WarFrigate(context);
                wf.create();
                entities.add(wf);
            }
        });


    }

    public List<Vessel> listVesselEntities() {
        return entities;
    }
}
