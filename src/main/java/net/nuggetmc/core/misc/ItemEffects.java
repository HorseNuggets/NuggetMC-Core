package net.nuggetmc.core.misc;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Horse;
import org.bukkit.entity.Horse.Color;
import org.bukkit.entity.Horse.Style;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import net.minecraft.server.v1_8_R3.EnumParticle;
import net.minecraft.server.v1_8_R3.PacketPlayOutWorldParticles;
import net.nuggetmc.core.Main;
import net.nuggetmc.core.data.Configs;
import net.nuggetmc.core.modifiers.CombatTracker;
import net.nuggetmc.core.setup.WorldManager;
import net.nuggetmc.core.util.ActionBar;
import net.nuggetmc.core.util.ColorCodes;

public class ItemEffects {

	private static List<String> worldlist;
	private static Map<Player, Long> heal;
	private static Set<Arrow> explosiveArrows;
	private static Map<Player, Long> boom;

	public static Main plugin;

	public ItemEffects(Main plugin) {
		ItemEffects.plugin = plugin;
		ItemEffects.worldlist = Configs.worldsettings.getConfig().getStringList("uhckit-worlds");
		ItemEffects.heal = new HashMap<>();
		ItemEffects.explosiveArrows = new HashSet<>();
		ItemEffects.boom = new HashMap<>();
	}

