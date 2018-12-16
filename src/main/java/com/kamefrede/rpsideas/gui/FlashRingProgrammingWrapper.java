package com.kamefrede.rpsideas.gui;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import vazkii.psi.common.block.tile.TileProgrammer;
import vazkii.psi.common.item.ItemSpellDrive;

import javax.annotation.Nonnull;

public class FlashRingProgrammingWrapper extends TileProgrammer {

    private EntityPlayer player;

    public FlashRingProgrammingWrapper(EntityPlayer player, ItemStack stack) {
        super();

        this.player = player;

        spell = ItemSpellDrive.getSpell(stack);
        enabled = true;
    }

    @Override
    public void onSpellChanged() {
        // NO-OP
    }

    @Override
    public boolean canPlayerInteract(EntityPlayer player) {
        return true;
    }

    @Nonnull
    @Override
    @SuppressWarnings({"ConstantConditions", "NullableProblems"})
    public World getWorld() {
        return new World(null, null, player.world.provider, null, true) {
            @Override
            protected IChunkProvider createChunkProvider() {
                return null;
            }

            @Override
            protected boolean isChunkLoaded(int i, int i1, boolean b) {
                return false;
            }

            @Nonnull
            @Override
            public TileEntity getTileEntity(@Nonnull BlockPos pos) {
                return FlashRingProgrammingWrapper.this;
            }
        };
    }
}
