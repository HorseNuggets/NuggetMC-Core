package net.nuggetmc.core;

import java.util.List;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import net.nuggetmc.core.commands.admin.admincmd;
import net.nuggetmc.core.commands.admin.ban;
import net.nuggetmc.core.commands.admin.debug;
import net.nuggetmc.core.commands.admin.ghead;
import net.nuggetmc.core.commands.admin.head;
import net.nuggetmc.core.commands.admin.invconvert;
import net.nuggetmc.core.commands.admin.nmc;
import net.nuggetmc.core.commands.admin.rank;
import net.nuggetmc.core.commands.def.defaultcmd;
import net.nuggetmc.core.data.Configs;
import net.nuggetmc.core.misc.TabComplete;
import net.nuggetmc.core.modifiers.HealthBoost;
import net.nuggetmc.core.modifiers.PlayerTracker;
import net.nuggetmc.core.modifiers.autorespawn.AutoRespawn;
import net.nuggetmc.core.modifiers.gheads.GHeads;
import net.nuggetmc.core.modifiers.nofall.NoFall;
import net.nuggetmc.core.modifiers.nofall.listeners.FallListener;
import net.nuggetmc.core.modifiers.nofall.listeners.MoveListener;
import net.nuggetmc.core.player.Levelup;
import net.nuggetmc.core.player.PlayerChat;
import net.nuggetmc.core.player.PlayerJoin;
import net.nuggetmc.core.player.PlayerKill;
import net.nuggetmc.core.player.PlayerSpawnLocation;
import net.nuggetmc.core.player.PlayerStats;
import net.nuggetmc.core.protocol.PacketHandler;
import net.nuggetmc.core.scoreboard.Sidebar;
import net.nuggetmc.core.setup.Announcements;
import net.nuggetmc.core.setup.WorldManager;
import net.nuggetmc.core.util.ItemSerializers;

public class Main extends JavaPlugin implements TabCompleter {
	
	/**
	 * NuggetMC-Core
	 * Version: 2.0
	 * The core plugin for the NuggetMC Network [nuggetmc.net].
	 * 
	 * @author HorseNuggets
	 * @since 2/22/2017
	 * 
	 * [DISCORD] @HorseNuggets#3141
	 * [PHONE] +1 (346) 233-7975
	 * [EMAIL] batchprogrammer314@gmail.com
	 */
	
	private Logger logger;
	
	public HealthBoost healthboost;
	public Announcements announcements;
	public AutoRespawn autoRespawn;
	public Configs configs;
	public FallListener fallListener;
	public GHeads gheads;
	public ItemSerializers itemSerializers;
	public Levelup levelup;
	public MoveListener moveListener;
	public NoFall noFall;
	public PacketHandler packetHandler;
	public PlayerChat playerChat;
	public PlayerJoin playerJoin;
	public PlayerKill playerKill;
	public PlayerSpawnLocation playerSpawnLocation;
	public PlayerStats playerStats;
	public PlayerTracker playerTracker;
	public Sidebar sidebar;
	public TabComplete tabComplete;
	public WorldManager worldManager;
	
	public void onEnable() {
		this.loadConfigs();
		this.worldsEnable();
		this.announcementsEnable();
		this.commandsEnable();
		this.listenersEnable();
		this.loggerEnable();
		this.modifiersEnable();
		this.packetHandlerEnable();
		this.playerEventsEnable();
		this.sidebarEnable();
		this.tabCompleteEnable();
		this.toolsEnable();
		this.utilsEnable();
		this.refreshSidebars();
		return;
	}
	
	private void announcementsEnable() {
		this.announcements = new Announcements(this);
		this.announcements.setup();
		this.announcements.run();
		return;
	}
	
	private void commandsEnable() {
		this.getCommand("ban").setExecutor(new ban(this));
		this.getCommand("debug").setExecutor(new debug(this));
		this.getCommand("ghead").setExecutor(new ghead());
		this.getCommand("gma").setExecutor(new admincmd(this));
		this.getCommand("gmc").setExecutor(new admincmd(this));
		this.getCommand("gms").setExecutor(new admincmd(this));
		this.getCommand("gmsp").setExecutor(new admincmd(this));
		this.getCommand("head").setExecutor(new head());
		this.getCommand("invconvert").setExecutor(new invconvert(this));
		this.getCommand("nuggetmc").setExecutor(new nmc(this));
		this.getCommand("rank").setExecutor(new rank(this));
		this.getCommand("spawn").setExecutor(new defaultcmd(this));
		this.getCommand("tpall").setExecutor(new admincmd(this));
		return;
	}
	
	private void tabCompleteEnable() {
		this.tabComplete = new TabComplete(this);
		this.getCommand("nuggetmc").setTabCompleter(this);
		this.getCommand("rank").setTabCompleter(this);
		return;
	}
	
	private void listenersEnable() {
		Bukkit.getServer().getPluginManager().registerEvents(new Listeners(this), this);
		return;
	}
	
	private void loggerEnable() {
		this.logger = getLogger();
		return;
	}
	
	private void modifiersEnable() {
		this.autoRespawn = new AutoRespawn(this);
		this.fallListener = new FallListener(this);
		this.healthboost = new HealthBoost(this);
		this.moveListener = new MoveListener(this);
		this.noFall = new NoFall(this);
		
		if (Configs.mainconfig.getConfig().getBoolean("enabled.heads-gheads")) {
			this.gheads = new GHeads(this);
		}
	}
	
	private void packetHandlerEnable() {
		this.packetHandler = new PacketHandler();
		return;
	}
	
	private void playerEventsEnable() {
		this.levelup = new Levelup();
		this.playerChat = new PlayerChat(this);
		this.playerJoin = new PlayerJoin();
		this.playerKill = new PlayerKill(this);
		this.playerSpawnLocation = new PlayerSpawnLocation(this);
		this.playerStats = new PlayerStats(this);
	}
	
	private void refreshSidebars() {
		Bukkit.getScheduler().scheduleSyncDelayedTask(this, () -> {
			for (Player player : Bukkit.getOnlinePlayers()) {
				this.sidebar.enable(player);
			}
		}, 10);
		return;
	}
	
	private void sidebarEnable() {
		this.sidebar = new Sidebar(this);
		return;
	}
	
	private void utilsEnable() {
		this.itemSerializers = new ItemSerializers();
		return;
	}
	
	public void log(String message) {
		this.logger.info(message);
		return;
	}
	
	public void loadConfigs() {
		this.configs = new Configs(this);
		return;
	}
	
	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
		return this.tabComplete.onTabComplete(sender, command, label, args);
	}
	
	public void reloadConfigs() {
		this.configs = new Configs(this);
		this.toolsEnable();
		return;
	}
	
	public void toolsEnable() {
		if (Configs.mainconfig.getConfig().getBoolean("enabled.playertracker")) {
			if (this.playerTracker == null) {
				this.playerTracker = new PlayerTracker(this);
			}
		}
		else {
			this.playerTracker = null;
		}
		return;
	}
	
	public void worldsEnable() {
		this.worldManager = new WorldManager(this);
		this.worldManager.loadAllWorlds();
		return;
	}
}
