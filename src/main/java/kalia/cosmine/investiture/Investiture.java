package kalia.cosmine.investiture;

import kalia.cosmine.Cosmine;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;

//This class represents the prototype of a specific investiture (e.g. Iron Allomancy, Copper Feruchemy, Surge of Transportation)
//There will be one instance of this class for each investiture, with each instance passing in its own IInvestitureEffects class to handle the effects
public final class Investiture {
    public InvestitureSystem system;
    public String name;
    public String fullName;

    public String localizedName;
    public ResourceLocation symbol;

    public float savantThreshold; //Minutes of usage
    public IInvestitureEffects effects;

    public Investiture(InvestitureSystem system, String name, float savantThreshold, IInvestitureEffects effects) {
        this.system = system;
        this.name = name;
        this.fullName = system.name + ":" + name;
        this.localizedName = I18n.format(String.format("investiture.%s.%s", system.name, name));
        this.symbol = new ResourceLocation(String.format("%s:textures/gui/%s/symbol%s.png", Cosmine.MOD_ID, system.name, name));

        this.savantThreshold = savantThreshold;
        this.effects = effects;
    }
}
