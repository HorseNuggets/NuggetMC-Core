package net.nuggetmc.core.commands.def;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scoreboard.Team;

import net.nuggetmc.core.Main;
import net.nuggetmc.core.data.Configs;
import net.nuggetmc.core.economy.KitCosts;
import net.nuggetmc.core.economy.KitGear;
import net.nuggetmc.core.gui.GUIKits;
import net.nuggetmc.core.setup.WorldManager;
import net.nuggetmc.core.util.Checks;
import net.nuggetmc.core.util.TimeConverter;

public class KitCommand implements CommandExecutor {

	private Main plugin;
	private FileConfiguration config;

	public KitCommand(Main plugin) {
		this.config = Configs.playerstats.getConfig();
		this.plugin = plugin;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

		if (args.length == 0) {
			Player player = (Player) sender;
			GUIKits.kit(player);
			return true;
		}

		Player player = Bukkit.getPlayer(sender.getName());
		String uuid = player.getUniqueId().toString();
		
		ItemStack swordItem = new ItemStack(Material.IRON_SWORD, 1);

		int helmetRandomizer = 0;
		int chestplateRandomizer = 0;
		int leggingsRandomizer = 0;
		int bootsRandomizer = 0;
		int bowRandomizer = 0;
		int swordRandomizer = 0;

		boolean check = false;
		boolean kit = false;

		int nuggets = config.getInt("players." + uuid + ".nuggets");
		int level = config.getInt("players." + uuid + ".level");

		if (args.length == 1) {
			
			if (plugin.kits.expiration.containsKey(player.getName())) {
				int seconds = (int) ((plugin.kits.expiration.get(player.getName()) - System.currentTimeMillis())
						/ 1000);
				if (seconds > 0) {
					player.sendMessage(ChatColor.WHITE + "You can't get a kit again for another" + ChatColor.YELLOW
							+ TimeConverter.intToStringElongated(
									(int) ((plugin.kits.expiration.get(player.getName()) - System.currentTimeMillis())
											/ 1000))
							+ ChatColor.WHITE + " or until you respawn.");
					return true;
				} else {
					plugin.kits.expiration.remove(player.getName());
				}
			}
			
			String kitName = args[0].substring(0, 1).toUpperCase() + args[0].substring(1);
			
			boolean loss = false;
			boolean notSufficientLevel = false;
			
			int cost = KitCosts.cost(args[0].toLowerCase());
			int requiredLevel = KitCosts.requiredLevel(args[0].toLowerCase());
			
			switch (args[0].toLowerCase()) {
			case "swordsman":
				helmetRandomizer = (int) (Math.random() * 3 + 3);
				chestplateRandomizer = (int) (Math.random() * 2 + 1);
				leggingsRandomizer = (int) (Math.random() * 1 + 3);
				bootsRandomizer = (int) (Math.random() * 2 + 1);
				bowRandomizer = (int) (Math.random() * 3 + 1);
				swordRandomizer = (int) (Math.random() * 3 + 4);
				
				kit = true;
				check = true;
				break;
				
			case "brute":
				helmetRandomizer = (int) (Math.random() * 2 + 1);
				chestplateRandomizer = (int) (Math.random() * 2 + 4);
				leggingsRandomizer = (int) (Math.random() * 2 + 3);
				bootsRandomizer = (int) (Math.random() * 3 + 3);
				bowRandomizer = (int) (Math.random() * 3 + 1);
				swordRandomizer = (int) (Math.random() * 3 + 2);

				kit = true;
				check = true;
				break;
				
			case "scout":
				helmetRandomizer = (int) (Math.random() * 2 + 1);
				chestplateRandomizer = (int) (Math.random() * 2 + 1);
				leggingsRandomizer = (int) (Math.random() * 2 + 4);
				bootsRandomizer = 0;
				
				helmetRandomizer = (int) (Math.random() * 2 + 1);
				chestplateRandomizer = (int) (Math.random() * 2 + 1);
				leggingsRandomizer = (int) (Math.random() * 2 + 4);
				bootsRandomizer = 0;

				ItemStack boots = new ItemStack(Material.DIAMOND_BOOTS, 1);
				boots.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
				ItemMeta bootsn = boots.getItemMeta();
				bootsn.setDisplayName(ChatColor.GREEN + "Scout Boots");
				ArrayList<String> bootsl = new ArrayList<String>();
				bootsl.add(ChatColor.GRAY + "Speed I");
				bootsl.add("");
				bootsl.add(ChatColor.DARK_GRAY + "Hitting players will grant");
				bootsl.add(ChatColor.DARK_GRAY + "Speed I for 2s.");
				bootsn.setLore(bootsl);

				bootsn.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);

				boots.setItemMeta(bootsn);

				if (player.getInventory().getBoots() == null) {
					player.getInventory().setBoots(boots);
				} else {
					player.getInventory().addItem(boots);
				}

				bowRandomizer = (int) (Math.random() * 2 + 2);
				swordRandomizer = 4;

				kit = true;
				check = true;
				break;
				
			case "apprentice":
				if (nuggets < cost) {
					loss = true;
					break;
				}
				else {
					helmetRandomizer = (int) (Math.random() * 2 + 3);
					chestplateRandomizer = (int) (Math.random() * 4 + 3);
					leggingsRandomizer = (int) (Math.random() * 2 + 1);
					bootsRandomizer = (int) (Math.random() * 2 + 4);
					bowRandomizer = (int) (Math.random() * 3 + 2);
					swordRandomizer = (int) (Math.random() * 3 + 4);

					kit = true;
					check = true;
					
					config.set("players." + uuid + ".nuggets", nuggets - cost);
				}
				break;
				
			case "horseman":
				if (level < requiredLevel) {
					notSufficientLevel = true;
					break;
				}
				if (nuggets < cost) {
					loss = true;
					break;
				}
				else {
					ItemStack sword = new ItemStack(Material.IRON_AXE, 1);
					sword.addEnchantment(Enchantment.DURABILITY, 1);
					ItemMeta swordn = sword.getItemMeta();
					swordn.setDisplayName(ChatColor.AQUA + "Horseman Axe");
					ArrayList<String> swordl = new ArrayList<String>();
					swordl.add(ChatColor.GRAY + "Jousting I");
					swordl.add("");
					swordl.add(ChatColor.DARK_GRAY + "This weapon's damage doubles if");
					swordl.add(ChatColor.DARK_GRAY + "you are on a horse.");
					swordl.add("");
					swordl.add(ChatColor.BLUE + "+5 " + ChatColor.DARK_BLUE + "(+10)" + ChatColor.BLUE + " Attack Damage");
					swordn.setLore(swordl);
					swordn.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
					sword.setItemMeta(swordn);
					player.getInventory().addItem(sword);

					helmetRandomizer = (int) (Math.random() * 2 + 1);
					chestplateRandomizer = 5;
					leggingsRandomizer = 5;
					bootsRandomizer = (int) (Math.random() * 2 + 1);
					bowRandomizer = (int) (Math.random() * 3 + 2);

					kit = true;
					check = true;

					ItemStack horse1 = new ItemStack(Material.MONSTER_EGG, 1, (short) 100);
					ItemMeta t1 = horse1.getItemMeta();
					t1.setDisplayName(ChatColor.RESET + "Spawn Horse " + ChatColor.GRAY + "(Horseman)");
					ArrayList<String> lore = new ArrayList<String>();
					lore.add(ChatColor.DARK_GRAY + "Health Boost I");
					t1.setLore(lore);
					horse1.setItemMeta(t1);

					BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
					scheduler.scheduleSyncDelayedTask(plugin, () -> {
						player.getInventory().addItem(horse1);
					}, 2);
					
					config.set("players." + uuid + ".nuggets", nuggets - cost);
				}
				break;
				
			case "essence":
				if (level < requiredLevel) {
					notSufficientLevel = true;
					break;
				}
				if (nuggets < cost) {
					loss = true;
					break;
				}
				else {
					helmetRandomizer = 4;
					chestplateRandomizer = 4;
					leggingsRandomizer = 4;
					bootsRandomizer = 4;
					bowRandomizer = (int) (Math.random() * 3 + 2);
					swordRandomizer = 4;

					kit = false;
					check = true;
					
					if (WorldManager.isInSpawn(player.getLocation())) {
						Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(),
								"effect " + player.getName() + " haste 120");
					}
					else {
						player.sendMessage("You didn't recieve the effects since you aren't at spawn!");
					}

					BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
					scheduler.scheduleSyncDelayedTask(plugin, () -> {
						Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(),
								"give " + player.getName() + " fishing_rod");
						Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(),
								"give " + player.getName() + " golden_apple 12");
						Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(),
								"give " + player.getName() + " arrow 48");
						Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(),
								"give " + player.getName() + " diamond_pickaxe 1 0 {ench:[{id:32,lvl:1}]}");
						Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(),
								"give " + player.getName() + " diamond_axe 1 0 {ench:[{id:32,lvl:1}]}");
						Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(),
								"give " + player.getName() + " diamond_shovel 1 0 {ench:[{id:32,lvl:1}]}");
						Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(),
								"give " + player.getName() + " cobblestone 64");
						Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(),
								"give " + player.getName() + " planks 64");
						Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(),
								"give " + player.getName() + " water_bucket 2");
						Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(),
								"give " + player.getName() + " lava_bucket 2");
						Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(),
								"give " + player.getName() + " cooked_beef 12");
						Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(),
								"give " + player.getName() + " enchanting_table");
						Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(),
								"give " + player.getName() + " experience_bottle 32");
						Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(),
								"give " + player.getName() + " book 42");
					}, 2);

					scheduler.scheduleSyncDelayedTask(plugin, () -> {
						Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(),
								"give " + player.getName() + " compass 1 0 {display:{Name:\"§fPlayer Tracker\"}}");
					}, 3);
					
					config.set("players." + uuid + ".nuggets", nuggets - cost);
				}
				break;
				
			case "jouster":
				if (level < requiredLevel) {
					notSufficientLevel = true;
					break;
				}
				if (nuggets < cost) {
					loss = true;
					break;
				}
				else {
					ItemStack sword = new ItemStack(Material.DIAMOND_AXE, 1);
					sword.addEnchantment(Enchantment.DURABILITY, 1);
					ItemMeta swordn = sword.getItemMeta();
					swordn.setDisplayName(ChatColor.AQUA + "Jouster Axe");
					ArrayList<String> swordl = new ArrayList<String>();
					swordl.add(ChatColor.GRAY + "Jousting I");
					swordl.add("");
					swordl.add(ChatColor.DARK_GRAY + "This weapon's damage doubles if");
					swordl.add(ChatColor.DARK_GRAY + "you are on a horse.");
					swordl.add("");
					swordl.add(ChatColor.BLUE + "+6 " + ChatColor.DARK_BLUE + "(+12)" + ChatColor.BLUE + " Attack Damage");
					swordn.setLore(swordl);
					swordn.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
					sword.setItemMeta(swordn);
					player.getInventory().addItem(sword);

					helmetRandomizer = (int) (Math.random() * 2 + 1);
					chestplateRandomizer = 5;
					leggingsRandomizer = 5;
					bootsRandomizer = (int) (Math.random() * 2 + 1);
					bowRandomizer = (int) (Math.random() * 3 + 2);

					kit = true;
					check = true;

					ItemStack horse2 = new ItemStack(Material.MONSTER_EGG, 1, (short) 100);
					ItemMeta t2 = horse2.getItemMeta();
					t2.setDisplayName(ChatColor.RESET + "Spawn Horse " + ChatColor.GRAY + "(Jouster)");
					ArrayList<String> lore2 = new ArrayList<String>();
					lore2.add(ChatColor.DARK_GRAY + "Health Boost VIII");
					lore2.add(ChatColor.DARK_GRAY + "Jump Boost I");
					t2.setLore(lore2);
					horse2.setItemMeta(t2);

					BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
					scheduler.scheduleSyncDelayedTask(plugin, () -> {
						player.getInventory().addItem(horse2);
					}, 2);
					
					config.set("players." + uuid + ".nuggets", nuggets - cost);
				}
				break;
				
			case "assassin":
				if (level < requiredLevel) {
					notSufficientLevel = true;
					break;
				}
				if (nuggets < cost) {
					loss = true;
					break;
				}
				else {
					helmetRandomizer = (int) (Math.random() * 4 + 3);
					chestplateRandomizer = (int) (Math.random() * 2 + 1);
					leggingsRandomizer = (int) (Math.random() * 2 + 1);
					bootsRandomizer = (int) (Math.random() * 2 + 1);
					bowRandomizer = 5;
					swordRandomizer = (int) (Math.random() * 3 + 6);

					kit = true;
					check = true;

					BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
					scheduler.scheduleSyncDelayedTask(plugin, () -> {
						if (WorldManager.isInSpawn(player.getLocation())) {
							Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(),
									"effect " + player.getName() + " strength 30");
						}
						else {
							player.sendMessage("You didn't recieve the effects since you aren't at spawn!");
						}
						ItemStack pearl = new ItemStack(Material.IRON_HOE, 1);
						pearl.addEnchantment(Enchantment.DURABILITY, 1);
						pearl.setDurability((short) 250);
						ItemMeta pearln = pearl.getItemMeta();
						pearln.setDisplayName(ChatColor.RED + "The Scythe");
						ArrayList<String> pearl1 = new ArrayList<String>();
						pearl1.add(ChatColor.GRAY + "Executioner I");
						pearl1.add("");
						pearl1.add(ChatColor.DARK_GRAY + "Instantly kills anyone");
						pearl1.add(ChatColor.DARK_GRAY + "under 6 HP.");
						pearln.setLore(pearl1);

						pearln.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ENCHANTS);
						pearl.setItemMeta(pearln);
						player.getInventory().addItem(pearl);
					}, 2);
					
					config.set("players." + uuid + ".nuggets", nuggets - cost);
				}
				break;
				
			case "enderman":
				if (level < requiredLevel) {
					notSufficientLevel = true;
					break;
				}
				if (nuggets < cost) {
					loss = true;
					break;
				}
				else {
					helmetRandomizer = (int) (Math.random() * 2 + 3);
					chestplateRandomizer = (int) (Math.random() * 2 + 1);
					leggingsRandomizer = 6;
					bootsRandomizer = 0;
					bowRandomizer = (int) (Math.random() * 3 + 2);
					swordRandomizer = (int) (Math.random() * 3 + 5);

					ItemStack enderboots = new ItemStack(Material.DIAMOND_BOOTS, 1);
					enderboots.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 3);
					enderboots.addEnchantment(Enchantment.PROTECTION_FALL, 2);
					ItemMeta endermeta = enderboots.getItemMeta();
					endermeta.setDisplayName(ChatColor.GREEN + "Enderman Boots");
					enderboots.setItemMeta(endermeta);

					if (player.getInventory().getBoots() == null) {
						player.getInventory().setBoots(enderboots);
					} else {
						player.getInventory().addItem(enderboots);
					}

					kit = true;
					check = true;

					BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
					scheduler.scheduleSyncDelayedTask(plugin, () -> {
						Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(),
								"give " + player.getName() + " ender_pearl 4");

						for (int j = 0; j < 8; j++) {
							ItemStack tpcr = new ItemStack(Material.NETHER_STAR);
							
							int random = (int) (Math.random() * 214748367 + 1);
							tpcr.addUnsafeEnchantment(Enchantment.ARROW_INFINITE, random);
							
							ItemMeta i = tpcr.getItemMeta();
							i.setDisplayName(ChatColor.DARK_PURPLE + "Teleport Crystal");

							ArrayList<String> lore1 = new ArrayList<String>();
							lore1.add(ChatColor.GRAY + "Teleport to a player");
							lore1.add(ChatColor.GRAY + "within " + ChatColor.YELLOW + "10 " + ChatColor.GRAY + "meters.");
							i.setLore(lore1);
							i.addItemFlags(ItemFlag.HIDE_ENCHANTS);
							tpcr.setItemMeta(i);
							player.getInventory().addItem(tpcr);
						}
					}, 2);
					
					config.set("players." + uuid + ".nuggets", nuggets - cost);
				}
				break;
				
			case "pyro":
				if (level < requiredLevel) {
					notSufficientLevel = true;
					break;
				}
				if (nuggets < cost) {
					loss = true;
					break;
				}
				else {
					helmetRandomizer = (int) (Math.random() * 2 + 5);
					chestplateRandomizer = (int) (Math.random() * 2 + 4);
					leggingsRandomizer = (int) (Math.random() * 3 + 3);
					bootsRandomizer = (int) (Math.random() * 2 + 1);
					swordRandomizer = (int) (Math.random() * 3 + 5);

					kit = false;
					check = true;
					
					if (WorldManager.isInSpawn(player.getLocation())) {
						Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(),
								"effect " + player.getName() + " fire_resistance 180");
					}
					else {
						player.sendMessage("You didn't recieve the effects since you aren't at spawn!");
					}

					BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
					scheduler.scheduleSyncDelayedTask(plugin, () -> {
						Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(),
								"give " + player.getName() + " golden_sword 1 0 {ench:[{id:20,lvl:1}]}");
						Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(),
								"give " + player.getName() + " bow 1 0 {ench:[{id:50,lvl:1}]}");
						Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(),
								"give " + player.getName() + " iron_pickaxe");
						Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(),
								"give " + player.getName() + " iron_axe");
						Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(),
								"give " + player.getName() + " iron_shovel");
						Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(),
								"give " + player.getName() + " arrow 48");
						Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(),
								"give " + player.getName() + " fishing_rod");
						Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(),
								"give " + player.getName() + " cobblestone 64");
						Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(),
								"give " + player.getName() + " planks 64");
						Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(),
								"give " + player.getName() + " golden_apple 12");
						Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(),
								"give " + player.getName() + " water_bucket 2");
						Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(),
								"give " + player.getName() + " lava_bucket 10");
						Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(),
								"give " + player.getName() + " cooked_beef 12");
					}, 2);
					scheduler.scheduleSyncDelayedTask(plugin, () -> {
						Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(),
								"give " + player.getName() + " compass 1 0 {display:{Name:\"§fPlayer Tracker\"}}");
					}, 3);
					
					config.set("players." + uuid + ".nuggets", nuggets - cost);
				}
				break;
				
			case "creeper":
				if (level < requiredLevel) {
					notSufficientLevel = true;
					break;
				}
				if (nuggets < cost) {
					loss = true;
					break;
				}
				else {
					helmetRandomizer = (int) (Math.random() * 2 + 4);
					chestplateRandomizer = (int) (Math.random() * 2 + 4);
					leggingsRandomizer = 6;
					bootsRandomizer = (int) (Math.random() * 2 + 4);
					bowRandomizer = (int) (Math.random() * 2 + 4);
					swordRandomizer = 7;

					kit = true;
					check = true;

					BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
					scheduler.scheduleSyncDelayedTask(plugin, () -> {
						int random = (int) (Math.random() * 214748367 + 1);
						Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(),
								"give " + player.getName() + " tnt 6");
						Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(),
								"give " + player.getName() + " redstone_block 1 0 {display:{Name:\"§cBoom Box\"},ench:[{id:51,lvl:" + random + "}],HideFlags:1}");
						random = (int) (Math.random() * 214748367 + 1);
						Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(),
								"give " + player.getName() + " redstone_block 1 0 {display:{Name:\"§cBoom Box\"},ench:[{id:51,lvl:" + random + "}],HideFlags:1}");
					}, 2);

					int totem = (int) (Math.random() * 6 + 1);
					if (totem == 4) {
						scheduler.scheduleSyncDelayedTask(plugin, () -> {
							Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "give " + player.getName()
									+ " nether_wart 1 0 {display:{Name:\"§cBlast Totem§r\",Lore:[\"§7Create an explosion\",\"§7on death.\"]},ench:[{id:34,lvl:1}],HideFlags:1}");
						}, 2);
					}
					
					config.set("players." + uuid + ".nuggets", nuggets - cost);
				}
				break;
				
			case "samurai":
				if (level < requiredLevel) {
					notSufficientLevel = true;
					break;
				}
				if (nuggets < cost) {
					loss = true;
					break;
				}
				else {
					helmetRandomizer = (int) (Math.random() * 3 + 3);
					chestplateRandomizer = (int) (Math.random() * 4 + 3);
					leggingsRandomizer = (int) (Math.random() * 3 + 3);
					bootsRandomizer = (int) (Math.random() * 3 + 3);
					bowRandomizer = (int) (Math.random() * 3 + 3);
					swordRandomizer = 0;

					ItemStack sword = new ItemStack(Material.IRON_SWORD, 1);
					sword.addEnchantment(Enchantment.DAMAGE_ALL, 3);
					ItemMeta swordn = sword.getItemMeta();
					swordn.setDisplayName(ChatColor.AQUA + "Samurai " + ChatColor.AQUA + "Sword" + ChatColor.WHITE);
					ArrayList<String> swordl = new ArrayList<String>();
					swordl.add(ChatColor.GRAY + "Block I");
					swordl.add("");
					swordl.add(ChatColor.DARK_GRAY + "Blocking with this sword");
					swordl.add(ChatColor.DARK_GRAY + "absorbs extra damage.");
					swordn.setLore(swordl);
					sword.setItemMeta(swordn);
					player.getInventory().addItem(sword);

					BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
					scheduler.scheduleSyncDelayedTask(plugin, () -> {
						ItemStack horse3 = new ItemStack(Material.MONSTER_EGG, 1, (short) 100);
						ItemMeta t3 = horse3.getItemMeta();
						t3.setDisplayName(ChatColor.RESET + "Spawn Horse " + ChatColor.GRAY + "(Areion)");
						ArrayList<String> lore3 = new ArrayList<String>();
						lore3.add(ChatColor.DARK_GRAY + "Health Boost IX");
						lore3.add(ChatColor.DARK_GRAY + "Jump Boost I");
						lore3.add(ChatColor.DARK_GRAY + "Regeneration I");
						t3.setLore(lore3);
						horse3.setItemMeta(t3);
						player.getInventory().addItem(horse3);
					}, 2);

					kit = true;
					check = true;
					
					if (WorldManager.isInSpawn(player.getLocation())) {
						Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(),
								"effect " + player.getName() + " speed 60 1");
					}
					else {
						player.sendMessage("You didn't recieve the effects since you aren't at spawn!");
					}
					
					config.set("players." + uuid + ".nuggets", nuggets - cost);
				}
				break;
				
			case "templar":
				if (level < requiredLevel) {
					notSufficientLevel = true;
					break;
				}
				if (nuggets < cost) {
					loss = true;
					break;
				}
				else {
					helmetRandomizer = (int) (Math.random() * 2 + 4);
					chestplateRandomizer = (int) (Math.random() * 2 + 4);
					leggingsRandomizer = (int) (Math.random() * 3 + 4);
					bootsRandomizer = (int) (Math.random() * 3 + 4);
					bowRandomizer = (int) (Math.random() * 3 + 3);
					

					ItemStack sword = new ItemStack(Material.GOLD_SWORD, 1);
					sword.addEnchantment(Enchantment.DAMAGE_ALL, 5);
					sword.addUnsafeEnchantment(Enchantment.DURABILITY, 10);
					ItemStack sword2 = new ItemStack(Material.GOLD_SWORD, 1);
					sword2.addEnchantment(Enchantment.KNOCKBACK, 2);
					ItemMeta swordn = sword.getItemMeta();
					swordn.setDisplayName(ChatColor.AQUA + "Templar " + ChatColor.AQUA + "Sword" + ChatColor.WHITE);
					ArrayList<String> swordl = new ArrayList<String>();
					
					swordl.add(ChatColor.GRAY + "Healing II");
					swordl.add("");
					swordl.add(ChatColor.DARK_GRAY + "Right-click on non-combat-");
					swordl.add(ChatColor.DARK_GRAY + "tagged players to heal them");
					swordl.add(ChatColor.DARK_GRAY + "8 HP. (12s cooldown)");
					
					swordn.setLore(swordl);
					
					sword.setItemMeta(swordn);

					player.getInventory().addItem(sword);
					player.getInventory().addItem(sword2);

					kit = true;
					check = true;
					
					config.set("players." + uuid + ".nuggets", nuggets - cost);
				}
				break;
				
			case "prestige":
				if (level < requiredLevel) {
					notSufficientLevel = true;
					break;
				}
				if (nuggets < cost) {
					loss = true;
					break;
				}
				else {
					helmetRandomizer = (int) (Math.random() * 2 + 5);
					chestplateRandomizer = (int) (Math.random() * 3 + 4);
					leggingsRandomizer = (int) (Math.random() * 3 + 4);
					bootsRandomizer = (int) (Math.random() * 2 + 5);
					bowRandomizer = 5;

					ItemStack sword = new ItemStack(Material.DIAMOND_SWORD, 1);
					sword.addEnchantment(Enchantment.DAMAGE_ALL, 3);
					ItemMeta swordn = sword.getItemMeta();
					swordn.setDisplayName(ChatColor.AQUA + "Prestige Sword");
					sword.setItemMeta(swordn);

					player.getInventory().addItem(sword);

					kit = true;
					check = true;
					
					if (WorldManager.isInSpawn(player.getLocation())) {
						Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(),
								"effect " + player.getName() + " absorption 15 2");
					}
					else {
						player.sendMessage("You didn't recieve the effects since you aren't at spawn!");
					}

					BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
					scheduler.scheduleSyncDelayedTask(plugin, () -> {
						Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(),
								"give " + player.getName() + " obsidian 32");
					}, 2);
					scheduler.scheduleSyncDelayedTask(plugin, () -> {
						ItemStack bdr = new ItemStack(Material.BEDROCK, 1);
						ItemMeta bdrn = bdr.getItemMeta();
						bdrn.setDisplayName(ChatColor.WHITE + "unsellable bedrock lol");
						bdr.setItemMeta(bdrn);
						player.getInventory().addItem(bdr);
					}, 5);
					
					config.set("players." + uuid + ".nuggets", nuggets - cost);
				}
				break;
				
			case "valkyrie":
				if (level < requiredLevel) {
					notSufficientLevel = true;
					break;
				}
				if (nuggets < cost) {
					loss = true;
					break;
				}
				else {
					helmetRandomizer = (int) (Math.random() * 2 + 5);
					chestplateRandomizer = (int) (Math.random() * 2 + 5);
					leggingsRandomizer = (int) (Math.random() * 2 + 4);
					bowRandomizer = 5;

					ItemStack sword = new ItemStack(Material.IRON_SWORD, 1);
					sword.addEnchantment(Enchantment.DAMAGE_ALL, 4);
					ItemMeta swordn = sword.getItemMeta();
					swordn.setDisplayName(ChatColor.AQUA + "Valkyrie " + ChatColor.AQUA + "Sword" + ChatColor.WHITE);
					ArrayList<String> swordl = new ArrayList<String>();
					swordl.add(ChatColor.GRAY + "Lightning I");
					swordn.setLore(swordl);
					sword.setItemMeta(swordn);
					player.getInventory().addItem(sword);

					ItemStack bootsv = new ItemStack(Material.DIAMOND_BOOTS, 1);
					bootsv.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 2);
					ItemMeta bootsvn = bootsv.getItemMeta();
					bootsvn.setDisplayName(ChatColor.GREEN + "Valkyrie Boots");
					ArrayList<String> bootsvl = new ArrayList<String>();
					
					bootsvl.add(ChatColor.GRAY + "Speed I");
					bootsvl.add("");
					bootsvl.add(ChatColor.DARK_GRAY + "Hitting players will grant");
					bootsvl.add(ChatColor.DARK_GRAY + "Speed I for 2s.");
					bootsvn.setLore(bootsvl);
					bootsv.setItemMeta(bootsvn);

					bootsvn.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);

					if (player.getInventory().getBoots() == null) {
						player.getInventory().setBoots(bootsv);
					} else {
						player.getInventory().addItem(bootsv);
					}

					kit = true;
					check = true;
					
					config.set("players." + uuid + ".nuggets", nuggets - cost);
				}
				break;
				
			case "champion":
				if (level < requiredLevel) {
					notSufficientLevel = true;
					break;
				}
				if (nuggets < cost) {
					loss = true;
					break;
				}
				else {
					helmetRandomizer = (int) (Math.random() * 2 + 5);
					chestplateRandomizer = (int) (Math.random() * 2 + 5);
					leggingsRandomizer = (int) (Math.random() * 2 + 5);
					bootsRandomizer = (int) (Math.random() * 2 + 5);
					bowRandomizer = 5;
					swordRandomizer = (int) (Math.random() * 3 + 6);

					kit = true;
					check = true;
					
					config.set("players." + uuid + ".nuggets", nuggets - cost);
				}
				break;
				
			case "witherman":
				if (level < requiredLevel) {
					notSufficientLevel = true;
					break;
				}
				if (nuggets < cost) {
					loss = true;
					break;
				}
				else {
					helmetRandomizer = 6;
					chestplateRandomizer = 6;
					leggingsRandomizer = 6;
					bootsRandomizer = 6;
					bowRandomizer = 5;

					ItemStack sword = new ItemStack(Material.STONE_SWORD, 1);
					sword.addEnchantment(Enchantment.DAMAGE_ALL, 4);
					sword.addUnsafeEnchantment(Enchantment.DURABILITY, 10);
					ItemMeta swordn = sword.getItemMeta();
					swordn.setDisplayName(ChatColor.DARK_GRAY + "Witherman " + ChatColor.DARK_GRAY + "Sword" + ChatColor.WHITE);
					ArrayList<String> swordl = new ArrayList<String>();
					swordl.add(ChatColor.GRAY + "Wither X");
					swordn.setLore(swordl);
					sword.setItemMeta(swordn);
					player.getInventory().addItem(sword);

					kit = true;
					check = true;

					ItemStack pearl = new ItemStack(Material.IRON_HOE, 1);
					pearl.addEnchantment(Enchantment.DURABILITY, 1);
					pearl.setDurability((short) 250);
					ItemMeta pearln = pearl.getItemMeta();
					pearln.setDisplayName(ChatColor.RED + "The Scythe");
					ArrayList<String> pearl1 = new ArrayList<String>();
					pearl1.add(ChatColor.GRAY + "Executioner I");
					pearl1.add("");
					pearl1.add(ChatColor.DARK_GRAY + "Instantly kills anyone");
					pearl1.add(ChatColor.DARK_GRAY + "under 6 HP.");
					pearln.setLore(pearl1);
					pearl.setItemMeta(pearln);
					player.getInventory().addItem(pearl);
					
					config.set("players." + uuid + ".nuggets", nuggets - cost);
				}
				break;
				
			case "sans":
				if (level < requiredLevel) {
					notSufficientLevel = true;
					break;
				}
				if (nuggets < cost) {
					loss = true;
					break;
				}
				else {
					helmetRandomizer = 6;
					chestplateRandomizer = 6;
					leggingsRandomizer = 6;
					bootsRandomizer = 6;
					bowRandomizer = 0;

					kit = true;
					check = true;
					
					ItemStack boen = new ItemStack(Material.BONE, 1);
					boen.addUnsafeEnchantment(Enchantment.DAMAGE_ALL, 9);
					ItemMeta boenn = boen.getItemMeta();
					boenn.setDisplayName(ChatColor.AQUA + "Sans Bone");
					ArrayList<String> boenl = new ArrayList<String>();
					boenl.add("");
					boenl.add(ChatColor.BLUE + "+11.25 Attack Damage");
					boenn.setLore(boenl);
					boen.setItemMeta(boenn);

					ItemStack boww = new ItemStack(Material.BOW, 1, (short) 355);
					ItemMeta t = boww.getItemMeta();
					t.setDisplayName(ChatColor.BLUE + "Gaster " + ChatColor.BLUE + "Blaster" + ChatColor.WHITE);
					ArrayList<String> bowl = new ArrayList<String>();
					bowl.add(ChatColor.GRAY + "do you wanna have a bad time?");
					t.setLore(bowl);
					boww.setItemMeta(t);

					player.getInventory().addItem(boen);
					player.getInventory().addItem(boww);
					
					config.set("players." + uuid + ".nuggets", nuggets - cost);
				}
				break;
				
			case "thanos":
				if (level < requiredLevel) {
					notSufficientLevel = true;
					break;
				}
				if (nuggets < cost) {
					loss = true;
					break;
				}
				else {
					helmetRandomizer = 6;
					chestplateRandomizer = 6;
					leggingsRandomizer = 6;
					bootsRandomizer = 6;

					bowRandomizer = 5;
					swordRandomizer = 8;

					kit = true;
					check = true;

					ItemStack pearl = new ItemStack(Material.GOLD_NUGGET, 1);
					pearl.addUnsafeEnchantment(Enchantment.DAMAGE_ALL, 1);
					ItemMeta pearln = pearl.getItemMeta();
					pearln.setDisplayName(ChatColor.DARK_PURPLE + "The " + ChatColor.DARK_PURPLE + "Infinity "
							+ ChatColor.DARK_PURPLE + "Gauntlet" + ChatColor.WHITE);
					ArrayList<String> pearl1 = new ArrayList<String>();
					pearl1.add(ChatColor.GRAY + "Thanos I");
					pearl1.add("");
					pearl1.add(ChatColor.DARK_GRAY + "There is a 2% chance to cut");
					pearl1.add(ChatColor.DARK_GRAY + "a player's health in half.");
					pearl1.add("");
					pearl1.add(ChatColor.DARK_GRAY + "Right-click to detonate.");
					pearl1.add(ChatColor.DARK_GRAY + "(10s cooldown)");
					pearln.setLore(pearl1);
					pearl.setItemMeta(pearln);

					BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
					scheduler.scheduleSyncDelayedTask(plugin, () -> {
						player.getInventory().addItem(pearl);
					}, 5);

					int q = (int) (Math.random() * 9 + 1);
					
					switch (q) {
					case 1:
						sender.sendMessage(ChatColor.DARK_PURPLE + "The end is near.");
						break;
					case 2:
						sender.sendMessage(ChatColor.DARK_PURPLE
								+ "You’re strong. But I could snap my fingers, and you’d all cease to exist.");
						break;
					case 3:
						sender.sendMessage(ChatColor.DARK_PURPLE
								+ "Fun isn’t something one considers when balancing the universe. But this… does put a smile on my face.");
						break;
					case 4:
						sender.sendMessage(ChatColor.DARK_PURPLE
								+ "Stark… you have my respect. I hope the people of Earth will remember you.");
						break;
					case 5:
						sender.sendMessage(ChatColor.DARK_PURPLE + "Perfectly balanced, as all things should be.");
						break;
					case 6:
						sender.sendMessage(ChatColor.DARK_PURPLE + "You should have gone for the head.");
						break;
					case 7:
						sender.sendMessage(ChatColor.DARK_PURPLE
								+ "I know what it’s like to lose. To feel so desperately that you’re right, yet to fail nonetheless. Dread it. Run from it. Destiny still arrives. Or should I say, I have.");
						break;
					case 8:
						sender.sendMessage(ChatColor.DARK_PURPLE
								+ "I ignored my destiny once, I can not do that again. Even for you. I’m sorry Little one.");
						break;
					case 9:
						sender.sendMessage(ChatColor.DARK_PURPLE + "The hardest choices require the strongest wills.");
						break;
					}
					
					config.set("players." + uuid + ".nuggets", nuggets - cost);
				}
				break;
				
			case "spoon":
				if (!Checks.checkSpoon(player)) {
					if (level < requiredLevel) {
						notSufficientLevel = true;
						break;
					}
					if (nuggets < cost) {
						loss = true;
						break;
					}
				}
				
				check = true;
				Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "give " + player.getName()
						+ " iron_shovel 1 0 {display:{Name:\"§r§9§kX§r"
						+ " §9The §9Spoon §kX§f\",Lore:[\"§7Lightning V\"]},ench:[{id:16,lvl:8},{id:32,lvl:8},{id:34,lvl:3}]}");
				
				helmetRandomizer = 6;
				chestplateRandomizer = 6;
				leggingsRandomizer = 6;
				bootsRandomizer = 6;

				bowRandomizer = 5;

				kit = true;
				check = true;
				
				if (!Checks.checkSpoon(player)) config.set("players." + uuid + ".nuggets", nuggets - cost);
				break;
			}
			
			if (notSufficientLevel) {
				sender.sendMessage(ChatColor.WHITE + "You must be at least level " + ChatColor.YELLOW + requiredLevel
						+ ChatColor.WHITE + " to purchase " + ChatColor.YELLOW + kitName + ChatColor.WHITE + "!");
				return true;
			}
			
			if (loss) {
				int nuggetDifference = cost - nuggets;
				sender.sendMessage(ChatColor.WHITE + "You need " + ChatColor.YELLOW + NumberFormat.getNumberInstance(Locale.US).format(nuggetDifference)
						+ ChatColor.WHITE + " more nuggets to purchase " + ChatColor.YELLOW + kitName + ChatColor.WHITE + "!");
				return true;
			}

			if (!check) {
				sender.sendMessage(ChatColor.WHITE + "That kit does not exist.");
				return true;
			}
			else {
				sender.sendMessage(ChatColor.WHITE + "You have received kit " + ChatColor.YELLOW + kitName + ChatColor.WHITE + ".");

				plugin.kits.expiration.put(player.getName(), System.currentTimeMillis() + 300000);
				
				if (swordRandomizer > 0) {
					swordItem = KitGear.sword(swordRandomizer, ChatColor.AQUA + kitName + " " + ChatColor.AQUA + "Sword" + ChatColor.WHITE);
					player.getInventory().addItem(swordItem);
				}
				
				if (bowRandomizer > 0) {
					player.getInventory().addItem(KitGear.bow(bowRandomizer));
				}
				
				if (helmetRandomizer > 0) {
					if (player.getInventory().getHelmet() == null) {
						player.getInventory().setHelmet(KitGear.armor("helmet", helmetRandomizer));
					}
					else {
						player.getInventory().addItem(KitGear.armor("helmet", helmetRandomizer));
					}
				}
				
				if (chestplateRandomizer > 0) {
					if (player.getInventory().getChestplate() == null) {
						player.getInventory().setChestplate(KitGear.armor("chestplate", chestplateRandomizer));
					}
					else {
						player.getInventory().addItem(KitGear.armor("chestplate", chestplateRandomizer));
					}
				}
				
				if (leggingsRandomizer > 0) {
					if (player.getInventory().getLeggings() == null) {
						player.getInventory().setLeggings(KitGear.armor("leggings", leggingsRandomizer));
					}
					else {
						player.getInventory().addItem(KitGear.armor("leggings", leggingsRandomizer));
					}
				}
				
				if (bootsRandomizer > 0) {
					if (player.getInventory().getBoots() == null) {
						player.getInventory().setBoots(KitGear.armor("boots", bootsRandomizer));
					}
					else {
						player.getInventory().addItem(KitGear.armor("boots", bootsRandomizer));
					}
				}

				if (kit == true) {

					Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(),
							"give " + player.getName() + " fishing_rod");
					Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(),
							"give " + player.getName() + " golden_apple 12");
					Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(),
							"give " + player.getName() + " arrow 48");
					Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(),
							"give " + player.getName() + " iron_pickaxe");
					Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(),
							"give " + player.getName() + " iron_axe");
					Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(),
							"give " + player.getName() + " iron_shovel");
					Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(),
							"give " + player.getName() + " cobblestone 64");
					Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(),
							"give " + player.getName() + " planks 64");
					Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(),
							"give " + player.getName() + " water_bucket 2");
					Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(),
							"give " + player.getName() + " lava_bucket 2");
					Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(),
							"give " + player.getName() + " cooked_beef 12");
					BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
					scheduler.scheduleSyncDelayedTask(plugin, () -> {
						Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(),
								"give " + player.getName() + " compass 1 0 {display:{Name:\"§fPlayer Tracker\"}}");
					}, 3);
				}

				player.updateInventory();
				BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
				scheduler.scheduleSyncDelayedTask(plugin, () -> {
					player.updateInventory();
				}, 5);
			}
		}
		
		Configs.playerstats.saveConfig();
		Team display = player.getScoreboard().getTeam("nuggets");
		String val = NumberFormat.getNumberInstance(Locale.US).format(Configs.playerstats.getConfig().get("players." + uuid + ".nuggets"));
		display.setSuffix(val);
		return true;
	}
}
