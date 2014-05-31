package zeropoint.minecraft.enchant.ench;


import java.util.Random;

import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import zeropoint.core.math.MathUtil;
import zeropoint.minecraft.core.ench.CraftedEnchantment;
import zeropoint.minecraft.core.util.ChatMsg;
import zeropoint.minecraft.core.util.manip.EnchantHelper;


@SuppressWarnings("javadoc")
public class EnchantmentDecapitate extends CraftedEnchantment {
	private static final Random r = new Random();
	public EnchantmentDecapitate(int id) {
		super(id, 2, EnumEnchantmentType.weapon);
	}
	// Section: configuration
	@Override
	public int getMaxLevel() {
		return 5;
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
		return (stack.getItem() instanceof ItemSword) || (stack.getItem() instanceof ItemAxe);
	}
	// Section: initialization
	@Override
	public final void registerLocalization() {
		this.setName("gtweaks.decapitate");
		this.setHumanReadableName("Decapitate");
	}
	// Section: enchantment via crafting
	@Override
	public int getEnchLevel(InventoryCrafting grid) {
		return findSkulls(grid);
	}
	private static int findSkulls(InventoryCrafting grid) {
		int foundSkulls = 0;
		for (int i = 0; i < grid.getSizeInventory(); i++ ) {
			ItemStack stack = grid.getStackInSlot(i);
			if ((stack != null) && (stack.itemID == Item.skull.itemID)) {
				if (stack.getItemDamage() == 1) {
					foundSkulls += 1;
				}
			}
		}
		return foundSkulls;
	}
	@Override
	public int findTarget(InventoryCrafting grid) {
		for (int i = 0; i < grid.getSizeInventory(); i++ ) {
			ItemStack stack = grid.getStackInSlot(i);
			if (stack != null) {
				Item item = stack.getItem();
				if ((item instanceof ItemSword) || (item instanceof ItemAxe)) {
					return i;
				}
			}
		}
		return -1;
	}
	@Override
	public ItemStack getCraftingResult(InventoryCrafting grid) {
		int skulls = findSkulls(grid);
		int toolSlot = this.findTarget(grid);
		if ((toolSlot < 0) || (skulls <= 0) || (skulls > 5)) {
			return null;
		}
		ItemStack output = grid.getStackInSlot(toolSlot).copy();
		if (EnchantHelper.getEnchantmentLevel(this.effectId, output) <= skulls) {
			return null;
		}
		EnchantHelper.setEnchantment(this.effectId, skulls, output);
		return output;
	}
	@Override
	public int getRecipeSize() {
		return 6;
	}
	// Section: enchantment event handling
	@ForgeSubscribe
	public void entityKilled(LivingDeathEvent e) {
		if (e.entityLiving.worldObj.isRemote) {
			return;
		}
		if (e.source == null) {
			return;
		}
		if (e.source.getEntity() == null) {
			return;
		}
		if ( !(e.source.getEntity() instanceof EntityPlayer)) {
			return;
		}
		EntityPlayer player = (EntityPlayer) e.source.getEntity();
		ItemStack held = player.getCurrentEquippedItem();
		if (held == null) {
			return;
		}
		int level = EnchantHelper.getEnchantmentLevel(this.effectId, held);
		int maxLevel = this.getMaxLevel();
		if (level == 0) {
			return;
		}
		ItemStack skull = makeSkull(e.entityLiving);
		if (skull == null) {
			return;
		}
		if ((level == 10) || (MathUtil.randInt(r, 1, (maxLevel * 2) + 1) < level)) {
			e.entityLiving.entityDropItem(skull, 0.5F);
			new ChatMsg("Your weapon cleanly separates the beast's head from its body!").send(player);
		}
	}
	protected static ItemStack makeSkull(Entity target) {
		ItemStack skull = new ItemStack(Item.skull);
		skull.stackTagCompound = new NBTTagCompound("tag");
		if (target instanceof EntitySkeleton) {
			skull.setItemDamage(((EntitySkeleton) target).getSkeletonType());
		}
		else if (target instanceof EntityCreeper) {
			skull.setItemDamage(4);
		}
		else if (target instanceof EntityZombie) {
			skull.setItemDamage(2);
		}
		else if (target instanceof EntityPlayer) {
			skull.setItemDamage(3);
			skull.stackTagCompound.setString("ExtraType", ((EntityPlayer) target).username);
		}
		else {
			return null;
		}
		// skull.stackTagCompound.setString("ExtraType", skull.stackTagCompound.getString("ExtraType"));
		// skull.stackTagCompound.setByte("SkullType", (byte) (skull.getItemDamage()));
		return skull;
	}
}
