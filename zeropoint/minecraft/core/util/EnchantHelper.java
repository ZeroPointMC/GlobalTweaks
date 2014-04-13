package zeropoint.minecraft.core.util;


import java.util.HashMap;
import java.util.Map;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.item.ItemStack;


public class EnchantHelper extends EnchantmentHelper {
	// Section: multi-enchantment modifications
	public static void addEnchantments(Map<Integer, Integer> enchants, ItemStack stack) {
		Map<Integer, Integer> existing = getEnchantments(stack);
		for (int id : enchants.keySet()) {
			if ( !existing.containsKey(id)) {
				existing.put(id, enchants.get(id));
			}
		}
		setEnchantments(existing, stack);
	}
	public static void updateExistingEnchantments(Map<Integer, Integer> enchants, ItemStack stack) {
		Map<Integer, Integer> existing = getEnchantments(stack);
		for (int id : enchants.keySet()) {
			if (existing.containsKey(id)) {
				existing.put(id, enchants.get(id));
			}
		}
		setEnchantments(existing, stack);
	}
	public static void updateAllEnchantments(Map<Integer, Integer> enchants, ItemStack stack) {
		Map<Integer, Integer> existing = getEnchantments(stack);
		existing.putAll(enchants);
		setEnchantments(existing, stack);
	}
	public static void removeEnchantments(Map<Integer, Integer> enchants, ItemStack stack) {
		Map<Integer, Integer> existing = getEnchantments(stack);
		for (int id : enchants.keySet()) {
			if (existing.containsKey(id)) {
				existing.remove(id);
			}
		}
		setEnchantments(existing, stack);
	}
	// Section: single enchantment modifications
	public static void addEnchantment(int id, int lvl, ItemStack stack) {
		Map<Integer, Integer> ench = new HashMap<Integer, Integer>();
		ench.put(id, lvl);
		addEnchantments(ench, stack);
	}
	public static void updateExistingEnchantment(int id, int lvl, ItemStack stack) {
		Map<Integer, Integer> ench = new HashMap<Integer, Integer>();
		ench.put(id, lvl);
		updateExistingEnchantments(ench, stack);
	}
	public static void setEnchantment(int id, int lvl, ItemStack stack) {
		Map<Integer, Integer> ench = new HashMap<Integer, Integer>();
		ench.put(id, lvl);
		updateAllEnchantments(ench, stack);
	}
	public static void removeEnchantment(int id, int lvl, ItemStack stack) {
		Map<Integer, Integer> ench = new HashMap<Integer, Integer>();
		ench.put(id, lvl);
		removeEnchantments(ench, stack);
	}
	// Section: other enchantment operations
	public static void unenchant(ItemStack stack) {
		stack.getTagCompound().removeTag("ench");
	}
}
