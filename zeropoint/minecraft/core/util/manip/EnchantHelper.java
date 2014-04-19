package zeropoint.minecraft.core.util.manip;


import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.item.ItemStack;


/**
 * An improvement of Minecraft's {@link EnchantmentHelper} class
 * 
 * @author Zero Point
 */
public class EnchantHelper extends EnchantmentHelper {
	/**
	 * Set multiple enchantments to specific level, but only if they don't already exist on the item
	 * 
	 * @param enchants
	 *            - the enchantments to add
	 * @param stack
	 *            - the {@link ItemStack} to manipulate
	 */
	public static void addEnchantments(Map<Integer, Integer> enchants, ItemStack stack) {
		Map<Integer, Integer> existing = getEnchantments(stack);
		for (int id : enchants.keySet()) {
			if ( !existing.containsKey(id)) {
				existing.put(id, enchants.get(id));
			}
		}
		setEnchantments(existing, stack);
	}
	/**
	 * Set multiple enchantments to specific level, but only if they already exist on the item
	 * 
	 * @param enchants
	 *            - the enchantments to add
	 * @param stack
	 *            - the {@link ItemStack} to manipulate
	 */
	public static void updateExistingEnchantments(Map<Integer, Integer> enchants, ItemStack stack) {
		Map<Integer, Integer> existing = getEnchantments(stack);
		for (int id : enchants.keySet()) {
			if (existing.containsKey(id)) {
				existing.put(id, enchants.get(id));
			}
		}
		setEnchantments(existing, stack);
	}
	/**
	 * Force multiple enchantments to specific level
	 * 
	 * @param enchants
	 *            - the enchantments to add
	 * @param stack
	 *            - the {@link ItemStack} to manipulate
	 */
	public static void updateAllEnchantments(Map<Integer, Integer> enchants, ItemStack stack) {
		Map<Integer, Integer> existing = getEnchantments(stack);
		existing.putAll(enchants);
		setEnchantments(existing, stack);
	}
	/**
	 * Remove multiple enchantments
	 * 
	 * @param enchants
	 *            - the List of enchantment IDs to remove
	 * @param stack
	 *            - the {@link ItemStack} to manipulate
	 */
	public static void removeEnchantments(Collection<Integer> enchants, ItemStack stack) {
		Map<Integer, Integer> existing = getEnchantments(stack);
		for (int id : enchants) {
			if (existing.containsKey(id)) {
				existing.remove(id);
			}
		}
		setEnchantments(existing, stack);
	}
	/**
	 * Set a certain enchantment to a certain level, providing it does not yet exist on the stack
	 * 
	 * @param id
	 *            - the ID of the enchantment to set
	 * @param lvl
	 *            - the level to set
	 * @param stack
	 *            - the {@link ItemStack} to manipulate
	 */
	public static void addEnchantment(int id, int lvl, ItemStack stack) {
		Map<Integer, Integer> ench = new HashMap<Integer, Integer>();
		ench.put(id, lvl);
		addEnchantments(ench, stack);
	}
	/**
	 * Set a certain enchantment to a certain level, provided it already exists
	 * 
	 * @param id
	 *            - the ID of the enchantment to set
	 * @param lvl
	 *            - the level to set
	 * @param stack
	 *            - the {@link ItemStack} to manipulate
	 */
	public static void updateExistingEnchantment(int id, int lvl, ItemStack stack) {
		Map<Integer, Integer> ench = new HashMap<Integer, Integer>();
		ench.put(id, lvl);
		updateExistingEnchantments(ench, stack);
	}
	/**
	 * Force a certain enchantment to a certain level
	 * 
	 * @param id
	 *            - the ID of the enchantment to set
	 * @param lvl
	 *            - the level to set
	 * @param stack
	 *            - the {@link ItemStack} to manipulate
	 */
	public static void setEnchantment(int id, int lvl, ItemStack stack) {
		Map<Integer, Integer> ench = new HashMap<Integer, Integer>();
		ench.put(id, lvl);
		updateAllEnchantments(ench, stack);
	}
	/**
	 * Remove a specific enchantment
	 * 
	 * @param id
	 *            - the ID of the enchantment to remove
	 * @param stack
	 *            - the {@link ItemStack} to manipulate
	 */
	public static void removeEnchantment(int id, ItemStack stack) {
		List<Integer> ench = new ArrayList<Integer>();
		ench.add(id);
		removeEnchantments(ench, stack);
	}
	/**
	 * Remove all enchantments
	 * 
	 * @param stack
	 *            - the {@link ItemStack} to manipulate
	 */
	public static void unenchant(ItemStack stack) {
		stack.getTagCompound().removeTag("ench");
	}
}
