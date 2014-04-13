package zeropoint.minecraft.core;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import zeropoint.minecraft.core.util.ChatMsg;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;


public abstract class GTBaseCommand extends CommandBase {
	protected List<String> aliases = new ArrayList<String>();
	abstract public String getCommandHelp(ICommandSender src);
	abstract public String getCommandArgs(ICommandSender src);
	abstract public void execute(ICommandSender src, EntityPlayer player, String[] args);
	public String getCommandUsage(ICommandSender src) {
		return getCommandNameSlash() + " " + getCommandArgs(src);
	}
	public boolean getDefaultState() {
		return this.isFinished();
	}
	abstract public boolean isFinished();
	public final String getCommandNamePlain() {
		return this.getCommandName().replaceAll("^/", "");
	}
	public final String getCommandNameSlash() {
		return "/" + this.getCommandNamePlain();
	}
	public final String getCommandUsagePlain() {
		return this.getCommandUsage(null).replaceAll("^/", "");
	}
	public final String getCommandUsageSlash() {
		return "/" + this.getCommandUsagePlain();
	}
	public final void sendUsageMessage(ICommandSender target) {
		new ChatMsg("Usage: " + this.getCommandUsageSlash()).send(target);
	}
	public final void sendUsageMessage(EntityPlayer target) {
		new ChatMsg("Usage: " + this.getCommandUsageSlash()).send(target);
	}
	public final void processCommand(ICommandSender src, String[] args) {
		try {
			this.execute(src, GTCore.getPlayerEntity(src), args);
		}
		catch (Exception e) {
			new ChatMsg("An exception was detected while executing this command:").send(src);
			new ChatMsg(e.toString()).send(src);
			e.printStackTrace();
		}
	}
	@Override
	public int getRequiredPermissionLevel() {
		return 0;
	}
	@Override
	public List getCommandAliases() {
		List<String> tmp = new ArrayList<String>();
		Iterator<String> iter = aliases.iterator();
		while (iter.hasNext()) {
			tmp.add(iter.next());
		}
		return tmp;
	}
	public String[] getAliases() {
		return aliases.toArray(new String[] {});
	}
	protected void addAlias(String... alias) {
		aliases.addAll(aliases);
	}
	protected void removeAlias(String... alias) {
		for (String a : alias) {
			aliases.remove(a);
		}
	}
	protected void clearAliases() {
		aliases.clear();
	}
	protected void removeAllAliasesExcept(String... alias) {
		aliases.retainAll(Arrays.asList(alias));
	}
	public boolean hasAlias(String test) {
		return aliases.contains(test);
	}
}
