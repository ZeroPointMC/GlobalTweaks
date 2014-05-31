// May 18, 2014 2:26:26 PM
package zeropoint.minecraft.tomes;


import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;


@SuppressWarnings("javadoc")
public class ItemTomeBlank extends ItemTome {
	public ItemTomeBlank(int id) {
		super(id);
		this.setMaxStackSize(64);
		this.setUnlocalizedName("gtweaks.arcana.tome.blank");
		this.setTextureName("gtweaks:tome-basic");
		GameRegistry.registerItem(this, "tomeBlank");
		LanguageRegistry.addName(this, "Blank Arcane Tome");
	}
	@Override
	public EnumRarity getRarity(ItemStack is) {
		return EnumRarity.rare;
	}
}
