package dev.odd.anvilrecipe.recipe;

import net.fabricmc.api.ModInitializer;

public class AnvilRecipeLoader implements ModInitializer{

    @Override
    public void onInitialize() {
        new AnvilSerializer();
        new AnvilRecipeType();
    }
}