package dev.odd.anvilrecipe.recipe;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.Blocks;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

public class SmithingRecipe extends AnvilRecipe {
   public SmithingRecipe(Identifier id, ItemStack inputItem, ItemStack modifierItem, ItemStack output, int levelcost, boolean shapeless) {
      super(AnvilRecipeType.SMITHING, AnvilSerializer.SMITHING, id, inputItem, modifierItem, output, levelcost, shapeless);
   }

   public boolean matches(Inventory inv, World world) {
      ItemStack firstSlot = inv.getInvStack(0);
      ItemStack secondSlot = inv.getInvStack(1);

      if(this.shapeless) {
         return   (this.inputItem.isItemEqual(firstSlot) && this.modifierItem.isItemEqual(secondSlot))
               || (this.inputItem.isItemEqual(secondSlot) && this.modifierItem.isItemEqual(firstSlot));
      }

      return   this.inputItem.isItemEqual(firstSlot)
            && this.inputItem.getCount() <= firstSlot.getCount()
            && this.modifierItem.isItemEqual(secondSlot) 
            && this.modifierItem.getCount() <= secondSlot.getCount();
   }

   @Environment(EnvType.CLIENT)
   public ItemStack getRecipeKindIcon() {
      return new ItemStack(Blocks.ANVIL);
   }
}
