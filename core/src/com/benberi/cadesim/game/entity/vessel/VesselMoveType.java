package com.benberi.cadesim.game.entity.vessel;

public enum VesselMoveType {
    FOUR_MOVES(45),
    THREE_MOVES(38);

    private int width;

    VesselMoveType(int width) {
        this.width = width;
    }

    public int getBarWidth() {
        return this.width;
    }
}
