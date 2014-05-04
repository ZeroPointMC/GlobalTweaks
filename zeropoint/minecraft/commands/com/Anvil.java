// May 4, 2014 1:15:39 PM
package zeropoint.minecraft.commands.com;


import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.ContainerRepair;
import net.minecraft.network.packet.Packet100OpenWindow;
import net.minecraft.world.World;
import zeropoint.minecraft.core.GTBaseCommand;


@SuppressWarnings("javadoc")
public class Anvil extends GTBaseCommand {
	public static class GlobalAnvil extends ContainerRepair {
		public GlobalAnvil(InventoryPlayer inv, World world, int x, int y, int z, EntityPlayer player) {
			super(inv, world, x, y, z, player);
		}
		@Override
		public boolean canInteractWith(EntityPlayer par1EntityPlayer) {
			return true;
		}
	}
	@Override
	public String getCommandName() {
		return "craft";
	}
	@Override
	public String getCommandHelp(ICommandSender src) {
		return "Opens an anvil";
	}
	@Override
	public String getCommandArgs(ICommandSender src) {
		return "";
	}
	@Override
	public void execute(ICommandSender src, EntityPlayer player, String[] args) {
		try {
			EntityPlayerMP p = (EntityPlayerMP) player;
			p.incrementWindowID();
			p.playerNetServerHandler.sendPacketToPlayer(new Packet100OpenWindow(p.currentWindowId, 1, "Crafting", 9, true));
			p.openContainer = new GlobalAnvil(p.inventory, p.worldObj, (int) p.posX, (int) p.posY, (int) p.posZ, p);
			p.openContainer.windowId = p.currentWindowId;
			p.openContainer.addCraftingToCrafters(p);
		}
		catch (ClassCastException e) {}
	}
	@Override
	public boolean isFinished() {
		return true;
	}
}
