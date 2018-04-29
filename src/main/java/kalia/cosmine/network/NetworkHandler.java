package kalia.cosmine.network;

import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

public class NetworkHandler {
    private static int packetID = 0;

    public static SimpleNetworkWrapper INSTANCE = null;

    public NetworkHandler() {

    }

    public static int nextID() {
        return packetID++;
    }

    public static void registerChannel(String channelName) {
        INSTANCE = NetworkRegistry.INSTANCE.newSimpleChannel(channelName);
    }

    public static void registerMessages() {
        INSTANCE.registerMessage(KeyPressedPacket.Handler.class, KeyPressedPacket.class, nextID(), Side.SERVER);
        INSTANCE.registerMessage(InvestitureActivationChangedPacket.Handler.class, InvestitureActivationChangedPacket.class, nextID(), Side.SERVER);
    }
}
