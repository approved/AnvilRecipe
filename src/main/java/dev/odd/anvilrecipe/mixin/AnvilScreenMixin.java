package dev.odd.anvilrecipe.mixin;

import org.spongepowered.asm.mixin.Mixin;

import net.minecraft.client.gui.screen.ingame.AnvilScreen;
import net.minecraft.client.gui.screen.ingame.ContainerScreen;
import net.minecraft.container.AnvilContainer;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;

@Mixin(AnvilScreen.class)
public abstract class AnvilScreenMixin extends ContainerScreen<AnvilContainer> {

    public AnvilScreenMixin(AnvilContainer container, PlayerInventory playerInventory, Text name) {
        super(container, playerInventory, name);
    }

    //private int scrollOffset = 0;
    //private boolean canCraft = true;

    /*
    public void drawMouseoverTooltip(int mouseX, int mouseY) {
        super.drawMouseoverTooltip(mouseX, mouseY);
        if (this.canCraft) {
           int i = this.x + 52;
           int j = this.y + 14;
           int k = this.scrollOffset + 12;
           List<AnvilRecipe> list = ((AnvilContainerMixin)this.container).getAvailableRecipes();
  
           for(int l = this.scrollOffset; l < k && l < ((AnvilContainerMixin)this.container).getAvailableRecipeCount(); ++l) {
              int m = l - this.scrollOffset;
              int n = i + m % 4 * 16;
              int o = j + m / 4 * 18 + 2;
              if (mouseX >= n && mouseX < n + 16 && mouseY >= o && mouseY < o + 18) {
                 this.renderTooltip(((AnvilRecipe)list.get(l)).getOutput(), mouseX, mouseY);
              }
           }
        }
     }
     */
}