package zeropoint.minecraft.commands.com;


import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import zeropoint.minecraft.core.GTBaseCommand;


/**
 * [/air] Refills the player's air supply
 * 
 * @author Zero Point
 */
public class Air extends GTBaseCommand {
	public String getCommandName() {
		return "air";
	}
	@Override
	public String getCommandHelp(ICommandSender src) {
		return "Refills the player's air supply";
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
		player.setAir(300);
	}
}
