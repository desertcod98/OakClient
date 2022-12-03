package me.leeeaf.oakclient.utils.player;

import net.minecraft.entity.Entity;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;

import static me.leeeaf.oakclient.OakClientClient.mc;

public class RotationUtils {
    public static double getYaw(double x, double z) {
        return mc.player.getYaw() + MathHelper.wrapDegrees((float) Math.toDegrees(Math.atan2(z + 0.5 - mc.player.getZ(), x + 0.5 - mc.player.getX())) - 90f - mc.player.getYaw());
    }

    public static double getPitch(double x, double y, double z) {
        double diffX = x + 0.5 - mc.player.getX();
        double diffY = y + 0.5 - (mc.player.getY() + mc.player.getEyeHeight(mc.player.getPose()));
        double diffZ = z + 0.5 - mc.player.getZ();

        double diffXZ = Math.sqrt(diffX * diffX + diffZ * diffZ);

        return mc.player.getPitch() + MathHelper.wrapDegrees((float) -Math.toDegrees(Math.atan2(diffY, diffXZ)) - mc.player.getPitch());
    }

    public static void rotate(double x, double y, double z) {
        rotate(x, y, z, false);
    }

    public static void rotate(double x, double y, double z, boolean clientSide) {
        rotate(getYaw(x, z), getPitch(x, y, z), clientSide);
    }

    public static void rotate(double yaw, double pitch, boolean clientSide) {
        if (clientSide) {
            mc.player.setYaw((float) yaw);
            mc.player.setPitch((float) pitch);
        } else {
            mc.getNetworkHandler().sendPacket(new PlayerMoveC2SPacket.LookAndOnGround((float) yaw, (float) pitch, mc.player.isOnGround()));
        }
    }
}
