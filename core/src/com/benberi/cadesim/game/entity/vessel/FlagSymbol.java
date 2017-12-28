package com.benberi.cadesim.game.entity.vessel;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.benberi.cadesim.game.cade.Team;

public class FlagSymbol extends TextureRegion {

    private int size;

    private Team team;

    private boolean war;

    private boolean isBlue;

    public FlagSymbol(int size, boolean atWar, Team controllerTeam) {
        this.size = size;
        this.war = atWar;
        this.team = controllerTeam;
    }

    public void setLocal(boolean b) {
        this.isBlue = b;
    }

    public int getSize() {
        return this.size;
    }

    public boolean isWar() {
        return this.war;
    }

    public void createTexture() {
        setTexture(new Texture("core/assets/cade/buoy_symbols.png"));
        if (isBlue) {
            switch (size) {
                case 1:
                    setRegion(0, 0, 10, 13);
                    break;
                case 2:
                    setRegion(10, 0, 10, 13);
                    break;
                case 3:
                    setRegion(20, 0, 10, 13);
                    break;
            }
            return;
        }
        else if (war) {
            switch (size) {
                case 1:
                    setRegion(120, 0, 10, 13);
                    break;
                case 2:
                    setRegion(130, 0, 10, 13);
                    break;
                case 3:
                    setRegion(140, 0, 10, 13);
                    break;
            }
            return;
        }
        else {
            switch (team) {
                case RED:
                    switch (size) {
                        case 1:
                            setRegion(60, 0, 10, 13);
                            break;
                        case 2:
                            setRegion(70, 0, 10, 13);
                            break;
                        case 3:
                            setRegion(80, 0, 10, 13);
                            break;
                    }
                    break;
                case GREEN:
                    switch (size) {
                        case 1:
                            setRegion(30, 0, 10, 13);
                            break;
                        case 2:
                            setRegion(40, 0, 10, 13);
                            break;
                        case 3:
                            setRegion(50, 0, 10, 13);
                            break;
                    }
                    break;
            }
        }
    }
}
