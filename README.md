<h1 align="center">Anvil Recipe</h1>
<h4 align="center">A mod that adds the ability to support crafting through the use of an anvil!</h4>

<h1 align="center">Recipe Construction</h1>

Example:
```json
{
    "type": "anvilrecipe:anvil_working",
    "shapeless": false,
    "firstingredient": {
        "item": "minecraft:diamond_sword"
    },
    "secondingredient": {
        "item": "minecraft:netherite_ingot",
        "count": 2
    },
    "result": {
        "item": "minecraft:netherite_sword"
    },
    "levelcost": 30
}
```
<table>
    <tbody>
        <tr>
            <th align="left">Value</th>
            <th align="left">Type</th>
            <th align="left">Description</th>
            <th align="center">Required</th>
            <th align="center">Default</th>
            <th align="center">Example</th>
        </tr>
        <tr>
            <th align="left">type</th>
            <th align="left"></h5>String</h5></th>
            <th align="left">The type of crafting recipe.<h6>For the usage of this mod, it will always be "anvilrecipe:anvil_working"</h6></th>
            <th align="center">`True`</th>
            <th align="center"></th>
            <th align="center">"anvilrecipe:anvil_working"</th>
        </tr>
        <tr>
            <th align="left">shapeless</th>
            <th align="left"><h5>Boolean</h5></th>
            <th align="left">Sets whether the crafting recipe requires the first ingredient to be in the first slot or not.</th>
            <th align="center">`False`</th>
            <th align="center">true</th>
            <th align="center">false</th>
        </tr>
        <tr>
            <th align="left">firstingredient</th>
            <th align="left"><h5>Ingredient</h5></th>
            <th align="left">The item that will be put in to the first slot of the anvil</th>
            <th align="center">`True`</th>
            <th align="center"></th>
            <th align="left">
                <h6>
<pre lang=json>
"firstingredient": {
    "item": "minecraft:diamond_sword"
}
</pre>
                </h6>
            </th>
        </tr>
        <tr>
            <th align="left">secondingredient</th>
            <th align="left"><h5>Ingredient</h5></th>
            <th align="left">The item that will be put in to the second slot of the anvil</th>
            <th align="center">`True`</th>
            <th align="center"></th>
            <th align="left">
                <h6>
<pre lang=json>
"secondingredient": {
    "item": "minecraft:netherite_ingot",
    "count": 2
}
</pre>
                </h6>
            </th>
        </tr>
        <tr>
            <th align="left">count</th>
            <th align="left"><h5>Integer</h5></th>
            <th align="left">Child of an ingredient object, determines how many of this item is required for the crafting recipe</th>
            <th align="center">`False`</th>
            <th align="center">1</th>
            <th align="center">2</th>
        </tr>
        <tr>
            <th align="left">levelcost</th>
            <th align="left"><h5>Integer</h5></th>
            <th align="left">The required amount of levels to craft this recipe</th>
            <th align="center">`False`</th>
            <th align="center">0</th>
            <th align="center">30</th>
        </tr>
        <tr>
            <th align="left">result</th>
            <th align="left"><h5>ItemStack</h5></th>
            <th align="left">The result of the crafting recipe</th>
            <th align="center">`True`</th>
            <th align="center"></th>
            <th align="center">"minecraft:netherite_sword"</th>
        </tr>
    </tbody>
</table>