package kalia.cosmine.registry;

import kalia.cosmine.Cosmine;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Keyboard;

@SideOnly(Side.CLIENT)
public class KeyBindingRegistry {
    public static final KeyBinding TEST_KEY_1 = new KeyBinding("key.test_1", Keyboard.KEY_Y, "key.categories." + Cosmine.MOD_ID);
    public static final KeyBinding TEST_KEY_2 = new KeyBinding("key.test_2", Keyboard.KEY_U, "key.categories." + Cosmine.MOD_ID);
    public static final KeyBinding TEST_KEY_3 = new KeyBinding("key.test_3", Keyboard.KEY_I, "key.categories." + Cosmine.MOD_ID);

    public static void init() {
        ClientRegistry.registerKeyBinding(TEST_KEY_1);
        ClientRegistry.registerKeyBinding(TEST_KEY_2);
        ClientRegistry.registerKeyBinding(TEST_KEY_3);
    }
}
