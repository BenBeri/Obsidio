package com.benberi.cadesim.game.scene.impl.control;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.benberi.cadesim.GameContext;
import com.benberi.cadesim.game.scene.SceneComponent;

public class BattleControlComponent extends SceneComponent<ControlAreaScene> {

    /**
     * Left moves
     */
    private int leftMoves;

    /**
     * Right moves
     */
    private int rightMoves;

    /**
     * Forward moves
     */
    private int forwardMoves;

    /**
     * The available shoots
     */
    private int shoots;

    private Texture shiphand;

    private TextureRegion leftMoveTexture;
    private TextureRegion rightMoveTexture;
    private TextureRegion forwardMoveTexture;
    private TextureRegion manuaverTexture; // for ships that only are 3 moves

    private TextureRegion emptyLeftMoveTexture;
    private TextureRegion emptyRightMoveTexture;
    private TextureRegion emptyForwardMoveTexture;

    private TextureRegion sandBottom;
    private TextureRegion sandTop;
    private Texture sandTrickle;

    private Texture hourGlass;

    private TextureRegion cannon;
    private TextureRegion emptyCannon;


    protected BattleControlComponent(GameContext context, ControlAreaScene owner) {
        super(context, owner);
    }

    @Override
    public void create() {
        shiphand = new Texture("core/assets/ui/shiphand.png");
        hourGlass = new Texture("core/assets/ui/hourglass.png");
    }

    @Override
    public void update() {

    }

    @Override
    public void render() {

    }

    public void updateLeftMoves(boolean inc) {
        if (inc) {
            leftMoves++;
        }
        else {
            leftMoves--;
        }
    }

    public void updateRightMoves(boolean inc) {
        if (inc) {
            rightMoves++;
        }
        else {
            rightMoves--;
        }
    }

    public void updateForwardMoves(boolean inc) {
        if (inc) {
            forwardMoves++;
        }
        else {
            forwardMoves--;
        }
    }

    public void updateShoots(boolean inc, int amount) {
        if (inc) {
            this.shoots += amount;
        }
        else {
            this.shoots -= amount;
            if (this.shoots < 0) {
                this.shoots = 0;
            }
        }
    }
}
