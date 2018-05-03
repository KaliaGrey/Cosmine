package kalia.cosmine.input;

import kalia.cosmine.capability.PlayerSpiritweb;
import kalia.cosmine.investiture.ActivationLevel;
import kalia.cosmine.investiture.Investiture;
import kalia.cosmine.investiture.allomancy.AllomancySystem;
import kalia.cosmine.network.NetworkHandler;
import kalia.cosmine.network.playerspiritweb.client.ClientInvestitureActivationPacket;
import kalia.cosmine.registry.KeyBindingRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.relauncher.Side;

@Mod.EventBusSubscriber(Side.CLIENT)
public class InputHandler {
    @SubscribeEvent
    public static void onKeyInput(InputEvent.KeyInputEvent event) {
        if (KeyBindingRegistry.TEST_KEY.isPressed()) {
            EntityPlayer player = Minecraft.getMinecraft().player;
            PlayerSpiritweb spiritweb = PlayerSpiritweb.getPlayerSpiritWeb(player);
            Investiture tinAllomancy = AllomancySystem.TIN;

            boolean activate = spiritweb.getSpiritwebInvestiture(tinAllomancy).getActivationLevel() == ActivationLevel.NONE;

            NetworkHandler.INSTANCE.sendToServer(
                new ClientInvestitureActivationPacket(player.getEntityId(), tinAllomancy, activate ? ActivationLevel.MEDIUM : ActivationLevel.NONE)
            );
        }
    }
}
