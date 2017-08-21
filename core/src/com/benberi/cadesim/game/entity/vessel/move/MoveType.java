package com.benberi.cadesim.game.entity.vessel.move;

/**
 * Represents a move type
 */
public enum MoveType {
    FORWARD,
    LEFT,
    RIGHT,
    NONE;

    public int getIncrementXForRotation(int rotationIndex) {
        switch(this) {
            case FORWARD:
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
            case LEFT:
                switch(rotationIndex) {
                    case 2:
                    case 6:
                        return 1;
                    case 10:
                    case 14:
                        return -1;
                }
            case RIGHT:
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
            case FORWARD:
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
            case LEFT:
                switch(rotationIndex) {
                    case 14:
                    case 2:
                        return 1;
                    case 10:
                    case 6:
                        return -1;
                }
            case RIGHT:
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
            case LEFT:
                rot = rotationIndex - 4;
                if (rot > 15) {
                    rot = 2;
                }
                break;
            case RIGHT:
                rot = rotationIndex + 4;
                if (rot < 0) {
                    rot = 14;
                }
                break;
        }
        return rot;
    }
}
