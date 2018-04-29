package kalia.cosmine.capability;

import kalia.cosmine.Cosmine;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class CapabilityHandler {
    public static final ResourceLocation SPIRITWEB = new ResourceLocation(Cosmine.MOD_ID, "spiritweb");

    @SubscribeEvent
    public void attachCapability(AttachCapabilitiesEvent<Entity> event) {
        if (event.getObject() instanceof EntityPlayer) {
            event.addCapability(SPIRITWEB, new PlayerSpiritwebProvider((EntityPlayer)event.getObject()));
        }
    }
}
