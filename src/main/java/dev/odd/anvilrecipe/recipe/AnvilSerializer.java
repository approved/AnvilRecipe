package dev.odd.anvilrecipe.recipe;

import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class AnvilSerializer {
    public static RecipeSerializer<AnvilRecipe> SMITHING;

    static {
        SMITHING = Registry.register(Registry.RECIPE_SERIALIZER, new Identifier("anvilrecipe", "anvil_working"), new AnvilRecipe.Serializer<AnvilRecipe>(SmithingRecipe::new));
    }
}