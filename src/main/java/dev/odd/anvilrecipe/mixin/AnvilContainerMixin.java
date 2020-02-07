package dev.odd.anvilrecipe.mixin;

import java.util.List;
import com.google.common.collect.Lists;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import dev.odd.anvilrecipe.recipe.AnvilRecipe;
import dev.odd.anvilrecipe.recipe.AnvilRecipeType;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.container.AnvilContainer;
import net.minecraft.container.BlockContext;
import net.minecraft.container.Container;
import net.minecraft.container.ContainerType;
import net.minecraft.container.Property;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

@Mixin(AnvilContainer.class)
public class AnvilContainerMixin extends Container {

    protected AnvilContainerMixin(ContainerType<?> type, int syncId) {
        super(type, syncId);
    }

    @Shadow
    private String newItemName;
    @Shadow
    private Inventory inventory;
    @Shadow
    private Inventory result;
    @Shadow
    private int repairItemUsage;
    @Shadow
    private Property levelCost;

    @Final
    private World world;

    private List<AnvilRecipe> availableRecipes = Lists.newArrayList();
    private boolean doesMatchRecipe = false;
    private int inputRepairCost = 1;

    @Inject(at = @At("RETURN"), method = "<init>(ILnet/minecraft/entity/player/PlayerInventory;Lnet/minecraft/container/BlockContext;)V")
    public void AnvilContainerCtor(int syncId, PlayerInventory inventory, final BlockContext blockContext, CallbackInfo ci) {
        this.world = inventory.player.world;
    }

    @Environment(EnvType.CLIENT)
    public List<AnvilRecipe> getAvailableRecipes() {
        return this.availableRecipes;
    }

    @Environment(EnvType.CLIENT)
    public int getAvailableRecipeCount() {
        return this.availableRecipes.size();
    }

    @Environment(EnvType.CLIENT)
    public boolean canCraft() {
        return !this.inventory.getInvStack(0).isEmpty() && !this.availableRecipes.isEmpty();
    }

    @Inject(at = @At(value = "INVOKE", target = "net/minecraft/container/AnvilContainer.updateResult()V", shift = Shift.BEFORE), method = "onContentChanged")
    public void whenContentChanged(Inventory inventory, CallbackInfo ci) {
        this.doesMatchRecipe = false;
        this.availableRecipes.clear();
        this.result.setInvStack(0, ItemStack.EMPTY);
        if(!this.inventory.isInvEmpty()) {
            if (!this.inventory.getInvStack(0).isEmpty()) {
                this.availableRecipes = this.world.getRecipeManager().getAllMatches(AnvilRecipeType.SMITHING, inventory, this.world);
            }
        }
    }

    @Inject(at = @At("HEAD"), method = "updateResult", cancellable = true)
    public void onUpdateResult(CallbackInfo ci) {
        if (!this.availableRecipes.isEmpty()) {
            ItemStack itemStack = this.inventory.getInvStack(0);
            if (!itemStack.isEmpty()) {
                for (AnvilRecipe recipe : availableRecipes) {
                    if(recipe.matches(this.inventory, this.world))
                    {
                        this.doesMatchRecipe = true;
                        this.levelCost.set(recipe.experienceLevels);
                        this.inputRepairCost = recipe.getInputCount();
                        this.repairItemUsage = recipe.getModifierCount();
                        ItemStack resultItem = recipe.craft(this.inventory);
                        this.result.setInvStack(0, resultItem);
                        this.sendContentUpdates();
                        ci.cancel();
                    }
                }
            }
        }
    }

    @Redirect(at = @At(value = "INVOKE", target = "Lnet/minecraft/inventory/Inventory;setInvStack(ILnet/minecraft/item/ItemStack;)V"), method = "Lnet/minecraft/container/AnvilContainer$2;onTakeItem(Lnet/minecraft/entity/player/PlayerEntity;Lnet/minecraft/item/ItemStack;)Lnet/minecraft/item/ItemStack;")
    public void onSetInvStack(Inventory inventory, int i, ItemStack itemStack) {
        if(this.doesMatchRecipe) {
            this.inventory.getInvStack(0).decrement(this.inputRepairCost);
        }
        else {
            this.inventory.setInvStack(0, ItemStack.EMPTY);
        }
    }

    @Shadow
    public boolean canUse(PlayerEntity player){ return true; }
}