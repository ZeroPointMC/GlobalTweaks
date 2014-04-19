package zeropoint.minecraft.core.util;


import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatMessageComponent;


/**
 * Quick utility to make it easier to make and send chat messages
 * 
 * @author Zero Point
 */
public class ChatMsg {
	/**
	 * Format code for Black (000000)
	 */
	public static final String BLACK = "§0";
	/**
	 * Format code for Dark Blue (0000AA)
	 */
	public static final String INDIGO = "§1";
	/**
	 * Format code for Dark Green (00AA00)
	 */
	public static final String GREEN = "§2";
	/**
	 * Format code for Dark Aqua (00AAAA)
	 */
	public static final String BLUE = "§3";
	/**
	 * Format code for Dark Red (AA0000)
	 */
	public static final String MAROON = "§4";
	/**
	 * Format code for Dark Purple (AA00AA)
	 */
	public static final String PURPLE = "§5";
	/**
	 * Format code for Gold (FFAA00)
	 */
	public static final String GOLD = "§6";
	/**
	 * Format code for Gray (AAAAAA)
	 */
	public static final String SILVER = "§7";
	/**
	 * Format code for Dark Gray (555555)
	 */
	public static final String GRAY = "§8";
	/**
	 * Format code for Blue (5555FF)
	 */
	public static final String CYAN = "§9";
	/**
	 * Format code for Green (55FF55)
	 */
	public static final String LIME = "§a";
	/**
	 * Format code for Aqua (55FFFF)
	 */
	public static final String SKY = "§b";
	/**
	 * Format code for Red (FF5555)
	 */
	public static final String RED = "§c";
	/**
	 * Format code for Light Purple (FF55FF)
	 */
	public static final String MAGENTA = "§d";
	/**
	 * Format code for Yellow (FFFF55)
	 */
	public static final String YELLOW = "§e";
	/**
	 * Format code for White (FFFFFF)
	 */
	public static final String WHITE = "§f";
	/**
	 * Format code for Obfuscation (random characters)
	 */
	public static final String RANDOM = "§k";
	/**
	 * Format code for Bold
	 */
	public static final String BOLD = "§l";
	/**
	 * Format code for Strikethrough
	 */
	public static final String STRIKE = "§m";
	/**
	 * Format code for Underline
	 */
	public static final String UNDERLINE = "§n";
	/**
	 * Format code for Italic
	 */
	public static final String ITALIC = "§o";
	/**
	 * Format code for Reset
	 */
	public static final String NONE = "§r";
	/**
	 * The content of the message
	 */
	protected ChatMessageComponent content;
	/**
	 * @param msg
	 *            - the content to send in the message
	 */
	public ChatMsg(String msg) {
		this.content = new ChatMessageComponent();
		this.content.addText(msg);
	}
	/**
	 * @param msg
	 *            - the content of the message
	 */
	public ChatMsg(ChatMessageComponent msg) {
		this.content = new ChatMessageComponent(msg);
	}
	@Override
	public String toString() {
		return this.content.toString();
	}
	/**
	 * @param target
	 *            - the {@link ICommandSender} to send the message to
	 */
	public void send(ICommandSender target) {
		target.sendChatToPlayer(this.content);
	}
	/**
	 * @param target
	 *            - the {@link EntityPlayer} to send the message to
	 */
	public void send(EntityPlayer target) {
		target.addChatMessage(this.toString());
	}
}
