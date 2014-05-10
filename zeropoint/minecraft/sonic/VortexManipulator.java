package zeropoint.minecraft.sonic;


import java.util.List;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;
import zeropoint.minecraft.core.util.ChatMsg;
import zeropoint.minecraft.core.util.EnumBlockSide;
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
		l.add(ChatMsg.SKY + "Allows you to teleport through blocks");
		l.add("Dangerous if you don't know where you're going to end up...");
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
			EnumBlockSide side = EnumBlockSide.getByInt(blockSideId);
			int px = x, py = (player.isSneaking() ? y - 1 : y), pz = z;
			if (side == side.BOTTOM) {
				do {
					++py;
				} while ( !(world.isAirBlock(px, py, pz) || world.isAirBlock(px, py + 1, pz)));
				warp(player, px, py, pz);
			}
			else if (side == side.TOP) {
				do {
					--py;
				} while ( !(world.isAirBlock(px, py, pz) || world.isAirBlock(px, py + 1, pz)));
				warp(player, px, --py, pz);
			}
			else if (side == side.EAST) {
				do {
					--px;
				} while ( !(world.isAirBlock(px, py, pz) || world.isAirBlock(px, py + 1, pz)));
				warp(player, px, py, pz);
			}
			else if (side == side.WEST) {
				do {
					++px;
				} while ( !(world.isAirBlock(px, py, pz) || world.isAirBlock(px, py + 1, pz)));
				warp(player, px, py, pz);
			}
			else if (side == side.NORTH) {
				do {
					++pz;
				} while ( !(world.isAirBlock(px, py, pz) || world.isAirBlock(px, py + 1, pz)));
				warp(player, px, py, pz);
			}
			else if (side == side.SOUTH) {
				do {
					--pz;
				} while ( !(world.isAirBlock(px, py, pz) || world.isAirBlock(px, py + 1, pz)));
				warp(player, px, py, pz);
			}
			else {
				new ChatMsg("How did you hit a non-existant block side?").send(player);
			}
		}
		player.inventory.onInventoryChanged();
		player.inventoryContainer.detectAndSendChanges();
		return world.isRemote ? false : true;
	}
	private static void warp(EntityPlayer player, int px, int py, int pz) {
		player.setPositionAndUpdate(px, py, pz);
		if ( !player.capabilities.isCreativeMode) {
			player.addPotionEffect(new PotionEffect(Potion.confusion.id, 160, 0, true));
			player.addPotionEffect(new PotionEffect(Potion.moveSlowdown.id, 40, 2, true));
			player.addPotionEffect(new PotionEffect(Potion.weakness.id, 200, 1, true));
		}
		// warpMsg(player, px, py, pz);
	}
	private static void warpMsg(EntityPlayer player, int px, int py, int pz) {
		new ChatMsg("Warping from " + ((int) player.posX) + " " + ((int) player.posY) + " " + ((int) player.posZ) + " to " + px + " " + py + " " + pz).send(player);
	}
	/**
	 * Consume fuel from the player inventory
	 * 
	 * @param player
	 *            the player who used the vortex manipulator
	 * @return <code>true</code> if fuel was consumed, <code>false</code> to cancel action
	 */
	protected static boolean useFuel(EntityPlayer player) {
		if ((GTSonic.vortexFuelID < 1) || (GTSonic.vortexFuelCount < 1)) {
			return true;
		}
		return InventoryHelper.consumeItem(player.inventory, GTSonic.vortexFuelID, GTSonic.vortexFuelMeta, GTSonic.vortexFuelCount);
	}
}
