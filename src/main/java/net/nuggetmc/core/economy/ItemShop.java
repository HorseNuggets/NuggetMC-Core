package net.nuggetmc.core.economy;

import java.text.NumberFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Sign;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import net.minecraft.server.v1_8_R3.MojangsonParseException;
import net.minecraft.server.v1_8_R3.MojangsonParser;
import net.minecraft.server.v1_8_R3.NBTTagCompound;
import net.nuggetmc.core.Main;
import net.nuggetmc.core.data.Configs;
import net.nuggetmc.core.setup.WorldManager;

public class ItemShop implements CommandExecutor {
	
	private Main plugin;
	private Map<String, Wrapper> shop;
	private FileConfiguration itemshop;
	private FileConfiguration playerstats;
	
	public ItemShop(Main plugin) {
		this.plugin = plugin;
		this.itemshop = Configs.itemshop.getConfig();
		this.playerstats = Configs.playerstats.getConfig();
		this.shopSetup();
	}
	
	private int[] pos1 = {28, 209, 1};
	private int[] pos2 = {50, 233, 29};
	
	class Wrapper {
	    public Wrapper(String tag, ItemStack value) {
	       this.tag = tag;
	       this.value = value;
	    }

		private String tag;
	    private ItemStack value;

	    public String getTag() {
	    	return this.tag;
	    }
	    
	    public ItemStack getValue() {
	    	return this.value;
	    }
	}
	
