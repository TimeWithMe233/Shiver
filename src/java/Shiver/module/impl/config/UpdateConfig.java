package Shiver.module.impl.config;

import Shiver.autoconfig.DownloadConfig;
import Shiver.module.Category;
import Shiver.module.EventListenType;
import Shiver.module.Module;
import tech.skidonion.obfuscator.annotations.NativeObfuscation;

@NativeObfuscation(virtualize = NativeObfuscation.VirtualMachine.TIGER_BLACK)
public class UpdateConfig
extends Module {
    public UpdateConfig() {
        super("Update Config", Category.CONFIG);
        this.listenType = EventListenType.MANUAL;
        this.setStateHidden(true);
        this.startListening();
        this.setEnabledSilently(true);
    }

    @Override
    public void onEnable() {
        DownloadConfig.init();
        this.toggle();
    }
}

