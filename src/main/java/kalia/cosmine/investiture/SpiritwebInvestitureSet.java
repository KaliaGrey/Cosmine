package kalia.cosmine.investiture;

import kalia.cosmine.Cosmine;
import kalia.cosmine.capability.ISpiritweb;
import kalia.cosmine.investiture.allomancy.InherentAllomancySource;
import kalia.cosmine.network.playerspiritweb.SpiritwebInvestiturePacket;
import kalia.cosmine.registry.InvestitureRegistry;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.common.util.INBTSerializable;
import org.apache.logging.log4j.Level;

import java.util.HashMap;

public class SpiritwebInvestitureSet implements INBTSerializable<NBTTagList> {
    private ISpiritweb spiritweb;
    private HashMap<Investiture, SpiritwebInvestiture> items;

    public SpiritwebInvestitureSet(ISpiritweb spiritweb) {
        this.spiritweb = spiritweb;
        this.items = new HashMap<Investiture, SpiritwebInvestiture>();
    }

    public void set(SpiritwebInvestiture spiritwebInvestiture) {
        SpiritwebInvestiture existing = this.items.get(spiritwebInvestiture.investiture);
        if (existing != null) {
            existing.synchronize(spiritwebInvestiture);
        }
        else {
            this.items.put(spiritwebInvestiture.investiture, spiritwebInvestiture);
        }
    }

    public SpiritwebInvestiture get(Investiture investiture) {
        return this.items.get(investiture);
    }

    public void remove(Investiture investiture) {
        items.remove(investiture);
    }

    public void applyInvestituresToEntity(EntityPlayer player) {
        for (SpiritwebInvestiture spiritwebInvestiture : this.items.values()) {
            spiritwebInvestiture.applyInvestitureToEntity(player);
        }
    }

    public void onSpiritwebInvestiturePacket(SpiritwebInvestiturePacket packet) {
        Investiture investiture = InvestitureRegistry.getInvestiture(packet.investiture);
        SpiritwebInvestiture spiritwebInvestiture = this.items.get(investiture);

        if (spiritwebInvestiture != null) {
            spiritwebInvestiture.deserializeNBT(packet.nbt);
            Cosmine.log(Level.DEBUG, String.format("Updated %s's %s SpiritwebInvestiture", this.spiritweb.getIdentity(), investiture.fullName));
        }
        else {
            this.items.put(investiture, investiture.system.createSpiritwebInvestiture(this.spiritweb, packet.nbt));
            Cosmine.log(Level.DEBUG, String.format("Created %s's %s SpiritwebInvestiture", this.spiritweb.getIdentity(), investiture.fullName));
        }
    }


    public void synchronize(SpiritwebInvestitureSet source) {
        items.clear(); //Todo: Ensure this is safe

        for(SpiritwebInvestiture sourceItem : source.items.values()) {
            this.set(sourceItem);
        }
    }

    public NBTTagList serializeNBT() {
        NBTTagList nbt = new NBTTagList();
        for (SpiritwebInvestiture spiritwebInvestiture : this.items.values()) {
            nbt.appendTag(spiritwebInvestiture.serializeNBT());
        }

        return nbt;
    }

    public void deserializeNBT(NBTTagList nbt) {
        for (NBTBase itemNBT : nbt) {
            NBTTagCompound spiritwebInvestitureNBT = (NBTTagCompound)itemNBT;
            Investiture investiture = InvestitureRegistry.getInvestiture(spiritwebInvestitureNBT.getString("investiture"));
            SpiritwebInvestiture spiritwebInvestiture = this.items.get(investiture);

            if (spiritwebInvestiture != null) {
                spiritwebInvestiture.deserializeNBT(spiritwebInvestitureNBT);
            }
            else {
                this.items.put(investiture, investiture.system.createSpiritwebInvestiture(this.spiritweb, spiritwebInvestitureNBT));
            }
        }
    }

    public void printDebugInformation(ICommandSender sender) {
        for (SpiritwebInvestiture spiritwebInvestiture : this.items.values()) {
            sender.sendMessage(new TextComponentString(String.format(
                    "Investiture: %s (%s) - %s",
                    spiritwebInvestiture.getInvestiture().fullName,
                    spiritwebInvestiture.getEffectiveIntensity(),
                    spiritwebInvestiture.activationLevel.toString()
            )));
        }
    }
}
