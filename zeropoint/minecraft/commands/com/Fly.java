package zeropoint.minecraft.commands.com;


import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import zeropoint.minecraft.core.GTBaseCommand;
import zeropoint.minecraft.core.util.ChatMsg;


/**
 * [/fly [on|off]] Toggle player flight capability
 * 
 * @author Zero Point
 */
public class Fly extends GTBaseCommand {
	@Override
	public void execute(ICommandSender src, EntityPlayer player, String[] args) {
		NBTTagCompound tag = new NBTTagCompound();
		player.capabilities.writeCapabilitiesToNBT(tag);
		if (args.length > 0) {
			MinecraftServer.getServer().setAllowFlight(true);
			String arg = args[0].toLowerCase();
			if (arg.equals("on")) {
				tag.setBoolean("mayfly", true);
			}
			else if (arg.equals("off")) {
				tag.setBoolean("mayfly", false);
				tag.setBoolean("flying", false);
			}
			// Undocumented!
			else if (arg.equals("toggle")) {
				boolean mayfly = tag.getBoolean("mayfly");
				tag.setBoolean("mayfly", !mayfly);
			}
			else {
				sendUsageMessage(src);
				return;
			}
		}
		else {
			new ChatMsg("Flying is currently " + (MinecraftServer.getServer().isFlightAllowed() && player.capabilities.allowFlying ? "en" : "dis") + "abled").send(src);
			return;
		}
		player.capabilities.readCapabilitiesFromNBT(tag);
		player.capabilities.allowFlying = tag.getBoolean("mayfly");
		player.capabilities.isFlying = tag.getBoolean("flying");
		player.sendPlayerAbilities();
		new ChatMsg("Flying " + (player.capabilities.allowFlying ? "en" : "dis") + "abled").send(src);
	}
	@Override
	public String getCommandArgs(ICommandSender src) {
		return "[on|off]";
	}
	@Override
	public String getCommandHelp(ICommandSender src) {
		return "Control player's ability to fly";
	}
	public String getCommandName() {
		return "fly";
	}
	@Override
	public boolean isFinished() {
		return true;
	}
}
