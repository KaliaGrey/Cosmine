package kalia.cosmine.investiture.allomancy;

import kalia.cosmine.investiture.Investiture;
import kalia.cosmine.investiture.InvestitureSystem;

public class AllomancySystem extends InvestitureSystem {
    public static Investiture TIN = null;

    public AllomancySystem() {
        super("allomancy");

        TIN = registerInvestiture(new Investiture(this,"tin", 300, new AllomancyTinEffects()));
    }
}
