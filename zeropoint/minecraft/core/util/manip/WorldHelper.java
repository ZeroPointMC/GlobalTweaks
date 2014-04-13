package zeropoint.minecraft.core.util.manip;


import net.minecraft.entity.Entity;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.world.Explosion;


public class WorldHelper {
	public static Explosion explodeAt(Entity target, float strength, boolean grief) {
		return target.worldObj.createExplosion(target, target.posX, target.posY, target.posZ, strength, grief);
	}
	public static Explosion explodeAt(Entity target, double strength, boolean grief) {
		return explodeAt(target, (float) strength, grief);
	}
	public static EntityLightningBolt zap(Entity target) {
		EntityLightningBolt bolt = new EntityLightningBolt(target.worldObj, target.posX, target.posY, target.posZ);
		target.worldObj.spawnEntityInWorld(bolt);
		return bolt;
	}
}
