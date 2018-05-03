package kalia.cosmine.investiture.allomancy;

import kalia.cosmine.investiture.IInvestitureEffects;
import kalia.cosmine.investiture.ActivationLevel;
import kalia.cosmine.investiture.SpiritwebInvestiture;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.PotionEffect;

public class AllomancyTinEffects implements IInvestitureEffects {
    public void applyEffectsToEntity(Entity entity, SpiritwebInvestiture spiritwebInvestiture) {
        if (entity instanceof EntityLivingBase) {
            EntityLivingBase ent = (EntityLivingBase)entity;
            if (spiritwebInvestiture.getActivationLevel() != ActivationLevel.NONE) {
                ent.addPotionEffect(new PotionEffect(MobEffects.NIGHT_VISION, Short.MAX_VALUE, 5, true, false));
                ent.removePotionEffect(MobEffects.BLINDNESS);
            } else {
                PotionEffect nightVision = ent.getActivePotionEffect(MobEffects.NIGHT_VISION);
                if (nightVision != null && nightVision.getAmplifier() == 5) {
                    ent.removePotionEffect(MobEffects.NIGHT_VISION);
                }
            }
        }
    }

    public void applySavantEffectsToEntity(Entity entity, SpiritwebInvestiture investiture) {
        if (entity instanceof EntityLivingBase) {
            if (investiture.getActivationLevel() == ActivationLevel.NONE) {
                ((EntityLivingBase) entity).addPotionEffect(new PotionEffect(MobEffects.BLINDNESS, 20));
            }
        }
    }
}
