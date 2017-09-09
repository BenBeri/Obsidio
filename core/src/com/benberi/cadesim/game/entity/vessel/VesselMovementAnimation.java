package com.benberi.cadesim.game.entity.vessel;


import com.benberi.cadesim.game.entity.vessel.move.MoveType;

public enum VesselMovementAnimation {
    NO_ANIMATION(-1),
    TURN_LEFT(0),
    TURN_RIGHT(1),
    MOVE_FORWARD(2),
    MOVE_BACKWARD(3),
    MOVE_LEFT(4),
    MOVE_RIGHT(5),
    TURN_LEFT_BUMP(6),
    TURN_RIGHT_BUMP(7),
    MOVE_FORWARD_BUMP(8);

    private int id;

    VesselMovementAnimation(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public static VesselMovementAnimation forId(int id) {
        for (VesselMovementAnimation v : values()) {
            if (v.getId() == id) {
                return v;
            }
        }
        return null;
    }

    public int getIncrementXForRotation(int rotationIndex) {
        switch(this) {
            case MOVE_FORWARD:
                switch(rotationIndex) {
                    case 2:
                        return 1;
                    case 6:
                        return 0;
                    case 10:
                        return -1;
                    case 14:
                        return 0;
                }
            case TURN_LEFT:
                switch(rotationIndex) {
                    case 2:
                    case 6:
                        return 1;
                    case 10:
                    case 14:
                        return -1;
                }
            case TURN_RIGHT:
                switch(rotationIndex) {
                    case 14:
                    case 2:
                        return 1;
                    case 10:
                    case 6:
                        return -1;
                }
        }
        return 0;
    }

    public int getIncrementYForRotation(int rotationIndex) {
        switch(this) {
            case MOVE_FORWARD:
                switch(rotationIndex) {
                    case 2:
                        return 0;
                    case 6:
                        return -1;
                    case 10:
                        return 0;
                    case 14:
                        return 1;
                }
            case TURN_LEFT:
                switch(rotationIndex) {
                    case 14:
                    case 2:
                        return 1;
                    case 10:
                    case 6:
                        return -1;
                }
            case TURN_RIGHT:
                switch(rotationIndex) {
                    case 10:
                    case 14:
                        return 1;
                    case 6:
                    case 2:
                        return -1;
                }
        }
        return 0;
    }

    public int getRotationTargetIndex(int rotationIndex) {
        int rot = 0;
        switch (this) {
            case TURN_LEFT:
                rot = rotationIndex - 4;
                if (rot < 0) {
                    rot = 14;
                }
                break;
            case TURN_RIGHT:
                rot = rotationIndex + 4;
                if (rot > 15) {
                    rot = 2;
                }
                break;
        }
        return rot;
    }

    public static VesselMovementAnimation getIdForMoveType(MoveType type) {
        switch (type) {
            case LEFT:
                return TURN_LEFT;
            case RIGHT:
                return TURN_RIGHT;
            case NONE:
                return NO_ANIMATION;
            default:
            case FORWARD:
                return MOVE_FORWARD;
        }
    }
}
