package Shiver.module;

import Shiver.module.impl.combat.Antibot;
import Shiver.module.impl.combat.Killaura;
import Shiver.module.impl.combat.TargetStrafe;
import Shiver.module.impl.combat.Teams;
import Shiver.module.impl.combat.Tickbase;
import Shiver.module.impl.combat.Velocity;
import Shiver.module.impl.config.UpdateConfig;
import Shiver.module.impl.exploit.Autoplay;
import Shiver.module.impl.exploit.Disabler;
import Shiver.module.impl.ghost.AimAssist;
import Shiver.module.impl.ghost.Autoclicker;
import Shiver.module.impl.ghost.Backtrack;
import Shiver.module.impl.ghost.DelayRemover;
import Shiver.module.impl.ghost.FastPlace;
import Shiver.module.impl.ghost.KeepSprint;
import Shiver.module.impl.ghost.Reach;
import Shiver.module.impl.ghost.Safewalk;
import Shiver.module.impl.ghost.WTap;
import Shiver.module.impl.movement.Blink;
import Shiver.module.impl.movement.Fly;
import Shiver.module.impl.movement.InventoryMove;
import Shiver.module.impl.movement.Longjump;
import Shiver.module.impl.movement.Noslow;
import Shiver.module.impl.movement.Speed;
import Shiver.module.impl.movement.Sprint;
import Shiver.module.impl.player.Antivoid;
import Shiver.module.impl.player.AutoTool;
import Shiver.module.impl.player.Breaker;
import Shiver.module.impl.player.ChestStealer;
import Shiver.module.impl.player.InventoryManager;
import Shiver.module.impl.player.Nofall;
import Shiver.module.impl.player.Scaffold;
import Shiver.module.impl.player.SpeedMine;
import Shiver.module.impl.player.Timer;
import Shiver.module.impl.visual.Animations;
import Shiver.module.impl.visual.BlockCounter;
import Shiver.module.impl.visual.Cape;
import Shiver.module.impl.visual.Chams;
import Shiver.module.impl.visual.ClickGuiModule;
import Shiver.module.impl.visual.ClientTheme;
import Shiver.module.impl.visual.Compass;
import Shiver.module.impl.visual.CustomGui;
import Shiver.module.impl.visual.ESP;
import Shiver.module.impl.visual.FakePlayer;
import Shiver.module.impl.visual.Fullbright;
import Shiver.module.impl.visual.GlowESP;
import Shiver.module.impl.visual.HUD;
import Shiver.module.impl.visual.Keystrokes;
import Shiver.module.impl.visual.NameProtect;
import Shiver.module.impl.visual.NetInfo;
import Shiver.module.impl.visual.Radar;
import Shiver.module.impl.visual.Rotations;
import Shiver.module.impl.visual.SelfDestruct;
import Shiver.module.impl.visual.SessionInfo;
import Shiver.module.impl.visual.TargetHUD;
import Shiver.module.impl.visual.TimeChanger;
import Shiver.module.impl.visual.Xray;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class ModuleManager {
    public List<Module> modules = new ArrayList<Module>();
    public List<HUDModule> hudModules;

    public ModuleManager() {
        this.modules.add(new Killaura());
        this.modules.add(new Velocity());
        this.modules.add(new TargetStrafe());
        this.modules.add(new Tickbase());
        this.modules.add(new Teams());
        this.modules.add(new Backtrack());
        this.modules.add(new Reach());
        this.modules.add(new Autoclicker());
        this.modules.add(new AimAssist());
        this.modules.add(new DelayRemover());
        this.modules.add(new WTap());
        this.modules.add(new Antibot());
        this.modules.add(new KeepSprint());
        this.modules.add(new Sprint());
        this.modules.add(new Fly());
        this.modules.add(new Speed());
        this.modules.add(new InventoryMove());
        this.modules.add(new Noslow());
        this.modules.add(new Blink());
        this.modules.add(new Safewalk());
        this.modules.add(new Longjump());
        this.modules.add(new ChestStealer());
        this.modules.add(new InventoryManager());
        this.modules.add(new Nofall());
        this.modules.add(new Antivoid());
        this.modules.add(new Timer());
        this.modules.add(new FastPlace());
        this.modules.add(new AutoTool());
        this.modules.add(new SpeedMine());
        this.modules.add(new Scaffold());
        this.modules.add(new Breaker());
        this.modules.add(new Rotations());
        this.modules.add(new HUD());
        this.modules.add(new ClientTheme());
        this.modules.add(new ClickGuiModule());
        this.modules.add(new ESP());
        this.modules.add(new Chams());
        this.modules.add(new Animations());
        this.modules.add(new TargetHUD());
        this.modules.add(new Keystrokes());
        this.modules.add(new TimeChanger());
        this.modules.add(new Fullbright());
        this.modules.add(new NameProtect());
        this.modules.add(new Xray());
        this.modules.add(new GlowESP());
        this.modules.add(new CustomGui());
        this.modules.add(new SessionInfo());
        this.modules.add(new Cape());
        this.modules.add(new Radar());
        this.modules.add(new Compass());
        this.modules.add(new BlockCounter());
        this.modules.add(new NetInfo());
        this.modules.add(new Autoplay());
        this.modules.add(new SelfDestruct());
        this.modules.add(new FakePlayer());
        this.modules.add(new UpdateConfig());
        this.modules.add(new Disabler());
        this.hudModules = this.modules.stream().filter(HUDModule.class::isInstance).map(HUDModule.class::cast).collect(Collectors.toList());
    }

    public <T extends Module> T getModule(Class<T> clazz) {
        Optional<Module> module = this.modules.stream().filter(m -> m.getClass().equals(clazz)).findFirst();
        if (module.isPresent()) {
            return (T)module.get();
        }
        return null;
    }

    public void addModules(List<Module> modules) {
        this.modules = modules;
    }

    public <T extends Module> T getModuleByName(String name) {
        Optional<Module> module = this.modules.stream().filter(m -> m.getName().equalsIgnoreCase(name)).findFirst();
        if (module.isPresent()) {
            return (T)module.get();
        }
        return null;
    }

    public <T extends Module> T getModuleByNameNoSpace(String name) {
        Optional<Module> module = this.modules.stream().filter(m -> m.getName().replace(" ", "").equalsIgnoreCase(name)).findFirst();
        if (module.isPresent()) {
            return (T)module.get();
        }
        return null;
    }
}

