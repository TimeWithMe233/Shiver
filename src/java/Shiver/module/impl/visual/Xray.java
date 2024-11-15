package Shiver.module.impl.visual;

import Shiver.module.Category;
import Shiver.module.Module;
import net.minecraft.block.Block;

public class Xray
extends Module {
    private float oldGamma;
    private final int[] blockIds = new int[]{14, 15, 56, 129};

    public Xray() {
        super("Xray", Category.VISUAL);
    }

    @Override
    public void onEnable() {
        this.oldGamma = Xray.mc.gameSettings.gammaSetting;
        Xray.mc.gameSettings.gammaSetting = 10.0f;
        Xray.mc.gameSettings.ambientOcclusion = 0;
        Xray.mc.renderGlobal.loadRenderers();
    }

    @Override
    public void onDisable() {
        Xray.mc.gameSettings.gammaSetting = this.oldGamma;
        Xray.mc.gameSettings.ambientOcclusion = 1;
        Xray.mc.renderGlobal.loadRenderers();
    }

    public boolean shouldRenderBlock(Block block) {
        for (int id : this.blockIds) {
            if (block != Block.getBlockById((int)id)) continue;
            return true;
        }
        return false;
    }
}

