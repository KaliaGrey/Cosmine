package kalia.cosmine.investiture.allomancy;

import kalia.cosmine.investiture.IInvestitureSource;
import kalia.cosmine.investiture.Investiture;

//This class represents an inherent generic investiture source for Allomancy.
public class InherentAllomancySource implements IInvestitureSource {
    private Investiture investiture;
    private float intensity;

    public InherentAllomancySource(Investiture investiture, float intensity) {
        this.investiture = investiture;
        this.intensity = intensity;
    }

    public Investiture getInvestiture() {
        return this.investiture;
    }

    public float getInvestitureIntensity() {
        return this.intensity;
    }
}
