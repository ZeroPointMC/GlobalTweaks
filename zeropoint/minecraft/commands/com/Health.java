package zeropoint.minecraft.commands.com;


import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import zeropoint.minecraft.core.GTBaseCommand;
import zeropoint.minecraft.core.GTCore;
import zeropoint.minecraft.core.util.ChatMsg;
import zeropoint.minecraft.core.util.manip.PlayerHelper;


/**
 * [/health [max] [&lt;amount>]] Set user's health
 * 
 * @author Zero Point
 */
public class Health extends GTBaseCommand {
	@Override
	public String getCommandName() {
		return "health";
	}
	@Override
	public String getCommandHelp(ICommandSender src) {
		return "Sets the player health, increasing max health if needed";
	}
	@Override
	public String getCommandArgs(ICommandSender src) {
		StringBuffer _ret = new StringBuffer("{max [<amount>]|[1-");
		_ret.append(Math.round(Math.floor(PlayerHelper.hpMax(GTCore.getPlayerEntity(src)))));
		_ret.append("]}");
		return _ret.toString();
	}
	@Override
	public boolean isFinished() {
		return true;
	}
	@Override
	public void execute(ICommandSender src, EntityPlayer player, String[] args) {
		if (args.length < 1) {
			player.setHealth(20);
		}
		else if ((args.length == 1) && args[0].equalsIgnoreCase("max")) {
			this.call(src, player, String.valueOf((int) player.getMaxHealth()));
		}
		else if ((args.length >= 2) && args[0].equalsIgnoreCase("max")) {
			try {
				int amnt = Integer.parseInt(args[1]);
				PlayerHelper.setPlayerMaxHealth(player, amnt < 1 ? 1 : amnt);
				player.setHealth(player.getHealth() > player.getMaxHealth() ? player.getMaxHealth() : player.getHealth());
				return;
			}
			catch (NumberFormatException e) {
				new ChatMsg(ChatMsg.RED + "Must provide integer argument for health").send(player);
			}
		}
		else if ((args.length == 1) && args[0].equalsIgnoreCase("get")) {
			new ChatMsg(ChatMsg.BLUE + "Health: " + ChatMsg.LIME + player.getHealth() + ChatMsg.NONE + "/" + ChatMsg.RED + player.getMaxHealth()).send(player);
		}
		else if ((args.length >= 1) && args[0].matches("^\\d+$")) {
			int arg;
			try {
				arg = Integer.parseInt(args[0]);
				PlayerHelper.setPlayerHealthWithOverheal(player, arg);
			}
			catch (Exception e) {
				this.call(src, player);
				return;
			}
		}
		else {
			new ChatMsg(ChatMsg.RED + "Invalid arguments").send(player);
			this.sendUsageMessage(player);
		}
	}
}
