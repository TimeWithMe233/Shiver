package Shiver.module.impl.visual;

import Shiver.Shiver;
import Shiver.event.Listener;
import Shiver.event.impl.PacketReceiveEvent;
import Shiver.event.impl.PacketSendEvent;
import Shiver.font.AcrimonyFont;
import Shiver.module.AlignType;
import Shiver.module.Category;
import Shiver.module.HUDModule;
import Shiver.setting.impl.BooleanSetting;
import Shiver.setting.impl.ModeSetting;
import Shiver.util.render.DrawUtil;
import java.awt.Color;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.shader.Framebuffer;
import net.minecraft.network.handshake.client.C00Handshake;
import net.minecraft.network.play.server.S01PacketJoinGame;
import net.minecraft.network.play.server.S02PacketChat;
import net.minecraft.network.play.server.S40PacketDisconnect;

public class SessionInfo
extends HUDModule {
    private final ModeSetting mode = new ModeSetting("Mode", "Future", "Future", "Outline", "Blur");
    private Framebuffer stencilFramebuffer = new Framebuffer(1, 1, false);
    private final BooleanSetting blurbg = new BooleanSetting("Blur Background", () -> this.mode.is("Future"), false);
    private AcrimonyFont sfpro;
    private AcrimonyFont sfprobold;
    private AcrimonyFont icons;
    private ClientTheme theme;
    private boolean initialised;
    private long gameJoined;
    private long killAmount;
    public long currentTime;
    private int winsAmount;
    private int lossAmount;
    private long timeJoined;

    public SessionInfo() {
        super("SessionInfo", Category.VISUAL, 8.0, 160.0, 170, 58, AlignType.LEFT);
        this.addSettings(this.mode, this.blurbg);
        this.posX.setValue(this.width);
        this.posY.setValue(this.height);
    }

    @Override
    public void onEnable() {
        super.onEnable();
        this.currentTime = System.currentTimeMillis();
    }

    private void initialise() {
        this.theme = Shiver.instance.getModuleManager().getModule(ClientTheme.class);
        this.sfpro = Shiver.instance.getFontManager().getSfpro();
        this.sfprobold = Shiver.instance.getFontManager().getSfprobold();
        this.icons = Shiver.instance.getFontManager().getIcon();
    }

    @Override
    public void onClientStarted() {
        this.timeJoined = System.currentTimeMillis();
    }

    @Listener
    public void onSend(PacketSendEvent event) {
        if (event.getPacket() instanceof C00Handshake) {
            this.currentTime = System.currentTimeMillis();
            this.currentTime = 0L;
            this.timeJoined = System.currentTimeMillis();
        }
    }

    @Listener
    public void onReceive(PacketReceiveEvent event) {
        try {
            if (event.getPacket() instanceof S02PacketChat) {
                S02PacketChat packet = (S02PacketChat)event.getPacket();
                String message = packet.getChatComponent().getUnformattedText();
                if (message.contains(mc.getSession().getUsername() + " wants to fight!")) {
                    ++this.gameJoined;
                }
                if (message.contains(mc.getSession().getUsername() + " has joined")) {
                    ++this.gameJoined;
                }
                if (message.contains("by " + mc.getSession().getUsername())) {
                    ++this.killAmount;
                }
            }
            if (event.getPacket() instanceof S01PacketJoinGame) {
                // empty if block
            }
            if (event.getPacket() instanceof S40PacketDisconnect) {
                this.currentTime = 0L;
                this.timeJoined = System.currentTimeMillis();
            }
        }
        catch (Exception var4) {
            var4.printStackTrace();
        }
    }

    @Override
    @Listener
    protected void renderModule(boolean inChat) {
        boolean canRender = inChat;
        if (inChat && this.isEnabled()) {
            this.renderSession(canRender);
        } else if (this.isEnabled()) {
            this.renderSession(canRender);
        }
    }

    public String getSessionLengthString() {
        long totalSeconds = (System.currentTimeMillis() - this.timeJoined) / 1000L;
        long hours = totalSeconds / 3600L;
        long minutes = totalSeconds % 3600L / 60L;
        long seconds = totalSeconds % 60L;
        return (hours > 0L ? hours + "h, " : "") + (minutes > 0L ? minutes + "m, " : "") + seconds + "s";
    }

    @Override
    public void onDisable() {
        this.currentTime = 0L;
        this.timeJoined = System.currentTimeMillis();
    }

    private void renderSession(boolean canRender) {
        if (!this.initialised) {
            this.initialise();
            this.initialised = true;
        }
        ScaledResolution sr = new ScaledResolution(mc);
        AcrimonyFont icon1 = Shiver.instance.getFontManager().getIconregular();
        if (SessionInfo.mc.gameSettings.showDebugInfo) {
            return;
        }
        int x = (int)this.posX.getValue();
        int y = (int)this.posY.getValue();
        String username = Shiver.instance.getModuleManager().getModule(NameProtect.class).isEnabled() ? "User" : mc.getSession().getUsername();
        long time = System.currentTimeMillis() - this.currentTime;
        switch (this.mode.getMode()) {
            case "Future": {
                if (this.blurbg.isEnabled()) {
                    Shiver.instance.blurHandler.blur((double)x, (double)y, (double)this.width, (double)this.height, 0.0f);
                }
                DrawUtil.drawRoundedRect(x, y, x + this.width, y + this.height, 1.0, Integer.MIN_VALUE);
                for (float i = (float)x; i < (float)(x + this.width); i += 1.0f) {
                    Gui.drawRect((double)i, (double)(y + 14), (double)(i + 1.0f), (double)(y + 15), (int)this.theme.getColor((int)(i * 10.0f)));
                }
                icon1.drawStringWithShadow("b", (double)(x + 4), (double)y + 4.5, this.theme.getColor(1));
                this.sfprobold.drawStringWithShadow("Session Stats", x + 20, y + 4, -1);
                icon1.drawStringWithShadow("a", x + 4, y + 20, this.theme.getColor(1));
                this.sfpro.drawStringWithShadow("Length: " + this.getSessionLengthString(), x + 20, y + 20, -1);
                icon1.drawStringWithShadow("f", (double)(x + 4), (double)y + 32.5, this.theme.getColor(1));
                this.sfpro.drawStringWithShadow("Username: " + username, x + 20, y + 32, -1);
                icon1.drawStringWithShadow("c", x + 4, y + 46, this.theme.getColor(1));
                this.sfpro.drawStringWithShadow("Kills: " + this.killAmount, x + 20, y + 46, -1);
                break;
            }
            case "Outline": {
                DrawUtil.drawRoundedRect(x, y, x + this.width, y + this.height, 1.0, Integer.MIN_VALUE);
                DrawUtil.drawGradientSideways(x, y + this.height, x + this.width, y + this.height + 2, ClientTheme.color1.getRGB(), ClientTheme.color2.getRGB());
                DrawUtil.drawGradientSideways(x, y, x + this.width, y + 2, ClientTheme.color1.getRGB(), ClientTheme.color2.getRGB());
                Gui.drawRect((double)(x - 2), (double)y, (double)x, (double)(y + this.height + 2), (int)ClientTheme.color1.getRGB());
                Gui.drawRect((double)(x + this.width), (double)y, (double)(x + this.width + 2), (double)(y + this.height + 2), (int)ClientTheme.color2.getRGB());
                this.sfprobold.drawStringWithShadow("Session Stats", x + 48, y + 8, -1);
                this.sfpro.drawStringWithShadow("Session Time: " + this.getSessionLengthString(), x + 8, y + 22, -1);
                this.sfpro.drawStringWithShadow("Username: " + username, x + 8, y + 34, -1);
                this.sfpro.drawStringWithShadow("Kills: " + this.killAmount, x + 8, y + 46, -1);
                break;
            }
            case "Blur": {
                Shiver.instance.blurHandler.bloom(x, y, this.width, this.height, ClientTheme.blurradius.getValue(), new Color(0, 0, 0, 150));
                for (float i = (float)x; i < (float)(x + this.width); i += 1.0f) {
                    Gui.drawRect((double)i, (double)(y + 16), (double)(i + 1.0f), (double)(y + 17), (int)this.theme.getColor((int)(i * 10.0f)));
                }
                this.sfprobold.drawStringWithShadow("Session Stats", x + 48, y + 6, -1);
                this.sfpro.drawStringWithShadow("Session Time: " + this.getSessionLengthString(), x + 8, y + 22, -1);
                this.sfpro.drawStringWithShadow("Username: " + username, x + 8, y + 34, -1);
                this.sfpro.drawStringWithShadow("Kills: " + this.killAmount, x + 8, y + 46, -1);
            }
        }
    }

    @Override
    public String getSuffix() {
        return this.mode.getMode();
    }
}

