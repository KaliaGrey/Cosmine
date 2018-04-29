package kalia.cosmine.registry;

import kalia.cosmine.Cosmine;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Keyboard;

@SideOnly(Side.CLIENT)
public class KeyBindingRegistry {
    public static KeyBinding TEST_KEY;

    public static void init() {
        TEST_KEY = new KeyBinding("key.test", Keyboard.KEY_T, "key.categories." + Cosmine.MOD_ID);
        ClientRegistry.registerKeyBinding(TEST_KEY);
    }
}
