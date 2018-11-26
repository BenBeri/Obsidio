package com.benberi.cadesim.game.entity.vessel;

import com.benberi.cadesim.GameContext;
import com.benberi.cadesim.game.entity.vessel.impl.WarBrig;
import com.benberi.cadesim.game.entity.vessel.impl.WarFrigate;
import com.benberi.cadesim.game.entity.vessel.impl.Xebec;
import com.benberi.cadesim.game.entity.vessel.impl.Junk;
import com.benberi.cadesim.game.entity.vessel.impl.WarGalleon;

public class VesselFactory {

    private static final int warBrig = 2;
    private static final int warFrig = 3;
    private static final int xebec = 4;
    private static final int junk = 5;
    private static final int warGalleon = 6;
    
    public static Vessel create(GameContext context, String name, int x, int y, int type) {
        switch (type) {
        	case junk:
        		return new Junk(context, name, x, y);
            case warBrig:
                return new WarBrig(context, name, x, y);
            case xebec:
            	return new Xebec(context, name, x, y);
            case warGalleon:
                return new WarGalleon(context, name, x, y);
            case warFrig:
                return new WarFrigate(context, name, x, y);
        }

        return new WarFrigate(context, name, x, y);
    }
}
