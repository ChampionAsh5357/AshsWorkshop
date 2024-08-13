package net.ashwork.mc.ashsworkshop.integration.jei;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import net.ashwork.mc.ashsworkshop.AshsWorkshop;
import net.minecraft.resources.ResourceLocation;

@JeiPlugin
public class WorkshopJeiPlugin implements IModPlugin {

    private static final ResourceLocation PLUGIN_ID = AshsWorkshop.fromMod("integration/jei");

    @Override
    public ResourceLocation getPluginUid() {
        return PLUGIN_ID;
    }
}
