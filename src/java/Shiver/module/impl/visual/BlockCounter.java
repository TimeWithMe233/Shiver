package Shiver.module.impl.visual;

import Shiver.Shiver;
import Shiver.font.AcrimonyFont;
import Shiver.module.AlignType;
import Shiver.module.Category;
import Shiver.module.HUDModule;
import Shiver.module.impl.player.Scaffold;
import Shiver.setting.impl.BooleanSetting;
import Shiver.setting.impl.EnumModeSetting;
import Shiver.setting.impl.IntegerSetting;
import Shiver.setting.impl.ModeSetting;
import Shiver.util.animation.Animation;
import Shiver.util.animation.AnimationType;
import Shiver.util.animation.AnimationUtil;
import Shiver.util.render.DrawUtil;
import Shiver.util.render.FontUtil;
import java.awt.Color;
import java.util.Arrays;
import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import org.lwjgl.opengl.GL11;

public class BlockCounter
extends HUDModule {
    private final ModeSetting mode = new ModeSetting("Mode", "Shiver", "Shiver", "Classic");
    private final BooleanSetting blurbg = new BooleanSetting("Blur Background", () -> this.mode.is("Shiver"), false);
    private ClientTheme theme;
    private AcrimonyFont sfui;
    private AcrimonyFont Neverlose;
    private Scaffold scaffoldModule;
    private Animation animation;
    private final EnumModeSetting<AnimationType> animationType = AnimationUtil.getAnimationType(AnimationType.POP);
    private final IntegerSetting animationDuration = AnimationUtil.getAnimationDuration(100);
    public static final List<Block> invalidBlocks = Arrays.asList(Blocks.enchanting_table, Blocks.furnace, Blocks.carpet, Blocks.crafting_table, Blocks.trapped_chest, Blocks.chest, Blocks.dispenser, Blocks.air, Blocks.water, Blocks.lava, Blocks.flowing_water, Blocks.flowing_lava, Blocks.sand, Blocks.snow_layer, Blocks.torch, Blocks.anvil, Blocks.jukebox, Blocks.stone_button, Blocks.wooden_button, Blocks.lever, Blocks.noteblock, Blocks.stone_pressure_plate, Blocks.light_weighted_pressure_plate, Blocks.wooden_pressure_plate, Blocks.heavy_weighted_pressure_plate, Blocks.stone_slab, Blocks.wooden_slab, Blocks.stone_slab2, Blocks.red_mushroom, Blocks.brown_mushroom, Blocks.yellow_flower, Blocks.red_flower, Blocks.anvil, Blocks.glass_pane, Blocks.stained_glass_pane, Blocks.iron_bars, Blocks.cactus, Blocks.ladder, Blocks.web);

    public BlockCounter() {
        super("BlockCounter", Category.VISUAL, BlockCounter.mc.displayWidth / 2, BlockCounter.mc.displayHeight / 2, 80, 18, AlignType.LEFT);
        this.addSettings(this.mode, this.blurbg, this.animationType, this.animationDuration);
        ScaledResolution sr = new ScaledResolution(mc);
        this.animation = new Animation();
        this.animation.setAnimDuration(this.animationDuration.getValue());
        this.animation.setAnimType(this.animationType.getMode());
    }

    @Override
    public void onClientStarted() {
        this.theme = Shiver.instance.getModuleManager().getModule(ClientTheme.class);
        this.sfui = Shiver.instance.getFontManager().getSfpro();
        this.Neverlose = Shiver.instance.getFontManager().getSfprobold();
        this.scaffoldModule = Shiver.instance.getModuleManager().getModule(Scaffold.class);
    }

    @Override
    public void renderModule(boolean inChat) {
        boolean canRender;
        boolean bl = canRender = this.scaffoldModule.isEnabled() || inChat;
        if (inChat && this.isEnabled()) {
            this.animation.getTimer().setTimeElapsed(this.animationDuration.getValue());
            this.renderBlockCounter(true);
        } else if (this.isEnabled()) {
            this.renderBlockCounter(canRender);
        } else {
            this.animation.getTimer().setTimeElapsed(0L);
        }
    }

    private void renderBlockCounter(boolean canRender) {
        int x = (int)this.posX.getValue();
        int y = (int)this.posY.getValue();
        ScaledResolution sr = new ScaledResolution(mc);
        this.animation.updateState(canRender);
        this.animation.setAnimDuration(this.animationDuration.getValue());
        this.animation.setAnimType(this.animationType.getMode());
        String info = this.getBlockCount() + "";
        String block = " blocks";
        double InfoWidth = this.getStringWidth(info + block);
        int color = this.getBlockCount() >= 48 ? -16711936 : (this.getBlockCount() >= 32 ? -256 : -65536);
        if (this.animation.isRendered() || !this.animation.isAnimDone()) {
            this.animation.render(() -> {
                switch (this.mode.getMode()) {
                    case "Shiver": {
                        if (this.blurbg.isEnabled()) {
                            Shiver.instance.blurHandler.blur((double)x, (double)y, InfoWidth + 26.0, (double)this.height, 0.0f);
                        }
                        DrawUtil.drawRoundedRect(x, y, (double)x + InfoWidth + 26.0, y + this.height, 2.0, new Color(0, 0, 0, 100).getRGB());
                        GL11.glEnable((int)2903);
                        GL11.glPushMatrix();
                        GL11.glColor4f((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
                        RenderHelper.enableGUIStandardItemLighting();
                        BlockCounter.renderItem(BlockCounter.mc.thePlayer.getHeldItem(), (float)x + 4.0f, y + 1, 1.0f);
                        RenderHelper.disableStandardItemLighting();
                        GL11.glPopMatrix();
                        GL11.glDisable((int)2903);
                        this.Neverlose.drawString(info, (double)(x + 21), (double)y + 5.8, new Color(255, 255, 255, 255).getRGB());
                        this.sfui.drawString(block, (double)x + InfoWidth - 10.0, (double)y + 5.8, new Color(255, 255, 255, 255).getRGB());
                        break;
                    }
                    case "Classic": {
                        BlockCounter.mc.fontRendererObj.drawStringWithShadow(info, (float)sr.getScaledWidth() / 2.0f, (float)sr.getScaledHeight() / 2.0f + 10.0f, color);
                    }
                }
            }, x, y, x + this.width, y);
        }
    }

    private boolean isValid(Item item) {
        return item instanceof ItemBlock && !invalidBlocks.contains(((ItemBlock)item).getBlock());
    }

    public int getBlockCount() {
        int n = 0;
        for (int i = 36; i < 45; ++i) {
            if (!BlockCounter.mc.thePlayer.inventoryContainer.getSlot(i).getHasStack()) continue;
            ItemStack stack = BlockCounter.mc.thePlayer.inventoryContainer.getSlot(i).getStack();
            Item item = stack.getItem();
            if (!(stack.getItem() instanceof ItemBlock) || !this.isValid(item)) continue;
            n += stack.stackSize;
        }
        return n;
    }

    public double getStringWidth(String s) {
        switch (this.mode.getMode()) {
            case "Shiver": {
                FontUtil.getStringWidth(this.Neverlose.toString(), s);
                break;
            }
            case "Classic": {
                BlockCounter.mc.fontRendererObj.getStringWidth(s);
            }
        }
        return BlockCounter.mc.fontRendererObj.getStringWidth(s);
    }

    public static void renderItem(ItemStack stack, float x, float y, float size) {
        RenderItem ri = mc.getRenderItem();
        ri.renderItemAndEffectIntoGUI(stack, (int)x, (int)y);
    }

    @Override
    public String getSuffix() {
        return "";
    }
}

