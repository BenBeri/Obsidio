package com.benberi.cadesim.game.entity.vessel;

import com.badlogic.gdx.math.Vector2;
import com.benberi.cadesim.GameContext;
import com.benberi.cadesim.game.entity.Entity;
import com.benberi.cadesim.game.entity.vessel.move.MoveType;
import com.benberi.cadesim.util.OrientationLocation;

/**
 * Represents a vessel abstraction
 */
public abstract class Vessel extends Entity {

    public static final int ROTATION_TICK_DELAY = 200;

    /**
     * The damage of this vessel
     */
    private double damage;

    /**
     * The maximum bilge
     */
    private int bilge;

    /**
     * The amount of filled cannons
     */
    private int filledCannons;

    /**
     * If the vessel is moving
     */
    private boolean isMoving;

    /**
     * Animation start position
     */
    private Vector2 start;

    /**
     * Animation inbetween position (only for left/right movements)
     */
    private Vector2 inbetween;

    /**
     * Animation ending position
     */
    private Vector2 end;

    /**
     * Linear vector for bezier curve
     */
    private Vector2 linear;

    /**
     * The current animation location
     */
    private Vector2 currentAnimationLocation;

    /**
     * The distance to target positon
     */
    private float distanceToEnd;

    /**
     * The rotation index of the vessel
     */
    private int rotationIndex;

    /**
     * The target index for the rotation in animation
     */
    private int rotationTargetIndex = -1;

    /**
     * Last animation update time in milliseconds
     */
    private long lastAnimationUpdate;

    /**
     * Current performing move
     */
    private MoveType currentPerformingMove;

    /**
     * The current step on curve
     */
    private double currentStep;

    public Vessel(GameContext context) {
        super(context);
    }

    /**
     * Starts to perform a given move
     * @param move The move to perform
     */
    public void performMove(MoveType move) {
        start = new Vector2(this.getX(), this.getY());
        currentAnimationLocation = start.cpy();
        this.currentPerformingMove = move;

        currentStep = 0;

        if (move == MoveType.FORWARD) {
            inbetween = null;
        }
        else {
            // Get the inbetween block by using forward
            this.inbetween = new Vector2(start.x + MoveType.FORWARD.getIncrementXForRotation(rotationIndex),
                    start.y + MoveType.FORWARD.getIncrementYForRotation(rotationIndex));
            System.out.println("currrent: " + getX() + " " + getY());
            System.out.println("rotation: " + rotationIndex + " inbetween: " + inbetween.x + " " + inbetween.y);
            this.rotationTargetIndex = move.getRotationTargetIndex(rotationIndex);
            System.out.println("Target set: " + rotationTargetIndex + " from " + rotationIndex);
        }

        this.end = new Vector2(start.x + move.getIncrementXForRotation(rotationIndex),
                start.y + move.getIncrementYForRotation(rotationIndex));

        System.out.println("END POSITION: " + end.x  + " " + end.y);
        this.linear = start.cpy();
        this.distanceToEnd = start.dst(end);
        this.lastAnimationUpdate = 0;
    }

    public MoveType getCurrentPerformingMove() {
        return this.currentPerformingMove;
    }

    public Vector2 getCurrentAnimationLocation() {
        return this.currentAnimationLocation;
    }

    public double getCurrentStep() {
        return this.currentStep;
    }

    public void addStep(double step) {
        this.currentStep += step;
    }

    /**
     * @return The target rotation index for animation
     */
    public int getRotationTargetIndex() {
        return this.rotationTargetIndex;
    }

    /**
     * Gets the damage of the ship
     * @return The damage of the hip {@link #damage}
     */
    public double getDamage() {
        return damage;
    }

    /**
     * Gets the bilge of the ship
     * @return {@link #bilge}
     */
    public int getBilge() {
        return bilge;
    }

    /**
     * Gets the amount of filled cannons in the ship
     * @return {@link #filledCannons}
     */
    public int getFilledCannons() {
        return filledCannons;
    }

    /**
     * Gets the last animation time
     * @return {@link #lastAnimationUpdate}
     */
    public long getLastAnimationUpdate() {
        return this.lastAnimationUpdate;
    }

    /**
     * If the ship currently performing move animation or not
     * @param flag If moving or not
     */
    public void setMoving(boolean flag) {
        this.isMoving = flag;
    }

    /**
     * If the ship is moving or not
     * @return TRUE if moving FALSE if not
     */
    public boolean isMoving() {
        return this.isMoving;
    }

    /**
     * Gets the starting point of the move
     * @return {@link #start}
     */
    public Vector2 getStartPoint() {
        return this.start;
    }

    /**
     * Gets the ending target point of the move
     * @return {@link #end}
     */
    public Vector2 getEndPoint() {
        return this.end;
    }

    /**
     * Gets the in-between point of the move
     * @return {@link #inbetween}
     */
    public Vector2 getInbetweenPoint() {
        return this.inbetween;
    }

    /**
     * Gets the linear vector using to calculate position move in the curve
     * @return {@link #linear}
     */
    public Vector2 getLinearVector() {
        return this.linear;
    }

    public int getRotationIndex() {
        return this.rotationIndex;
    }

    /**
     * Ticks up to next rotation
     */
    public void tickRotation() {
        System.out.println(rotationIndex + " " + rotationTargetIndex);
        if (rotationIndex == rotationTargetIndex) {
            return;
        }
        if (currentPerformingMove == MoveType.LEFT) {
            this.rotationIndex--;
        }
        else {
            this.rotationIndex++;
        }

        if (rotationIndex > 15) {
            rotationIndex = 0;
        }
        else if (rotationIndex < 0) {
            rotationIndex = 14;
        }

        this.updateRotation();
        this.lastAnimationUpdate = System.currentTimeMillis();
    }

    /**
     * Sets rotation index
     * @param index The new index
     */
    public void setRotationIndex(int index) {
        this.rotationIndex = index;
        this.updateRotation();
    }

    /**
     * Updates sprite region to new rotation
     */
    private void updateRotation() {
        this.setOrientationLocation(this.rotationIndex);
        OrientationLocation location = this.getOrientationLocation();
        try {
            this.setRegion(location.getX(), location.getY(), location.getWidth(), location.getHeight());
        }
        catch(NullPointerException e) {
            System.err.println(rotationIndex + " " + rotationTargetIndex);
        }
    }

    /**
     * Maximum amount of damage to sink
     */
    public abstract float getMaxDamage();

    /**
     * Maximum amount of bilge
     */
    public abstract int getMaxBilge();

    /**
     * Maximum amount of cannons
     */
    public abstract int getMaxCannons();
}
