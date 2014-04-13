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
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkMod;


@Mod(modid = GTCore.modid, name = GTCore.name, version = GTCore.version)
@NetworkMod(clientSideRequired = true, serverSideRequired = false)
public class GTCore {
	public static final String modid = "gtweaks-core";
	public static final String name = "GlobalTweaks";
	public static final String version = "release";
	public static final Logger logger = Log.getLogger(name);
	private static Configuration config;
	private static Config cfg;
	protected static boolean bronyModeEnabled = false;
	private static final List<String> loadedMods = new ArrayList<String>();
	private static final List<String> loadedModsUnsafe = new ArrayList<String>();
	private static boolean modListLoaded = false;
	public static final class Modules {
		protected static boolean commands = false;
		protected static boolean craft = false;
		protected static boolean enchant = false;
		protected static boolean sonic = false;
		protected static boolean tomes = false;
		public static boolean commandsEnabled() {
			return commands;
		}
		public static boolean craftEnabled() {
			return craft;
		}
		public static boolean enchantEnabled() {
			return enchant;
		}
		public static boolean sonicEnabled() {
			return sonic;
		}
		public static boolean tomesEnabled() {
			return tomes;
		}
	}
	protected static void initConf() {
		config = new Configuration(new File("config/" + GTCore.name + ".cfg"));
		cfg = new Config(config);
		bronyModeEnabled = cfg.bool("core", "bronyMode", bronyModeEnabled, "Do you like ponies?");
		Modules.commands = cfg.bool("enable", "commands", true, "Enable the GlobalTweaks|Commands module?");
		Modules.craft = cfg.bool("enable", "recipes", true, "Enable the GlobalTweaks|Recipes module?");
		Modules.sonic = cfg.bool("enable", "sonic", true, "Enable the GlobalTweaks|Sonic module?");
		Modules.enchant = cfg.bool("enable", "enchant", true, "Enable the GlobalTweaks|Enchant module?");
		Modules.tomes = cfg.bool("enable", "tomes", true, "Enable the GlobalTweaks|Tomes module?");
	}
	@EventHandler
	public void preinit(FMLPreInitializationEvent event) {
		this.initConf();
	}
	@EventHandler
	public void init(FMLInitializationEvent event) {
		loadModList(true);
	}
	@EventHandler
	public void postinit(FMLPostInitializationEvent event) {
		cfg.save();
	}
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
	public static final boolean bronyMode() {
		return bronyModeEnabled;
	}
	public static final Configuration getConfiguration() {
		return config;
	}
	public static final Config getConfig() {
		return cfg;
	}
	public static final void loadModList() {
		loadModList(false);
	}
	protected static final void loadModList(boolean force) {
		if (modListLoaded && !force) {
			return;
		}
		List<ModContainer> mods = Loader.instance().getActiveModList();
		for (ModContainer mod : mods) {
			loadedMods.add(mod.getModId());
			loadedModsUnsafe.add(mod.getModId().toLowerCase());
		}
	}
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
	public static final boolean isModLoaded(String modId) {
		return isModLoaded(modId, false);
	}
}
