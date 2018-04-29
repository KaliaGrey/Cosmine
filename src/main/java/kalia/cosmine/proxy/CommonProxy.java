package kalia.cosmine.proxy;

import kalia.cosmine.Cosmine;
import kalia.cosmine.capability.CapabilityHandler;
import kalia.cosmine.capability.PlayerSpiritweb;
import kalia.cosmine.capability.PlayerSpiritwebProvider;
import kalia.cosmine.config.Config;
import kalia.cosmine.network.NetworkHandler;
import kalia.cosmine.registry.CapabilityRegistry;
import kalia.cosmine.registry.InvestitureRegistry;
import kalia.cosmine.registry.ItemRegistry;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

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
        CapabilityRegistry.register(CapabilityManager.INSTANCE);
        MinecraftForge.EVENT_BUS.register(new CapabilityHandler());

        InvestitureRegistry.register();
    }

    public void postInit(FMLPostInitializationEvent event) {
        if (config.hasChanged()) {
            config.save();
        }
    }

    @SubscribeEvent
    public static void registerBlocks(RegistryEvent.Register<Block> event) {
        //BlockRegistry.register(event);
    }

    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> event) {
        ItemRegistry.register(event);
    }

    @SubscribeEvent
    public void onWorldTick(TickEvent.WorldTickEvent event) {
        if (event.phase == TickEvent.Phase.END) {
            for (EntityPlayer player : event.world.playerEntities) {
                PlayerSpiritweb.getPlayerSpiritWeb(player).onWorldTick(event);
            }
        }
    }
}
