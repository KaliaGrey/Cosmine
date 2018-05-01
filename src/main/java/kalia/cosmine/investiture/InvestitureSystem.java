package kalia.cosmine.investiture;

import kalia.cosmine.capability.ISpiritweb;
import kalia.cosmine.registry.InvestitureRegistry;
import net.minecraft.nbt.NBTTagCompound;

import java.util.HashMap;

public abstract class InvestitureSystem {
    public String name;
    public HashMap<String, Investiture> investitures;

    public InvestitureSystem(String name) {
        this.name = name;
        this.investitures = new HashMap<String, Investiture>();
    }

    public Investiture getInvestiture(String name) {
        return this.investitures.get(name);
    }

    protected Investiture registerInvestiture(Investiture investiture) {
        this.investitures.put(investiture.name, investiture);
        InvestitureRegistry.registerInvestiture(investiture);
        return investiture;
    }

    public abstract SpiritwebInvestiture createSpiritwebInvestiture(ISpiritweb spiritweb, NBTTagCompound nbt);
}
