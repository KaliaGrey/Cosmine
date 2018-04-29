package kalia.cosmine.investiture;

import kalia.cosmine.capability.PlayerSpiritweb;
import net.minecraft.entity.Entity;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.ArrayList;

//This class represents the total effective power of a specific investiture available on a spiritweb, whether derived from one source or multiple
public abstract class SpiritwebInvestiture {
    public Investiture investiture;
    public ArrayList<IInvestitureSource> sources;
    public long totalActivationTicks;
    public ActivationLevel activationLevel;

    public SpiritwebInvestiture(Investiture investiture, ArrayList<IInvestitureSource> sources) {
        this.investiture = investiture;
        this.sources = sources;
        this.totalActivationTicks = 0;
        this.activationLevel = ActivationLevel.NONE;
    }

    public float getEffectiveIntensity() {
        float sum = 0;

        for(IInvestitureSource source : this.sources) {
            sum += source.getInvestitureIntensity();
        }

        return sum;
    }

    public void applyInvestitureToEntity(TickEvent.WorldTickEvent event, Entity entity) {
        if (this.activationLevel != ActivationLevel.NONE) {
            this.investiture.effects.applyEffectsToEntity(entity, this);
        }

        if (this.totalActivationTicks > (this.investiture.savantThreshold * 1200)) {
            this.investiture.effects.applySavantEffectsToEntity(entity, this);
        }

        this.totalActivationTicks++;
    }
}
