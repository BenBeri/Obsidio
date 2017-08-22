package com.benberi.cadesim.game.entity.vessel;

import com.badlogic.gdx.math.Vector2;

public class VesselAnimationVector {


    /**
     * Last animation update time in milliseconds
     */
    private long lastAnimationUpdate;


    /**
     * The current step on curve
     */
    private double currentStep;

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
     * The current animation location
     */
    private Vector2 currentAnimationLocation;

    public VesselAnimationVector(Vector2 start, Vector2 inbetween, Vector2 end, Vector2 currentAnimationLocation, Vector2 linear) {
        this.start = start;
        this.inbetween = inbetween;
        this.end = end;
        this.currentAnimationLocation = currentAnimationLocation;
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
}
