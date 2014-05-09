// May 9, 2014 9:59:01 AM
package zeropoint.minecraft.sonic;


import java.util.List;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;


@SuppressWarnings("javadoc")
public class DblIronBand extends Item {
	public DblIronBand(int id) {
		super(id);
		this.setMaxStackSize(64);
		this.setCreativeTab(CreativeTabs.tabMaterials);
		this.setUnlocalizedName("gtweaks.item.dblIronBand");
		this.setTextureName("gtweaks:dbl-iron-band");
		GameRegistry.addRecipe(new ItemStack(this), "i i", "g g", "i i", 'i', new ItemStack(Item.ingotIron), 'g', new ItemStack(Item.ingotGold));
		GameRegistry.registerItem(this, "gtweaks.doubleIronBand");
		LanguageRegistry.addName(this, "Double Band");
	}
	@Override
	public void addInformation(ItemStack is, EntityPlayer player, List l, boolean B) {
		l.add("Crafting component");
	}
}
