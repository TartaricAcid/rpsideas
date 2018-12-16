package com.kamefrede.rpsideas.spells.base;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import vazkii.psi.api.internal.Vector3;
import vazkii.psi.api.spell.*;
import vazkii.psi.api.spell.param.ParamVector;
import vazkii.psi.api.spell.piece.PieceOperator;

public abstract class OperatorEnergy extends PieceOperator {

    private SpellParam position;
    private SpellParam axis;

    public OperatorEnergy(Spell spell) {
        super(spell);
    }

    @Override
    public void initParams() {
        addParam(position = new ParamVector(SpellParam.GENERIC_NAME_POSITION, SpellParam.RED, false, false));
        addParam(axis = new ParamVector(SpellParam.GENERIC_NAME_TARGET, SpellParam.BLUE, true, false));
    }

    @Override
    public void addToMetadata(SpellMetadata meta) throws SpellCompilationException {
        meta.addStat(EnumSpellStat.COMPLEXITY, 2);
    }

    @Override
    public Object execute(SpellContext context) throws SpellRuntimeException {
        if (context.caster.world.isRemote) return null;

        Vector3 posVec = this.getParamValue(context, position);

        Vector3 axisVec = this.getParamValue(context, axis);
        if (axisVec != null && !axisVec.isAxial())
            throw new SpellRuntimeException(SpellRuntimeExceptions.NON_AXIAL_VECTOR);

        EnumFacing facing = axisVec == null ? null : EnumFacing.getFacingFromVector(
                (float) axisVec.x,
                (float) axisVec.y,
                (float) axisVec.z);

        if (posVec == null || posVec.isZero())
            throw new SpellRuntimeException(SpellRuntimeException.NULL_VECTOR);

        BlockPos pos = new BlockPos(posVec.x, posVec.y, posVec.z);
        World world = context.caster.world;

        TileEntity tile = world.getTileEntity(pos);
        if (tile != null && tile.hasCapability(CapabilityEnergy.ENERGY, facing)) {
            IEnergyStorage storage = tile.getCapability(CapabilityEnergy.ENERGY, facing);

            if (storage != null)
                return result(world, pos, tile, storage);
        }
        return 0.0;
    }

    protected abstract double result(World world, BlockPos pos, TileEntity tile, IEnergyStorage storage);

    @Override
    public Class<?> getEvaluationType() {
        return Double.class;
    }
}
