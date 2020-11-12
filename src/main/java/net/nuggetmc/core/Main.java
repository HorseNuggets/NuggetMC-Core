package net.nuggetmc.core;

import java.util.List;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import net.nuggetmc.core.commands.admin.AdminCommand;
import net.nuggetmc.core.commands.admin.DebugCommand;
import net.nuggetmc.core.commands.admin.GHeadCommand;
import net.nuggetmc.core.commands.admin.HeadCommand;
import net.nuggetmc.core.commands.admin.InvConvertCommand;
import net.nuggetmc.core.commands.admin.NMCMainCommand;
import net.nuggetmc.core.commands.admin.RankCommand;
import net.nuggetmc.core.commands.def.DefaultCommand;
import net.nuggetmc.core.commands.def.HelpCommand;
import net.nuggetmc.core.commands.def.HomeCommand;
import net.nuggetmc.core.commands.def.KitCommand;
import net.nuggetmc.core.commands.def.MSGCommand;
import net.nuggetmc.core.commands.def.TPACommand;
import net.nuggetmc.core.commands.mod.BanCommand;
import net.nuggetmc.core.commands.mod.MuteCommand;
import net.nuggetmc.core.data.Configs;
import net.nuggetmc.core.data.Leaderboards;
import net.nuggetmc.core.economy.ItemShop;
import net.nuggetmc.core.economy.Kits;
import net.nuggetmc.core.events.Event;
import net.nuggetmc.core.events.FFADeathmatch;
import net.nuggetmc.core.events.Join;
import net.nuggetmc.core.events.Tournament;
import net.nuggetmc.core.gui.EnderChest;
import net.nuggetmc.core.gui.GUIMain;
import net.nuggetmc.core.gui.Voting;
import net.nuggetmc.core.misc.Credits;
import net.nuggetmc.core.misc.Flags;
import net.nuggetmc.core.misc.FlyVanish;
import net.nuggetmc.core.misc.ItemEffects;
import net.nuggetmc.core.misc.TabComplete;
import net.nuggetmc.core.modifiers.CombatTracker;
import net.nuggetmc.core.modifiers.HealthBoost;
import net.nuggetmc.core.modifiers.PlayerTracker;
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
	public AdminCommand adminCommand;
	public Announcements announcements;
	public BanCommand banCommand;
	public CombatTracker combatTracker;
	public Configs configs;
	public Credits credits;
	public DefaultCommand defaultCommand;
	public EnderChest enderChest;
	public Event events;
	public FallListener fallListener;
	public FFADeathmatch dm;
	public FlyVanish flyVanish;
	public GHeads gheads;
	public GUIMain guiMain;
	public HomeCommand homeCommand;
	public ItemEffects itemEffects;
	public ItemSerializers itemSerializers;
	public ItemShop itemShop;
	public Kits kits;
	public Levelup levelup;
	public MoveListener moveListener;
	public MSGCommand msgCommand;
	public MuteCommand muteCommand;
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
	public Tournament trn;
	public TPACommand tpaCommand;
	public WorldManager worldManager;
	
	public void onEnable() {
		this.loadConfigs();
		this.worldsEnable();
		this.announcementsEnable();
		this.economyEnable();
		this.eventsEnable();
		this.guiEnable();
		this.itemEffectsEnable();
		this.listenersEnable();
		this.loggerEnable();
		this.modifiersEnable();
		this.playerEventsEnable();
		this.protocolEnable();
		this.sidebarEnable();
		this.tabCompleteEnable();
		this.toolsEnable();
		this.utilsEnable();
		this.commandsEnable();
		this.taskTimer();
		return;
	}
	
	private void announcementsEnable() {
		this.announcements = new Announcements(this);
		this.announcements.setup();
		this.announcements.run();
		return;
	}
	
	private void commandsEnable() {
		this.tpaCommand = new TPACommand(this);
		getCommand("tpa").setExecutor(tpaCommand);
		getCommand("tphere").setExecutor(tpaCommand);
		getCommand("tpyes").setExecutor(tpaCommand);
		
		this.adminCommand = new AdminCommand(this);
		getCommand("alert").setExecutor(adminCommand);
		getCommand("gma").setExecutor(adminCommand);
		getCommand("gmc").setExecutor(adminCommand);
		getCommand("gms").setExecutor(adminCommand);
		getCommand("gmsp").setExecutor(adminCommand);
		getCommand("justice").setExecutor(adminCommand);
		getCommand("setkills").setExecutor(adminCommand);
		getCommand("setnuggets").setExecutor(adminCommand);
		getCommand("tpall").setExecutor(adminCommand);
		getCommand("wr").setExecutor(adminCommand);
		getCommand("wrset").setExecutor(adminCommand);
		
		this.homeCommand = new HomeCommand();
		getCommand("delhome").setExecutor(homeCommand);
		getCommand("home").setExecutor(homeCommand);
		getCommand("homes").setExecutor(homeCommand);
		getCommand("sethome").setExecutor(homeCommand);
		
		this.defaultCommand = new DefaultCommand(this);
		getCommand("blocks").setExecutor(defaultCommand);
		getCommand("boat").setExecutor(defaultCommand);
		getCommand("discord").setExecutor(defaultCommand);
		getCommand("lead").setExecutor(defaultCommand);
		getCommand("levels").setExecutor(defaultCommand);
		getCommand("rules").setExecutor(defaultCommand);
		getCommand("shop").setExecutor(defaultCommand);
		getCommand("spawn").setExecutor(defaultCommand);
		getCommand("stats").setExecutor(defaultCommand);
		getCommand("suicide").setExecutor(defaultCommand);
		getCommand("warp").setExecutor(defaultCommand);
		getCommand("wrt").setExecutor(defaultCommand);
		
		this.muteCommand = new MuteCommand(this);
		getCommand("mute").setExecutor(muteCommand);
		getCommand("unmute").setExecutor(muteCommand);
		getCommand("mutelist").setExecutor(muteCommand);
		
		this.msgCommand = new MSGCommand(this);
		getCommand("msg").setExecutor(msgCommand);
		getCommand("r").setExecutor(msgCommand);
		getCommand("ignore").setExecutor(msgCommand);
		getCommand("unignore").setExecutor(msgCommand);
		getCommand("ignorelist").setExecutor(msgCommand);
		
		this.banCommand = new BanCommand(this);
		getCommand("ban").setExecutor(banCommand);
		getCommand("banlist").setExecutor(banCommand);
		getCommand("ban-ip").setExecutor(banCommand);
		getCommand("pardon-ip").setExecutor(banCommand);
		getCommand("ipbanlist").setExecutor(banCommand);
		
		this.credits = new Credits(this);
		getCommand("credit").setExecutor(credits);
		getCommand("promo").setExecutor(credits);
		
		this.events = new Event(this);
		getCommand("event").setExecutor(events);
		
		this.dm = new FFADeathmatch(this);
		getCommand("ar").setExecutor(dm);
		
		new Leaderboards(this);
		
		this.getCommand("flag").setExecutor(new Flags(this));
		
		this.getCommand("ushop").setExecutor(itemShop);
		this.getCommand("tournament").setExecutor(trn);
		
		this.getCommand("debug").setExecutor(new DebugCommand(this));
		this.getCommand("join").setExecutor(new Join());
		this.getCommand("ghead").setExecutor(new GHeadCommand());
		this.getCommand("head").setExecutor(new HeadCommand());
		this.getCommand("help").setExecutor(new HelpCommand());
		this.getCommand("invconvert").setExecutor(new InvConvertCommand(this));
		this.getCommand("nuggetmc").setExecutor(new NMCMainCommand(this));
		this.getCommand("rank").setExecutor(new RankCommand(this));
		this.getCommand("vanish").setExecutor(new FlyVanish(this));
		this.getCommand("vote").setExecutor(new Voting());
		this.getCommand("kit").setExecutor(new KitCommand(this));
		return;
	}
	
	private void economyEnable() {
		this.kits = new Kits(this);
		this.itemShop = new ItemShop(this);
		return;
	}
	
	private void eventsEnable() {
		this.trn = new Tournament(this);
		return;
	}
	
	private void guiEnable() {
		this.guiMain = new GUIMain(this);
		return;
	}
	
	private void tabCompleteEnable() {
		this.tabComplete = new TabComplete(this);
		this.getCommand("kit").setTabCompleter(this);
		this.getCommand("lead").setTabCompleter(this);
		this.getCommand("nuggetmc").setTabCompleter(this);
		this.getCommand("rank").setTabCompleter(this);
		this.getCommand("unignore").setTabCompleter(this);
		this.getCommand("pardon-ip").setTabCompleter(this);
		return;
	}
	
	private void itemEffectsEnable() {
		this.itemEffects = new ItemEffects(this);
		return;
	}
	
	private void listenersEnable() {
		Bukkit.getPluginManager().registerEvents(new Listeners(this), this);
		return;
	}
	
	private void loggerEnable() {
		this.logger = getLogger();
		return;
	}
	
	private void modifiersEnable() {
		this.enderChest = new EnderChest();
		this.fallListener = new FallListener(this);
		this.healthboost = new HealthBoost(this);
		this.moveListener = new MoveListener(this);
		this.noFall = new NoFall(this);
		this.combatTracker = new CombatTracker(this);
		
		if (Configs.mainconfig.getConfig().getBoolean("enabled.heads-gheads")) {
			this.gheads = new GHeads(this);
		}
	}
	
	private void playerEventsEnable() {
		this.levelup = new Levelup();
		this.playerChat = new PlayerChat(this);
		this.playerJoin = new PlayerJoin();
		this.playerKill = new PlayerKill(this);
		this.playerSpawnLocation = new PlayerSpawnLocation(this);
		this.playerStats = new PlayerStats(this);
	}
	
	private void protocolEnable() {
		//this.packetHandler = new PacketHandler();
		return;
	}
	
	private void sidebarEnable() {
		this.sidebar = new Sidebar(this);
		
		for (Player all : Bukkit.getOnlinePlayers()) {
			Sidebar.enable(all, (byte) 0);
		}
		return;
	}
	
	private void utilsEnable() {
		this.itemSerializers = new ItemSerializers();
		return;
	}
	
	private void taskTimer() {
		this.worldManager.worldReloadTimer();
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
