package kalia.cosmine.config;

import kalia.cosmine.proxy.CommonProxy;
import net.minecraftforge.common.config.Configuration;

public class Config {
    private static final String CATEGORY_GENERAL = "general";
    private static final String CATEGORY_BALANCE = "balance";

    public static void read() {
        Configuration config = CommonProxy.config;
        try {
            config.load();
            //initGeneralCategory(config);
            //initBalanceCategory(config);
        } catch (Exception e) {
            //Log!
        } finally {
            if (config.hasChanged()) {
                config.save();
            }
        }
    }
}
