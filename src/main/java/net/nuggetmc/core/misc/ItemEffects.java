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
import org.bukkit.entity.ThrownPotion;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageModifier;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.entity.PotionSplashEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.event.player.PlayerItemDamageEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.Potion;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import net.minecraft.server.v1_8_R3.EntityPlayer;
import net.minecraft.server.v1_8_R3.EnumParticle;
import net.minecraft.server.v1_8_R3.PacketPlayOutWorldParticles;
import net.nuggetmc.core.Main;
import net.nuggetmc.core.data.Configs;
import net.nuggetmc.core.modifiers.CombatTracker;
import net.nuggetmc.core.setup.WorldManager;
import net.nuggetmc.core.util.ActionBar;
import net.nuggetmc.core.util.ColorCodes;
import net.nuggetmc.core.util.TimeConverter;

public class ItemEffects {

	private static List<String> worldlist;
	private static Set<Arrow> explosiveArrows;
	private static Set<Arrow> switchArrows;
	private static Set<Arrow> lightningArrows;
	public static Map<TNTPrimed, Player> boomBox;
	private static Map<Player, Integer> rune;
	private static Map<Player, Long> heal;
	private static Map<Player, Long> fire;
	private static Map<Player, Long> boom;
	private static Map<Player, Long> pearl;
	private static Map<String, Long> gapple;

	public static Main plugin;

	public ItemEffects(Main plugin) {
		ItemEffects.plugin = plugin;
		ItemEffects.worldlist = Configs.worldsettings.getConfig().getStringList("uhckit-worlds");
		ItemEffects.explosiveArrows = new HashSet<>();
		ItemEffects.switchArrows = new HashSet<>();
		ItemEffects.lightningArrows = new HashSet<>();
		ItemEffects.boomBox = new HashMap<>();
		ItemEffects.rune = new HashMap<>();
		ItemEffects.heal = new HashMap<>();
		ItemEffects.fire = new HashMap<>();
		ItemEffects.boom = new HashMap<>();
		ItemEffects.pearl = new HashMap<>();
		ItemEffects.gapple = new HashMap<>();
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
											boomBox.put(tnt, player);
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
		if (event.getEntity() instanceof Player) {
			Player victim = (Player) event.getEntity();
			if (event.getDamager() instanceof Player) {
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
						
						ItemStack item = player.getItemInHand();
						if (item != null) {
							if (item.hasItemMeta()) {
								ItemMeta meta = item.getItemMeta();
								if (meta.hasDisplayName()) {
									String name = meta.getDisplayName();
									
									switch(name) {
									case "§bHorseman Axe":
										Entity vehicle = player.getVehicle();
										if (vehicle != null) {
											if (vehicle.getType() == EntityType.HORSE) {
												event.setDamage(event.getDamage() * 2);
												Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
													Location loc = victim.getLocation().add(0, 1, 0);
													
													float x = (float) loc.getX();
													float y = (float) loc.getY();
													float z = (float) loc.getZ();
													
													PacketPlayOutWorldParticles lavaParticles = new PacketPlayOutWorldParticles(EnumParticle.LAVA,
															true, x, y, z, (float) 0.3, (float) 0.3, (float) 0.3, (float) 0, 10, null);
													
													for (Player all : Bukkit.getOnlinePlayers()) {
														((CraftPlayer) all).getHandle().playerConnection.sendPacket(lavaParticles);
													}
												});
											}
										}
										break;
										
									case "§bJouster Axe":
										Entity vehicle1 = player.getVehicle();
										if (vehicle1 != null) {
											if (vehicle1.getType() == EntityType.HORSE) {
												event.setDamage(event.getDamage() * 2);
												Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
													Location loc = victim.getLocation().add(0, 1, 0);
													
													float x = (float) loc.getX();
													float y = (float) loc.getY();
													float z = (float) loc.getZ();
													
													PacketPlayOutWorldParticles lavaParticles = new PacketPlayOutWorldParticles(EnumParticle.LAVA,
															true, x, y, z, (float) 0.3, (float) 0.3, (float) 0.3, (float) 0, 10, null);
													
													for (Player all : Bukkit.getOnlinePlayers()) {
														((CraftPlayer) all).getHandle().playerConnection.sendPacket(lavaParticles);
													}
												});
											}
										}
										break;
										
									case "§4The Scythe":
										EntityPlayer entity = (EntityPlayer) ((CraftPlayer) victim).getHandle();
										float abs = (float) victim.getHealth() + entity.getAbsorptionHearts();
										if (abs <= 6) {
											event.setDamage(1000);
										}
										Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> {
											if (item.getAmount() == 1) {
												player.setItemInHand(null);
											} else {
												item.setAmount(item.getAmount() - 1);
											}
										}, 1);
										break;
										
									case "§eSword of JUSTICE":
										victim.getWorld().strikeLightning(victim.getLocation());
										break;
										
									case "§r§9§kX§r §9The §9Spoon §kX§f":
										int rando = (int) (Math.random() * 4);

										if (rando == 2) {
											victim.getWorld().strikeLightningEffect(victim.getLocation());
											event.setDamage(event.getDamage() * 2);
										}
										break;
										
									case "§8Witherman §8Sword§f":
										Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(),
												"effect " + victim.getName() + " wither 3");
										break;
										
									case "§4Venomshank":
										Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(),
												"effect " + victim.getName() + " poison 3");
										break;
										
