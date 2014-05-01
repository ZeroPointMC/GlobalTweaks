// May 1, 2014 9:18:41 AM
package zeropoint.minecraft.effect.potion;


import net.minecraft.entity.EntityLivingBase;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import zeropoint.minecraft.core.GTCore;
import zeropoint.minecraft.effect.DmgSrc;


@SuppressWarnings("javadoc")
public class PotionBleed extends Potion {
	public final int damage;
	public PotionBleed(int id, int dmg) {
		super(id, true, 5312497);
		this.setEffectiveness(1.0D);
		this.damage = dmg;
		this.setIconIndex( -1, -1);
		this.setPotionName("potion.bleed");
		GTCore.injectStringTranslation("potion.bleed", "Bleeding");
	}
	@Override
	public boolean isReady(int dur, int lvl) {
		// I think this triggers it once per second (with level 1)
		// But I don't know for certain, because MC's
		// code is a messy thing and it's hard to follow.
		// Also, I hate bitwise math.
		int k = 40 >> lvl;
		return k > 0 ? (dur % k) == 0 : true;
	}
	@ForgeSubscribe
	public void onEntityUpdate(LivingUpdateEvent event) {
		EntityLivingBase victim = event.entityLiving;
		if ( !victim.isPotionActive(this.id)) {
			return;
		}
		PotionEffect effect = victim.getActivePotionEffect(this);
		if (effect.getDuration() <= 0) {
			victim.removePotionEffect(this.id);
			return;
		}
		if ( !this.isReady(effect.getDuration(), effect.getAmplifier())) {
			return;
		}
		PotionEffect slowDig = new PotionEffect(Potion.digSlowdown.id, effect.getDuration(), effect.getAmplifier(), true);
		PotionEffect weak = new PotionEffect(Potion.weakness.id, effect.getDuration(), effect.getAmplifier(), true);
		PotionEffect slow = new PotionEffect(Potion.moveSlowdown.id, effect.getDuration(), 0, true);
		if ( !victim.isPotionActive(slowDig.getPotionID())) {
			victim.addPotionEffect(slowDig);
		}
		if ( !victim.isPotionActive(weak.getPotionID())) {
			victim.addPotionEffect(weak);
		}
		if ( !victim.isPotionActive(slow.getPotionID())) {
			victim.addPotionEffect(slow);
		}
		victim.attackEntityFrom(DmgSrc.BLEEDING, this.damage);
	}
}
