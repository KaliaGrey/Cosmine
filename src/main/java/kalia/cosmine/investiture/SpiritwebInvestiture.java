package kalia.cosmine.investiture;

import kalia.cosmine.capability.ISpiritweb;
import kalia.cosmine.registry.InvestitureRegistry;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.fml.common.gameevent.TickEvent;

//This class represents the total effective power of a specific investiture available on a spiritweb, whether derived from one source or multiple
public abstract class SpiritwebInvestiture implements INBTSerializable<NBTTagCompound> {
    public ISpiritweb spiritweb;
    public Investiture investiture;
    public long totalActivationTicks;
    public ActivationLevel activationLevel;

    public SpiritwebInvestiture(ISpiritweb spiritweb, Investiture investiture) {
        this.spiritweb = spiritweb;
        this.investiture = investiture;
        this.totalActivationTicks = 0;
        this.activationLevel = ActivationLevel.NONE;
    }

    public SpiritwebInvestiture(ISpiritweb spiritweb, NBTTagCompound nbt) {
        this.spiritweb = spiritweb;
        this.investiture = InvestitureRegistry.INVESTITURES.get(nbt.getString("investiture"));
        this.deserializeNBT(nbt);
    }

    public float getEffectiveIntensity() {
        float sum = 0;

        for(IInvestitureSource source : this.spiritweb.getInvestitureSources(this.investiture)) {
            sum += source.getIntensity();
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

    public NBTTagCompound serializeNBT() {
        NBTTagCompound nbt = new NBTTagCompound();

        nbt.setString("investiture", this.investiture.name);
        nbt.setLong("totalActivationTicks", this.totalActivationTicks);
        nbt.setInteger("activationLevel", ActivationLevel.toIndex(this.activationLevel));

        return nbt;
    }

    public void deserializeNBT(NBTTagCompound nbt) {
        if (this.investiture.name.equals(nbt.getString("investiture"))) {
            this.totalActivationTicks = nbt.getLong("totalActivationTicks");
            this.activationLevel = ActivationLevel.fromIndex(nbt.getInteger("activationLevel"));
        }
    }
}
