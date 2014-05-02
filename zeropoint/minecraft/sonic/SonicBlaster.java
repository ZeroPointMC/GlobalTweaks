package zeropoint.minecraft.sonic;


import java.util.List;
import java.util.logging.Logger;

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
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.Event.Result;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;
import zeropoint.minecraft.core.util.ChatMsg;
import zeropoint.minecraft.core.util.Log;
import zeropoint.minecraft.core.util.manip.InventoryHelper;


@SuppressWarnings("javadoc")
public class SonicBlaster extends Item {
	private static final Logger LOG = Log.getLogger(GTSonic.name);
	public SonicBlaster(int id) {
		super(id);
		this.setMaxStackSize(1);
		this.setCreativeTab(CreativeTabs.tabTools);
		this.setUnlocalizedName("gtweaks.sonic.sonicblaster");
		this.setTextureName("gtweaks:blaster");
		MinecraftForge.EVENT_BUS.register(this);
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
			if ( !player.capabilities.isCreativeMode && !useFuel(player)) {
				new ChatMsg(ChatMsg.RED + "Your sonic blaster is out of fuel!").send(player);
				new ChatMsg(ChatMsg.GRAY + "You need " + GTSonic.blasterFuelCount + " of " + GTSonic.blasterFuelName + ChatMsg.GRAY + " to use your blaster").send(player);
				return false;
			}
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
	/**
	 * Consume one unit of fuel from the player inventory
	 * 
	 * @param player
	 *            the player who used the sonic blaster
	 * @return <code>true</code> if fuel was consumed, <code>false</code> to cancel action
	 */
	protected static boolean useFuel(EntityPlayer player) {
		if ((GTSonic.blasterFuelID < 1) || (GTSonic.blasterFuelCount < 1)) {
			return true;
		}
		return InventoryHelper.consumeItem(player.inventory, GTSonic.blasterFuelID, GTSonic.blasterFuelMeta, 1);
	}
	private static void doDrops(World world, int cX, int cY, int cZ, Block hit, EntityPlayer player) {
		final int x = cX;
		final int y = cY + 1;
		final int z = cZ;
		List<ItemStack> drops = hit.getBlockDropped(world, cX, cY, cZ, world.getBlockMetadata(cX, cY, cZ), 0);
		for (ItemStack drop : drops) {
			// The old method of handling direct drop-to-inventory
			// transitions didn't play well with Forestry's backpacks.
			// Unfortunately, NEITHER method works right in creative.
			// I'll see what I can do about that.
			EntityItemPickupEvent evt = new EntityItemPickupEvent(player, new EntityItem(world, player.posX, player.posY, player.posZ, drop));
			MinecraftForge.EVENT_BUS.post(evt);
			Result r = evt.getResult();
			// LOG.info((evt.isCanceled() ? "Canceled" : "Uncanceled") + " EntityItemPickupEvent result: " + (r == Result.ALLOW ? "ALLOW" : r == Result.DEFAULT ? "DEFAULT" : r == Result.DENY ? "DENY" : r));
			if (r == Result.DENY) {
				EntityItem ent = new EntityItem(world, x, y, z, drop);
				world.spawnEntityInWorld(ent);
			}
		}
	}
	@SuppressWarnings("static-method")
	@ForgeSubscribe
	public void onPickup(EntityItemPickupEvent evt) {
		if (evt.getResult() == Result.DEFAULT) {
			if (evt.entityPlayer.inventory.addItemStackToInventory(evt.item.getEntityItem())) {
				evt.setResult(Result.ALLOW);
			}
			else {
				evt.setResult(Result.DENY);
			}
		}
	}
}
