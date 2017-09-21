package com.benberi.cadesim.game.scene.impl.battle.map.layer;

import com.benberi.cadesim.game.scene.impl.battle.map.GameObject;

import java.util.ArrayList;
import java.util.List;

public class BlockadeMapLayer {

    /**
     * The objects in this layer
     */
    private List<GameObject> objects = new ArrayList<>();

    public void add(GameObject o) {
        objects.add(o);
    }

    public List<GameObject> getObjects() {
        return this.objects;
    }
}
