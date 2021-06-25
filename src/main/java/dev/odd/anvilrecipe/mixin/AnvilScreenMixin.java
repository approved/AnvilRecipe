package dev.odd.anvilrecipe.mixin;

import org.spongepowered.asm.mixin.Mixin;

import net.minecraft.client.gui.screen.ingame.AnvilScreen;
import net.minecraft.client.gui.screen.ingame.ForgingScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.AnvilScreenHandler;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

@Mixin(AnvilScreen.class)
public abstract class AnvilScreenMixin extends ForgingScreen<AnvilScreenHandler> {

    public AnvilScreenMixin(AnvilScreenHandler container, PlayerInventory playerInventory, Text name,
            Identifier texture) {
        super(container, playerInventory, name, texture);
    }
}