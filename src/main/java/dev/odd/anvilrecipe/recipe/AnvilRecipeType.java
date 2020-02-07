package dev.odd.anvilrecipe.recipe;

import net.minecraft.recipe.RecipeType;

public class AnvilRecipeType {
    public static RecipeType<AnvilRecipe> SMITHING;

    static {
        SMITHING = RecipeType.register("anvil");
    }
}