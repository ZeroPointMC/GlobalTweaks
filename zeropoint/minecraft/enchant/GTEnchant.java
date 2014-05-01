package zeropoint.minecraft.enchant;


import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import net.minecraft.enchantment.Enchantment;
import zeropoint.minecraft.core.Config;
import zeropoint.minecraft.core.GTCore;
import zeropoint.minecraft.core.util.Log;
import zeropoint.minecraft.enchant.ench.EnchantmentDecapitate;
import zeropoint.minecraft.enchant.ench.EnchantmentGodslayer;
import zeropoint.minecraft.enchant.ench.EnchantmentJagged;
import zeropoint.minecraft.enchant.ench.EnchantmentWolfSpeed;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.network.NetworkMod;
import cpw.mods.fml.common.registry.LanguageRegistry;


@SuppressWarnings("javadoc")
@Mod(modid = GTEnchant.modid, name = GTEnchant.name, version = GTEnchant.version, dependencies = "required-after:gtweaks-core")
@NetworkMod(clientSideRequired = true, serverSideRequired = false)
public class GTEnchant {
	public static final String modid = "gtweaks-enchant";
	public static final String name = "GlobalTweaks|Enchant";
	public static final String version = "release";
	private static final Map<String, Enchantment> enchants = new HashMap<String, Enchantment>();
	private static Config cfg;
	private static final Logger LOG = Log.getLogger(name);
	@SuppressWarnings("unused")
	@EventHandler
	public static void init(FMLInitializationEvent event) {
		cfg = GTCore.getConfig();
		if ( !GTCore.Modules.enchantEnabled()) {
			LOG.warning("Module disabled!");
			return;
		}
		int decap = getEnchantID("decapitate", 10, "Force mobs to drop their heads more often");
		int slayer = getEnchantID("godslayer", 11, "Deal MASSIVE damage to boss mobs");
		int wolfspeed = getEnchantID("speedOfTheWolf", 12, "(Semi-)early-game enchant to increase movement speed");
		int jagged = getEnchantID("jagged", 13, "Cause bleeding DoT to targets");
		boolean wolfspeedShaped = cfg.bool("enchant.misc", "speedOfTheWolf.shapedRecipes", true, "{[Sugar / Boots / Sugar] [Sugar / Redstone / Sugar] [Redstone - Feather - Redstone]} if true");
		int slayerBossDamage = cfg.integer("enchant.misc", "godslayer.damage.boss", 200, "Damage dealt to boss mobs each hit with a Godslayer-enchanted weapon");
		int slayerDamage = cfg.integer("enchant.misc", "godslayer.damage.normal", 10, "Damage dealt to non-boss mobs with a Godslayer-enchanted weapons");
		boolean slayerShaped = cfg.bool("enchant.misc", "godslayer.shapedRecipes", true, "Sword in the middle, dragon egg on top, nether stars all around (if false, ONLY position is ignored!)");
		boolean slayerHardMode = cfg.bool("enchant.misc", "godslayer.hardMode", true, "Require a dragon egg to get the enchantment? (If false, another nether star will be needed instead)");
		int jaggedMaxLvl = cfg.integer("enchant.misc", "jagged.maxLevel", 3, "Maximum level of the Jagged enchantment");
		int jaggedBaseDuration = cfg.integer("enchant.misc", "jagged.baseTime", 20, "The base duration of the Bleed effect inflicted by Jagged weapons");
		int jaggedFactor = cfg.integer("enchant.misc", "jagged.levelFactor", 5, "When using a Jagged weapon, the target is given the Bleed effect.\nIt will last for (base time + (enchantment level * this number))");
		if (getEnchantEnabled("decapitate", true, "Register the Decapitation enchantment?")) {
			enchants.put("decapitate", new EnchantmentDecapitate(decap));
			LOG.info("Registered Decapitate enchantment");
		}
		else {
			LOG.info("Didn't register Decapitate enchantment");
		}
		if (getEnchantEnabled("godslayer", false, "Register the completely OP Godslayer enchantment?")) {
			enchants.put("godslayer", new EnchantmentGodslayer(slayer, slayerBossDamage, slayerDamage, slayerShaped, slayerHardMode));
			LOG.info("Registered Godslayer enchantment");
		}
		else {
			LOG.info("Didn't register Godslayer enchantment");
		}
		if (getEnchantEnabled("speedOfTheWolf", false, "Register the Direwolf20-inspired Speed of the Wolf enchantment?")) {
			enchants.put("wolfspeed", new EnchantmentWolfSpeed(wolfspeed, wolfspeedShaped));
			LOG.info("Registered Speed of the Wolf enchantment");
		}
		else {
			LOG.info("Didn't register Speed of the Wolf enchantment");
		}
		if (getEnchantEnabled("jagged", true, "Register the DoT Jagged enchantment?")) {
			enchants.put("jagged", new EnchantmentJagged(jagged, jaggedMaxLvl > 10 ? 10 : jaggedMaxLvl < 1 ? 1 : jaggedMaxLvl, jaggedBaseDuration, jaggedFactor));
			LOG.info("Registered Jagged enchantment");
		}
		else {
			LOG.info("Didn't register Jagged enchantment");
		}
	}
	protected static int getEnchantID(String enchName, int def, String cmnt) {
		int ID = cfg.integer("enchant.id", enchName, def, cmnt);
		if (ID >= Enchantment.enchantmentsList.length) {
			throw new IndexOutOfBoundsException("Enchantment ID cannot be higher than " + (Enchantment.enchantmentsList.length - 1) + "!");
		}
		return ID;
	}
	protected static boolean getEnchantEnabled(String enchName, boolean def, String cmnt) {
		return cfg.bool("enchant.enable", enchName, def, cmnt);
	}
	public static final Enchantment getEnch(String enchName) {
		if (enchants.containsKey(enchName)) {
			return enchants.get(enchName);
		}
		return null;
	}
	public static final void extendLevelLocalization() {
		// From 11 to 50! Woo~!
		LanguageRegistry.instance().addStringLocalization("enchantment.level.11", "XI");
		LanguageRegistry.instance().addStringLocalization("enchantment.level.12", "XII");
		LanguageRegistry.instance().addStringLocalization("enchantment.level.13", "XIII");
		LanguageRegistry.instance().addStringLocalization("enchantment.level.14", "XIV");
		LanguageRegistry.instance().addStringLocalization("enchantment.level.15", "XV");
		LanguageRegistry.instance().addStringLocalization("enchantment.level.16", "XVI");
		LanguageRegistry.instance().addStringLocalization("enchantment.level.17", "XVII");
		LanguageRegistry.instance().addStringLocalization("enchantment.level.18", "XVIII");
		LanguageRegistry.instance().addStringLocalization("enchantment.level.19", "XIX");
		LanguageRegistry.instance().addStringLocalization("enchantment.level.20", "XX");
		LanguageRegistry.instance().addStringLocalization("enchantment.level.21", "XXI");
		LanguageRegistry.instance().addStringLocalization("enchantment.level.22", "XXII");
		LanguageRegistry.instance().addStringLocalization("enchantment.level.23", "XXIII");
		LanguageRegistry.instance().addStringLocalization("enchantment.level.24", "XXIV");
		LanguageRegistry.instance().addStringLocalization("enchantment.level.25", "XXV");
		LanguageRegistry.instance().addStringLocalization("enchantment.level.26", "XXVI");
		LanguageRegistry.instance().addStringLocalization("enchantment.level.27", "XXVII");
		LanguageRegistry.instance().addStringLocalization("enchantment.level.28", "XXVIII");
		LanguageRegistry.instance().addStringLocalization("enchantment.level.29", "XXIX");
		LanguageRegistry.instance().addStringLocalization("enchantment.level.30", "XXX");
		LanguageRegistry.instance().addStringLocalization("enchantment.level.31", "XXXI");
		LanguageRegistry.instance().addStringLocalization("enchantment.level.32", "XXXII");
		LanguageRegistry.instance().addStringLocalization("enchantment.level.33", "XXXIII");
		LanguageRegistry.instance().addStringLocalization("enchantment.level.34", "XXXIV");
		LanguageRegistry.instance().addStringLocalization("enchantment.level.35", "XXXV");
		LanguageRegistry.instance().addStringLocalization("enchantment.level.36", "XXXVI");
		LanguageRegistry.instance().addStringLocalization("enchantment.level.37", "XXXVII");
		LanguageRegistry.instance().addStringLocalization("enchantment.level.38", "XXXVIII");
		LanguageRegistry.instance().addStringLocalization("enchantment.level.39", "XXXIX");
		LanguageRegistry.instance().addStringLocalization("enchantment.level.40", "XL");
		LanguageRegistry.instance().addStringLocalization("enchantment.level.41", "XLI");
		LanguageRegistry.instance().addStringLocalization("enchantment.level.42", "XLII");
		LanguageRegistry.instance().addStringLocalization("enchantment.level.43", "XLIII");
		LanguageRegistry.instance().addStringLocalization("enchantment.level.44", "XLIV");
		LanguageRegistry.instance().addStringLocalization("enchantment.level.45", "XLV");
		LanguageRegistry.instance().addStringLocalization("enchantment.level.46", "XLVI");
		LanguageRegistry.instance().addStringLocalization("enchantment.level.47", "XLVII");
		LanguageRegistry.instance().addStringLocalization("enchantment.level.48", "XLVIII");
		LanguageRegistry.instance().addStringLocalization("enchantment.level.49", "XLIX");
		LanguageRegistry.instance().addStringLocalization("enchantment.level.50", "L");
	}
}
