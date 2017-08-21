package com.benberi.cadesim.game.entity.vessel.move;

import com.benberi.cadesim.game.entity.projectile.CannonBall;

public class Move {

    /**
     * The move type
     */
    private MoveType type;

    /**
     * Left cannon ball shoots
     */
    private CannonBall[] left = new CannonBall[2];

    /**
     * Right cannon ball shots
     */
    private CannonBall[] right = new CannonBall[2];

    public void setMoveType(MoveType type) {
        this.type = type;
    }

    public void addLeftCannon() {
        if (left[0] == null) {
            left[0] = new CannonBall(); // todo pass cannon factory based on ship size
        }
        else {
            left[1] = new CannonBall();
        }
    }

    public void addRightCannon() {
        if (right[0] == null) {
             right[0] = new CannonBall(); // todo pass cannon factory based on ship size
        }
        else {
            right[1] = new CannonBall();
        }
    }

    public MoveType getType() {
        return type;
    }

    public CannonBall[] getLeft() {
        return left;
    }

    public CannonBall[] getRight() {
        return right;
    }
}
