package com.benberi.cadesim.game.entity.vessel;


import com.badlogic.gdx.math.Vector2;
import com.benberi.cadesim.game.entity.vessel.move.MoveType;
import com.benberi.cadesim.util.Face;

public enum VesselMovementAnimation {
    NO_ANIMATION(-1),
    TURN_LEFT(0),
    TURN_RIGHT(1),
    MOVE_FORWARD(2),

    MOVE_BACKWARD(3),
    MOVE_LEFT(4),
    MOVE_RIGHT(5),
    BUMP_PHASE_1(6),
    BUMP_PHASE_2(7);

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
                    case Face.EAST:
                        return 1;
                    case Face.SOUTH:
                        return 0;
                    case Face.WEST:
                        return -1;
                    case Face.NORTH:
                        return 0;
                }
            case MOVE_BACKWARD:
                switch(rotationIndex) {
                    case Face.EAST:
                        return -1;
                    case Face.SOUTH:
                        return 0;
                    case Face.WEST:
                        return 1;
                    case Face.NORTH:
                        return 0;
                }
            case MOVE_LEFT:
                switch (rotationIndex) {
                    case Face.EAST:
                        return 0;
                    case Face.SOUTH:
                        return 1;
                    case Face.WEST:
                        return 0;
                    case Face.NORTH:
                        return -1;
                }
            case MOVE_RIGHT:
                switch (rotationIndex) {
                    case Face.EAST:
                        return 0;
                    case Face.SOUTH:
                        return -1;
                    case Face.WEST:
                        return 0;
                    case Face.NORTH:
                        return 1;
                }
            case TURN_LEFT:
                switch(rotationIndex) {
                    case Face.EAST:
                    case Face.SOUTH:
                        return 1;
                    case Face.WEST:
                    case Face.NORTH:
                        return -1;
                }

            case TURN_RIGHT:
                switch(rotationIndex) {
                    case Face.NORTH:
                    case Face.EAST:
                        return 1;
                    case Face.WEST:
                    case Face.SOUTH:
                        return -1;
                }
        }
        return 0;
    }

    public int getIncrementYForRotation(int rotationIndex) {
        switch(this) {
            case MOVE_FORWARD:
                switch(rotationIndex) {
                    case Face.EAST:
                        return 0;
                    case Face.SOUTH:
                        return -1;
                    case Face.WEST:
                        return 0;
                    case Face.NORTH:
                        return 1;
                }
            case MOVE_BACKWARD:
                switch(rotationIndex) {
                    case Face.EAST:
                        return 0;
                    case Face.SOUTH:
                        return 1;
                    case Face.WEST:
                        return 0;
                    case Face.NORTH:
                        return -1;
                }
            case MOVE_LEFT:
                switch (rotationIndex) {
                    case Face.EAST:
                        return 1;
                    case Face.SOUTH:
                        return 0;
                    case Face.WEST:
                        return -1;
                    case Face.NORTH:
                        return 0;
                }
            case MOVE_RIGHT:
                switch (rotationIndex) {
                    case Face.EAST:
                        return -1;
                    case Face.SOUTH:
                        return 0;
                    case Face.WEST:
                        return 1;
                    case Face.NORTH:
                        return 0;
                }
            case TURN_LEFT:
                switch(rotationIndex) {
                    case Face.NORTH:
                    case Face.EAST:
                        return 1;
                    case Face.WEST:
                    case Face.SOUTH:
                        return -1;
                }
            case TURN_RIGHT:
                switch(rotationIndex) {
                    case Face.WEST:
                    case Face.NORTH:
                        return 1;
                    case Face.SOUTH:
                    case Face.EAST:
                        return -1;
                }
        }
        return 0;
    }

    public boolean isOneDimensionMove() {
        return this == MOVE_FORWARD || this == MOVE_BACKWARD || this == MOVE_LEFT || this == MOVE_RIGHT;
    }

    public Vector2 getBumpTargetPosition(int rotationIndex) {
        switch (this) {
            case BUMP_PHASE_1:
                switch (rotationIndex) {
                    case Face.NORTH:
                        return new Vector2(0, 0.2f);
                    case Face.SOUTH:
                        return new Vector2(0, -0.2f);
                    case Face.EAST:
                        return new Vector2(0.2f, 0);
                    case Face.WEST:
                        return new Vector2(-0.2f, 0);
                }
            case BUMP_PHASE_2:
                return new Vector2(MOVE_FORWARD.getIncrementXForRotation(rotationIndex), MOVE_FORWARD.getIncrementYForRotation(rotationIndex));
        }

        return null;
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

    public static boolean isBump(VesselMovementAnimation animation) {
        return animation == BUMP_PHASE_1 || animation == BUMP_PHASE_2;
    }
}
