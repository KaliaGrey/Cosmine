package kalia.cosmine.network.playerspiritweb;

import io.netty.buffer.ByteBuf;
import kalia.cosmine.capability.PlayerSpiritweb;
import kalia.cosmine.investiture.SpiritwebInvestiture;
import kalia.cosmine.network.PacketHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class SpiritwebInvestiturePacket implements IMessage {
    public int entityID;
    public String investiture;
    public NBTTagCompound nbt;

    @Override
    public void fromBytes(ByteBuf buffer) {
        this.entityID = buffer.readInt();
        this.investiture = ByteBufUtils.readUTF8String(buffer);
        this.nbt = ByteBufUtils.readTag(buffer);
    }

    @Override
    public  void toBytes(ByteBuf buffer) {
        buffer.writeInt(entityID);
        ByteBufUtils.writeUTF8String(buffer, this.investiture);
        ByteBufUtils.writeTag(buffer, nbt);
    }

    public SpiritwebInvestiturePacket() {
        this.entityID = -1;
        this.investiture = null;
        this.nbt = null;
    }

    public SpiritwebInvestiturePacket(int entityID, SpiritwebInvestiture spiritwebInvestiture) {
        this.entityID = entityID;
        this.investiture = spiritwebInvestiture.investiture.name;
        this.nbt = spiritwebInvestiture.serializeNBT();
    }

    public static class Handler extends PacketHandler<SpiritwebInvestiturePacket> {
        @Override
        protected void handleMessage(SpiritwebInvestiturePacket message, MessageContext context) {
            EntityPlayer player = (EntityPlayer)Minecraft.getMinecraft().world.getEntityByID(message.entityID);
            PlayerSpiritweb spiritweb = PlayerSpiritweb.getPlayerSpiritWeb(player);

            spiritweb.onSpiritwebInvestiturePacket(message);
        }
    }
}
