package Shiver.handler.client;

import Shiver.Shiver;
import Shiver.event.Listener;
import Shiver.event.impl.KeyPressEvent;

public class KeybindHandler {
    public KeybindHandler() {
        Shiver.instance.getEventManager().register(this);
    }

    @Listener
    public void onKeyPress(KeyPressEvent event) {
        Shiver.instance.getModuleManager().modules.stream().filter(m -> m.getKey() == event.getKey()).forEach(m -> m.toggle());
    }
}

