package net.ashwork.mc.ashsworkshop.recipe.block;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeInput;
import net.minecraft.world.level.block.state.BlockState;

public interface BlockInput extends RecipeInput {

    BlockState getBlock(int index);

    @Override
    default ItemStack getItem(int index) {
        throw new UnsupportedOperationException("ItemStack methods are not supported on Block recipes.");
    }

    @Override
    default boolean isEmpty() {
        for (int i = 0; i < this.size(); i++) {
            if (!this.getBlock(i).isEmpty()) {
                return false;
            }
        }

        return true;
    }

    record Single(BlockState state) implements BlockInput {

        @Override
        public BlockState getBlock(int index) {
            if (index != 0) {
                throw new IllegalArgumentException("No item for index " + index);
            } else {
                return this.state;
            }
        }

        @Override
        public int size() {
            return 1;
        }
    }
}
