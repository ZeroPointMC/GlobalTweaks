package zeropoint.minecraft.commands.com;


import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import zeropoint.minecraft.core.GTBaseCommand;
import zeropoint.minecraft.core.util.ChatMsg;
import zeropoint.minecraft.core.util.manip.PlayerHelper;


/**
 * [/repair [hand|hotbar|inv|armo[u]r|all]] Repair item(s) by setting meta to zero
 * 
 * @author Zero Point
 */
public class Repair extends GTBaseCommand {
	public String getCommandName() {
		return "repair";
	}
	@Override
	public String getCommandHelp(ICommandSender src) {
		return "Repairs the specified item[s] (by setting metadata to zero)";
	}
	@Override
	public String getCommandArgs(ICommandSender src) {
		return "[hand|hotbar|inv|armo[u]r|all]";
	}
	@Override
	public boolean isFinished() {
		return true;
	}
	@Override
	public void execute(ICommandSender src, EntityPlayer player, String[] args) {
		if (args.length > 0) {
			String arg = args[0].toLowerCase();
			if (arg.equals("hand")) {
				PlayerHelper.repairHeld(player);
			}
			else if (arg.equals("hotbar")) {
				PlayerHelper.repairHotbar(player);
			}
			else if (arg.equals("inv")) {
				PlayerHelper.repairInventory(player);
			}
			else if (arg.equals("armor") || arg.equals("armour")) {
				PlayerHelper.repairArmour(player);
			}
			else if (arg.equals("all")) {
				PlayerHelper.repairAll(player);
			}
			else {
				new ChatMsg(getCommandUsage(src)).send(src);
			}
		}
		else {
			PlayerHelper.repairHeld(player);
		}
	}
}
