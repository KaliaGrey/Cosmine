package kalia.cosmine.capability;

import kalia.cosmine.Cosmine;
import kalia.cosmine.investiture.*;
import kalia.cosmine.investiture.allomancy.AllomancySystem;
import kalia.cosmine.investiture.allomancy.InherentAllomancySet;
import kalia.cosmine.investiture.allomancy.InherentAllomancySource;
import kalia.cosmine.investiture.allomancy.SpiritwebAllomancy;
import kalia.cosmine.network.NetworkHandler;
import kalia.cosmine.network.allomancy.InherentAllomancyPacket;
import kalia.cosmine.network.playerspiritweb.BurstingStatusPacket;
import kalia.cosmine.network.playerspiritweb.InherentIdentityIntensityPacket;
import kalia.cosmine.network.playerspiritweb.SpiritwebInvestiturePacket;
import kalia.cosmine.network.playerspiritweb.client.ClientInvestitureActivationPacket;
import kalia.cosmine.registry.InvestitureRegistry;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.ArrayList;
import java.util.concurrent.Callable;

public class PlayerSpiritweb implements ISpiritweb {
    private EntityPlayer player;
    private float inherentIdentityIntensity;
    private boolean bursting;

    private SpiritwebInvestitureSet spiritwebInvestitures;

    private InherentAllomancySet inherentAllomancies;

    public static PlayerSpiritweb getPlayerSpiritWeb(EntityPlayer player) {
        return (PlayerSpiritweb)player.getCapability(PlayerSpiritwebProvider.SPIRITWEB, null);
    }

    public PlayerSpiritweb() {
        this(null);
    }

    public PlayerSpiritweb(EntityPlayer player) {
        this.player = player;
        this.inherentIdentityIntensity = 1.0f;

        this.spiritwebInvestitures = new SpiritwebInvestitureSet(this);

        this.inherentAllomancies = new InherentAllomancySet(this);
    }

    //region Inherent Identity

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

    //endregion

    //region Duralumin bursting

    public boolean isBursting() {
        return this.bursting;
    }

    //endregion

    //region Inherent investitures

    public void setInherentInvestiture(Investiture investiture, float intensity) {
        SpiritwebInvestiture newSpiritwebInvestiture = null;

        switch (investiture.system.name) {
            case AllomancySystem.NAME:
                boolean createSpiritwebInvestiture = this.inherentAllomancies.get(investiture) == null;
                sendInherentAllomancyPacket(this.inherentAllomancies.setIntensity(investiture, intensity));

                if (createSpiritwebInvestiture) {
                    newSpiritwebInvestiture = new SpiritwebAllomancy(this, investiture);
                }
                break;
        }

        if (newSpiritwebInvestiture != null) {
            this.spiritwebInvestitures.set(newSpiritwebInvestiture);
            this.sendSpiritwebInvestiturePacket(newSpiritwebInvestiture);
        }
    }

    public InherentAllomancySource getInherentAllomancy(Investiture investiture) {
        return this.inherentAllomancies.get(investiture);
    }

    public void sendInherentAllomancyPacket(InherentAllomancySource inherentAllomancy) {
        if (!this.player.world.isRemote) {
            NetworkHandler.INSTANCE.sendTo(new InherentAllomancyPacket(this.player.getEntityId(), inherentAllomancy), (EntityPlayerMP)this.player);
        }
    }

    //endregion

    //region Investiture sources

    public ArrayList<IInvestitureSource> getInvestitureSources(Investiture investiture) {
        ArrayList<IInvestitureSource> sources = new ArrayList<IInvestitureSource>();

        if (investiture.system == InvestitureRegistry.ALLOMANCY) {
            InherentAllomancySource inherentAllomancy = this.inherentAllomancies.get(investiture);
            if (inherentAllomancy != null) {
                sources.add(inherentAllomancy);
            }
        }

        return sources;
    }

    //endregion

    //region Spiritweb investitures

    public void setActivationLevel(Investiture investiture, ActivationLevel level) {
        SpiritwebInvestiture spiritwebInvestiture = this.getSpiritwebInvestiture(investiture);
        spiritwebInvestiture.setActivationLevel(level);
        sendSpiritwebInvestiturePacket(spiritwebInvestiture);
    }

    public SpiritwebInvestiture getSpiritwebInvestiture(Investiture investiture) {
        return this.spiritwebInvestitures.get(investiture);
    }

    public void sendSpiritwebInvestiturePacket(SpiritwebInvestiture spiritwebInvestiture) {
        if (!this.player.world.isRemote) {
            NetworkHandler.INSTANCE.sendTo(new SpiritwebInvestiturePacket(this.player.getEntityId(), spiritwebInvestiture), (EntityPlayerMP)this.player);
        }
    }

    //endregion

    //region Event handlers

    public void onWorldTick(TickEvent.WorldTickEvent event) {
        this.spiritwebInvestitures.applyInvestituresToEntity(event, this.player);

        //Todo: Check for deactivate bursting
    }

    public void onInherentIdentityIntensityPacket(InherentIdentityIntensityPacket packet) {
        this.inherentIdentityIntensity = packet.intensity;
        Cosmine.logger.debug(String.format("Updated %s%n's inherent Identity intensity to %s%n", this.getIdentity(), this.getIdentityIntensity()));
    }

    public void onSpiritwebInvestiturePacket(SpiritwebInvestiturePacket packet) {
        this.spiritwebInvestitures.onSpiritwebInvestiturePacket(packet);
    }

    public void onInherentAllomancyPacket(InherentAllomancyPacket packet) {
        this.inherentAllomancies.onInherentAllomancyPacket(packet);
    }

    public void onBurstingStatusPacket(BurstingStatusPacket packet) {
        this.bursting = packet.bursting;
        Cosmine.logger.debug(String.format("Updated %s%n's bursting status to %s%n", this.getIdentity(), this.getIdentityIntensity()));
    }

    public void onClientInvestitureActivationPacket(ClientInvestitureActivationPacket packet) {
        this.setActivationLevel(InvestitureRegistry.INVESTITURES.get(packet.investiture), packet.level);
    }

    //endregion

    //region (De)serialization

    public void synchronize(PlayerSpiritweb source) {
        this.player = source.player;
        this.inherentIdentityIntensity = source.inherentIdentityIntensity;
        this.bursting = source.bursting;

        this.spiritwebInvestitures.synchronize(source.spiritwebInvestitures);

        //Todo: Synchronise inherent allomancies
    }

    public NBTTagCompound serializeNBT() {
        NBTTagCompound nbt = new NBTTagCompound();

        nbt.setString("inherentIdentity", this.getIdentity());
        nbt.setFloat("inherentIdentityIntensity", this.getIdentityIntensity());
        nbt.setBoolean("bursting", this.isBursting());

        nbt.setTag("spiritwebInvestitures", this.spiritwebInvestitures.serializeNBT());

        nbt.setTag("inherentAllomancies", this.inherentAllomancies.serializeNBT());

        return nbt;
    }

    public void deserializeNBT(NBTTagCompound nbt) {
        if (this.getIdentity().equals(nbt.getString("identity"))) {
            this.inherentIdentityIntensity = nbt.getFloat("inherentIdentityIntensity");
            this.bursting = nbt.getBoolean("bursting");

            this.spiritwebInvestitures.deserializeNBT(nbt.getTagList("spiritwebInvestitures", Constants.NBT.TAG_COMPOUND));

            this.inherentAllomancies.deserializeNBT(nbt.getTagList("inherentAllomancies", Constants.NBT.TAG_COMPOUND));
        }
    }

    //endregion


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
