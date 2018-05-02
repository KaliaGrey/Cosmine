package kalia.cosmine.proxy;

import kalia.cosmine.Cosmine;
import kalia.cosmine.capability.CapabilityHandler;
import kalia.cosmine.capability.PlayerSpiritweb;
import kalia.cosmine.config.Config;
import kalia.cosmine.investiture.allomancy.AllomancySystem;
import kalia.cosmine.network.NetworkHandler;
import kalia.cosmine.network.playerspiritweb.PlayerSpiritwebPacket;
import kalia.cosmine.registry.CapabilityRegistry;
import kalia.cosmine.registry.InvestitureRegistry;
import kalia.cosmine.registry.ItemRegistry;
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
        MinecraftForge.EVENT_BUS.register(this);

        //MinecraftForge.EVENT_BUS.register(BlockRegistry.class);
        MinecraftForge.EVENT_BUS.register(ItemRegistry.class);

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
    public void onPlayerClone(PlayerEvent.Clone event) {
        EntityPlayer oldPlayer = event.getOriginal();
        PlayerSpiritweb oldSpiritweb = PlayerSpiritweb.getPlayerSpiritWeb(oldPlayer);

        EntityPlayer newPlayer = event.getEntityPlayer();
        PlayerSpiritweb newSpiritweb = PlayerSpiritweb.getPlayerSpiritWeb(newPlayer);

        if (event.isWasDeath() && !newPlayer.world.getGameRules().getBoolean("keepInventory")) {
            //Todo: On death (without keepinventory), remove metal reserves
        }

        newSpiritweb.synchronize(oldSpiritweb);
        NetworkHandler.INSTANCE.sendTo(new PlayerSpiritwebPacket(newPlayer.getEntityId(), oldSpiritweb), (EntityPlayerMP)newPlayer);
    }

    @SubscribeEvent
    public void onPlayerLogin(EntityJoinWorldEvent event) {
        if (event.getEntity() instanceof EntityPlayerMP) {
            EntityPlayerMP player = (EntityPlayerMP)event.getEntity();
            PlayerSpiritweb spiritweb = PlayerSpiritweb.getPlayerSpiritWeb(player);

            //Todo: Test Allomancy, Please Remove
            if (spiritweb.getInherentAllomancy(AllomancySystem.TIN) == null) {
                spiritweb.setInherentInvestiture(AllomancySystem.TIN, 1.0f);
            }

            NetworkHandler.INSTANCE.sendTo(new PlayerSpiritwebPacket(player.getEntityId(), spiritweb), player);
        }
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
