package Shiver;

import Shiver.command.CommandManager;
import Shiver.event.EventManager;
import Shiver.filesystem.FileSystem;
import Shiver.font.FontManager;
import Shiver.handler.client.BalanceHandler;
import Shiver.handler.client.BlurHandler;
import Shiver.handler.client.CameraHandler;
import Shiver.handler.client.KeybindHandler;
import Shiver.handler.client.SlotSpoofHandler;
import Shiver.handler.packet.PacketBlinkHandler;
import Shiver.handler.packet.PacketDelayHandler;
import Shiver.module.ModuleManager;
import Shiver.ui.menu.ShiverMenu;
import Shiver.ui.notification.NotificationHandler;
import Shiver.util.AcrimonyClientUtil;
import Shiver.util.IMinecraft;
import Shiver.util.render.FontUtil;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;

import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiScreen;
import net.viamcp.ViaMCP;
import tech.skidonion.obfuscator.annotations.NativeObfuscation;

@NativeObfuscation(virtualize = NativeObfuscation.VirtualMachine.TIGER_BLACK)
public class Shiver
implements IMinecraft {
    public static final Shiver instance = new Shiver();
    public final String name = "Shiver";
    public final String version = "v2.0";
    private EventManager eventManager;
    private ModuleManager moduleManager;
    private CommandManager commandManager;
    private PacketDelayHandler packetDelayHandler;
    private PacketBlinkHandler packetBlinkHandler;
    private AcrimonyClientUtil acrimonyClientUtil;
    private NotificationHandler notificationHandler;
    private KeybindHandler keybindHandler;
    private BalanceHandler balanceHandler;
    private CameraHandler cameraHandler;
    private SlotSpoofHandler slotSpoofHandler;
    private FileSystem fileSystem;
    private FontManager fontManager;
    public BlurHandler blurHandler;
    private boolean destructed;

    public void start() throws IOException, NoSuchAlgorithmException {
        this.eventManager = new EventManager();
        this.moduleManager = new ModuleManager();
        this.commandManager = new CommandManager();
        this.packetDelayHandler = new PacketDelayHandler();
        this.packetBlinkHandler = new PacketBlinkHandler();
        this.acrimonyClientUtil = new AcrimonyClientUtil();
        this.notificationHandler = new NotificationHandler();
        this.keybindHandler = new KeybindHandler();
        this.balanceHandler = new BalanceHandler();
        this.slotSpoofHandler = new SlotSpoofHandler();
        this.cameraHandler = new CameraHandler();
        this.fileSystem = new FileSystem();
        this.fontManager = new FontManager();
        this.blurHandler = new BlurHandler(true);
        this.fileSystem.loadDefaultConfig();
        this.fileSystem.loadKeybinds();
        this.moduleManager.modules.forEach(m -> m.onClientStarted());
        FontUtil.initFonts();
        try {
            ViaMCP.getInstance().start();
            ViaMCP.getInstance().initAsyncSlider();
            ViaMCP.getInstance().initAsyncSlider(10, 7, 70, 20);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void shutdown() {
        if (!this.destructed) {
            Shiver.instance.fileSystem.saveDefaultConfig();
            Shiver.instance.fileSystem.saveKeybinds();
        }
    }

    public GuiScreen getMainMenu() {
        return this.destructed ? new GuiMainMenu() : new ShiverMenu();
    }

    public String getName() {
        return this.name;
    }

    public String getVersion() {
        return this.version;
    }

    public EventManager getEventManager() {
        return this.eventManager;
    }

    public ModuleManager getModuleManager() {
        return this.moduleManager;
    }

    public CommandManager getCommandManager() {
        return this.commandManager;
    }

    public PacketDelayHandler getPacketDelayHandler() {
        return this.packetDelayHandler;
    }

    public PacketBlinkHandler getPacketBlinkHandler() {
        return this.packetBlinkHandler;
    }

    public AcrimonyClientUtil getAcrimonyClientUtil() {
        return this.acrimonyClientUtil;
    }

    public NotificationHandler getNotificationHandler() {
        return this.notificationHandler;
    }

    public KeybindHandler getKeybindHandler() {
        return this.keybindHandler;
    }

    public BalanceHandler getBalanceHandler() {
        return this.balanceHandler;
    }

    public CameraHandler getCameraHandler() {
        return this.cameraHandler;
    }

    public SlotSpoofHandler getSlotSpoofHandler() {
        return this.slotSpoofHandler;
    }

    public FileSystem getFileSystem() {
        return this.fileSystem;
    }

    public FontManager getFontManager() {
        return this.fontManager;
    }

    public BlurHandler getBlurHandler() {
        return this.blurHandler;
    }

    public boolean isDestructed() {
        return this.destructed;
    }

    public void setDestructed(boolean destructed) {
        this.destructed = destructed;
    }
}

