package kalia.cosmine.investiture;

//This class represents the prototype of a specific investiture (e.g. Iron Allomancy, Copper Feruchemy, Surge of Transportation)
//There will be one instance of this class for each investiture, with each instance passing in its own IInvestitureEffects class to handle the effects
public final class Investiture {
    public InvestitureSystem system;
    public String name;
    public float savantThreshold; //Minutes of usage
    public IInvestitureEffects effects;

    public Investiture(InvestitureSystem system, String name, float savantThreshold, IInvestitureEffects effects) {
        this.system = system;
        this.name = name;
        this.savantThreshold = savantThreshold;
        this.effects = effects;
    }
}
