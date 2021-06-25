package dev.odd.anvilrecipe.recipe;

import net.minecraft.inventory.Inventory;

public interface AnvilScreenHandlerAccessor {
    public Inventory getInventory();

    public int getInputRepairCost();

    public boolean getDoesMatchRecipe();
}