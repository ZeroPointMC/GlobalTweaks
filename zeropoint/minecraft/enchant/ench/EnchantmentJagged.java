// May 1, 2014 11:01:19 AM
package zeropoint.minecraft.enchant.ench;


import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.potion.PotionEffect;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import zeropoint.minecraft.core.ench.CraftedEnchantment;
import zeropoint.minecraft.core.util.manip.EnchantHelper;
import zeropoint.minecraft.effect.GTEffect;


@SuppressWarnings("javadoc")
public class EnchantmentJagged extends CraftedEnchantment {
	public final int maxLevel;
	public final int baseDuration;
	public final int levelFactor;
	public EnchantmentJagged(int id, int maxLvl, int baseLength, int lvlFactor) {
		super(id, 2, EnumEnchantmentType.weapon);
		this.maxLevel = maxLvl;
		this.baseDuration = baseLength;
		this.levelFactor = lvlFactor;
	}
	@Override
	public ItemStack getCraftingResult(InventoryCrafting grid) {
		final int targetSlot = this.findTarget(grid);
		final int level = this.getEnchLevel(grid);
		if ((level > 0) && (targetSlot > -1)) {
			ItemStack output = grid.getStackInSlot(targetSlot).copy();
			EnchantHelper.setEnchantment(this.effectId, level, output);
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
		try {
			return grid.getStackInSlot(4).getItem() instanceof ItemSword ? 4 : -1;
		}
		catch (NullPointerException e) {
			return -1;
		}
	}
	@Override
	public int getEnchLevel(InventoryCrafting grid) {
		try {
			// This stupidly long line checks to ensure that the recipe is valid:
			// FSF
			// S S
			// FSF
			// where F = flint and S = slimeball
			if ((this.findTarget(grid) < 0) || (grid.getStackInSlot(0).itemID != Item.flint.itemID) || (grid.getStackInSlot(1).itemID != Item.slimeBall.itemID) || (grid.getStackInSlot(2).itemID != Item.flint.itemID) || (grid.getStackInSlot(3).itemID != Item.slimeBall.itemID) || (grid.getStackInSlot(5).itemID != Item.slimeBall.itemID) || (grid.getStackInSlot(6).itemID != Item.flint.itemID) || (grid.getStackInSlot(7).itemID != Item.slimeBall.itemID) || (grid.getStackInSlot(8).itemID != Item.flint.itemID)) {
				return -1;
			}
			ItemStack target = grid.getStackInSlot(this.findTarget(grid));
			if (EnchantHelper.getEnchantmentLevel(this.effectId, target) >= this.maxLevel) {
				return -1;
			}
			return EnchantHelper.getEnchantmentLevel(this.effectId, target) + 1;
		}
		catch (Exception e) {
			if ( !(e instanceof NullPointerException)) {
				e.printStackTrace();
			}
			return -1;
		}
	}
	@Override
	public boolean canApply(ItemStack stack) {
		return stack.getItem() instanceof ItemSword;
	}
	@Override
	public void registerLocalization() {
		this.setName("gtweaks.jagged");
		this.setHumanReadableName("Jagged");
	}
	// Section: enchantment event handling
	@ForgeSubscribe
	public void entityStruck(AttackEntityEvent e) {
		if ( !(e.target instanceof EntityLiving)) {
			return;
		}
		final EntityLiving target = (EntityLiving) e.target;
		final EntityPlayer player = e.entityPlayer;
		final ItemStack held = player.getCurrentEquippedItem();
		if (held == null) {
			return;
		}
		final int level = EnchantHelper.getEnchantmentLevel(this.effectId, held);
		if (level < 1) {
			return;
		}
		PotionEffect bleed = new PotionEffect(GTEffect.bleedID(), this.baseDuration + (level * this.levelFactor), level, true);
		target.addPotionEffect(bleed);
	}
}
