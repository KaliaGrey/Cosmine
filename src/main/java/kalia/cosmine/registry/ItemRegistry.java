package kalia.cosmine.registry;

import net.minecraft.item.Item;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.registries.IForgeRegistry;

public class ItemRegistry {
    //@GameRegistry.ObjectHolder("cosmine:whateveritem")
    //public static WhateverItem whateverItem;

    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> event) {
        IForgeRegistry<Item> registry = event.getRegistry();

        //registry.register(new WhateverItem());
    }
}
