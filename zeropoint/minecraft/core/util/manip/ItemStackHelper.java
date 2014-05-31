package zeropoint.minecraft.core.util.manip;


import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.ItemSkull;
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
		return fix(broken, true);
	}
	/**
	 * Prepare an {@link ItemStack} for manipulation
	 * 
	 * @param broken
	 *            - the ItemStack to prepare
	 * @param copy
	 *            - <code>true</code> to operate on a copy of the given ItemStack, <code>false</code> to affect the original
	 * @return an ItemStack, ready for use
	 */
	public static final ItemStack fix(ItemStack broken, boolean copy) {
		ItemStack fixed = copy ? broken.copy() : broken;
		if (fixed.stackTagCompound == null) {
			fixed.stackTagCompound = new NBTTagCompound("tag");
		}
		if (fixed.getItem() instanceof ItemSkull) {
			fixed.stackTagCompound.setString("ExtraType", fixed.stackTagCompound.getString("ExtraType"));
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
	/**
	 * Check whether two {@link ItemStack} objects are considered equal
	 * 
	 * @param a
	 *            - an ItemStack to check
	 * @param b
	 *            - an ItemStack to check
	 * @return <code>true</code> if the stacks are equal, <code>false</code> otherwise
	 */
	public static final boolean equal(ItemStack a, ItemStack b) {
		if (a == b) {
			return true;
		}
		if (a.itemID == b.itemID) {
			if (a.stackSize == b.stackSize) {
				if (a.getItemDamage() == b.getItemDamage()) {
					if (a.hasTagCompound() && b.hasTagCompound()) {
						if (a.stackTagCompound.equals(b.stackTagCompound)) {
							return true;
						}
					}
					else if (a.hasTagCompound() == b.hasTagCompound()) {
						return true;
					}
				}
			}
		}
		return false;
	}
}
