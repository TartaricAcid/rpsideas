package com.github.kamefrede.rpsideas.items.components;

import com.github.kamefrede.rpsideas.items.base.ItemComponent;
import com.github.kamefrede.rpsideas.util.Reference;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import vazkii.arl.network.NetworkHandler;
import vazkii.psi.api.PsiAPI;
import vazkii.psi.api.cad.*;
import vazkii.psi.common.core.handler.PlayerDataHandler;
import vazkii.psi.common.network.message.MessageDataSync;

import javax.annotation.Nonnull;
import java.util.ConcurrentModificationException;
import java.util.List;

@Mod.EventBusSubscriber
public class ItemUnstableBattery extends ItemComponent {
    private static final int PSI_REGEN_BONUS = 10;

    @Override
    protected void registerStats() {
        addStat(EnumCADStat.OVERFLOW, 800);
    }

    @Override
    public EnumCADComponent getComponentType(ItemStack itemStack) {
        return EnumCADComponent.BATTERY;
    }

    @Override
    protected void addTooltipTags(List<String> tooltip) {
        addTooltipTag(true, tooltip, Reference.MODID + ".upsides.boost_regen", PSI_REGEN_BONUS);
        addTooltipTag(false, tooltip, Reference.MODID + ".downsides.on_damage");
    }

    @SubscribeEvent
    public static void onDamage(LivingHurtEvent e) {
        EntityLivingBase living = e.getEntityLiving();
        if(living.world.isRemote) return;
        if(!(living instanceof EntityPlayer)) return;
        if(e.getAmount() < 1) return;

        EntityPlayer player = (EntityPlayer) living;
        ItemStack cad = PsiAPI.getPlayerCAD(player);

        if(!cad.isEmpty()) {
            ICAD icad = (ICAD) cad.getItem();

            ItemStack battery = icad.getComponentInSlot(cad, EnumCADComponent.BATTERY);
            if(!battery.isEmpty() && battery.getItem() instanceof ItemUnstableBattery) {
                try{
                    PlayerDataHandler.PlayerData data = PlayerDataHandler.get(player);
                    data.availablePsi  = 0;
                    data.regenCooldown = 20;
                    data.save();
                    NetworkHandler.INSTANCE.sendTo(new MessageDataSync(data), (EntityPlayerMP)player);
                } catch (ConcurrentModificationException except) {
                    //same as the twinflow
                }

            }
        }
    }


}
