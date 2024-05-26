package Shiver.ui.menu;

import Shiver.Shiver;
import Shiver.font.AcrimonyFont;
import Shiver.ui.menu.components.Button;
import Shiver.util.glsl.GLSLSandboxShader;
import com.mojang.realmsclient.gui.ChatFormatting;
import java.awt.Color;
import java.io.IOException;
import java.util.ArrayList;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiMultiplayer;
import net.minecraft.client.gui.GuiOptions;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiSelectWorld;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import tech.skidonion.obfuscator.annotations.NativeObfuscation;

@NativeObfuscation(virtualize = NativeObfuscation.VirtualMachine.TIGER_BLACK)
public class ShiverMenu
extends GuiScreen {
    private long initTime = System.currentTimeMillis();
    private GLSLSandboxShader backgroundShader;
    private final ArrayList<Changelog> changelogs = new ArrayList();
    private final Button[] buttons = new Button[]{new Button("Singleplayer"), new Button("Multiplayer"), new Button("AltManager"), new Button("Settings"), new Button("Quit")};

    public void initGui() {
        super.initGui();
        try {
            this.backgroundShader = new GLSLSandboxShader("/assets/minecraft/acrimony/shader/load.fsh");
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        this.initTime = System.currentTimeMillis();
        this.buttonList.clear();
        this.changelogs.clear();
        int initHeight = this.height / 4 + 48;
        int initWidth = this.width / 2 - 51;
        this.buttonList.add(new GuiButton(0, initWidth - 40, initHeight + 20, "Singleplayer"));
        this.buttonList.add(new GuiButton(1, initWidth - 40, initHeight + 42, "Multiplayer"));
        this.buttonList.add(new GuiButton(2, initWidth - 40, initHeight + 64, "AltManager"));
        this.buttonList.add(new GuiButton(3, initWidth - 40, initHeight + 90, 98, 20, "Options.."));
        this.buttonList.add(new GuiButton(4, this.width / 2 + 11, initHeight + 90, 98, 20, "Exit Game"));
    }

    protected void actionPerformed(GuiButton button) throws IOException {
        super.actionPerformed(button);
        switch (button.id) {
            case 0: {
                this.mc.displayGuiScreen((GuiScreen)new GuiSelectWorld((GuiScreen)this));
                break;
            }
            case 1: {
                this.mc.displayGuiScreen((GuiScreen)new GuiMultiplayer((GuiScreen)this));
                break;
            }
            case 2: {
                this.mc.displayGuiScreen((GuiScreen)new AltLoginScreen());
                break;
            }
            case 3: {
                this.mc.displayGuiScreen((GuiScreen)new GuiOptions((GuiScreen)this, this.mc.gameSettings));
                break;
            }
            case 4: {
                this.mc.shutdown();
            }
        }
    }

    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);
        AcrimonyFont reguler = Shiver.instance.getFontManager().getSfpro();
        AcrimonyFont bigFont = Shiver.instance.getFontManager().getSfpro23();
        AcrimonyFont titleFont = Shiver.instance.getFontManager().getSfproTitle();
        GlStateManager.disableCull();
        this.backgroundShader.useShader(this.width, this.height, mouseX, mouseY, (float)(System.currentTimeMillis() - this.initTime) / 1000.0f);
        GL11.glBegin((int)7);
        GL11.glVertex2f((float)-1.0f, (float)-1.0f);
        GL11.glVertex2f((float)-1.0f, (float)1.0f);
        GL11.glVertex2f((float)1.0f, (float)1.0f);
        GL11.glVertex2f((float)1.0f, (float)-1.0f);
        GL11.glEnd();
        GL20.glUseProgram((int)0);
        GlStateManager.disableAlpha();
        ScaledResolution sr = new ScaledResolution(this.mc);
        for (GuiButton g : this.buttonList) {
            g.drawButton(this.mc, mouseX, mouseY);
        }
        int changeY = 0;
        for (Changelog c : this.changelogs) {
            int set = 0;
            String text = "";
            switch (c.getType()) {
                case ADD: {
                    text = ChatFormatting.DARK_GRAY + "[" + ChatFormatting.GREEN + "+" + ChatFormatting.DARK_GRAY + "] " + ChatFormatting.RESET;
                    break;
                }
                case FIXED: {
                    text = ChatFormatting.DARK_GRAY + "[" + ChatFormatting.YELLOW + "*" + ChatFormatting.DARK_GRAY + "] " + ChatFormatting.RESET;
                    break;
                }
                case REMOVE: {
                    text = ChatFormatting.DARK_GRAY + "[" + ChatFormatting.RED + "-" + ChatFormatting.DARK_GRAY + "] " + ChatFormatting.RESET;
                }
            }
            for (String description : c.getDescription()) {
                int before = 14 + changeY;
                reguler.drawString(text + description, 10.0, (double)(before + (set += 12)), new Color(190, 190, 190).getRGB());
            }
            changeY += c.getDescription().length * 12;
        }
    }
}

