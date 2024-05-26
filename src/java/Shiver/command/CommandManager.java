package Shiver.command;

import Shiver.Shiver;
import Shiver.command.impl.Bind;
import Shiver.command.impl.Config;
import Shiver.command.impl.ConfigList;
import Shiver.command.impl.Toggle;
import Shiver.event.Listener;
import Shiver.event.impl.ChatSendEvent;
import Shiver.ui.notification.Notification;
import Shiver.ui.notification.NotificationType;
import java.util.ArrayList;

public class CommandManager {
    public final ArrayList<Command> commands = new ArrayList();

    public CommandManager() {
        Shiver.instance.getEventManager().register(this);
        this.commands.add(new Toggle());
        this.commands.add(new Bind());
        this.commands.add(new Config());
        this.commands.add(new ConfigList());
    }

    @Listener
    public void onChatSend(ChatSendEvent event) {
        String message = event.getMessage();
        if (message.startsWith(".")) {
            Object command;
            event.setCancelled(true);
            String commandName = "";
            for (int i = 0; i < message.length(); ++i) {
                if (i <= 0) continue;
                char c = message.charAt(i);
                if (c == ' ') break;
                commandName = commandName + c;
            }
            if ((command = this.getCommandByName(commandName)) != null) {
                String commandWithoutDot = message.substring(1);
                String[] commandParts = commandWithoutDot.split(" ");
                ((Command)command).onCommand(commandParts);
            } else {
                Shiver.instance.getNotificationHandler().postNotification(new Notification(NotificationType.ERROR, "Command", "Command not found.", 3000L));
            }
        }
    }

    public <T extends Command> T getCommandByName(String name) {
        for (Command command : this.commands) {
            if (command.getName().equalsIgnoreCase(name)) {
                return (T)command;
            }
            if (command.getAliases() == null || command.getAliases().length <= 0) continue;
            for (String alias : command.getAliases()) {
                if (!alias.equalsIgnoreCase(name)) continue;
                return (T)command;
            }
        }
        return null;
    }
}

