package kalia.cosmine.investiture.allomancy;

import kalia.cosmine.investiture.ActivationLevel;
import kalia.cosmine.investiture.IInvestitureEffects;
import kalia.cosmine.investiture.SpiritwebInvestiture;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.PotionEffect;

public class AllomancyPewterEffects implements IInvestitureEffects {
    private float getIntenstiyFactor(SpiritwebInvestiture spiritwebInvestiture, float multiplier) {
        return ActivationLevel.toIndex(spiritwebInvestiture.getActivationLevel()) * spiritwebInvestiture.getEffectiveIntensity() * (multiplier / 3f);
    }

    public void applyEffectsToEntity(Entity entity, SpiritwebInvestiture spiritwebInvestiture, ActivationLevel lastTickActivationLevel) {
        if (entity instanceof EntityLivingBase) {
            EntityLivingBase ent = (EntityLivingBase)entity;
            if (spiritwebInvestiture.getActivationLevel() != ActivationLevel.NONE) {
                //Max potion level for a full-intensity Allomancer = 5 (amplifier of 4)
                int amplifier = Math.min(Math.round(getIntenstiyFactor(spiritwebInvestiture, 4f)), 4) - 1;

                ent.addPotionEffect(new PotionEffect(MobEffects.STRENGTH, 2, amplifier, true, false));
                ent.addPotionEffect(new PotionEffect(MobEffects.RESISTANCE, 2, amplifier, true, false));
                ent.removePotionEffect(MobEffects.WEAKNESS);
            }
        }
    }

    public void applySavantEffectsToEntity(Entity entity, SpiritwebInvestiture spiritwebInvestiture, ActivationLevel lastTickActivationLevel) {
        if (entity instanceof EntityLivingBase) {
            EntityLivingBase ent = (EntityLivingBase)entity;
            if (spiritwebInvestiture.getActivationLevel() != ActivationLevel.NONE) {
                ent.addPotionEffect(new PotionEffect(MobEffects.REGENERATION, 2, 0, true, false));
            }
        }
    }
}
