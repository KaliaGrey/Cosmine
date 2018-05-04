package kalia.cosmine.network.playerspiritweb.client;

import io.netty.buffer.ByteBuf;
import kalia.cosmine.capability.PlayerSpiritweb;
import kalia.cosmine.network.PacketHandler;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class ClientBurstingPacket implements IMessage {
    public int entityID;
    public boolean bursting;

    @Override
    public void fromBytes(ByteBuf buffer) {
        this.entityID = buffer.readInt();
        this.bursting = buffer.readBoolean();
    }

    @Override
    public  void toBytes(ByteBuf buffer) {
        buffer.writeInt(this.entityID);
        buffer.writeBoolean(this.bursting);
    }

    public ClientBurstingPacket() {
        this.entityID = -1;
        this.bursting = false;
    }

    public ClientBurstingPacket(int entityID, boolean bursting) {
        this.entityID = entityID;
        this.bursting = bursting;
    }

    public static class Handler extends PacketHandler<ClientBurstingPacket> {
        @Override
        protected void handleMessage(ClientBurstingPacket message, MessageContext context) {
            EntityPlayerMP player = context.getServerHandler().player; //Todo: Check this always works correctly, may make entityID redundant?
            PlayerSpiritweb spiritweb = PlayerSpiritweb.getPlayerSpiritWeb(player);

            spiritweb.onClientBurstingPacket(message);
        }
    }
}
