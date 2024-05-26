package Shiver.ui.notification;

import Shiver.Shiver;
import Shiver.event.Listener;
import Shiver.event.impl.RenderEvent;
import Shiver.font.AcrimonyFont;
import Shiver.module.impl.visual.ClientTheme;
import Shiver.module.impl.visual.HUD;
import Shiver.util.IMinecraft;
import Shiver.util.animation.AnimationHolder;
import Shiver.util.animation.AnimationType;
import Shiver.util.render.DrawUtil;
import java.awt.Color;
import java.util.ArrayList;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.ResourceLocation;

public class NotificationHandler
implements IMinecraft {
    private final ArrayList<AnimationHolder<Notification>> notifications = new ArrayList();
    private ClientTheme theme;

    public NotificationHandler() {
        Shiver.instance.getEventManager().register(this);
        this.theme = Shiver.instance.getModuleManager().getModule(ClientTheme.class);
    }

    @Listener
    public void onRender(RenderEvent event) {
        if (HUD.notification.isEnabled()) {
            switch (HUD.notificationmode.getMode()) {
                case "Simple": {
                    int start;
                    if (this.notifications.isEmpty()) break;
                    ArrayList<AnimationHolder<Notification>> toRemove = new ArrayList<AnimationHolder<Notification>>();
                    int notifAmount = 0;
                    for (AnimationHolder<Notification> animationHolder : this.notifications) {
                        if (!animationHolder.isAnimDone() && animationHolder.isRendered()) {
                            animationHolder.setAnimType(AnimationType.SLIDE);
                            animationHolder.setAnimDuration(500L);
                        } else {
                            animationHolder.setAnimType(AnimationType.SLIDE);
                            animationHolder.setAnimDuration(200L);
                        }
                        animationHolder.updateState(!animationHolder.get().shouldBeRemoved());
                        if (!animationHolder.isRendered() && animationHolder.isAnimDone()) {
                            toRemove.add(animationHolder);
                        }
                        ++notifAmount;
                    }
                    if (!toRemove.isEmpty()) {
                        for (AnimationHolder<Notification> animationHolder : toRemove) {
                            this.notifications.remove(animationHolder);
                            --notifAmount;
                        }
                        toRemove.clear();
                    }
                    if (notifAmount <= 0) break;
                    AcrimonyFont sfpro = Shiver.instance.getFontManager().getSfpro();
                    AcrimonyFont acrimonyFont = Shiver.instance.getFontManager().getSfprobold();
                    ScaledResolution sr = new ScaledResolution(mc);
                    int height = 36;
                    int spacing = 4;
                    int y = start = sr.getScaledHeight() - 2 - height * notifAmount - spacing * (notifAmount - 1);
                    for (AnimationHolder<Notification> notif : this.notifications) {
                        int textWidth = sfpro.getStringWidth(notif.get().getText());
                        int length = Math.max(textWidth, 8);
                        int startX = sr.getScaledWidth() - length;
                        int startY = y;
                        int endX = sr.getScaledWidth() - 2;
                        int endY = y + height;
                        notif.render(() -> {
                            int color = ((Notification)notif.get()).getType() == NotificationType.INFO ? new Color(33, 115, 33, 255).getRGB() : (((Notification)notif.get()).getType() == NotificationType.WARNING ? new Color(121, 121, 26, 255).getRGB() : (((Notification)notif.get()).getType() == NotificationType.ERROR ? new Color(134, 28, 28, 255).getRGB() : 0x65000000));
                            DrawUtil.drawRoundedRect(startX - 26, startY, endX, endY, 0.0, new Color(33, 33, 33).getRGB());
                            DrawUtil.drawRoundedRect(startX - 24, startY + 2, startX - 18, endY - 2, 0.0, color);
                            Shiver.instance.getFontManager().getSfpro().drawStringWithShadow(((Notification)notif.get()).getTitle(), startX - 8, startY + 8, -1);
                            sfpro.drawStringWithShadow(((Notification)notif.get()).getText(), startX - 8, startY + 20, -1);
                        }, startX, startY, endX, endY);
                        y += height + spacing;
                    }
                    break;
                }
                case "Outline": {
                    int start;
                    if (this.notifications.isEmpty()) break;
                    ArrayList<AnimationHolder<Notification>> toRemove = new ArrayList<AnimationHolder<Notification>>();
                    int notifAmount = 0;
                    for (AnimationHolder<Notification> animationHolder : this.notifications) {
                        if (!animationHolder.isAnimDone() && animationHolder.isRendered()) {
                            animationHolder.setAnimType(AnimationType.POP);
                            animationHolder.setAnimDuration(180L);
                        } else {
                            animationHolder.setAnimType(AnimationType.POP);
                            animationHolder.setAnimDuration(150L);
                        }
                        animationHolder.updateState(!animationHolder.get().shouldBeRemoved());
                        if (!animationHolder.isRendered() && animationHolder.isAnimDone()) {
                            toRemove.add(animationHolder);
                        }
                        ++notifAmount;
                    }
                    if (!toRemove.isEmpty()) {
                        for (AnimationHolder animationHolder : toRemove) {
                            this.notifications.remove(animationHolder);
                            --notifAmount;
                        }
                        toRemove.clear();
                    }
                    if (notifAmount <= 0) break;
                    AcrimonyFont sfpro = Shiver.instance.getFontManager().getSfpro();
                    AcrimonyFont acrimonyFont = Shiver.instance.getFontManager().getSfprobold();
                    AcrimonyFont icon = Shiver.instance.getFontManager().getIcon();
                    ScaledResolution sr = new ScaledResolution(mc);
                    int height = 28;
                    int spacing = 4;
                    int y = start = sr.getScaledHeight() - 2 - height * notifAmount - spacing * (notifAmount - 1);
                    for (AnimationHolder<Notification> notif : this.notifications) {
                        int textWidth = sfpro.getStringWidth(notif.get().getText());
                        int length = Math.max(textWidth, 8);
                        int startX = sr.getScaledWidth() - length;
                        int startY = y;
                        int endX = sr.getScaledWidth() - 2;
                        int endY = y + height;
                        notif.render(() -> {
                            int color = ((Notification)notif.get()).getType() == NotificationType.INFO ? new Color(34, 143, 34, 255).getRGB() : (((Notification)notif.get()).getType() == NotificationType.WARNING ? new Color(154, 154, 26, 255).getRGB() : (((Notification)notif.get()).getType() == NotificationType.ERROR ? new Color(157, 33, 33, 255).getRGB() : 0x65000000));
                            DrawUtil.drawRoundedRect(startX - 38, startY, endX, endY, 0.0, -2063597568);
                            DrawUtil.drawRoundedRect(startX - 38, startY, startX - 10, endY, 0.0, color);
                            DrawUtil.drawGradientSideways(startX - 38, startY, endX, startY + 2, ClientTheme.color1.getRGB(), ClientTheme.color2.getRGB());
                            DrawUtil.drawGradientSideways(startX - 38, endY - 2, endX, endY, ClientTheme.color1.getRGB(), ClientTheme.color2.getRGB());
                            Gui.drawRect((double)(startX - 38), (double)startY, (double)(startX - 36), (double)endY, (int)ClientTheme.color1.getRGB());
                            Gui.drawRect((double)(endX - 2), (double)startY, (double)endX, (double)endY, (int)ClientTheme.color2.getRGB());
                            if (((Notification)notif.get()).getType() == NotificationType.INFO) {
                                icon.drawStringWithShadow("o", (double)startX - 31.5, (double)(startY + 8), -1);
                            } else if (((Notification)notif.get()).getType() == NotificationType.WARNING) {
                                icon.drawStringWithShadow("r", (double)startX - 31.5, (double)(startY + 8), -1);
                            } else if (((Notification)notif.get()).getType() == NotificationType.ERROR) {
                                icon.drawStringWithShadow("p", startX - 30, startY + 7, -1);
                            }
                            Shiver.instance.getFontManager().getSfpro().drawStringWithShadow(((Notification)notif.get()).getTitle(), startX - 6, startY + 5, -1);
                            sfpro.drawStringWithShadow(((Notification)notif.get()).getText(), startX - 6, startY + 16, -1);
                        }, startX, startY, endX, endY);
                        y += height + spacing;
                    }
                    break;
                }
                case "Blur": {
                    int start;
                    if (this.notifications.isEmpty()) break;
                    ArrayList<AnimationHolder<Notification>> toRemove = new ArrayList<AnimationHolder<Notification>>();
                    int notifAmount = 0;
                    for (AnimationHolder<Notification> animationHolder : this.notifications) {
                        if (!animationHolder.isAnimDone() && animationHolder.isRendered()) {
                            animationHolder.setAnimType(AnimationType.SLIDE);
                            animationHolder.setAnimDuration(280L);
                        } else {
                            animationHolder.setAnimType(AnimationType.SLIDE);
                            animationHolder.setAnimDuration(350L);
                        }
                        animationHolder.updateState(!animationHolder.get().shouldBeRemoved());
                        if (!animationHolder.isRendered() && animationHolder.isAnimDone()) {
                            toRemove.add(animationHolder);
                        }
                        ++notifAmount;
                    }
                    if (!toRemove.isEmpty()) {
                        for (AnimationHolder animationHolder : toRemove) {
                            this.notifications.remove(animationHolder);
                            --notifAmount;
                        }
                        toRemove.clear();
                    }
                    if (notifAmount <= 0) break;
                    AcrimonyFont sfpro = Shiver.instance.getFontManager().getSfpro();
                    AcrimonyFont acrimonyFont = Shiver.instance.getFontManager().getSfprobold();
                    AcrimonyFont icon = Shiver.instance.getFontManager().getIcon();
                    ScaledResolution sr = new ScaledResolution(mc);
                    int height = 28;
                    int spacing = 4;
                    int y = start = sr.getScaledHeight() - 2 - height * notifAmount - spacing * (notifAmount - 1);
                    for (AnimationHolder<Notification> notif : this.notifications) {
                        int textWidth = sfpro.getStringWidth(notif.get().getText());
                        int length = Math.max(textWidth, 8);
                        int startX = sr.getScaledWidth() - length;
                        int startY = y;
                        int endX = length + 36;
                        int endY = height;
                        notif.render(() -> {
                            Shiver.instance.blurHandler.bloom(startX - 38, startY, endX, endY, ClientTheme.blurradius.getValue(), new Color(0, 0, 0, 150));
                            Shiver.instance.blurHandler.bloom(startX - 38, startY, 30, endY, ClientTheme.blurradius.getValue(), this.theme.getColor(startY * -17));
                            if (((Notification)notif.get()).getType() == NotificationType.INFO) {
                                icon.drawStringWithShadow("o", (double)startX - 31.5, (double)(startY + 8), -1);
                            } else if (((Notification)notif.get()).getType() == NotificationType.WARNING) {
                                icon.drawStringWithShadow("r", (double)startX - 31.5, (double)(startY + 8), -1);
                            } else if (((Notification)notif.get()).getType() == NotificationType.ERROR) {
                                icon.drawStringWithShadow("p", startX - 30, startY + 7, -1);
                            }
                            Shiver.instance.getFontManager().getSfpro().drawStringWithShadow(((Notification)notif.get()).getTitle(), startX - 6, startY + 5, -1);
                            sfpro.drawStringWithShadow(((Notification)notif.get()).getText(), startX - 6, startY + 16, -1);
                        }, startX, startY, endX, endY);
                        y += height + spacing;
                    }
                    break;
                }
                case "Jello": {
                    int start;
                    if (this.notifications.isEmpty()) break;
                    ArrayList<AnimationHolder<Notification>> toRemove = new ArrayList<AnimationHolder<Notification>>();
                    int notifAmount = 0;
                    for (AnimationHolder<Notification> animationHolder : this.notifications) {
                        if (!animationHolder.isAnimDone() && animationHolder.isRendered()) {
                            animationHolder.setAnimType(AnimationType.SLIDE);
                            animationHolder.setAnimDuration(380L);
                        } else {
                            animationHolder.setAnimType(AnimationType.SLIDE);
                            animationHolder.setAnimDuration(180L);
                        }
                        animationHolder.updateState(!animationHolder.get().shouldBeRemoved());
                        if (!animationHolder.isRendered() && animationHolder.isAnimDone()) {
                            toRemove.add(animationHolder);
                        }
                        ++notifAmount;
                    }
                    if (!toRemove.isEmpty()) {
                        for (AnimationHolder animationHolder : toRemove) {
                            this.notifications.remove(animationHolder);
                            --notifAmount;
                        }
                        toRemove.clear();
                    }
                    if (notifAmount <= 0) break;
                    AcrimonyFont sfpro = Shiver.instance.getFontManager().getSfpro();
                    AcrimonyFont acrimonyFont = Shiver.instance.getFontManager().getSfprobold();
                    AcrimonyFont icon = Shiver.instance.getFontManager().getIcon();
                    ScaledResolution sr = new ScaledResolution(mc);
                    int height = 32;
                    int spacing = 2;
                    int y = start = sr.getScaledHeight() - 2 - height * notifAmount - spacing * (notifAmount - 1);
                    for (AnimationHolder<Notification> notif : this.notifications) {
                        int textWidth = sfpro.getStringWidth(notif.get().getText());
                        int length = Math.max(textWidth, 8);
                        int startX = sr.getScaledWidth() - length;
                        int startY = y;
                        int endX = length + 32;
                        int endY = height;
                        notif.render(() -> {
                            Shiver.instance.blurHandler.bloom(startX - 38, startY, endX, endY, 20, new Color(0, 0, 0, 150));
                            DrawUtil.drawRoundedRect(startX - 38, startY, endX + startX, endY + startY, 0.0, -2063597568);
                            DrawUtil.drawRoundedRect(startX, startY, startX, startY, 0.0, -1);
                            DrawUtil.drawImage(new ResourceLocation("minecraft", "acrimony/misc/jellowarning.png"), startX - 32, startY + 6, 20, 20);
                            Shiver.instance.getFontManager().getSfpro().drawStringWithShadow(((Notification)notif.get()).getTitle(), startX - 6, startY + 6, -1);
                            sfpro.drawStringWithShadow(((Notification)notif.get()).getText(), startX - 6, startY + 18, -1);
                        }, startX, startY, endX, endY);
                        y += height + spacing;
                    }
                    break;
                }
            }
        }
    }

    public void postNotification(Notification notif) {
        this.notifications.add(new AnimationHolder<Notification>(notif));
    }
}

