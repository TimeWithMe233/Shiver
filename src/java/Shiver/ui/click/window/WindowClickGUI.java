package Shiver.ui.click.window;

import Shiver.Shiver;
import Shiver.font.AcrimonyFont;
import Shiver.module.Category;
import Shiver.module.Module;
import Shiver.module.impl.visual.ClickGuiModule;
import Shiver.module.impl.visual.ClientTheme;
import Shiver.setting.AbstractSetting;
import Shiver.setting.impl.BooleanSetting;
import Shiver.setting.impl.CustomDoubleSetting;
import Shiver.setting.impl.DoubleSetting;
import Shiver.setting.impl.EnumModeSetting;
import Shiver.setting.impl.IntegerSetting;
import Shiver.setting.impl.ModeSetting;
import Shiver.ui.click.dropdown.holder.CategoryHolder;
import Shiver.ui.click.dropdown.holder.ModuleHolder;
import Shiver.ui.click.dropdown.holder.SettingHolder;
import Shiver.util.misc.TimerUtil;
import Shiver.util.render.FontUtil;
import Shiver.util.render.StencilUtil;
import java.awt.Color;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.ChatAllowedCharacters;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

public class WindowClickGUI
extends GuiScreen {
    private float offsetY;
    private final ClickGuiModule module;
    private CategoryHolder currentCategory;
    private ModuleHolder currentModule;
    private CustomDoubleSetting customDSetting;
    private String customDSettingText;
    private final TimerUtil cursorTimer = new TimerUtil();
    private boolean deleteKeyPressed;
    private final TimerUtil deleteTimer = new TimerUtil();
    private final TimerUtil deleteSpeedTimer = new TimerUtil();
    private int scrollY;
    private boolean changingKeybind;
    private final ArrayList<CategoryHolder> categories = new ArrayList();
    private float targetScrollY = 0.0f;

    public WindowClickGUI(ClickGuiModule module) {
        this.module = module;
        int x = 20;
        int y = 20;
        for (Category category : Category.values()) {
            ArrayList<ModuleHolder> modules = new ArrayList<ModuleHolder>();
            Shiver.instance.getModuleManager().modules.stream().filter(m -> m.getCategory() == category).sorted(Comparator.comparing(Module::getName)).forEach(m -> modules.add(new ModuleHolder((Module)m)));
            this.categories.add(new CategoryHolder(category, modules, x, y, true));
        }
    }

    public void initGui() {
        this.scrollY = 0;
    }

    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        AcrimonyFont fr = Shiver.instance.getFontManager().getSfpro();
        ScaledResolution sr = new ScaledResolution(this.mc);
        this.offsetY = 0.0f;
        if (this.currentCategory == null) {
            this.currentCategory = this.categories.get(0);
        }
        float offsetYDelta = (this.targetScrollY - (float)this.scrollY) * 0.1f;
        this.scrollY += (int)offsetYDelta;
        int middleX = sr.getScaledWidth() / 2;
        int middleY = sr.getScaledHeight() / 2;
        int startWindowX = middleX - 240;
        int startWindowY = middleY - 180;
        Shiver.instance.blurHandler.bloom(startWindowX - 10, startWindowY - 10, sr.getScaledWidth() / 2 + 50, sr.getScaledHeight() / 2 + 40, 10, new Color(55, 55, 55).getRGB());
        Shiver.instance.blurHandler.bloom(startWindowX, startWindowY + 36, sr.getScaledWidth() / 2 + 30, sr.getScaledHeight() / 2 - 20, 18, new Color(55, 55, 55).getRGB());
        Shiver.instance.blurHandler.bloom(startWindowX, startWindowY, sr.getScaledWidth() / 2 + 20, 26, 18, ClientTheme.color1.getRGB());
        Shiver.instance.blurHandler.bloom(startWindowX, startWindowY + 36, sr.getScaledWidth() / 2 + 30, sr.getScaledHeight() / 2 - 20, 18, new Color(35, 35, 35).getRGB());
        int categoryAmount = Category.values().length;
        int startCategoryX = middleX - 210;
        int endCategoryX = middleX + 260;
        float diff = endCategoryX - startCategoryX;
        int i = 0;
        for (CategoryHolder category : this.categories) {
            float posX = (float)startCategoryX + (float)i * diff / (float)categoryAmount;
            fr.drawStringWithShadow(category.getCategory().name(), posX, startWindowY + 10, category == this.currentCategory ? ClientTheme.color2.getRGB() : -1);
            ++i;
            if (category != this.currentCategory) continue;
            int renderModuleX = startWindowX + 10;
            int renderModuleY = startWindowY + 50;
            for (ModuleHolder m : category.getModules()) {
                StencilUtil.initStencilToWrite();
                Shiver.instance.blurHandler.bloom(startWindowX, startWindowY + 50, 110, sr.getScaledHeight() / 2 - 48, 18, new Color(35, 35, 35).getRGB());
                StencilUtil.readStencilBuffer(1);
                GL11.glTranslatef((float)0.0f, (float)this.scrollY, (float)0.0f);
                fr.drawStringWithShadow(m.getModule().getName(), renderModuleX, renderModuleY, m.getModule().isEnabled() ? ClientTheme.color1.getRGB() : -1);
                renderModuleY += 20;
                GL11.glTranslatef((float)0.0f, (float)(-this.scrollY), (float)0.0f);
                StencilUtil.uninitStencilBuffer();
            }
        }
        if (this.currentModule != null) {
            int renderSettingX = startWindowX + 120;
            int renderSettingY = startWindowY + 38;
            for (SettingHolder sHolder : this.currentModule.getSettings()) {
                float startSettingY;
                float endSettingX;
                float startSettingX;
                AbstractSetting doubleSetting;
                Object abstractSetting = sHolder.getSetting();
                if (!((AbstractSetting)abstractSetting).getVisibility().get().booleanValue()) continue;
                if (abstractSetting instanceof BooleanSetting) {
                    BooleanSetting boolSetting = (BooleanSetting)sHolder.getSetting();
                    if (boolSetting.isShownInColor()) {
                        fr.drawStringWithShadow(boolSetting.getDisplayName(), renderSettingX, (float)renderSettingY + 3.5f, boolSetting.isEnabled() ? ClientTheme.color1.getRGB() : -1);
                    } else {
                        fr.drawStringWithShadow(boolSetting.getDisplayName() + " : " + boolSetting.isEnabled(), renderSettingX, (float)renderSettingY + 3.5f, -1);
                    }
                } else if (abstractSetting instanceof ModeSetting) {
                    ModeSetting modeSetting = (ModeSetting)sHolder.getSetting();
                    fr.drawStringWithShadow(modeSetting.getDisplayName() + " : " + modeSetting.getMode(), renderSettingX, (float)renderSettingY + 3.5f, -1);
                } else if (abstractSetting instanceof EnumModeSetting) {
                    EnumModeSetting enumSetting = (EnumModeSetting)sHolder.getSetting();
                    fr.drawStringWithShadow(enumSetting.getDisplayName() + " : " + enumSetting.getMode(), renderSettingX, (float)renderSettingY + 3.5f, -1);
                } else if (abstractSetting instanceof DoubleSetting) {
                    doubleSetting = (DoubleSetting)sHolder.getSetting();
                    startSettingX = renderSettingX;
                    float startSettingY2 = (float)renderSettingY + 1.5f;
                    float endSettingY = (float)renderSettingY + 14.5f;
                    double startSliderX = startSettingX + (float)fr.getStringWidth(doubleSetting.getDisplayName()) + 3.0f;
                    double endSliderX = startSliderX + 100.0;
                    double length = endSliderX - startSliderX;
                    double numberX = startSliderX + (((DoubleSetting)doubleSetting).getValue() - ((DoubleSetting)doubleSetting).getMin()) * length / (((DoubleSetting)doubleSetting).getMax() - ((DoubleSetting)doubleSetting).getMin());
                    if (sHolder.isHoldingMouse() && (double)mouseX >= startSliderX && (double)mouseX <= endSliderX && (float)mouseY > startSettingY2 && (float)mouseY < endSettingY) {
                        double mousePos = (double)mouseX - startSliderX;
                        double thing = mousePos / length;
                        ((DoubleSetting)doubleSetting).setValue(thing * (((DoubleSetting)doubleSetting).getMax() - ((DoubleSetting)doubleSetting).getMin()) + ((DoubleSetting)doubleSetting).getMin());
                    }
                    this.renderNumberSettingSlider(startSliderX, endSliderX, numberX, startSettingY2, endSettingY);
                    fr.drawStringWithShadow(doubleSetting.getDisplayName(), startSettingX, startSettingY2 + 3.5f, -1);
                    fr.drawStringWithShadow("" + ((DoubleSetting)doubleSetting).getValue(), startSettingX + (float)fr.getStringWidth(doubleSetting.getDisplayName()) + 106.0f, startSettingY2 + 3.5f, -1);
                } else if (abstractSetting instanceof IntegerSetting) {
                    IntegerSetting intSetting = (IntegerSetting)sHolder.getSetting();
                    startSettingX = renderSettingX;
                    endSettingX = renderSettingX + 100;
                    startSettingY = (float)renderSettingY + 1.5f;
                    float endSettingY = (float)renderSettingY + 14.5f;
                    double startSliderX = startSettingX + (float)fr.getStringWidth(intSetting.getDisplayName()) + 3.0f;
                    double endSliderX = startSliderX + 100.0;
                    double length = endSliderX - startSliderX;
                    double numberX = startSliderX + (double)(intSetting.getValue() - intSetting.getMin()) * length / (double)(intSetting.getMax() - intSetting.getMin());
                    if (sHolder.isHoldingMouse() && (double)mouseX >= startSliderX && (double)mouseX <= endSliderX && (float)mouseY > startSettingY && (float)mouseY < endSettingY) {
                        double mousePos = (double)mouseX - startSliderX;
                        double thing = mousePos / length;
                        int value = (int)(thing * (double)(intSetting.getMax() - intSetting.getMin()) + (double)intSetting.getMin());
                        intSetting.setValue(value);
                    }
                    this.renderNumberSettingSlider(startSliderX, endSliderX, numberX, startSettingY, endSettingY);
                    fr.drawStringWithShadow(intSetting.getDisplayName(), startSettingX, startSettingY + 3.5f, -1);
                    fr.drawStringWithShadow("" + intSetting.getValue(), startSettingX + (float)fr.getStringWidth(intSetting.getDisplayName()) + 106.0f, startSettingY + 3.5f, -1);
                } else if (abstractSetting instanceof CustomDoubleSetting) {
                    doubleSetting = (CustomDoubleSetting)sHolder.getSetting();
                    startSettingX = renderSettingX;
                    endSettingX = renderSettingX + 100;
                    startSettingY = (float)renderSettingY + 1.5f;
                    float endSettingY = (float)renderSettingY + 14.5f;
                    String aaa = doubleSetting.getDisplayName() + " : " + (this.customDSetting == doubleSetting ? this.customDSettingText : Double.valueOf(((CustomDoubleSetting)doubleSetting).getValue()));
                    fr.drawStringWithShadow(aaa, startSettingX, startSettingY + 3.5f, -1);
                    this.deleteKeyPressed = Keyboard.isKeyDown((int)14);
                    if (this.deleteKeyPressed) {
                        if (this.deleteTimer.getTimeElapsed() >= 600L && this.deleteSpeedTimer.getTimeElapsed() >= 30L) {
                            this.deleteCustomDoubleCharacter();
                            this.deleteSpeedTimer.reset();
                        }
                    } else {
                        this.deleteTimer.reset();
                    }
                    if (this.cursorTimer.getTimeElapsed() % 1200L < 600L && this.customDSetting == doubleSetting) {
                        Gui.drawRect((double)(startSettingX + (float)fr.getStringWidth(aaa) + 1.0f), (double)(startSettingY + 1.5f), (double)(startSettingX + (float)fr.getStringWidth(aaa) + 2.5f), (double)(startSettingY + 13.0f), (int)new Color(25, 25, 25).getRGB());
                    }
                }
                renderSettingY += 14;
            }
        }
    }

    public void handleMouseInput() throws IOException {
        super.handleMouseInput();
        int i = Integer.signum(Mouse.getEventDWheel());
        this.targetScrollY -= (float)(i * 8);
        this.targetScrollY = Math.max(-200.0f, Math.min(200.0f, this.targetScrollY));
    }

    private void renderNumberSettingSlider(double startX, double endX, double numberX, double startSettingY, double endSettingY) {
        int color = new Color(12, 12, 12).getRGB();
        Gui.drawRect((double)startX, (double)(startSettingY + 1.0), (double)endX, (double)(endSettingY - 1.0), (int)0x65000000);
        double aaa = Math.min(Math.max(numberX, startX + 1.0), endX - 1.0);
        Gui.drawRect((double)(aaa - 1.0), (double)(startSettingY + 1.0), (double)(aaa + 1.0), (double)(endSettingY - 1.0), (int)color);
    }

    protected void mouseClicked(int mouseX, int mouseY, int button) throws IOException {
        AcrimonyFont fr = Shiver.instance.getFontManager().getSfpro();
        ScaledResolution sr = new ScaledResolution(this.mc);
        int fontHeight = FontUtil.getFontHeight(Shiver.instance.getFontManager().getSfpro().toString());
        int middleX = sr.getScaledWidth() / 2;
        int middleY = sr.getScaledHeight() / 2;
        int startWindowX = middleX - 240;
        int endWindowX = middleX + 240;
        int startWindowY = middleY - 180;
        int endWindowY = middleY + 180;
        int categoryAmount = Category.values().length;
        int startCategoryX = middleX - 210;
        int endCategoryX = middleX + 260;
        float diff = endCategoryX - startCategoryX;
        int renderModuleX = startWindowX + 20;
        int renderModuleY = startWindowY + 45;
        int i = 0;
        for (CategoryHolder category : this.categories) {
            int nameLength = fr.getStringWidth(category.getCategory().name());
            float posX = (float)startCategoryX + (float)i * diff / (float)categoryAmount;
            int posY = startWindowY + 10;
            int startX = (int)(posX - 5.0f);
            int startY = posY - 5;
            int endX = (int)(posX + (float)nameLength + 5.0f);
            int endY = posY + fontHeight + 5;
            if (mouseX > startX && mouseX < endX && mouseY > startY && mouseY < endY && button == 0 && category != this.currentCategory) {
                this.currentCategory = category;
                this.currentModule = null;
                this.setCustomDoubleSetting();
            }
            if (category == this.currentCategory) {
                for (ModuleHolder m : category.getModules()) {
                    int nameWidth = fr.getStringWidth(m.getModule().getName());
                    int startModuleX = renderModuleX - 5;
                    int startModuleY = renderModuleY - 5 + this.scrollY;
                    int endModuleX = renderModuleX + nameWidth + 5;
                    int endModuleY = renderModuleY + fontHeight + 5 + this.scrollY;
                    if (mouseX > startModuleX && mouseX < endModuleX && mouseY > startModuleY && mouseY < endModuleY) {
                        if (button == 0) {
                            m.getModule().toggle();
                        } else if (button == 1) {
                            if (this.currentModule != m) {
                                this.setCustomDoubleSetting();
                                this.currentModule = m;
                            } else {
                                this.setCustomDoubleSetting();
                                this.currentModule = null;
                            }
                        }
                    }
                    renderModuleY += 20;
                }
            }
            ++i;
        }
        if (this.currentModule != null) {
            int renderSettingX = startWindowX + 120;
            int renderSettingY = startWindowY + 38;
            for (SettingHolder sHolder : this.currentModule.getSettings()) {
                Object abstractSetting = sHolder.getSetting();
                if (!((AbstractSetting)abstractSetting).getVisibility().get().booleanValue()) continue;
                if (abstractSetting instanceof BooleanSetting) {
                    BooleanSetting boolSetting = (BooleanSetting)sHolder.getSetting();
                    float startX = renderSettingX;
                    float startY = (float)renderSettingY + 2.5f;
                    float endX = renderSettingX + fr.getStringWidth(boolSetting.getDisplayName() + " : " + boolSetting.isEnabled());
                    float endY = startY + (float)fontHeight;
                    if ((float)mouseX > startX && (float)mouseX < endX && (float)mouseY > startY && (float)mouseY < endY && button == 0) {
                        boolSetting.setEnabled(!boolSetting.isEnabled());
                    }
                } else if (abstractSetting instanceof ModeSetting) {
                    ModeSetting modeSetting = (ModeSetting)sHolder.getSetting();
                    float startX = renderSettingX;
                    float startY = (float)renderSettingY + 2.5f;
                    float endX = renderSettingX + fr.getStringWidth(modeSetting.getDisplayName() + " : " + modeSetting.getMode());
                    float endY = startY + (float)fontHeight;
                    if ((float)mouseX > startX && (float)mouseX < endX && (float)mouseY > startY && (float)mouseY < endY) {
                        if (button == 0) {
                            modeSetting.increment();
                        } else if (button == 1) {
                            modeSetting.decrement();
                        }
                    }
                } else if (abstractSetting instanceof EnumModeSetting) {
                    EnumModeSetting enumSetting = (EnumModeSetting)sHolder.getSetting();
                    float startX = renderSettingX;
                    float startY = (float)renderSettingY + 2.5f;
                    float endX = renderSettingX + fr.getStringWidth(enumSetting.getDisplayName() + " : " + enumSetting.getMode());
                    float endY = startY + (float)fontHeight;
                    if ((float)mouseX > startX && (float)mouseX < endX && (float)mouseY > startY && (float)mouseY < endY) {
                        if (button == 0) {
                            enumSetting.increment();
                        } else if (button == 1) {
                            enumSetting.decrement();
                        }
                    }
                } else if (abstractSetting instanceof IntegerSetting || abstractSetting instanceof DoubleSetting) {
                    int startSettingX = renderSettingX;
                    int endSettingX = renderSettingX + 100;
                    int startSettingY = renderSettingY + 1;
                    int endSettingY = renderSettingY + 15;
                    double startSliderX = startSettingX + fr.getStringWidth(((AbstractSetting)abstractSetting).getDisplayName()) + 3;
                    double endSliderX = startSliderX + 100.0;
                    if ((double)mouseX >= startSliderX && (double)mouseX <= endSliderX && mouseY >= startSettingY && mouseY <= endSettingY) {
                        sHolder.setHoldingMouse(true);
                    }
                } else if (abstractSetting instanceof CustomDoubleSetting) {
                    CustomDoubleSetting doubleSetting = (CustomDoubleSetting)sHolder.getSetting();
                    float startSettingX = renderSettingX;
                    float endSettingX = renderSettingX + 100;
                    float startSettingY = (float)renderSettingY + 1.5f;
                    float endSettingY = (float)renderSettingY + 14.5f;
                    int length = fr.getStringWidth(doubleSetting.getDisplayName() + " : " + (this.customDSetting == doubleSetting ? this.customDSettingText : Double.valueOf(doubleSetting.getValue())));
                    if ((float)mouseX >= startSettingX && (float)mouseX <= startSettingX + Math.max(endSettingX, (float)(length + 20)) && (float)mouseY > startSettingY && (float)mouseY < endSettingY) {
                        if (this.customDSetting != doubleSetting) {
                            if (this.customDSetting != null) {
                                try {
                                    this.customDSetting.setValue(Double.parseDouble(this.customDSettingText));
                                }
                                catch (Exception exception) {
                                    // empty catch block
                                }
                            }
                            this.customDSetting = doubleSetting;
                            this.customDSettingText = "" + doubleSetting.getValue();
                            this.cursorTimer.reset();
                            this.deleteTimer.reset();
                            this.deleteSpeedTimer.reset();
                            this.deleteKeyPressed = Keyboard.isKeyDown((int)14);
                        }
                    } else {
                        this.setCustomDoubleSetting();
                    }
                }
                renderSettingY += 14;
            }
        }
    }

    protected void mouseReleased(int mouseX, int mouseY, int state) {
        this.categories.forEach(c -> c.getModules().forEach(m -> m.getSettings().forEach(s -> s.setHoldingMouse(false))));
    }

    protected void keyTyped(char typedChar, int key) throws IOException {
        if (key == 1) {
            this.mc.displayGuiScreen(null);
            if (this.mc.currentScreen == null) {
                this.mc.setIngameFocus();
            }
            this.setCustomDoubleSetting();
        } else if (this.customDSetting != null) {
            if (key == 28) {
                this.setCustomDoubleSetting();
            } else if (key == 14) {
                this.deleteCustomDoubleCharacter();
            } else if (ChatAllowedCharacters.isAllowedCharacter((char)typedChar)) {
                this.customDSettingText = this.customDSettingText + typedChar;
            }
        }
    }

    private void setCustomDoubleSetting() {
        if (this.customDSetting != null) {
            try {
                this.customDSetting.setValue(Double.parseDouble(this.customDSettingText));
            }
            catch (Exception exception) {
                // empty catch block
            }
        }
        this.customDSetting = null;
        this.customDSettingText = "";
        this.cursorTimer.reset();
        this.deleteTimer.reset();
        this.deleteKeyPressed = false;
    }

    private void deleteCustomDoubleCharacter() {
        this.customDSettingText = this.customDSettingText.length() >= 2 ? this.customDSettingText.substring(0, this.customDSettingText.length() - 1) : "";
        this.cursorTimer.reset();
        this.deleteTimer.reset();
        this.deleteSpeedTimer.reset();
    }

    public void onGuiClosed() {
        this.module.setEnabled(false);
        this.categories.forEach(c -> c.getModules().forEach(m -> m.getSettings().forEach(s -> s.setHoldingMouse(false))));
    }

    public boolean doesGuiPauseGame() {
        return false;
    }
}

