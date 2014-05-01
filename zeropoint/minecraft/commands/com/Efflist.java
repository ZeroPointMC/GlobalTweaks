package zeropoint.minecraft.commands.com;


import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.Potion;
import zeropoint.minecraft.core.GTBaseCommand;


/**
 * [/efflist] List all effects to the console
 * 
 * @author Zero Point
 */
public class Efflist extends GTBaseCommand {
	@Override
	public String getCommandName() {
		return "efflist";
	}
	@Override
	public String getCommandHelp(ICommandSender src) {
		return "Lists all status effects to the console - purely debug";
	}
	@Override
	public String getCommandArgs(ICommandSender src) {
		return "";
	}
	@Override
	public void execute(ICommandSender src, EntityPlayer player, String[] args) {
		for (int i = 0; i < Potion.potionTypes.length; i++ ) {
			Potion pot = Potion.potionTypes[i];
			if (pot != null) {
				System.out.println("Potion " + i + ": " + pot.getName());
			}
		}
	}
	@Override
	public boolean isFinished() {
		return true;
	}
}
