package Shiver.module;

import Shiver.Shiver;
import Shiver.module.impl.visual.HUD;
import Shiver.setting.AbstractSetting;
import Shiver.ui.notification.Notification;
import Shiver.ui.notification.NotificationType;
import Shiver.util.IMinecraft;
import Shiver.util.misc.AudioUtil;
import com.mojang.realmsclient.gui.ChatFormatting;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;

public abstract class Module
implements IMinecraft {
    private String name;
    private Category category;
    private boolean stateHidden;
    private int key;
    private boolean enabled;
    private boolean listening;
    protected EventListenType listenType;
    private ArrayList<AbstractSetting> settings = new ArrayList();

    public Module(String name, Category category) {
        this.name = name;
        this.category = category;
        this.listenType = EventListenType.AUTOMATIC;
    }

    protected void onEnable() {
    }

    protected void onDisable() {
    }

    public void onClientStarted() {
    }

    public final void setEnabled(boolean enabled) {
        if (this.enabled != enabled) {
            this.enabled = enabled;
            if (enabled) {
                this.onEnable();
                if (this.listenType == EventListenType.AUTOMATIC) {
                    this.startListening();
                }
            } else {
                if (this.listenType == EventListenType.AUTOMATIC) {
                    this.stopListening();
                }
                this.onDisable();
            }
        }
    }

    public final void setEnabledSilently(boolean enabled) {
        if (this.enabled != enabled) {
            this.enabled = enabled;
            if (enabled) {
                if (this.listenType == EventListenType.AUTOMATIC) {
                    this.startListening();
                }
            } else if (this.listenType == EventListenType.AUTOMATIC) {
                this.stopListening();
            }
        }
    }

    public final void toggle() {
        boolean bl = this.enabled = !this.enabled;
        if (this.enabled) {
            this.onEnable();
            if (this.listenType == EventListenType.AUTOMATIC) {
                Shiver.instance.getNotificationHandler().postNotification(new Notification(NotificationType.INFO, "Enable", "Module : " + this.getName(), 1000L));
                if (HUD.notificationsounds.isEnabled()) {
                    AudioUtil.enable();
                }
                this.startListening();
            }
        } else {
            if (this.listenType == EventListenType.AUTOMATIC) {
                Shiver.instance.getNotificationHandler().postNotification(new Notification(NotificationType.ERROR, "Disable", "Module : " + this.getName(), 1000L));
                if (HUD.notificationsounds.isEnabled()) {
                    AudioUtil.disable();
                }
                this.stopListening();
            }
            this.onDisable();
        }
    }

    public final void toggleSilently() {
        boolean bl = this.enabled = !this.enabled;
        if (this.enabled) {
            if (this.listenType == EventListenType.AUTOMATIC) {
                this.startListening();
            }
        } else if (this.listenType == EventListenType.AUTOMATIC) {
            this.stopListening();
        }
    }

    protected final void startListening() {
        if (!this.listening) {
            Shiver.instance.getEventManager().register(this);
            this.listening = true;
        }
    }

    protected final void stopListening() {
        if (this.listening) {
            Shiver.instance.getEventManager().unregister(this);
            this.listening = false;
        }
    }

    public boolean isStateHidden() {
        return this.stateHidden;
    }

    public void setStateHidden(boolean stateHidden) {
        this.stateHidden = stateHidden;
    }

    public void addSettings(AbstractSetting ... settings) {
        this.settings.addAll(Arrays.asList(settings));
    }

    public <T extends AbstractSetting> T getSettingByName(String name) {
        Optional<AbstractSetting> optional = this.settings.stream().filter(m -> m.getName().equals(name)).findFirst();
        if (optional.isPresent()) {
            return (T)optional.get();
        }
        return null;
    }

    public String getSuffix() {
        return null;
    }

    public final String getDisplayName() {
        return this.getDisplayName(ChatFormatting.GRAY);
    }

    public final String getDisplayName(ChatFormatting formatting) {
        String tag = this.getSuffix();
        if (tag == null || tag.equals("")) {
            return this.name;
        }
        return this.name + formatting + " " + tag;
    }

    public String getName() {
        return this.name;
    }

    public Category getCategory() {
        return this.category;
    }

    public int getKey() {
        return this.key;
    }

    public boolean isEnabled() {
        return this.enabled;
    }

    public boolean isListening() {
        return this.listening;
    }

    public EventListenType getListenType() {
        return this.listenType;
    }

    public ArrayList<AbstractSetting> getSettings() {
        return this.settings;
    }

    public void setKey(int key) {
        this.key = key;
    }
}

