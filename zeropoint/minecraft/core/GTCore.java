package zeropoint.minecraft.core;


import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.common.Configuration;
import zeropoint.minecraft.core.util.Log;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.ModContainer;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkMod;


/**
 * GlobalTweaks core class - contains miscellneous functions/properties
 * 
 * @author Zero Point
 */
@Mod(modid = GTCore.modid, name = GTCore.name, version = GTCore.version)
@NetworkMod(clientSideRequired = true, serverSideRequired = false)
public class GTCore {
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
	public static final String version = "public";
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
		private static boolean tomes = false;
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
		public static boolean tomesEnabled() {
			return tomes;
		}
	}
	/**
	 * Initialize the config file
	 */
	@SuppressWarnings("synthetic-access")
	protected static void initConf() {
		config = new Configuration(new File("config/GlobalTweaks.cfg"));
		cfg = new Config(config);
		bronyModeEnabled = cfg.bool("core", "bronyMode", bronyModeEnabled, "Do you like ponies?");
		Modules.commands = cfg.bool("enable", "commands", true, "Enable the GlobalTweaks|Commands module?");
		Modules.craft = cfg.bool("enable", "recipes", true, "Enable the GlobalTweaks|Recipes module?");
		Modules.sonic = cfg.bool("enable", "sonic", true, "Enable the GlobalTweaks|Sonic module?");
		Modules.enchant = cfg.bool("enable", "enchant", true, "Enable the GlobalTweaks|Enchant module?");
		Modules.tomes = cfg.bool("enable", "tomes", true, "Enable the GlobalTweaks|Tomes module?");
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
}
