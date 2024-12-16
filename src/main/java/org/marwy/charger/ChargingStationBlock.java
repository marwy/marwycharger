package org.marwy.charger;

import com.simibubi.create.content.kinetics.base.KineticBlock;
import com.simibubi.create.foundation.block.IBE;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;
import net.minecraft.block.ShapeContext;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.WorldView;

public class ChargingStationBlock extends KineticBlock implements IBE<ChargingStationBlockEntity> {

    // Определите форму блока на основе вашей модели
    private static final VoxelShape SHAPE = VoxelShapes.union(
        VoxelShapes.cuboid(0.0625, 0.0625, 0.0625, 0.9375, 0.5, 0.9375)
    );

    public ChargingStationBlock(Settings settings) {
        super(settings);
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return SHAPE;
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return SHAPE;
    }

    @Override
    public Direction.Axis getRotationAxis(BlockState state) {
        return Direction.Axis.Y; // Ось вращения - вертикальная
    }

    @Override
    public Class<ChargingStationBlockEntity> getBlockEntityClass() {
        return ChargingStationBlockEntity.class;
    }

    @Override
    public BlockEntityType<? extends ChargingStationBlockEntity> getBlockEntityType() {
        return ModBlocks.CHARGING_STATION_BLOCK_ENTITY.get();
    }

    public boolean hasShaftTowards(WorldView world, BlockPos pos, BlockState state, Direction face) {
        return face == Direction.DOWN;
    }

    @Override
    public boolean hideStressImpact() {
        return false;
    }

    @Override
    public float getParticleTargetRadius() {
        return 0.85f;
    }

    @Override
    public float getParticleInitialRadius() {
        return 0.75f;
    }
}
