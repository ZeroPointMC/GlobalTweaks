package zeropoint.minecraft.core.ench;


import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.world.World;
import cpw.mods.fml.common.registry.GameRegistry;


// TODO: write Javadoc
public abstract class CraftedEnchantment extends BasicEnchantment implements IRecipe {
	protected CraftedEnchantment(int par1, int par2, EnumEnchantmentType par3EnumEnchantmentType) {
		super(par1, par2, par3EnumEnchantmentType);
	}
	@Override
	public final boolean canApplyAtEnchantingTable(ItemStack stack) {
		return false;
	}
	public void registerRecipe() {
		GameRegistry.addRecipe(this);
	}
	@Override
	public void initialize() {
		super.initialize();
		registerRecipe();
	}
	public abstract int findTarget(InventoryCrafting grid);
	public abstract int getEnchLevel(InventoryCrafting grid);
	public boolean matches(InventoryCrafting grid, World world) {
		return getCraftingResult(grid) == null ? false : true;
	}
	public ItemStack getRecipeOutput() {
		return null;
	}
}
