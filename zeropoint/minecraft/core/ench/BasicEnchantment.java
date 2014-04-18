package zeropoint.minecraft.core.ench;


import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;
import net.minecraftforge.common.MinecraftForge;
import cpw.mods.fml.common.registry.LanguageRegistry;


//TODO: write Javadoc
public abstract class BasicEnchantment extends Enchantment {
	protected boolean hasLevels = true;
	protected BasicEnchantment(int par1, int par2, EnumEnchantmentType par3EnumEnchantmentType) {
		super(par1, par2, par3EnumEnchantmentType);
	}
	@Override
	public String getTranslatedName(int level) {
		String sname = StatCollector.translateToLocal(this.getName());
		if (this.hasLevels) {
			String lvl = StatCollector.translateToLocal("enchantment.level." + level);
			return sname + " " + lvl;
		}
		return sname;
	}
	@Override
	public int getMinEnchantability(int level) {
		return 5 * level;
	}
	@Override
	public int getMaxEnchantability(int level) {
		return getMinEnchantability(level) + 10;
	}
	@Override
	public boolean isAllowedOnBooks() {
		return true;
	}
	@Override
	public abstract boolean canApplyAtEnchantingTable(ItemStack stack);
	@Override
	public abstract boolean canApply(ItemStack stack);
	public void initialize() {
		registerLocalization();
		registerAsHandler();
	}
	public abstract void registerLocalization();
	protected final void setHumanReadableName(String readableName) {
		LanguageRegistry.instance().addStringLocalization(this.getName(), readableName);
	}
	public void registerAsHandler() {
		MinecraftForge.EVENT_BUS.register(this);
	}
}
