package kalia.cosmine;

import kalia.cosmine.command.InherentInvestitureCommand;
import kalia.cosmine.proxy.CommonProxy;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Logger;

@Mod(modid = Cosmine.MOD_ID, name = Cosmine.MOD_NAME, version = Cosmine.MOD_VERSION, /*dependencies = "required-after:Forge@[14.23.3.2655,)",*/ useMetadata = true)
public class Cosmine {
    public static final String MOD_ID = "cosmine";
    public static final String MOD_NAME = "Cosmine";
    public static final String MOD_VERSION = "0.0-a1"; //Major.Minor(-Alpha/Beta#)
    public static final String ROOT_PACKAGE = "kalia.cosmine";

    @SidedProxy(clientSide = Cosmine.ROOT_PACKAGE + ".proxy.ClientProxy", serverSide = Cosmine.ROOT_PACKAGE + ".proxy.ServerProxy")
    public static CommonProxy proxy;

    @Mod.Instance
    public static Cosmine instance;

    public static Logger logger;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        logger = event.getModLog();
        proxy.preInit(event);
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        proxy.init(event);
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        proxy.postInit(event);
    }

    @Mod.EventHandler
    public void serverLoad(FMLServerStartingEvent event) {
        event.registerServerCommand(new InherentInvestitureCommand());
    }

    public static void log(Level level, String content) {
        logger.log(Level.INFO, content); //Overriding log level since Forge doesn't show Debug-level logs
    }
}
