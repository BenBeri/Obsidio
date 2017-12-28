package com.benberi.cadesim.game.scene.impl.connect;

import com.badlogic.gdx.scenes.scene2d.ui.Label;

public class TeamTypeLabel extends Label {
    public TeamTypeLabel( CharSequence text, LabelStyle style) {
        super(text, style);
    }

    @Override
    public String toString() {
        return "Team: " + getText();
    }

    public int getType() {
        if (getText().toString().equalsIgnoreCase("green")) {
            return 1;
        }
        return 0;
    }
}
