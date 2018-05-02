package kalia.cosmine.network;

import kalia.cosmine.network.allomancy.InherentAllomancyPacket;
import kalia.cosmine.network.playerspiritweb.BurstingStatusPacket;
import kalia.cosmine.network.playerspiritweb.PlayerSpiritwebPacket;
import kalia.cosmine.network.playerspiritweb.SpiritwebInvestiturePacket;
import kalia.cosmine.network.playerspiritweb.client.ClientInvestitureActivationPacket;
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
        INSTANCE.registerMessage(PlayerSpiritwebPacket.Handler.class,  PlayerSpiritwebPacket.class, nextID(), Side.CLIENT);
        INSTANCE.registerMessage(SpiritwebInvestiturePacket.Handler.class,  SpiritwebInvestiturePacket.class, nextID(), Side.CLIENT);
        INSTANCE.registerMessage(InherentAllomancyPacket.Handler.class,  InherentAllomancyPacket.class, nextID(), Side.CLIENT);
        INSTANCE.registerMessage(BurstingStatusPacket.Handler.class,  BurstingStatusPacket.class, nextID(), Side.CLIENT);

        INSTANCE.registerMessage(ClientInvestitureActivationPacket.Handler.class, ClientInvestitureActivationPacket.class, nextID(), Side.SERVER);
    }
}
