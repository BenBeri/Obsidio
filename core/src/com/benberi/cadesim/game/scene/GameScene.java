package com.benberi.cadesim.game.scene;

/**
 * Represents a game scene view
 */
public interface GameScene {

    /**
     * Create components
     */
    void create();

    /**
     * Update logic
     */
    void update();

    /**
     * Render results
     */
    void render();

    /**
     * Dispose
     */
    void dispose();

    /**
     * Handles mouse drag input
     * @param x drag-x
     * @param y drag-y
     */
    void handleDrag(float x, float y);

    /**
     * Handles click on screen
     * @param x click-x
     * @param y click-y
     */
    void handleClick(float x, float y);

}
