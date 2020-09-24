package net.nuggetmc.core.modifiers.gheads.setup;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ShapedRecipe;

import net.nuggetmc.core.modifiers.gheads.util.TextureProfileField;

public class GHeadCraft {
	
	@SuppressWarnings("deprecation")
	public static void setup() {
		TextureProfileField TextureProfileField = new TextureProfileField();
		ShapedRecipe recipe = new ShapedRecipe(TextureProfileField.headGold());
		recipe.shape(
        		"###",
        		"#@#",
        		"###");
		recipe.setIngredient('@', Material.SKULL_ITEM, 3);
		recipe.setIngredient('#', Material.GOLD_INGOT);
        Bukkit.getServer().addRecipe(recipe);
		return;
	}

}
