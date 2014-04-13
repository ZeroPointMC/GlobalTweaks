package zeropoint.minecraft.core.util;


import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import zeropoint.core.io.file.ZeroCoreFileBase;


public class WrittenBookTag {
	public static final String FORMAT_CODE_CHAR = "§";
	public static final String DEF_AUTHOR = "Zero Point";
	public static final String DEF_TITLE = "A book of words";
	protected String author = "";
	protected String title = "";
	protected ArrayList<String> pages = new ArrayList<String>();
	public static final String sep = ZeroCoreFileBase.lineSep;
	protected static final PrintStream out = System.out;
	protected static final PrintStream err = System.err;
	public static ItemStack setAuthor(ItemStack book, String newAuthor) {
		book.stackTagCompound.removeTag("author");
		book.stackTagCompound.setString("author", newAuthor);
		return book;
	}
	public static ItemStack setTitle(ItemStack book, String newTitle) {
		book.stackTagCompound.removeTag("title");
		book.stackTagCompound.setString("title", newTitle);
		return book;
	}
	public static final WrittenBookTag unserialize(String data) {
		String[] content = data.split(ZeroCoreFileBase.lineSep, 3);
		String writer = DEF_AUTHOR;
		String name = DEF_TITLE;
		if (content.length > 1) {
			name = content[0];
		}
		if (content.length > 2) {
			writer = content[1];
		}
		WrittenBookTag tag = new WrittenBookTag(writer, name);
		try {
			tag.append(content[2].replaceAll("&&", FORMAT_CODE_CHAR).split(sep + "--PAGEBREAK--" + sep));
		}
		catch (ArrayIndexOutOfBoundsException e) {}
		return tag;
	}
	public static final String serialize(WrittenBookTag tag) {
		return serialize(tag.create());
	}
	public static final String serialize(ItemStack book) {
		// Require that we get a written book for serialization
		if (book.itemID != Item.writtenBook.itemID) {
			throw new IllegalArgumentException("WrittenBookTag.serialize(ItemStack) must be passed an ItemStack holding a written book");
		}
		NBTTagCompound tag = book.getTagCompound();
		// Play it safe
		if (tag == null) {
			tag = new NBTTagCompound();
		}
		// This is where the serialized content will go
		StringBuffer data = new StringBuffer();
		// Start with the title, then the author
		// 'sep' is a String holding the system line separator
		data.append(tag.getString("title") + sep + tag.getString("author") + sep);
		// Make a copy of the tags, so we don't mess around with the actual book
		NBTTagList pages = (NBTTagList) tag.getTagList("pages").copy();
		// Find out how many times we should loop
		int tagCount = pages.tagCount();
		// Debug line, print the number of pages
		err.println("Beginning serialization of " + tagCount + " pages");
		// Start serialization
		for (int i = 0; i < tagCount; i++ ) {
			// Catch exceptions
			try {
				// More debug
				err.println("Getting page " + i);
				NBTTagString page = (NBTTagString) pages.tagAt(i);
				// Don't operate on a null page, that'll just break things
				if ((page == null) || (page.toString() == null)) {
					err.println("Null page found, terminating loop");
					break;
				}
				// Debug again; current page index (zero-based) and the content
				err.println("Current page (" + i + "): " + page.data);
				// Another bit of debug...
				err.println("Appending page to serialized string");
				// The string "--PAGEBREAK--" is used to indicate that a new page should be started.
				// It makes the serialized form a bit more human-readable.
				data.append((i > 0 ? sep + "--PAGEBREAK--" + sep : "") + page.data);
			}
			// NBTTagList directly uses a List (untyped), and does no bounds checking,
			// so catch Index OOB here, just in case. Never actually happened to me,
			// but better safe than sorry.
			catch (IndexOutOfBoundsException e) {
				err.println("Index " + i + " OOB, terminating");
				break;
			}
			// Same deal, if we get a null pointer, don't explode.
			catch (NullPointerException e) {
				err.println("NPE on index " + i + ", terminating");
				break;
			}
			catch (ClassCastException e) {
				err.println("Cannot retrieve an NBTTagString from the NBTTagList at index " + i + " - wtf?");
				break;
			}
		}
		// End of serialization, print a message, return the result
		err.println("Serialization complete");
		return data.toString();
	}
	public WrittenBookTag() {
		this(DEF_AUTHOR, DEF_TITLE);
	}
	public WrittenBookTag(String writer) {
		this(writer, DEF_TITLE);
	}
	public WrittenBookTag(String writer, String name) {
		author = writer;
		title = name;
	}
	public WrittenBookTag addPage(String content) {
		pages.add(content.intern());
		return this;
	}
	public WrittenBookTag removePage(String content) {
		pages.remove(content.intern());
		return this;
	}
	public WrittenBookTag removePage(int index) {
		pages.remove(index);
		return this;
	}
	public WrittenBookTag append(String... content) {
		pages.addAll(Arrays.asList(content));
		return this;
	}
	public WrittenBookTag append(Collection<? extends String> content) {
		pages.addAll(content);
		return this;
	}
	public WrittenBookTag append(NBTTagList content) {
		if (content.tagCount() > 0) {
			if ( !content.NBTTypes[content.tagAt(0).getId()].equals("STRING")) {
				return this;
			}
			int tags = content.tagCount();
			try {
				for (int i = 0; i < tags; i++ ) {
					NBTTagString tag = (NBTTagString) content.tagAt(0);
					pages.add(tag.toString());
				}
			}
			catch (ClassCastException e) {
				return this;
			}
		}
		return this;
	}
	public WrittenBookTag clear() {
		pages.clear();
		return this;
	}
	public WrittenBookTag setPages(String... content) {
		return this.clear().append(content);
	}
	public WrittenBookTag setPages(Collection<? extends String> content) {
		return this.clear().append(content);
	}
	public WrittenBookTag setPages(NBTTagList content) {
		return this.clear().append(content);
	}
	public String getAuthor() {
		return author;
	}
	public String getTitle() {
		return title;
	}
	public ArrayList<String> getPagesAsList() {
		return (ArrayList<String>) pages.clone();
	}
	public String[] getPagesAsArray() {
		return pages.toArray(new String[] {});
	}
	public NBTTagList getPagesAsNBT() {
		NBTTagList tag = new NBTTagList();
		for (String page : pages) {
			tag.appendTag(new NBTTagString("", page));
		}
		return tag;
	}
	public NBTTagCompound getCompleteTag() {
		NBTTagCompound tag = new NBTTagCompound();
		tag.setString("author", author);
		tag.setString("title", title);
		tag.setTag("pages", getPagesAsNBT());
		return tag;
	}
	public void applyToNBT(NBTTagCompound tag) {
		tag.setString("author", getAuthor());
		tag.setString("title", getTitle());
		tag.setTag("pages", getPagesAsNBT());
	}
	public ItemStack create() {
		ItemStack book = new ItemStack(Item.writtenBook);
		if (book.stackTagCompound == null) {
			book.stackTagCompound = new NBTTagCompound();
		}
		this.applyToNBT(book.getTagCompound());
		return book;
	}
	public final String serialize() {
		return serialize(this);
	}
	@Override
	public String toString() {
		return this.serialize();
	}
}
