package kalia.cosmine.investiture;

import java.util.HashMap;

public enum ActivationLevel {
    NONE,
    LOW,
    MEDIUM,
    HIGH;

    public static HashMap<ActivationLevel, Integer> index;

    public static int toIndex(ActivationLevel activationLevel) {
        return ActivationLevel.index.get(activationLevel);
    }

    public static ActivationLevel fromIndex(int index) {
        return ActivationLevel.values()[index];
    }

    static {
        index = new HashMap<ActivationLevel, Integer>();

        int i = 0;
        for(ActivationLevel level : ActivationLevel.values()) {
            index.put(level, i);
            i++;
        }
    }
}