									case "§3Runeblade":
										Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
											int rando0 = (int) (Math.random() * 3);
											if (rando0 == 0) {
												for (Player all : Bukkit.getOnlinePlayers()) {
													all.playSound(victim.getLocation(), Sound.BLAZE_BREATH, (float) 0.4, 2);
												}
												
												Location loc = player.getLocation().add(0, 1, 0);
												float x = (float) loc.getX();
												float y = (float) loc.getY();
												float z = (float) loc.getZ();
												
												PacketPlayOutWorldParticles packet = new PacketPlayOutWorldParticles(EnumParticle.HEART, true, x, y, z, (float) 0.5, (float) 0.5, (float) 0.5, (float) 0.15, 2, null);
												for (Player all : Bukkit.getOnlinePlayers()) {
													((CraftPlayer) all).getHandle().playerConnection.sendPacket(packet);
												}
												if (player.getHealth() < 39)
													player.setHealth(player.getHealth() + 1);
												else
													player.setHealth(40);
											}
										});
											
										if (rune.containsKey(player)) {
											if (rune.get(player) >= 30) {
												for (Player all : Bukkit.getOnlinePlayers()) {
													all.playSound(victim.getLocation(), Sound.ZOMBIE_WOODBREAK, 1, 1);
												}
												event.setDamage(DamageModifier.ARMOR, 0);
												rune.remove(player);
											}
											else {
												rune.put(player, rune.get(player) + 1);
												if (rune.get(player) == 30) {
													ActionBar ab = new ActionBar("Hits: §a(" + rune.get(player) + "/30)");
													ab.send(player);
												}
												
												else {
													ActionBar ab = new ActionBar("Hits: (§a" + rune.get(player) + "§r/30)");
													ab.send(player);
												}
											}
										}
										else {
											rune.put(player, 1);
											ActionBar ab = new ActionBar("Hits: (§a" + rune.get(player) + "§r/30)");
											ab.send(player);
										}
										return;
										
									case "§5The §5Infinity §5Gauntlet§f":
										int rando1 = (int) (Math.random() * 50 + 1);

										if (rando1 == 29) {
											double health = victim.getHealth() / 2;
											victim.setHealth(health);
											
											for (Player all : Bukkit.getOnlinePlayers()) {
												all.playSound(victim.getLocation(), Sound.ZOMBIE_WOODBREAK, 1, 1);
											}
										}
										break;
										
									case "§bValkyrie §bSword§f":
										int rando2 = (int) (Math.random() * 20);

										if (rando2 == 7) {
											victim.getWorld().strikeLightningEffect(victim.getLocation());
											event.setDamage(event.getDamage() * 2);
										}
										break;
									}
									
									if (name.equals("§0Omega")) {
										victim.setMaximumNoDamageTicks(1);
										victim.setNoDamageTicks(1);
										return;
									}
								}
							}
						}
						rune.remove(player);
						if (victim.getMaximumNoDamageTicks() == 1) {
							victim.setMaximumNoDamageTicks(20);
							victim.setNoDamageTicks(20);
						}
					}
				}
				return;
			}
			
			else if (event.getDamager() instanceof Arrow) {
				Arrow arrow = (Arrow) event.getDamager();
				if (switchArrows.contains(arrow)) {
					if (arrow.getShooter() instanceof Player) {
						Player player = (Player) arrow.getShooter();
						
						Location playerLoc = player.getLocation();
						Location victimLoc = victim.getLocation();
						
						if (WorldManager.isInSpawn(playerLoc)) return;
						if (WorldManager.isInSpawn(victimLoc)) return;
						
						player.teleport(victimLoc);
						victim.teleport(playerLoc);
						
						float x1 = (float) playerLoc.getX();
						float y1 = (float) (playerLoc.getY() + 1);
						float z1 = (float) playerLoc.getZ();
						
						float x2 = (float) victimLoc.getX();
						float y2 = (float) (victimLoc.getY() + 1);
						float z2 = (float) victimLoc.getZ();
						
						PacketPlayOutWorldParticles packet1 = new PacketPlayOutWorldParticles(EnumParticle.SPELL_WITCH, true, x1, y1, z1, (float) 0.5, (float) 0.5, (float) 0.5, (float) 0.15, 40, null);
						PacketPlayOutWorldParticles packet2 = new PacketPlayOutWorldParticles(EnumParticle.SPELL_WITCH, true, x2, y2, z2, (float) 0.5, (float) 0.5, (float) 0.5, (float) 0.15, 40, null);
						
						for (Player all : Bukkit.getOnlinePlayers()) {
							all.playSound(playerLoc, Sound.ENDERMAN_TELEPORT, 1, 1);
							all.playSound(victimLoc, Sound.ENDERMAN_TELEPORT, 1, 1);
							((CraftPlayer) all).getHandle().playerConnection.sendPacket(packet1);
							((CraftPlayer) all).getHandle().playerConnection.sendPacket(packet2);
						}
					}
				}
				return;
			}
			
			else if(event.getDamager() instanceof TNTPrimed) {
	            TNTPrimed tnt = (TNTPrimed) event.getDamager();
	            if(boomBox.containsKey(tnt)) {
	                if (boomBox.get(tnt) != victim) {
	                	CombatTracker.combatCount(victim, 15);
	                }
	                Bukkit.getScheduler().runTaskLaterAsynchronously(plugin, () -> {
	                	if(boomBox.containsKey(tnt)) {
	                		boomBox.remove(tnt);
	                	}
	                }, 1);
	            }
	        }
		}
		return;
	}
	
	public static void durability(PlayerItemDamageEvent event) {
		ItemStack item = event.getItem();
		if (item != null) {
			if (item.hasItemMeta()) {
				ItemMeta meta = item.getItemMeta();
				if (meta.hasDisplayName()) {
					String name = meta.getDisplayName();
					if (name.equals("§0Omega")) {
						int damage = event.getDamage();
						if ((131 - item.getDurability()) > 5) {
							if (damage > 0) {
								int random = (int) (Math.random() * 10);
								if (random != 4) {
									event.setDamage(0);
								}
							}
						}
						return;
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
									actionBar.send(healer);
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
											actionBar.send(healer);
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
									actionBar.send(player);
									
									ActionBar healConf = new ActionBar("You have healed " + ColorCodes.colorName(player.getUniqueId(), player.getName()) + "§r!");
									healConf.send(healer);
									
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
					switch(name) {
					case "§9Gaster §9Blaster§f":
						explosiveArrows.add((Arrow) event.getProjectile());
						break;
					case "§dSwitchbow":
						switchArrows.add((Arrow) event.getProjectile());
						break;
					case "§1Zeus":
						lightningArrows.add((Arrow) event.getProjectile());
						break;
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
			return;
		}
		else if (lightningArrows.contains(projectile)) {
			if (worldlist.contains(projectile.getWorld().getName())) {
				if (!WorldManager.isInSpawn(loc)) {
					projectile.getWorld().strikeLightning(loc);
				}
			}
			lightningArrows.remove(projectile);
			return;
		}
		return;
	}
	
	@SuppressWarnings({"deprecation", "incomplete-switch"})
	public static void eat(PlayerItemConsumeEvent event) {
		Player player = event.getPlayer();
		String playername = player.getName();
		ItemStack item = event.getItem();
		
		switch (item.getType()) {
		case GOLDEN_APPLE:
			if (item.getData().getData() == 1) {
				if (gapple.containsKey(playername)) {
					Long difference = gapple.get(playername) - System.currentTimeMillis() / 1000;
					if (difference > 0) {
						String msg = TimeConverter.intToString(difference.intValue());
						if (msg.startsWith(" ")) {
							msg = msg.substring(1);
						}
						ActionBar actionBar = new ActionBar("You are on a cooldown! (§e" + msg + "§r)");
						actionBar.send(player);
						event.setCancelled(true);
						return;
					}
					
					else {
						gapple.remove(playername);
					}
				}
				
				gapple.put(playername, (System.currentTimeMillis() / 1000) + 720);
			}
			break;
			
		case POTION:
			Potion potion = Potion.fromItemStack(item);
			for (PotionEffect effect : potion.getEffects()) {
				if (effect.getType().equals(PotionEffectType.INCREASE_DAMAGE)) {
					if (worldlist.contains(player.getWorld().getName())) {
						event.setCancelled(true);
						player.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 240, 0));
						if (item.getAmount() == 1) {
							player.setItemInHand(new ItemStack(Material.GLASS_BOTTLE));
						} else {
							item.setAmount(item.getAmount() - 1);
						}
						break;
					}
				}
			}
			break;
		}
		return;
	}
	
	public static void splashPotion(PotionSplashEvent event) {
		ThrownPotion potion = event.getPotion();
		for (Entity entity : event.getAffectedEntities()) {
			if (entity instanceof Player) {
				Player player = (Player) entity;
				for (PotionEffect effect : potion.getEffects()) {
					if (effect.getType().equals(PotionEffectType.INCREASE_DAMAGE)) {
						if (worldlist.contains(player.getWorld().getName())) {
							event.setCancelled(true);
							player.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 240, 0));
							break;
						}
					}
				}
			}
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
							switch(name) {
							case "§bSamurai §bSword§f":
								Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(),
										"effect " + player.getName() + " resistance 1");
								
								Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
									for (Player all : Bukkit.getOnlinePlayers()) {
										all.playSound(player.getLocation(), Sound.ANVIL_LAND, 1, (float) 1.7);
									}
								});
								break;
								
							case "§cFlameweaver":
								Location loc = player.getLocation().add(0, 1, 0);
								
								if (fire.containsKey(player)) {
									Long difference = fire.get(player) - System.currentTimeMillis() / 1000;
									if (difference > 0) {
										ActionBar actionBar = new ActionBar("You are on a cooldown! (§e" + difference + "s§r)");
										actionBar.send(player);
										return;
									}
									
									else {
										fire.remove(player);
									}
								}
										
								Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
									float x = (float) loc.getX();
									float y = (float) loc.getY();
									float z = (float) loc.getZ();
									
									PacketPlayOutWorldParticles lavaParticles = new PacketPlayOutWorldParticles(EnumParticle.LAVA,
											true, x, y, z, (float) 3, (float) 0.1, (float) 3, (float) 10, 50, null);
									
									PacketPlayOutWorldParticles lavaParticles2 = new PacketPlayOutWorldParticles(EnumParticle.LAVA,
											true, x, y, z, (float) 0.3, (float) 0.3, (float) 0.3, (float) 0, 10, null);
									
									for (Player all : Bukkit.getOnlinePlayers()) {
										((CraftPlayer) all).getHandle().playerConnection.sendPacket(lavaParticles);
										((CraftPlayer) all).getHandle().playerConnection.sendPacket(lavaParticles2);
										all.playSound(loc, Sound.GHAST_FIREBALL, 1, 1);
										if (all != player) {
											if (!WorldManager.isInSpawn(all.getLocation())) {
												if (all.getWorld() == player.getWorld()) {
													if (all.getLocation().distance(loc) < 16) {
														all.setFireTicks(300);
													}
												}
											}
										}
									}
								});
								
								fire.put(player, (System.currentTimeMillis() / 1000) + 10);
								break;
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
											if (!WorldManager.isInArena(victim.getLocation())) {
												near = victim;
											}
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
								
								Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
									float x = (float) player.getLocation().getX();
									float y = (float) (player.getLocation().getY() + 1);
									float z = (float) player.getLocation().getZ();
									
									Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "playsound mob.endermen.portal @a " + x + " " + y + " " + z);
									
									PacketPlayOutWorldParticles packet = new PacketPlayOutWorldParticles(EnumParticle.SPELL_WITCH, true, x, y, z, (float) 0.5, (float) 0.5, (float) 0.5, (float) 0.15, 40, null);
									for (Player all : Bukkit.getOnlinePlayers()) ((CraftPlayer) all).getHandle().playerConnection.sendPacket(packet);
								});
							}
							
							else {
								ActionBar actionBar = new ActionBar("No players within §e10 §rmeters!");
								actionBar.send(player);
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
									actionBar.send(player);
									return;
								}
								
								else {
									boom.remove(player);
								}
							}
							
							Location loc = player.getLocation();
							
							if (WorldManager.isInSpawn(loc)) {
								ActionBar ab = new ActionBar("You cannot use this here!");
								ab.send(player);
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
			
			else {
				if (item.getType() == Material.ENDER_PEARL) {
					if (event.getAction() == Action.RIGHT_CLICK_BLOCK || event.getAction() == Action.RIGHT_CLICK_AIR) {
						if (pearl.containsKey(player)) {
							Long difference = pearl.get(player) - System.currentTimeMillis() / 1000;
							if (difference > 0) {
								ActionBar actionBar = new ActionBar("You are on a cooldown! (§e" + difference + "s§r)");
								actionBar.send(player);
								event.setCancelled(true);
								player.updateInventory();
								return;
							}
							
							else {
								pearl.remove(player);
							}
						}
						
						pearl.put(player, (System.currentTimeMillis() / 1000) + 15);
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
								
								switch (name) {
								case "§rSpawn Horse §7(Horseman)":
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
									break;
									
								case "§rSpawn Horse §7(Jouster)":
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
									break;
									
								case "§rSpawn Horse §7(Areion)":
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
									break;
									
								case "§rSpawn Horse §7(Tulpar)":
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
									break;
									
								case "§rSpawn Horse §7(Sleipnir)":
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
									break;
									
								case "§rSpawn Horse §7(Phaethon)":
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
									break;
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