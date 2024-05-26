package Shiver.command.impl;

import Shiver.autoconfig.DownloadConfig;
import Shiver.command.Command;
import Shiver.util.misc.LogUtil;

public class ConfigList
extends Command {
    public ConfigList() {
        super("ConfigList", "ConfigList.", "list");
    }

    @Override
    public void onCommand(String[] args) {
        if (args.length >= 2) {
            String action;
            switch (action = args[1].toLowerCase()) {
                case "onlinelist": {
                    LogUtil.addChatMessage(String.valueOf("List of online configs: " + DownloadConfig.getOnlineConfigList(DownloadConfig.repoOwner, DownloadConfig.repoName, DownloadConfig.token)));
                }
            }
        }
    }
}

