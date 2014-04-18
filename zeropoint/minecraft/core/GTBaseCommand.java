package zeropoint.minecraft.core;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import zeropoint.minecraft.core.util.ChatMsg;


/**
 * Abstract base class for chat commands
 * 
 * @author Zero Point
 */
public abstract class GTBaseCommand extends CommandBase {
	private List<String> aliases = new ArrayList<String>();
	/**
	 * Get information about what the command is for
	 * 
	 * @param src
	 *            - the {@link ICommandSender} that sent the help request
	 * @return information about the purpose of the command
	 */
	abstract public String getCommandHelp(ICommandSender src);
	/**
	 * Get information about the arguments to the command
	 * 
	 * @param src
	 *            - the {@link ICommandSender} that sent the help request
	 * @return information about the arguments for the command
	 */
	abstract public String getCommandArgs(ICommandSender src);
	/**
	 * Execute the command
	 * 
	 * @param src
	 *            - the {@link ICommandSender} that tried to use the command
	 * @param player
	 *            - the {@link EntityPlayer} that used the command, or <code>null</code> if it wasn't used by a player
	 * @param args
	 *            - the arguments to the command
	 */
	abstract public void execute(ICommandSender src, EntityPlayer player, String[] args);
	public String getCommandUsage(ICommandSender src) {
		return getCommandNameSlash() + " " + getCommandArgs(src);
	}
	/**
	 * @return <code>true</code> to indicate that this command should be registered by default, <code>false</code> otherwise
	 */
	public boolean getDefaultState() {
		return this.isFinished();
	}
	/**
	 * @return <code>true</code> to indicate that the command is fully functional
	 */
	abstract public boolean isFinished();
	/**
	 * @return the name of the command <i>without</i> the leading slash
	 */
	public final String getCommandNamePlain() {
		return this.getCommandName().replaceAll("^/", "");
	}
	/**
	 * @return the name of the command <i>with</i> the leading slash
	 */
	public final String getCommandNameSlash() {
		return "/" + this.getCommandNamePlain();
	}
	/**
	 * @return the usage message of the command <i>without</i> the leading slash
	 */
	public final String getCommandUsagePlain() {
		return this.getCommandUsage(null).replaceAll("^/", "");
	}
	/**
	 * @return the usage message of the command <i>with</i> the leading slash
	 */
	public final String getCommandUsageSlash() {
		return "/" + this.getCommandUsagePlain();
	}
	/**
	 * Send a usage message to the {@link ICommandSender} that used the command
	 * 
	 * @param target
	 *            - the ICommandSender that used the command
	 */
	public final void sendUsageMessage(ICommandSender target) {
		new ChatMsg("Usage: " + this.getCommandUsageSlash()).send(target);
	}
	/**
	 * Send a usage message to the player that used the command
	 * 
	 * @param target
	 *            - the {@link EntityPlayer} that used the command
	 */
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
	public final List<String> getCommandAliases() {
		List<String> tmp = new ArrayList<String>();
		Iterator<String> iter = this.aliases.iterator();
		while (iter.hasNext()) {
			tmp.add(iter.next());
		}
		return tmp;
	}
	/**
	 * @return a <code>String</code> array of aliases for this command
	 */
	public final String[] getAliases() {
		return this.aliases.toArray(new String[] {});
	}
	/**
	 * Add one or more aliases for this command (uses varargs)
	 * 
	 * @param alias
	 *            - the alias(es) for this command
	 */
	protected final void addAlias(String... alias) {
		this.aliases.addAll(this.aliases);
	}
	/**
	 * Remove one or more aliases from this command (uses varargs)
	 * 
	 * @param alias
	 *            - the alias(es) to remove from this command
	 */
	protected final void removeAlias(String... alias) {
		for (String a : alias) {
			this.aliases.remove(a);
		}
	}
	/**
	 * Remove all aliases from this command
	 */
	protected final void clearAliases() {
		this.aliases.clear();
	}
	/**
	 * Remove all aliases from this command, except those specified
	 * 
	 * @param alias
	 *            - the alias(es) to keep
	 */
	protected final void removeAllAliasesExcept(String... alias) {
		this.aliases.retainAll(Arrays.asList(alias));
	}
	/**
	 * Determine whether this command has the given alias
	 * 
	 * @param test
	 *            - the alias to check for
	 * @return <code>true</code> if the command has the given alias, <code>false</code> otherwise
	 */
	public final boolean hasAlias(String test) {
		return this.aliases.contains(test);
	}
}
