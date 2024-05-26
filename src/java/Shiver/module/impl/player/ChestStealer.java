package Shiver.module.impl.player;

import Shiver.Shiver;
import Shiver.event.Listener;
import Shiver.event.impl.UpdateEvent;
import Shiver.module.Category;
import Shiver.module.Module;
import Shiver.setting.impl.BooleanSetting;
import Shiver.setting.impl.IntegerSetting;
import net.minecraft.block.Block;
import net.minecraft.block.BlockChest;
import net.minecraft.block.BlockEnderChest;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ContainerChest;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import tech.skidonion.obfuscator.annotations.NativeObfuscation;

@NativeObfuscation(virtualize = NativeObfuscation.VirtualMachine.TIGER_BLACK)
public class ChestStealer
extends Module {
    private final IntegerSetting delay = new IntegerSetting("Delay", 1, 0, 10, 1);
    private final BooleanSetting filter = new BooleanSetting("Filter", true);
    public static final BooleanSetting silent = new BooleanSetting("Silent", false);
    private final BooleanSetting autoClose = new BooleanSetting("Autoclose", true);
    private final BooleanSetting guiDetect = new BooleanSetting("Gui detect", true);
    private int counter;
    private InventoryManager invManager;

    public ChestStealer() {
        super("Chest Stealer", Category.PLAYER);
        this.addSettings(this.delay, this.filter, this.autoClose, this.guiDetect, silent);
    }

    @Override
    public void onClientStarted() {
        this.invManager = Shiver.instance.getModuleManager().getModule(InventoryManager.class);
    }

    @Listener
    public void onUpdate(UpdateEvent event) {
        if (!(ChestStealer.mc.thePlayer.openContainer == null || !(ChestStealer.mc.thePlayer.openContainer instanceof ContainerChest) || this.isGUI() && this.guiDetect.isEnabled())) {
            ContainerChest container = (ContainerChest)ChestStealer.mc.thePlayer.openContainer;
            for (int i = 0; i < container.getLowerChestInventory().getSizeInventory(); ++i) {
                ItemStack stack = container.getLowerChestInventory().getStackInSlot(i);
                if (stack == null || this.isUseless(stack) || ++this.counter <= this.delay.getValue()) continue;
                ChestStealer.mc.playerController.windowClick(container.windowId, i, 1, 1, (EntityPlayer)ChestStealer.mc.thePlayer);
                this.counter = 0;
                return;
            }
            if (this.autoClose.isEnabled() && this.isChestEmpty(container)) {
                ChestStealer.mc.thePlayer.closeScreen();
            }
        }
    }

    private boolean isChestEmpty(ContainerChest container) {
        for (int i = 0; i < container.getLowerChestInventory().getSizeInventory(); ++i) {
            ItemStack stack = container.getLowerChestInventory().getStackInSlot(i);
            if (stack == null || this.isUseless(stack)) continue;
            return false;
        }
        return true;
    }

    private boolean isUseless(ItemStack stack) {
        if (!this.filter.isEnabled()) {
            return false;
        }
        return this.invManager.isUseless(stack);
    }

    private boolean isGUI() {
        for (double x = ChestStealer.mc.thePlayer.posX - 5.0; x <= ChestStealer.mc.thePlayer.posX + 5.0; x += 1.0) {
            for (double y = ChestStealer.mc.thePlayer.posY - 5.0; y <= ChestStealer.mc.thePlayer.posY + 5.0; y += 1.0) {
                for (double z = ChestStealer.mc.thePlayer.posZ - 5.0; z <= ChestStealer.mc.thePlayer.posZ + 5.0; z += 1.0) {
                    BlockPos pos = new BlockPos(x, y, z);
                    Block block = ChestStealer.mc.theWorld.getBlockState(pos).getBlock();
                    if (!(block instanceof BlockChest) && !(block instanceof BlockEnderChest)) continue;
                    return false;
                }
            }
        }
        return true;
    }
}

