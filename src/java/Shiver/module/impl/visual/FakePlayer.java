package Shiver.module.impl.visual;

import Shiver.module.Category;
import Shiver.module.Module;
import com.mojang.authlib.GameProfile;
import java.util.UUID;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;

public class FakePlayer
extends Module {
    public FakePlayer() {
        super("FakePlayer", Category.VISUAL);
    }

    @Override
    public void onEnable() {
        EntityOtherPlayerMP fakePlayer = new EntityOtherPlayerMP((World)FakePlayer.mc.theWorld, new GameProfile(UUID.fromString("4f7700aa-93d0-4c6a-b58a-d99b1c7287fd"), mc.getSession().getUsername()));
        fakePlayer.copyLocationAndAnglesFrom((Entity)FakePlayer.mc.thePlayer);
        FakePlayer.mc.theWorld.addEntityToWorld(69420, (Entity)fakePlayer);
    }

    @Override
    public void onDisable() {
        FakePlayer.mc.theWorld.removeEntityFromWorld(69420);
    }
}

