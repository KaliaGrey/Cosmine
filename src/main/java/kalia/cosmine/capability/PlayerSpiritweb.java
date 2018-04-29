package kalia.cosmine.capability;

import kalia.cosmine.investiture.*;
import kalia.cosmine.investiture.allomancy.InherentAllomancySource;
import kalia.cosmine.investiture.allomancy.SpiritwebAllomancy;
import kalia.cosmine.registry.InvestitureRegistry;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTBase;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.Callable;

public class PlayerSpiritweb implements ISpiritweb {
    private EntityPlayer player;
    private ArrayList<IIdentitySource> appliedIdentities;

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
        this.appliedIdentities = new ArrayList<IIdentitySource>();

        this.spiritwebInvestitures = new HashMap<Investiture, SpiritwebInvestiture>();

        this.inherentAllomancySources = new HashMap<Investiture, InherentAllomancySource>();

        //Todo: JUST A TEST! REMOVE LATER!
        addInherentAllomancy(new InherentAllomancySource(InvestitureRegistry.getInvestiture("allomancy", "tin"), 1.0f));
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
        return 1.0f; //Todo: Allow this to be decreased by Al-minds
    }

    public boolean hasIdentity(String identity) {
        if (identity.equals(this.getIdentity()) && this.getIdentityIntensity() > 0) {
            return true;
        }
        else {
            for (IIdentitySource source : this.appliedIdentities) {
                if (identity.equals(source.getIdentity()) && source.getIdentityIntensity() > 0) {
                    return true;
                }
            }
            return false;
        }
    }

    public void addInherentAllomancy(InherentAllomancySource source) {
        Investiture investiture = source.getInvestiture();
        if (!this.inherentAllomancySources.containsKey(investiture)) {
            this.inherentAllomancySources.put(investiture, source);
        }

        if (!this.spiritwebInvestitures.containsKey(investiture)) {
            ArrayList<IInvestitureSource> sources = new ArrayList<IInvestitureSource>();
            sources.add(source);

            this.spiritwebInvestitures.put(investiture, new SpiritwebAllomancy(investiture, sources));
        }
    }

    public InherentAllomancySource getInherentAllomancy(Investiture investiture) {
        return this.inherentAllomancySources.get(investiture);
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
    }

    public static class Storage implements Capability.IStorage<ISpiritweb> {
        public static final Storage INSTANCE = new Storage();

        @Override
        public NBTBase writeNBT(Capability<ISpiritweb> capability, ISpiritweb instance, EnumFacing side) {
            //Todo: Write InherentAllomancySources

            return null;
        }

        @Override
        public void readNBT(Capability<ISpiritweb> capability, ISpiritweb instance, EnumFacing side, NBTBase nbt) {
            //Todo: Read InherentAllomancySources
        }
    }

    public static class Factory implements Callable<PlayerSpiritweb> {
        public PlayerSpiritweb call() throws Exception {
            return new PlayerSpiritweb();
        }
    }
}
