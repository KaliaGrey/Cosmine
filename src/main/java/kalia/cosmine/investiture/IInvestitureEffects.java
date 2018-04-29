package kalia.cosmine.investiture;

import net.minecraft.entity.Entity;

//This interface represents a class that implements the effects for a specific investiture and is bound to a single Investiture class instance
public interface IInvestitureEffects {
    void applyEffectsToEntity(Entity entity, SpiritwebInvestiture investiture);
    void applySavantEffectsToEntity(Entity entity, SpiritwebInvestiture investiture);

    //void applyPowerToBlock()
}
