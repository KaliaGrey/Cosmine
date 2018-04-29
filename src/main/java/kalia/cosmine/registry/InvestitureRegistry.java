package kalia.cosmine.registry;

import kalia.cosmine.investiture.InvestitureSystem;
import kalia.cosmine.investiture.allomancy.AllomancySystem;
import kalia.cosmine.investiture.Investiture;

import java.util.HashMap;

public class InvestitureRegistry {
    public static HashMap<String, InvestitureSystem> SYSTEMS;

    public static void register() {
        SYSTEMS = new HashMap<String, InvestitureSystem>();
        registerSystem(new AllomancySystem());
    }

    public static Investiture getInvestiture(String system, String investiture) {
        return SYSTEMS.get(system).getInvestiture(investiture);
    }

    private static void registerSystem(AllomancySystem system) {
        SYSTEMS.put(system.name, system);
    }
}
