package kalia.cosmine.capability;

import kalia.cosmine.investiture.*;
import kalia.cosmine.investiture.allomancy.InherentAllomancySource;
import kalia.cosmine.network.allomancy.InherentAllomancyPacket;
import kalia.cosmine.network.playerspiritweb.BurstingStatusPacket;
import kalia.cosmine.network.playerspiritweb.InherentIdentityIntensityPacket;
import kalia.cosmine.network.playerspiritweb.SpiritwebInvestiturePacket;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.ArrayList;

public interface ISpiritweb extends IIdentitySource, INBTSerializable<NBTTagCompound> {
    boolean hasIdentity(String identity);
    boolean isBursting();

    void setInherentInvestiture(Investiture investiture, float intensity);
    InherentAllomancySource getInherentAllomancy(Investiture investiture);

    ArrayList<IInvestitureSource> getInvestitureSources(Investiture investiture);

    SpiritwebInvestiture getSpiritwebInvestiture(Investiture investiture);
    void setActivationLevel(Investiture investiture, ActivationLevel level);


    void onWorldTick(TickEvent.WorldTickEvent event);

    void onInherentIdentityIntensityPacket(InherentIdentityIntensityPacket packet);
    void onSpiritwebInvestiturePacket(SpiritwebInvestiturePacket packet);
    void onInherentAllomancyPacket(InherentAllomancyPacket packet);
    void onBurstingStatusPacket(BurstingStatusPacket packet);
}
