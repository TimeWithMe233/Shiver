package Shiver.util.misc;

import Shiver.Shiver;
import Shiver.util.IMinecraft;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IChatComponent;

public class LogUtil
implements IMinecraft {
    private static final String prefix = "[" + Shiver.instance.name + "]";

    public static void print(Object message) {
        System.out.println(prefix + " " + message);
    }

    public static void addChatMessage(String message) {
        LogUtil.mc.thePlayer.addChatMessage((IChatComponent)new ChatComponentText(message));
    }
}

