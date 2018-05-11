package kalia.cosmine.utils;

public class DrawUtils {
    public static float[] getGaugeColour(float normalisedValue) {
        return new float[] {
            0.75f * (1.0f - normalisedValue),
            0.75f * normalisedValue,
            0
        };
    }
}