	public static void boomBox(BlockPlaceEvent event) {
		Player player = event.getPlayer();
		if (worldlist.contains(player.getWorld().getName())) {
			if (!player.getItemInHand().equals(null)) {
				Block block = event.getBlock();
				if (block.getType() == Material.TNT) {
					if (player.isSneaking()) {
						block.setType(Material.AIR);
						player.getWorld().spawnEntity(block.getLocation().add(0.5D, 0.5D, 0.5D), EntityType.PRIMED_TNT);
					}
				}
				if (player.getItemInHand().getType() == Material.REDSTONE_BLOCK) {
					if (block.getType() == Material.REDSTONE_BLOCK) {
						if (player.getItemInHand().hasItemMeta()) {
							if (player.getItemInHand().getItemMeta().hasDisplayName()) {
								if (player.getItemInHand().getItemMeta().getDisplayName().equals(ChatColor.RED + "Boom Box")) {
									
									Location location = event.getBlock().getLocation();
									if (!WorldManager.isInSpawn(location)) {
										player.setItemInHand(null);
										
										location.getBlock().setType(Material.AIR);
										Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> {
											TNTPrimed tnt = (TNTPrimed) player.getWorld().spawnEntity(location, EntityType.PRIMED_TNT);
											tnt.setFuseTicks(0);
										}, 2);
									}
								}
							}
						}
					}
				}
			}
		}
		return;
	}

	public static void onEntityDamage(EntityDamageByEntityEvent event) {
		if (event.getDamager() instanceof Player) {
			if (event.getEntity() instanceof Player) {
				Player player = (Player) event.getDamager();
				if (event.getDamage() > 0 && !event.isCancelled()) {
					if (worldlist.contains(player.getWorld().getName())) {
						if (player.getInventory().getBoots() != null) {
							if (player.getInventory().getBoots().hasItemMeta()) {
								if (player.getInventory().getBoots().getItemMeta().hasDisplayName()) {
									if (player.getInventory().getBoots().getItemMeta().getDisplayName().equals(ChatColor.GREEN + "Scout Boots")
											|| player.getInventory().getBoots().getItemMeta().getDisplayName().equals(ChatColor.GREEN + "Valkyrie Boots")) {
										Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(),
												"effect " + player.getName() + " speed 2");
									}
								}
							}
						}
						
						Player victim = (Player) event.getEntity();
						ItemStack item = player.getItemInHand();
						if (item != null) {
							if (item.hasItemMeta()) {
								ItemMeta meta = item.getItemMeta();
								if (meta.hasDisplayName()) {
									String name = meta.getDisplayName();
									if (name.equals(ChatColor.AQUA + "Horseman Axe") || name.equals(ChatColor.AQUA + "Jouster Axe")) {
										Entity vehicle = player.getVehicle();
										if (vehicle != null) {
											if (vehicle.getType() == EntityType.HORSE) {
												event.setDamage(event.getDamage() * 2);
												Location loc = victim.getLocation().add(0, 1, 0);
												
												float x = (float) loc.getX();
												float y = (float) loc.getY();
												float z = (float) loc.getZ();
												
												PacketPlayOutWorldParticles lavaParticles = new PacketPlayOutWorldParticles(EnumParticle.LAVA,
														true, x, y, z, (float) 0.3, (float) 0.3, (float) 0.3, (float) 0, 10, null);
												
												for (Player all : Bukkit.getOnlinePlayers()) {
													((CraftPlayer) all).getHandle().playerConnection.sendPacket(lavaParticles);
												}
											}
										}
									}
									
									else if (name.equals(ChatColor.RED + "The Scythe")) {
										if (victim.getHealth() <= 6) {
											event.setDamage(1000);
										}
										if (item.getAmount() == 1) {
											player.setItemInHand(null);
										} else {
											item.setAmount(item.getAmount() - 1);
										}
									}
									
									else if (name.equals(ChatColor.YELLOW + "Sword of JUSTICE")) {
										victim.getWorld().strikeLightning(victim.getLocation());
									}
									
									else if (name.equals("§r§9§kX§r §9The §9Spoon §kX§f")) {
										int rando = (int) (Math.random() * 4);

										if (rando == 2) {
											victim.getWorld().strikeLightning(victim.getLocation());
										}
									}
									
									else if (name.equals(ChatColor.DARK_GRAY + "Witherman " + ChatColor.DARK_GRAY + "Sword" + ChatColor.WHITE)) {
										Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(),
												"effect " + victim.getName() + " wither 3");
									}
									
									else if (name.equals(ChatColor.DARK_PURPLE + "The " + ChatColor.DARK_PURPLE + "Infinity "
											+ ChatColor.DARK_PURPLE + "Gauntlet" + ChatColor.WHITE)) {
										int rando = (int) (Math.random() * 50 + 1);

										if (rando == 29) {
											double health = victim.getHealth() / 2;
											victim.setHealth(health);
											
											for (Player all : Bukkit.getOnlinePlayers()) {
												all.playSound(victim.getLocation(), Sound.ZOMBIE_WOODBREAK, 1, 1);
											}
										}
									}
									
									else if (name.equals(ChatColor.AQUA + "Valkyrie " + ChatColor.AQUA + "Sword" + ChatColor.WHITE)) {
										int rando = (int) (Math.random() * 20);

										if (rando == 7) {
											victim.getWorld().strikeLightning(victim.getLocation());
										}
									}
								}
							}
						}
					}
				}
			}
		}
		return;
	}
	
	public static void onDeath(PlayerDeathEvent event) {
		Player player = event.getEntity();
		if (worldlist.contains(player.getWorld().getName())) {
			Location location = player.getLocation();
			if (!WorldManager.isInSpawn(location)) {
				if (player.getInventory().contains(Material.NETHER_STALK)) {
					boolean check = false;
					for (int i = 0; i < 36; i++) {
						ItemStack slot = player.getInventory().getItem(i);
						if (slot != null) {
							if (slot.getType() == Material.NETHER_STALK) {
								if (slot.hasItemMeta()) {
									ItemMeta meta = slot.getItemMeta();
									if (meta.hasDisplayName()) {
										if (meta.getDisplayName().equals(ChatColor.RED + "Blast Totem")) {
											check = true;
										}
									}
								}
							}
						}
					}
					
					if (check) {
						Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "clear " + player.getName() + " nether_wart");
						TNTPrimed tnt = (TNTPrimed) player.getWorld().spawnEntity(player.getLocation(), EntityType.PRIMED_TNT);
						tnt.setFuseTicks(0);
					}
				}
			}
		}
		return;
	}
	
	private static String horseman = ChatColor.RESET + "Spawn Horse " + ChatColor.GRAY + "(Horseman)";
	private static String jouster = ChatColor.RESET + "Spawn Horse " + ChatColor.GRAY + "(Jouster)";
	private static String areion = ChatColor.RESET + "Spawn Horse " + ChatColor.GRAY + "(Areion)";
	private static String tulpar = ChatColor.RESET + "Spawn Horse " + ChatColor.GRAY + "(Tulpar)";
	private static String sleipnir = ChatColor.RESET + "Spawn Horse " + ChatColor.GRAY + "(Sleipnir)";
	private static String phaethon = ChatColor.RESET + "Spawn Horse " + ChatColor.GRAY + "(Phaethon)";
	
	public static void templar(PlayerInteractEntityEvent event) {
		Player healer = event.getPlayer();
		if (event.getRightClicked() instanceof Player) {
			ItemStack item = healer.getItemInHand();
			if (item != null) {
				if (item.getType() == Material.GOLD_SWORD) {
					if (item.hasItemMeta()) {
						ItemMeta meta = item.getItemMeta();
						if (meta.hasDisplayName()) {
							String name = meta.getDisplayName();
							if (name.equals(ChatColor.AQUA + "Templar " + ChatColor.AQUA + "Sword" + ChatColor.WHITE)) {
								
								Player player = (Player) event.getRightClicked();
								
								if (CombatTracker.combatTime.containsKey(player)) {
									ActionBar actionBar = new ActionBar("§cYou cannot heal someone who is in combat!");
									actionBar.Send(healer);
									return;
								}
								
								else {
									Location loc = player.getLocation().add(0, 1, 0);
									
									float x = (float) loc.getX();
									float y = (float) loc.getY();
									float z = (float) loc.getZ();
									
									
									if (heal.containsKey(healer)) {
										Long difference = heal.get(healer) - System.currentTimeMillis() / 1000;
										if (difference > 0) {
											ActionBar actionBar = new ActionBar("You are on a cooldown! (§e" + difference + "s§r)");
											actionBar.Send(healer);
											return;
										}
										
										else {
											heal.remove(healer);
										}
									}
											
									Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "effect " + player.getName() + " instant_health 1 1");
									
									PacketPlayOutWorldParticles packet = new PacketPlayOutWorldParticles(EnumParticle.HEART, true, x, y, z, (float) 0.5, (float) 0.5, (float) 0.5, (float) 0.15, 12, null);
									for (Player all : Bukkit.getOnlinePlayers()) {
										all.playSound(loc, Sound.LEVEL_UP, 1, 2);
										((CraftPlayer) all).getHandle().playerConnection.sendPacket(packet);
									}
									
									ActionBar actionBar = new ActionBar("You have been healed by " + ColorCodes.colorName(healer.getUniqueId(), healer.getName()) + "§r!");
									actionBar.Send(player);
									
									ActionBar healConf = new ActionBar("You have healed " + ColorCodes.colorName(player.getUniqueId(), player.getName()) + "§r!");
									healConf.Send(healer);
									
									heal.put(healer, ((Long) ((System.currentTimeMillis() / 1000)) + 12));
								}
								
								return;
							}
		    			}
					}
	    		}
			}
		}
		return;
	}
	
	public static void onEntityShootBow(EntityShootBowEvent event) {
		ItemStack bow = event.getBow();
		if (bow != null) {
			if (bow.hasItemMeta()) {
				ItemMeta meta = bow.getItemMeta();
				if (meta.hasDisplayName()) {
					String name = meta.getDisplayName();
					if (name.equals(ChatColor.BLUE + "Gaster " + ChatColor.BLUE + "Blaster" + ChatColor.WHITE)) {
						explosiveArrows.add((Arrow) event.getProjectile());
					}
				}
			}
		}
		return;
	}

	public static void onProjectileHit(ProjectileHitEvent event) {
		Projectile projectile = event.getEntity();
		Location loc = projectile.getLocation();
		
		if (explosiveArrows.contains(projectile)) {
			if (worldlist.contains(projectile.getWorld().getName())) {
				if (!WorldManager.isInSpawn(loc)) {
					projectile.getWorld().createExplosion(projectile.getLocation(), 2.0F);
				}
			}
			explosiveArrows.remove(projectile);
		}
		return;
	}
	
	public static void itemInteract(PlayerInteractEvent event) {
		Player player = event.getPlayer();
		ItemStack item = player.getItemInHand();
		
		if (item != null) {
			if (item.hasItemMeta()) {
				ItemMeta meta = item.getItemMeta();
				if (meta.hasDisplayName()) {
					String name = meta.getDisplayName();
					
					if (event.getAction().equals(Action.RIGHT_CLICK_BLOCK) || event.getAction().equals(Action.RIGHT_CLICK_AIR)) {
						if (player.getItemInHand().getType() == Material.IRON_SWORD) {
							if (name.equals(ChatColor.AQUA + "Samurai " + ChatColor.AQUA + "Sword" + ChatColor.WHITE)) {
								
								Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(),
										"effect " + player.getName() + " resistance 1");
								
								Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
									for (Player all : Bukkit.getOnlinePlayers()) {
										all.playSound(player.getLocation(), Sound.ANVIL_LAND, 1, (float) 1.7);
									}
								});
							}
						}
					}
					
					if (player.getItemInHand().getType() == Material.NETHER_STAR) {
						if (name.equals(ChatColor.DARK_PURPLE + "Teleport Crystal")) {
							Player near = null;
							
							for (Player victim : Bukkit.getOnlinePlayers()) {
								if (player.getWorld() == victim.getWorld()) {
									if (player.getLocation().distance(victim.getLocation()) <= 10 && !player.equals(victim) && !victim.isDead() && victim.getGameMode() == GameMode.SURVIVAL) {
										if (near == null) {
											near = victim;
										}
										else if (player.getLocation().distance(victim.getLocation()) < player.getLocation().distance(near.getLocation())) {
											near = victim;
										}
									}
								}
							}
							
							if (near != null) {
								player.teleport(near);
								
								if (item.getAmount() == 1) {
									player.setItemInHand(null);
								} else {
									item.setAmount(item.getAmount() - 1);
								}
								
								float x = (float) player.getLocation().getX();
								float y = (float) (player.getLocation().getY() + 1);
								float z = (float) player.getLocation().getZ();
								
								Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "playsound mob.endermen.portal @a " + x + " " + y + " " + z);
								
								PacketPlayOutWorldParticles packet = new PacketPlayOutWorldParticles(EnumParticle.SPELL_WITCH, true, x, y, z, (float) 0.5, (float) 0.5, (float) 0.5, (float) 0.15, 40, null);
								for (Player all : Bukkit.getOnlinePlayers()) ((CraftPlayer) all).getHandle().playerConnection.sendPacket(packet);
							}
							
							else {
								ActionBar actionBar = new ActionBar("No players within §e10 §rmeters!");
								actionBar.Send(player);
							}
						}
					}
					
					else if (name.equals(ChatColor.DARK_PURPLE + "The " + ChatColor.DARK_PURPLE + "Infinity "
							+ ChatColor.DARK_PURPLE + "Gauntlet" + ChatColor.WHITE)) {
						if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
							
							if (boom.containsKey(player)) {
								Long difference = boom.get(player) - System.currentTimeMillis() / 1000;
								if (difference > 0) {
									ActionBar actionBar = new ActionBar("You are on a cooldown! (§e" + difference + "s§r)");
									actionBar.Send(player);
									return;
								}
								
								else {
									boom.remove(player);
								}
							}
							
							Location loc = player.getLocation();
							
							if (WorldManager.isInSpawn(loc)) {
								ActionBar ab = new ActionBar("You cannot use this here!");
								ab.Send(player);
								return;
							}

							else {
								TNTPrimed tnt = (TNTPrimed) player.getWorld().spawnEntity(loc, EntityType.PRIMED_TNT);
								tnt.setFuseTicks(0);
							}
							
							boom.put(player, ((Long) ((System.currentTimeMillis() / 1000)) + 10));
						}
					}
				}
			}
		}
		
		if (event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
			if (item.getType() == Material.MONSTER_EGG) {
				if (item.hasItemMeta()) {
					ItemMeta meta = item.getItemMeta();
					if (meta.hasDisplayName()) {
						Location loc = event.getClickedBlock().getLocation().add(0, 1, 0);
						if (worldlist.contains(player.getWorld().getName())) {
							if (!WorldManager.isInSpawn(loc)) {
								
								boolean check = false;
								Horse horse = (Horse) player.getWorld().spawnEntity(loc, EntityType.HORSE);
								String name = meta.getDisplayName();
								
								if (name.equals(horseman)) {
									check = true;
									horse.setVariant(Horse.Variant.HORSE);
									horse.setStyle(Style.NONE);
									horse.setColor(Color.CREAMY);
									horse.setMaxHealth(24);
									horse.setOwner(player);
									horse.setTamed(true);
									horse.setAdult();
									horse.setJumpStrength(0.7);
									horse.getInventory().setArmor(new ItemStack(Material.IRON_BARDING));
									horse.getInventory().setSaddle(new ItemStack(Material.SADDLE));
	
									Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> {
										horse.setHealth(24);
									}, 3);
								}
								
								else if (name.equals(jouster)) {
									check = true;
									horse.setVariant(Horse.Variant.HORSE);
									horse.setStyle(Style.NONE);
									horse.setColor(Color.WHITE);
									horse.setMaxHealth(52);
									horse.setOwner(player);
									horse.setTamed(true);
									horse.setAdult();
									horse.setJumpStrength(0.8);
									horse.getInventory().setArmor(new ItemStack(Material.IRON_BARDING));
									horse.getInventory().setSaddle(new ItemStack(Material.SADDLE));
	
									Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> {
										horse.setHealth(52);
									}, 3);
								}
								
								else if (name.equals(areion)) {
									check = true;
									horse.setVariant(Horse.Variant.HORSE);
									horse.setStyle(Style.WHITEFIELD);
									horse.setColor(Color.CHESTNUT);
									horse.setMaxHealth(56);
									horse.setOwner(player);
									horse.setTamed(true);
									horse.setAdult();
									horse.setJumpStrength(0.8);
									horse.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 2147483647, 0));
									horse.getInventory().setArmor(new ItemStack(Material.IRON_BARDING));
									horse.getInventory().setSaddle(new ItemStack(Material.SADDLE));
	
									Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> {
										horse.setHealth(56);
									}, 3);
								}
								
								else if (name.equals(tulpar)) {
									check = true;
									horse.setVariant(Horse.Variant.HORSE);
									horse.setStyle(Style.BLACK_DOTS);
									horse.setColor(Color.GRAY);
									horse.setMaxHealth(60);
									horse.setOwner(player);
									horse.setTamed(true);
									horse.setAdult();
									horse.setJumpStrength(0.7);
									horse.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 2147483647, 0));
									horse.getInventory().setArmor(new ItemStack(Material.IRON_BARDING));
									horse.getInventory().setSaddle(new ItemStack(Material.SADDLE));
	
									Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> {
											horse.setHealth(60);
									}, 3);
								}
								
								else if (name.equals(sleipnir)) {
									check = true;
									horse.setVariant(Horse.Variant.HORSE);
									horse.setStyle(Style.WHITE_DOTS);
									horse.setColor(Color.BROWN);
									horse.setMaxHealth(56);
									horse.setOwner(player);
									horse.setTamed(true);
									horse.setAdult();
									horse.setJumpStrength(1);
									horse.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 2147483647, 0));
									horse.getInventory().setArmor(new ItemStack(Material.IRON_BARDING));
									horse.getInventory().setSaddle(new ItemStack(Material.SADDLE));
	
									Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> {
										horse.setHealth(56);
									}, 3);
								}
								
								else if (name.equals(phaethon)) {
									check = true;
									horse.setVariant(Horse.Variant.SKELETON_HORSE);
									horse.setMaxHealth(36);
									horse.setOwner(player);
									horse.setTamed(true);
									horse.setAdult();
									horse.setJumpStrength(0.7);
									horse.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 2147483647, 1));
									horse.getInventory().setSaddle(new ItemStack(Material.SADDLE));
									
									Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> {
										horse.setHealth(36);
									}, 3);
								}
								
								if (check) {
									if (item.getAmount() == 1) {
										player.setItemInHand(null);
									} else {
										item.setAmount(item.getAmount() - 1);
									}
								}
								
								return;
							}
						}
						
						player.sendMessage("You cannot spawn horses here!");
					}
				}
			}
		}
		return;
	}
}