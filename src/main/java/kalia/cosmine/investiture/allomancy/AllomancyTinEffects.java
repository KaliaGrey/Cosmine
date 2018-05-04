package kalia.cosmine.investiture.allomancy;

import kalia.cosmine.investiture.IInvestitureEffects;
import kalia.cosmine.investiture.ActivationLevel;
import kalia.cosmine.investiture.SpiritwebInvestiture;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.PotionEffect;

public class AllomancyTinEffects implements IInvestitureEffects {
    //Unfortunately we need to do some add/remove messing with the Night Vision effect, since night vision flickers and flashes when
    //rapidly applied with a short lifetime (a la pewter's buffs). We have to check if we have to remove the night vision effect
    //when the power is deactivated...

    public void applyEffectsToEntity(Entity entity, SpiritwebInvestiture spiritwebInvestiture, ActivationLevel lastTickActivationLevel) {
        if (entity instanceof EntityLivingBase) {
            EntityLivingBase entityLiving = (EntityLivingBase)entity;
            if (spiritwebInvestiture.getActivationLevel() != ActivationLevel.NONE) {
                if (lastTickActivationLevel == ActivationLevel.NONE) {
                    entityLiving.addPotionEffect(new PotionEffect(MobEffects.NIGHT_VISION, Short.MAX_VALUE, 5, true, false));
                }
                entityLiving.removePotionEffect(MobEffects.BLINDNESS);
            } else if (lastTickActivationLevel != ActivationLevel.NONE) {
                PotionEffect nightVision = entityLiving.getActivePotionEffect(MobEffects.NIGHT_VISION);
                if (nightVision != null && nightVision.getAmplifier() == 5) {
                    entityLiving.removePotionEffect(MobEffects.NIGHT_VISION);
                }
            }
        }
    }

    public void applySavantEffectsToEntity(Entity entity, SpiritwebInvestiture spiritwebInvestiture, ActivationLevel lastTickActivationLevel) {
        if (entity instanceof EntityLivingBase) {
            EntityLivingBase entityLiving = (EntityLivingBase)entity;
            if (spiritwebInvestiture.getActivationLevel() == ActivationLevel.NONE) {
                entityLiving.addPotionEffect(new PotionEffect(MobEffects.BLINDNESS, 2, 5, true, false));
            }
            else if (lastTickActivationLevel != ActivationLevel.NONE) {
                PotionEffect blindness = entityLiving.getActivePotionEffect(MobEffects.BLINDNESS);
                if (blindness != null && blindness.getAmplifier() == 5) {
                    entityLiving.removePotionEffect(MobEffects.BLINDNESS);
                }
            }
        }
    }
}
