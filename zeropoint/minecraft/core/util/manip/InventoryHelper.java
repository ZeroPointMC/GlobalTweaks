package zeropoint.minecraft.core.util.manip;


import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;


/**
 * Inventory manipulation utilities
 * 
 * @author Zero Point
 */
public class InventoryHelper {
	/**
	 * @param inv
	 *            - the inventory to manipulate
	 * @param id
	 *            - the ID of the item to consume
	 * @return <code>true</code> if the item was consumed, <code>false</code> otherwise
	 */
	public static boolean consumeItem(IInventory inv, int id) {
		return consumeItem(inv, id, 0);
	}
	/**
	 * @param inv
	 *            - the inventory to manipulate
	 * @param id
	 *            - the ID of the item to consume
	 * @param meta
	 *            - the metadata of the item to consume
	 * @return <code>true</code> if the item was consumed, <code>false</code> otherwise
	 */
	public static boolean consumeItem(IInventory inv, int id, int meta) {
		return consumeItem(inv, id, meta, 1);
	}
	/**
	 * @param inv
	 *            - the inventory to manipulate
	 * @param id
	 *            - the ID of the item to consume
	 * @param meta
	 *            - the metadata of the item to consume
	 * @param count
	 *            - the number of the given item to consume
	 * @return <code>true</code> if the item was consumed, <code>false</code> otherwise
	 */
	public static boolean consumeItem(IInventory inv, int id, int meta, int count) {
		for (int slotNum = 0; slotNum < inv.getSizeInventory(); slotNum++ ) {
			ItemStack slot = inv.getStackInSlot(slotNum);
			if (slot == null) {
				continue;
			}
			if ((slot.itemID == id) && (slot.getItemDamage() == meta)) {
				if (slot.stackSize >= count) {
					slot.stackSize -= count;
					if (slot.stackSize <= 0) {
						inv.setInventorySlotContents(slotNum, null);
					}
					return true;
				}
				return false;
			}
		}
		return false;
	}
}
