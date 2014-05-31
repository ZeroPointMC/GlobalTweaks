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
	 * Format code for Dark Blue (<span style="color:#0000AA;">0000AA</span>)
	 */
	public static final String INDIGO = "§1";
	/**
	 * Format code for Dark Green (<span style="color:#00AA00;">00AA00</span>)
	 */
	public static final String GREEN = "§2";
	/**
	 * Format code for Dark Aqua (<span style="color:#00AAAA;">00AAAA</span>)
	 */
	public static final String BLUE = "§3";
	/**
	 * Format code for Dark Red (<span style="color:#AA0000;">AA0000</span>)
	 */
	public static final String MAROON = "§4";
	/**
	 * Format code for Dark Purple (<span style="color:#AA00AA;">AA00AA</span>)
	 */
	public static final String PURPLE = "§5";
	/**
	 * Format code for Gold (<span style="color:#FFAA00;">FFAA00</span>)
	 */
	public static final String GOLD = "§6";
	/**
	 * Format code for Gray (<span style="color:#AAAAAA;">AAAAAA</span>)
	 */
	public static final String SILVER = "§7";
	/**
	 * Format code for Dark Gray (<span style="color:#555555;">555555</span>)
	 */
	public static final String GRAY = "§8";
	/**
	 * Format code for Blue (<span style="color:#5555FF;">5555FF</span>)
	 */
	public static final String CYAN = "§9";
	/**
	 * Format code for Green (<span style="color:#55FF55;">55FF55</span>)
	 */
	public static final String LIME = "§a";
	/**
	 * Format code for Aqua (<span style="color:#55FFFF;">55FFFF</span>)
	 */
	public static final String SKY = "§b";
	/**
	 * Format code for Red (<span style="color:#FF5555;">FF5555</span>)
	 */
	public static final String RED = "§c";
	/**
	 * Format code for Light Purple (<span style="color:#FF55FF;">FF55FF</span>)
	 */
	public static final String MAGENTA = "§d";
	/**
	 * Format code for Yellow (<span style="color:#FFFF55;">FFFF55</span>)
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
	 * Format code for <b>Bold</b>
	 */
	public static final String BOLD = "§l";
	/**
	 * Format code for <span style="text-decoration:line-through;">Strikethrough</span>
	 */
	public static final String STRIKE = "§m";
	/**
	 * Format code for <u>Underline</u>
	 */
	public static final String UNDERLINE = "§n";
	/**
	 * Format code for <i>Italic</i>
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
