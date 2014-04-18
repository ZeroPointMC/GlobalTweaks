package zeropoint.minecraft.commands.com;


import java.util.UUID;

import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import zeropoint.core.string.SystemClipboard;
import zeropoint.minecraft.core.GTBaseCommand;
import zeropoint.minecraft.core.util.ChatMsg;


/**
 * [/uuid [copy]] Generate a random UUID, optionally copy to clipboard
 * 
 * @author Zero Point
 */
public class Uuid extends GTBaseCommand {
	@Override
	public String getCommandName() {
		return "uuid";
	}
	@Override
	public String getCommandHelp(ICommandSender src) {
		return "Generates a random UUID, optionally copying it to the user's clipboard";
	}
	@Override
	public String getCommandArgs(ICommandSender src) {
		return "[copy]";
	}
	@Override
	public void execute(ICommandSender src, EntityPlayer player, String[] args) {
		UUID id = UUID.randomUUID();
		new ChatMsg("UUID: " + id.toString()).send(player);
		if (args.length > 0) {
			if (args[0].toLowerCase().equals("copy")) {
				SystemClipboard.setString(id.toString());
			}
		}
	}
	@Override
	public boolean isFinished() {
		return true;
	}
}
