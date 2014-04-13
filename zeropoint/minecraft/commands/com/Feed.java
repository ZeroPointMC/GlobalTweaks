package zeropoint.minecraft.commands.com;


import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import zeropoint.minecraft.core.GTBaseCommand;
import zeropoint.minecraft.core.util.manip.PlayerHelper;


public class Feed extends GTBaseCommand {
	public String getCommandName() {
		return "feed";
	}
	@Override
	public String getCommandHelp(ICommandSender src) {
		return "Sets the player's food level";
	}
	@Override
	public String getCommandArgs(ICommandSender src) {
		return "[1-20]";
	}
	@Override
	public boolean isFinished() {
		return true;
	}
	@Override
	public void execute(ICommandSender src, EntityPlayer player, String[] args) {
		if (args.length < 1) {
			player.getFoodStats().setFoodLevel(20);
			player.getFoodStats().setFoodSaturationLevel(20);
		}
		else {
			int arg;
			try {
				arg = Integer.parseInt(args[0]);
			}
			catch (Exception e) {
				arg = 1;
			}
			PlayerHelper.setPlayerFood(player, arg);
		}
	}
}
