package kalia.cosmine.registry;

import kalia.cosmine.capability.ISpiritweb;
import kalia.cosmine.capability.PlayerSpiritweb;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;

public class CapabilityRegistry {
    @CapabilityInject(ISpiritweb.class)
    public static Capability<ISpiritweb> SPIRITWEB = null;

    public static void register(CapabilityManager capabilityManager) {
        capabilityManager.register(ISpiritweb.class, new PlayerSpiritweb.Storage(), () -> new PlayerSpiritweb());
    }
}
