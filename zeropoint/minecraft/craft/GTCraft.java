package zeropoint.minecraft.craft;


import java.util.logging.Logger;

import net.minecraft.block.Block;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.world.World;
import zeropoint.minecraft.core.Config;
import zeropoint.minecraft.core.GTCore;
import zeropoint.minecraft.core.util.Log;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.registry.GameRegistry;


@SuppressWarnings("javadoc")
@Mod(modid = GTCraft.modid, name = GTCraft.name, version = GTCraft.version, dependencies = "required-after:gtweaks-core")
public class GTCraft {
	public static final String modid = "gtweaks-craft";
	public static final String name = "GlobalTweaks|Craft";
	public static final String version = "release";
	private static Config cfg;
	private static final Logger LOG = Log.getLogger(name);
	private static boolean enableHook = false;
	private static boolean enableDebug = false;
	@SuppressWarnings("unused")
	@EventHandler
	public static void init(FMLInitializationEvent event) {
		cfg = GTCore.getConfig();
		if ( !GTCore.Modules.craftEnabled()) {
			LOG.warning("Module disabled!");
			return;
		}
		enableHook = cfg.bool("recipes.debug", "hook", false, "Output the contents of the crafting grid to the console EVERY TIME it changes.\nFor ANY AND EVERY CRAFTING GRID.\nTurning this on will redefine 'console spam', so be careful.");
		enableDebug = cfg.bool("recipes.debug", "debug", false, "Allow external mods to output the contents of a crafting grid on demand?");
		Recipes.hook();
		Recipes.bedrock();
		Recipes.endPortalFrame();
		Recipes.netherStar();
	}
	public static final boolean allowDebug() {
		return enableDebug;
	}
	public static final void debug(InventoryCrafting grid) {
		if (allowDebug()) {
			Recipes.Hook.debug(grid);
		}
	}
	private static final class Recipes {
		private static final class Hook implements IRecipe {
			private static final Logger l = Log.getLogger(name + " DebugHook");
			@SuppressWarnings("synthetic-access")
			public static void debug(InventoryCrafting grid) {
				if ( !GTCraft.enableDebug && !GTCraft.enableHook) {
					return;
				}
				l.info("BEGIN CRAFTING DEBUG HOOK OUTPUT");
				for (int i = 0; i < grid.getSizeInventory(); i++ ) {
					String msg = "";
					ItemStack slot = grid.getStackInSlot(i);
					msg = String.valueOf(slot);
					l.info("Slot " + i + ": " + msg);
				}
				l.info("END CRAFTING DEBUG HOOK OUTPUT");
			}
			@Override
			public boolean matches(InventoryCrafting grid, World world) {
				debug(grid);
				return false;
			}
			@Override
			public ItemStack getCraftingResult(InventoryCrafting grid) {
				return null;
			}
			@Override
			public int getRecipeSize() {
				return 9;
			}
			@Override
			public ItemStack getRecipeOutput() {
				return new ItemStack(Item.appleGold);
			}
		}
		@SuppressWarnings("synthetic-access")
		public static void hook() {
			if ( !GTCraft.enableHook) {
				return;
			}
			GameRegistry.addRecipe(new Hook());
			Hook.l.severe("Crafting manager hooked!");
			Hook.l.severe("Console will be spammed with crafting grid contents on change!");
			Hook.l.severe("This should be disabled as soon as possible!");
		}
		@SuppressWarnings("synthetic-access")
		public static void bedrock() {
			if (cfg.bool("recipes", "bedrock", false, "Enable crafting of bedrock from end stone (x4), obsidian (x4), and diamond block (x1)?")) {
				LOG.info("Registering bedrock crafting recipe");
				GameRegistry.addRecipe(new ItemStack(Block.bedrock, 4), "eoe", "odo", "eoe", 'e', new ItemStack(Block.whiteStone), 'o', new ItemStack(Block.obsidian), 'd', new ItemStack(Block.blockDiamond));
			}
			else {
				LOG.info("Bedrock crafting recipe disabled");
			}
		}
		@SuppressWarnings("synthetic-access")
		public static void endPortalFrame() {
			if (cfg.bool("recipes", "endPortalFrame", false, "Enable crafting of end portal frames from obsidian (x4), diamond (x2), and end stone (x1)?")) {
				LOG.info("Registering end portal frame crafting recipe");
				GameRegistry.addRecipe(new ItemStack(Block.endPortalFrame), "oeo", "dsd", "oeo", 'o', new ItemStack(Block.obsidian), 'e', new ItemStack(Item.eyeOfEnder), 'd', new ItemStack(Item.diamond), 's', new ItemStack(Block.whiteStone));
			}
			else {
				LOG.info("End portal frame crafting recipe disabled");
			}
		}
		@SuppressWarnings("synthetic-access")
		public static void netherStar() {
			if (cfg.bool("recipes", "netherStar", false, "Enable crafting of nether stars from wither skull (x2), diamond (x2), gold block (x4), and obsidian (x1)?")) {
				LOG.info("Registering nether star crafting recipe");
				ItemStack gold = new ItemStack(Block.blockGold);
				ItemStack diamond = new ItemStack(Item.diamond);
				ItemStack obsidian = new ItemStack(Block.obsidian);
				ItemStack witherSkull = new ItemStack(Item.skull, 1, 1);
				GameRegistry.addRecipe(new ItemStack(Item.netherStar), "gsg", "dod", "gsg", 'g', gold, 's', witherSkull, 'd', diamond, 'o', obsidian);
			}
			else {
				LOG.info("Nether star crafting recipe disabled");
			}
		}
	}
}
