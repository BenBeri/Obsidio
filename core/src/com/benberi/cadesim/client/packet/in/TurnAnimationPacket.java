package com.benberi.cadesim.client.packet.in;

import com.benberi.cadesim.GameContext;
import com.benberi.cadesim.client.codec.util.Packet;
import com.benberi.cadesim.client.packet.ClientPacketExecutor;
import com.benberi.cadesim.game.entity.vessel.Vessel;
import com.benberi.cadesim.game.entity.vessel.VesselMovementAnimation;
import com.benberi.cadesim.game.entity.vessel.move.MoveAnimationTurn;

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
                for (int slot = 0; slot < 4; slot++) {
                    MoveAnimationTurn turn = vessel.getStructure().getTurn(slot);
                    turn.setAnimation(VesselMovementAnimation.forId(p.readByte()));
                    turn.setSubAnimation(VesselMovementAnimation.forId(p.readByte()));
                    turn.setLeftShoots(p.readByte());
                    turn.setRightShoots(p.readByte());
                }

                System.out.println(name + ": " + vessel.getStructure().toString());
            }
            else {
                p.getBuffer().readerIndex(p.getBuffer().readerIndex() + 4);
            }

        }

        getContext().getBattleScene().setTurnExecute();
        getContext().getControlScene().getBnavComponent().resetMoves();
    }

    @Override
    public int getSize() {
        return -1;
    }
}
