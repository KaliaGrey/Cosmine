package kalia.cosmine.network;

import io.netty.buffer.ByteBuf;
import kalia.cosmine.capability.PlayerSpiritweb;
import kalia.cosmine.capability.PlayerSpiritwebProvider;
import kalia.cosmine.investiture.ActivationLevel;
import kalia.cosmine.investiture.Investiture;
import kalia.cosmine.investiture.SpiritwebInvestiture;
import kalia.cosmine.registry.InvestitureRegistry;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class InvestitureActivationChangedPacket implements IMessage {
    private String system;
    private String investiture;
    private ActivationLevel activationLevel;

    @Override
    public void fromBytes(ByteBuf buffer) {
        this.system = ByteBufUtils.readUTF8String(buffer);
        this.investiture = ByteBufUtils.readUTF8String(buffer);
        this.activationLevel = ActivationLevel.values()[buffer.readInt()];
    }

    @Override
    public  void toBytes(ByteBuf buffer) {
        ByteBufUtils.writeUTF8String(buffer, this.system);
        ByteBufUtils.writeUTF8String(buffer, this.investiture);
        buffer.writeInt(ActivationLevel.index.get(this.activationLevel));
    }

    public InvestitureActivationChangedPacket() {

    }

    public InvestitureActivationChangedPacket(SpiritwebInvestiture investiture) {
        this.system = investiture.investiture.system.name;
        this.investiture = investiture.investiture.name;
        this.activationLevel = investiture.activationLevel;
    }

    public InvestitureActivationChangedPacket(String system, String investiture, ActivationLevel level) {
        this.system = system;
        this.investiture = investiture;
        this.activationLevel = level;
    }

    public static class Handler extends PacketHandler<InvestitureActivationChangedPacket> {
        @Override
        protected void handleMessage(InvestitureActivationChangedPacket message, MessageContext context) {
            EntityPlayer player = context.getServerHandler().player;
            PlayerSpiritweb spiritweb = PlayerSpiritweb.getPlayerSpiritWeb(player);
            Investiture investiture = InvestitureRegistry.getInvestiture(message.system, message.investiture);

            String activateString = message.activationLevel == ActivationLevel.NONE ? "Deactivating " : "Activating ";
            String investitureString = investiture.system.name + "." + investiture.name;

            player.sendStatusMessage(new TextComponentString(activateString + investitureString), false);
            spiritweb.setActivationLevel(investiture, message.activationLevel);
        }
    }
}
