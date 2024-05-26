package Shiver.command.impl;

import Shiver.Shiver;
import Shiver.command.Command;
import Shiver.module.Module;
import Shiver.ui.notification.Notification;
import Shiver.ui.notification.NotificationType;

public class Toggle
extends Command {
    public Toggle() {
        super("Toggle", "Turns on or off the specified module.", "t");
    }

    @Override
    public void onCommand(String[] args) {
        if (args.length >= 2) {
            Object module = Shiver.instance.getModuleManager().getModuleByNameNoSpace(args[1]);
            if (module != null) {
                ((Module)module).toggle();
                Shiver.instance.getNotificationHandler().postNotification(new Notification(NotificationType.WARNING, "Toggle Module", (((Module)module).isEnabled() ? "Enabled " : "Disabled ") + ((Module)module).getName(), 3000L));
            }
        } else {
            Shiver.instance.getNotificationHandler().postNotification(new Notification(NotificationType.WARNING, "Toggle Module", "Usage : .t/toggle modulename", 3000L));
        }
    }
}

