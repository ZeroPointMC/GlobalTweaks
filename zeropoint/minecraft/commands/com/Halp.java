package zeropoint.minecraft.commands.com;


import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import zeropoint.minecraft.commands.GTCommands;
import zeropoint.minecraft.core.GTBaseCommand;
import zeropoint.minecraft.core.util.ChatMsg;


/**
 * [/halp &lt;command>] Get extended command help
 * 
 * @author Zero Point
 */
public class Halp extends GTBaseCommand {
	@Override
	public String getCommandName() {
		return "halp";
	}
	@Override
	public String getCommandArgs(ICommandSender src) {
		return "<command>";
	}
	@Override
	public boolean isFinished() {
		return true;
	}
	@Override
	public void execute(ICommandSender src, EntityPlayer player, String[] args) {
		if (args.length < 0) {
			this.sendUsageMessage(src);
		}
		else {
			GTBaseCommand cmd = GTCommands.getCommand(args[0]);
			if (cmd != null) {
				String usage = cmd.getCommandUsage(src);
				new ChatMsg(usage.startsWith("/") ? usage : "/" + usage).send(src);
				new ChatMsg(cmd.getCommandHelp(src)).send(src);
			}
			else {
				new ChatMsg("Can't get extended help for " + (args[0].startsWith("/") ? args[0] : "/" + args[0])).send(src);
			}
		}
	}
	@Override
	public String getCommandHelp(ICommandSender src) {
		return "Displays extended help for commands registered with GlobalTweaks|Commands";
	}
}