	private void shopSetup() {
		this.shop = new HashMap<>();
		for (String key : itemshop.getKeys(false)) {
			String keySpace = key.replaceAll("_", " ");
			
			String entry = itemshop.getString(key + ".code");
			entry = entry.replaceAll("&", "§");
			entry = entry.replaceAll(";;", ":");
			String[] sep = entry.split("%%");
			
			List<Integer> coords = itemshop.getIntegerList(key + ".pos");
			Location loc = new Location(Bukkit.getWorld("main"), coords.get(0), coords.get(1), coords.get(2));
			
			for (int i = -1; i <= 1; i += 2) {
				Location signLoc = loc.clone().add(0, i, 0);
				
				Block block = signLoc.getBlock();
				block.setType(Material.WALL_SIGN);
				
				Sign sign = (Sign) block.getState();
				
				org.bukkit.material.Sign matSign = (org.bukkit.material.Sign) block.getState().getData();
				
				switch (coords.get(3)) {
				case 0:
					matSign.setFacingDirection(BlockFace.NORTH);
					break;
				case 1:
					matSign.setFacingDirection(BlockFace.SOUTH);
				}
				
				sign.setData(matSign);
				sign.setLine(2, keySpace);
				
				int value = 0;
				int amount = 0;
				
				switch (i) {
				case -1:
					value = itemshop.getInt(key + ".sell.value");
					amount = itemshop.getInt(key + ".sell.amount");
					sign.setLine(0, "[Sell]");
					break;
				case 1:
					value = itemshop.getInt(key + ".buy.value");
					amount = itemshop.getInt(key + ".buy.amount");
					sign.setLine(0, "[Buy]");
				}
	
				sign.setLine(1, NumberFormat.getNumberInstance(Locale.US).format(amount));
				sign.setLine(3, NumberFormat.getNumberInstance(Locale.US).format(value));
				
				if (value == -1 || amount == -1) {
					sign.setLine(0, "");
					sign.setLine(1, "You cannot");
					sign.setLine(2, "sell this item!");
					sign.setLine(3, "");
				}
				
				sign.update();
			}
			
			ItemStack item = new ItemStack(Material.valueOf(sep[0]), Integer.parseInt(sep[2]), Byte.parseByte(sep[3]));
			net.minecraft.server.v1_8_R3.ItemStack nmsStack = CraftItemStack.asNMSCopy(item);
			
			ItemStack bukkitStack = null;
			
			try {
				NBTTagCompound nbt = MojangsonParser.parse(sep[4]);
				nmsStack.setTag(nbt);
				bukkitStack = CraftItemStack.asBukkitCopy(nmsStack);
			}
			
			catch (MojangsonParseException e) {
				bukkitStack = new ItemStack(Material.AIR);
			}
			
			shop.put(keySpace, new Wrapper(sep[1] + " " + sep[2] + " "  + sep[3] + " "  + sep[4], bukkitStack));
			
			if (bukkitStack.getType() == Material.DIAMOND_SWORD) {
				ItemMeta bukkitStackMeta = bukkitStack.getItemMeta();
				bukkitStackMeta.setDisplayName(ChatColor.WHITE + "Sharpness III");
				bukkitStack.setItemMeta(bukkitStackMeta);
			}
			
			World world = loc.getWorld();
			for(Entity i : world.getEntities()){
	            if (i.getType().equals(EntityType.ITEM_FRAME)) {
	            	Location location = i.getLocation();
	            	
	            	int x = location.getBlockX();
	            	int y = location.getBlockY();
	            	int z = location.getBlockZ();
				
	            	if (x > pos1[0] && x < pos2[0] && y > pos1[1] && y < pos2[1] && z > pos1[2] && z < pos2[2]) {
	            		i.remove();
	            	}
	            }
	        }
			
			final ItemStack frameItem = bukkitStack;
			Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> {
				ItemFrame frame = (ItemFrame) world.spawnEntity(world.getBlockAt(loc).getLocation(), EntityType.ITEM_FRAME);
				frame.setItem(frameItem);
			}, 10);
		
		}
		return;
	}
	
	public void transaction(PlayerInteractEvent event) {
		if (event.getClickedBlock() != null) {
			Player player = event.getPlayer();
			if (player.getWorld().getName().equals(WorldManager.spawnworld) || player.getWorld().getName().equals("world")) {
				
				Block block = event.getClickedBlock();
				Material sign = block.getType();
				
				if (sign.equals(Material.SIGN_POST) || sign.equals(Material.WALL_SIGN)) {
					
					Location location = player.getLocation();
					
					int x = location.getBlockX();
					int y = location.getBlockY();
					int z = location.getBlockZ();
					
					if (x > pos1[0] && x < pos2[0] && y > pos1[1] && y < pos2[1] && z > pos1[2] && z < pos2[2]) {
						UUID uuid = player.getUniqueId();
						int nuggets = playerstats.getInt("players." + uuid + ".nuggets");
						
						Sign signVar = (Sign) block.getState();
						String[] ln = signVar.getLines();
						
						if (ln[2].equals("all")) {
							for (String id : shop.keySet()) {
								Wrapper w = shop.get(id);
								String tag = w.getTag();
								tag = tag.replaceAll("<random>", String.valueOf((int) (Math.random() * 214748367 + 1)));
								Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "give " + player.getName() + " " + tag);
							}
							return;
						}
						
						try {
							Wrapper val = shop.get(ln[2]);
							int price = Integer.parseInt(ln[3].replaceAll(",", ""));
							
							if (ln[0].equals("[Buy]")) {
								if (nuggets < price) {
									String s = "s";
									if (price == 1) s = "";
									player.sendMessage("You need at least " + ChatColor.YELLOW
											+ NumberFormat.getNumberInstance(Locale.US).format(price)
											+ ChatColor.RESET + " point" + s + " to buy this!");
									return;
								}
								
								else {
									String tag = val.getTag();
									tag = tag.replaceAll("<random>", String.valueOf((int) (Math.random() * 214748367 + 1)));
									
									Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "give " + player.getName() + " " + tag);
									playerstats.set("players." + uuid + ".nuggets", nuggets - price);
									Configs.playerstats.saveConfig();
									plugin.sidebar.enable(player);
								}
							}
							
							else if (ln[0].equals("[Sell]")) {
								ItemStack item = val.getValue();
								if (player.getInventory().containsAtLeast(item, 1)) {
									if (player.isSneaking()) {
										
										ItemStack check = item.clone();
										check.setAmount(1);
										
										int amount = 0;
										if (!(check == null)) {
											for (int i = 0; i < 36; i++) {
												ItemStack slot = player.getInventory().getItem(i);
												if (slot == null || !slot.isSimilar(check)) continue;
												amount += slot.getAmount();
											}
										}
										
										int sells = (int) amount / item.getAmount();
										for (int i = 1; i <= amount; i++) {
											player.getInventory().removeItem(item);
										}
										
										playerstats.set("players." + uuid + ".nuggets", nuggets + sells * price);
										Configs.playerstats.saveConfig();
										plugin.sidebar.enable(player);

										player.playSound(location, Sound.CHICKEN_EGG_POP, 1, 1);
									}
									
									else {
										player.getInventory().removeItem(item);
										playerstats.set("players." + uuid + ".nuggets", nuggets + price);
										Configs.playerstats.saveConfig();
										plugin.sidebar.enable(player);

										double dX = location.getX();
										double dY = location.getY();
										double dZ = location.getZ();

										Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(),
												"playsound random.pop " + player.getName() + " " + dX + " " + dY + " " + dZ + " 0.4 1.6");
									}
								}
								
								else {
									player.sendMessage("You have none of this item to sell!");
								}
							}
						}
						
						catch (NullPointerException | NumberFormatException e) {
							return;
						}
					}
				}
			}
		}
		return;
	}
	
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		shop.clear();
		shopSetup();
		return true;
	}
}
