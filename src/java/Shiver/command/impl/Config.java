package Shiver.command.impl;

import Shiver.Shiver;
import Shiver.autoconfig.DownloadConfig;
import Shiver.command.Command;
import Shiver.ui.notification.Notification;
import Shiver.ui.notification.NotificationType;
import Shiver.util.misc.LogUtil;
import java.util.List;

public class Config
extends Command {
    public Config() {
        super("Config", "Loads or saves a config.");
    }

    @Override
    public void onCommand(String[] args) {
        if (args.length >= 3) {
            String action = args[1].toLowerCase();
            String configName = args[2];
            switch (action) {
                case "load": {
                    boolean success = Shiver.instance.getFileSystem().loadConfig(configName, false);
                    if (success) {
                        Shiver.instance.getNotificationHandler().postNotification(new Notification(NotificationType.WARNING, "Config announcement", "Loaded config " + configName + " in game.", 3000L));
                        break;
                    }
                    Shiver.instance.getNotificationHandler().postNotification(new Notification(NotificationType.WARNING, "Config announcement", "Config not found in FileSystem, Please make sure you type the correct config name in Chat.", 3000L));
                    break;
                }
                case "save": {
                    Shiver.instance.getFileSystem().saveConfig(configName);
                    Shiver.instance.getNotificationHandler().postNotification(new Notification(NotificationType.WARNING, "Config announcement", "Saved config as " + configName + " in game.", 3000L));
                    break;
                }
                case "install": {
                    DownloadConfig.downloadConfig(configName, DownloadConfig.repoOwner, DownloadConfig.repoName, DownloadConfig.downloadPath, DownloadConfig.token);
                    System.out.println("testsss");
                }
            }
        }
        if (args[2].toLowerCase().equals("onlinelist")) {
            List<String> fileList = DownloadConfig.getOnlineConfigList(DownloadConfig.repoOwner, DownloadConfig.repoName, DownloadConfig.token);
            System.out.println(fileList);
            if (fileList != null) {
                for (String file : fileList) {
                    LogUtil.addChatMessage("Acrimony: " + file);
                }
            } else {
                Shiver.instance.getNotificationHandler().postNotification(new Notification(NotificationType.WARNING, "Config announcement", "Error Fetching List of Online Configs", 3000L));
            }
        }
    }
}

