package com.github.kamefrede.rpsideas.spells.trick.potion;

import net.minecraft.init.MobEffects;
import net.minecraft.potion.Potion;
import vazkii.psi.api.spell.Spell;
import vazkii.psi.common.spell.trick.potion.PieceTrickPotionBase;

public class PieceTrickNausea extends PieceTrickPotionBase {


    public PieceTrickNausea(Spell spell) {
        super(spell);
    }

    @Override
    public Potion getPotion() {
        return MobEffects.NAUSEA;
    }


    @Override
    public boolean hasPower() {
        return false;
    }
}
