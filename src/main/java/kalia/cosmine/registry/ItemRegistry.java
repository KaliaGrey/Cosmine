package kalia.cosmine.registry;

import kalia.cosmine.Cosmine;
import kalia.cosmine.creativetab.CosmineCreativeTab;
import kalia.cosmine.item.LerasiumNugget;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.registries.IForgeRegistry;

@Mod.EventBusSubscriber
public class ItemRegistry {
    public static final CreativeTabs COSMINE_CREATIVE_TAB = new CosmineCreativeTab(CreativeTabs.getNextID(), Cosmine.MOD_ID);

    public static final LerasiumNugget LERASIUM_NUGGET = new LerasiumNugget();

    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> event) {
        IForgeRegistry<Item> registry = event.getRegistry();

        registry.register(LERASIUM_NUGGET);
    }

    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public static void registerModels(ModelRegistryEvent event) {
        LERASIUM_NUGGET.registerModel();
    }
}
