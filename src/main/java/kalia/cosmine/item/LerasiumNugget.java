package kalia.cosmine.item;

import kalia.cosmine.Cosmine;
import kalia.cosmine.capability.PlayerSpiritweb;
import kalia.cosmine.investiture.Investiture;
import kalia.cosmine.investiture.allomancy.Allomancy;
import kalia.cosmine.investiture.allomancy.InherentAllomancySource;
import kalia.cosmine.registry.ItemRegistry;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.*;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;

public class LerasiumNugget extends ItemFood {
    public static final String REGISTRY_NAME = "nuggetlerasium";
    
    private static final Investiture[] ALLOMANCIES = new Investiture[]{
        Allomancy.STEEL, Allomancy.IRON, Allomancy.PEWTER, Allomancy.TIN,
        Allomancy.ZINC, Allomancy.BRASS, Allomancy.COPPER, Allomancy.BRONZE,
        Allomancy.DURALUMIN, Allomancy.ALUMINUM, Allomancy.NICROSIL, Allomancy.CHROMIUM,
        Allomancy.GOLD, Allomancy.ELECTRUM, Allomancy.CADMIUM, Allomancy.BENDALLOY
    };

    public LerasiumNugget() {
        super(0, false);
        setRegistryName(REGISTRY_NAME);
        setUnlocalizedName(Cosmine.MOD_ID + "." + REGISTRY_NAME);
        setAlwaysEdible();
        setHasSubtypes(true);
        setMaxDamage(0);
        setCreativeTab(ItemRegistry.COSMINE_CREATIVE_TAB);
        this.maxStackSize = 1;
    }

    public void registerModel() {
        ModelLoader.setCustomModelResourceLocation(this, 0, new ModelResourceLocation(getRegistryName(), "inventory"));

        for(int i = 0; i < 16; i++) {
            Investiture allomancy = ALLOMANCIES[i];
            ModelLoader.setCustomModelResourceLocation(this, i + 1, new ModelResourceLocation(getRegistryName() + "." + allomancy.name, "inventory"));
        }
    }

    @Override
    public EnumAction getItemUseAction(ItemStack itemStack) {
        return EnumAction.EAT;
    }

    @Override
    public int getMaxItemUseDuration(ItemStack itemStack) {
        return 1;
    }

    @Override
    public EnumRarity getRarity(ItemStack itemStack) {
        return EnumRarity.EPIC;
    }

    @Override
    public boolean hasEffect(ItemStack itemStack) {
        return itemStack.getMetadata() == 0; //Only give pure lerasium the shiny effect
    }

    public String getUnlocalizedName(ItemStack stack) {
        int metalIndex = stack.getMetadata() - 1;

        if (metalIndex < 0) {
            return super.getUnlocalizedName() + ".pure";
        }
        else {
            Investiture allomancy = ALLOMANCIES[metalIndex];
            return super.getUnlocalizedName() + "." + allomancy.name;
        }
    }

    public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items) {
        if (this.isInCreativeTab(tab))
        {
            //Pure lerasium, then one item for each of the 16 metals
            for (int i = 0; i < 17; ++i)
            {
                items.add(new ItemStack(this, 1, i));
            }
        }
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand){
        ItemStack stack = player.getHeldItem(hand);

        if (canPlayerUse(player, stack)) {
            player.setActiveHand(hand);
            return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, stack);
        }
        else {
            return new ActionResult<ItemStack>(EnumActionResult.FAIL, stack);
        }
    }

    @Override
    public ItemStack onItemUseFinish(ItemStack stack, World worldIn, EntityLivingBase entityLiving) {
        if (entityLiving instanceof EntityPlayerMP) {
            PlayerSpiritweb spiritweb = PlayerSpiritweb.getPlayerSpiritWeb((EntityPlayerMP)entityLiving);
            int metalIndex = stack.getMetadata() - 1;

            if (metalIndex < 0) {
                for (Investiture investiture : ALLOMANCIES) {
                    spiritweb.setInherentInvestiture(investiture, 1.0f);
                }
            }
            else {
                spiritweb.setInherentInvestiture(ALLOMANCIES[metalIndex], 1.0f);
            }
        }

        return super.onItemUseFinish(stack, worldIn, entityLiving);
    }

    private static boolean canPlayerUse(EntityPlayer player, ItemStack stack) {
        PlayerSpiritweb spiritweb = PlayerSpiritweb.getPlayerSpiritWeb(player);
        int metalIndex = stack.getMetadata() - 1;

        if (metalIndex < 0) {
            for (Investiture investiture : ALLOMANCIES) {
                InherentAllomancySource allomancy = spiritweb.getInherentAllomancy(investiture);
                if (allomancy == null || allomancy.getIntensity() < 1) {
                    return true;
                }
            }
        }
        else {
            Investiture investiture = ALLOMANCIES[metalIndex];
            InherentAllomancySource allomancy = spiritweb.getInherentAllomancy(investiture);
            return allomancy == null || allomancy.getIntensity() < 1;
        }

        return false;
    }
}
