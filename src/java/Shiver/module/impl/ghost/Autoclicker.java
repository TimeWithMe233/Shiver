package Shiver.module.impl.ghost;

import Shiver.event.Listener;
import Shiver.event.impl.RenderEvent;
import Shiver.event.impl.TickEvent;
import Shiver.module.Category;
import Shiver.module.Module;
import Shiver.setting.impl.IntegerSetting;
import Shiver.util.misc.TimerUtil;
import java.util.concurrent.ThreadLocalRandom;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MovingObjectPosition;
import org.lwjgl.input.Mouse;

public class Autoclicker
extends Module {
    private boolean wasHoldingMouse;
    private boolean clickingTick;
    private boolean breakingBlock = false;
    private final TimerUtil timer = new TimerUtil();
    private final IntegerSetting minCPS = new IntegerSetting("Min CPS", 8, 1, 20, 1);
    private final IntegerSetting maxCPS = new IntegerSetting("Max CPS", 15, 1, 20, 1);

    public Autoclicker() {
        super("Autoclicker", Category.GHOST);
        this.addSettings(this.minCPS, this.maxCPS);
    }

    @Override
    public void onEnable() {
        this.wasHoldingMouse = false;
    }

    @Listener
    public void onRender(RenderEvent event) {
        if (this.wasHoldingMouse) {
            long minDelay;
            long delay;
            long maxDelay = (long)(1000.0 / (double)this.minCPS.getValue());
            long l = delay = maxDelay > (minDelay = (long)(1000.0 / (double)this.maxCPS.getValue())) ? ThreadLocalRandom.current().nextLong(minDelay, maxDelay) : minDelay;
            if (this.timer.getTimeElapsed() >= delay) {
                this.clickingTick = true;
                this.timer.reset();
            }
        }
    }

    @Listener
    public void onTick(TickEvent event) {
        if (Mouse.isButtonDown((int)0) && !Mouse.isButtonDown((int)1) && !Autoclicker.mc.thePlayer.isUsingItem() && !(Autoclicker.mc.currentScreen instanceof GuiInventory)) {
            if (this.wasHoldingMouse && this.clickingTick) {
                Autoclicker.mc.leftClickCounter = 0;
                mc.clickMouse();
                this.clickingTick = false;
            }
            this.wasHoldingMouse = true;
        } else {
            this.wasHoldingMouse = false;
        }
        if (Autoclicker.mc.objectMouseOver != null && Autoclicker.mc.objectMouseOver.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK) {
            BlockPos pos = Autoclicker.mc.objectMouseOver.getBlockPos();
            this.breakingBlock = true;
        } else {
            this.breakingBlock = false;
        }
    }
}

