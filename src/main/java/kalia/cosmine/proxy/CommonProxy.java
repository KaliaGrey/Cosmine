package kalia.cosmine.proxy;

import kalia.cosmine.Cosmine;
import kalia.cosmine.capability.PlayerSpiritweb;
import kalia.cosmine.config.Config;
import kalia.cosmine.network.NetworkHandler;
import kalia.cosmine.network.playerspiritweb.PlayerSpiritwebPacket;
import kalia.cosmine.registry.CapabilityRegistry;
import kalia.cosmine.registry.InvestitureRegistry;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.io.File;

@Mod.EventBusSubscriber
public class CommonProxy {
    public static Configuration config;

    public void preInit(FMLPreInitializationEvent event) {
        File directory = event.getModConfigurationDirectory();
        config = new Configuration(new File(directory.getPath(), "cosmine.cfg"));
        Config.read();

        NetworkHandler.registerChannel(Cosmine.MOD_ID);
        NetworkHandler.registerMessages();
    }

    public void init(FMLInitializationEvent event) {
        MinecraftForge.EVENT_BUS.register(this);

        CapabilityRegistry.register(CapabilityManager.INSTANCE);

        InvestitureRegistry.register();
    }

    public void postInit(FMLPostInitializationEvent event) {
        if (config.hasChanged()) {
            config.save();
        }
    }

    @SubscribeEvent
    public static void onPlayerClone(PlayerEvent.Clone event) {
        if (event.getEntity() instanceof EntityPlayerMP) {
            EntityPlayer oldPlayer = event.getOriginal();
            PlayerSpiritweb oldSpiritweb = PlayerSpiritweb.getPlayerSpiritWeb(oldPlayer);

            EntityPlayer newPlayer = event.getEntityPlayer();
            PlayerSpiritweb newSpiritweb = PlayerSpiritweb.getPlayerSpiritWeb(newPlayer);

            if (event.isWasDeath() && !newPlayer.world.getGameRules().getBoolean("keepInventory")) {
                //Todo: On death (without keepinventory), remove metal reserves
            }

            newSpiritweb.synchronize(oldSpiritweb);
            NetworkHandler.INSTANCE.sendTo(new PlayerSpiritwebPacket(newPlayer.getEntityId(), oldSpiritweb), (EntityPlayerMP) newPlayer);
        }
    }

    @SubscribeEvent
    public static void onPlayerLogin(EntityJoinWorldEvent event) {
        if (event.getEntity() instanceof EntityPlayerMP) {
            EntityPlayerMP player = (EntityPlayerMP)event.getEntity();
            PlayerSpiritweb spiritweb = PlayerSpiritweb.getPlayerSpiritWeb(player);

            NetworkHandler.INSTANCE.sendTo(new PlayerSpiritwebPacket(player.getEntityId(), spiritweb), player);
        }
    }

    @SubscribeEvent
    public void onPlayerUpdate(PlayerEvent.LivingUpdateEvent event) {
        if (event.getEntity() instanceof EntityPlayer) {
            PlayerSpiritweb.getPlayerSpiritWeb((EntityPlayer)event.getEntity()).onTick();
        }
    }
}
