package zeropoint.minecraft.core.util.manip;


import net.minecraft.entity.Entity;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.world.Explosion;


/**
 * Basic class to make it easier to manipulate aspects of a world
 * 
 * @author Zero Point
 */
public class WorldHelper {
	/**
	 * Create an explosion at an {@link Entity}
	 * 
	 * @param target
	 *            - the entity to blow up
	 * @param strength
	 *            - the force of the explosion
	 * @param grief
	 *            - whether to destroy blocks
	 * @return the {@link Explosion}
	 */
	public static Explosion explodeAt(Entity target, double strength, boolean grief) {
		return target.worldObj.createExplosion(target, target.posX, target.posY, target.posZ, (float) strength, grief);
	}
	/**
	 * Hit an {@link Entity} with a lightning bolt
	 * 
	 * @param target
	 *            - the entity to target
	 * @return the {@link EntityLightningBolt}
	 */
	public static EntityLightningBolt zap(Entity target) {
		EntityLightningBolt bolt = new EntityLightningBolt(target.worldObj, target.posX, target.posY, target.posZ);
		target.worldObj.spawnEntityInWorld(bolt);
		return bolt;
	}
}
