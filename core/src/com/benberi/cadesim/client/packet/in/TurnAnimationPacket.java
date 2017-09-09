package com.benberi.cadesim.client.packet.in;

import com.benberi.cadesim.GameContext;
import com.benberi.cadesim.client.codec.util.Packet;
import com.benberi.cadesim.client.packet.ClientPacketExecutor;
import com.benberi.cadesim.game.entity.vessel.Vessel;
import com.benberi.cadesim.game.entity.vessel.VesselMovementAnimation;

public class TurnAnimationPacket extends ClientPacketExecutor {


    public TurnAnimationPacket(GameContext ctx) {
        super(ctx);
    }

    @Override
    public void execute(Packet p) {

        int numberOfShips = p.readByte();

        for (int i = 0; i < numberOfShips; i++) {
            String name = p.readByteString();

            Vessel vessel = getContext().getEntities().getVesselByName(name);
            if (vessel != null) {
                for (int j = 0; j < 4; j++) {
                    VesselMovementAnimation anim = VesselMovementAnimation.forId(p.readByte());
                    vessel.getAnimationsQueue().add(anim);
                }
            }
        }

        getContext().getControlScene().getBnavComponent().resetMoves();
    }

    @Override
    public int getSize() {
        return -1;
    }
}
