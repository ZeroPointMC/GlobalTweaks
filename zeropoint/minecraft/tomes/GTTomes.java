package zeropoint.minecraft.tomes;


import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.Collection;
import java.util.logging.Logger;

import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.client.ClientCommandHandler;
import zeropoint.core.io.file.InputFile;
import zeropoint.core.io.file.OutputFile;
import zeropoint.minecraft.commands.GTCommands;
import zeropoint.minecraft.core.Config;
import zeropoint.minecraft.core.GTBaseCommand;
import zeropoint.minecraft.core.GTCore;
import zeropoint.minecraft.core.util.ChatMsg;
import zeropoint.minecraft.core.util.Log;
import zeropoint.minecraft.core.util.WrittenBookTag;
import zeropoint.minecraft.core.util.manip.PlayerHelper;
import zeropoint.minecraft.core.util.manip.WorldHelper;
import zeropoint.minecraft.sonic.GTSonic;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.network.NetworkMod;
import cpw.mods.fml.relauncher.Side;


@SuppressWarnings("javadoc")
@Mod(modid = GTTomes.modid, name = GTTomes.name, version = GTTomes.version, dependencies = "required-after:gtweaks-core")
@NetworkMod(clientSideRequired = true, serverSideRequired = false)
public class GTTomes {
	public static final String modid = "gtweaks-tomes";
	public static final String name = "GlobalTweaks|Tomes";
	public static final String version = "beta";
	protected static String path = "";
	protected static double loadBlastSize = 1.0;
	protected static boolean loadBlastGrief = false;
	protected static double failBlastMultiplier = 2.5;
	protected static boolean failBlastGrief = true;
	private static final Logger LOG = Log.getLogger(name);
	private static Config cfg;
	public static class CommandTome extends GTBaseCommand {
		public String getCommandName() {
			return "tome";
		}
		@Override
		public String getCommandHelp(ICommandSender src) {
			return "Save and load books from disk";
		}
		@Override
		public String getCommandArgs(ICommandSender src) {
			return "{save [name]|load [name]|debug [name]|list}";
		}
		@Override
		public void execute(ICommandSender src, EntityPlayer player, String[] args) {
			if ((player == null) || (args.length < 1)) {
				return;
			}
			// Purpose: list the available tomes
			if (args[0].equalsIgnoreCase("list")) {
				String[] tomes = FileSystems.getDefault().getPath(path).toFile().list();
				if (tomes == null) {
					throw new NullPointerException("Internal error - cannot resolve tome path to a directory!");
				}
				new ChatMsg("The following tomes are available:").send(player);
				for (String tome : tomes) {
					new ChatMsg(tome).send(player);
				}
				return;
			}
			// Purpose: turn a single book into a written book
			if (PlayerHelper.isHolding(player, Item.book)) {
				ItemStack tmp = player.getCurrentEquippedItem();
				if (tmp.stackSize == 1) {
					tmp.itemID = Item.writtenBook.itemID;
					// new ChatMsg("You change your book into a tome!").send(player);
				}
			}
			// Purpose: require the player now be holding a written book
			if ( !PlayerHelper.isHolding(player, Item.writtenBook)) {
				WorldHelper.explodeAt(player, loadBlastSize * failBlastMultiplier, failBlastGrief);
				new ChatMsg("You try to change your item into a tome, but it fails!").send(player);
				return;
			}
			ItemStack held = player.getCurrentEquippedItem();
			if (held.stackTagCompound == null) {
				held.stackTagCompound = new NBTTagCompound();
			}
			NBTTagCompound tag = held.getTagCompound();
			String tomeName = "";
			try {
				tomeName = args[1];
			}
			catch (ArrayIndexOutOfBoundsException e) {
				tomeName = player.getCurrentEquippedItem().stackTagCompound.getString("title");
			}
			tomeName = tomeName.replaceAll("[/\\\\:\\.\\?\\*\\^\\&\\$\\#\\@\\$\\!\\(\\)\\[\\]\\{\\}\\<\\>]", "_").replaceAll("\\s+", "-");
			// Purpose: load a tome from disk
			if (args[0].equalsIgnoreCase("load")) {
				InputFile tome = new InputFile(path + "/" + tomeName);
				try {
					tome.create();
					WrittenBookTag.unserialize(tome.readAll()).applyToNBT(tag);
					WorldHelper.explodeAt(player, loadBlastSize, false);
					new ChatMsg("With a flash and a bang, your book changes!").send(player);
				}
				catch (IOException e) {
					new ChatMsg("Something went wrong!").send(player);
					new ChatMsg(e.toString()).send(player);
					return;
				}
			}
			// Purpose: save a tome to disk
			else if (args[0].equalsIgnoreCase("save")) {
				new ChatMsg("Book saving is not yet functional. Use 'force-save' to do it anyway.").send(player);
			}
			else if (args[0].equalsIgnoreCase("force-save")) {
				if ( !player.worldObj.isRemote) {
					return;
				}
				if (tomeName.equalsIgnoreCase("instructions")) {
					if (GTCore.bronyMode()) {
						new ChatMsg("Twilight says 'instructions' is a reserved name.").send(player);
						return;
					}
					new ChatMsg("Sorry, but you can't save any tome with the name 'instructions' (case-insensitive)").send(player);
					return;
				}
				OutputFile tome = new OutputFile(path + tomeName);
				try {
					tome.create();
					WrittenBookTag btag = new WrittenBookTag(tag.getString("author"), tag.getString("title"));
					btag.append(tag.getTagList("pages"));
					tome.print(btag);
					if (GTCore.bronyMode()) {
						new ChatMsg("Twilight Sparkle copies your book into her personal library!").send(player);
					}
					else {
						new ChatMsg("You saved your tome into the magical ether known as library-space!").send(player);
					}
				}
				catch (IOException e) {
					new ChatMsg("Something went wrong!").send(player);
					new ChatMsg(e.toString()).send(player);
					return;
				}
				finally {
					tome.close();
				}
			}
			// Purpose: find the path where the current tome will be saved
			else if (args[0].equalsIgnoreCase("debug")) {
				Path tome = FileSystems.getDefault().getPath(path, tomeName).toAbsolutePath();
				new ChatMsg("This will be saved to the following location:").send(player);
				new ChatMsg(tome.toString()).send(player);
			}
		}
		@Override
		public boolean isFinished() {
			return false;
		}
	}
	protected static final CommandTome cmdTome = new CommandTome();
	protected static ItemStack bookFactory(String title, String author, String... pages) {
		return new WrittenBookTag(title, author).append(pages).create();
	}
	protected static ItemStack bookFactory(String title, String author, Collection<? extends String> pages) {
		return bookFactory(title, author, pages.toArray(new String[] {}));
	}
	@SuppressWarnings("unused")
	@EventHandler
	public static void load(FMLInitializationEvent event) {
		if ( !GTCore.Modules.tomesEnabled()) {
			LOG.severe("Module disabled! All GlobalTweaks in-game guides will be unavailable!");
			return;
		}
		cfg = GTCore.getConfig();
		path = cfg.dir("tomes", "path", "config/gtweaks/tomes/", "The directory that holds the tomes (in text files)");
		loadBlastSize = cfg.decimal("tomes", "loadExplosionSize", loadBlastSize, "How big is the explosion when loading a saved tome?");
		loadBlastGrief = cfg.bool("tomes", "loadExplosionGriefing", loadBlastGrief, "Should the blast from loading a tome damage blocks?");
		failBlastMultiplier = cfg.decimal("tomes", "failBlastMultiplier", failBlastMultiplier, "How big is the explosion when unable to convert a held item into a tome?");
		failBlastGrief = cfg.bool("tomes", "failExplosionGriefing", failBlastGrief, "Should the blast from failing to convert an item damage blocks?");
		if (loadBlastSize < 0.5) {
			loadBlastSize = 0.5;
		}
		if (failBlastMultiplier < 1.0) {
			failBlastMultiplier = 1.0;
		}
		createInstructions("INSTRUCTIONS");
		createGuideCommands("HELP-COMMANDS");
		createGuideCraft("HELP-CRAFT");
		createGuideEnchant("HELP-ENCHANT");
		createGuideSonic("HELP-SONIC");
	}
	@EventHandler
	public static void register(FMLPostInitializationEvent event) {
		if (event.getSide() == Side.CLIENT) {
			ClientCommandHandler.instance.registerCommand(cmdTome);
			LOG.info("Registered client side tome command");
		}
	}
	protected static final void createInstructions(String fileName) {
		OutputFile help = createFile(fileName);
		help.println(name + " Instructions");
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
		GTBaseCommand[] cmds = GTCommands.getCommandArray();
		help.println("GlobalTweaks|Commands Help");
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
		for (GTBaseCommand cmd : cmds) {
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
		help.println("GlobalTweaks|Recipes Help");
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
		help.println("GlobalTweaks|Enchant Help");
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
		help.println("Place the tool you wish to enchant in a crafting table with 1-5 Wither Skulls. Each skull gives another level of Decapitate.");
		help.println("--PAGEBREAK--");
		help.println("§lGodslayer§0");
		help.println("Max level: I (1)");
		help.println("Applies to: Sword");
		help.println("Causes ridiculously high damage to boss mobs and hits them with lightning. We're talking one, MAYBE two hits to kill ANYTHING.");
		help.println("§4MAY BE SHAPED§0 - Place the sword in a crafting table with seven nether stars and one dragon egg. If easy mode is enabled, replace the dragon egg with another nether star. If shaped, sword goes in the middle and egg (if needed) on top.");
		help.close();
		LOG.info("Wrote help file " + fileName);
	}
	protected static final void createGuideSonic(String fileName) {
		OutputFile help = createFile(fileName);
		help.println("GlobalTweaks|Sonic Help");
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
		help.close();
		LOG.info("Wrote help file " + fileName);
	}
	protected static final OutputFile createFile(String fileName) {
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
			throw new RuntimeException("Could not create " + fileName + " help file", e);
		}
		LOG.info("Initialized help file " + fileName);
		return help;
	}
}
