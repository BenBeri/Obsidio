package com.benberi.cadesim.game.scene.impl.control;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
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
    private int cannons;

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

    protected BattleControlComponent(GameContext context, ControlAreaScene owner, boolean big) {
        super(context, owner);
        if (big) {
            movesHolder = new BigShipHandMove[4];
        }
        else {
            movesHolder = new SmallShipHandMove[4];
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

        cannonSlots = new Texture("core/assets/ui/cannonslots.png");
        moves = new Texture("core/assets/ui/move.png");
        emptyMoves = new Texture("core/assets/ui/move_empty.png");
        shiphand = new Texture("core/assets/ui/shiphand.png");
        hourGlass = new Texture("core/assets/ui/hourglass.png");
        controlBackground = new Texture("core/assets/ui/moves-background.png");
        shipStatus = new Texture("core/assets/ui/status.png");
        shipStatusBg = new Texture("core/assets/ui/status-bg.png");
        moveGetTargetTexture = new Texture("core/assets/ui/sel_border_square.png");

        damage = new TextureRegion(new Texture("core/assets/ui/damage.png"));
        bilge = new TextureRegion(new Texture("core/assets/ui/bilge.png"));
        damage.flip(false, true);
        bilge.flip(false, true);

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

    @Override
    public void update() {
       // System.out.println(Gdx.input.getX() + " " + Gdx.input.getY());
    }

    @Override
    public void render() {
        renderMoveControl();
    }

    @Override
    public boolean handleClick(float x, float y) {
        if (isChosedLeft(x, y)) {
            this.targetMove = MoveType.LEFT;
        }
        else if (isChosedForward(x, y)) {
            this.targetMove = MoveType.FORWARD;
        }
        else if (isChosedRight(x, y)) {
            this.targetMove = MoveType.RIGHT;
        }
        return false;
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
        batch.end();
    }

    private void drawMoveHolder() {
        // The hand bg
        batch.draw(shiphand, controlBackground.getWidth() - shiphand.getWidth() - 80, 19);

        int height = controlBackground.getHeight() - 40;
        for (int i = 0; i < movesHolder.length; i++) {
            HandMove move = movesHolder[i];

            batch.draw(emptyCannonLeft, controlBackground.getWidth() - shiphand.getWidth() - 81, height);
            batch.draw(emptyCannonRight, controlBackground.getWidth() - shiphand.getWidth() - 35, height);

            if (movesHolder instanceof BigShipHandMove[]) {
                batch.draw(emptyCannonLeft, controlBackground.getWidth() - shiphand.getWidth() - 96, height);
                batch.draw(emptyCannonRight, controlBackground.getWidth() - shiphand.getWidth() - 20, height);
            }

            height -= 34;
        }
    }

    /**
     * Draws the movement selection section
     */
    private void drawMovesSelect() {
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
}
