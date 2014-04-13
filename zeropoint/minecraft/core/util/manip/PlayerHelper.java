package zeropoint.minecraft.core.util.manip;


import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;


public class PlayerHelper {
	public static final int MAX_OVERHEAL = 80;
	public static final int DEFAULT_MAX_HEALTH_BASE = 20;
	public static final float hpMax(EntityPlayer player) {
		try {
			return player.getMaxHealth() + MAX_OVERHEAL;
		}
		catch (NullPointerException e) {
			return DEFAULT_MAX_HEALTH_BASE + MAX_OVERHEAL;
		}
	}
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
	public final static void setPlayerFood(EntityPlayer player, int food) {
		player.getFoodStats().setFoodLevel(Math.min(food, 20));
		player.getFoodStats().setFoodSaturationLevel(Math.min(food, 20));
	}
	public final static void repairHeld(EntityPlayer player) {
		try {
			player.getCurrentEquippedItem().setItemDamage(0);
		}
		catch (NullPointerException e) {}
	}
	public final static void repairHotbar(EntityPlayer player) {
		InventoryPlayer inv = player.inventory;
		for (int i = 0; i < inv.getHotbarSize(); i++ ) {
			try {
				inv.getStackInSlot(i).setItemDamage(0);
			}
			catch (Exception e) {}
		}
	}
	public final static void repairInventory(EntityPlayer player) {
		InventoryPlayer inv = player.inventory;
		for (int i = inv.getHotbarSize(); i < inv.getSizeInventory(); i++ ) {
			try {
				inv.getStackInSlot(i).setItemDamage(0);
			}
			catch (Exception e) {}
		}
	}
	public final static void repairArmour(EntityPlayer player) {
		for (int i = 0; i < 4; i++ ) {
			try {
				player.getCurrentArmor(i).setItemDamage(0);
			}
			catch (Exception e) {}
		}
	}
	public final static void repairAll(EntityPlayer player) {
		repairHotbar(player);
		repairInventory(player);
		repairArmour(player);
	}
	public static final boolean isHolding(EntityPlayer player) {
		return (player != null) && (player.getCurrentEquippedItem() != null);
	}
	public static final boolean isHolding(EntityPlayer player, Item testFor) {
		return isHolding(player) && (player.getCurrentEquippedItem().itemID == testFor.itemID);
	}
	@Deprecated
	public static final boolean isHolding(EntityPlayer player, ItemStack testFor) {
		return isHolding(player, testFor.getItem());
	}
}
