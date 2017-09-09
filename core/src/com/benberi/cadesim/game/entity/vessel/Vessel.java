package com.benberi.cadesim.game.entity.vessel;

import com.badlogic.gdx.math.Vector2;
import com.benberi.cadesim.GameContext;
import com.benberi.cadesim.game.entity.Entity;
import com.benberi.cadesim.game.entity.vessel.move.MoveType;
import com.benberi.cadesim.game.entity.vessel.move.VesselMoveTurn;
import com.benberi.cadesim.util.OrientationLocation;

import java.util.LinkedList;
import java.util.Queue;

/**
 * Represents a vessel abstraction
 */
public abstract class Vessel extends Entity {

    public static final int ROTATION_TICK_DELAY = 200;

    /**
     * The name of this vessel player
     */
    private String name;

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
     * The rotation index of the vessel
     */
    private int rotationIndex;

    /**
     * The target index for the rotation in animation
     */
    private int rotationTargetIndex = -1;

    /**
     * The animation handler
     */
    private VesselAnimationVector animation;

    /**
     * Current performing move
     */
    private VesselMovementAnimation currentPerformingMove;

    /**
     * The current turn
     */
    private VesselMoveTurn turn;

    /**
     * If the vessel is performing shoot
     */
    private boolean isPerformingShoot;

    private Queue<VesselMovementAnimation> animations = new LinkedList<VesselMovementAnimation>();

    public Vessel(GameContext context, String name, int x, int y, int face) {
        super(context);
        this.name = name;
        this.setPosition(x, y);
        this.rotationIndex = face;
        turn = new VesselMoveTurn();
    }

    /**
     * Starts to perform a given move
     * @param move The move to perform
     */
    public void performMove(VesselMovementAnimation move) {
        if (move == VesselMovementAnimation.NO_ANIMATION) {
            animations.poll();
            return;
        }
        Vector2 start = new Vector2(this.getX(), this.getY());
        Vector2 currentAnimationLocation = start.cpy();
        this.currentPerformingMove = move;

        Vector2 inbetween = null;
        if (move != VesselMovementAnimation.MOVE_FORWARD) {
            // Get the inbetween block by using forward
            inbetween = new Vector2(start.x + MoveType.FORWARD.getIncrementXForRotation(rotationIndex),
                    start.y + MoveType.FORWARD.getIncrementYForRotation(rotationIndex));
            this.rotationTargetIndex = move.getRotationTargetIndex(rotationIndex);
        }

        Vector2 end = new Vector2(start.x + move.getIncrementXForRotation(rotationIndex),
                start.y + move.getIncrementYForRotation(rotationIndex));

        Vector2 linear = start.cpy();
        this.animation = new VesselAnimationVector(start, inbetween, end, currentAnimationLocation, linear);
        setMoving(true);
    }

    public Queue<VesselMovementAnimation> getAnimationsQueue() {
        return animations;
    }

    public String getName() {
        return this.name;
    }

    public VesselMoveTurn getTurn() {
        return this.turn;
    }

    /**
     * Gets the animation handler for vessel
     * @return {@link #animation}
     */
    public VesselAnimationVector getAnimation() {
        return this.animation;
    }

    /**
     * Gets the current performing move
     * @return {@link #currentPerformingMove}
     */
    public VesselMovementAnimation getCurrentPerformingMove() {
        return this.currentPerformingMove;
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
     * If the ship currently performing move animation or not
     * @param flag If moving or not
     */
    public void setMoving(boolean flag) {
        this.isMoving = flag;
    }

    public boolean isPerformingShoot() {
        return this.isPerformingShoot;
    }

    public void setPerformingShoot(boolean flag) {
        this.isPerformingShoot = flag;
    }

    /**
     * If the ship is moving or not
     * @return TRUE if moving FALSE if not
     */
    public boolean isMoving() {
        return this.isMoving;
    }

    /**
     * Gets the current rotation index
     * @return {{@link #rotationIndex}}
     */
    public int getRotationIndex() {
        return this.rotationIndex;
    }

    /**
     * Ticks up to next rotation
     */
    public void tickRotation() {
        if (rotationIndex == rotationTargetIndex) {
            return;
        }
        if (currentPerformingMove == VesselMovementAnimation.TURN_LEFT) {
            this.rotationIndex--;
        }
        else if (currentPerformingMove == VesselMovementAnimation.TURN_RIGHT) {
            this.rotationIndex++;
        }

        if (rotationIndex > 15) {
            rotationIndex = 0;
        }
        else if (rotationIndex < 0) {
            rotationIndex = 14;
        }

        this.updateRotation();
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

    public void performShoot() {

        //getContext().getProjectileManager().fireProjectile(this, );
    }
}
