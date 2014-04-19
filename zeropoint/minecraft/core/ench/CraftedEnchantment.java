package zeropoint.minecraft.core.ench;


import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.world.World;
import cpw.mods.fml.common.registry.GameRegistry;


/**
 * An enchantment that can be acquired through crafting should extend this class
 * 
 * @author Zero Point
 */
public abstract class CraftedEnchantment extends BasicEnchantment implements IRecipe {
	/**
	 * @param id
	 *            - the enchantment ID
	 * @param weight
	 *            - the weight of the enchantment; the chance for it to be applied at a table
	 * @param type
	 *            - the {@link EnumEnchantmentType} of the enchantment
	 */
	protected CraftedEnchantment(int id, int weight, EnumEnchantmentType type) {
		super(id, weight, type);
	}
	@Override
	public final boolean canApplyAtEnchantingTable(ItemStack stack) {
		return false;
	}
	/**
	 * Add the recipe for obtaining the enchantment to the game
	 */
	public void registerRecipe() {
		GameRegistry.addRecipe(this);
	}
	@Override
	public void initialize() {
		super.initialize();
		registerRecipe();
	}
	/**
	 * Find the tool that should be enchanted
	 * 
	 * @param grid
	 *            - the {@link InventoryCrafting} object representing the crafting grid
	 * @return the slot number of the enchantment target
	 */
	public abstract int findTarget(InventoryCrafting grid);
	/**
	 * Get the level of the enchantment to apply
	 * 
	 * @param grid
	 *            - the {@link InventoryCrafting} object representing the crafting grid
	 * @return the strength for the enchantment
	 */
	public abstract int getEnchLevel(InventoryCrafting grid);
	public boolean matches(InventoryCrafting grid, World world) {
		return getCraftingResult(grid) == null ? false : true;
	}
	public ItemStack getRecipeOutput() {
		return null;
	}
}
