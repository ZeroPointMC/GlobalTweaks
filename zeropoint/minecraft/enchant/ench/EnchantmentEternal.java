// May 2, 2014 2:52:46 PM
package zeropoint.minecraft.enchant.ench;


import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.entity.item.ItemExpireEvent;
import zeropoint.minecraft.core.ench.BasicEnchantment;
import zeropoint.minecraft.core.util.manip.EnchantHelper;


@SuppressWarnings("javadoc")
public class EnchantmentEternal extends BasicEnchantment {
	public EnchantmentEternal(int id) {
		super(id, 1, EnumEnchantmentType.all);
		this.hasLevels = false;
		addToBookList(this);
	}
	@Override
	public boolean canApplyAtEnchantingTable(ItemStack stack) {
		return true;
	}
	@Override
	public boolean canApply(ItemStack stack) {
		return true;
	}
	@Override
	public void registerLocalization() {
		this.setHumanReadableName("Eternal");
	}
	@ForgeSubscribe
	public void onItemExpire(ItemExpireEvent evt) {
		ItemStack stack = evt.entityItem.getEntityItem();
		if (EnchantHelper.getEnchantmentLevel(this.effectId, stack) > 0) {
			evt.extraLife = Integer.MAX_VALUE;
			evt.setCanceled(true);
		}
	}
}
