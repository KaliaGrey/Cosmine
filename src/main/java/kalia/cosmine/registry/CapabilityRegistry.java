package kalia.cosmine.registry;

import kalia.cosmine.Cosmine;
import kalia.cosmine.capability.ISpiritweb;
import kalia.cosmine.capability.PlayerSpiritweb;
import kalia.cosmine.capability.PlayerSpiritwebProvider;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber
public class CapabilityRegistry {
    @CapabilityInject(ISpiritweb.class)
    public static Capability<ISpiritweb> SPIRITWEB = null;
    public static final ResourceLocation SPIRITWEB_RESOURCE = new ResourceLocation(Cosmine.MOD_ID, "spiritweb");

    public static void register(CapabilityManager capabilityManager) {
        capabilityManager.register(ISpiritweb.class, new PlayerSpiritweb.Storage(), () -> new PlayerSpiritweb());
    }

    @SubscribeEvent
    public static void attachCapability(AttachCapabilitiesEvent<Entity> event) {
        if (event.getObject() instanceof EntityPlayer) {
            event.addCapability(SPIRITWEB_RESOURCE, new PlayerSpiritwebProvider((EntityPlayer)event.getObject()));
        }
    }
}
