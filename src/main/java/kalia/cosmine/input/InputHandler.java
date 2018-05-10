package kalia.cosmine.input;

import kalia.cosmine.capability.PlayerSpiritweb;
import kalia.cosmine.gui.AllomancyGui;
import kalia.cosmine.investiture.ActivationLevel;
import kalia.cosmine.investiture.Investiture;
import kalia.cosmine.investiture.allomancy.Allomancy;
import kalia.cosmine.network.NetworkHandler;
import kalia.cosmine.network.playerspiritweb.client.ClientInvestitureActivationPacket;
import kalia.cosmine.registry.KeyBindingRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.relauncher.Side;

@Mod.EventBusSubscriber(Side.CLIENT)
public class InputHandler {
    @SubscribeEvent
    public static void onKeyInput(InputEvent.KeyInputEvent event) {
        if (KeyBindingRegistry.TEST_KEY_1.isPressed()) {
            EntityPlayer player = Minecraft.getMinecraft().player;
            PlayerSpiritweb spiritweb = PlayerSpiritweb.getPlayerSpiritWeb(player);
            Investiture tinAllomancy = Allomancy.TIN;

            if (spiritweb.getInherentAllomancy(tinAllomancy) != null) {
                boolean activate = spiritweb.getSpiritwebInvestiture(tinAllomancy).getActivationLevel() == ActivationLevel.NONE;

                NetworkHandler.INSTANCE.sendToServer(
                        new ClientInvestitureActivationPacket(player.getEntityId(), tinAllomancy, activate ? ActivationLevel.MEDIUM : ActivationLevel.NONE)
                );
                player.playSound(new SoundEvent(new ResourceLocation("block.fire.extinguish")), 1, 4);
            }
        }

        if (KeyBindingRegistry.TEST_KEY_2.isPressed()) {
            EntityPlayer player = Minecraft.getMinecraft().player;
            PlayerSpiritweb spiritweb = PlayerSpiritweb.getPlayerSpiritWeb(player);
            Investiture pewterAllomancy = Allomancy.PEWTER;

            if (spiritweb.getInherentAllomancy(pewterAllomancy) != null) {
                int currentActivationIndex = ActivationLevel.toIndex(spiritweb.getSpiritwebInvestiture(pewterAllomancy).getActivationLevel());
                ActivationLevel newActivationLevel = ActivationLevel.fromIndex((currentActivationIndex + 1) % 4);

                NetworkHandler.INSTANCE.sendToServer(
                        new ClientInvestitureActivationPacket(player.getEntityId(), pewterAllomancy, newActivationLevel)
                );
                player.playSound(new SoundEvent(new ResourceLocation("block.fire.extinguish")), 1, 4);
            }
        }

        if (KeyBindingRegistry.TEST_KEY_3.isPressed()) {
            Minecraft minecraft = Minecraft.getMinecraft();

            if (minecraft.currentScreen == null) {
                minecraft.displayGuiScreen(new AllomancyGui());
            }
        }
    }
}
