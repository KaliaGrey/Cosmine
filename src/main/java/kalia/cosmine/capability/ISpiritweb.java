package kalia.cosmine.capability;

import kalia.cosmine.investiture.ActivationLevel;
import kalia.cosmine.investiture.SpiritwebInvestiture;
import kalia.cosmine.investiture.IIdentitySource;
import kalia.cosmine.investiture.Investiture;
import kalia.cosmine.investiture.allomancy.InherentAllomancySource;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public interface ISpiritweb extends IIdentitySource {
    boolean hasIdentity(String identity);

    void addInherentAllomancy(InherentAllomancySource source);
    InherentAllomancySource getInherentAllomancy(Investiture investiture);

    SpiritwebInvestiture getSpiritwebInvestiture(Investiture investiture);
    void setActivationLevel(Investiture investiture, ActivationLevel level);

    void onWorldTick(TickEvent.WorldTickEvent event);
}
