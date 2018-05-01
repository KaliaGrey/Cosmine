package kalia.cosmine.input;

import kalia.cosmine.capability.PlayerSpiritweb;
import kalia.cosmine.investiture.ActivationLevel;
import kalia.cosmine.investiture.Investiture;
import kalia.cosmine.investiture.allomancy.AllomancySystem;
import kalia.cosmine.network.NetworkHandler;
import kalia.cosmine.registry.KeyBindingRegistry;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class InputHandler {
    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public void onKeyInput(InputEvent.KeyInputEvent event) {
        if (KeyBindingRegistry.TEST_KEY.isPressed()) {
            PlayerSpiritweb spiritweb = PlayerSpiritweb.getPlayerSpiritWeb(Minecraft.getMinecraft().player);
            Investiture tinAllomancy = AllomancySystem.TIN;

            boolean activate = spiritweb.getSpiritwebInvestiture(tinAllomancy).activationLevel == ActivationLevel.NONE;

            /*NetworkHandler.INSTANCE.sendToServer(
                new InvestitureActivationPacket(tinAllomancy.system.name, tinAllomancy.name, activate ? ActivationLevel.MEDIUM : ActivationLevel.NONE)
            );*/
        }
    }
}
