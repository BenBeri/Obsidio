package com.benberi.cadesim.game.scene.impl.control;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
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

    private HandMove[] movesHolder;

    private Texture shiphand;

    private Texture moves;
    private Texture emptyMoves;

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

    private Texture cannonSlots;
    private TextureRegion cannonLeft;
    private TextureRegion cannonRight;
    private TextureRegion emptyCannonLeft;
    private TextureRegion emptyCannonRight;

    private Texture controlBackground;

    private SpriteBatch batch;
    private ShapeRenderer shape;

    protected BattleControlComponent(GameContext context, ControlAreaScene owner) {
        super(context, owner);
     //   for (int i = 0; i < movesHolder.length; i++) {
//            if (big) {
//                movesHolder[i] = new BigShipHandMove();
//            }
//            else {
//                movesHolder[i] = new SmallShipHandMove();
//            }
      //  }
    }

    @Override
    public void create() {
        batch = new SpriteBatch();
        shape = new ShapeRenderer();

        cannonSlots = new Texture("core/assets/ui/cannonslots.png");
        moves = new Texture("core/assets/ui/move.png");
        emptyMoves = new Texture("core/assets/ui/move_empty.png");
        shiphand = new Texture("core/assets/ui/shiphand.png");
        hourGlass = new Texture("core/assets/ui/hourglass.png");
        controlBackground = new Texture("core/assets/ui/moves-background.png");

        leftMoveTexture = new TextureRegion(moves, 0, 0, 28, 28);
        forwardMoveTexture = new TextureRegion(moves, 28, 0, 28, 28);
        rightMoveTexture = new TextureRegion(moves, 56, 0, 28, 28);
        manuaverTexture = new TextureRegion(moves, 84, 0, 28, 28);

        emptyLeftMoveTexture = new TextureRegion(emptyMoves, 0, 0, 28, 28);
        emptyForwardMoveTexture = new TextureRegion(emptyMoves, 28, 0, 28, 28);
        emptyRightMoveTexture = new TextureRegion(emptyMoves, 56, 0, 28, 28);

        emptyCannonLeft = new TextureRegion(cannonSlots, 0, 0, 16, 18);
        emptyCannonRight = new TextureRegion(cannonSlots, 16, 0, 16, 18);

        cannonLeft = new TextureRegion(cannonSlots, 32, 0, 16, 18);
        cannonRight = new TextureRegion(cannonSlots, 48, 0, 16, 18);
    }

    @Override
    public void update() {

    }

    @Override
    public void render() {
        renderMoveControl();
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

    private void renderMoveControl() {
        batch.begin();

        // The yellow BG
        batch.draw(controlBackground, 5, 8, controlBackground.getWidth(), controlBackground.getHeight() + 5);

        // The hand bg
        batch.draw(shiphand, controlBackground.getWidth() - shiphand.getWidth() - 80, 19);

        int height = controlBackground.getHeight() - 40;

//        for (int i = 0; i < movesHolder.length; i++) {
//            // SmallShipHandMove m = movesHolder[i];
//
//            batch.draw(emptyCannonLeft, controlBackground.getWidth() - shiphand.getWidth() - 80, height);
//            batch.draw(emptyCannonRight, controlBackground.getWidth() - shiphand.getWidth() - 35, height);
//        }

        // The timer
        batch.draw(hourGlass, controlBackground.getWidth() - hourGlass.getWidth() - 22, 22);

        batch.draw(emptyLeftMoveTexture, 70, controlBackground.getHeight() - 85);
        batch.draw(emptyForwardMoveTexture, 102, controlBackground.getHeight() - 85);
        batch.draw(emptyRightMoveTexture, 134, controlBackground.getHeight() - 85);
        batch.end();
    }
}
