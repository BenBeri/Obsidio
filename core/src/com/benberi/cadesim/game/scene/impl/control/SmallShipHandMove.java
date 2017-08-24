package com.benberi.cadesim.game.scene.impl.control;

import com.benberi.cadesim.game.entity.vessel.move.MoveType;

public class SmallShipHandMove implements HandMove {

    private boolean[] left = new boolean[1];
    private boolean[] right = new boolean[1];
    private MoveType move = MoveType.NONE;

    public void resetLeft() {
        left = new boolean[1];
    }

    public void resetRight() {
        right = new boolean[1];
    }

    public void addLeft() {
        if (!left[0]) {
            left[0] = true;
        }
    }

    public void addRight() {
        if (!right[0]) {
            right[0] = true;
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
