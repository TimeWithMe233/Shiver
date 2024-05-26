package Shiver.module.impl.combat;

import Shiver.module.Category;
import Shiver.module.Module;
import Shiver.setting.impl.BooleanSetting;
import Shiver.setting.impl.DoubleSetting;
import Shiver.util.ModuleUtil;
import Shiver.util.player.RotationsUtil;
import Shiver.util.world.WorldUtil;
import net.minecraft.entity.EntityLivingBase;
import org.lwjgl.input.Keyboard;

public class TargetStrafe
extends Module {
    private final DoubleSetting maxRange = new DoubleSetting("Max range", 3.0, 1.0, 6.0, 0.1);
    public final BooleanSetting whilePressingSpace = new BooleanSetting("While pressing space", false);
    private boolean goingRight;
    private Killaura killaura;

    public TargetStrafe() {
        super("TargetStrafe", Category.COMBAT);
        this.addSettings(this.maxRange, this.whilePressingSpace);
    }

    @Override
    public void onClientStarted() {
        this.killaura = ModuleUtil.getKillaura();
    }

    public boolean shouldTargetStrafe() {
        return this.killaura.isEnabled() && this.killaura.getTarget() != null && this.killaura.getDistanceToEntity(this.killaura.getTarget()) <= this.killaura.rotationRange.getValue() && this.isEnabled() && (Keyboard.isKeyDown((int)TargetStrafe.mc.gameSettings.keyBindJump.getKeyCode()) || !this.whilePressingSpace.isEnabled());
    }

    public float getDirection() {
        float direction;
        EntityLivingBase target;
        double distance;
        if (TargetStrafe.mc.thePlayer.isCollidedHorizontally || !WorldUtil.isBlockUnder(3)) {
            boolean bl = this.goingRight = !this.goingRight;
        }
        if ((distance = this.killaura.getDistanceToEntity(target = this.killaura.getTarget())) > this.maxRange.getValue()) {
            direction = RotationsUtil.getRotationsToEntity(target, false)[0];
        } else {
            double offset = 90.0 - this.killaura.getDistanceToEntity(target) * 5.0;
            if (!this.goingRight) {
                offset = -offset;
            }
            direction = (float)((double)RotationsUtil.getRotationsToEntity(target, false)[0] + offset);
        }
        return (float)Math.toRadians(direction);
    }
}

