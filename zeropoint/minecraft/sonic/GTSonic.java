package zeropoint.minecraft.sonic;


import java.util.logging.Logger;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import zeropoint.core.math.MathUtil;
import zeropoint.minecraft.core.Config;
import zeropoint.minecraft.core.GTCore;
import zeropoint.minecraft.core.util.Log;
import zeropoint.minecraft.sonic.SonicScrewdriver.SonicColour;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.network.NetworkMod;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;


@Mod(modid = GTSonic.modid, name = GTSonic.name, version = GTSonic.version, dependencies = "required-after:gtweaks-core")
@NetworkMod(clientSideRequired = true, serverSideRequired = false)
public class GTSonic {
	public static final String modid = "gtweaks-sonic";
	public static final String name = "GlobalTweaks|Sonic";
	public static final String version = "release";
	public static double speedBoost;
	public static int maxCreatureHealPercent;
	protected SonicScrewdriver sonicGreen;
	protected SonicScrewdriver sonicBlue;
	protected static int redstoneLampActiveId;
	protected static int redstoneLampInactiveId;
	private static Config cfg;
	private static final Logger LOG = Log.getLogger(name);
	@EventHandler
	public void init(FMLInitializationEvent event) {
		cfg = GTCore.getConfig();
		if ( !GTCore.Modules.sonicEnabled()) {
			LOG.warning("Module disabled!");
			return;
		}
		redstoneLampActiveId = cfg.block("sonicRedstoneLamp", 2742, "The ID of the always-on redstone lamp");
		redstoneLampInactiveId = cfg.block("sonicRedstoneLampOff", 2743, "The ID of the always-off redstone lamp");
		speedBoost = MathUtil.bound(cfg.decimal("sonic", "speedBoostPercentage", 0.5, "Must be between 0 and 1, inclusive"), 0, 1);
		maxCreatureHealPercent = MathUtil.bound(cfg.integer("sonic", "maxCreatureHealPercent", 75, "Must be between 50 and 90, inclusive"), 50, 90);
		sonicGreen = new SonicScrewdriver(cfg.item("sonicGreen", 26042, "The eleventh doctor's screwdriver"), SonicColour.GREEN);
		sonicBlue = new SonicScrewdriver(cfg.item("sonicBlue", 26043, "The tenth doctor's screwdriver"), SonicColour.BLUE);
		registerItems();
		addRecipes();
	}
	protected void registerItems() {
		Block redstoneLampActive = new BlockRedstoneLamp(redstoneLampActiveId, true);
		Block redstoneLampInactive = new BlockRedstoneLamp(redstoneLampInactiveId, false);
		GameRegistry.registerBlock(redstoneLampActive, "sonicRedstoneLamp");
		LanguageRegistry.addName(redstoneLampActive, "Sonic'd Redstone Lamp");
		GameRegistry.registerBlock(redstoneLampInactive, "sonicRedstoneLampOff");
		LanguageRegistry.addName(redstoneLampInactive, "Sonic'd Redstone Lamp");
		GameRegistry.registerItem(sonicGreen, "sonicScrewdriverGreen");
		LanguageRegistry.addName(sonicGreen, "Sonic Screwdriver");
		GameRegistry.registerItem(sonicBlue, "sonicScrewdriverBlue");
		LanguageRegistry.addName(sonicBlue, "Sonic Screwdriver");
		LOG.info("Items/blocks registered");
	}
	protected void addRecipes() {
		ItemStack sonicProbeGreen = new ItemStack(sonicGreen);
		ItemStack sonicProbeBlue = new ItemStack(sonicBlue);
		ItemStack emerald = new ItemStack(Item.emerald);
		ItemStack diamond = new ItemStack(Item.diamond);
		ItemStack gold = new ItemStack(Item.ingotGold);
		ItemStack iron = new ItemStack(Item.ingotIron);
		GameRegistry.addRecipe(sonicProbeGreen, "e  ", " g ", "  i", 'e', emerald, 'g', gold, 'i', iron);
		GameRegistry.addRecipe(sonicProbeBlue, "d  ", " g ", "  i", 'd', diamond, 'g', gold, 'i', iron);
		LOG.info("Recipes registered");
	}
}
