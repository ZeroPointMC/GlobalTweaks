// May 18, 2014 2:34:46 PM
package zeropoint.minecraft.tomes;


import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import zeropoint.minecraft.core.util.manip.WorldHelper;


@SuppressWarnings("javadoc")
public class ItemTome extends Item {
	public ItemTome(int id) {
		super(id);
	}
	public static boolean isAdv(Item check) {
		return check instanceof ItemTomeWritten;
	}
	@Override
	public void addInformation(ItemStack is, EntityPlayer player, List l, boolean B) {}
	@Override
	public EnumAction getItemUseAction(ItemStack par1ItemStack) {
		return EnumAction.none;
	}
	@Override
	public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, int cX, int cY, int cZ, int blockSideId, float hitX, float hitY, float hitZ) {
		return false;
	}
	@Override
	public boolean onItemUseFirst(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int blockSideId, float hitX, float hitY, float hitZ) {
		ItemStack held = player.getCurrentEquippedItem();
		final int hitID = world.getBlockId(x, y, z);
		if (held.itemID == GTArcana.blankTome.itemID) {
			if ((hitID == Block.blockDiamond.blockID) || (hitID == Block.blockEmerald.blockID)) {
				world.setBlockToAir(x, y, z);
				player.getCurrentEquippedItem().itemID = GTArcana.activeTome.itemID;
				Explosion explosion = new Explosion(world, player, x, y, z, 8.0F);
				explosion.isFlaming = true;
				explosion.isSmoking = true;
				WorldHelper.zap(player);
				explosion.doExplosionA();
				explosion.doExplosionB(true);
			}
		}
		return true;
	}
}
