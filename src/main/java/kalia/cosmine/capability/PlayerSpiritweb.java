package kalia.cosmine.capability;

import kalia.cosmine.Cosmine;
import kalia.cosmine.investiture.*;
import kalia.cosmine.investiture.allomancy.AllomancySystem;
import kalia.cosmine.investiture.allomancy.InherentAllomancySource;
import kalia.cosmine.investiture.allomancy.SpiritwebAllomancy;
import kalia.cosmine.network.allomancy.InherentAllomancyPacket;
import kalia.cosmine.network.playerspiritweb.BurstingStatusPacket;
import kalia.cosmine.network.playerspiritweb.InherentIdentityIntensityPacket;
import kalia.cosmine.network.playerspiritweb.SpiritwebInvestiturePacket;
import kalia.cosmine.registry.InvestitureRegistry;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.Callable;

public class PlayerSpiritweb implements ISpiritweb {
    private EntityPlayer player;
    private float inherentIdentityIntensity;
    private boolean bursting;

    private HashMap<Investiture, SpiritwebInvestiture> spiritwebInvestitures;

    private HashMap<Investiture, InherentAllomancySource> inherentAllomancySources;

    public static PlayerSpiritweb getPlayerSpiritWeb(EntityPlayer player) {
        return (PlayerSpiritweb)player.getCapability(PlayerSpiritwebProvider.SPIRITWEB, null);
    }

    public PlayerSpiritweb() {
        this(null);
    }

    public PlayerSpiritweb(EntityPlayer player) {
        this.player = player;
        this.inherentIdentityIntensity = 1.0f;

        this.spiritwebInvestitures = new HashMap<Investiture, SpiritwebInvestiture>();

        this.inherentAllomancySources = new HashMap<Investiture, InherentAllomancySource>();
    }

    public String getIdentity() {
        if (this.player != null) {
            return this.player.getName();
        }
        else {
            return "";
        }
    }

    public float getIdentityIntensity() {
        return this.inherentIdentityIntensity;
    }

    public boolean hasIdentity(String identity) {
        if (identity.equals(this.getIdentity()) && this.getIdentityIntensity() > 0) {
            return true;
        }
        /*else {
            for (IIdentitySource source : this.appliedIdentities) {
                if (identity.equals(source.getIdentity()) && source.getIdentityIntensity() > 0) {
                    return true;
                }
            }
            return false;
        }*/
        return false;
    }

    public boolean isBursting() {
        return this.bursting;
    }

    public void setInherentInvestiture(Investiture investiture, float intensity) {
        IInvestitureSource source = null;

        switch (investiture.system.name) {
            case AllomancySystem.NAME:
                if (this.inherentAllomancySources.containsKey(investiture)) {
                    if (intensity > 0) {
                        source = this.inherentAllomancySources.get(investiture);
                        source.setIntensity(intensity);
                    }
                    else {
                        this.inherentAllomancySources.remove(investiture);
                    }
                }
                else {
                    source = new InherentAllomancySource(investiture, intensity);
                    this.inherentAllomancySources.put(investiture, (InherentAllomancySource)source);
                }
                break;
        }

        if (source != null && !this.spiritwebInvestitures.containsKey(investiture)) {
            this.spiritwebInvestitures.put(investiture, new SpiritwebAllomancy(this, investiture));
        }
    }

    public InherentAllomancySource getInherentAllomancy(Investiture investiture) {
        return this.inherentAllomancySources.get(investiture);
    }

    public ArrayList<IInvestitureSource> getInvestitureSources(Investiture investiture) {
        ArrayList<IInvestitureSource> sources = new ArrayList<IInvestitureSource>();

        if (investiture.system == InvestitureRegistry.ALLOMANCY) {
            InherentAllomancySource inherentAllomancy = this.inherentAllomancySources.get(investiture);
            if (inherentAllomancy != null) {
                sources.add(inherentAllomancy);
            }
        }

        return sources;
    }

    public SpiritwebInvestiture getSpiritwebInvestiture(Investiture investiture) {
        return this.spiritwebInvestitures.get(investiture);
    }

    public void setActivationLevel(Investiture investiture, ActivationLevel level) {
        this.getSpiritwebInvestiture(investiture).activationLevel = level;
    }

    public void onWorldTick(TickEvent.WorldTickEvent event) {
        for (SpiritwebInvestiture spiritwebInvestiture : this.spiritwebInvestitures.values()) {
            spiritwebInvestiture.applyInvestitureToEntity(event, this.player);
        }

        //Todo: Check for deactivate bursting
    }

    public void onInherentIdentityIntensityPacket(InherentIdentityIntensityPacket packet) {
        this.inherentIdentityIntensity = packet.intensity;
        Cosmine.logger.debug(String.format("Updated %s%n's inherent Identity intensity to %s%n", this.getIdentity(), this.getIdentityIntensity()));
    }

