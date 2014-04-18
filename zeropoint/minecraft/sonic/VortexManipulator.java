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


@SuppressWarnings("javadoc")
public class VortexManipulator extends Item {
	public VortexManipulator(int id) {
		super(id);
		setMaxStackSize(1);
		setCreativeTab(CreativeTabs.tabTools);
		setUnlocalizedName("gtweaks.sonic.vortexmanip");
		setTextureName("gtweaks:vortex-manipulator");
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
		l.add("");
		l.add("Always know where you're teleporting,");
		l.add("lest you end up in a wall!");
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
			new ChatMsg("Sorry, NYI!").send(player);
		}
		return world.isRemote ? false : true;
	}
}
