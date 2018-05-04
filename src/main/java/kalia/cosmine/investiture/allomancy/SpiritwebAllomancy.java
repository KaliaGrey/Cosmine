package kalia.cosmine.investiture.allomancy;

import kalia.cosmine.capability.ISpiritweb;
import kalia.cosmine.investiture.IInvestitureSource;
import kalia.cosmine.investiture.Investiture;
import kalia.cosmine.investiture.SpiritwebInvestiture;
import net.minecraft.nbt.NBTTagCompound;

import java.util.ArrayList;

public class SpiritwebAllomancy extends SpiritwebInvestiture {
    private boolean compounding;

    public SpiritwebAllomancy(ISpiritweb spiritweb, Investiture investiture) {
        super(spiritweb, investiture);
        //Todo: Add parameter for AllomanticReserveSet from spiritweb
    }

    public SpiritwebAllomancy(ISpiritweb spiritweb, NBTTagCompound nbt) {
        super(spiritweb, nbt);
    }

    public void setCompounding(boolean compounding) {
        this.compounding = compounding;
    }

    public boolean isCompounding() {
        return this.compounding;
    }

    public boolean canUse() {
        //Todo: Also check metal reserves when implemented
        ArrayList<IInvestitureSource> sources = this.spiritweb.getInvestitureSources(this.investiture);

        if (sources.size() > 0) {
            for (IInvestitureSource source : sources) {
                if (source.getIntensity() > 0) {
                    return true;
                }
            }
        }

        return false;
    }

    public void synchronize(SpiritwebInvestiture source) {
        this.compounding = ((SpiritwebAllomancy)source).compounding;
        super.synchronize(source);
    }

    public NBTTagCompound serializeNBT() {
        NBTTagCompound nbt = super.serializeNBT();

        nbt.setBoolean("compounding", this.compounding);

        return nbt;
    }

    public void deserializeNBT(NBTTagCompound nbt) {
        super.deserializeNBT(nbt);
        if (this.investiture.name.equals(nbt.getString("investiture"))) {
            this.compounding = nbt.getBoolean("compounding");
        }
    }
}
