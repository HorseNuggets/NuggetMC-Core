package net.nuggetmc.core.modifiers.gheads.util;

import java.lang.reflect.Field;
import java.util.UUID;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.SkullType;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;

import net.minecraft.server.v1_8_R3.EntityPlayer;
import net.nuggetmc.core.data.Configs;

public class TextureProfileField {
	
	private FileConfiguration config;
	
	public TextureProfileField() {
		this.config = Configs.gheads.getConfig();
	}
	
	public ItemStack headPlayer(Player player) {
		
		ItemStack head = new ItemStack(Material.SKULL_ITEM, 1, (short) SkullType.PLAYER.ordinal());
		SkullMeta headMeta = (SkullMeta) head.getItemMeta();
		EntityPlayer playerNMS = ((CraftPlayer) player).getHandle();
		headMeta.setOwner(player.getName());
		
		GameProfile profile = playerNMS.getProfile();
		
		try {
			Field profileField = headMeta.getClass().getDeclaredField("profile");
			profileField.setAccessible(true);
			profileField.set(headMeta, profile);
			
		}
		catch (IllegalArgumentException | NoSuchFieldException | SecurityException | IllegalAccessException e) {
			e.printStackTrace();
		}
		
		String username = config.getString("heads.head-name").replaceAll("<username>", player.getName());
		
		headMeta.setDisplayName(ChatColor.WHITE + username);
		head.setItemMeta(headMeta);
		
		return head;
	}
	
	public ItemStack headGold() {
		
		ItemStack head = new ItemStack(Material.SKULL_ITEM, 1, (short)3);
		SkullMeta headMeta = (SkullMeta) head.getItemMeta();
		UUID uuid = UUID.fromString("38400000-8cf0-11bd-b23e-10b96e4ef00d");
		GameProfile profile = new GameProfile(uuid, null);
		profile.getProperties().put("textures", new Property("textures", config.getString("gheads.ghead-texture")));
		
		try {
			Field profileField = headMeta.getClass().getDeclaredField("profile");
			profileField.setAccessible(true);
			profileField.set(headMeta, profile);
			
		}
		catch (IllegalArgumentException | NoSuchFieldException | SecurityException | IllegalAccessException e) {
			e.printStackTrace();
		}
		headMeta.setDisplayName(config.getString("gheads.ghead-name"));
		head.setItemMeta(headMeta);
		return head;
	}
}