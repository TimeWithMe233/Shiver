package Shiver.module.impl.visual;

import Shiver.event.Listener;
import Shiver.event.impl.MotionEvent;
import Shiver.event.impl.RotationsRenderEvent;
import Shiver.module.Category;
import Shiver.module.EventListenType;
import Shiver.module.Module;
import Shiver.setting.impl.BooleanSetting;

public class Rotations
extends Module {
    private float yaw;
    private float pitch;
    private float lastYaw;
    private float lastPitch;
    private boolean customRender;
    private BooleanSetting smooth = new BooleanSetting("Smooth", true);

    public Rotations() {
        super("Rotations", Category.VISUAL);
        this.addSettings(this.smooth);
        this.listenType = EventListenType.MANUAL;
        this.setStateHidden(true);
        this.setEnabledSilently(true);
        this.startListening();
    }

    @Listener(value=4)
    public void onMotion(MotionEvent event) {
        this.customRender = Rotations.mc.thePlayer.rotationYaw != event.getYaw() || Rotations.mc.thePlayer.rotationPitch != event.getPitch();
        this.lastYaw = this.yaw;
        this.lastPitch = this.pitch;
        this.yaw = event.getYaw();
        this.pitch = event.getPitch();
    }

    @Listener(value=4)
    public void onRotsRender(RotationsRenderEvent event) {
        if (this.customRender) {
            float partialTicks = event.getPartialTicks();
            event.setYaw(this.smooth.isEnabled() ? this.interpolateRotation(this.lastYaw, this.yaw, partialTicks) : this.yaw);
            event.setBodyYaw(this.smooth.isEnabled() ? this.interpolateRotation(this.lastYaw, this.yaw, partialTicks) : this.yaw);
            event.setPitch(this.smooth.isEnabled() ? this.lastPitch + (this.pitch - this.lastPitch) * partialTicks : this.pitch);
        }
    }

    protected float interpolateRotation(float par1, float par2, float par3) {
        float f;
        for (f = par2 - par1; f < -180.0f; f += 360.0f) {
        }
        while (f >= 180.0f) {
            f -= 360.0f;
        }
        return par1 + par3 * f;
    }
}

