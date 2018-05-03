package kalia.cosmine.network.playerspiritweb;

import io.netty.buffer.ByteBuf;
import kalia.cosmine.capability.ISpiritweb;
import kalia.cosmine.capability.PlayerSpiritweb;
import kalia.cosmine.network.PacketHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class BurstingStatusPacket implements IMessage {
    public int entityID;
    public boolean bursting;

    @Override
    public void fromBytes(ByteBuf buffer) {
        this.entityID = buffer.readInt();
        this.bursting = buffer.readBoolean();
    }

    @Override
    public  void toBytes(ByteBuf buffer) {
        buffer.writeInt(entityID);
        buffer.writeBoolean(this.bursting);
    }

    public BurstingStatusPacket() {
        this.entityID = -1;
        this.bursting = false;
    }

    public BurstingStatusPacket(int entityID, ISpiritweb spiritweb) {
        this.entityID = entityID;
        this.bursting = spiritweb.isBursting();
    }

    public static class Handler extends PacketHandler<BurstingStatusPacket> {
        @Override
        protected void handleMessage(BurstingStatusPacket message, MessageContext context) {
            EntityPlayer player = (EntityPlayer)Minecraft.getMinecraft().world.getEntityByID(message.entityID);
            PlayerSpiritweb spiritweb = PlayerSpiritweb.getPlayerSpiritWeb(player);

            spiritweb.onBurstingStatusPacket(message);
        }
    }
}
