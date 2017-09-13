package com.benberi.cadesim.game.scene.impl.control;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.benberi.cadesim.Constants;
import com.benberi.cadesim.GameContext;
import com.benberi.cadesim.game.entity.vessel.move.MoveType;
import com.benberi.cadesim.game.scene.SceneComponent;
import com.benberi.cadesim.game.scene.impl.control.hand.HandMove;
import com.benberi.cadesim.game.scene.impl.control.hand.impl.BigShipHandMove;
import com.benberi.cadesim.game.scene.impl.control.hand.impl.SmallShipHandMove;

public class BattleControlComponent extends SceneComponent<ControlAreaScene> {

    /**
     * Left moves
     */
    private int leftMoves = 2;

    /**
     * Right moves
     */
    private int rightMoves = 4;

    /**
     * Forward moves
     */
    private int forwardMoves;

    /**
     * The available shoots
     */
    private int cannons = 0;

    /**
     * The selected moves
     */
    private HandMove[] movesHolder;

    /**
     * Batch renderer for sprites and textures
     */
    private SpriteBatch batch;

    /**
     * Shape renderer for shapes, used for damage/bilge and such
     */
    private ShapeRenderer shape;

    /**
     * Font for texts
     */
    private BitmapFont font;

    /**
     * The target move
     */
    private MoveType targetMove = MoveType.RIGHT;

    /**
     * The damage of the vessel
     */
    private float damageAmount;

    /**
     * The bilge of the vessel
     */
    private float bilgeAmount;

    /**
     * If the move selection is automatic or not
     */
    private boolean auto;

    /**
     * The turn time
     */
    private int time = 0;

    /**
     * Textures
     */
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
    private Texture sandTopTexture;
    private Texture sandBottomTexture;
    private TextureRegion sandBottom;
    private TextureRegion sandTop;

    private TextureRegion emptyCannon;
    private TextureRegion cannon;

    private Texture sandTrickleTexture;
    private TextureRegion sandTrickle;

    private Texture hourGlass;
    private Texture cannonSlots;
    private TextureRegion cannonLeft;
    private TextureRegion cannonRight;
    private TextureRegion emptyCannonLeft;
    private TextureRegion emptyCannonRight;
    private Texture controlBackground;
    private Texture shipStatus;
    private Texture shipStatusBg;
    private TextureRegion damage;
    private TextureRegion bilge;
    private Texture moveGetTargetTexture;
    private TextureRegion moveTargetSelAuto;
    private TextureRegion moveTargetSelForce;
    private Texture title;
    private Texture radioOn;
    private Texture radioOff;
    private Texture autoOn;
    private Texture autoOff;

    private Texture cannonSelection;
    private Texture cannonSelectionEmpty;

    private int manuaverSlot = 3;

    private boolean isBigShip;

    private boolean isDragging;
    private Vector2 draggingPosition;

    protected BattleControlComponent(GameContext context, ControlAreaScene owner, boolean big) {
        super(context, owner);
        if (big) {
            movesHolder = new BigShipHandMove[4];
            isBigShip = true;
        }
        else {
            movesHolder = new SmallShipHandMove[4];
        }

        for (int i = 0; i < movesHolder.length; i++) {
            movesHolder[i] = createMove();
        }
    }

