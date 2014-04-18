package zeropoint.minecraft.sonic;


import java.util.logging.Logger;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import zeropoint.core.math.MathUtil;
import zeropoint.minecraft.core.Config;
import zeropoint.minecraft.core.GTCore;
import zeropoint.minecraft.core.util.Log;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.network.NetworkMod;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;


@SuppressWarnings("javadoc")
@Mod(modid = GTSonic.modid, name = GTSonic.name, version = GTSonic.version, dependencies = "required-after:gtweaks-core")
@NetworkMod(clientSideRequired = true, serverSideRequired = false)
public class GTSonic {
	public static final String modid = "gtweaks-sonic";
	public static final String name = "GlobalTweaks|Sonic";
	public static final String version = "release";
	public static double speedBoost;
	public static int maxCreatureHealPercent;
	protected SonicScrewdriver sonic11;
	protected SonicScrewdriver sonic10;
	protected SonicScrewdriver sonic8;
	protected SonicScrewdriver sonic4;
	protected SonicBlaster blaster;
	protected VortexManipulator vortexManip;
	protected static int redstoneLampActiveId;
	protected static int redstoneLampInactiveId;
	private static Config cfg;
	private static final Logger LOG = Log.getLogger(name);
	@SuppressWarnings("unused")
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
		this.sonic11 = new SonicScrewdriver(cfg.item("sonic", "sonicEleven", 26042, "The eleventh doctor's screwdriver"), SonicScrewdriver.SonicType.ELEVEN);
		this.sonic10 = new SonicScrewdriver(cfg.item("sonic", "sonicTen", 26043, "The tenth doctor's screwdriver"), SonicScrewdriver.SonicType.TENTH);
		this.sonic8 = new SonicScrewdriver(cfg.item("sonic", "sonicEight", 26044, "The eighth doctor's screwdriver"), SonicScrewdriver.SonicType.EIGHT);
		this.sonic4 = new SonicScrewdriver(cfg.item("sonic", "sonicFour", 26045, "The fourth doctor's screwdriver"), SonicScrewdriver.SonicType.FOUR);
		this.blaster = new SonicBlaster(cfg.item("sonic", "sonicBlaster", 26046, "The sonic blaster (or 'squareness gun') used by Captain Jack Harkness"));
		this.vortexManip = new VortexManipulator(cfg.item("sonic", "vorteManipulator", 26047, "The vortex manipulator used by Captain Jack Harkness"));
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
		GameRegistry.registerItem(this.sonic11, "sonicScrewdriverEleven");
		LanguageRegistry.addName(this.sonic11, "Sonic Screwdriver");
		GameRegistry.registerItem(this.sonic10, "sonicScrewdriverTen");
		LanguageRegistry.addName(this.sonic10, "Sonic Screwdriver");
		GameRegistry.registerItem(this.sonic8, "sonicScrewdriverEight");
		LanguageRegistry.addName(this.sonic8, "Sonic Screwdriver");
		GameRegistry.registerItem(this.sonic4, "sonicScrewdriverFour");
		LanguageRegistry.addName(this.sonic4, "Sonic Screwdriver");
		GameRegistry.registerItem(this.blaster, "sonicBlaster");
		LanguageRegistry.addName(this.blaster, "Sonic Blaster");
		GameRegistry.registerItem(this.vortexManip, "vortexManipulator");
		LanguageRegistry.addName(this.vortexManip, "Vortex Manipulator");
		LOG.info("Items/blocks registered");
	}
	protected void addRecipes() {
		ItemStack sonicProbe11 = new ItemStack(this.sonic11);
		ItemStack sonicProbe10 = new ItemStack(this.sonic10);
		ItemStack sonicProbe8 = new ItemStack(this.sonic8);
		ItemStack sonicProbe4 = new ItemStack(this.sonic4);
		ItemStack sonicBlaster = new ItemStack(this.blaster);
		ItemStack emerald = new ItemStack(Item.emerald);
		ItemStack diamond = new ItemStack(Item.diamond);
		ItemStack gold = new ItemStack(Item.ingotGold);
		ItemStack iron = new ItemStack(Item.ingotIron);
		ItemStack wood = new ItemStack(Block.planks);
		ItemStack redstone = new ItemStack(Item.redstone);
		GameRegistry.addRecipe(sonicProbe11, "e  ", " g ", "  i", 'e', emerald, 'g', gold, 'i', iron);
		GameRegistry.addRecipe(sonicProbe10, "d  ", " g ", "  i", 'd', diamond, 'g', gold, 'i', iron);
		GameRegistry.addRecipe(sonicProbe8, "g  ", " i ", "  i", 'g', gold, 'i', iron);
		GameRegistry.addRecipe(sonicProbe4, "d  ", " r ", "  w", 'd', diamond, 'r', redstone, 'w', wood);
		GameRegistry.addRecipe(sonicBlaster, "dgr", " ir", " pr", 'd', diamond, 'g', gold, 'r', redstone, 'i', iron, 'p', wood);
		LOG.info("Recipes registered");
	}
}
