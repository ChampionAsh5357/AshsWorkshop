package net.ashwork.mc.ashsworkshop.data.client;

import net.ashwork.mc.ashsworkshop.AshsWorkshop;
import net.ashwork.mc.ashsworkshop.init.ItemRegistrar;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemDisplayContext;
import net.neoforged.neoforge.client.model.generators.ItemModelBuilder;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import net.neoforged.neoforge.client.model.generators.ModelProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

public class WorkshopItemModelProvider extends ItemModelProvider {

    public WorkshopItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, AshsWorkshop.ID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        var analyzer = this.basicItem(ItemRegistrar.ANALYZER.get())
                .transforms()
                .transform(ItemDisplayContext.THIRD_PERSON_RIGHT_HAND)
                .rotation(0, 0, 45).translation(0, 4, 1).scale(0.85F, 0.85F, 0.85F).end()
                .transform(ItemDisplayContext.THIRD_PERSON_LEFT_HAND)
                .rotation(0, 0, -45).translation(0, 4, 1).scale(0.85F, 0.85F, 0.85F).end()
                .transform(ItemDisplayContext.FIRST_PERSON_RIGHT_HAND)
                .rotation(-60, 0, 45).translation(1.13F, 3.2F, 1.13F).scale(0.68F, 0.68F, 0.68F).end()
                .transform(ItemDisplayContext.FIRST_PERSON_LEFT_HAND)
                .rotation(-60, 0, -45).translation(1.13F, 3.2F, 1.13F).scale(0.68F, 0.68F, 0.68F).end()
                .transform(ItemDisplayContext.HEAD)
                .rotation(-90, 0, 45).translation(0, 7, 4).scale(1, 1, 1).end()
                .end();
        for (int i = 0; i < 4; i++) {
            var analyzerState = this.basicItemWithParent(ItemRegistrar.ANALYZER.getId().withSuffix("_analysis_" + i), analyzer);
            analyzer.override()
                    .model(analyzerState)
                    .predicate(ItemRegistrar.ANALYZING_PROPERTY, 1)
                    .predicate(ItemRegistrar.STATUS_PROPERTY, 0.32F * i)
                    .end();
        }
    }

    private ItemModelBuilder basicItemWithParent(ResourceLocation item, ModelFile parent) {
        return getBuilder(item.toString())
                .parent(parent)
                .texture("layer0", item.withPrefix(ModelProvider.ITEM_FOLDER + "/"));
    }
}
