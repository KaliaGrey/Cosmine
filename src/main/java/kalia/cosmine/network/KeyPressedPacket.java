package kalia.cosmine.network;

import io.netty.buffer.ByteBuf;
import kalia.cosmine.capability.PlayerSpiritweb;
import kalia.cosmine.capability.PlayerSpiritwebProvider;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class KeyPressedPacket implements IMessage {
    private int key;

    @Override
    public void fromBytes(ByteBuf buffer) {
        this.key = buffer.readInt();
    }

    @Override
    public  void toBytes(ByteBuf buffer) {
        buffer.writeInt(this.key);
    }

    public KeyPressedPacket() {
        this(-1);
    }

    public  KeyPressedPacket(int key) {
        this.key = key;
    }

    public static class Handler extends PacketHandler<KeyPressedPacket> {
        @Override
        protected void handleMessage(KeyPressedPacket message, MessageContext context) {
            EntityPlayerMP player = context.getServerHandler().player;
            PlayerSpiritweb spiritweb = (PlayerSpiritweb)player.getCapability(PlayerSpiritwebProvider.SPIRITWEB, null);

            String hasSpiritweb = spiritweb != null ? " Has spiritweb!" : " No spiritweb..";
            String inherentIdentity = spiritweb != null ? spiritweb.getIdentity() : " No identity..";

            player.sendStatusMessage(new TextComponentString("Test key pressed!" + hasSpiritweb + inherentIdentity), false);
        }
    }
}
