package com.benberi.cadesim.game.entity.vessel;

import com.badlogic.gdx.math.Vector2;
import com.benberi.cadesim.GameContext;
import com.benberi.cadesim.game.entity.Entity;
import com.benberi.cadesim.game.entity.projectile.CannonBall;
import com.benberi.cadesim.game.entity.vessel.move.MoveAnimationStructure;
import com.benberi.cadesim.game.entity.vessel.move.MovePhase;
import com.benberi.cadesim.game.entity.vessel.move.MoveType;
import com.benberi.cadesim.game.entity.vessel.move.VesselMoveTurn;
import com.benberi.cadesim.util.OrientationLocation;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * Represents a vessel abstraction
 */
public abstract class Vessel extends Entity {

    /**
     * The name of this vessel player
     */
    private String name;

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
     * The turn animation structure
     */
    private MoveAnimationStructure structure = new MoveAnimationStructure();

    /**
     * The current turn
     */
    private VesselMoveTurn turn;

    /**
     * The last finished phase
     */
    private MovePhase finishedPhase;

    private boolean performingLeftShoot;
    private boolean performingRightShoot;

    /**
     * The cannon balls that were shoot
     */
    private List<CannonBall> cannonballs = new ArrayList<CannonBall>();

    public Vessel(GameContext context, String name, int x, int y, int face) {
        super(context);
        this.name = name;
        this.setPosition(x, y);
        this.rotationIndex = face;
        turn = new VesselMoveTurn();
    }

    public void setMovePhase(MovePhase phase) {
        this.finishedPhase = phase;
    }

    public MovePhase getMovePhase() {
        return finishedPhase;
    }

    public MoveAnimationStructure getStructure() {
        return structure;
    }

    /**
     * Starts to perform a given move
     * @param move The move to perform
     */
    public void performMove(VesselMovementAnimation move) {
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


    public String getName() {
        return this.name;
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

    private Vector2 getClosestLeftCannonCollide() {
        switch (rotationIndex) {
            case 2:
                break;
            case 6:
                for (int i = 1; i < 4; i++) {
                    Vessel vessel = getContext().getEntities().getVesselByPosition(getX() + i, getY());
                    if (vessel != null) {
                        return new Vector2(getX() + i, getY());
                    }
                }
                return new Vector2(getX() + 3, getY());
            case 10:
                break;
            case 14:
                for (int i = 1; i < 4; i++) {
                    Vessel vessel = getContext().getEntities().getVesselByPosition(getX() - i, getY());
                    if (vessel != null) {
                        return new Vector2(getX() - i, getY());
                    }
                }
                return new Vector2(getX() - 3, getY());
        }
        return null;
    }

    /**
     * Maximum amount of cannons
     */
    public abstract int getMaxCannons();

    public abstract CannonBall createCannon(GameContext ctx, Vessel source, Vector2 target);

    public List<CannonBall> getCannonballs() {
        return this.cannonballs;
    }

    public void performLeftShoot(int leftShoots) {
        if (leftShoots == 1) {
            Vector2 target = getClosestLeftCannonCollide();
            CannonBall ball = createCannon(getContext(), this, target);
            cannonballs.add(ball);
        }
        else if (leftShoots == 2) {
            Vector2 target = getClosestLeftCannonCollide();

            CannonBall ball1 = createCannon(getContext(), this, target);
            cannonballs.add(ball1);

            CannonBall ball2 = createCannon(getContext(), this, target);
            ball2.setDelay(150);
            cannonballs.add(ball2);
        }
    }

    public void performRightShoot(int leftShoots) {

    }
}
