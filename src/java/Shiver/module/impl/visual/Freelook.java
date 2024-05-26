package Shiver.module.impl.visual;

import Shiver.Shiver;
import Shiver.event.Listener;
import Shiver.event.impl.TickEvent;
import Shiver.module.Category;
import Shiver.module.EventListenType;
import Shiver.module.Module;
import org.lwjgl.input.Keyboard;

public class Freelook
extends Module {
    private boolean wasFreelooking;

    public Freelook() {
        super("Freelook", Category.VISUAL);
        this.listenType = EventListenType.MANUAL;
        this.startListening();
    }

    @Listener
    public void onTick(TickEvent event) {
        if (Freelook.mc.thePlayer.ticksExisted < 10) {
            this.stop();
        }
        if (Keyboard.isKeyDown((int)this.getKey())) {
            this.wasFreelooking = true;
            Shiver.instance.getCameraHandler().setFreelooking(true);
            Freelook.mc.gameSettings.thirdPersonView = 1;
        } else if (this.wasFreelooking) {
            this.stop();
        }
    }

    private void stop() {
        this.setEnabled(false);
        Shiver.instance.getCameraHandler().setFreelooking(false);
        this.wasFreelooking = false;
        Freelook.mc.gameSettings.thirdPersonView = 0;
    }
}

