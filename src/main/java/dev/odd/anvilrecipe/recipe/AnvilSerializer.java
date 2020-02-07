package dev.odd.anvilrecipe.recipe;

import net.minecraft.recipe.RecipeSerializer;

public class AnvilSerializer {
    public static RecipeSerializer<AnvilRecipe> SMITHING;

    static {
        SMITHING = RecipeSerializer.register("smithing", new AnvilRecipe.Serializer<AnvilRecipe>(SmithingRecipe::new));
    }
}