package zeropoint.minecraft.sonic;


import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockRedstoneLight;
import net.minecraft.world.World;


public class BlockRedstoneLamp extends BlockRedstoneLight {
	public BlockRedstoneLamp(int par1, boolean par2) {
		super(par1, par2);
		setHardness(0.3F);
		setStepSound(Block.soundGlassFootstep);
		setUnlocalizedName("sonicRedstoneLight");
		if (par2) {
			setLightValue(1.0F);
			setTextureName("redstone_lamp_on");
		}
		else {
			setLightValue(0.0F);
			setTextureName("redstone_lamp_off");
		}
	}
	@Override
	public void updateTick(World par1World, int par2, int par3, int par4, Random par5Random) {
		// This method is overridden so that the lamp stays in its original state
	}
}
