package kalia.cosmine.network.allomancy;

import io.netty.buffer.ByteBuf;
import kalia.cosmine.capability.PlayerSpiritweb;
import kalia.cosmine.investiture.allomancy.InherentAllomancySource;
import kalia.cosmine.network.PacketHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class InherentAllomancyPacket implements IMessage {
    public String identity;
    public String investiture;
    public NBTTagCompound nbt;

    @Override
    public void fromBytes(ByteBuf buffer) {
        this.identity = ByteBufUtils.readUTF8String(buffer);
        this.investiture = ByteBufUtils.readUTF8String(buffer);
        this.nbt = ByteBufUtils.readTag(buffer);
    }

    @Override
    public  void toBytes(ByteBuf buffer) {
        ByteBufUtils.writeUTF8String(buffer, this.identity);
        ByteBufUtils.writeUTF8String(buffer, this.investiture);
        ByteBufUtils.writeTag(buffer, nbt);
    }

    public InherentAllomancyPacket() {
        this.identity = null;
        this.investiture = null;
        this.nbt = null;
    }

    public InherentAllomancyPacket(String identity, InherentAllomancySource inherentAllomancySource) {
        this.identity = identity;
        this.investiture = inherentAllomancySource.getInvestiture().name;
        this.nbt = inherentAllomancySource.serializeNBT();
    }

    public static class Handler extends PacketHandler<InherentAllomancyPacket> {
        @Override
        protected void handleMessage(InherentAllomancyPacket message, MessageContext context) {
            EntityPlayer player = Minecraft.getMinecraft().player;
            PlayerSpiritweb spiritweb = PlayerSpiritweb.getPlayerSpiritWeb(player);

            spiritweb.onInherentAllomancyPacket(message);
        }
    }
}
