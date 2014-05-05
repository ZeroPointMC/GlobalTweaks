// May 4, 2014 1:55:25 PM
package zeropoint.minecraft.misc;


import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import zeropoint.minecraft.core.util.ChatMsg;
import cpw.mods.fml.common.registry.GameRegistry;


@SuppressWarnings("javadoc")
public class QuillAndInk extends Item {
	public QuillAndInk(int id, int dmg) {
		super(id);
		this.setMaxStackSize(1);
		this.setCreativeTab(CreativeTabs.tabTools);
		this.setUnlocalizedName("gtweaks.misc.quill");
		this.setTextureName("gtweaks:quill");
		this.setHasSubtypes(false);
		this.setMaxDamage(dmg);
		GameRegistry.addShapelessRecipe(new ItemStack(this), Item.feather, new ItemStack(Item.dyePowder, 1, 0), Item.glassBottle);
	}
	@Override
	public void addInformation(ItemStack is, EntityPlayer player, List l, boolean B) {
		l.add(ChatMsg.CYAN + "Sneak and right click a sign to edit it.");
	}
	@Override
	public EnumAction getItemUseAction(ItemStack par1ItemStack) {
		return EnumAction.none;
	}
	@Override
	public EnumRarity getRarity(ItemStack is) {
		return EnumRarity.common;
	}
	@Override
	public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, int cX, int cY, int cZ, int blockSideId, float hitX, float hitY, float hitZ) {
		return false;
	}
	@Override
	public boolean onItemUseFirst(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int blockSideId, float hitX, float hitY, float hitZ) {
		if ( !player.isSneaking() || world.isRemote) {
			return false;
		}
		if ((world.getBlockId(x, y, z) == Block.signPost.blockID) || (world.getBlockId(x, y, z) == Block.signWall.blockID)) {
			player.displayGUIEditSign(world.getBlockTileEntity(x, y, z));
			player.getCurrentEquippedItem().damageItem(1, player);
		}
		return true;
	}
}
