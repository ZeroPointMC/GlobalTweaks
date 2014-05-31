package zeropoint.minecraft.tomes;


import java.util.logging.Logger;

import zeropoint.minecraft.core.Config;
import zeropoint.minecraft.core.GTCore;
import zeropoint.minecraft.core.util.Log;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.network.NetworkMod;


@SuppressWarnings("javadoc")
@Mod(modid = GTArcana.modid, name = GTArcana.name, version = GTArcana.version, dependencies = "required-after:gtweaks-core;required-after:gtweaks-enchant")
@NetworkMod(clientSideRequired = true, serverSideRequired = false)
public class GTArcana {
	public static final String modid = "gtweaks-arcana";
	public static final String name = "GlobalTweaks|Arcana";
	public static final String version = "pre-alpha";
	protected static String path = "";
	private static final Logger LOG = Log.getLogger(name);
	private static Config cfg;
	protected static ItemTomeBlank blankTome;
	protected static ItemTomeWritten activeTome;
	@SuppressWarnings("unused")
	@EventHandler
	public static void init(FMLInitializationEvent event) {
		cfg = GTCore.getConfig();
		if ( !GTCore.Modules.arcanaEnabled()) {
			LOG.warning("Module disabled!");
			return;
		}
		final int idBlank = cfg.item("arcana.tome.id", "blank", 2442, "Item ID for the Blank Arcane Tome");
		final int idWritten = cfg.item("arcana.tome.id", "written", 2443, "Item ID for the Arcane Tome");
		final int dmg = cfg.integer("arcana.tome", "uses", 16, "Maximum number of uses for the Arcane Tome\nEmpowered Arcane Tomes get double the uses") - 1;
		blankTome = new ItemTomeBlank(idBlank);
		activeTome = new ItemTomeWritten(idWritten, dmg);
	}
	public static ItemTomeBlank blank() {
		return blankTome;
	}
	public static ItemTomeWritten written() {
		return activeTome;
	}
}
