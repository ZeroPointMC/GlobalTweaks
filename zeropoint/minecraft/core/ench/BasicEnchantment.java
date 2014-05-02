package zeropoint.minecraft.core.ench;


import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;
import net.minecraftforge.common.MinecraftForge;
import cpw.mods.fml.common.registry.LanguageRegistry;


/**
 * A basic enchantment, applied at an enchanting table, should extend this class.
 * 
 * @author Zero Point
 */
public abstract class BasicEnchantment extends Enchantment {
	/**
	 * If false, the "translated name" will not include a number; as in "Enchantment" instead of "Enchantment I"
	 */
	protected boolean hasLevels = true;
	/**
	 * @param id
	 *            - the enchantment ID
	 * @param weight
	 *            - the weight of the enchantment; the chance for it to be applied at a table
	 * @param type
	 *            - the {@link EnumEnchantmentType} of the enchantment
	 */
	protected BasicEnchantment(int id, int weight, EnumEnchantmentType type) {
		super(id, weight, type);
		this.initialize();
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
		return this.getMinEnchantability(level) + 10;
	}
	@Override
	public boolean isAllowedOnBooks() {
		return true;
	}
	@Override
	public abstract boolean canApplyAtEnchantingTable(ItemStack stack);
	@Override
	public abstract boolean canApply(ItemStack stack);
	/**
	 * Do whatever final bits are needed to make the enchantment usable
	 */
	public void initialize() {
		this.registerLocalization();
		this.registerAsHandler();
	}
	/**
	 * Register the localization with Minecraft, so you get "Enchantment Name I" instead "enchant.myenchant.name I"
	 */
	public abstract void registerLocalization();
	/**
	 * Injects the translation for the enchantment into the game
	 * 
	 * @param readableName
	 *            - the human readable name of the enchantment
	 */
	protected final void setHumanReadableName(String readableName) {
		LanguageRegistry.instance().addStringLocalization(this.getName(), readableName);
	}
	/**
	 * The enchantment should provide its own event handler. This method registers it with Minecraft Forge.
	 */
	public void registerAsHandler() {
		MinecraftForge.EVENT_BUS.register(this);
	}
}