    public void onSpiritwebInvestiturePacket(SpiritwebInvestiturePacket packet) {
        Investiture investiture = InvestitureRegistry.INVESTITURES.get(packet.investiture);
        SpiritwebInvestiture spiritwebInvestiture = this.spiritwebInvestitures.get(investiture);

        if (spiritwebInvestiture != null) {
            spiritwebInvestiture.deserializeNBT(packet.nbt);
            Cosmine.logger.debug(String.format("Updated %s%n's %s%n SpiritwebInvestiture", this.getIdentity(), investiture.name));
        }
        else {
            this.spiritwebInvestitures.put(investiture, investiture.system.createSpiritwebInvestiture(this, packet.nbt));
            Cosmine.logger.debug(String.format("Created %s%n's %s%n SpiritwebInvestiture", this.getIdentity(), investiture.name));
        }
    }

    public void onInherentAllomancyPacket(InherentAllomancyPacket packet) {
        Investiture investiture = InvestitureRegistry.INVESTITURES.get(packet.investiture);
        InherentAllomancySource inherentAllomancy = this.inherentAllomancySources.get(investiture);

        if (inherentAllomancy != null) {
            inherentAllomancy.deserializeNBT(packet.nbt);
            Cosmine.logger.debug(String.format("Updated %s%n's %s%n InherentAllomancySource", this.getIdentity(), investiture.name));
        }
        else {
            this.inherentAllomancySources.put(investiture, new InherentAllomancySource(packet.nbt));
            Cosmine.logger.debug(String.format("Created %s%n's %s%n InherentAllomancySource", this.getIdentity(), investiture.name));
        }
    }

    public void onBurstingStatusPacket(BurstingStatusPacket packet) {
        this.bursting = packet.bursting;
        Cosmine.logger.debug(String.format("Updated %s%n's bursting status to %s%n", this.getIdentity(), this.getIdentityIntensity()));
    }

    public NBTTagCompound serializeNBT() {
        NBTTagCompound nbt = new NBTTagCompound();

        nbt.setString("inherentIdentity", this.getIdentity());
        nbt.setFloat("inherentIdentityIntensity", this.getIdentityIntensity());
        nbt.setBoolean("bursting", this.isBursting());

        NBTTagList spiritwebInvestituresNBT = new NBTTagList();
        for (SpiritwebInvestiture spiritwebInvestiture : this.spiritwebInvestitures.values()) {
            spiritwebInvestituresNBT.appendTag(spiritwebInvestiture.serializeNBT());
        }
        nbt.setTag("spiritwebInvestitures", spiritwebInvestituresNBT);

        NBTTagList inherentAllomanciesNBT = new NBTTagList();
        for (InherentAllomancySource inherentAllomancy : this.inherentAllomancySources.values()) {
            inherentAllomanciesNBT.appendTag(inherentAllomancy.serializeNBT());
        }
        nbt.setTag("inherentAllomancies", inherentAllomanciesNBT);

        return nbt;
    }

    public void deserializeNBT(NBTTagCompound nbt) {
        if (this.getIdentity().equals(nbt.getString("identity"))) {
            this.inherentIdentityIntensity = nbt.getFloat("inherentIdentityIntensity");
            this.bursting = nbt.getBoolean("bursting");

            NBTTagList spiritwebInvestituresNBT = nbt.getTagList("spiritwebInvestitures", Constants.NBT.TAG_COMPOUND);
            for (NBTBase listNBT : spiritwebInvestituresNBT) {
                NBTTagCompound spiritwebInvestitureNBT = (NBTTagCompound)listNBT;
                Investiture investiture = InvestitureRegistry.INVESTITURES.get(spiritwebInvestitureNBT.getString("investiture"));
                SpiritwebInvestiture spiritwebInvestiture = this.spiritwebInvestitures.get(investiture);

                if (spiritwebInvestiture != null) {
                    spiritwebInvestiture.deserializeNBT(spiritwebInvestitureNBT);
                }
                else {
                    this.spiritwebInvestitures.put(investiture, investiture.system.createSpiritwebInvestiture(this, spiritwebInvestitureNBT));
                }
            }

            NBTTagList inherentAllomanciesNBT = nbt.getTagList("inherentAllomancies", Constants.NBT.TAG_COMPOUND);
            for (NBTBase listNBT : inherentAllomanciesNBT) {
                NBTTagCompound inherentAllomancyNBT = (NBTTagCompound)listNBT;
                Investiture investiture = InvestitureRegistry.INVESTITURES.get(inherentAllomancyNBT.getString("investiture"));
                InherentAllomancySource inherentAllomancy = this.inherentAllomancySources.get(investiture);

                if (inherentAllomancy != null) {
                    inherentAllomancy.deserializeNBT(inherentAllomancyNBT);
                }
                else {
                    this.inherentAllomancySources.put(investiture, new InherentAllomancySource(inherentAllomancyNBT));
                }
            }
        }
    }

    public static class Storage implements Capability.IStorage<ISpiritweb> {
        public static final Storage INSTANCE = new Storage();

        @Override
        public NBTBase writeNBT(Capability<ISpiritweb> capability, ISpiritweb instance, EnumFacing side) {
            return instance.serializeNBT();
        }

        @Override
        public void readNBT(Capability<ISpiritweb> capability, ISpiritweb instance, EnumFacing side, NBTBase nbt) {
            instance.deserializeNBT((NBTTagCompound)nbt);
        }
    }

    public static class Factory implements Callable<PlayerSpiritweb> {
        public PlayerSpiritweb call() throws Exception {
            return new PlayerSpiritweb();
        }
    }
}
