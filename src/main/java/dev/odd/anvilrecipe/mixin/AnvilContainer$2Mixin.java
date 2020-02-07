package dev.odd.anvilrecipe.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import dev.odd.anvilrecipe.recipe.AnvilContainerAccessor;
import net.minecraft.container.AnvilContainer;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;

@Mixin(targets = "net/minecraft/container/AnvilContainer$2")
public class AnvilContainer$2Mixin {

    @Shadow
    private AnvilContainer field_7781;

    @Redirect(at = @At(value = "INVOKE", target = "Lnet/minecraft/inventory/Inventory;setInvStack(ILnet/minecraft/item/ItemStack;)V", ordinal = 0), method = "Lnet/minecraft/container/AnvilContainer$2;onTakeItem(Lnet/minecraft/entity/player/PlayerEntity;Lnet/minecraft/item/ItemStack;)Lnet/minecraft/item/ItemStack;")
    public void onSetInvStack(Inventory inventory, int i, ItemStack itemStack) {
        if(((AnvilContainerAccessor)field_7781).getDoesMatchRecipe()) {
            ((AnvilContainerAccessor)field_7781).getInventory().getInvStack(0).decrement(((AnvilContainerAccessor)field_7781).getInputRepairCost());
        }
        else {
            ((AnvilContainerAccessor)field_7781).getInventory().setInvStack(0, ItemStack.EMPTY);
        }
    }
}