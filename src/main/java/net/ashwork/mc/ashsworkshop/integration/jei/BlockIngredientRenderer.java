package net.ashwork.mc.ashsworkshop.integration.jei;

import mezz.jei.api.ingredients.IIngredientRenderer;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.block.Block;

import java.util.List;

public class BlockIngredientRenderer implements IIngredientRenderer<Block> {

    @Override
    public void render(GuiGraphics guiGraphics, Block ingredient) {
        // TODO: Replace with actual block rendering
        guiGraphics.renderItem(new ItemStack(ingredient), 0, 0);
    }

    @Override
    public List<Component> getTooltip(Block ingredient, TooltipFlag tooltipFlag) {
        return List.of(ingredient.getName());
    }
}
