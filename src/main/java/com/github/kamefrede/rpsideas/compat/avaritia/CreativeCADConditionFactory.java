package com.github.kamefrede.rpsideas.compat.avaritia;

import com.github.kamefrede.rpsideas.proxy.ConfigHandler;
import com.google.gson.JsonObject;
import net.minecraft.util.JsonUtils;
import net.minecraftforge.common.crafting.IConditionFactory;
import net.minecraftforge.common.crafting.JsonContext;

import java.util.function.BooleanSupplier;

public class CreativeCADConditionFactory implements IConditionFactory {
    @Override
    public BooleanSupplier parse(JsonContext context, JsonObject json) {
        boolean value = JsonUtils.getBoolean(json, "value", true);
        return () -> ConfigHandler.enableCreativeCAD == value;

    }
}
