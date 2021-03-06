package net.nuggetmc.core.gui;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class Voting implements CommandExecutor {
	
	Inventory inv = Bukkit.createInventory(null, 36, "Voting");
	
	public Voting() {
		ItemStack v1  = new ItemStack(Material.PAPER);
		ItemMeta meta1  = v1.getItemMeta();
		ItemStack v2  = new ItemStack(Material.PAPER);
		ItemMeta meta2  = v2.getItemMeta();
		ItemStack v3  = new ItemStack(Material.PAPER);
		ItemMeta meta3  = v3.getItemMeta();
		ItemStack v4  = new ItemStack(Material.PAPER);
		ItemMeta meta4  = v4.getItemMeta();
		ItemStack v5  = new ItemStack(Material.PAPER);
		ItemMeta meta5  = v5.getItemMeta();
		ItemStack v6  = new ItemStack(Material.PAPER);
		ItemMeta meta6  = v6.getItemMeta();
		ItemStack v7  = new ItemStack(Material.PAPER);
		ItemMeta meta7  = v7.getItemMeta();
		
		ItemStack close = new ItemStack(Material.BARRIER);
		ItemMeta metaclose = close.getItemMeta();
		
		meta1.setDisplayName(ChatColor.YELLOW + "Vote on " + ChatColor.AQUA + "MinecraftServers§3.org");
		ArrayList<String> lore1 = new ArrayList<String>();
		lore1.add(ChatColor.GRAY + "Reward: " + ChatColor.GREEN + "1 Uncommon Nugget");
		lore1.add("");
		lore1.add(ChatColor.GRAY + "Click to vote!");
		meta1.setLore(lore1);
		
		meta2.setDisplayName(ChatColor.YELLOW + "Vote on " + ChatColor.AQUA + "MineStatus§3.net");
		ArrayList<String> lore2 = new ArrayList<String>();
		lore2.add(ChatColor.GRAY + "Reward: " + ChatColor.GREEN + "1 Uncommon Nugget");
		lore2.add("");
		lore2.add(ChatColor.GRAY + "Click to vote!");
		meta2.setLore(lore2);
		
		meta3.setDisplayName(ChatColor.YELLOW + "Vote on " + ChatColor.AQUA + "VoteMC§3.com");
		ArrayList<String> lore3 = new ArrayList<String>();
		lore3.add(ChatColor.GRAY + "Reward: " + ChatColor.GREEN + "1 Uncommon Nugget");
		lore3.add("");
		lore3.add(ChatColor.GRAY + "Click to vote!");
		meta3.setLore(lore3);
		
		metaclose.setDisplayName(ChatColor.RED + "Close");
		
		v1.setItemMeta(meta1);
		v2.setItemMeta(meta2);
		v3.setItemMeta(meta3);
		v4.setItemMeta(meta4);
		v5.setItemMeta(meta5);
		v6.setItemMeta(meta6);
		v7.setItemMeta(meta7);
		close.setItemMeta(metaclose);
		
		inv.setItem(12, v1);
		inv.setItem(13, v2);
		inv.setItem(14, v3);
		
		inv.setItem(31, close);
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (sender instanceof Player) {
			Player player = (Player) sender;
			vote(player);
		}
		return true;
	}
	
	private void vote(Player player) {
		player.updateInventory();
		player.openInventory(inv);
		return;
	}
	
	public static void onClick(InventoryClickEvent event) {
		String name = event.getCurrentItem().getItemMeta().getDisplayName();
		Player player = (Player) event.getWhoClicked();
		boolean success = false;
		
		if (name.equals(ChatColor.YELLOW + "Vote on " + ChatColor.AQUA + "MinecraftServers§3.org")) {
			success = true;
			player.closeInventory();
			player.sendMessage(ChatColor.GRAY + "--------------------------------------");
			player.sendMessage("");
			player.sendMessage(ChatColor.YELLOW + "               Vote for " + ChatColor.GOLD + ChatColor.BOLD + "NuggetMC" + ChatColor.RESET + ChatColor.YELLOW + "!");
			player.sendMessage(ChatColor.DARK_AQUA + "      minecraftservers.org/vote/589423");
			player.sendMessage("");
			player.sendMessage(ChatColor.GRAY + "--------------------------------------");
		}
		else if (name.equals(ChatColor.YELLOW + "Vote on " + ChatColor.AQUA + "MineStatus§3.net")) {
			success = true;
			player.closeInventory();
			player.sendMessage(ChatColor.GRAY + "--------------------------------------");
			player.sendMessage("");
			player.sendMessage(ChatColor.YELLOW + "               Vote for " + ChatColor.GOLD + ChatColor.BOLD + "NuggetMC" + ChatColor.RESET + ChatColor.YELLOW + "!");
			player.sendMessage(ChatColor.DARK_AQUA + "   minestatus.net/server/vote/nuggetmc.net");
			player.sendMessage("");
			player.sendMessage(ChatColor.GRAY + "--------------------------------------");
		}
		else if (name.equals(ChatColor.YELLOW + "Vote on " + ChatColor.AQUA + "VoteMC§3.com")) {
			success = true;
			player.closeInventory();
			player.sendMessage(ChatColor.GRAY + "--------------------------------------");
			player.sendMessage("");
			player.sendMessage(ChatColor.YELLOW + "               Vote for " + ChatColor.GOLD + ChatColor.BOLD + "NuggetMC" + ChatColor.RESET + ChatColor.YELLOW + "!");
			player.sendMessage(ChatColor.DARK_AQUA + "         votemc.com/nuggetmc.479/vote");
			player.sendMessage("");
			player.sendMessage(ChatColor.GRAY + "--------------------------------------");
		}
		
		if (success) {
			player.playSound(player.getLocation(), Sound.CHICKEN_EGG_POP, 1, 1);
		}
		return;
	}
}
