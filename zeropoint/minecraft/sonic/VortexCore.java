// May 9, 2014 10:04:02 AM
package zeropoint.minecraft.sonic;


import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;


@SuppressWarnings("javadoc")
public class VortexCore extends Item {
	public VortexCore(int id) {
		super(id);
		this.setMaxStackSize(64);
		this.setCreativeTab(CreativeTabs.tabMaterials);
		this.setUnlocalizedName("gtweaks.item.vortexCore");
		this.setTextureName("gtweaks:vortex-core");
		GameRegistry.addRecipe(new ItemStack(this), "grg", "rdr", "grg", 'g', new ItemStack(Item.ingotGold), 'r', new ItemStack(Block.blockRedstone), 'd', new ItemStack(Item.diamond));
		GameRegistry.registerItem(this, "gtweaks.vortexCore");
		LanguageRegistry.addName(this, "Vortex Core");
	}
	@Override
	public void addInformation(ItemStack is, EntityPlayer player, List l, boolean B) {
		l.add("Crafting component");
	}
}
