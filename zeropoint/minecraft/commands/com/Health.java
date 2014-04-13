package zeropoint.minecraft.commands.com;


import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import zeropoint.minecraft.core.GTBaseCommand;
import zeropoint.minecraft.core.GTCore;
import zeropoint.minecraft.core.util.manip.PlayerHelper;


public class Health extends GTBaseCommand {
	public String getCommandName() {
		return "health";
	}
	@Override
	public String getCommandHelp(ICommandSender src) {
		return "Sets the player health (adding absorbtion if needed)";
	}
	@Override
	public String getCommandArgs(ICommandSender src) {
		StringBuffer _ret = new StringBuffer("[1-");
		_ret.append(Math.round(Math.floor(PlayerHelper.hpMax(GTCore.getPlayerEntity(src)))));
		_ret.append("]");
		return _ret.toString();
	}
	@Override
	public boolean isFinished() {
		return true;
	}
	@Override
	public void execute(ICommandSender src, EntityPlayer player, String[] args) {
		if (args.length < 1) {
			player.setHealth(player.getMaxHealth());
		}
		else {
			int arg;
			try {
				arg = Integer.parseInt(args[0]);
			}
			catch (Exception e) {
				arg = 1;
			}
			PlayerHelper.setPlayerHealth(player, arg);
		}
	}
}
