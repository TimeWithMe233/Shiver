package Shiver.command.impl;

import Shiver.Shiver;
import Shiver.command.Command;
import Shiver.module.Module;
import Shiver.ui.notification.Notification;
import Shiver.ui.notification.NotificationType;
import org.lwjgl.input.Keyboard;

public class Bind
extends Command {
    public Bind() {
        super("Bind", "Changes the keybind of the specified module.");
    }

    @Override
    public void onCommand(String[] args) {
        if (args.length >= 3) {
            Object module = Shiver.instance.getModuleManager().getModuleByNameNoSpace(args[1]);
            if (module != null) {
                String keyName = args[2].toUpperCase();
                ((Module)module).setKey(Keyboard.getKeyIndex((String)keyName));
                Shiver.instance.getNotificationHandler().postNotification(new Notification(NotificationType.WARNING, "KeyBind Manager", "Bound module : " + ((Module)module).getName() + " to " + keyName, 3000L));
            }
        } else {
            Shiver.instance.getNotificationHandler().postNotification(new Notification(NotificationType.WARNING, "KeyBind Manager", "Usage : .bind module keybind", 3000L));
        }
    }
}

