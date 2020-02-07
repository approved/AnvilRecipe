package dev.odd.anvilrecipe.recipe;

import net.minecraft.inventory.Inventory;

public interface AnvilContainerAccessor {
    public Inventory getInventory();

    public int getInputRepairCost();

    public boolean getDoesMatchRecipe();
}