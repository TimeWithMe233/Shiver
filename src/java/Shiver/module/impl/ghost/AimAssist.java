package Shiver.module.impl.ghost;

import Shiver.Shiver;
import Shiver.event.Listener;
import Shiver.event.impl.RenderEvent;
import Shiver.module.Category;
import Shiver.module.Module;
import Shiver.module.impl.combat.Antibot;
import Shiver.module.impl.combat.Teams;
import Shiver.setting.impl.DoubleSetting;
import Shiver.setting.impl.IntegerSetting;
import Shiver.setting.impl.ModeSetting;
import Shiver.util.misc.TimerUtil;
import Shiver.util.player.FixedRotations;
import Shiver.util.player.RotationsUtil;
import java.util.ArrayList;
import java.util.Comparator;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.MathHelper;
import org.lwjgl.input.Mouse;

public class AimAssist
extends Module {
    private Antibot antibotModule;
    private Teams teamsModule;
    private final ModeSetting filter = new ModeSetting("Filter", "Range", "Range", "Health");
    private final DoubleSetting range = new DoubleSetting("Range", 4.5, 3.0, 8.0, 0.1);
    private final IntegerSetting speed = new IntegerSetting("Speed", 10, 1, 40, 1);
    private final TimerUtil timer = new TimerUtil();
    private EntityPlayer target;
    private FixedRotations rotations;

    public AimAssist() {
        super("AimAssist", Category.GHOST);
        this.addSettings(this.filter, this.range, this.speed);
    }

    @Override
    public void onEnable() {
        this.rotations = new FixedRotations(AimAssist.mc.thePlayer.rotationYaw, AimAssist.mc.thePlayer.rotationPitch);
    }

    @Override
    public void onClientStarted() {
        this.antibotModule = Shiver.instance.getModuleManager().getModule(Antibot.class);
        this.teamsModule = Shiver.instance.getModuleManager().getModule(Teams.class);
    }

    @Listener
    public void onRender(RenderEvent event) {
        this.target = this.findTarget();
        if (this.target != null && Mouse.isButtonDown((int)0) && AimAssist.mc.currentScreen == null) {
            float[] rots = RotationsUtil.getRotationsToEntity((EntityLivingBase)this.target, false);
            float yaw = rots[0];
            float currentYaw = MathHelper.wrapAngleTo180_float((float)AimAssist.mc.thePlayer.rotationYaw);
            float diff = Math.abs(currentYaw - yaw);
            if (diff >= 4.0f && diff <= 356.0f) {
                float aa = diff <= (float)this.speed.getValue() ? diff * 0.9f : (float)((double)this.speed.getValue() - Math.random() * 0.5);
                float finalSpeed = aa * (float)Math.max(this.timer.getTimeElapsed(), 1L) * 0.01f;
                AimAssist.mc.thePlayer.rotationYaw = diff <= 180.0f ? (currentYaw > yaw ? (AimAssist.mc.thePlayer.rotationYaw -= finalSpeed) : (AimAssist.mc.thePlayer.rotationYaw += finalSpeed)) : (currentYaw > yaw ? (AimAssist.mc.thePlayer.rotationYaw += finalSpeed) : (AimAssist.mc.thePlayer.rotationYaw -= finalSpeed));
            }
        }
        this.rotations.updateRotations(AimAssist.mc.thePlayer.rotationYaw, AimAssist.mc.thePlayer.rotationPitch);
        AimAssist.mc.thePlayer.rotationYaw = this.rotations.getYaw();
        AimAssist.mc.thePlayer.rotationPitch = this.rotations.getPitch();
        this.timer.reset();
    }

    public EntityPlayer findTarget() {
        ArrayList<EntityPlayer> entities = new ArrayList<EntityPlayer>();
        for (Entity entity2 : AimAssist.mc.theWorld.loadedEntityList) {
            EntityPlayer player;
            if (!(entity2 instanceof EntityPlayer) || entity2 == AimAssist.mc.thePlayer || !this.canAttackEntity(player = (EntityPlayer)entity2)) continue;
            entities.add(player);
        }
        if (entities != null && entities.size() > 0) {
            switch (this.filter.getMode()) {
                case "Range": {
                    entities.sort(Comparator.comparingDouble(entity -> entity.getDistanceToEntity((Entity)AimAssist.mc.thePlayer)));
                    break;
                }
                case "Health": {
                    entities.sort(Comparator.comparingDouble(entity -> entity.getHealth()));
                }
            }
            return (EntityPlayer)entities.get(0);
        }
        return null;
    }

    private boolean canAttackEntity(EntityPlayer player) {
        if (!player.isDead && (double)AimAssist.mc.thePlayer.getDistanceToEntity((Entity)player) < this.range.getValue() && !player.isInvisible() && !player.isInvisibleToPlayer((EntityPlayer)AimAssist.mc.thePlayer)) {
            if (!this.teamsModule.canAttack(player)) {
                return false;
            }
            return this.antibotModule.canAttack((EntityLivingBase)player, this);
        }
        return false;
    }
}

