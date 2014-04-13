package zeropoint.minecraft.commands.com;


import net.minecraft.command.ICommandSender;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.player.EntityPlayer;
import zeropoint.minecraft.core.GTBaseCommand;


public class Enchlist extends GTBaseCommand {
	@Override
	public String getCommandName() {
		return "enchlist";
	}
	@Override
	public String getCommandHelp(ICommandSender src) {
		return "Lists all enchantments to the console - purely debug";
	}
	@Override
	public String getCommandArgs(ICommandSender src) {
		return "";
	}
	@Override
	public void execute(ICommandSender src, EntityPlayer player, String[] args) {
		for (int i = 0; i < Enchantment.enchantmentsList.length; i++ ) {
			Enchantment ench = Enchantment.enchantmentsList[i];
			if (ench != null) {
				System.out.println("Enchant " + i + ": " + ench.getTranslatedName(1).replaceAll("\\s+\\w\\w?$", ""));
			}
		}
	}
	@Override
	public boolean isFinished() {
		return true;
	}
}
