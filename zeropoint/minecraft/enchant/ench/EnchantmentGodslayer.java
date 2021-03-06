package zeropoint.minecraft.enchant.ench;


import java.util.logging.Logger;

import net.minecraft.block.Block;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.boss.IBossDisplayData;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.util.EntityDamageSource;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import zeropoint.minecraft.core.ench.CraftedEnchantment;
import zeropoint.minecraft.core.util.Log;
import zeropoint.minecraft.core.util.manip.EnchantHelper;
import zeropoint.minecraft.core.util.manip.WorldHelper;
import zeropoint.minecraft.enchant.GTEnchant;


@SuppressWarnings("javadoc")
public class EnchantmentGodslayer extends CraftedEnchantment {
	public static final float DEFAULT_DAMAGE_BASE = 10F;
	public static final float DEFAULT_DAMAGE_BOSS_FACTOR = 15F;
	public static final boolean DEF_SHAPE = true;
	public static final boolean DEF_HARD = true;
	public final float bossDamage;
	public final float damage;
	public final boolean useShaped;
	public final boolean hardMode;
	public EnchantmentGodslayer(int id) {
		this(id, DEFAULT_DAMAGE_BASE);
	}
	public EnchantmentGodslayer(int id, float damageBase) {
		this(id, damageBase, DEFAULT_DAMAGE_BOSS_FACTOR);
	}
	public EnchantmentGodslayer(int id, float damageBase, float bossFactor) {
		this(id, damageBase, bossFactor, DEF_SHAPE);
	}
	public EnchantmentGodslayer(int id, float damageBase, float bossFactor, boolean shaped) {
		this(id, damageBase, bossFactor, shaped, DEF_HARD);
	}
	public EnchantmentGodslayer(int id, float damageBase, float bossFactor, boolean shaped, boolean hard) {
		super(id, 2, EnumEnchantmentType.weapon);
		this.damage = damageBase;
		this.bossDamage = damageBase * bossFactor;
		this.useShaped = shaped;
		this.hardMode = hard;
		this.hasLevels = false;
	}
	// Section: configuration
	@Override
	public int getMaxLevel() {
		return 1;
	}
	@Override
	public boolean isAllowedOnBooks() {
		return false;
	}
	@Override
	public boolean canApply(ItemStack stack) {
		if (stack.getItem() == null) {
			return false;
		}
		return stack.getItem() instanceof ItemSword;
	}
	// Section: enchantment via crafting
	@Override
	public ItemStack getCraftingResult(InventoryCrafting grid) {
		int toolSlot = this.findTarget(grid);
		int lvl = this.getEnchLevel(grid);
		if ((toolSlot < 0) || (lvl < 1)) {
			return null;
		}
		ItemStack output = grid.getStackInSlot(toolSlot).copy();
		EnchantHelper.setEnchantment(this.effectId, lvl, output);
		return output;
	}
	@Override
	public int getRecipeSize() {
		return 9;
	}
	public static int findToolShaped(InventoryCrafting grid) {
		ItemStack stack = grid.getStackInSlot(4);
		if (stack == null) {
			return -1;
		}
		if (stack.getItem() instanceof ItemSword) {
			return 4;
		}
		return -1;
	}
	public int getEnchLevelShaped(InventoryCrafting grid) {
		if (this.hardMode) {
			return getEnchLevelShapedWithEgg(grid);
		}
		return getEnchLevelShapedWithoutEgg(grid);
	}
	public int getEnchLevelShapeless(InventoryCrafting grid) {
		if (this.hardMode) {
			return getEnchLevelShapelessWithEgg(grid);
		}
		return getEnchLevelShapelessWithoutEgg(grid);
	}
	public static int getEnchLevelShapedWithoutEgg(InventoryCrafting grid) {
		int stars = 0;
		for (int i = 0; i <= 9; i++ ) {
			if (i != 4) {
				if (grid.getStackInSlot(i) == null) {
					continue;
				}
				int id = grid.getStackInSlot(i).itemID;
				if ((id == Item.netherStar.itemID)) {
					stars += 1;
				}
			}
		}
		return ((stars >= 8) ? 1 : 0);
	}
	public static int getEnchLevelShapedWithEgg(InventoryCrafting grid) {
		boolean egg = false;
		int stars = 0;
		for (int i = 0; i <= 9; i++ ) {
			if (i != 4) {
				if (grid.getStackInSlot(i) == null) {
					continue;
				}
				int id = grid.getStackInSlot(i).itemID;
				if ((id == Block.dragonEgg.blockID) && (i == 1)) {
					egg = true;
				}
				else if ((id == Item.netherStar.itemID) && (i != 1)) {
					stars += 1;
				}
			}
		}
		return (egg && (stars >= 7) ? 1 : 0);
	}
	public static int findToolShapeless(InventoryCrafting grid) {
		for (int i = 0; i < grid.getSizeInventory(); i++ ) {
			ItemStack stack = grid.getStackInSlot(i);
			if (stack != null) {
				Item item = stack.getItem();
				if (item instanceof ItemSword) {
					return i;
				}
			}
		}
		return -1;
	}
	public static int getEnchLevelShapelessWithoutEgg(InventoryCrafting grid) {
		int stars = 0;
		for (int i = 0; i < grid.getSizeInventory(); i++ ) {
			ItemStack stack = grid.getStackInSlot(i);
			if (stack != null) {
				if (stack.itemID == Item.netherStar.itemID) {
					stars += 1;
				}
			}
		}
		if ((stars < 8)) {
			return 0;
		}
		return 1;
	}
	public static int getEnchLevelShapelessWithEgg(InventoryCrafting grid) {
		boolean egg = false;
		int stars = 0;
		for (int i = 0; i < grid.getSizeInventory(); i++ ) {
			ItemStack stack = grid.getStackInSlot(i);
			if (stack != null) {
				if (stack.itemID == Block.dragonEgg.blockID) {
					egg = true;
				}
				else if (stack.itemID == Item.netherStar.itemID) {
					stars += 1;
				}
			}
		}
		if ( !egg || (stars < 7)) {
			return 0;
		}
		return 1;
	}
	@Override
	public int findTarget(InventoryCrafting grid) {
		if (this.useShaped) {
			return findToolShaped(grid);
		}
		return findToolShapeless(grid);
	}
	@Override
	public int getEnchLevel(InventoryCrafting grid) {
		if (this.useShaped) {
			return this.getEnchLevelShaped(grid);
		}
		return this.getEnchLevelShapeless(grid);
	}
	@Override
	public void registerLocalization() {
		this.setName("gtweaks.godslayer");
		this.setHumanReadableName("Godslayer");
	}
	// Section: enchantment event handling
	@ForgeSubscribe
	public void entityStruck(AttackEntityEvent e) {
		try {
			if (e.entityPlayer.worldObj.isRemote) {
				return;
			}
			if (e.entityPlayer.getCurrentEquippedItem() == null) {
				return;
			}
			if (EnchantHelper.getEnchantmentLevel(this.effectId, e.entityPlayer.getCurrentEquippedItem()) < 1) {
				return;
			}
			EntityLiving target;
			try {
				target = (EntityLiving) e.target;
			}
			catch (ClassCastException ex) {
				return;
			}
			if ((target == null) || target.isEntityInvulnerable()) {
				return;
			}
			EntityDamageSource src = new EntityDamageSource("godslayer", e.entityPlayer);
			if (target instanceof IBossDisplayData) {
				target.attackEntityFrom(src, this.bossDamage);
				WorldHelper.zap(target);
			}
			else {
				target.attackEntityFrom(src, this.damage);
			}
		}
		catch (Exception ex) {
			Logger LOG = Log.getLogger(GTEnchant.name);
			LOG.warning("An exception was detected while handling an AttackEntityEvent:");
			ex.printStackTrace();
		}
	}
}
