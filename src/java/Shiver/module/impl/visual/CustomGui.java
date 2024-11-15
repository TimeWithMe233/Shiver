package Shiver.module.impl.visual;

import Shiver.Shiver;
import Shiver.event.Listener;
import Shiver.event.impl.Render2DEvent;
import Shiver.module.Category;
import Shiver.module.Module;
import Shiver.setting.impl.BooleanSetting;
import Shiver.setting.impl.DoubleSetting;
import Shiver.setting.impl.ModeSetting;
import Shiver.ui.notification.Notification;
import Shiver.ui.notification.NotificationType;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.IOException;
import java.util.Locale;

public class CustomGui
extends Module {
    public static BooleanSetting itemphysics = new BooleanSetting("Item Physics", true);
    public static BooleanSetting nohurtcam = new BooleanSetting("Disable HurtCam", true);
    public static BooleanSetting motionblur = new BooleanSetting("Enable MotionBlur", false);
    private final DoubleSetting motionbluramount = new DoubleSetting("Blur amount", () -> motionblur.isEnabled(), 1.0, 0.1, 10.0, 0.5);
    public static BooleanSetting clearChat = new BooleanSetting("Clear Chat", true);
    public static ModeSetting guiContainerAnimation = new ModeSetting("Inventory", "Default", "Scale", "Slide", "Default");

    public CustomGui() {
        super("Gui", Category.VISUAL);
        this.addSettings(guiContainerAnimation, nohurtcam, clearChat, itemphysics, motionblur, this.motionbluramount);
    }

    @Override
    public void onEnable() {
        if (motionblur.isEnabled()) {
            Shiver.instance.getNotificationHandler().postNotification(new Notification(NotificationType.WARNING, "MotionBlur", "Toggle Module again for motion-blur value ingame apply.", 3000L));
            CustomGui.mc.entityRenderer.theShaderGroup = null;
            if (CustomGui.mc.theWorld != null) {
                try {
                    CustomGui.mc.entityRenderer.loadCustomShader();
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Listener
    public final void onRender2D(Render2DEvent event) {
        if (motionblur.isEnabled() && CustomGui.mc.entityRenderer.theShaderGroup == null && CustomGui.mc.theWorld != null) {
            try {
                CustomGui.mc.entityRenderer.loadCustomShader();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public JsonObject getJsonObject() {
        String JSON = "{\"targets\":[\"swap\",\"previous\"],\"passes\":[{\"name\":\"phosphor\",\"intarget\":\"minecraft:main\",\"outtarget\":\"swap\",\"auxtargets\":[{\"name\":\"PrevSampler\",\"id\":\"previous\"}],\"uniforms\":[{\"name\":\"Phosphor\",\"values\":[%.2f, %.2f, %.2f]}]},{\"name\":\"blit\",\"intarget\":\"swap\",\"outtarget\":\"previous\"},{\"name\":\"blit\",\"intarget\":\"swap\",\"outtarget\":\"minecraft:main\"}]}";
        double amount = 0.7 + this.motionbluramount.getValue() / 100.0 * 3.0 - 0.01;
        JsonParser parser = new JsonParser();
        return parser.parse(String.format(Locale.ENGLISH, JSON, amount, amount, amount)).getAsJsonObject();
    }

    public DoubleSetting getMotionbluramount() {
        return this.motionbluramount;
    }
}

