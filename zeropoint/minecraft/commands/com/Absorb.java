package zeropoint.minecraft.commands.com;


import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import zeropoint.minecraft.core.GTBaseCommand;
import zeropoint.minecraft.core.util.manip.PlayerHelper;


/**
 * [/absorb &lt;amount>] Sets the player's absorption hearts
 * 
 * @author Zero Point
 */
public class Absorb extends GTBaseCommand {
	@Override
	public String getCommandName() {
		return "absorb";
	}
	@Override
	public String getCommandHelp(ICommandSender src) {
		return "Sets the number of absorption hearts a player has";
	}
	@Override
	public String getCommandArgs(ICommandSender src) {
		return "[0-" + PlayerHelper.MAX_OVERHEAL + "]";
	}
	@Override
	public boolean isFinished() {
		return true;
	}
	@Override
	public void execute(ICommandSender src, EntityPlayer player, String[] args) {
		if (args.length < 1) {
			player.setAbsorptionAmount(0);
		}
		else {
			int arg;
			try {
				arg = Integer.parseInt(args[0]);
			}
			catch (Exception e) {
				arg = 0;
			}
			if (arg > 80) {
				arg = 80;
			}
			else if (arg < 0) {
				arg = 0;
			}
			player.setAbsorptionAmount(arg);
		}
	}
}
