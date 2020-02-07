package dev.odd.anvilrecipe.recipe;

import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.util.DefaultedList;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import net.minecraft.util.PacketByteBuf;
import net.minecraft.util.registry.Registry;

public abstract class AnvilRecipe implements Recipe<Inventory> {

    protected final ItemStack inputItem, modifierItem, output;
    private final RecipeType<?> type;
    private final RecipeSerializer<?> serializer;
    protected final Identifier id;
    public final int levelcost;
    public final boolean shapeless;

    public AnvilRecipe(RecipeType<?> type, RecipeSerializer<?> serializer, Identifier id,  ItemStack inputItem, ItemStack modifierItem, ItemStack output, int levelcost, boolean shapeless) {
        System.out.println("Registered Anvil Crafting Recipe Sucessfully: " + id.toString());
        this.type = type;
        this.serializer = serializer;
        this.id = id;
        this.inputItem = inputItem;
        this.modifierItem = modifierItem;
        this.output = output;
        this.levelcost = levelcost;
        this.shapeless = shapeless;
    }

    public RecipeType<?> getType() {
        return this.type;
    }

    public RecipeSerializer<?> getSerializer() {
        return this.serializer;
    }

    public Identifier getId() {
        return this.id;
    }

    public ItemStack getOutput() {
        return this.output;
    }

    public int getModifierCount() {
        return this.modifierItem.getCount();
    }

    public int getInputCount() {
        return this.inputItem.getCount();
    }

    public DefaultedList<Ingredient> getPreviewInputs() {
        DefaultedList<Ingredient> list = DefaultedList.of();
        list.add(Ingredient.ofStacks(this.inputItem));
        list.add(Ingredient.ofStacks(this.modifierItem));
        return list;
    }

    @Environment(EnvType.CLIENT)
    public boolean fits(int width, int height) {
        return true;
    }

    public ItemStack craft(Inventory inv) {
        return this.output.copy();
    }

    public static class Serializer<T extends AnvilRecipe> implements RecipeSerializer<T> {
        final AnvilRecipe.Serializer.RecipeFactory<T> recipeFactory;

        protected Serializer(AnvilRecipe.Serializer.RecipeFactory<T> recipeFactory) {
            this.recipeFactory = recipeFactory;
        }

        public T read(Identifier identifier, JsonObject jsonObject) {
            boolean shapeless = true;
            if(jsonObject.has("shapeless")) {
                shapeless = JsonHelper.getBoolean(jsonObject, "shapeless");
            }

            JsonObject inputObject = JsonHelper.getObject(jsonObject, "firstingredient");
            ItemStack inputIngredient = null;
            if (inputObject.has("item")) {
                String inputItemString = JsonHelper.getString(inputObject, "item");
                Item item = Registry.ITEM.get(new Identifier(inputItemString));

                int inputCount = 1;
                if (inputObject.has("count")) {
                    inputCount = JsonHelper.getInt(inputObject, "count");
                    if (inputCount > item.getMaxCount()) {
                        throw new JsonParseException("First ingredient count can not be higher than max item count");
                    } else if (inputCount < 1) {
                        throw new JsonParseException("First ingredient count must be higher than 0");
                    }
                }

                inputIngredient = new ItemStack((ItemConvertible) item, inputCount);
            }

            if (inputIngredient == null) {
                throw new JsonParseException("No first ingredient in smithing recipe");
            }

            JsonObject modifierObject = JsonHelper.getObject(jsonObject, "secondingredient");
            ItemStack modifierIngredient = null;
            if (modifierObject.has("item")) {
                String modifierItemString = JsonHelper.getString(modifierObject, "item");
                Item item = Registry.ITEM.get(new Identifier(modifierItemString));

                int modifierCount = 1;
                if (modifierObject.has("count")) {
                    modifierCount = JsonHelper.getInt(modifierObject, "count");
                    if (modifierCount > item.getMaxCount()) {
                        throw new JsonParseException("Second ingredient count can not be higher than max item count");
                    } else if (modifierCount < 1) {
                        throw new JsonParseException("Second ingredient count must be higher than 0");
                    }
                }

                modifierIngredient = new ItemStack((ItemConvertible) item, modifierCount);
            }

            if (modifierIngredient == null) {
                throw new JsonParseException("No second ingredient in smithing recipe");
            }

            JsonObject resultObject = JsonHelper.getObject(jsonObject, "result");
            if (resultObject.has("item")) {
                String resultItemString = JsonHelper.getString(resultObject, "item");
                Item resultItem = Registry.ITEM.get(new Identifier(resultItemString));

                int resultCount = 1;
                if(resultObject.has("count")) {
                    resultCount = JsonHelper.getInt(resultObject, "count");
                    if(resultCount > resultItem.getMaxCount()) {
                        throw new JsonParseException("Result item count can not be higher than max item count");
                    } else if (resultCount < 1) {
                        throw new JsonParseException("Result item count must be higher than 0");
                    }
                }

                int levelcost = 1;
                if(jsonObject.has("levelcost")) {
                    levelcost = JsonHelper.getInt(jsonObject, "levelcost");
                }

                return this.recipeFactory.create(identifier, inputIngredient, modifierIngredient, new ItemStack(resultItem, resultCount), levelcost, shapeless);
            } else {
                throw new JsonParseException("No result for smithing recipe");
            }
        }

        public T read(Identifier identifier, PacketByteBuf packetByteBuf) {
            int i = packetByteBuf.readVarInt();
            DefaultedList<Ingredient> ingredients = DefaultedList.ofSize(i, Ingredient.EMPTY);

            for (int j = 0; j < ingredients.size(); ++j) {
                ingredients.set(j, Ingredient.fromPacket(packetByteBuf));
            }

            ItemStack itemStack = packetByteBuf.readItemStack();
            return this.recipeFactory.create(identifier, ingredients.get(0).getMatchingStacksClient()[0], ingredients.get(1).getMatchingStacksClient()[0], itemStack, 1, true);
        }

        public void write(PacketByteBuf packetByteBuf, T smithingRecipe) {
            packetByteBuf.writeItemStack(smithingRecipe.output);
        }

        interface RecipeFactory<T extends AnvilRecipe> {
            T create(Identifier identifier, ItemStack inputItem, ItemStack modifierItem, ItemStack result, int levelcost, boolean shaped);
        }
    }
}