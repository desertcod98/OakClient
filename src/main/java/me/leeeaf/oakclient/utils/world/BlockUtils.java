package me.leeeaf.oakclient.utils.world;

import me.leeeaf.oakclient.utils.player.RotationUtils;
import net.minecraft.block.*;
import net.minecraft.network.packet.c2s.play.HandSwingC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import static me.leeeaf.oakclient.OakClientClient.mc;

public class BlockUtils {
    public static boolean place(BlockPos blockPos, int invSlot, Hand hand, boolean rotate, boolean checkEntities, boolean swingHand, boolean airPlace){
        if (invSlot < 0 || invSlot > 8) return false;
        if (!canPlace(blockPos, checkEntities)) return false;
        Vec3d hitPos = new Vec3d(blockPos.getX() + 0.5, blockPos.getY() + 0.5, blockPos.getZ() + 0.5); //why +0.5 works
        BlockPos neighbour;
        Direction side = getPlaceSide(blockPos);
        if (side == null) {
            if(!airPlace) return false;
            side = Direction.UP;
            neighbour = blockPos;
        }else {
            neighbour = blockPos.offset(side.getOpposite());
            hitPos.add(side.getOffsetX() * 0.5, side.getOffsetY() * 0.5, side.getOffsetZ() * 0.5); //why *0.5 works
        }
        if(rotate){
            RotationUtils.rotate(blockPos.getX(),blockPos.getY(),blockPos.getZ());
        }
        place(new BlockHitResult(hitPos, side, neighbour, false), hand, swingHand);
        return true;
}

    public static boolean canPlace(BlockPos blockPos, boolean checkEntities) {
        if(blockPos==null) return false;
        if(!World.isValid(blockPos)) return false;
        if (!mc.world.getBlockState(blockPos).getMaterial().isReplaceable()) return false; //todo check Material class
        return !checkEntities || mc.world.canPlace(mc.world.getBlockState(blockPos), blockPos, ShapeContext.absent()); //todo ShapeContext???
    }

    private static void place(BlockHitResult blockHitResult, Hand hand, boolean swing){
        boolean wasSneaking = mc.player.input.sneaking; //why cant sneak? todo
        mc.player.input.sneaking = false;
        ActionResult result = mc.interactionManager.interactBlock(mc.player, hand, blockHitResult);
        if (result.shouldSwingHand()) {
            if (swing) mc.player.swingHand(hand);
            else mc.getNetworkHandler().sendPacket(new HandSwingC2SPacket(hand));
        }

        mc.player.input.sneaking = wasSneaking;
    }

    public static Direction getPlaceSide(BlockPos blockPos){
        for (Direction side : Direction.values()) { //for every possible direction
            BlockPos neighbor = blockPos.offset(side);  //get blockPos neighbor in that direction
            Direction side2 = side.getOpposite();

            BlockState state = mc.world.getBlockState(neighbor);

            // Check if neighbour isn't empty
            if (state.isAir() || isClickable(state.getBlock())) continue;

            // Check if neighbour is a fluid
            if (!state.getFluidState().isEmpty()) continue;

            return side2;
        }

        return null;
    }

    public static boolean isClickable(Block block) {
        return block instanceof CraftingTableBlock
                || block instanceof AnvilBlock
                || block instanceof AbstractButtonBlock
                || block instanceof AbstractPressurePlateBlock
                || block instanceof BlockWithEntity
                || block instanceof BedBlock
                || block instanceof FenceGateBlock
                || block instanceof DoorBlock
                || block instanceof NoteBlock
                || block instanceof TrapdoorBlock;
    }
}
