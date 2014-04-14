package zeropoint.minecraft.sonic;


import java.util.List;
import java.util.UUID;

import net.minecraft.block.Block;
import net.minecraft.block.BlockDoor;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityTNTPrimed;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.world.World;
import zeropoint.minecraft.core.GTCore;
import zeropoint.minecraft.core.util.ChatMsg;
import zeropoint.minecraft.core.util.EnumBlockSide;
import zeropoint.minecraft.core.world.entity.EntityMiningTNT;


public class SonicScrewdriver extends Item {
	public final SonicColour colour;
	public enum SonicColour {
		BLUE("blue"), GREEN("green");
		// Below is the stuff that makes this bit cool
		private final String colour;
		private SonicColour(String col) {
			colour = col;
		}
		@Override
		public final String toString() {
			return colour.toLowerCase();
		}
	}
	public SonicScrewdriver(int id) {
		this(id, SonicColour.GREEN);
	}
	public SonicScrewdriver(int id, SonicColour col) {
		super(id);
		setMaxStackSize(1);
		setCreativeTab(CreativeTabs.tabTools);
		setUnlocalizedName("gtweaks.sonic.sonicprobe");
		setTextureName("gtweaks:sonic-" + col);
		colour = col;
	}
	@Override
	public void addInformation(ItemStack is, EntityPlayer player, List l, boolean B) {
		SonicScrewdriver sonic = (SonicScrewdriver) is.getItem();
		if (sonic.colour == SonicColour.BLUE) {
			l.add("The tenth Doctor's sonic screwdriver");
		}
		else if (sonic.colour == SonicColour.GREEN) {
			l.add("The eleventh Doctor's sonic screwdriver");
		}
		else {
			l.add("A brand new sonic screwdriver");
		}
		l.add("");
		l.add("Does too many things to explain here.");
		l.add("Use the HELP-SONIC book from the GT Tomes module!");
		l.add("To get it, hold a book and use this chat command:");
		l.add("/tome load HELP-SONIC");
		if ( !GTCore.Modules.tomesEnabled()) {
			l.add("WARNING! Tomes module disabled!");
		}
	}
	@Override
	public EnumAction getItemUseAction(ItemStack par1ItemStack) {
		return EnumAction.bow;
	}
	@Override
	public EnumRarity getRarity(ItemStack is) {
		return EnumRarity.uncommon;
	}
	@Override
	public void onCreated(ItemStack stack, World world, EntityPlayer player) {
		if (stack.stackTagCompound == null) {
			stack.stackTagCompound = new NBTTagCompound("tag");
		}
		NBTTagCompound tag = stack.getTagCompound();
		if (GTSonic.speedBoost > 0) {
			NBTTagList attrmods = new NBTTagList("AttributeModifiers");
			NBTTagCompound speed = new NBTTagCompound();
			speed.setString("AttributeName", "generic.movementSpeed");
			speed.setString("Name", "SpeedyTimelord");
			speed.setDouble("Amount", GTSonic.speedBoost);
			speed.setInteger("Operation", 2);
			UUID id = UUID.fromString("4F3A9AB0-A88E-11E3-A5E2-0800200C9A66");
			// UUID id = UUID.randomUUID();
			speed.setLong("UUIDMost", id.getMostSignificantBits());
			speed.setLong("UUIDLeast", id.getLeastSignificantBits());
			attrmods.appendTag(speed);
			tag.setTag("AttributeModifiers", attrmods);
			tag.setDouble("speedBoost", GTSonic.speedBoost);
		}
		tag.setBoolean("speedy", (GTSonic.speedBoost > 0 ? true : false));
	}
	@Override
	public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, int par4, int par5, int par6, int par7, float par8, float par9, float par10) {
		return false;
	}
	@Override
	public boolean onItemUseFirst(ItemStack stack, EntityPlayer player, World world, int cX, int cY, int cZ, int blockSideId, float hitX, float hitY, float hitZ) {
		int x = cX;
		int y = cY;
		int z = cZ;
		final int target = world.getBlockId(x, y, z);
		final Block blockTarget = Block.blocksList[target];
		final EnumBlockSide side = EnumBlockSide.getByInt(blockSideId);
		// Purpose: open doors
		if (blockTarget instanceof BlockDoor) {
			if ( !world.isRemote) {
				if ((world.getBlockId(x, y - 1, z) == target) && ((world.getBlockMetadata(x, y, z) & 8) != 0)) {
					// They hit the top half
					y -= 1;
				}
				int meta = world.getBlockMetadata(x, y, z);
				boolean isDoorOpen = (meta & 4) != 0;
				if (isDoorOpen) {
					if ( !world.isRemote) {
						world.setBlockMetadataWithNotify(x, y, z, meta ^ 4, 2);
					}
				}
				else {
					if ( !world.isRemote) {
						world.setBlockMetadataWithNotify(x, y, z, meta | 4, 2);
					}
				}
			}
			else {
				world.markBlockRangeForRenderUpdate(x, y, z, cX, cY, cZ);
			}
			if (world.isRemote) {
				return false;
			}
			return true;
		}
		else if (world.isRemote) {
			return false;
		}
		// Purpose: turn on redstone lamps
		else if (target == Block.redstoneLampIdle.blockID) {
			// Sneak to force it to remain in the current state, otherwise the state is toggled
			world.setBlock(x, y, z, player.isSneaking() ? GTSonic.redstoneLampInactiveId : GTSonic.redstoneLampActiveId);
		}
		else if (target == GTSonic.redstoneLampActiveId) {
			// Sneak to return it to the vanilla redstone lamp, otherwise the state is toggled
			world.setBlock(x, y, z, player.isSneaking() ? Block.redstoneLampActive.blockID : GTSonic.redstoneLampInactiveId);
		}
		// Purpose: turn off redstone lamps
		else if (target == Block.redstoneLampActive.blockID) {
			// Sneak to force it to remain in the current state, otherwise the state is toggled
			world.setBlock(x, y, z, player.isSneaking() ? GTSonic.redstoneLampActiveId : GTSonic.redstoneLampInactiveId);
		}
		else if (target == GTSonic.redstoneLampInactiveId) {
			// Sneak to return it to the vanilla redstone lamp, otherwise the state is toggled
			world.setBlock(x, y, z, player.isSneaking() ? Block.redstoneLampIdle.blockID : GTSonic.redstoneLampActiveId);
		}
		// Purpose: grow grass on dirt
		else if (target == Block.dirt.blockID) {
			if (side == EnumBlockSide.TOP) {
				world.setBlock(x, y, z, Block.grass.blockID, Block.grass.getDamageValue(world, x, y, z), 3);
			}
			else {
				new ChatMsg("Hrm, maybe the screwdriver is only effective on the top?").send(player);
			}
			return true;
		}
		// Purpose: sandstone > sand > glass > glass pane
		else if (target == Block.sandStone.blockID) {
			world.setBlock(x, y, z, Block.sand.blockID);
			return true;
		}
		else if (target == Block.sand.blockID) {
			world.setBlock(x, y, z, Block.glass.blockID);
			return true;
		}
		else if (target == Block.glass.blockID) {
			world.setBlock(x, y, z, Block.thinGlass.blockID);
			return true;
		}
		// Purpose: destroy ice
		else if (target == Block.ice.blockID) {
			if (player.isSneaking()) {
				world.setBlock(x, y, z, Block.waterMoving.blockID);
			}
			else {
				world.setBlock(x, y, z, 0);
			}
			return true;
		}
		// Purpose: ignite TNT
		else if (target == Block.tnt.blockID) {
			// Light that baby up!
			EntityTNTPrimed tnt;
			if (player.isSneaking()) {
				tnt = new EntityMiningTNT(world, x, y + 1, z, player);
			}
			else {
				tnt = new EntityTNTPrimed(world, x, y + 1, z, player);
			}
			new ChatMsg("Boom goes the dynamite, in " + (tnt.fuse / 20) + " seconds!").send(player);
			world.setBlock(x, y, z, 0);
			world.spawnEntityInWorld(tnt);
			return true;
		}
		// Purpose: stabilize cobble into smoothstone
		else if (target == Block.cobblestone.blockID) {
			world.setBlock(x, y, z, Block.stone.blockID);
			return true;
		}
		// Purpose: stabilize gravel into cobble
		else if (target == Block.gravel.blockID) {
			world.setBlock(x, y, z, Block.cobblestone.blockID);
			return true;
		}
		return false;
	}
	@Override
	public boolean onLeftClickEntity(ItemStack stack, EntityPlayer player, Entity entity) {
		if (player.worldObj.isRemote) {
			return true;
		}
		try {
			EntityLivingBase target = (EntityLivingBase) entity;
			int healthPercent = (int) ((100 / target.getMaxHealth()) * target.getHealth());
			if (healthPercent >= GTSonic.maxCreatureHealPercent) {
				new ChatMsg("You can't heal this creature any further").send(player);
			}
			else {
				target.setHealth(target.getHealth() + 1);
				int health = (int) ((100 / target.getMaxHealth()) * target.getHealth());
				new ChatMsg("This creature's health is now about " + health + "% maximum").send(player);
			}
			return true;
		}
		catch (ClassCastException e) {
			return true;
		}
	}
	@Override
	public boolean itemInteractionForEntity(ItemStack stack, EntityPlayer player, EntityLivingBase target) {
		if (player.worldObj.isRemote) {
			return true;
		}
		int health = (int) ((100 / target.getMaxHealth()) * target.getHealth());
		new ChatMsg("Scans indicate that this creature's health is at about " + health + "% maximum").send(player);
		return true;
	}
}
