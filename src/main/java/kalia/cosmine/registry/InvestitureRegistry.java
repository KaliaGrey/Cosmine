package kalia.cosmine.registry;

import kalia.cosmine.investiture.InvestitureSystem;
import kalia.cosmine.investiture.allomancy.AllomancySystem;
import kalia.cosmine.investiture.Investiture;

import java.util.HashMap;

public class InvestitureRegistry {
    public static HashMap<String, InvestitureSystem> SYSTEMS;
    public static HashMap<String, Investiture> INVESTITURES;

    public static AllomancySystem ALLOMANCY;

    public static void register() {
        SYSTEMS = new HashMap<String, InvestitureSystem>();
        INVESTITURES = new HashMap<String, Investiture>();

        ALLOMANCY = (AllomancySystem)registerSystem(new AllomancySystem());
    }

    public static InvestitureSystem registerSystem(InvestitureSystem system) {
        SYSTEMS.put(system.name, system);
        return system;
    }

    public static Investiture registerInvestiture(Investiture investiture) {
        INVESTITURES.put(investiture.name, investiture);
        return investiture;
    }
}
