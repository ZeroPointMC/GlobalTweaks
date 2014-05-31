package zeropoint.minecraft.core.util.manip;


import java.util.UUID;

import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.BaseAttributeMap;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;


/**
 * Player manipulation and examination utilities
 * 
 * @author Zero Point
 */
public class PlayerHelper {
	/**
	 * The name of the AttributeModifer applied when increasing max health
	 */
	public static final String MODIFIER_NAME = "GT_command_health";
	/**
	 * The UUID of the AttributeModifier applied when increasing max health
	 */
	public static final UUID MODIFIER_UUID = UUID.fromString("5B71B8ED-34AD-4386-930B-FFC9C091A974");
	/**
	 * The maximum amount of absorption to add
	 */
	public static final int MAX_OVERHEAL = 80;
	/**
	 * The amount of health to assume is the maximum, when unable to determine based on a player object
	 */
	public static final int DEFAULT_MAX_HEALTH_BASE = 20;
	/**
	 * @param player
	 *            - the {@link EntityPlayer} to get the maximum health for
	 * @return the max health + absorption for the player
	 */
	public static final float hpMax(EntityPlayer player) {
		try {
			return player.getMaxHealth() + MAX_OVERHEAL;
		}
		catch (NullPointerException e) {
			return DEFAULT_MAX_HEALTH_BASE + MAX_OVERHEAL;
		}
	}
	/**
	 * @param player
	 *            - the player to set the health of
	 * @param health
	 *            - the amount of health (optionally with absorption) to add
	 * @param overheal
	 *            - <code>true</code> to increase max health (if needed), <code>false</code> to apply absorption instead
	 * @see #setPlayerHealth(EntityPlayer, int)
	 * @see #setPlayerHealthWithOverheal(EntityPlayer, int)
	 */
	public final static void setPlayerHealth(EntityPlayer player, int health, boolean overheal) {
		if (overheal) {
			setPlayerHealthWithOverheal(player, health);
		}
		else {
			setPlayerHealth(player, health);
		}
	}
	/**
	 * @param player
	 *            - the player to set the health of
	 * @param health
	 *            - the amount of health (optionally with absorption) to add
	 * @see #hpMax(EntityPlayer player)
	 */
	public final static void setPlayerHealth(EntityPlayer player, int health) {
		final int amax = (int) player.getMaxHealth();
		final int smax = (int) hpMax(player);
		if (health > smax) {
			health = smax;
		}
		else if (health < 0) {
			player.setHealth(0);
			player.setDead();
			return;
		}
		if (health > amax) {
			int absorb = health - amax;
			player.setAbsorptionAmount(absorb);
			player.setHealth(amax);
		}
		else {
			player.setHealth(health);
			player.setAbsorptionAmount(0);
		}
	}
	/**
	 * <b><i>This function is experimental.</i></b><br/>
	 * <b>It may not work properly.</b><br/>
	 * <br/>
	 * You are advised to instead call the method {@link #setPlayerMaxHealth(EntityPlayer, int)} followed
	 * by {@link #setPlayerHealth(EntityPlayer, int)}.
	 * 
	 * @param player
	 *            - the player to set the health of
	 * @param health
	 *            - the amount of health (optionally with absorption) to add
	 */
	public final static void setPlayerHealthWithOverheal(EntityPlayer player, int health) {
		final int max = (int) player.getMaxHealth();
		if (health < 0) {
			player.setHealth(0);
			player.setDead();
			return;
		}
		if (health > max) {
			setPlayerMaxHealth(player, health);
		}
		player.setHealth(health);
	}
	/**
	 * @param player
	 *            - the player to set the maximum health of
	 * @param newMax
	 *            - the new maximum health for the player
	 */
	public static final void setPlayerMaxHealth(EntityPlayer player, int newMax) {
		int amnt = newMax - 20;
		BaseAttributeMap attrs = player.getAttributeMap();
		Multimap<String, AttributeModifier> modMap = HashMultimap.<String, AttributeModifier>create();
		AttributeModifier modifier = new AttributeModifier(MODIFIER_UUID, MODIFIER_NAME, amnt, 0);
		modMap.put(SharedMonsterAttributes.maxHealth.getAttributeUnlocalizedName(), modifier);
		if (amnt != 0) {
			attrs.applyAttributeModifiers(modMap);
		}
		else {
			attrs.removeAttributeModifiers(modMap);
		}
	}
	/**
	 * @param player
	 *            - the {@link EntityPlayer} to manipulate
	 * @param food
	 *            - the food and saturation level for the player
	 */
	public final static void setPlayerFood(EntityPlayer player, int food) {
		player.getFoodStats().setFoodLevel(Math.min(food, 20));
		player.getFoodStats().setFoodSaturationLevel(Math.min(food, 20));
	}
	/**
	 * Set the metadata of the player's held item to 0
	 * 
	 * @param player
	 *            - the {@link EntityPlayer} to manipulate
	 */
	public final static void repairHeld(EntityPlayer player) {
		try {
			player.getCurrentEquippedItem().setItemDamage(0);
		}
		catch (NullPointerException e) {}
	}
	/**
	 * Set the metadata of the player's hotbar to 0
	 * 
	 * @param player
	 *            - the {@link EntityPlayer} to manipulate
	 */
	public final static void repairHotbar(EntityPlayer player) {
		InventoryPlayer inv = player.inventory;
		for (int i = 0; i < inv.getHotbarSize(); i++ ) {
			try {
				inv.getStackInSlot(i).setItemDamage(0);
			}
			catch (Exception e) {}
		}
	}
	/**
	 * Set the metadata of the player's chest-sized inventory to 0
	 * 
	 * @param player
	 *            - the {@link EntityPlayer} to manipulate
	 */
	public final static void repairInventory(EntityPlayer player) {
		InventoryPlayer inv = player.inventory;
		for (int i = inv.getHotbarSize(); i < inv.getSizeInventory(); i++ ) {
			try {
				inv.getStackInSlot(i).setItemDamage(0);
			}
			catch (Exception e) {}
		}
	}
	/**
	 * Set the metadata of the player's armour to 0
	 * 
	 * @param player
	 *            - the {@link EntityPlayer} to manipulate
	 */
	public final static void repairArmour(EntityPlayer player) {
		for (int i = 0; i < 4; i++ ) {
			try {
				player.getCurrentArmor(i).setItemDamage(0);
			}
			catch (Exception e) {}
		}
	}
	/**
	 * Set the metadata of the player's entire inventory (hotbar, chest inventory, armour) to 0
	 * 
	 * @param player
	 *            - the {@link EntityPlayer} to manipulate
	 */
	public final static void repairAll(EntityPlayer player) {
		repairHotbar(player);
		repairInventory(player);
		repairArmour(player);
	}
	/**
	 * Check if the player is holding an item
	 * 
	 * @param player
	 *            - the player to examine
	 * @return <code>true</code> if the player is holding anything
	 */
	public static final boolean isHolding(EntityPlayer player) {
		return (player != null) && (player.getCurrentEquippedItem() != null);
	}
	/**
	 * Check if the player is holding a specific item
	 * 
	 * @param player
	 *            - the player to examine
	 * @param testFor
	 *            - the Item to check for
	 * @return <code>true</code> if the player is holding an item with the given ID
	 */
	public static final boolean isHolding(EntityPlayer player, Item testFor) {
		return isHolding(player) && (player.getCurrentEquippedItem().itemID == testFor.itemID);
	}
	/**
	 * Check if the player is holding a specific stack of items
	 * 
	 * @param player
	 *            - the player to examine
	 * @param testFor
	 *            - the ItemStack to check for
	 * @return <code>true</code> if the player is holding an item with the same ID as the given ItemStack
	 * @deprecated Does not compare any other aspects of the item stack - only the ID.
	 */
	@Deprecated
	public static final boolean isHolding(EntityPlayer player, ItemStack testFor) {
		if ( !isHolding(player)) {
			return false;
		}
		ItemStack held = player.getCurrentEquippedItem();
		return ItemStackHelper.equal(held, testFor);
	}
}
