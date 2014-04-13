package zeropoint.minecraft.commands.com;


import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import zeropoint.minecraft.core.GTBaseCommand;
import zeropoint.minecraft.core.util.ChatMsg;


public class God extends GTBaseCommand {
	public String getCommandName() {
		return "god";
	}
	@Override
	public String getCommandHelp(ICommandSender src) {
		return "Controls player damage";
	}
	@Override
	public String getCommandArgs(ICommandSender src) {
		return "[on|off]";
	}
	@Override
	public boolean isFinished() {
		return true;
	}
	@Override
	public void execute(ICommandSender src, EntityPlayer player, String[] args) {
		NBTTagCompound tag = new NBTTagCompound();
		NBTTagCompound plr = new NBTTagCompound();
		player.capabilities.writeCapabilitiesToNBT(tag);
		player.writeEntityToNBT(plr);
		if (args.length > 0) {
			String arg = args[0].toLowerCase();
			if (arg.equals("on")) {
				tag.setBoolean("invulnerable", true);
				plr.setBoolean("Invulnerable", true);
			}
			else if (arg.equals("off")) {
				tag.setBoolean("invulnerable", false);
				plr.setBoolean("Invulnerable", false);
			}
			else {
				sendUsageMessage(src);
				return;
			}
		}
		else {
			new ChatMsg("Damage is currently " + (player.capabilities.disableDamage ? "dis" : "en") + "abled").send(src);
			return;
		}
		player.readEntityFromNBT(plr);
		player.capabilities.readCapabilitiesFromNBT(tag);
		player.capabilities.disableDamage = tag.getBoolean("invulnerable") || plr.getBoolean("Invulnerable");
		player.sendPlayerAbilities();
		new ChatMsg("Damage " + (player.capabilities.disableDamage ? "dis" : "en") + "abled").send(src);
	}
}
