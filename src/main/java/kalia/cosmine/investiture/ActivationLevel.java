package kalia.cosmine.investiture;

import java.util.HashMap;

public enum ActivationLevel {
    NONE,
    LOW,
    MEDIUM,
    HIGH;

    public static HashMap<ActivationLevel, Integer> index;

    static {
        index = new HashMap<ActivationLevel, Integer>();

        int i = 0;
        for(ActivationLevel level : ActivationLevel.values()) {
            index.put(level, i);
            i++;
        }
    }
}
