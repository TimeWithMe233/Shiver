package Shiver.util.misc;

import Shiver.util.IMinecraft;
import net.minecraft.client.audio.ISound;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.util.ResourceLocation;

public class AudioUtil
implements IMinecraft {
    public static void buttonClick() {
        mc.getSoundHandler().playSound((ISound)PositionedSoundRecord.create((ResourceLocation)new ResourceLocation("gui.button.press"), (float)1.0f));
    }

    public static void enable() {
        mc.getSoundHandler().playSound((ISound)PositionedSoundRecord.create((ResourceLocation)new ResourceLocation("gui.button.sigmao"), (float)1.0f));
    }

    public static void disable() {
        mc.getSoundHandler().playSound((ISound)PositionedSoundRecord.create((ResourceLocation)new ResourceLocation("gui.button.sigmad"), (float)1.0f));
    }
}

