package zeropoint.minecraft.core.world;


import java.util.Iterator;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.util.MathHelper;
import net.minecraft.world.ChunkPosition;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;


// This entire class was created just so I could change ONE line.
// Also, it's only 'safe' in the sense that it drops everything.
// It'll still kill things.
public class MiningExplosion extends Explosion {
	protected World worldObj;
	protected Random explosionRNG = new Random();
	public MiningExplosion(World world, Entity corpseToBe, double x, double y, double z, float strength) {
		super(world, corpseToBe, x, y, z, strength);
		worldObj = world;
	}
	// The one line I needed to change is in here.
	@Override
	public void doExplosionB(boolean par1) {
		this.worldObj.playSoundEffect(this.explosionX, this.explosionY, this.explosionZ, "random.explode", 4.0F, (1.0F + ((this.worldObj.rand.nextFloat() - this.worldObj.rand.nextFloat()) * 0.2F)) * 0.7F);
		if ((this.explosionSize >= 2.0F) && this.isSmoking) {
			this.worldObj.spawnParticle("hugeexplosion", this.explosionX, this.explosionY, this.explosionZ, 1.0D, 0.0D, 0.0D);
		}
		else {
			this.worldObj.spawnParticle("largeexplode", this.explosionX, this.explosionY, this.explosionZ, 1.0D, 0.0D, 0.0D);
		}
		@SuppressWarnings("rawtypes")
		Iterator iterator;
		ChunkPosition chunkposition;
		int i;
		int j;
		int k;
		int l;
		if (this.isSmoking) {
			iterator = this.affectedBlockPositions.iterator();
			while (iterator.hasNext()) {
				chunkposition = (ChunkPosition) iterator.next();
				i = chunkposition.x;
				j = chunkposition.y;
				k = chunkposition.z;
				l = this.worldObj.getBlockId(i, j, k);
				if (par1) {
					double d0 = i + this.worldObj.rand.nextFloat();
					double d1 = j + this.worldObj.rand.nextFloat();
					double d2 = k + this.worldObj.rand.nextFloat();
					double d3 = d0 - this.explosionX;
					double d4 = d1 - this.explosionY;
					double d5 = d2 - this.explosionZ;
					double d6 = MathHelper.sqrt_double((d3 * d3) + (d4 * d4) + (d5 * d5));
					d3 /= d6;
					d4 /= d6;
					d5 /= d6;
					double d7 = 0.5D / ((d6 / this.explosionSize) + 0.1D);
					d7 *= (this.worldObj.rand.nextFloat() * this.worldObj.rand.nextFloat()) + 0.3F;
					d3 *= d7;
					d4 *= d7;
					d5 *= d7;
					this.worldObj.spawnParticle("explode", (d0 + (this.explosionX * 1.0D)) / 2.0D, (d1 + (this.explosionY * 1.0D)) / 2.0D, (d2 + (this.explosionZ * 1.0D)) / 2.0D, d3, d4, d5);
					this.worldObj.spawnParticle("smoke", d0, d1, d2, d3, d4, d5);
				}
				if (l > 0) {
					Block block = Block.blocksList[l];
					if (block.canDropFromExplosion(this)) {
						// Behold, the one line I actually needed to change
						// The only change I made was setting the block drop chance to Float.MAX_VALUE
						block.dropBlockAsItemWithChance(this.worldObj, i, j, k, this.worldObj.getBlockMetadata(i, j, k), Float.MAX_VALUE, 0);
					}
					block.onBlockExploded(this.worldObj, i, j, k, this);
				}
			}
		}
		if (this.isFlaming) {
			iterator = this.affectedBlockPositions.iterator();
			while (iterator.hasNext()) {
				chunkposition = (ChunkPosition) iterator.next();
				i = chunkposition.x;
				j = chunkposition.y;
				k = chunkposition.z;
				l = this.worldObj.getBlockId(i, j, k);
				int i1 = this.worldObj.getBlockId(i, j - 1, k);
				if ((l == 0) && Block.opaqueCubeLookup[i1] && (this.explosionRNG.nextInt(3) == 0)) {
					this.worldObj.setBlock(i, j, k, Block.fire.blockID);
				}
			}
		}
	}
}
