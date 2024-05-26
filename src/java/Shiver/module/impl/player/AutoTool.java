package Shiver.module.impl.player;

import Shiver.Shiver;
import Shiver.event.Listener;
import Shiver.event.impl.TickEvent;
import Shiver.module.Category;
import Shiver.module.Module;
import Shiver.setting.impl.BooleanSetting;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MovingObjectPosition;
import org.lwjgl.input.Mouse;

public class AutoTool
extends Module {
    private int oldSlot;
    private boolean wasDigging;
    private final BooleanSetting spoof = new BooleanSetting("Item spoof", false);

    public AutoTool() {
        super("AutoTool", Category.PLAYER);
        this.addSettings(this.spoof);
    }

    @Override
    public void onDisable() {
        if (this.wasDigging) {
            AutoTool.mc.thePlayer.inventory.currentItem = this.oldSlot;
            this.wasDigging = false;
        }
        Shiver.instance.getSlotSpoofHandler().stopSpoofing();
    }

    @Listener(value=3)
    public void onTick(TickEvent event) {
        if ((Mouse.isButtonDown((int)0) || AutoTool.mc.gameSettings.keyBindAttack.isKeyDown()) && AutoTool.mc.objectMouseOver != null && AutoTool.mc.objectMouseOver.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK) {
            Block block = AutoTool.mc.theWorld.getBlockState(AutoTool.mc.objectMouseOver.getBlockPos()).getBlock();
            float strength = 0.0f;
            if (!this.wasDigging) {
                this.oldSlot = AutoTool.mc.thePlayer.inventory.currentItem;
                if (this.spoof.isEnabled()) {
                    Shiver.instance.getSlotSpoofHandler().startSpoofing(this.oldSlot);
                }
            }
            for (int i = 0; i <= 8; ++i) {
                float slotStrength;
                ItemStack stack = AutoTool.mc.thePlayer.inventory.getStackInSlot(i);
                if (stack == null || !((slotStrength = stack.getStrVsBlock(block)) > strength)) continue;
                AutoTool.mc.thePlayer.inventory.currentItem = i;
                strength = slotStrength;
            }
            this.wasDigging = true;
        } else if (this.wasDigging) {
            AutoTool.mc.thePlayer.inventory.currentItem = this.oldSlot;
            Shiver.instance.getSlotSpoofHandler().stopSpoofing();
            this.wasDigging = false;
        } else {
            this.oldSlot = AutoTool.mc.thePlayer.inventory.currentItem;
        }
    }
}

