package kalia.cosmine.investiture.allomancy;

import kalia.cosmine.investiture.IInvestitureSource;
import kalia.cosmine.investiture.Investiture;
import kalia.cosmine.registry.InvestitureRegistry;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.INBTSerializable;

//This class represents an inherent generic investiture source for Allomancy.
public class InherentAllomancySource implements IInvestitureSource, INBTSerializable<NBTTagCompound> {
    private Investiture investiture;
    private float intensity;

    public InherentAllomancySource(Investiture investiture, float intensity) {
        this.investiture = investiture;
        this.intensity = intensity;
    }

    public InherentAllomancySource(NBTTagCompound nbt) {
        this.investiture = InvestitureRegistry.getInvestiture(nbt.getString("investiture"));
        this.deserializeNBT(nbt);
    }

    public Investiture getInvestiture() {
        return this.investiture;
    }

    public void setIntensity(float intensity) {
        this.intensity = intensity;
    }

    public float getIntensity() {
        return this.intensity;
    }

    public void synchronize(InherentAllomancySource source) {
        this.intensity = source.intensity;
    }

    public NBTTagCompound serializeNBT() {
        NBTTagCompound nbt = new NBTTagCompound();

        nbt.setString("investiture", this.investiture.fullName);
        nbt.setFloat("intensity", this.intensity);

        return nbt;
    }

    public void deserializeNBT(NBTTagCompound nbt) {
        if (this.investiture.fullName.equals(nbt.getString("investiture"))) {
            this.intensity = nbt.getFloat("intensity");
        }
    }
}
