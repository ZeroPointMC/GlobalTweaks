// May 1, 2014 9:55:42 AM
package zeropoint.minecraft.effect;


import net.minecraft.util.DamageSource;
import zeropoint.minecraft.core.GTCore;


@SuppressWarnings("javadoc")
public class DmgSrc extends DamageSource {
	public static final DamageSource BLEEDING = new DmgSrc("bleeding", "%s died from exsanguination", "%s was exsanguinated by %s").setDamageBypassesArmor();
	protected DmgSrc(String dmgType, String deathMessage) {
		this(dmgType, deathMessage, deathMessage + " while fighting %s");
	}
	protected DmgSrc(String dmgType, String deathMessage, String deathMessageLong) {
		super(dmgType);
		GTCore.injectStringTranslation("death.attack." + dmgType, deathMessage);
		GTCore.injectStringTranslation("death.attack." + dmgType + ".player", deathMessageLong);
	}
}
