package zeropoint.minecraft.commands.com;


import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import zeropoint.minecraft.core.GTBaseCommand;
import zeropoint.minecraft.core.util.ChatMsg;
import zeropoint.minecraft.core.util.WrittenBookTag;


public class Book extends GTBaseCommand {
	public String getCommandName() {
		return "book";
	}
	@Override
	public String getCommandHelp(ICommandSender src) {
		return "Multipurpose book editor";
	}
	@Override
	public String getCommandArgs(ICommandSender src) {
		return "{unsign|author <new author>|title <new title>}";
	}
	@Override
	public void execute(ICommandSender src, EntityPlayer player, String[] args) {
		if ((player == null) || (args.length < 1)) {
			return;
		}
		ItemStack held = player.getCurrentEquippedItem();
		int heldID = held.itemID;
		if (args[0].equalsIgnoreCase("clear")) {
			if ((heldID == Item.writableBook.itemID)) {
				NBTTagList pages = new NBTTagList();
				pages.appendTag(new NBTTagString("", ""));
				held.stackTagCompound.setTag("pages", pages);
				return;
			}
			new ChatMsg("Can't clear a signed book!").send(player);
		}
		if (heldID != Item.writtenBook.itemID) {
			new ChatMsg("That's not a book, silly!").send(player);
			return;
		}
		if (args[0].equalsIgnoreCase("unsign")) {
			held.itemID = Item.writableBook.itemID;
		}
		else if (args.length < 2) {
			return;
		}
		else if (args[0].equalsIgnoreCase("title")) {
			StringBuffer newTitle = new StringBuffer(args[1]);
			if (args.length > 2) {
				for (int i = 2; i < args.length; i++ ) {
					newTitle.append(" " + args[i]);
				}
			}
			WrittenBookTag.setTitle(held, newTitle.toString().replaceAll("&&", "§"));
		}
		else if (args[0].equalsIgnoreCase("author")) {
			StringBuffer newAuthor = new StringBuffer(args[1]);
			if (args.length > 2) {
				for (int i = 2; i < args.length; i++ ) {
					newAuthor.append(" " + args[i]);
				}
			}
			WrittenBookTag.setAuthor(held, newAuthor.toString().replaceAll("&&", "§"));
		}
	}
	@Override
	public boolean isFinished() {
		return true;
	}
}
