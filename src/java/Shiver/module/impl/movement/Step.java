package Shiver.module.impl.movement;

import Shiver.event.Listener;
import Shiver.event.impl.MotionEvent;
import Shiver.event.impl.PostStepEvent;
import Shiver.event.impl.PreStepEvent;
import Shiver.event.impl.UpdateEvent;
import Shiver.module.Category;
import Shiver.module.Module;
import Shiver.setting.impl.DoubleSetting;
import Shiver.setting.impl.ModeSetting;
import Shiver.util.network.PacketUtils;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;

public class Step
extends Module {
    private final ModeSetting mode = new ModeSetting("Mode", "Vanilla", "Vanilla", "NCP");
    private final DoubleSetting height = new DoubleSetting("Height", () -> this.mode.is("Vanilla"), 1.0, 1.0, 9.0, 0.5);
    private final DoubleSetting timer = new DoubleSetting("Timer", 1.0, 0.1, 1.0, 0.05);
    private boolean prevOffGround;
    private boolean timerTick;

    public Step() {
        super("Step", Category.MOVEMENT);
        this.addSettings(this.mode, this.height, this.timer);
    }

    @Override
    public void onDisable() {
        this.prevOffGround = false;
        Step.mc.timer.timerSpeed = 1.0f;
        Step.mc.thePlayer.stepHeight = 0.6f;
    }

    @Listener
    public void onUpdate(UpdateEvent event) {
        switch (this.mode.getMode()) {
            case "Vanilla": {
                Step.mc.thePlayer.stepHeight = (float)this.height.getValue();
                break;
            }
            case "NCP": {
                Step.mc.thePlayer.stepHeight = 1.0f;
            }
        }
        if (this.timerTick) {
            Step.mc.timer.timerSpeed = 1.0f;
            this.timerTick = false;
        }
    }

    @Listener
    public void onPreStep(PreStepEvent event) {
        if (!this.mode.is("Vanilla") && Step.mc.thePlayer.onGround && this.prevOffGround && (double)event.getHeight() > 0.6) {
            event.setHeight(0.6f);
        }
    }

    @Listener
    public void onPostStep(PostStepEvent event) {
        if (event.getHeight() > 0.6f) {
            if (this.timer.getValue() < 1.0) {
                Step.mc.timer.timerSpeed = (float)this.timer.getValue();
                this.timerTick = true;
            }
            switch (this.mode.getMode()) {
                case "NCP": {
                    PacketUtils.sendPacket((Packet)new C03PacketPlayer.C04PacketPlayerPosition(Step.mc.thePlayer.posX, Step.mc.thePlayer.posY + 0.42, Step.mc.thePlayer.posZ, false));
                    PacketUtils.sendPacket((Packet)new C03PacketPlayer.C04PacketPlayerPosition(Step.mc.thePlayer.posX, Step.mc.thePlayer.posY + 0.75, Step.mc.thePlayer.posZ, false));
                }
            }
        }
    }

    @Listener
    public void onMotion(MotionEvent event) {
        this.prevOffGround = !event.isOnGround();
    }
}

