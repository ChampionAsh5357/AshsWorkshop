package net.ashwork.mc.ashsworkshop.integration.jei;

import com.mojang.math.Axis;
import mezz.jei.api.ingredients.IIngredientRenderer;
import net.minecraft.ChatFormatting;
import net.minecraft.advancements.critereon.BlockPredicate;
import net.minecraft.advancements.critereon.StatePropertiesPredicate;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class BlockPredicateIngredientRenderer implements IIngredientRenderer<BlockPredicate> {

    @Override
    public void render(GuiGraphics guiGraphics, BlockPredicate ingredient) {
        // Rotate rendered item based on attached side
        Direction attachedSide = facing(ingredient);
        guiGraphics.pose().pushPose();
        if (getObject(ingredient) == Blocks.LIGHTNING_ROD) {
            guiGraphics.pose().rotateAround(Axis.ZP.rotationDegrees(directionToRotation(attachedSide)), 8f, 8f, 8f);
        }
        guiGraphics.renderItem(new ItemStack(getObject(ingredient)), 0, 0);
        guiGraphics.pose().popPose();
    }

    @Override
    public List<Component> getTooltip(BlockPredicate ingredient, TooltipFlag tooltipFlag) {
        List<Component> tooltip = new ArrayList<>();
        // Get block tooltip
        tooltip.add(getObject(ingredient).getName());

        // Get state properties tooltip
        ingredient.properties().ifPresent(predicate -> {
            for (var property : predicate.properties()) {
                String value = switch (property.valueMatcher()) {
                    case StatePropertiesPredicate.ExactMatcher exact -> exact.value();
                    case StatePropertiesPredicate.RangedMatcher ranged ->
                        ranged.minValue().map(str -> "[" + str).orElse("(inf") + ", " + ranged.maxValue().map(str -> str + "]").orElse("inf)");
                    default -> property.valueMatcher().toString();
                };

                tooltip.add(Component.literal(property.name() + ": " + value).setStyle(Style.EMPTY.withColor(ChatFormatting.GRAY)));
            }
        });

        return tooltip;
    }

    private static int directionToRotation(Direction direction) {
        return switch (direction) {
            case DOWN -> 180;
            case UP -> 0;
            case NORTH -> 45;
            case SOUTH -> -135;
            case WEST -> -90;
            case EAST -> 90;
        };
    }

    private static Block getObject(BlockPredicate predicate) {
        return predicate.blocks().orElseThrow().get(0).value();
    }

    private static Direction facing(BlockPredicate predicate) {
        return predicate.properties()
                .flatMap(props -> props.properties().stream()
                        .filter(matcher -> matcher.name().equals("facing")).findFirst()
                ).map(matcher -> getPropertyData(predicate, matcher, Direction.UP))
                .orElse(Direction.UP);
    }

    private static Direction getPropertyData(BlockPredicate predicate, StatePropertiesPredicate.PropertyMatcher matcher, Direction _default) {
        var stateDefn = getObject(predicate).getStateDefinition();
        if (matcher.valueMatcher() instanceof StatePropertiesPredicate.ExactMatcher exact) {
            Optional<?> result = stateDefn.getProperty(matcher.name()).getValue(exact.value());
            return result.filter(val -> val instanceof Direction).map(Direction.class::cast).orElse(_default);
        }

        return _default;
    }
}
