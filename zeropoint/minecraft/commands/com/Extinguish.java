package zeropoint.minecraft.commands.com;


import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import zeropoint.minecraft.core.GTBaseCommand;


/**
 * [/extinguish] Put out player fire
 * 
 * @author Zero Point
 */
public class Extinguish extends GTBaseCommand {
	@Override
	public String getCommandName() {
		return "extinguish";
	}
	@Override
	public String getCommandHelp(ICommandSender src) {
		return "Extinguishes the player if on fire";
	}
	@Override
	public String getCommandArgs(ICommandSender src) {
		return "";
	}
	@Override
	public boolean isFinished() {
		return true;
	}
	@Override
	public void execute(ICommandSender src, EntityPlayer player, String[] args) {
		player.extinguish();
	}
}
