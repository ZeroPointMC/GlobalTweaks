package zeropoint.minecraft.commands.com;


import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import zeropoint.minecraft.core.GTBaseCommand;
import zeropoint.minecraft.core.util.ChatMsg;


/**
 * [/vanish [on|off|toggle]] Control invisibility
 * 
 * @author Zero Point
 */
public class Vanish extends GTBaseCommand {
	@Override
	public String getCommandName() {
		return "vanish";
	}
	@Override
	public String getCommandHelp(ICommandSender src) {
		return "Controls invisibility";
	}
	@Override
	public String getCommandArgs(ICommandSender src) {
		return "[on|off|toggle]";
	}
	@Override
	public boolean isFinished() {
		return true;
	}
	@Override
	public void execute(ICommandSender src, EntityPlayer player, String[] args) {
		if (args.length > 0) {
			String arg = args[0].toLowerCase();
			if (arg.equals("on")) {
				player.setInvisible(true);
			}
			else if (arg.equals("off")) {
				player.setInvisible(false);
			}
			else if (arg.equals("toggle")) {
				player.setInvisible( !player.isInvisible());
			}
			else {
				new ChatMsg(this.getCommandUsage(src)).send(src);
			}
		}
		else {
			player.setInvisible( !player.isInvisible());
		}
	}
}
