package kalia.cosmine.network.playerspiritweb;

import io.netty.buffer.ByteBuf;
import kalia.cosmine.capability.PlayerSpiritweb;
import kalia.cosmine.network.PacketHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PlayerSpiritwebPacket implements IMessage {
    public int entityID;
    public NBTTagCompound nbt;

    @Override
    public void fromBytes(ByteBuf buffer) {
        this.entityID = buffer.readInt();
        this.nbt = ByteBufUtils.readTag(buffer);
    }

    @Override
    public  void toBytes(ByteBuf buffer) {
        buffer.writeInt(entityID);
        ByteBufUtils.writeTag(buffer, nbt);
    }

    public PlayerSpiritwebPacket() {
        this.entityID = -1;
        this.nbt = null;
    }

    public PlayerSpiritwebPacket(int entityID, PlayerSpiritweb spiritweb) {
        this.entityID = entityID;
        this.nbt = spiritweb.serializeNBT();
    }

    public static class Handler extends PacketHandler<PlayerSpiritwebPacket> {
        @Override
        protected void handleMessage(PlayerSpiritwebPacket message, MessageContext context) {
            EntityPlayer player = (EntityPlayer)Minecraft.getMinecraft().world.getEntityByID(message.entityID);
            PlayerSpiritweb spiritweb = PlayerSpiritweb.getPlayerSpiritWeb(player);

            spiritweb.deserializeNBT(message.nbt);
        }
    }
}
