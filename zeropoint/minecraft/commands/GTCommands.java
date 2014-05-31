package zeropoint.minecraft.commands;


import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;

import net.minecraft.command.ICommand;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.NetLoginHandler;
import net.minecraft.network.packet.NetHandler;
import net.minecraft.network.packet.Packet1Login;
import net.minecraft.server.MinecraftServer;
import zeropoint.core.io.file.OutputFile;
import zeropoint.minecraft.commands.com.*;
import zeropoint.minecraft.core.Config;
import zeropoint.minecraft.core.GTBaseCommand;
import zeropoint.minecraft.core.GTCore;
import zeropoint.minecraft.core.util.ChatMsg;
import zeropoint.minecraft.core.util.Log;
import zeropoint.minecraft.core.util.WrittenBookTag;
import zeropoint.minecraft.sonic.GTSonic;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.network.IConnectionHandler;
import cpw.mods.fml.common.network.NetworkMod;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.Player;


@SuppressWarnings("javadoc")
@Mod(modid = GTCommands.modid, name = GTCommands.name, version = GTCommands.version, dependencies = "required-after:gtweaks-core")
@NetworkMod(clientSideRequired = true, serverSideRequired = false)
public final class GTCommands {
	public static final String modid = "gtweaks-commands";
	public static final String name = "GlobalTweaks|Commands";
	public static final String version = "release";
	protected static HashMap<String, GTBaseCommand> cmds = new HashMap<String, GTBaseCommand>();
	protected static GTBaseCommand[] cmdNames;
	private static Config cfg;
	private static final Logger LOG = Log.getLogger(name);
	@SuppressWarnings("unused")
	@EventHandler
	public static void preinit(FMLPreInitializationEvent event) {
		cfg = GTCore.getConfig();
	}
	@SuppressWarnings("unused")
	@EventHandler
	public static void load(FMLInitializationEvent event) {
		if ( !GTCore.Modules.commandsEnabled()) {
			LOG.warning("Module disabled!");
			return;
		}
		Tome.loadBlastSize = cfg.decimal("tomes", "loadExplosionSize", Tome.loadBlastSize, "How big is the explosion when loading a saved tome?");
		Tome.loadBlastGrief = cfg.bool("tomes", "loadExplosionGriefing", Tome.loadBlastGrief, "Should the blast from loading a tome damage blocks?");
		Tome.failBlastMultiplier = cfg.decimal("tomes", "failBlastMultiplier", Tome.failBlastMultiplier, "How big is the explosion when unable to convert a held item into a tome?");
		Tome.failBlastGrief = cfg.bool("tomes", "failExplosionGriefing", Tome.failBlastGrief, "Should the blast from failing to convert an item damage blocks?");
		if (Tome.loadBlastSize < 0.5) {
			Tome.loadBlastSize = 0.5;
		}
		if (Tome.failBlastMultiplier < 1.0) {
			Tome.failBlastMultiplier = 1.0;
		}
		cmdNames = new GTBaseCommand[] {
			// [/absorb] Set absorption hearts
			new Absorb(),
			// [/air] Refill oxygen
			new Air(),
			// [/anvil] Open an anvil
			new Anvil(),
			// [/book] Multipurpose book editing command
			new Book(),
			// [/clipboard] Manipulate the user's clipboard
			new Clipboard(),
			// [/craft] Open a 3x3 crafting grid on the go
			new Craft(),
			// [/efflist] List all effects to console
			new Efflist(),
			// [/enchlist] List all enchantments to console
			new Enchlist(),
			// [/extinguish] Remove fire
			new Extinguish(),
			// [/feed] Set hunger points
			new Feed(),
			// [/fly] Toggle flight ability
			new Fly(),
			// [/god] Toggle taking damage
			new God(),
			// [/halp] Get JCraft extended command help
			new Halp(),
			// [/heal] Add health
			new Heal(),
			// [/health] Set health
			new Health(),
			// [/repair] Repair hand/hotbar/inventory/armour/all
			new Repair(),
			// [/jcraft] General command
			new Gtweaks(),
			// [/tome] Save and load books on disk
			new Tome(),
			// [/uuid] Generate UUIDs
			new Uuid(),
			// [/vanish] Toggle invisibility
			new Vanish()
		};
		if (GTCore.inDev()) {
			LOG.info("Running in a development environment, registering all commands");
			for (GTBaseCommand com : cmdNames) {
				cfg.bool("commands", com.getCommandNamePlain(), com.getDefaultState(), com.getCommandHelp(null));
				register(com);
			}
		}
		else {
			for (GTBaseCommand com : cmdNames) {
				if (com.getCommandNamePlain().toLowerCase().equals("gtweak")) {
					register(com);
					continue;
				}
				if (cfg.bool("commands", com.getCommandNamePlain(), com.getDefaultState(), com.getCommandHelp(null))) {
					register(com);
					LOG.info("Registering" + (com.isFinished() ? "" : " unfinished") + " command: " + com.getCommandNameSlash());
				}
				else {
					LOG.info("Ignoring" + (com.isFinished() ? "" : " unfinished") + " command: " + com.getCommandNameSlash());
				}
			}
		}
		createInstructions("INSTRUCTIONS");
		createGuideCommands("HELP-COMMANDS");
		createGuideCraft("HELP-CRAFT");
		createGuideEnchant("HELP-ENCHANT");
		createGuideSonic("HELP-SONIC");
	}
	@SuppressWarnings("unused")
	@EventHandler
	public static void postinit(FMLPostInitializationEvent event) {
		NetworkRegistry.instance().registerConnectionHandler(new CommandsConnectionHandler());
		LOG.info("Registered connection handler");
	}
	@EventHandler
	public static void serverStart(FMLServerStartingEvent event) {
		if ( !cfg.bool("enable", "commands.register", true, "Register commands on world load? This affects other mods that use GT|Commands to add commands!")) {
			LOG.severe("Command registration disabled! This may cause problems!");
			return;
		}
		Iterator<GTBaseCommand> iter = cmds.values().iterator();
		while (iter.hasNext()) {
			ICommand cmd = iter.next();
			event.registerServerCommand(cmd);
		}
	}
	public static void register(GTBaseCommand cmd) {
		cmds.put(cmd.getCommandNamePlain(), cmd);
	}
	public static GTBaseCommand getCommand(String pname) {
		return cmds.containsKey(pname) ? cmds.get(pname) : null;
	}
	public static String getCommandHelp(String pname) {
		return cmds.containsKey(pname) ? cmds.get(pname).getCommandHelp(null) : "";
	}
	public static String[] getCommands() {
		List<String> tmp = new ArrayList<String>();
		for (GTBaseCommand com : getCommandArray()) {
			tmp.add(com.getCommandName().replaceAll("^/", ""));
		}
		return tmp.toArray(new String[] {});
	}
	public static GTBaseCommand[] getCommandArray() {
		return cmds.values().toArray(new GTBaseCommand[] {});
	}
	protected static class CommandsConnectionHandler implements IConnectionHandler {
		@Override
		public void playerLoggedIn(Player player, NetHandler netHandler, INetworkManager manager) {}
		@Override
		public String connectionReceived(NetLoginHandler netHandler, INetworkManager manager) {
			return "";
		}
		@Override
		public void connectionOpened(NetHandler netClientHandler, String server, int port, INetworkManager manager) {}
		@Override
		public void connectionOpened(NetHandler netClientHandler, MinecraftServer server, INetworkManager manager) {}
		@Override
		public void connectionClosed(INetworkManager manager) {}
		@Override
		@SuppressWarnings("synthetic-access")
		public void clientLoggedIn(NetHandler clientHandler, INetworkManager manager, Packet1Login login) {
			EntityPlayer player = clientHandler.getPlayer();
			if (player == null) {
				LOG.severe("CommandsConnectionHandler.clientLoggedIn(...): cannot get EntityPlayer!");
				return;
			}
			new ChatMsg(ChatMsg.SILVER + "You have " + ChatMsg.PURPLE + "GlobalTweaks|Commands" + ChatMsg.SILVER + " enabled!").send(player);
			new ChatMsg(ChatMsg.SILVER + "For a list of commands, use '" + ChatMsg.GOLD + "/gtweak list" + ChatMsg.SILVER + "'").send(player);
			new ChatMsg(ChatMsg.SILVER + "Alternatively, hold a book and use '" + ChatMsg.GOLD + "/tome load HELP-COMMANDS" + ChatMsg.SILVER + "'").send(player);
		}
	}
	protected static ItemStack bookFactory(String title, String author, String... pages) {
		return new WrittenBookTag(title, author).append(pages).create();
	}
	protected static ItemStack bookFactory(String title, String author, Collection<? extends String> pages) {
		return bookFactory(title, author, pages.toArray(new String[] {}));
	}
	protected static final void createInstructions(String fileName) {
		OutputFile help = createFile(fileName);
		help.println("GT Tomes Instructions");
		help.println("Zero Point");
		help.println("Currently, saving books is nonfunctional. This is because I cannot iterate over each string tag in the book. Trying yields the first page over and over.\n--PAGEBREAK--");
		help.println("Loading works properly, though. This means you can manually edit the book files on your hard drive, and load them in-game.\n--PAGEBREAK--");
		help.println("The format is as follows:\n");
		help.println("<TITLE>");
		help.println("<AUTHOR>");
		help.println("<CONTENT>");
		help.println("To insert a pagebreak, put the string '--PAGEBREAK--' on its own line.\n--PAGEBREAK--");
		help.println("Blank lines are kept as-is, and included in the loaded book. To create formatting, use either the formatting symbol or double ampersands ('&' twice) with the correct code.\n--PAGEBREAK--");
		help.println("Formatting codes can be found easily on the Minecraft wiki.\n\nPlease note that no checks are done to ensure that the content you provide fits on one line.\n--PAGEBREAK--");
		help.print("That's all there is to it!\nAnd if you can help me figure out why the NBTTagList seems to be caching entries, I'll put your name in the credits for this mod.\n\n~Zero");
		help.close();
		LOG.info("Wrote help file " + fileName);
	}
	protected static final void createGuideCommands(String fileName) {
		OutputFile help = createFile(fileName);
		GTBaseCommand[] loadedCommands = GTCommands.getCommandArray();
		help.println("GT Commands Help");
		help.println("Zero Point");
		help.print("The GT Commands module is currently ");
		if (GTCore.Modules.commandsEnabled()) {
			help.println("§2ENABLED§0");
		}
		else {
			help.println("§4DISABLED§0");
		}
		help.println("");
		help.print("The Commands module adds a large number of commands, mostly for... ");
		help.println("less-than-legit gameplay. This book lists all of the commands currently registered.");
		for (GTBaseCommand cmd : loadedCommands) {
			help.println("--PAGEBREAK--");
			help.println("§l" + cmd.getCommandNamePlain() + "§0");
			help.println("Args:");
			help.println(cmd.getCommandArgs(null));
			help.println("");
			help.println(cmd.getCommandHelp(null));
		}
		help.close();
		LOG.info("Wrote help file " + fileName);
	}
	protected static final void createGuideCraft(String fileName) {
		OutputFile help = createFile(fileName);
		help.println("GT Recipes Help");
		help.println("Zero Point");
		help.print("The GT Recipes module is currently ");
		if (GTCore.Modules.craftEnabled()) {
			help.println("§2ENABLED§0");
		}
		else {
			help.println("§4DISABLED§0");
		}
		help.println("");
		help.println("The Recipes module adds whatever miscellaneous crafting recipes I've thought of. This book lists all of them, but they might be disabled!");
		help.println("--PAGEBREAK--");
		help.println("§lBedrock§0");
		help.println("EOE");
		help.println("ODO");
		help.println("EOE");
		help.println("E = End Stone");
		help.println("O = Obsidian");
		help.println("D = Diamond Block");
		help.println("--PAGEBREAK--");
		help.println("§lEnd Portal Frame§0");
		help.println("OEO");
		help.println("DSD");
		help.println("OEO");
		help.println("E = Eye of Ender");
		help.println("O = Obsidian");
		help.println("D = Diamond");
		help.println("S = End Stone");
		help.println("--PAGEBREAK--");
		help.println("§lNether Star§0");
		help.println("GSG");
		help.println("DOD");
		help.println("GSG");
		help.println("G = Gold Ingot");
		help.println("S = Wither Skull");
		help.println("D = Diamond Block");
		help.println("O = Obsidian");
		help.close();
		LOG.info("Wrote help file " + fileName);
	}
	protected static final void createGuideEnchant(String fileName) {
		OutputFile help = createFile(fileName);
		help.println("GT Enchant Help");
		help.println("Zero Point");
		help.print("The GT Enchant module is currently ");
		if (GTCore.Modules.enchantEnabled()) {
			help.println("§2ENABLED§0");
		}
		else {
			help.println("§4DISABLED§0");
		}
		help.println("");
		help.println("The Enchant module adds a couple of new enchantments. This book explains what they do and how to get them.");
		help.println("--PAGEBREAK--");
		help.println("§lDecapitate§0");
		help.println("Max level: V (5)");
		help.println("Applies to: Sword/Axe");
		help.println("Each level grants an additional 10% chance of a mob dropping its head, starting at 10% for level I (1).");
		help.println("Place the tool in a crafting table with 1-5 Wither Skulls. Each skull gives another level of Decapitate.");
		help.println("--PAGEBREAK--");
		help.println("§lGodslayer§0");
		help.println("Max level: I (1)");
		help.println("Applies to: Sword");
		help.println("Causes ridiculously high damage to boss mobs and hits them with lightning.");
		help.print("Place the sword in a crafting table with seven nether stars and one dragon egg. If easy mode is enabled, replace the dragon egg with a nether star. If shaped, sword goes in the middle and egg (if needed) on top.");
		help.println("--PAGEBREAK--");
		help.println("§Speed of the Wolf§0");
		help.println("Max level: I (1)");
		help.println("Applies to: Boots");
		help.println("Increases movement speed.\n\nInspired, of course, by Direwolf20.");
		help.println("§4SHAPED§0 - recipe:");
		help.println("S B S");
		help.println("S R S");
		help.println("R F R");
		help.println("'S' is sugar, 'B' is the boots, 'R' is redstone, and 'F' is a feather.");
		help.println("--PAGEBREAK--");
		help.println("§lJagged§0");
		help.println("Max level: configurable");
		help.println("Applies to: Sword");
		help.println("Inflicts the Bleeding effect, causing damage over time.");
		help.println("");
		help.println("Player death via bleeding has its own death messages!");
		help.close();
		LOG.info("Wrote help file " + fileName);
	}
	protected static final void createGuideSonic(String fileName) {
		OutputFile help = createFile(fileName);
		help.println("GT Sonic Help");
		help.println("Zero Point");
		help.print("The GT Sonic module is currently ");
		if (GTCore.Modules.sonicEnabled()) {
			help.println("§2ENABLED§0");
		}
		else {
			help.println("§4DISABLED§0");
		}
		help.println("");
		help.println("The Sonic module adds - what else - Sonic Screwdrivers! This book lists their effects.");
		help.println("--PAGEBREAK--");
		help.println("Opens/closes doors");
		help.println("Grows grass");
		help.println("Breaks down sandstone into sand");
		help.println("Heats sand into glass blocks");
		help.println("Compresses glass blocks into glass panes");
		help.println("Destroys ice (sneak to melt it into water)");
		help.println("Ignites TNT (sneak to improve it for mining)");
		help.println("--PAGEBREAK--");
		help.println("Stabilizes gravel into cobblestone");
		help.println("Heats cobblestone into smoothstone");
		help.println("Fiddles with redstone lamps");
		help.println("Scans health of living entities");
		help.println("Heals living entities up to " + GTSonic.maxCreatureHealPercent + "% max");
		help.println("--PAGEBREAK--");
		help.print("Sneak when right clicking a redstone lamp to force it to stay in its current state (on or off). ");
		help.println("Otherwise, it will toggle and " + ChatMsg.ITALIC + "then" + ChatMsg.BLACK + " remain in that state.");
		help.println("--PAGEBREAK--");
		help.print("When right clicking a sonic'd lamp, sneak to return it to normal, so redstone controls it. ");
		help.println("Otherwise, it will just toggle states.");
		help.println("--PAGEBREAK--");
		help.println("Please note that sonic-ing " + ChatMsg.ITALIC + "active" + ChatMsg.BLACK + " redstone lamps is buggy and won't work yet. I'm working on fixing it.");
		help.println("--PAGEBREAK--");
		help.println("Also, I'm aware that the Sonic Screwdriver speed boost appears to be broken. I have no idea why, but I do have a metaphorical sledgehammer which I am using to scare my code into working again.");
		help.close();
		LOG.info("Wrote help file " + fileName);
	}
	protected static final OutputFile createFile(String fileName) {
		// FIXME make one centralized location for this line (see Tome.java)
		String path = GTCore.getConfig().dir("tomes", "path", "config/gtweaks/tomes/", "The directory that holds the tomes (in text files)");
		Path tomes = FileSystems.getDefault().getPath(path);
		tomes.toFile().mkdirs();
		String fpath = FileSystems.getDefault().getPath(tomes.toAbsolutePath().toString() + "/" + fileName).toAbsolutePath().toString();
		OutputFile help = new OutputFile(fpath);
		try {
			help.delete();
			help.create();
			help.open(false);
		}
		catch (IOException e) {
			throw new RuntimeException("Could not create " + fileName + " premade book file", e);
		}
		LOG.info("Initialized premade book file: " + fileName);
		return help;
	}
}
