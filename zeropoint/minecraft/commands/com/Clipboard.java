package zeropoint.minecraft.commands.com;


import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import zeropoint.core.string.SystemClipboard;
import zeropoint.minecraft.core.GTBaseCommand;
import zeropoint.minecraft.core.util.ChatMsg;


public class Clipboard extends GTBaseCommand {
	public static final String GET = "get".intern();
	public static final String SET = "set".intern();
	@Override
	public String getCommandName() {
		return "clipboard";
	}
	@Override
	public String getCommandHelp(ICommandSender src) {
		return "Manipulates the user's clipboard";
	}
	@Override
	public String getCommandArgs(ICommandSender src) {
		return "{get <text>|set <text>}";
	}
	@Override
	public void execute(ICommandSender src, EntityPlayer player, String[] args) {
		try {
			String action = args[0].toLowerCase().intern();
			if (action == GET) {
				String clipboard = SystemClipboard.getString();
				if (clipboard == null) {
					new ChatMsg("<Clipboard empty>").send(player);
				}
				else {
					new ChatMsg("<Clipboard> " + clipboard).send(player);
				}
			}
			else if (action == SET) {
				StringBuffer sb = new StringBuffer();
				for (int i = 1; i < args.length; i++ ) {
					sb.append(sb.toString().isEmpty() ? "" : " ");
					sb.append(args[i]);
				}
				if (sb.toString().isEmpty()) {
					sendUsageMessage(player);
				}
				else {
					SystemClipboard.setString(sb.toString());
				}
			}
		}
		catch (ArrayIndexOutOfBoundsException e) {
			sendUsageMessage(player);
		}
	}
	@Override
	public boolean isFinished() {
		return true;
	}
}
