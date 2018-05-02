package kalia.cosmine.investiture.allomancy;

import kalia.cosmine.Cosmine;
import kalia.cosmine.capability.ISpiritweb;
import kalia.cosmine.investiture.Investiture;
import kalia.cosmine.network.allomancy.InherentAllomancyPacket;
import kalia.cosmine.registry.InvestitureRegistry;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.common.util.INBTSerializable;

import java.util.HashMap;

public class InherentAllomancySet implements INBTSerializable<NBTTagList> {
    private ISpiritweb spiritweb;
    private HashMap<Investiture, InherentAllomancySource> items;

    public InherentAllomancySet(ISpiritweb spiritweb) {
        this.spiritweb = spiritweb;
        this.items = new HashMap<Investiture, InherentAllomancySource>();
    }

    public void set(InherentAllomancySource inherentAllomancy) {
        InherentAllomancySource existing = this.items.get(inherentAllomancy.getInvestiture());
        if (existing != null) {
            existing.synchronize(inherentAllomancy);
        }
        else {
            this.items.put(inherentAllomancy.getInvestiture(), inherentAllomancy);
        }
    }

    public InherentAllomancySource setIntensity(Investiture investiture, float intensity) {
        InherentAllomancySource inherentAllomancy = this.items.get(investiture);

        if (inherentAllomancy != null) {
            inherentAllomancy.setIntensity(intensity);
            if (inherentAllomancy.getIntensity() > 0) {
                Cosmine.logger.debug(String.format("Updated %s%n's %s%n InherentAllomancySource", this.spiritweb.getIdentity(), investiture.name));
            }
            else {
                this.remove(investiture);
                Cosmine.logger.debug(String.format("Removed %s%n's %s%n InherentAllomancySource", this.spiritweb.getIdentity(), investiture.name));
            }
        }
        else {
            inherentAllomancy = new InherentAllomancySource(investiture, intensity);
            this.set(inherentAllomancy);
            Cosmine.logger.debug(String.format("Created %s%n's %s%n InherentAllomancySource", this.spiritweb.getIdentity(), investiture.name));
        }

        return inherentAllomancy;
    }

    public InherentAllomancySource get(Investiture investiture) {
        return this.items.get(investiture);
    }

    public void remove(Investiture investiture) {
        items.remove(investiture);
    }

    public void onInherentAllomancyPacket(InherentAllomancyPacket packet) {
        Investiture investiture = InvestitureRegistry.INVESTITURES.get(packet.investiture);
        setIntensity(investiture, packet.nbt.getFloat("intensity"));
    }

    public void synchronize(InherentAllomancySet source) {
        items.clear(); //Todo: Ensure this is safe

        for(InherentAllomancySource sourceItem : source.items.values()) {
            this.set(sourceItem);
        }
    }

    public NBTTagList serializeNBT() {
        NBTTagList nbt = new NBTTagList();
        for (InherentAllomancySource inherentAllomancy : this.items.values()) {
            nbt.appendTag(inherentAllomancy.serializeNBT());
        }

        return nbt;
    }

    public void deserializeNBT(NBTTagList nbt) {
        for (NBTBase itemNBT : nbt) {
            NBTTagCompound inherentAllomancyNBT = (NBTTagCompound)itemNBT;
            Investiture investiture = InvestitureRegistry.INVESTITURES.get(inherentAllomancyNBT.getString("investiture"));
            InherentAllomancySource inherentAllomancy = this.items.get(investiture);

            if (inherentAllomancy != null) {
                inherentAllomancy.deserializeNBT(inherentAllomancyNBT);
            }
            else {
                this.items.put(investiture, new InherentAllomancySource(inherentAllomancyNBT));
            }
        }
    }
}
