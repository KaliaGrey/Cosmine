package kalia.cosmine.investiture.allomancy;

import kalia.cosmine.Cosmine;
import kalia.cosmine.capability.ISpiritweb;
import kalia.cosmine.investiture.Investiture;
import kalia.cosmine.network.allomancy.InherentAllomancyPacket;
import kalia.cosmine.registry.InvestitureRegistry;
import net.minecraft.command.ICommandSender;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.common.util.INBTSerializable;
import org.apache.logging.log4j.Level;

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
                Cosmine.log(Level.DEBUG, String.format("Updated %s's %s InherentAllomancySource", this.spiritweb.getIdentity(), investiture.fullName));
            }
            else {
                this.remove(investiture);
                Cosmine.log(Level.DEBUG, String.format("Removed %s's %s InherentAllomancySource", this.spiritweb.getIdentity(), investiture.fullName));
            }
        }
        else {
            inherentAllomancy = new InherentAllomancySource(investiture, intensity);
            this.set(inherentAllomancy);
            Cosmine.log(Level.DEBUG, String.format("Created %s's %s InherentAllomancySource", this.spiritweb.getIdentity(), investiture.fullName));
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
        Investiture investiture = InvestitureRegistry.getInvestiture(packet.investiture);
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
            Investiture investiture = InvestitureRegistry.getInvestiture(inherentAllomancyNBT.getString("investiture"));
            InherentAllomancySource inherentAllomancy = this.items.get(investiture);

            if (inherentAllomancy != null) {
                inherentAllomancy.deserializeNBT(inherentAllomancyNBT);
            }
            else {
                this.items.put(investiture, new InherentAllomancySource(inherentAllomancyNBT));
            }
        }
    }

    public void printDebugInformation(ICommandSender sender) {
        for (InherentAllomancySource inherentAllomancy : this.items.values()) {
            sender.sendMessage(new TextComponentString(String.format(
                "Inherent Allomancy: %s (%s)",
                inherentAllomancy.getInvestiture().fullName,
                inherentAllomancy.getIntensity()
            )));
        }
    }
}
