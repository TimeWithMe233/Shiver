package Shiver.util.player;

import Shiver.util.IMinecraft;
import Shiver.util.network.PacketUtils;
import net.minecraft.item.Item;
import net.minecraft.item.ItemAnvilBlock;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;

public class PlayerUtil
implements IMinecraft {
    public static void ncpDamage() {
        for (int i = 0; i < 49; ++i) {
            PacketUtils.sendPacketNoEvent((Packet)new C03PacketPlayer.C04PacketPlayerPosition(PlayerUtil.mc.thePlayer.posX, PlayerUtil.mc.thePlayer.posY + 0.0625, PlayerUtil.mc.thePlayer.posZ, false));
            PacketUtils.sendPacketNoEvent((Packet)new C03PacketPlayer.C04PacketPlayerPosition(PlayerUtil.mc.thePlayer.posX, PlayerUtil.mc.thePlayer.posY, PlayerUtil.mc.thePlayer.posZ, false));
        }
        PacketUtils.sendPacketNoEvent((Packet)new C03PacketPlayer(true));
    }

    public static boolean isBlockBlacklisted(Item item) {
        return item instanceof ItemAnvilBlock || item.getUnlocalizedName().contains("sand") || item.getUnlocalizedName().contains("gravel") || item.getUnlocalizedName().contains("ladder") || item.getUnlocalizedName().contains("tnt") || item.getUnlocalizedName().contains("chest") || item.getUnlocalizedName().contains("web");
    }
}

