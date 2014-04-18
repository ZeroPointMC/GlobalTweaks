package zeropoint.minecraft.core.util.manip;


import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;


/**
 * Small yet useful utility for manipulating {@link ItemStack} objects
 * 
 * @author Zero Point
 */
public class ItemStackHelper {
	/**
	 * Prepare an {@link ItemStack} for manipulation
	 * 
	 * @param broken
	 *            - the ItemStack to prepare
	 * @return a copy of the original ItemStack, ready for use
	 */
	public static final ItemStack fix(ItemStack broken) {
		ItemStack fixed = broken.copy();
		if (fixed.stackTagCompound == null) {
			fixed.stackTagCompound = new NBTTagCompound("tag");
		}
		return fixed;
	}
	/**
	 * Apply an enchantment to an {@link ItemStack}
	 * 
	 * @param blah
	 *            - the ItemStack to enchant
	 * @param ench
	 *            - the {@link Enchantment} to apply
	 * @return an enchanted copy of the original ItemStack
	 */
	public static final ItemStack enchant(ItemStack blah, Enchantment ench) {
		return enchant(blah, ench, 1);
	}
	/**
	 * Apply an enchantment to an {@link ItemStack}
	 * 
	 * @param blah
	 *            - the ItemStack to enchant
	 * @param ench
	 *            - the id of the {@link Enchantment} to apply
	 * @return an enchanted copy of the original ItemStack
	 */
	public static final ItemStack enchant(ItemStack blah, int ench) {
		return enchant(blah, ench, 1);
	}
	/**
	 * Apply an enchantment to an {@link ItemStack}
	 * 
	 * @param blah
	 *            - the ItemStack to enchant
	 * @param ench
	 *            - the {@link Enchantment} to apply
	 * @param level
	 *            - the level of the enchantment to apply
	 * @return an enchanted copy of the original ItemStack
	 */
	public static final ItemStack enchant(ItemStack blah, Enchantment ench, int level) {
		return enchant(blah, ench.effectId, level);
	}
	/**
	 * Apply an enchantment to an {@link ItemStack}
	 * 
	 * @param blah
	 *            - the ItemStack to enchant
	 * @param ench
	 *            - the id of the {@link Enchantment} to apply
	 * @param level
	 *            - the level of the enchantment to apply
	 * @return an enchanted copy of the original ItemStack
	 */
	public static final ItemStack enchant(ItemStack blah, int ench, int level) {
		ItemStack whee = fix(blah);
		EnchantHelper.addEnchantment(ench, level, whee);
		return whee;
	}
}
