package kalia.cosmine.investiture;

import kalia.cosmine.capability.ISpiritweb;
import kalia.cosmine.registry.InvestitureRegistry;
import net.minecraft.nbt.NBTTagCompound;

import java.util.HashMap;

public abstract class InvestitureSystem {
    public String name;

    public InvestitureSystem(String name) {
        this.name = name;
    }

    protected Investiture registerInvestiture(Investiture investiture) {
        InvestitureRegistry.registerInvestiture(investiture);
        return investiture;
    }

    public abstract SpiritwebInvestiture createSpiritwebInvestiture(ISpiritweb spiritweb, NBTTagCompound nbt);
}
