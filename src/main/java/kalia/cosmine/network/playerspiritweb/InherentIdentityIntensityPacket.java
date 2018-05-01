package kalia.cosmine.network.playerspiritweb;

import io.netty.buffer.ByteBuf;
import kalia.cosmine.capability.ISpiritweb;
import kalia.cosmine.capability.PlayerSpiritweb;
import kalia.cosmine.network.PacketHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class InherentIdentityIntensityPacket implements IMessage {
    public int entityID;
    public float intensity;

    @Override
    public void fromBytes(ByteBuf buffer) {
        this.entityID = buffer.readInt();
        this.intensity = buffer.readFloat();
    }

    @Override
    public  void toBytes(ByteBuf buffer) {
        buffer.writeInt(entityID);
        buffer.writeFloat(this.intensity);
    }

    public InherentIdentityIntensityPacket() {
        this.entityID = -1;
        this.intensity = 0;
    }

    public InherentIdentityIntensityPacket(int entityID, ISpiritweb spiritweb) {
        this.entityID = entityID;
        this.intensity = spiritweb.getIdentityIntensity();
    }

    public static class Handler extends PacketHandler<InherentIdentityIntensityPacket> {
        @Override
        protected void handleMessage(InherentIdentityIntensityPacket message, MessageContext context) {
            EntityPlayer player = (EntityPlayer)Minecraft.getMinecraft().world.getEntityByID(message.entityID);
            PlayerSpiritweb spiritweb = PlayerSpiritweb.getPlayerSpiritWeb(player);

            spiritweb.onInherentIdentityIntensityPacket(message);
        }
    }
}
