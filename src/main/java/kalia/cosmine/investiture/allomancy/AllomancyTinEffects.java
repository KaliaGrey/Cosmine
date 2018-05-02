package kalia.cosmine.investiture.allomancy;

import kalia.cosmine.investiture.IInvestitureEffects;
import kalia.cosmine.investiture.ActivationLevel;
import kalia.cosmine.investiture.SpiritwebInvestiture;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.PotionEffect;

public class AllomancyTinEffects implements IInvestitureEffects {
    public void applyEffectsToEntity(Entity entity, SpiritwebInvestiture investiture) {
        if (entity instanceof EntityLivingBase) {
            ((EntityLivingBase) entity).addPotionEffect(new PotionEffect(MobEffects.NIGHT_VISION, 2));
        }
    }

    public void applySavantEffectsToEntity(Entity entity, SpiritwebInvestiture investiture) {
        if (entity instanceof EntityLivingBase) {
            if (investiture.getActivationLevel() != ActivationLevel.NONE) {
                ((EntityLivingBase) entity).addPotionEffect(new PotionEffect(MobEffects.NIGHT_VISION, 2));
            }
            else {
                ((EntityLivingBase) entity).addPotionEffect(new PotionEffect(MobEffects.BLINDNESS, 2));
            }
        }
    }
}
