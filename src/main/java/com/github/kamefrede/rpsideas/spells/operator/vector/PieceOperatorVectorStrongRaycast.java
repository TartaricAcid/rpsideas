package com.github.kamefrede.rpsideas.spells.operator.vector;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import vazkii.psi.api.internal.Vector3;
import vazkii.psi.api.spell.*;
import vazkii.psi.api.spell.param.ParamNumber;
import vazkii.psi.api.spell.param.ParamVector;
import vazkii.psi.api.spell.piece.PieceOperator;

public class PieceOperatorVectorStrongRaycast extends PieceOperator {

    SpellParam origin;
    SpellParam ray;
    SpellParam maxParam;

    private static final double MAX_MAX = 32d;



    public PieceOperatorVectorStrongRaycast(Spell spell){
        super(spell);
    }

    @Override
    public void initParams() {
        addParam(origin = new ParamVector(SpellParam.GENERIC_NAME_POSITION, SpellParam.BLUE, false, false));
        addParam(ray = new ParamVector("psi.spellparam.ray", SpellParam.GREEN, false, false));
        addParam(maxParam = new ParamNumber(SpellParam.GENERIC_NAME_MAX, SpellParam.PURPLE, true, false));
    }

    @Override
    public void addToMetadata(SpellMetadata meta) throws SpellCompilationException {
        super.addToMetadata(meta);
    }

    @Override
    public Object execute(SpellContext context) throws SpellRuntimeException {
        Vector3 originVal = this.<Vector3>getParamValue(context, origin);
        Vector3 rayVal = this.<Vector3>getParamValue(context, ray);

        if(originVal == null || rayVal == null)
            throw new SpellRuntimeException(SpellRuntimeException.NULL_VECTOR);

        double maxLen = SpellContext.MAX_DISTANCE;
        Double numberVal = this.<Double>getParamValue(context, maxParam);
        if(numberVal != null)
            maxLen = numberVal.doubleValue();
        maxLen = Math.min(SpellContext.MAX_DISTANCE, maxLen);

        RayTraceResult pos = raycast(context.caster.getEntityWorld(), originVal, rayVal, maxLen);
        if(pos == null || pos.getBlockPos() == null)
            throw new SpellRuntimeException(SpellRuntimeException.NULL_VECTOR);

        return new Vector3(pos.getBlockPos().getX(), pos.getBlockPos().getY(), pos.getBlockPos().getZ());
    }

    public static RayTraceResult raycast(Entity e, double len) throws SpellRuntimeException {
        Vector3 vec = Vector3.fromEntity(e);
        if(e instanceof EntityPlayer)
            vec.add(0, e.getEyeHeight(), 0);

        Vec3d look = e.getLookVec();
        if(look == null)
            throw new SpellRuntimeException(SpellRuntimeException.NULL_VECTOR);

        return raycast(e.getEntityWorld(), vec, new Vector3(look), len);
    }

    public static RayTraceResult raycast(World world, Vector3 origin, Vector3 ray, double len) {
        Vector3 end = origin.copy().add(ray.copy().normalize().multiply(len));
        RayTraceResult pos = world.rayTraceBlocks(origin.toVec3D(), end.toVec3D(), false, true, false);
        return pos;
    }

    @Override
    public Class<?> getEvaluationType() {
        return Vector3.class;
    }
}
