package kalia.cosmine.investiture.allomancy;

import kalia.cosmine.capability.ISpiritweb;
import kalia.cosmine.investiture.Investiture;
import kalia.cosmine.investiture.InvestitureSystem;
import kalia.cosmine.registry.InvestitureRegistry;
import net.minecraft.nbt.NBTTagCompound;

public class Allomancy extends InvestitureSystem {
    public static final String NAME = "allomancy";

    //Physical metals
    public static Investiture STEEL = null;
    public static Investiture IRON = null;
    public static Investiture PEWTER = null;
    public static Investiture TIN = null;

    //Mental metals
    public static Investiture ZINC = null;
    public static Investiture BRASS = null;
    public static Investiture COPPER = null;
    public static Investiture BRONZE = null;

    //Enhancement metals
    public static Investiture DURALUMIN = null;
    public static Investiture ALUMINUM = null;
    public static Investiture NICROSIL = null;
    public static Investiture CHROMIUM = null;

    //Temporal metals
    public static Investiture GOLD = null;
    public static Investiture ELECTRUM = null;
    public static Investiture CADMIUM = null;
    public static Investiture BENDALLOY = null;

    public Allomancy() {
        super(NAME);

        //Todo: Placeholders. Replace these with proper implementations
        STEEL = registerInvestiture(new Investiture(this, "steel", 999, null));
        IRON = registerInvestiture(new Investiture(this, "iron", 999, null));

        PEWTER = registerInvestiture(new Investiture(this, "pewter", 500, new AllomancyPewterEffects()));
        TIN = registerInvestiture(new Investiture(this, "tin", 300, new AllomancyTinEffects()));

        //Todo: Placeholders. Replace these with proper implementations
        ZINC = registerInvestiture(new Investiture(this, "zinc", 999, null));
        BRASS = registerInvestiture(new Investiture(this, "brass", 999, null));
        COPPER = registerInvestiture(new Investiture(this, "copper", 999, null));
        BRONZE = registerInvestiture(new Investiture(this, "bronze", 999, null));

        DURALUMIN = registerInvestiture(new Investiture(this, "duralumin", 999, null));
        ALUMINUM = registerInvestiture(new Investiture(this, "aluminum", 999, null));
        NICROSIL = registerInvestiture(new Investiture(this, "nicrosil", 999, null));
        CHROMIUM = registerInvestiture(new Investiture(this, "chromium", 999, null));

        GOLD = registerInvestiture(new Investiture(this, "gold", 999, null));
        ELECTRUM = registerInvestiture(new Investiture(this, "electrum", 999, null));
        CADMIUM = registerInvestiture(new Investiture(this, "cadmium", 999, null));
        BENDALLOY = registerInvestiture(new Investiture(this, "bendalloy", 999, null));
    }

    public SpiritwebAllomancy createSpiritwebInvestiture(ISpiritweb spiritweb, NBTTagCompound nbt) {
        return new SpiritwebAllomancy(spiritweb, nbt);
    }

    public static Investiture getInvestiture(String name) {
        return InvestitureRegistry.getInvestiture(NAME + ":" + name);
    }
}