    @Override
    public void create() {
        batch = new SpriteBatch();
        shape = new ShapeRenderer();

        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("core/assets/font/Roboto-Regular.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 14;
        font = generator.generateFont(parameter);

        title = new Texture("core/assets/ui/title.png");
        radioOn = new Texture("core/assets/ui/radio-on.png");
        radioOff = new Texture("core/assets/ui/radio-off.png");
        autoOn = new Texture("core/assets/ui/auto-on.png");
        autoOff = new Texture("core/assets/ui/auto-off.png");

        sandTopTexture = new Texture("core/assets/ui/sand_top.png");
        sandBottomTexture = new Texture("core/assets/ui/sand_bot.png");

        sandTrickleTexture = new Texture("core/assets/ui/sand_trickle.png");
        sandTrickle = new TextureRegion(sandTrickleTexture, 0, 0, 1, sandTopTexture.getHeight());

        sandTop = new TextureRegion(sandTopTexture, sandTopTexture.getWidth(), sandTopTexture.getHeight());
        sandBottom= new TextureRegion(sandBottomTexture, sandBottomTexture.getWidth(), sandBottomTexture.getHeight());

        cannonSlots = new Texture("core/assets/ui/cannonslots.png");
        moves = new Texture("core/assets/ui/move.png");
        emptyMoves = new Texture("core/assets/ui/move_empty.png");
        shiphand = new Texture("core/assets/ui/shiphand.png");
        hourGlass = new Texture("core/assets/ui/hourglass.png");
        controlBackground = new Texture("core/assets/ui/moves-background.png");
        shipStatus = new Texture("core/assets/ui/status.png");
        shipStatusBg = new Texture("core/assets/ui/status-bg.png");
        moveGetTargetTexture = new Texture("core/assets/ui/sel_border_square.png");
        cannonSelectionEmpty = new Texture("core/assets/ui/grapplecannon_empty.png");
        cannonSelection = new Texture("core/assets/ui/grapplecannon.png");
        damage = new TextureRegion(new Texture("core/assets/ui/damage.png"));
        bilge = new TextureRegion(new Texture("core/assets/ui/bilge.png"));
        damage.flip(false, true);
        bilge.flip(false, true);

        emptyCannon = new TextureRegion(cannonSelectionEmpty, 25, 0, 25, 25);
        cannon = new TextureRegion(cannonSelection, 25, 0, 25, 25);

        damage.setRegionWidth(damage.getTexture().getWidth());
        bilge.setRegionWidth(bilge.getTexture().getWidth());

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

        moveTargetSelForce = new TextureRegion(moveGetTargetTexture, 0, 0, 36, 36);
        moveTargetSelAuto = new TextureRegion(moveGetTargetTexture, 36, 0, 36, 36);

        setDamagePercentage(70);
        setBilgePercentage(30);

    }

    public HandMove createMove() {
        if (isBigShip) {
            return new BigShipHandMove();
        }
        return new SmallShipHandMove();
    }

    @Override
    public void update() {

        double ratio = (double) sandTopTexture.getHeight() / (double) Constants.TURN_TIME;

        sandTop.setRegionY(sandTopTexture.getHeight() - (int) Math.round(time * ratio));
        sandTop.setRegionHeight((int) Math.round(time * ratio));

        ratio =  (double) sandBottomTexture.getHeight() / (double) Constants.TURN_TIME;

        sandBottom.setRegionY(sandBottomTexture.getHeight() - (int) Math.round((Constants.TURN_TIME - time) * ratio));
        sandBottom.setRegionHeight((int) Math.round((Constants.TURN_TIME - time) * ratio));
    }

    @Override
    public void render() {
        renderMoveControl();
    }

    @Override
    public void dispose() {
        resetMoves();
        targetMove = MoveType.FORWARD;
        manuaverSlot = 3;
    }


    @Override
    public boolean handleClick(float x, float y, int button) {
        System.out.println(x + " " + y);
        if (isPlacingMoves(x, y)) {
            if (y >= 538 && y <= 569) {
                handleMovePlace(0, button);
            }
            else if (y >= 573 && y <= 603) {
                handleMovePlace(1, button);
            }
            else if (y >= 606 && y <= 637) {
                handleMovePlace(2, button);
            }
            else if(y >= 642 && y <= 670) {
                handleMovePlace(3, button);
            }
        }
        else if (isPlacingLeftCannons(x, y)) {
            if (y >= 548 && y <= 562) {
                getContext().sendAddCannon(0, 0);
            }
            else if (y >= 582 && y <= 597) {
                getContext().sendAddCannon(0, 1);
            }
            else if (y >= 618 && y <= 630) {
                getContext().sendAddCannon(0, 2);
            }
            else if (y >= 650 && y <= 665) {
                getContext().sendAddCannon(0, 3);
            }
        }
        else if (isPlacingRightCannons(x, y)) {
            if (y >= 548 && y <= 562) {
                getContext().sendAddCannon(1, 0);
            }
            else if (y >= 582 && y <= 597) {
                getContext().sendAddCannon(1, 1);
            }
            else if (y >= 618 && y <= 630) {
                getContext().sendAddCannon(1, 2);
            }
            else if (y >= 650 && y <= 665) {
                getContext().sendAddCannon(1, 3);
            }
        }
        else if (isTogglingAuto(x, y)) {
            if (auto) {
                auto = false;
            }
            else {
                auto = true;
            }
            getContext().sendToggleAuto(auto);
        }
        else if (!auto){
             if (isChosedLeft(x, y)) {
                this.targetMove = MoveType.LEFT;
                getContext().sendGenerationTarget(targetMove);
            }
            else if (isChosedForward(x, y)) {
                this.targetMove = MoveType.FORWARD;
                 getContext().sendGenerationTarget(targetMove);
            }
            else if (isChosedRight(x, y)) {
                this.targetMove = MoveType.RIGHT;
                 getContext().sendGenerationTarget(targetMove);
            }
        }

        return false;
    }

    private void handleLeftCannonPlace(float x, float y) {

    }

    private boolean isTogglingAuto(float x, float y) {
        return x >= 52 && x <= 68 && y >= 579 && y <= 591;
    }

    private boolean isPlacingLeftCannons(float x, float y) {
        return x >= 181 && x <= 206;
    }

    private boolean isPlacingRightCannons(float x, float y) {
        return x >= 241 && x <= 271;
    }

    private int getSlotForPosition(float x, float y) {
        if (y >= 538 && y <= 569) {
            return 0;
        }
        else if (y >= 573 && y <= 603) {
           return 1;
        }
        else if (y >= 606 && y <= 637) {
            return 2;
        }
        else if(y >= 642 && y <= 670) {
           return 3;
        }
        return -1;
    }

    @Override
    public boolean handleDrag(float x, float y, float ix, float iy) {
        if (!isDragging) {
            if (getSlotForPosition(x, y) == manuaverSlot) {
                isDragging = true;
            }
        }

        if (isDragging) {
            draggingPosition = new Vector2(x, y);
        }
        return false;
    }

    @Override
    public boolean handleRelease(float x, float y, int button) {
        if (isDragging) {
            isDragging = false;
            int slot = getSlotForPosition(x, y);
            if (slot != -1) {
                manuaverSlot = slot;
                getContext().sendManuaverSlotChanged(manuaverSlot);
            }

            draggingPosition = null;
        }
        return false;
    }

    private void handleMovePlace(int position, int button) {
        if (position == manuaverSlot) {
            return;
        }
        HandMove move = movesHolder[position];
        if (move.getMove() == MoveType.NONE) {
            if (button == Input.Buttons.LEFT) {
                //move.setMove(MoveType.LEFT);
                if (leftMoves > 0) {
                    getContext().sendSelectMoveSlot(position, MoveType.LEFT);
                }
                else if (forwardMoves > 0) {
                    getContext().sendSelectMoveSlot(position, MoveType.FORWARD);
                }
                else if (rightMoves > 0) {
                    getContext().sendSelectMoveSlot(position, MoveType.RIGHT);
                }
            }
            else if (button == Input.Buttons.MIDDLE) {
               // move.setMove(MoveType.FORWARD);
                if (forwardMoves > 0) {
                    getContext().sendSelectMoveSlot(position, MoveType.FORWARD);
                }
                else if (rightMoves > 0) {
                    getContext().sendSelectMoveSlot(position, MoveType.RIGHT);
                }
                else if (leftMoves > 0) {
                    getContext().sendSelectMoveSlot(position, MoveType.LEFT);
                }
            }
            else if (button == Input.Buttons.RIGHT) {
               // move.setMove(MoveType.RIGHT);
                if (rightMoves > 0) {
                    getContext().sendSelectMoveSlot(position, MoveType.RIGHT);
                }
                else if (leftMoves > 0) {
                    getContext().sendSelectMoveSlot(position, MoveType.LEFT);
                }
                else if (forwardMoves > 0) {
                    getContext().sendSelectMoveSlot(position, MoveType.FORWARD);
                }
            }
        }
        else {
            if (button == Input.Buttons.LEFT) {
               // move.setMove(move.getMove().getNext());
                MoveType next = move.getMove().getNext();
                if (hasMove(next)) {
                    getContext().sendSelectMoveSlot(position, next);
                }
                else if (hasMove(next.getNext())) {
                    getContext().sendSelectMoveSlot(position, next.getNext());
                }
                else if (hasMove(next.getNext().getNext())) {
                    getContext().sendSelectMoveSlot(position, next.getNext().getNext());
                }
            }
            else if (button == Input.Buttons.RIGHT) {
               // move.setMove(move.getMove().getPrevious());
                MoveType prev = move.getMove().getPrevious();
                if (hasMove(prev)) {
                    getContext().sendSelectMoveSlot(position, prev);
                }
                else if (hasMove(prev.getPrevious())) {
                    getContext().sendSelectMoveSlot(position, prev.getPrevious());
                }
                else if (hasMove(prev.getPrevious().getPrevious())) {
                    getContext().sendSelectMoveSlot(position, prev.getPrevious().getPrevious());
                }
            }
        }

    }

    private boolean hasMove(MoveType move) {
        switch (move) {
            case LEFT:
                return leftMoves > 0;
            case RIGHT:
                return rightMoves > 0;
            case FORWARD:
                return forwardMoves > 0;
            default:
                return true;
        }
    }

    private boolean isPlacingMoves(float x, float y) {
        return x >= 208 && x <= 239;
    }

    /**
     * Sets the turn time
     * @param time  The turn time in seconds
     */
    public void setTime(int time) {
        this.time = time;
        int sandX = sandTrickle.getRegionX();
        sandX++;
        if (sandX > sandTrickleTexture.getWidth()) {
            sandX = 0;
        }

        sandTrickle.setRegionX(sandX);
        sandTrickle.setRegionWidth(1);
    }

    /**
     * Sets the damage percentage
     * @param d The percentage to set out of 100
     */
    public void setDamagePercentage(int d) {
        if (d > 100) {
            d = 100;
        }
        this.damageAmount = (float) d;
    }

    /**
     * Sets the bilge percentage
     * @param b The percentage to set out of 100
     */
    public void setBilgePercentage(int b) {
        if (b > 100) {
            b = 100;
        }
        this.bilgeAmount = (float) b;
    }

    public void setMoveSelAutomatic(boolean auto) {
        this.auto = auto;
    }

    /**
     * Sets the available moves to use
     * @param left      The amount of left movements
     * @param forward   The amount of forward movements
     * @param right     The amount of right movements
     */
    public void setMoves(int left, int forward, int right) {
        this.leftMoves = left;
        this.forwardMoves = forward;
        this.rightMoves = right;
    }

    /**
     * Sets the available cannonballs to use
     * @param cannonballs The number of available cannonballs for use
     */
    public void setLoadedCannonballs(int cannonballs) {
        this.cannons = cannonballs;
    }

    private void renderMoveControl() {
        batch.begin();

        // The yellow BG
        batch.draw(controlBackground, 5, 8, controlBackground.getWidth(), controlBackground.getHeight() + 5);

        drawMoveHolder();
        drawShipStatus();
        drawTimer();
        drawMovesSelect();
        batch.draw(title, 65, 140);
        if (isDragging) {
            batch.draw(manuaverTexture, draggingPosition.x - manuaverTexture.getRegionWidth() / 2, Gdx.graphics.getHeight() - draggingPosition.y - manuaverTexture.getRegionHeight() / 2);
        }
        batch.end();
    }

    private void drawMoveHolder() {

        // The hand bg
        batch.draw(shiphand, controlBackground.getWidth() - shiphand.getWidth() - 80, 19);


        int height = controlBackground.getHeight() - 40;
        for (int i = 0; i < movesHolder.length; i++) {
            HandMove move = movesHolder[i];

            boolean[] left = move.getLeft();
            boolean[] right = move.getRight();

            batch.draw(emptyCannonLeft, controlBackground.getWidth() - shiphand.getWidth() - 81, height); // left
            if (left[0]) {
                batch.draw(cannonLeft, controlBackground.getWidth() - shiphand.getWidth() - 81, height); // left
            }
            batch.draw(emptyCannonRight, controlBackground.getWidth() - shiphand.getWidth() - 35, height); // right
            if (right[0]) {
                batch.draw(cannonRight, controlBackground.getWidth() - shiphand.getWidth() - 35, height); // left
            }

            if (movesHolder instanceof BigShipHandMove[]) {
                batch.draw(emptyCannonLeft, controlBackground.getWidth() - shiphand.getWidth() - 96, height); // left
                if (left[0] && left[1]) {
                    batch.draw(cannonLeft, controlBackground.getWidth() - shiphand.getWidth() - 96, height); // left
                }
                batch.draw(emptyCannonRight, controlBackground.getWidth() - shiphand.getWidth() - 20, height); // right
                if (right[0] && right[1]) {
                    batch.draw(cannonRight, controlBackground.getWidth() - shiphand.getWidth() - 20, height); // right
                }
            }

            if (i == manuaverSlot) {
                batch.draw(manuaverTexture, controlBackground.getWidth() - shiphand.getWidth() - 64, height - 4);
            }
            else {
                if (move.getMove() != MoveType.NONE) {
                    batch.draw(getTextureForMove(move.getMove()), controlBackground.getWidth() - shiphand.getWidth() - 64, height - 4);
                }
            }

            height -= 34;
        }
    }

    private TextureRegion getTextureForMove(MoveType type) {
        switch (type) {
            case LEFT:
                return leftMoveTexture;
            case RIGHT:
                return rightMoveTexture;
            default:
            case FORWARD:
                return forwardMoveTexture;
        }
    }

    /**
     * Draws the movement selection section
     */
    private void drawMovesSelect() {

        font.draw(batch, "Auto", 18, controlBackground.getHeight() - 54);
        if (auto) {
            batch.draw(autoOn, 53, controlBackground.getHeight() - 70);
        }
        else {
            batch.draw(autoOff, 53, controlBackground.getHeight() - 70);
        }

        if (cannons > 0) {
            batch.draw(cannon, 49, controlBackground.getHeight() - 103);
        }
        else {
            batch.draw(emptyCannon, 49, controlBackground.getHeight() - 103);
        }

        font.draw(batch, "x" + Integer.toString(cannons), 56, controlBackground.getHeight() - 109);

        int x = 80;
        int y = controlBackground.getHeight() - 100;

        if (leftMoves == 0) {
            batch.draw(emptyLeftMoveTexture, x, y);
        }
        else {
            batch.draw(leftMoveTexture, x, y);
        }

        if (forwardMoves == 0) {
            batch.draw(emptyForwardMoveTexture, x + emptyLeftMoveTexture.getRegionWidth() + 2, y);
        }
        else {
            batch.draw(forwardMoveTexture, x + emptyLeftMoveTexture.getRegionWidth() + 2, y);
        }

        if (rightMoves == 0) {
            batch.draw(emptyRightMoveTexture, x + (emptyLeftMoveTexture.getRegionWidth() * 2) + 4, y);
        }
        else {
            batch.draw(rightMoveTexture, x + (emptyLeftMoveTexture.getRegionWidth() * 2) + 4, y);
        }


        TextureRegion sel = auto ? moveTargetSelAuto : moveTargetSelForce;

        if (targetMove != null) {
            switch(targetMove) {
                case LEFT:
                    batch.draw(sel, x - 4, y - 4);
                    break;
                case FORWARD:
                    batch.draw(sel, x + emptyLeftMoveTexture.getRegionWidth() + 2 - 4, y - 4);
                    break;
                case RIGHT:
                    batch.draw(sel, x + (emptyLeftMoveTexture.getRegionWidth() * 2) + 4 - 4, y - 4);
                    break;


            }
        }



        font.setColor(Color.BLACK);
        font.draw(batch, "x" + Integer.toString(leftMoves), x + 5, y - 5);
        font.draw(batch, "x" + Integer.toString(forwardMoves), x + emptyLeftMoveTexture.getRegionWidth() + 2 + 5, y - 5);
        font.draw(batch, "x" + Integer.toString(rightMoves), x + (emptyLeftMoveTexture.getRegionWidth() * 2) + 4 + 5, y - 5);
    }

    /**
     * Draws the sand clock
     */
    private void drawTimer() {
        batch.draw(hourGlass, controlBackground.getWidth() - hourGlass.getWidth() - 20, 25);
        batch.draw(sandTrickle,controlBackground.getWidth() - hourGlass.getWidth() - 7, 30 );
        batch.draw(sandTop, controlBackground.getWidth() - hourGlass.getWidth() - 16, 72);
        batch.draw(sandBottom, controlBackground.getWidth() - hourGlass.getWidth() - 16, 28);
    }

    /**
     * Draws ship status
     *
     * Ship damage, Ship bilge, etc
     */
    private void drawShipStatus() {
        int x = controlBackground.getWidth() - shipStatus.getWidth() - 12;
        int y = controlBackground.getHeight() - 50;
        batch.draw(shipStatusBg, x, y);

        batch.end();

        shape.begin(ShapeRenderer.ShapeType.Filled);

        // The values for damage and water are hard-coded here, they
        // should come from your code

        float redstuff = damageAmount / 100f;
        float redStart = 90.0f + 180.0f * (1.0f - redstuff);
        float redLength = 180.0f * redstuff;

        float bluestuff = bilgeAmount / 100;

        float blueStart = 270.0f;
        float blueLength = 180.0f * bluestuff;

        shape.setColor(new Color(131 / 255f, 6 / 255f, 0f, .7f));
        shape.arc(301, 146, 16.5f, redStart, redLength);
        shape.setColor(new Color(0f, 207 / 255f, 249f, .7f));
        shape.arc(304, 146, 16.5f, blueStart, blueLength);
        shape.end();

        batch.begin();

        batch.draw(shipStatus, x, y);
    }


    public boolean isChosedLeft(float x, float y) {
        return x >= 80 && x <= 107 && y >= 598 && y <= 624;
    }

    public boolean isChosedForward(float x, float y) {
        return x >= 110 && x <= 135 && y >= 598 && y <= 624;
    }

    public boolean isChosedRight(float x, float y) {
        return x >= 140 && x <= 166 && y >= 598 && y <= 624;
    }

    public void placeMove(int slot, MoveType move) {
        HandMove hm = movesHolder[slot];
        hm.setMove(move);
    }

    public void resetMoves() {
        for (int i = 0; i < movesHolder.length; i++) {
            movesHolder[i].setMove(MoveType.NONE);
            movesHolder[i].resetLeft();
            movesHolder[i].resetRight();
        }
        cannons = 0;
    }

    public void setCannons(int side, int slot, int amount) {
        if (side == 0) {
            movesHolder[slot].resetLeft();
            for (int i = 0; i < amount; i++)
                movesHolder[slot].addLeft();
        }
        else if (side == 1) {
            movesHolder[slot].resetRight();
            for (int i = 0; i < amount; i++)
                movesHolder[slot].addRight();
        }
    }

    public void setMoveSealTarget(MoveType moveSealTarget) {
        this.targetMove = moveSealTarget;
    }
}
