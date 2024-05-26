package Shiver.module.impl.combat;

import Shiver.module.Category;
import Shiver.module.Module;
import net.minecraft.entity.player.EntityPlayer;

public class Teams
extends Module {
    public Teams() {
        super("Teams", Category.COMBAT);
    }

    public boolean canAttack(EntityPlayer entity) {
        if (!this.isEnabled()) {
            return true;
        }
        if (Teams.mc.thePlayer.getTeam() != null && entity.getTeam() != null) {
            Character targetColor = Character.valueOf(entity.getDisplayName().getFormattedText().charAt(1));
            Character playerColor = Character.valueOf(Teams.mc.thePlayer.getDisplayName().getFormattedText().charAt(1));
            return !playerColor.equals(targetColor);
        }
        return false;
    }
}

