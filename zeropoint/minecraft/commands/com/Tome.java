// May 18, 2014 9:41:36 AM
package zeropoint.minecraft.commands.com;


import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;

import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import zeropoint.core.io.file.InputFile;
import zeropoint.core.io.file.OutputFile;
import zeropoint.minecraft.core.GTBaseCommand;
import zeropoint.minecraft.core.GTCore;
import zeropoint.minecraft.core.util.ChatMsg;
import zeropoint.minecraft.core.util.WrittenBookTag;
import zeropoint.minecraft.core.util.manip.PlayerHelper;
import zeropoint.minecraft.core.util.manip.WorldHelper;


@SuppressWarnings("javadoc")
public class Tome extends GTBaseCommand {
	protected static String path = "";
	public static double loadBlastSize = 1.0;
	public static boolean loadBlastGrief = false;
	public static double failBlastMultiplier = 2.5;
	public static boolean failBlastGrief = true;
	public Tome() {
		super();
		path = GTCore.getConfig().dir("tomes", "path", "config/gtweaks/tomes/", "The directory that holds the tomes (in text files)");
	}
	@Override
	public String getCommandName() {
		return "tome";
	}
	@Override
	public String getCommandHelp(ICommandSender src) {
		return "Save and load books from disk";
	}
	@Override
	public String getCommandArgs(ICommandSender src) {
		return "{help|save [<name>]|load [<name>]|debug [<name>]|list}";
	}
	@Override
	public void execute(ICommandSender src, EntityPlayer player, String[] args) {
		if (player == null) {
			return;
		}
		else if (args.length < 1) {
			this.sendUsageMessage(player);
		}
		else if (args[0].equalsIgnoreCase("help")) {
			if (args.length < 2) {
				new ChatMsg(ChatMsg.GOLD + "/tome save [<name>]").send(player);
				new ChatMsg(ChatMsg.GOLD + "/tome load [<name>]").send(player);
				new ChatMsg(ChatMsg.GOLD + "/tome debug [<name>]").send(player);
				new ChatMsg(ChatMsg.GOLD + "/tome list").send(player);
				new ChatMsg(ChatMsg.INDIGO + "Use '" + ChatMsg.GOLD + "/tome help <command>" + ChatMsg.INDIGO + "' for more info").send(player);
			}
			else {
				String ar = args[1].toLowerCase();
				if (ar.equals("save")) {
					new ChatMsg(ChatMsg.GOLD + "/tome save [<name>]").send(player);
					new ChatMsg(ChatMsg.SILVER + "Saves a written book to disk.").send(player);
					new ChatMsg(ChatMsg.SILVER + "If no name is provided, the name of").send(player);
					new ChatMsg(ChatMsg.SILVER + "the book being held will be used.").send(player);
				}
				else if (ar.equals("load")) {
					new ChatMsg(ChatMsg.GOLD + "/tome load [<name>]").send(player);
					new ChatMsg(ChatMsg.SILVER + "Loads a written book from disk.").send(player);
					new ChatMsg(ChatMsg.SILVER + "If no name is provided, the name of").send(player);
					new ChatMsg(ChatMsg.SILVER + "the book being held will be used.").send(player);
				}
				else if (ar.equals("debug")) {
					new ChatMsg(ChatMsg.GOLD + "/tome debug [<name>]").send(player);
					new ChatMsg(ChatMsg.SILVER + "Find out " + ChatMsg.ITALIC + "exactly" + ChatMsg.SILVER + " where a book").send(player);
					new ChatMsg(ChatMsg.SILVER + "will be saved. " + ChatMsg.BOLD + "Does not actually save the book.").send(player);
					new ChatMsg(ChatMsg.SILVER + "If no name is provided, the name of").send(player);
					new ChatMsg(ChatMsg.SILVER + "the book being held will be used.").send(player);
				}
				else if (ar.equals("list")) {
					new ChatMsg(ChatMsg.GOLD + "/tome list").send(player);
					new ChatMsg(ChatMsg.SILVER + "List all saved books found in the").send(player);
					new ChatMsg(ChatMsg.SILVER + "current book path (set in the config").send(player);
					new ChatMsg(ChatMsg.SILVER + "file GlobalTweaks.cfg).").send(player);
				}
				else {
					new ChatMsg(ChatMsg.RED + "Unknown command!");
					this.call(src, player, "help");
				}
			}
		}
		// Purpose: list the available tomes
		else if (args[0].equalsIgnoreCase("list")) {
			String[] tomes = FileSystems.getDefault().getPath(path).toFile().list();
			if (tomes == null) {
				throw new NullPointerException("Internal error - cannot resolve tome path to a directory!");
			}
			new ChatMsg("The following tomes are available:").send(player);
			for (String tome : tomes) {
				new ChatMsg(tome).send(player);
			}
			return;
		}
		// Purpose: turn a single book into a written book
		if (PlayerHelper.isHolding(player, Item.book)) {
			ItemStack tmp = player.getCurrentEquippedItem();
			if (tmp.stackSize == 1) {
				tmp.itemID = Item.writtenBook.itemID;
				// new ChatMsg("You change your book into a tome!").send(player);
				player.inventory.onInventoryChanged();
				player.inventoryContainer.detectAndSendChanges();
			}
		}
		// Purpose: require the player now be holding a written book
		if ( !PlayerHelper.isHolding(player, Item.writtenBook)) {
			WorldHelper.explodeAt(player, loadBlastSize * failBlastMultiplier, failBlastGrief);
			new ChatMsg("You try to change your item into a tome, but it fails!").send(player);
			return;
		}
		ItemStack held = player.getCurrentEquippedItem();
		if (held.stackTagCompound == null) {
			held.stackTagCompound = new NBTTagCompound();
		}
		NBTTagCompound tag = held.getTagCompound();
		String tomeName = "";
		try {
			tomeName = args[1];
		}
		catch (ArrayIndexOutOfBoundsException e) {
			tomeName = player.getCurrentEquippedItem().stackTagCompound.getString("title");
		}
		tomeName = tomeName.replaceAll("[/\\\\:\\.\\?\\*\\^\\&\\$\\#\\@\\$\\!\\(\\)\\[\\]\\{\\}\\<\\>]", "_").replaceAll("\\s+", "-");
		// Purpose: load a tome from disk
		if (args[0].equalsIgnoreCase("load")) {
			InputFile tome = new InputFile(path + "/" + tomeName);
			try {
				tome.create();
				WrittenBookTag.unserialize(tome.readAll()).applyToNBT(tag);
				WorldHelper.explodeAt(player, loadBlastSize, false);
				new ChatMsg("With a flash and a bang, your book changes!").send(player);
			}
			catch (IOException e) {
				new ChatMsg("Something went wrong!").send(player);
				new ChatMsg(e.toString()).send(player);
				return;
			}
			player.inventory.onInventoryChanged();
			player.inventoryContainer.detectAndSendChanges();
		}
		// Purpose: save a tome to disk
		else if (args[0].equalsIgnoreCase("save")) {
			if ( !player.worldObj.isRemote) {
				return;
			}
			if (tomeName.toLowerCase().matches("^(?:instructions|help-(?:sonic|tomes|enchant|commands|craft))$")) {
				if (GTCore.bronyMode()) {
					new ChatMsg("Twilight says that's a reserved name.").send(player);
					return;
				}
				new ChatMsg("Sorry, but you can't save a tome with that name (case-insensitive)").send(player);
				return;
			}
			OutputFile tome = new OutputFile(path + "/" + tomeName);
			try {
				tome.create();
				WrittenBookTag btag = new WrittenBookTag(tag.getString("author"), tag.getString("title"));
				btag.append(tag.getTagList("pages"));
				tome.print(btag);
				if (GTCore.bronyMode()) {
					new ChatMsg("Twilight Sparkle copies your book into her personal library!").send(player);
				}
				else {
					new ChatMsg("You saved your tome into the magical ether known as library-space!").send(player);
				}
			}
			catch (IOException e) {
				new ChatMsg(ChatMsg.RED + "Something went wrong!").send(player);
				new ChatMsg(e.toString()).send(player);
				return;
			}
			finally {
				tome.close();
			}
		}
		// Purpose: find the path where the current tome will be saved
		else if (args[0].equalsIgnoreCase("debug")) {
			Path tome = FileSystems.getDefault().getPath(path, tomeName).toAbsolutePath();
			new ChatMsg("This will be saved to the following location:").send(player);
			new ChatMsg(tome.toString()).send(player);
		}
	}
	@Override
	public boolean isFinished() {
		return true;
	}
}
