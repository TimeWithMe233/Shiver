package Shiver.module.impl.ghost;

import Shiver.Shiver;
import Shiver.event.Listener;
import Shiver.event.impl.EntityActionEvent;
import Shiver.event.impl.TickEvent;
import Shiver.module.Category;
import Shiver.module.Module;
import Shiver.module.impl.combat.Killaura;
import Shiver.setting.impl.ModeSetting;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.MovingObjectPosition;
import org.lwjgl.input.Keyboard;

public class WTap
extends Module {
    private boolean taped;
    private boolean stoppedLastTick;
    private final ModeSetting mode = new ModeSetting("Mode", "Legit", "Legit", "Spoof");
    private Killaura killauraModule;
    private EntityLivingBase lastCursorTarget;
    private int cursorTargetTicks;

    public WTap() {
        super("WTap", Category.GHOST);
        this.addSettings(this.mode);
    }

    @Override
    public void onEnable() {
        this.taped = false;
        this.stoppedLastTick = false;
    }

    @Override
    public void onClientStarted() {
        this.killauraModule = Shiver.instance.getModuleManager().getModule(Killaura.class);
    }

    @Listener(value=3)
    public void onTick(TickEvent event) {
        if (this.mode.is("Legit")) {
            if (this.stoppedLastTick) {
                WTap.mc.gameSettings.keyBindForward.pressed = Keyboard.isKeyDown((int)WTap.mc.gameSettings.keyBindForward.getKeyCode());
                this.stoppedLastTick = false;
                return;
            }
            EntityLivingBase target = this.getCurrentTarget();
            if (target != null) {
                if (target.hurtTime >= 2 && WTap.mc.thePlayer.onGround && WTap.mc.thePlayer.isSprinting()) {
                    if (!this.taped) {
                        WTap.mc.gameSettings.keyBindSprint.pressed = false;
                        WTap.mc.gameSettings.keyBindForward.pressed = false;
                        this.stoppedLastTick = true;
                        this.taped = true;
                    }
                } else {
                    this.taped = false;
                }
            } else {
                this.taped = false;
            }
        }
    }

    @Listener(value=3)
    public void onEntityAction(EntityActionEvent event) {
        if (this.mode.is("Spoof")) {
            EntityLivingBase target = this.getCurrentTarget();
            if (target != null) {
                if (target.hurtTime >= 2 && WTap.mc.thePlayer.onGround && WTap.mc.thePlayer.isSprinting()) {
                    if (!this.taped) {
                        event.setSprinting(false);
                        this.taped = true;
                    }
                } else {
                    this.taped = false;
                }
            } else {
                this.taped = false;
            }
        }
    }

    public EntityLivingBase getCurrentTarget() {
        if (this.killauraModule == null) {
            this.killauraModule = Shiver.instance.getModuleManager().getModule(Killaura.class);
        }
        if (this.killauraModule.isEnabled() && this.killauraModule.getTarget() != null) {
            return this.killauraModule.getTarget();
        }
        if (WTap.mc.objectMouseOver != null && WTap.mc.objectMouseOver.typeOfHit == MovingObjectPosition.MovingObjectType.ENTITY && WTap.mc.objectMouseOver.entityHit instanceof EntityLivingBase) {
            this.lastCursorTarget = (EntityLivingBase)WTap.mc.objectMouseOver.entityHit;
            return (EntityLivingBase)WTap.mc.objectMouseOver.entityHit;
        }
        if (this.lastCursorTarget != null) {
            if (++this.cursorTargetTicks > 10) {
                this.lastCursorTarget = null;
            } else {
                return this.lastCursorTarget;
            }
        }
        return null;
    }
}

