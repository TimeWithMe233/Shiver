package Shiver.module.impl.visual;

import Shiver.Shiver;
import Shiver.event.Listener;
import Shiver.event.impl.RenderEvent;
import Shiver.module.Category;
import Shiver.module.Module;
import Shiver.setting.impl.BooleanSetting;
import Shiver.setting.impl.ModeSetting;
import Shiver.ui.click.dropdown.DropdownClickGUI;
import Shiver.ui.click.window.WindowClickGUI;
import net.minecraft.client.gui.GuiScreen;

public class ClickGuiModule
extends Module {
    private final ModeSetting mode = new ModeSetting("Mode", "Dropdown", "Window", "Dropdown");
    private DropdownClickGUI dropdownClickGUI;
    private WindowClickGUI window;
    public final BooleanSetting boxOnHover = new BooleanSetting("Box on hover", false);
    public final BooleanSetting boxOnSettings = new BooleanSetting("Box on settings", () -> this.boxOnHover.isEnabled(), false);
    private ClientTheme theme;

    public ClickGuiModule() {
        super("ClickGUI", Category.VISUAL);
        this.setKey(54);
        this.addSettings(this.mode, this.boxOnHover, this.boxOnSettings);
    }

    @Override
    public void onEnable() {
        switch (this.mode.getMode()) {
            case "Dropdown": {
                if (this.dropdownClickGUI == null) {
                    this.dropdownClickGUI = new DropdownClickGUI(this);
                }
                mc.displayGuiScreen((GuiScreen)this.dropdownClickGUI);
                break;
            }
            case "Window": {
                if (this.window == null) {
                    this.window = new WindowClickGUI(this);
                }
                mc.displayGuiScreen((GuiScreen)this.window);
            }
        }
    }

    @Override
    public void onClientStarted() {
        this.theme = Shiver.instance.getModuleManager().getModule(ClientTheme.class);
    }

    @Listener
    public void onRender(RenderEvent event) {
    }
}

