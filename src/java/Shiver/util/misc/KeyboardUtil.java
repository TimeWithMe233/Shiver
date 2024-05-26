package Shiver.util.misc;

import Shiver.util.IMinecraft;
import net.minecraft.client.settings.KeyBinding;
import org.lwjgl.input.Keyboard;

public class KeyboardUtil {
    public static boolean isPressed(KeyBinding key) {
        return Keyboard.isKeyDown((int)key.getKeyCode()) && IMinecraft.mc.currentScreen == null;
    }

    public static void resetKeybinding(KeyBinding key) {
        key.pressed = IMinecraft.mc.currentScreen != null ? false : KeyboardUtil.isPressed(key);
    }

    public static void resetKeybindings(KeyBinding ... keys) {
        for (KeyBinding key : keys) {
            KeyboardUtil.resetKeybinding(key);
        }
    }
}

