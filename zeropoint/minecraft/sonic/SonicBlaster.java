package zeropoint.minecraft.sonic;


import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.BlockDoor;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import zeropoint.minecraft.core.util.ChatMsg;


@SuppressWarnings("javadoc")
public class SonicBlaster extends Item {
	public SonicBlaster(int id) {
		super(id);
		setMaxStackSize(1);
		setCreativeTab(CreativeTabs.tabTools);
		setUnlocalizedName("gtweaks.sonic.sonicblaster");
		setTextureName("gtweaks:blaster");
	}
	@Override
	public void addInformation(ItemStack is, EntityPlayer player, List l, boolean B) {
		l.add("Instantly breaks blocks, and attempts");
		l.add("to place the drops in your inventory.");
		l.add("Sneak and right click a block to use.");
		l.add("");
		l.add("Doesn't collect items from inventories!");
		l.add("Careful when using on chests!");
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
		return false;
	}
	@Override
	public boolean onItemUseFirst(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int blockSideId, float hitX, float hitY, float hitZ) {
		if ( !player.isSneaking()) {
			return false;
		}
		if ( !world.isRemote) {
			final int id = world.getBlockId(x, y, z);
			final Block hit = Block.blocksList[id];
			if (hit == null) {
				new ChatMsg("*fizzle*").send(player);
			}
			else {
				if (hit instanceof BlockDoor) {
					if ((world.getBlockId(x, y - 1, z) == id) && ((world.getBlockMetadata(x, y, z) & 8) != 0)) {
						// They hit the top half
						doDrops(world, x, y - 1, z, hit, player);
						world.setBlockToAir(x, y - 1, z);
					}
				}
				else {
					doDrops(world, x, y, z, hit, player);
				}
			}
			world.setBlockToAir(x, y, z);
		}
		player.inventory.onInventoryChanged();
		player.inventoryContainer.detectAndSendChanges();
		return world.isRemote ? false : true;
	}
	private static void doDrops(World world, int cX, int cY, int cZ, Block hit, EntityPlayer player) {
		final int x = cX;
		final int y = cY + 1;
		final int z = cZ;
		List<ItemStack> drops = hit.getBlockDropped(world, cX, cY, cZ, world.getBlockMetadata(cX, cY, cZ), 0);
		for (ItemStack drop : drops) {
			if ( !player.inventory.addItemStackToInventory(drop)) {
				EntityItem ent = new EntityItem(world, x, y, z, drop);
				world.spawnEntityInWorld(ent);
			}
		}
	}
}