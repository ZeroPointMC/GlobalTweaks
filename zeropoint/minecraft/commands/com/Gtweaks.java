package zeropoint.minecraft.commands.com;


import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import zeropoint.minecraft.commands.GTCommands;
import zeropoint.minecraft.core.GTBaseCommand;
import zeropoint.minecraft.core.util.ChatMsg;


/**
 * [/gtweak list] List commands registered with GlobalTweaks|Commands
 * 
 * @author Zero Point
 */
public class Gtweaks extends GTBaseCommand {
	@Override
	public String getCommandName() {
		return "gtweaks";
	}
	@Override
	public String getCommandArgs(ICommandSender src) {
		return "[list]";
	}
	@Override
	public boolean isFinished() {
		return true;
	}
	@Override
	public void execute(ICommandSender src, EntityPlayer player, String[] args) {
		if (args.length < 1) {
			this.call(src, player, "list");
			return;
		}
		String arg = args[0].toLowerCase();
		if (arg.equals("list")) {
			String[] cmdList = GTCommands.getCommands();
			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < (cmdList.length - 1); i++ ) {
				sb.append(cmdList[i] + ", ");
			}
			sb.append(cmdList[cmdList.length - 1]);
			new ChatMsg(sb.toString()).send(src);
		}
	}
	@Override
	public String getCommandHelp(ICommandSender src) {
		return "Base GlobalTweaks command";
	}
}
