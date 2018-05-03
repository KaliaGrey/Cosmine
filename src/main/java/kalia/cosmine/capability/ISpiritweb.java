package kalia.cosmine.capability;

import kalia.cosmine.investiture.*;
import kalia.cosmine.investiture.allomancy.InherentAllomancySource;
import kalia.cosmine.network.allomancy.InherentAllomancyPacket;
import kalia.cosmine.network.playerspiritweb.BurstingStatusPacket;
import kalia.cosmine.network.playerspiritweb.InherentIdentityIntensityPacket;
import kalia.cosmine.network.playerspiritweb.SpiritwebInvestiturePacket;
import kalia.cosmine.network.playerspiritweb.client.ClientInvestitureActivationPacket;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.INBTSerializable;

import java.util.ArrayList;

public interface ISpiritweb extends IIdentitySource, INBTSerializable<NBTTagCompound> {
    boolean hasIdentity(String identity);
    boolean isBursting();

    void setInherentInvestiture(Investiture investiture, float intensity);
    InherentAllomancySource getInherentAllomancy(Investiture investiture);
    void sendInherentAllomancyPacket(InherentAllomancySource inherentAllomancy);

    ArrayList<IInvestitureSource> getInvestitureSources(Investiture investiture);

    void setActivationLevel(Investiture investiture, ActivationLevel level);
    SpiritwebInvestiture getSpiritwebInvestiture(Investiture investiture);
    void sendSpiritwebInvestiturePacket(SpiritwebInvestiture spiritwebInvestiture);

    void synchronize(PlayerSpiritweb source);

    void onTick();

    void onInherentIdentityIntensityPacket(InherentIdentityIntensityPacket packet);
    void onSpiritwebInvestiturePacket(SpiritwebInvestiturePacket packet);
    void onInherentAllomancyPacket(InherentAllomancyPacket packet);
    void onBurstingStatusPacket(BurstingStatusPacket packet);

    void onClientInvestitureActivationPacket(ClientInvestitureActivationPacket packet);
}
