package kalia.cosmine.investiture.allomancy;

import kalia.cosmine.capability.ISpiritweb;
import kalia.cosmine.investiture.Investiture;
import kalia.cosmine.investiture.InvestitureSystem;
import net.minecraft.nbt.NBTTagCompound;

public class AllomancySystem extends InvestitureSystem {
    public static final String NAME = "allomancy";

    public static Investiture TIN = null;

    public AllomancySystem() {
        super(NAME);

        TIN = registerInvestiture(new Investiture(this,"tin", 300, new AllomancyTinEffects()));
    }

    public SpiritwebAllomancy createSpiritwebInvestiture(ISpiritweb spiritweb, NBTTagCompound nbt) {
        return new SpiritwebAllomancy(spiritweb, nbt);
    }
}
