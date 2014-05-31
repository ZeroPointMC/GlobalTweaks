package zeropoint.minecraft.core;


import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import net.minecraft.command.ICommandSender;
import net.minecraft.crash.CrashReport;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ReportedException;
import net.minecraft.util.StringTranslate;
import net.minecraftforge.common.Configuration;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import zeropoint.core.io.StringBufferInputStream;
import zeropoint.minecraft.core.asm.GTLoadingPlugin;
import zeropoint.minecraft.core.util.Log;
import zeropoint.minecraft.core.util.manip.EnchantHelper;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.ModContainer;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkMod;
import cpw.mods.fml.common.registry.LanguageRegistry;


/**
 * GlobalTweaks core class - contains miscellaneous functions/properties
 * 
 * @author Zero Point
 */
@Mod(modid = GTCore.modid, name = GTCore.name, version = GTCore.version)
@NetworkMod(clientSideRequired = true, serverSideRequired = false)
public class GTCore {
	/**
	 * Event handlers that modify vanilla behavior in some way
	 * 
	 * @author Zero Point
	 */
	@SuppressWarnings("javadoc")
	public static class BasicHandlers {
		public final boolean handleItemDrop;
		public BasicHandlers() {
			this.handleItemDrop = cfg.bool("handlers", "drop", true, "Register the EntityJoinWorldEvent handler to modify items dropped in the world?\nExtends lifetime of dropped items when enchanted with Unbreaking\nExperimental - might not have any real effect, might (shouldn't, but might) break things");
		}
		/**
		 * Extend the lifetime of dropped items that have the Unbreaking enchantment
		 */
		@ForgeSubscribe
		public void onItemDrop(EntityJoinWorldEvent evt) {
			if ( !this.handleItemDrop) {
				return;
			}
			Entity ent = evt.entity;
			if (ent instanceof EntityItem) {
				EntityItem item = (EntityItem) ent;
				ItemStack stack = item.getEntityItem();
				final int unbreaking = EnchantHelper.getEnchantmentLevel(Enchantment.unbreaking.effectId, stack);
				item.lifespan *= unbreaking + 1;
			}
		}
	}
	/**
	 * The Forge ModID of the GT Core module
	 */
	public static final String modid = "gtweaks-core";
	/**
	 * The Forge Mod Name of the GT Core module
	 */
	public static final String name = "GlobalTweaks|Core";
	/**
	 * The Forge Mod Version of the GT Core module
	 */
	public static final String version = "release";
	/**
	 * The public debug logger
	 */
	public static final Logger DEBUG = Log.getLogger("GT Debug");
	private static final Logger logger = Log.getLogger(name);
	private static Configuration config;
	private static Config cfg;
	private static boolean bronyModeEnabled = false;
	private static final List<String> loadedMods = new ArrayList<String>();
	private static final List<String> loadedModsUnsafe = new ArrayList<String>();
	private static boolean modListLoaded = false;
	/**
	 * State of each of the GT modules
	 */
	public static final class Modules {
		private static boolean commands = false;
		private static boolean craft = false;
		private static boolean enchant = false;
		private static boolean sonic = false;
		private static boolean arcana = false;
		private static boolean effects = false;
		private static boolean misc = false;
		/**
		 * @return <code>true</code> iff the Commands module is enabled
		 */
		public static boolean commandsEnabled() {
			return commands;
		}
		/**
		 * @return <code>true</code> iff the Recipes module is enabled
		 */
		public static boolean craftEnabled() {
			return craft;
		}
		/**
		 * @return <code>true</code> iff the Effects module is enabled
		 */
		public static boolean effectsEnabled() {
			return effects;
		}
		/**
		 * @return <code>true</code> iff the Enchant module is enabled
		 */
		public static boolean enchantEnabled() {
			return enchant;
		}
		/**
		 * @return <code>true</code> iff the Sonic module is enabled
		 */
		public static boolean sonicEnabled() {
			return sonic;
		}
		/**
		 * @return <code>true</code> iff the Tomes module is enabled
		 */
		public static boolean arcanaEnabled() {
			return arcana;
		}
		/**
		 * @return <code>true</code> iff the Misc module is enabled
		 */
		public static boolean miscEnabled() {
			return misc;
		}
	}
	/**
	 * Initialize the config file
	 */
	protected static void initConf() {
		config = new Configuration(new File("config/GlobalTweaks.cfg"));
		cfg = new Config(config);
		bronyModeEnabled = cfg.bool("core", "bronyMode", bronyModeEnabled, "Do you like ponies?");
		Modules.commands = cfg.bool("enable", "commands", true, "Enable the GlobalTweaks|Commands module?");
		Modules.craft = cfg.bool("enable", "recipes", true, "Enable the GlobalTweaks|Recipes module?");
		Modules.sonic = cfg.bool("enable", "sonic", true, "Enable the GlobalTweaks|Sonic module?");
		Modules.effects = cfg.bool("enable", "effects", true, "Enable the GlobalTweaks|Effects module?");
		Modules.enchant = cfg.bool("enable", "enchant", true, "Enable the GlobalTweaks|Enchant module?");
		Modules.arcana = cfg.bool("enable", "arcana", true, "Enable the GlobalTweaks|Arcana module?");
		Modules.misc = cfg.bool("enable", "misc", true, "Enable the GlobalTweaks|Misc module?");
		logger.config("Configuration initialized");
	}
	@SuppressWarnings({
		"javadoc",
		"unused"
	})
	@EventHandler
	public void preinit(FMLPreInitializationEvent event) {
		this.initConf();
		loadModList(true);
		MinecraftForge.EVENT_BUS.register(new BasicHandlers());
	}
	@SuppressWarnings({
		"javadoc",
		"unused"
	})
	@EventHandler
	public static void postinit(FMLPostInitializationEvent event) {
		cfg.save(logger);
	}
	/**
	 * Get the {@link EntityPlayer} associated with the given {@link ICommandSender}
	 * 
	 * @param ics
	 *            - the ICommandSender object to retrieve the player object from
	 * @return EntityPlayer represented by the given ICommandSender, or <code>null</code> if there isn't one
	 */
	public final static EntityPlayer getPlayerEntity(ICommandSender ics) {
		try {
			EntityPlayer pl = ics.getEntityWorld().getPlayerEntityByName(ics.getCommandSenderName());
			if (pl == null) {
				throw new NullPointerException();
			}
			return pl;
		}
		catch (Exception e) {
			return null;
		}
	}
	/**
	 * Indicates whether the user wants to ponify the mod
	 * 
	 * @return <code>true</code> iff the user's config has enabled brony mode, <code>false</code> otherwise
	 */
	public static final boolean bronyMode() {
		return bronyModeEnabled;
	}
	/**
	 * Get the GlobalTweaks global {@link Configuration} object
	 * 
	 * @return the Configuration object used by all of GlobalTweaks
	 * @deprecated GlobalTweaks|Core includes a special {@link Config} class for use instead.
	 */
	@Deprecated
	public static final Configuration getConfiguration() {
		return config;
	}
	/**
	 * Get the {@link Config} used by all GlobalTweaks
	 * 
	 * @return the Config object wrapping the {@link Configuration} holding GlobalTweaks settings
	 */
	public static final Config getConfig() {
		return cfg;
	}
	/**
	 * Load the internal list of all active mods iff it has not yet been loaded
	 */
	public static final void loadModList() {
		loadModList(false);
	}
	/**
	 * Load the internal list of all active mods, optionally forcing a reload if needed
	 * 
	 * @param force
	 *            - whether to reload the list if it has already been loaded
	 */
	protected static final void loadModList(boolean force) {
		if (modListLoaded && !force) {
			return;
		}
		if (modListLoaded && force) {
			logger.config("Reloading active mod list");
		}
		else {
			logger.config("Loading active mod list");
		}
		List<ModContainer> mods = Loader.instance().getActiveModList();
		for (ModContainer mod : mods) {
			loadedMods.add(mod.getModId());
			loadedModsUnsafe.add(mod.getModId().toLowerCase());
		}
	}
	/**
	 * Checks whether any mod with the given mod id is loaded
	 * 
	 * @param modId
	 *            - the Forge Mod ID to look for
	 * @param allowUnsafe
	 *            - <code>true</code> to ignore case
	 * @return <code>true</code> iff there is a mod with the given mod id loaded
	 */
	public static final boolean isModLoaded(String modId, boolean allowUnsafe) {
		loadModList();
		if (loadedMods.contains(modId)) {
			return true;
		}
		if (allowUnsafe) {
			return loadedModsUnsafe.contains(modId.toLowerCase());
		}
		return false;
	}
	/**
	 * Checks whether any mod with the given mod id is loaded, not ignoring case
	 * 
	 * @param modId
	 *            - the Forge Mod ID to look for
	 * @return <code>true</code> iff there is a mod with the given mod id loaded
	 */
	public static final boolean isModLoaded(String modId) {
		return isModLoaded(modId, false);
	}
	/**
	 * Inject a line of text into the minecraft string translation table
	 * 
	 * @param from
	 *            - the unlocalized name
	 * @param to
	 *            - the translated name
	 */
	public static final void injectStringTranslation(String from, String to) {
		StringBufferInputStream stream = new StringBufferInputStream();
		stream.appendToBuffer(from + "=" + to);
		StringTranslate.inject(stream);
		try {
			stream.close();
		}
		catch (IOException e) {
			logger.log(Level.WARNING, "Error injecting string localization", e);
		}
		LanguageRegistry.instance().addStringLocalization(from, to);
	}
	/**
	 * @param description
	 *            - the reason for crashing
	 * @param cause
	 *            - the {@link Throwable} that caused the crash
	 * @throws ReportedException
	 *             with the given description and cause
	 */
	public static final void crash(String description, Throwable cause) throws ReportedException {
		crash(description, cause, 1);
	}
	/**
	 * @param cause
	 *            - the {@link Throwable} that caused the crash
	 * @throws ReportedException
	 *             with the given description and cause
	 */
	public static final void crash(Throwable cause) throws ReportedException {
		crash(cause.getLocalizedMessage(), cause, 1);
	}
	/**
	 * @param description
	 *            - the reason for crashing
	 * @param cause
	 *            - the {@link Throwable} that caused the crash
	 * @param level
	 *            - how many frames to pull from the stack trace
	 * @throws ReportedException
	 *             with the given description and cause
	 */
	public static final void crash(String description, Throwable cause, int level) throws ReportedException {
		++level;
		ReportedException e = new ReportedException(new CrashReport(description, cause));
		StackTraceElement[] trace = e.getStackTrace();
		StackTraceElement[] newTrace = new StackTraceElement[trace.length - level];
		System.arraycopy(trace, 1, newTrace, 0, trace.length - level);
		e.setStackTrace(newTrace);
		throw e;
	}
	/**
	 * @return <code>true</code> if we are running in a deobfuscated environment, <code>false</code> otherwise
	 */
	public static final boolean inDev() {
		return GTLoadingPlugin.isDevEnrivonment();
	}
}
