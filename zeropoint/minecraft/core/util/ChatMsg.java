package zeropoint.minecraft.core.util;


import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatMessageComponent;


//TODO: write Javadoc
public class ChatMsg {
	public static final String BLACK = "§0";
	public static final String INDIGO = "§1";
	public static final String GREEN = "§2";
	public static final String BLUE = "§3";
	public static final String MAROON = "§4";
	public static final String PURPLE = "§5";
	public static final String GOLD = "§6";
	public static final String SILVER = "§7";
	public static final String GRAY = "§8";
	public static final String CYAN = "§9";
	public static final String LIME = "§a";
	public static final String SKY = "§b";
	public static final String RED = "§c";
	public static final String MAGENTA = "§d";
	public static final String YELLOW = "§e";
	public static final String WHITE = "§f";
	public static final String RANDOM = "§k";
	public static final String BOLD = "§l";
	public static final String STRIKE = "§m";
	public static final String UNDERLINE = "§n";
	public static final String ITALIC = "§o";
	public static final String NONE = "§r";
	protected ChatMessageComponent content;
	public ChatMsg(String msg) {
		this.content = new ChatMessageComponent();
		this.content.addText(msg);
	}
	public ChatMsg(ChatMessageComponent msg) {
		this.content = new ChatMessageComponent(msg);
	}
	@Override
	public String toString() {
		return this.content.toString();
	}
	public void send(ICommandSender target) {
		target.sendChatToPlayer(this.content);
	}
	public void send(EntityPlayer target) {
		target.addChatMessage(this.toString());
	}
}
