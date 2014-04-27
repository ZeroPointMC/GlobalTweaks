package zeropoint.minecraft.sonic;


import java.util.List;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import zeropoint.minecraft.core.util.ChatMsg;
import zeropoint.minecraft.core.util.manip.InventoryHelper;


@SuppressWarnings("javadoc")
public class VortexManipulator extends Item {
	public VortexManipulator(int id) {
		super(id);
		this.setMaxStackSize(1);
		this.setCreativeTab(CreativeTabs.tabTools);
		this.setUnlocalizedName("gtweaks.sonic.vortexmanip");
		this.setTextureName("gtweaks:vortex-manipulator");
	}
	@Override
	public void addInformation(ItemStack is, EntityPlayer player, List l, boolean B) {
		l.add("NYI");
		if ( !B) {
			return;
		}
		l.add("Allows you to teleport on a whim!");
		l.add("Unfortunately, spacio-temporal travel");
		l.add("without a capsule tends to mess you up...");
	}
	@Override
	public EnumAction getItemUseAction(ItemStack par1ItemStack) {
		return EnumAction.none;
	}
	@Override
	public EnumRarity getRarity(ItemStack is) {
		return EnumRarity.rare;
	}
	@Override
	public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, int cX, int cY, int cZ, int blockSideId, float hitX, float hitY, float hitZ) {
		return true;
	}
	@Override
	public boolean onItemUseFirst(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int blockSideId, float hitX, float hitY, float hitZ) {
		if ( !world.isRemote) {
			if ( !player.capabilities.isCreativeMode && !this.useFuel(player)) {
				new ChatMsg(ChatMsg.RED + "Your vortex manipulator is out of fuel!").send(player);
				new ChatMsg(ChatMsg.GRAY + "You need " + GTSonic.vortexFuelCount + " of " + GTSonic.vortexFuelName + ChatMsg.GRAY + " to use your vortex manipulator").send(player);
				return false;
			}
			new ChatMsg(ChatMsg.RED + "The Vortex Manipulator is not yet functional, sorry!" + ChatMsg.GRAY + " ~ZeroPoint").send(player);
		}
		player.inventory.onInventoryChanged();
		player.inventoryContainer.detectAndSendChanges();
		return world.isRemote ? false : true;
	}
	/**
	 * Consume one unit of fuel from the player inventory
	 * 
	 * @param player
	 *            the player who used the vortex manipulator
	 * @return <code>true</code> if fuel was consumed, <code>false</code> to cancel action
	 */
	protected static boolean useFuel(EntityPlayer player) {
		if ((GTSonic.vortexFuelID + GTSonic.vortexFuelCount) < 1) {
			return true;
		}
		return InventoryHelper.consumeItem(player.inventory, GTSonic.vortexFuelID, GTSonic.vortexFuelMeta, GTSonic.vortexFuelCount);
	}
}
