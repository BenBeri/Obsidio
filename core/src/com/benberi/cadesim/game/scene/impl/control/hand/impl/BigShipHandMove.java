package com.benberi.cadesim.game.scene.impl.control.hand.impl;

import com.benberi.cadesim.game.entity.vessel.move.MoveType;
import com.benberi.cadesim.game.scene.impl.control.hand.HandMove;

public class BigShipHandMove implements HandMove {

    private boolean[] left = new boolean[2];
    private boolean[] right = new boolean[2];
    private MoveType move = MoveType.NONE;

    public void resetLeft() {
        left = new boolean[2];
    }

    public void resetRight() {
        right = new boolean[2];
    }

    public void addLeft() {
        if (!left[0]) {
            left[0] = true;
        }
        else {
            left[1] = true;
        }
    }

    public void addRight() {
        if (!right[0]) {
            right[0] = true;
        }
        else {
            right[1] = true;
        }
    }
    public void setMove(MoveType move) {
        this.move = move;
    }

    public MoveType getMove() {
        return this.move;
    }

    public boolean[] getLeft() {
        return this.left;
    }

    public boolean[] getRight() {
        return this.right;
    }
}
