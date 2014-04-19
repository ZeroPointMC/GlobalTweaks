package zeropoint.minecraft.core.util.manip;


import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;


/**
 * Player manipulation and examination utilities
 * 
 * @author Zero Point
 */
public class PlayerHelper {
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
	 * @see #hpMax(EntityPlayer player)
	 */
	public final static void setPlayerHealth(EntityPlayer player, int health) {
		final int amax = (int) player.getMaxHealth();
		final int smax = (int) hpMax(player);
		if (health > smax) {
			health = smax;
		}
		else if (health < 1) {
			health = 1;
		}
		if (health > amax) {
			player.setHealth(amax);
			player.setAbsorptionAmount(health - amax);
		}
		else {
			player.setHealth(health);
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
	 * @param player
	 *            - the player to examine
	 * @return <code>true</code> if the player is holding anything
	 */
	public static final boolean isHolding(EntityPlayer player) {
		return (player != null) && (player.getCurrentEquippedItem() != null);
	}
	/**
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
	 * @param player
	 *            - the player to examine
	 * @param testFor
	 *            - the ItemStack to check for
	 * @return <code>true</code> if the player is holding an item with the same ID as the given ItemStack
	 * @deprecated Does not compare any other aspects of the item stack - only the ID.
	 */
	@Deprecated
	public static final boolean isHolding(EntityPlayer player, ItemStack testFor) {
		return isHolding(player, testFor.getItem());
	}
}
