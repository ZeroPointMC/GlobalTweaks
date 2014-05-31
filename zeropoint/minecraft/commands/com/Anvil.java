// May 4, 2014 1:15:39 PM
package zeropoint.minecraft.commands.com;


import java.util.Iterator;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import net.minecraft.command.ICommandSender;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.ContainerRepair;
import net.minecraft.inventory.ContainerRepairINNER2;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryCraftResult;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemEnchantedBook;
import net.minecraft.item.ItemStack;
import net.minecraft.network.packet.Packet100OpenWindow;
import net.minecraft.world.World;

import org.apache.commons.lang3.StringUtils;

import zeropoint.core.logger.LoggingLevel;
import zeropoint.minecraft.core.GTBaseCommand;
import zeropoint.minecraft.core.util.Log;


@SuppressWarnings("javadoc")
public class Anvil extends GTBaseCommand {
	// protected static final Logger log = Log.getLogger("/anvil");
	public static class GlobalAnvil extends ContainerRepair {
		// I reflected ContainerRepairINNER1, but I needed to change methods for this one.
		// It's ContainerRepairINNER2, only fixed.
		public static class InnerRepairContainerOutput extends ContainerRepairINNER2 {
			protected final World world;
			protected final GlobalAnvil repairContainer;
			public InnerRepairContainerOutput(GlobalAnvil cr, IInventory par2IInventory, int par3, int par4, int par5, World par6World) {
				super(cr, par2IInventory, par3, par4, par5, par6World, (int) cr.thePlayer.posX, (int) cr.thePlayer.posY, (int) cr.thePlayer.posZ);
				final Logger log = Log.getLogger("/anvil ContainerRepairINNER2.<init>(...) " + (par6World.isRemote ? "CLIENT" : "SERVER") + "");
				log.log(LoggingLevel.TRACE, "Constructed new InnerRepairContainerOutput object");
				this.repairContainer = cr;
				this.world = par6World;
			}
			@Override
			public boolean isItemValid(ItemStack par1ItemStack) {
				return false;
			}
			@Override
			public boolean canTakeStack(EntityPlayer par1EntityPlayer) {
				return this.getHasStack();
			}
			@Override
			public void onPickupFromSlot(EntityPlayer par1EntityPlayer, ItemStack par2ItemStack) {
				final Logger log = Log.getLogger("/anvil ContainerRepairINNER2.onPickupFromSlot(...) " + (this.world.isRemote ? "CLIENT" : "SERVER") + "");
				log.log(LoggingLevel.TRACE, "Stack removed from slot");
				this.repairContainer.inputSlots.setInventorySlotContents(0, (ItemStack) null);
				if (this.repairContainer.stackSizeToBeUsedInRepair > 0) {
					ItemStack itemstack1 = this.repairContainer.inputSlots.getStackInSlot(1);
					if ((itemstack1 != null) && (itemstack1.stackSize > this.repairContainer.stackSizeToBeUsedInRepair)) {
						itemstack1.stackSize -= this.repairContainer.stackSizeToBeUsedInRepair;
						this.repairContainer.inputSlots.setInventorySlotContents(1, itemstack1);
					}
					else {
						this.repairContainer.inputSlots.setInventorySlotContents(1, (ItemStack) null);
					}
				}
				else {
					this.repairContainer.inputSlots.setInventorySlotContents(1, (ItemStack) null);
				}
				this.repairContainer.maximumCost = 0;
				if ( !this.world.isRemote) {
					this.world.playAuxSFX(1021, (int) this.repairContainer.thePlayer.posX, (int) this.repairContainer.thePlayer.posY, (int) this.repairContainer.thePlayer.posZ, 0);
				}
			}
		}
		public GlobalAnvil(InventoryPlayer inv, World world, int x, int y, int z, EntityPlayer player) {
			super(inv, world, x, y, z, player);
			final Logger log = Log.getLogger("/anvil GlobalAnvil.<init>(...) " + (player.worldObj.isRemote ? "CLIENT" : "SERVER") + "");
			this.outputSlot = new InventoryCraftResult();
			log.log(LoggingLevel.TRACE, "Constructed new GlobalAnvil");
			log.log(LoggingLevel.TRACE, "Adding anvil slots");
			this.addSlotToContainer(new Slot(this.inputSlots, 0, 27, 47));
			this.addSlotToContainer(new Slot(this.inputSlots, 1, 76, 47));
			this.addSlotToContainer(new InnerRepairContainerOutput(this, this.outputSlot, 2, 134, 47, world));
			int l;
			log.log(LoggingLevel.TRACE, "Preparing player inventory slots");
			for (l = 0; l < 3; ++l) {
				for (int i1 = 0; i1 < 9; ++i1) {
					this.addSlotToContainer(new Slot(player.inventory, i1 + (l * 9) + 9, 8 + (i1 * 18), 84 + (l * 18)));
				}
			}
			log.log(LoggingLevel.TRACE, "Preparing hotbar slots");
			for (l = 0; l < 9; ++l) {
				log.log(LoggingLevel.TRACE, "Preparing hotbar slot " + l);
				this.addSlotToContainer(new Slot(player.inventory, l, 8 + (l * 18), 142));
				log.log(LoggingLevel.TRACE, "Prepared hotbar slot " + l);
			}
			log.log(LoggingLevel.TRACE, "Prepared hotbar slots");
		}
		@Override
		public boolean canInteractWith(EntityPlayer par1EntityPlayer) {
			return true;
		}
		@Override
		public void updateRepairOutput() {
			final Logger log = Log.getLogger("/anvil GlobalAnvil.updateRepairOutput() " + (this.thePlayer.worldObj.isRemote ? "CLIENT" : "SERVER") + "");
			log.log(LoggingLevel.TRACE, "Updating output");
			ItemStack itemstack = this.inputSlots.getStackInSlot(0);
			this.maximumCost = 0;
			int i = 0;
			byte b0 = 0;
			int j = 0;
			if (itemstack == null) {
				this.outputSlot.setInventorySlotContents(0, (ItemStack) null);
				this.maximumCost = 0;
			}
			else {
				ItemStack itemstack1 = itemstack.copy();
				ItemStack itemstack2 = this.inputSlots.getStackInSlot(1);
				Map<Integer, Integer> map = EnchantmentHelper.getEnchantments(itemstack1);
				boolean flag = false;
				int k = b0 + itemstack.getRepairCost() + (itemstack2 == null ? 0 : itemstack2.getRepairCost());
				this.stackSizeToBeUsedInRepair = 0;
				int l;
				int i1;
				int j1;
				int k1;
				int l1;
				Iterator<Integer> iterator;
				Enchantment enchantment;
				if (itemstack2 != null) {
					flag = (itemstack2.itemID == Item.enchantedBook.itemID) && (Item.enchantedBook.func_92110_g(itemstack2).tagCount() > 0);
					if (itemstack1.isItemStackDamageable() && Item.itemsList[itemstack1.itemID].getIsRepairable(itemstack, itemstack2)) {
						l = Math.min(itemstack1.getItemDamageForDisplay(), itemstack1.getMaxDamage() / 4);
						if (l <= 0) {
							this.outputSlot.setInventorySlotContents(0, (ItemStack) null);
							this.maximumCost = 0;
							return;
						}
						for (i1 = 0; (l > 0) && (i1 < itemstack2.stackSize); ++i1) {
							j1 = itemstack1.getItemDamageForDisplay() - l;
							itemstack1.setItemDamage(j1);
							i += Math.max(1, l / 100) + map.size();
							l = Math.min(itemstack1.getItemDamageForDisplay(), itemstack1.getMaxDamage() / 4);
						}
						this.stackSizeToBeUsedInRepair = i1;
					}
					else {
						if ( !flag && ((itemstack1.itemID != itemstack2.itemID) || !itemstack1.isItemStackDamageable())) {
							this.outputSlot.setInventorySlotContents(0, (ItemStack) null);
							this.maximumCost = 0;
							return;
						}
						if (itemstack1.isItemStackDamageable() && !flag) {
							l = itemstack.getMaxDamage() - itemstack.getItemDamageForDisplay();
							i1 = itemstack2.getMaxDamage() - itemstack2.getItemDamageForDisplay();
							j1 = i1 + ((itemstack1.getMaxDamage() * 12) / 100);
							int i2 = l + j1;
							k1 = itemstack1.getMaxDamage() - i2;
							if (k1 < 0) {
								k1 = 0;
							}
							if (k1 < itemstack1.getItemDamage()) {
								itemstack1.setItemDamage(k1);
								i += Math.max(1, j1 / 100);
							}
						}
						Map<Integer, Integer> map1 = EnchantmentHelper.getEnchantments(itemstack2);
						iterator = map1.keySet().iterator();
						while (iterator.hasNext()) {
							j1 = iterator.next().intValue();
							enchantment = Enchantment.enchantmentsList[j1];
							k1 = map.containsKey(Integer.valueOf(j1)) ? map.get(Integer.valueOf(j1)).intValue() : 0;
							l1 = map1.get(Integer.valueOf(j1)).intValue();
							int j2;
							if (k1 == l1) {
								++l1;
								j2 = l1;
							}
							else {
								j2 = Math.max(l1, k1);
							}
							l1 = j2;
							int k2 = l1 - k1;
							boolean flag1 = enchantment.canApply(itemstack);
							if (this.thePlayer.capabilities.isCreativeMode || (itemstack.itemID == ItemEnchantedBook.enchantedBook.itemID)) {
								flag1 = true;
							}
							Iterator<Integer> iterator1 = map.keySet().iterator();
							while (iterator1.hasNext()) {
								int l2 = iterator1.next().intValue();
								if ((l2 != j1) && !enchantment.canApplyTogether(Enchantment.enchantmentsList[l2])) {
									flag1 = false;
									i += k2;
								}
							}
							if (flag1) {
								if (l1 > enchantment.getMaxLevel()) {
									l1 = enchantment.getMaxLevel();
								}
								map.put(Integer.valueOf(j1), Integer.valueOf(l1));
								int i3 = 0;
								switch (enchantment.getWeight()) {
									case 1:
										i3 = 8;
										break;
									case 2:
										i3 = 4;
										//$FALL-THROUGH$
									case 3:
									case 4:
									case 6:
									case 7:
									case 8:
									case 9:
									default:
										break;
									case 5:
										i3 = 2;
										break;
									case 10:
										i3 = 1;
								}
								if (flag) {
									i3 = Math.max(1, i3 / 2);
								}
								i += i3 * k2;
							}
						}
					}
				}
				if (StringUtils.isBlank(this.repairedItemName)) {
					if (itemstack.hasDisplayName()) {
						j = itemstack.isItemStackDamageable() ? 7 : itemstack.stackSize * 5;
						i += j;
						itemstack1.func_135074_t();
					}
				}
				else if ( !this.repairedItemName.equals(itemstack.getDisplayName())) {
					j = itemstack.isItemStackDamageable() ? 7 : itemstack.stackSize * 5;
					i += j;
					if (itemstack.hasDisplayName()) {
						k += j / 2;
					}
					itemstack1.setItemName(this.repairedItemName);
				}
				l = 0;
				for (iterator = map.keySet().iterator(); iterator.hasNext(); k += l + (k1 * l1)) {
					j1 = iterator.next().intValue();
					enchantment = Enchantment.enchantmentsList[j1];
					k1 = map.get(Integer.valueOf(j1)).intValue();
					l1 = 0;
					++l;
					switch (enchantment.getWeight()) {
						case 1:
							l1 = 8;
							break;
						case 2:
							l1 = 4;
							//$FALL-THROUGH$
						case 3:
						case 4:
						case 6:
						case 7:
						case 8:
						case 9:
						default:
							break;
						case 5:
							l1 = 2;
							break;
						case 10:
							l1 = 1;
					}
					if (flag) {
						l1 = Math.max(1, l1 / 2);
					}
				}
				if (flag) {
					k = Math.max(1, k / 2);
				}
				if (flag && (itemstack1 != null) && !Item.itemsList[itemstack1.itemID].isBookEnchantable(itemstack1, itemstack2)) {
					itemstack1 = null;
				}
				// this.maximumCost = k + i;
				if (i <= 0) {
					itemstack1 = null;
				}
				// if ((j == i) && (j > 0) && (this.maximumCost >= 40)) {
				// this.maximumCost = 39;
				// }
				if (itemstack1 != null) {
					i1 = itemstack1.getRepairCost();
					if ((itemstack2 != null) && (i1 < itemstack2.getRepairCost())) {
						i1 = itemstack2.getRepairCost();
					}
					if (itemstack1.hasDisplayName()) {
						i1 -= 9;
					}
					if (i1 < 0) {
						i1 = 0;
					}
					i1 += 2;
					itemstack1.setRepairCost(i1);
					EnchantmentHelper.setEnchantments(map, itemstack1);
				}
				this.maximumCost = 0;
				log.log(LoggingLevel.TRACE, "Output updated");
				this.outputSlot.setInventorySlotContents(0, itemstack1);
				this.detectAndSendChanges();
			}
		}
	}
	@Override
	public String getCommandName() {
		return "anvil";
	}
	@Override
	public String getCommandHelp(ICommandSender src) {
		return "Opens an anvil";
	}
	@Override
	public String getCommandArgs(ICommandSender src) {
		return "";
	}
	@Override
	public void execute(ICommandSender src, EntityPlayer player, String[] args) {
		final Logger log = Log.getLogger("/anvil GlobalAnvil.execute(...) " + (player.worldObj.isRemote ? "CLIENT" : "SERVER") + "");
		log.log(LoggingLevel.TRACE, "Command entered by player");
		try {
			EntityPlayerMP p = (EntityPlayerMP) player;
			log.log(LoggingLevel.TRACE, "Incrementing window ID");
			p.incrementWindowID();
			log.log(LoggingLevel.TRACE, "Sending packet");
			p.playerNetServerHandler.sendPacketToPlayer(new Packet100OpenWindow(p.currentWindowId, 8, "Repairing", 3, true));
			log.log(LoggingLevel.TRACE, "Setting open container");
			p.openContainer = new GlobalAnvil(p.inventory, p.worldObj, (int) p.posX, (int) p.posY, (int) p.posZ, p);
			log.log(LoggingLevel.TRACE, "Setting open container window ID");
			p.openContainer.windowId = p.currentWindowId;
			log.log(LoggingLevel.TRACE, "Adding to crafters");
			p.openContainer.addCraftingToCrafters(p);
			log.log(LoggingLevel.TRACE, "Done");
		}
		catch (ClassCastException e) {
			log.log(Level.WARNING, "failure", e);
		}
	}
	@Override
	public boolean isFinished() {
		return false;
	}
}
