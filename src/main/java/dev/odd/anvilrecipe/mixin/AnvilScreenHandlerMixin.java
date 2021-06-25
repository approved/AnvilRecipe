package dev.odd.anvilrecipe.mixin;

import java.util.List;
import com.google.common.collect.Lists;

import net.minecraft.inventory.CraftingResultInventory;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import dev.odd.anvilrecipe.recipe.AnvilScreenHandlerAccessor;
import dev.odd.anvilrecipe.recipe.AnvilRecipe;
import dev.odd.anvilrecipe.recipe.AnvilRecipeType;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.AnvilScreenHandler;
import net.minecraft.screen.ForgingScreenHandler;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.screen.Property;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.world.World;

@Mixin(AnvilScreenHandler.class)
public class AnvilScreenHandlerMixin extends ForgingScreenHandler implements AnvilScreenHandlerAccessor {

    protected AnvilScreenHandlerMixin(ScreenHandlerType<?> type, int syncId, PlayerInventory inventory,
            ScreenHandlerContext context) {
        super(type, syncId, inventory, context);
    }

    @Shadow
    private String newItemName;

    @Shadow
    private int repairItemUsage;

    @Shadow
    @Final
    private Property levelCost;

    private World world;

    private List<AnvilRecipe> availableRecipes = Lists.newArrayList();
    private boolean doesMatchRecipe = false;
    private int inputRepairCost = 1;

    @Inject(at = @At("RETURN"), method = "<init>(ILnet/minecraft/entity/player/PlayerInventory;Lnet/minecraft/screen/ScreenHandlerContext;)V")
    public void AnvilContainerCtor(int syncId, PlayerInventory inventory, final ScreenHandlerContext context,
            CallbackInfo ci) {
        this.world = inventory.player.world;
    }

    @ModifyConstant(method = "canTakeOutput(Lnet/minecraft/entity/player/PlayerEntity;Z)Z", constant = @Constant(expandZeroConditions = Constant.Condition.GREATER_THAN_ZERO, ordinal = 0), require = 1)
    private int allowZeroLevelCost(int value) {
        return -1;
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
        return !this.input.getStack(0).isEmpty() && !this.availableRecipes.isEmpty();
    }

    @Inject(at = @At("HEAD"), method = "updateResult")
    public void whenContentChanged(CallbackInfo ci) {
        this.doesMatchRecipe = false;
        this.availableRecipes.clear();
        this.output.setStack(0, ItemStack.EMPTY);
        if (!this.input.isEmpty()) {
            if (!this.input.getStack(0).isEmpty()) {
                this.availableRecipes = this.world.getRecipeManager().getAllMatches(AnvilRecipeType.SMITHING, input, this.world);
            }
        }
    }

    @Inject(at = @At("HEAD"), method = "updateResult", cancellable = true)
    public void onUpdateResult(CallbackInfo ci) {
        if (!this.availableRecipes.isEmpty()) {
            ItemStack itemStack = this.input.getStack(0);
            if (!itemStack.isEmpty()) {
                for (AnvilRecipe recipe : availableRecipes) {
                    if (recipe.matches(this.input, this.world)) {
                        this.doesMatchRecipe = true;
                        this.levelCost.set(recipe.levelcost);
                        this.inputRepairCost = recipe.getInputCount();
                        this.repairItemUsage = recipe.getModifierCount();
                        ItemStack resultItem = recipe.craft(this.input);
                        this.output.setStack(0, resultItem);
                        this.sendContentUpdates();
                        ci.cancel();
                    }
                }
            }
        }
    }

    @Override
    public Inventory getInventory() {
        return this.input;
    }

    @Override
    public int getInputRepairCost() {
        return this.inputRepairCost;
    }

    @Override
    public boolean getDoesMatchRecipe() {
        return this.doesMatchRecipe;
    }

    @Shadow
    protected boolean canTakeOutput(PlayerEntity player, boolean present) {
        return false;
    }

    @Shadow
    protected boolean canUse(BlockState state) {
        return false;
    }

    @Shadow
    protected void onTakeOutput(PlayerEntity player, ItemStack stack) {
    }

    @Shadow
    public void updateResult() {
    }
}