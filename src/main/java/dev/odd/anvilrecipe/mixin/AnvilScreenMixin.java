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
}