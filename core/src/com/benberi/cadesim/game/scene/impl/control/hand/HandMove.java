package com.benberi.cadesim.game.scene.impl.control.hand;

/**
 * Created by User on 24/08/2017.
 */
public interface HandMove {
    boolean[] getLeft();
    boolean[] getRight();
    void resetLeft();
    void resetRight();
    void addLeft();
    void addRight();
}
