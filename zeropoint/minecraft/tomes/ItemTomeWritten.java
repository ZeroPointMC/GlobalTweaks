// May 18, 2014 2:29:02 PM
package zeropoint.minecraft.tomes;


import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;


@SuppressWarnings("javadoc")
public class ItemTomeWritten extends ItemTome {
	public ItemTomeWritten(int id, int dmg) {
		super(id);
		this.setMaxStackSize(1);
		this.setUnlocalizedName("gtweaks.arcana.tome.written");
		this.setTextureName("gtweaks:tome-advanced");
		this.setMaxDamage(dmg);
		GameRegistry.registerItem(this, "tomeWritten");
		LanguageRegistry.addName(this, "Arcane Tome");
	}
	@Override
	public EnumRarity getRarity(ItemStack is) {
		return EnumRarity.epic;
	}
}
