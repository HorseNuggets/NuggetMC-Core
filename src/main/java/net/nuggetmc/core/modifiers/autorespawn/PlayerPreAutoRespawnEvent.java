package net.nuggetmc.core.modifiers.autorespawn;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityDamageEvent;

public class PlayerPreAutoRespawnEvent extends Event implements Cancellable {
	
	private Player player;

	private Location deathLoc;

	private boolean cancelled = false;

	private static final HandlerList handlers = new HandlerList();

	public PlayerPreAutoRespawnEvent(Player player, Location deathLoc) {
		this.player = player;
		this.deathLoc = deathLoc;
	}

	public Player getPlayer() {
		return this.player;
	}

	public Location getDeathLocation() {
		return this.deathLoc;
	}

	public EntityDamageEvent.DamageCause getDeathCause() {
		return this.player.getLastDamageCause().getCause();
	}

	public boolean killedByPlayer() {
		if (this.player.getLastDamageCause().getEntity() instanceof Player)
			return true;
		if (this.player.getLastDamageCause().getEntity() instanceof Projectile)
			return ((Projectile) this.player.getLastDamageCause().getEntity()).getShooter() instanceof Player;
		return false;
	}

	public Player getKiller() {
		return this.player.getKiller();
	}

	public HandlerList getHandlers() {
		return handlers;
	}

	public static HandlerList getHandlerList() {
		return handlers;
	}

	public boolean isCancelled() {
		return this.cancelled;
	}

	public void setCancelled(boolean arg0) {
		this.cancelled = arg0;
	}
}