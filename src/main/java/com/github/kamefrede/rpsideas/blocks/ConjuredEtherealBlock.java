package com.github.kamefrede.rpsideas.blocks;

import com.github.kamefrede.rpsideas.tiles.TileEthereal;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.IBlockState;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import vazkii.arl.block.BlockModContainer;

import java.util.List;
import java.util.Random;

public class ConjuredEtherealBlock extends BlockModContainer implements IPsiamBlock {

    public ConjuredEtherealBlock() {
        super("conjuredblock", Material.GLASS);
        setDefaultState(makeDefaultState());
        setLightOpacity(0);

    }


    public static final PropertyBool SOLID = PropertyBool.create("solid");
    public static final PropertyBool BLOCK_UP = PropertyBool.create("block_up");
    public static final PropertyBool BLOCK_DOWN = PropertyBool.create("block_down");
    public static final PropertyBool BLOCK_NORTH = PropertyBool.create("block_north");
    public static final PropertyBool BLOCK_SOUTH = PropertyBool.create("block_south");
    public static final PropertyBool BLOCK_WEST = PropertyBool.create("block_west");
    public static final PropertyBool BLOCK_EAST = PropertyBool.create("block_east");

    public IBlockState makeDefaultState() {
        return getStateFromMeta(0);
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, getAllProperties());
    }


    public IProperty[] getIgnoredProperties() {
        return getAllProperties();
    }

    public IProperty[] getAllProperties() {
        return new IProperty[] { SOLID, BLOCK_UP, BLOCK_DOWN, BLOCK_NORTH, BLOCK_SOUTH, BLOCK_WEST, BLOCK_EAST };
    }


    @Override
    public BlockRenderLayer getRenderLayer() {
        return BlockRenderLayer.TRANSLUCENT;
    }


    @Override
    public boolean isFullCube(IBlockState state) {
        return false;
    }

    @Override
    public boolean isFullBlock(IBlockState state) {
        return false;
    }

    @Override
    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }

    @Override
    public int quantityDropped(Random random) {
        return 0;
    }

    @Override
    public boolean canSilkHarvest(World world, BlockPos pos, IBlockState state, EntityPlayer player) {
        return false;
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        IBlockState state = getDefaultState();
        return state.withProperty(SOLID, (meta & 1) > 0);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return (state.getValue(SOLID) ? 1 : 0);
    }

    @Override
    public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
        IBlockState origState = state;
        state = state.withProperty(BLOCK_UP, worldIn.getBlockState(pos.up()).equals(origState));
        state = state.withProperty(BLOCK_DOWN, worldIn.getBlockState(pos.down()).equals(origState));
        state = state.withProperty(BLOCK_NORTH, worldIn.getBlockState(pos.north()).equals(origState));
        state = state.withProperty(BLOCK_SOUTH, worldIn.getBlockState(pos.south()).equals(origState));
        state = state.withProperty(BLOCK_WEST, worldIn.getBlockState(pos.west()).equals(origState));
        state = state.withProperty(BLOCK_EAST, worldIn.getBlockState(pos.east()).equals(origState));

        return state;
    }

    @Override
    public int getLightValue(IBlockState state, IBlockAccess world, BlockPos pos) {
        return 0;
    }

    @Override
    public void addCollisionBoxToList(IBlockState state, World worldIn, BlockPos pos, AxisAlignedBB aabb, List<AxisAlignedBB> list, Entity entity, boolean blarg) {
            addCollisionBoxToList(pos, null, list, null);
    }


    @Override
    public TileEntity createTileEntity(World world, IBlockState state) {
        return new TileEthereal();
    }

    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return null;
    }


}