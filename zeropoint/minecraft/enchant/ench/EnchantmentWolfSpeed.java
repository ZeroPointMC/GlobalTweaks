package zeropoint.minecraft.enchant.ench;


import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import zeropoint.minecraft.core.ench.CraftedEnchantment;
import zeropoint.minecraft.core.util.Log;
import zeropoint.minecraft.core.util.manip.EnchantHelper;
import zeropoint.minecraft.core.util.manip.ItemStackHelper;
import zeropoint.minecraft.enchant.GTEnchant;


@SuppressWarnings("javadoc")
public class EnchantmentWolfSpeed extends CraftedEnchantment {
	public final boolean shaped;
	public EnchantmentWolfSpeed(int id) {
		this(id, false);
	}
	public EnchantmentWolfSpeed(int id, boolean shape) {
		super(id, 2, EnumEnchantmentType.armor_feet);
		this.shaped = shape;
		this.hasLevels = false;
	}
	@Override
	public ItemStack getCraftingResult(InventoryCrafting grid) {
		int targetSlot = this.findTarget(grid);
		int level = this.getEnchLevel(grid);
		if ((targetSlot > -1) && (level > 0)) {
			ItemStack output = ItemStackHelper.fix(grid.getStackInSlot(targetSlot));
			return output;
		}
		return null;
	}
	@Override
	public int getRecipeSize() {
		return 9;
	}
	@Override
	public int findTarget(InventoryCrafting grid) {
		if ( !this.shaped) {
			for (int i = 0; i < grid.getSizeInventory(); i++ ) {
				ItemStack slot = grid.getStackInSlot(i);
				if (slot == null) {
					continue;
				}
				if (slot.getItem() instanceof ItemArmor) {
					ItemArmor item = (ItemArmor) slot.getItem();
					if (item == null) {
						Log.getLogger(GTEnchant.name).warning("Null Item in non-null ItemStack - slot " + i + "!");
						continue;
					}
					if (item.armorType == 3) {
						return i;
					}
				}
			}
		}
		else {
			try {
				Item target = grid.getStackInSlot(1).getItem();
				if (target instanceof ItemArmor) {
					return (((ItemArmor) target).armorType == 3 ? 1 : -1);
				}
			}
			catch (NullPointerException ex) {}
		}
		return -1;
	}
	@Override
	public int getEnchLevel(InventoryCrafting grid) {
		if ( !this.shaped) {
			int sugar = 0;
			int feather = 0;
			int redstone = 0;
			int reqSugar = 4;
			int reqFeather = 1;
			int reqRedstone = 3;
			for (int i = 0; i < grid.getSizeInventory(); i++ ) {
				ItemStack slot = grid.getStackInSlot(i);
				if ((slot == null) || (slot.getItem() == null)) {
					continue;
				}
				if (slot.itemID == Item.sugar.itemID) {
					sugar += 1;
				}
				else if (slot.itemID == Item.feather.itemID) {
					feather += 1;
				}
				else if (slot.itemID == Item.redstone.itemID) {
					redstone += 1;
				}
			}
			if ((sugar == reqSugar) && (feather == reqFeather) && (redstone == reqRedstone)) {
				return 1;
			}
		}
		else {
			int sugar = Item.sugar.itemID;
			int redstone = Item.redstone.itemID;
			int feather = Item.feather.itemID;
			try {
				if (grid.getStackInSlot(0).itemID == sugar) {
					if (grid.getStackInSlot(2).itemID == sugar) {
						if (grid.getStackInSlot(3).itemID == sugar) {
							if (grid.getStackInSlot(4).itemID == redstone) {
								if (grid.getStackInSlot(5).itemID == sugar) {
									if (grid.getStackInSlot(6).itemID == redstone) {
										if (grid.getStackInSlot(7).itemID == feather) {
											if (grid.getStackInSlot(8).itemID == redstone) {
												return 1;
											}
										}
									}
								}
							}
						}
					}
				}
			}
			catch (NullPointerException ex) {}
		}
		return 0;
	}
	@Override
	public boolean canApply(ItemStack stack) {
		return (stack.getItem() instanceof ItemArmor) && (((ItemArmor) (stack.getItem())).armorType == 3);
	}
	@Override
	public void registerLocalization() {
		this.setName("gtweaks.wolfspeed");
		this.setHumanReadableName("Speed of the Wolf");
	}
	@ForgeSubscribe
	public void onLivingUpdate(LivingUpdateEvent e) {
		EntityLivingBase entity = e.entityLiving;
		final int level = EnchantHelper.getEnchantmentLevel(this.effectId, entity.getCurrentItemOrArmor(1));
		if (level > 0) {
			entity.addPotionEffect(new PotionEffect(Potion.moveSpeed.id, 10, level + 1, true));
		}
	}
}
