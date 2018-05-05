package kalia.cosmine.item;

import kalia.cosmine.Cosmine;
import kalia.cosmine.capability.PlayerSpiritweb;
import kalia.cosmine.investiture.Investiture;
import kalia.cosmine.investiture.allomancy.Allomancy;
import kalia.cosmine.investiture.allomancy.InherentAllomancySource;
import kalia.cosmine.registry.ItemRegistry;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.EnumAction;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;

public class LerasiumNugget extends ItemFood {
    public static final String REGISTRY_NAME = "nuggetlerasium";

    public LerasiumNugget() {
        super(0, false);
        setRegistryName(REGISTRY_NAME);
        setUnlocalizedName(Cosmine.MOD_ID + "." + REGISTRY_NAME);
        setAlwaysEdible();
        setHasSubtypes(false);
        setCreativeTab(ItemRegistry.COSMINE_CREATIVE_TAB);

        this.maxStackSize = 1;
    }

    public void registerModel() {
        ModelLoader.setCustomModelResourceLocation(this, 0, new ModelResourceLocation(getRegistryName(), "inventory"));
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
    public EnumRarity getRarity(ItemStack itemStack)
    {
        return EnumRarity.EPIC;
    }

    @Override
    public boolean hasEffect(ItemStack itemStack) {
        return true;
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand){
        if (canPlayerUse(player)) {
            player.setActiveHand(hand);
            return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, player.getHeldItem(hand));
        } else {
            return new ActionResult<ItemStack>(EnumActionResult.FAIL, player.getHeldItem(hand));
        }
    }

    @Override
    public ItemStack onItemUseFinish(ItemStack stack, World worldIn, EntityLivingBase entityLiving){
        if (entityLiving instanceof EntityPlayerMP) {
            PlayerSpiritweb spiritweb = PlayerSpiritweb.getPlayerSpiritWeb((EntityPlayerMP)entityLiving);
            spiritweb.setInherentInvestiture(Allomancy.PEWTER, 1.0f);
            spiritweb.setInherentInvestiture(Allomancy.TIN, 1.0f);
            //Todo: Add other allomancies when implemented
        }

        return super.onItemUseFinish(stack, worldIn, entityLiving);
    }

    private static boolean canPlayerUse(EntityPlayer player) {
        PlayerSpiritweb spiritweb = PlayerSpiritweb.getPlayerSpiritWeb(player);

        for (Investiture investiture : (new Investiture[]{
                Allomancy.PEWTER,
                Allomancy.TIN
                //Todo: Add other allomancies when implemented
        })) {
            InherentAllomancySource allomancy = spiritweb.getInherentAllomancy(investiture);
            if (allomancy == null || allomancy.getIntensity() < 1) {
                return true;
            }
        }

        return false;
    }
}
