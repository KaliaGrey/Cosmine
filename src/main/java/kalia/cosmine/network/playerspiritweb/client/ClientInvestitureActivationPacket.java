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

public class ClientInvestitureActivationPacket implements IMessage {
    public int entityID;
    public String investiture;
    public ActivationLevel level;

    @Override
    public void fromBytes(ByteBuf buffer) {
        this.entityID = buffer.readInt();
        this.investiture = ByteBufUtils.readUTF8String(buffer);
        this.level = ActivationLevel.fromIndex(buffer.readInt());
    }

    @Override
    public  void toBytes(ByteBuf buffer) {
        buffer.writeInt(entityID);
        ByteBufUtils.writeUTF8String(buffer, this.investiture);
        buffer.writeInt(ActivationLevel.toIndex(this.level));
    }

    public ClientInvestitureActivationPacket() {
        this.entityID = -1;
        this.investiture = null;
        this.level = ActivationLevel.NONE;
    }

    public ClientInvestitureActivationPacket(int entityID, Investiture investiture, ActivationLevel level) {
        this.entityID = entityID;
        this.investiture = investiture.name;
        this.level = level;
    }

    public static class Handler extends PacketHandler<ClientInvestitureActivationPacket> {
        @Override
        protected void handleMessage(ClientInvestitureActivationPacket message, MessageContext context) {
            EntityPlayerMP player = context.getServerHandler().player; //Todo: Check this always works correctly, may make entityID redundant?
            PlayerSpiritweb spiritweb = PlayerSpiritweb.getPlayerSpiritWeb(player);

            spiritweb.onClientInvestitureActivationPacket(message);
        }
    }
}
