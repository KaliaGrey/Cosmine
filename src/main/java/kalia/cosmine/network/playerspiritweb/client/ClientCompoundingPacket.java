package kalia.cosmine.network.playerspiritweb.client;

import io.netty.buffer.ByteBuf;
import kalia.cosmine.capability.PlayerSpiritweb;
import kalia.cosmine.investiture.ActivationLevel;
import kalia.cosmine.investiture.Investiture;
import kalia.cosmine.network.PacketHandler;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class ClientCompoundingPacket implements IMessage {
    public int entityID;
    public String investiture;
    public boolean compounding;

    @Override
    public void fromBytes(ByteBuf buffer) {
        this.entityID = buffer.readInt();
        this.investiture = ByteBufUtils.readUTF8String(buffer);
        this.compounding = buffer.readBoolean();
    }

    @Override
    public  void toBytes(ByteBuf buffer) {
        buffer.writeInt(this.entityID);
        ByteBufUtils.writeUTF8String(buffer, this.investiture);
        buffer.writeBoolean(this.compounding);
    }

    public ClientCompoundingPacket() {
        this.entityID = -1;
        this.investiture = null;
        this.compounding = false;
    }

    public ClientCompoundingPacket(int entityID, Investiture investiture, boolean compounding) {
        this.entityID = entityID;
        this.investiture = investiture.fullName;
        this.compounding = compounding;
    }

    public static class Handler extends PacketHandler<ClientCompoundingPacket> {
        @Override
        protected void handleMessage(ClientCompoundingPacket message, MessageContext context) {
            EntityPlayerMP player = context.getServerHandler().player; //Todo: Check this always works correctly, may make entityID redundant?
            PlayerSpiritweb spiritweb = PlayerSpiritweb.getPlayerSpiritWeb(player);

            spiritweb.onClientCompoundingPacket(message);
        }
    }
}
