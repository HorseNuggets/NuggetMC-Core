package net.nuggetmc.core.modifiers;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.FishHook;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.scheduler.BukkitRunnable;

import net.nuggetmc.core.Main;
import net.nuggetmc.core.util.Checks;

public class CombatTracker {
	
	private Main plugin;
	private Map<Player, BukkitRunnable> combatTask;
	
	public static Map<Player, Integer> combatTime;
	
	public CombatTracker(Main plugin) {
		this.plugin = plugin;
		this.combatTask = new HashMap<>();
		
		CombatTracker.combatTime = new HashMap<>();
	}
	
	public void inCombatCommand(PlayerCommandPreprocessEvent event) {
    	String base = event.getMessage().split(" ")[0];
    	Player player = event.getPlayer();
    	
    	if (Checks.cmCheck1(base)) {
			if (!Checks.checkHighStaff(player)) {
				player.sendMessage(ChatColor.RED + "You do not have permission.");
				event.setCancelled(true);
	    		return;
	    	}
    	}
    	
    	if (combatTime.containsKey(player)) {
			if (Checks.cmCheck2(base)) {
				
				if (Checks.checkStaff(player)) {
					player.sendMessage(ChatColor.RED + "Your rank allows you to bypass this combat-tagged command.");
		    		return;
		    	}
				else {
					player.sendMessage(ChatColor.RED + "Command disabled during combat!");
					event.setCancelled(true);
					return;
				}
			}
    	}
    	
		return;
	}
	
	public void onQuit(PlayerQuitEvent event) {
		Player player = event.getPlayer();
		if (combatTime.containsKey(player)) combatTime.remove(player);
		if (combatTask.containsKey(player)) combatTask.remove(player);
		return;
	}
	
	public void onDeath(PlayerDeathEvent event) {
		Player player = event.getEntity();
		if (combatTime.containsKey(player)) combatTime.remove(player);
		if (combatTask.containsKey(player)) combatTask.remove(player);
		return;
	}
	
	public void onRespawn(PlayerRespawnEvent event) {
		plugin.sidebar.enable(event.getPlayer());
		return;
	}
    
    public void playerCombat(EntityDamageByEntityEvent event) {
    	
    	if (event.isCancelled() || event.getDamage() == 0.0D) return;
    	
    	else {
	    	if (!(event.getDamager() instanceof Player)) return;
			if (!(event.getEntity() instanceof Player)) return;
		    	
	    	Player attacker = (Player) event.getDamager();
	    	Player victim = (Player) event.getEntity();
	    	
	    	if (attacker != victim) {
		    	combatCount(attacker, 15);
		    	combatCount(victim, 15);
	    	}
    	}
    	return;
    }
    
    public void playerCombatProjectiles(EntityDamageByEntityEvent event) {  // eventually add snowballs + eggs
    	
    	if (event.getEntity() instanceof Player) {
	    	if (!event.isCancelled()) {
		    	Player victim = (Player) event.getEntity();
		    	
		    	if (event.getDamager() instanceof FishHook) {
		    		FishHook fishHook = (FishHook) event.getDamager();
		    		if (fishHook.getShooter() instanceof Player) {
		    			Player attacker = (Player) fishHook.getShooter();
		    			if (attacker != victim) {
			    			combatCount(attacker, 15);
			    	    	combatCount(victim, 15);
		    			}
		    		}
		    	}
		    	
		    	else {
		    		if (event.getDamage() != 0.0D) {
				    	if (event.getDamager() instanceof Arrow) {
				    		Arrow arrow = (Arrow) event.getDamager();
				    		if (arrow.getShooter() instanceof Player) {
				    			Player attacker = (Player) arrow.getShooter();
				    			if (attacker != victim) {
					    			combatCount(attacker, 15);
					    	    	combatCount(victim, 15);
				    			}
				    		}
				    	}
		    		}
		    	}
	    	}
    	}
    	return;
    }
    
    public void combatContinue(EntityDamageEvent event) {
		if (event.getEntity() instanceof Player) {
	    	Player player = (Player) event.getEntity();
	    	if (combatTime.containsKey(player)) {
		    	combatCount(player, 15);
	    	}
		}
    	
    	return;
    }
    
    public void combatCount(Player player, int countdown) {
    	
    	if (combatTime.containsKey(player)) {
    		combatTask.get(player).cancel();
		}
    	
    	BukkitRunnable combatRunnable = new BukkitRunnable() {
        	public void run() {
        		if (!combatTime.containsKey(player) || player == null) {
        			if (combatTime.containsKey(player)) combatTime.remove(player);
        			if (combatTask.containsKey(player)) combatTask.remove(player);
                    this.cancel();
                    return;
        		}
        		
        		plugin.sidebar.enable(player);
        		
                combatTime.put(player, combatTime.get(player) - 1);
                if (combatTime.get(player) <= 0) {
                	if (combatTime.containsKey(player)) combatTime.remove(player);
        			if (combatTask.containsKey(player)) combatTask.remove(player);
                    
                    if (player != null) {
                		plugin.sidebar.enable(player);
                		
                		Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                			plugin.sidebar.enable(player);
                		}, 20);
                	}
                    
                    this.cancel();
                    return;
                }
        	}
        };
    	
    	combatTime.put(player, countdown);
        combatTask.put(player, combatRunnable);
        combatTask.get(player).runTaskTimer(plugin, 0, 20);
    	return;
    }
}
