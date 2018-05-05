package kalia.cosmine.registry;

import kalia.cosmine.investiture.InvestitureSystem;
import kalia.cosmine.investiture.allomancy.Allomancy;
import kalia.cosmine.investiture.Investiture;

import java.util.HashMap;

public class InvestitureRegistry {
    private static HashMap<String, InvestitureSystem> systems;
    private static HashMap<String, Investiture> investitures;

    public static Allomancy ALLOMANCY;

    public static void register() {
        systems = new HashMap<String, InvestitureSystem>();
        investitures = new HashMap<String, Investiture>();

        ALLOMANCY = (Allomancy)registerSystem(new Allomancy());
    }

    public static InvestitureSystem registerSystem(InvestitureSystem system) {
        systems.put(system.name, system);
        return system;
    }

    public static Investiture registerInvestiture(Investiture investiture) {
        investitures.put(investiture.name, investiture);
        return investiture;
    }

    public static InvestitureSystem getSystem(String name) {
        return systems.get(name);
    }

    public static Investiture getInvestiture(String name) {
        return investitures.get(name);
    }
}
