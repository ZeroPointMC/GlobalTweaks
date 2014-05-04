// May 4, 2014 12:49:21 PM
package zeropoint.minecraft.commands.com;


import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.ContainerWorkbench;
import net.minecraft.network.packet.Packet100OpenWindow;
import net.minecraft.world.World;
import zeropoint.minecraft.core.GTBaseCommand;


@SuppressWarnings("javadoc")
public class Craft extends GTBaseCommand {
	public static class GlobalWorkbench extends ContainerWorkbench {
		public GlobalWorkbench(InventoryPlayer inv, World world, int x, int y, int z) {
			super(inv, world, x, y, z);
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
		return "Opens a 3x3 crafting grid";
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
			p.openContainer = new GlobalWorkbench(p.inventory, p.worldObj, (int) p.posX, (int) p.posY, (int) p.posZ);
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
