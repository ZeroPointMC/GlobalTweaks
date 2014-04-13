package zeropoint.minecraft.enchant;


import java.util.HashMap;
import java.util.Map;

import net.minecraft.enchantment.Enchantment;
import zeropoint.minecraft.core.Config;
import zeropoint.minecraft.core.GTCore;
import zeropoint.minecraft.enchant.ench.EnchantmentDecapitate;
import zeropoint.minecraft.enchant.ench.EnchantmentGodslayer;
import zeropoint.minecraft.enchant.ench.EnchantmentWolfSpeed;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.network.NetworkMod;


@Mod(modid = GTEnchant.modid, name = GTEnchant.name, version = GTEnchant.version, dependencies = "required-after:gtweaks-core")
@NetworkMod(clientSideRequired = true, serverSideRequired = false)
public class GTEnchant {
	public static final String modid = "gtweaks-enchant";
	public static final String name = "GlobalTweaks|Enchant";
	public static final String version = "semi-beta";
	private static final Map<String, Enchantment> enchants = new HashMap<String, Enchantment>();
	private static Config cfg;
	@EventHandler
	public void init(FMLInitializationEvent event) {
		cfg = GTCore.getConfig();
		if ( !GTCore.Modules.enchantEnabled()) {
			return;
		}
		int decap = getEnchantID("decapitate", 10, "Force mobs to drop their heads more often");
		int slayer = getEnchantID("godslayer", 11, "Deal MASSIVE damage to boss mobs");
		int wolfspeed = getEnchantID("speedOfTheWolf", 12, "(Semi-)early-game enchant to increase movement speed");
		boolean wolfspeedShaped = cfg.bool("enchant.misc", "speedOfTheWolf.shapedRecipes", true, "{[Sugar / Boots / Sugar] [Sugar / Redstone / Sugar] [Redstone - Feather - Redstone]} if true");
		int slayerBossDamage = cfg.integer("enchant.misc", "godslayer.damage.boss", 200, "Damage dealt to boss mobs each hit with a Godslayer-enchanted weapon");
		int slayerDamage = cfg.integer("enchant.misc", "godslayer.damage.normal", 10, "Damage dealt to non-boss mobs with a Godslayer-enchanted weapons");
		boolean slayerShaped = cfg.bool("enchant.misc", "godslayer.shapedRecipes", true, "Sword in the middle, dragon egg on top, nether stars all around (if false, ONLY position is ignored!)");
		boolean slayerHardMode = cfg.bool("enchant.misc", "godslayer.hardMode", true, "Require a dragon egg to get the enchantment? (If false, another nether star will be needed instead)");
		if (getEnchantEnabled("decapitate", true, "Register the Decapitation enchantment?")) {
			enchants.put("decapitate", new EnchantmentDecapitate(decap));
		}
		if (getEnchantEnabled("godslayer", false, "Register the completely OP Godslayer enchantment?")) {
			enchants.put("godslayer", new EnchantmentGodslayer(slayer, slayerBossDamage, slayerDamage, slayerShaped, slayerHardMode));
		}
		if (getEnchantEnabled("speedOfTheWolf", false, "Register the Direwolf20-inspired Speed of the Wolf enchantment?")) {
			enchants.put("wolfspeed", new EnchantmentWolfSpeed(wolfspeed, wolfspeedShaped));
		}
	}
	protected static int getEnchantID(String enchName, int def, String cmnt) {
		int ID = cfg.integer("enchant.id", enchName, def, cmnt);
		if (ID == 0) {
			throw new IndexOutOfBoundsException("Enchantment ID cannot be zero!");
		}
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
}
