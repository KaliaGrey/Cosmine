package kalia.cosmine.creativetab;

import kalia.cosmine.Cosmine;
import kalia.cosmine.registry.ItemRegistry;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;

public class CosmineCreativeTab extends CreativeTabs {
    public CosmineCreativeTab(int id, String modID) {
        super(id, modID);
    }

    @Override
    public String getTabLabel() {
        return Cosmine.MOD_ID;
    }

    @Override
    public ItemStack getIconItemStack() {
        return new ItemStack(ItemRegistry.LERASIUM_NUGGET, 1);
    }

    @Override
    public ItemStack getTabIconItem() {
        return null;
    }
}
