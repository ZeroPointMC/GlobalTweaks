package zeropoint.minecraft.core.world.entity;


import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityTNTPrimed;
import net.minecraft.world.World;
import zeropoint.minecraft.core.world.MiningExplosion;


public class EntityMiningTNT extends EntityTNTPrimed {
	public EntityMiningTNT(World world) {
		super(world);
	}
	public EntityMiningTNT(World world, double x, double y, double z, EntityLivingBase pyro) {
		super(world, x, y, z, pyro);
	}
	@Override
	public void onUpdate() {
		/*
		 * this.prevPosX = this.posX;
		 * this.prevPosY = this.posY;
		 * this.prevPosZ = this.posZ;
		 * this.motionX *= 0.9800000190734863D;
		 * this.motionY *= 0.9800000190734863D;
		 * this.motionZ *= 0.9800000190734863D;
		 * if (this.onGround) {
		 * this.motionX *= 0.699999988079071D;
		 * this.motionZ *= 0.699999988079071D;
		 * this.motionY *= -0.5D;
		 * }
		 */
		this.motionY -= 0.03999999910593033D;
		this.moveEntity(0, this.motionY, 0);
		if (this.onGround) {
			this.motionY *= -0.5D;
		}
		if (this.fuse-- <= 0) {
			this.setDead();
			if ( !this.worldObj.isRemote) {
				this.explode();
			}
		}
		else {
			this.worldObj.spawnParticle("smoke", this.posX, this.posY + 0.5D, this.posZ, 0.0D, 0.0D, 0.0D);
		}
	}
	protected void explode() {
		MiningExplosion boom = new MiningExplosion(this.worldObj, this, this.posX, this.posY, this.posZ, 32.0F);
		boom.isFlaming = false;
		boom.isSmoking = true;
		boom.doExplosionA();
		boom.doExplosionB(true);
	}
}
