package dev.odd.anvilrecipe.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Constant.Condition;

import dev.odd.anvilrecipe.recipe.AnvilScreenHandlerAccessor;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.AnvilScreenHandler;

@Mixin(targets = "net/minecraft/screen/AnvilScreenHandler$2")
public class AnvilScreenHandler$2Mixin {

    @Shadow
    private AnvilScreenHandler field_7781;

    @Redirect(at = @At(value = "INVOKE", target = "Lnet/minecraft/inventory/Inventory;setInvStack(ILnet/minecraft/item/ItemStack;)V", ordinal = 0), method = "Lnet/minecraft/screen/AnvilScreenHandler$2;onTakeOutput(Lnet/minecraft/entity/player/PlayerEntity;Lnet/minecraft/item/ItemStack;)Lnet/minecraft/item/ItemStack;")
    public void onSetInvStack(Inventory inventory, int i, ItemStack itemStack) {
        if (((AnvilScreenHandlerAccessor) field_7781).getDoesMatchRecipe()) {
            ((AnvilScreenHandlerAccessor) field_7781).getInventory().getStack(0)
                    .decrement(((AnvilScreenHandlerAccessor) field_7781).getInputRepairCost());
        } else {
            ((AnvilScreenHandlerAccessor) field_7781).getInventory().setStack(0, ItemStack.EMPTY);
        }
    }
}