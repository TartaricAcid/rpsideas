package com.github.kamefrede.rpsideas.spells.selector;

import net.minecraft.entity.Entity;
import net.minecraft.util.math.AxisAlignedBB;
import vazkii.psi.api.internal.Vector3;
import vazkii.psi.api.spell.*;
import vazkii.psi.api.spell.param.ParamNumber;
import vazkii.psi.api.spell.param.ParamVector;
import vazkii.psi.api.spell.piece.PieceSelector;
import vazkii.psi.common.entity.EntitySpellCharge;

import java.util.List;
import java.util.Objects;

public class PieceSelectorNumberCharges extends PieceSelector {

    SpellParam position;
    SpellParam radius;

    public PieceSelectorNumberCharges(Spell spell) {
        super(spell);
    }

    @Override
    public void initParams() {
        addParam(position = new ParamVector(SpellParam.GENERIC_NAME_POSITION, SpellParam.BLUE, false, false));
        addParam(radius = new ParamNumber(SpellParam.GENERIC_NAME_RADIUS, SpellParam.GREEN, false, true));
    }

    @Override
    public void addToMetadata(SpellMetadata meta) throws SpellCompilationException {
        super.addToMetadata(meta);

        Double radiusVal = this.<Double>getParamEvaluation(radius);
        if(radiusVal == null || radiusVal <= 0)
            throw new SpellCompilationException(SpellCompilationException.NON_POSITIVE_VALUE, x, y);
    }

    @Override
    public Object execute(SpellContext context) throws SpellRuntimeException {
        Vector3 positionVal = this.<Vector3>getParamValue(context, position);
        Double radiusVal = this.<Double>getParamValue(context, radius);

        if(!context.isInRadius(positionVal))
            throw new SpellRuntimeException(SpellRuntimeException.OUTSIDE_RADIUS);

        AxisAlignedBB axis = new AxisAlignedBB(positionVal.x - radiusVal, positionVal.y - radiusVal, positionVal.z - radiusVal, positionVal.x + radiusVal, positionVal.y + radiusVal, positionVal.z + radiusVal);


        List<Entity> list = context.caster.getEntityWorld().getEntitiesWithinAABB(Entity.class, axis, (Entity e) -> {
            return e != null  && e != context.caster && e != context.focalPoint && context.isInRadius(e) && e instanceof EntitySpellCharge && (Objects.requireNonNull(((EntitySpellCharge) e).getThrower()).getName().equals(context.caster.getName()));
        });


        if(list != null){
            return list.size() * 1D;
        }
        return 0.D;

    }




    @Override
    public Class<?> getEvaluationType() {
        return Double.class;
    }
}
