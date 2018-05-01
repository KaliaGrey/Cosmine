package kalia.cosmine.investiture.allomancy;

import kalia.cosmine.capability.ISpiritweb;
import kalia.cosmine.investiture.Investiture;
import kalia.cosmine.investiture.SpiritwebInvestiture;
import net.minecraft.nbt.NBTTagCompound;

public class SpiritwebAllomancy extends SpiritwebInvestiture {
    public SpiritwebAllomancy(ISpiritweb spiritweb, Investiture investiture) {
        super(spiritweb, investiture);
        //Todo: Add parameter for AllomanticReserveSet from spiritweb
    }

    public SpiritwebAllomancy(ISpiritweb spiritweb, NBTTagCompound nbt) {
        super(spiritweb, nbt);
    }
}
