package zeropoint.minecraft.core.util;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import zeropoint.core.io.file.FileBase;
import zeropoint.minecraft.core.GTCore;
import zeropoint.minecraft.core.util.manip.ItemStackHelper;


/**
 * Class for manipulating the NBT data used to represent a written book
 * 
 * @author Zero Point
 */
public class WrittenBookTag {
	/**
	 * The character used by Minecraft to indicate a formatting code
	 */
	public static final String FORMAT_CODE_CHAR = "§";
	/**
	 * The default author, used when no author is specified
	 */
	public static final String DEF_AUTHOR = "Anonymous";
	/**
	 * The default title for books
	 */
	public static final String DEF_TITLE = "The Invisible Book of Invisibility";
	/**
	 * The author of the book represented by this object
	 */
	protected String author = "";
	/**
	 * The title of the book represented by this object
	 */
	protected String title = "";
	/**
	 * The contents of our book
	 */
	protected ArrayList<String> pages = new ArrayList<String>();
	private static final String sep = FileBase.LINE_SEP;
	private static final Logger DEBUG = GTCore.DEBUG;
	/**
	 * Change the author value of the given book
	 * 
	 * @param book
	 *            - an {@link ItemStack} holding a written book to edit
	 * @param newAuthor
	 *            - the new author value to use
	 * @return - a modified ItemStack
	 */
	public static ItemStack setAuthor(ItemStack book, String newAuthor) {
		ItemStack fixed = ItemStackHelper.fix(book);
		fixed.stackTagCompound.removeTag("author");
		fixed.stackTagCompound.setString("author", newAuthor);
		return fixed;
	}
	/**
	 * Change the title value of the given book
	 * 
	 * @param book
	 *            - an {@link ItemStack} holding a written book to edit
	 * @param newTitle
	 *            - the new title value to use
	 * @return - a modified ItemStack
	 */
	public static ItemStack setTitle(ItemStack book, String newTitle) {
		ItemStack fixed = ItemStackHelper.fix(book);
		fixed.stackTagCompound.removeTag("title");
		fixed.stackTagCompound.setString("title", newTitle);
		return fixed;
	}
	/**
	 * Create a new WrittenBookTag from a serialized string
	 * 
	 * @param data
	 *            - the serialized form of a written book
	 * @return a WrittenBookTag representing the unserialized book
	 */
	public static final WrittenBookTag unserialize(String data) {
		String[] content = data.split(FileBase.LINE_SEP, 3);
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
	/**
	 * Create a serialized string from a WrittenBookTag
	 * 
	 * @param tag
	 *            - the WrittenBookTag to serialize
	 * @return the serialized string representation
	 */
	public static final String serialize(WrittenBookTag tag) {
		return serialize(tag.create());
	}
	/**
	 * Create a serialized string from an {@link ItemStack}
	 * 
	 * @param book
	 *            - the ItemStack to serialize
	 * @return the serialized string representation
	 */
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
		DEBUG.fine("Beginning serialization of " + tagCount + " pages");
		// Start serialization
		for (int i = 0; i < tagCount; i++ ) {
			// Catch exceptions
			try {
				// More debug
				DEBUG.fine("Getting page " + i);
				NBTTagString page = (NBTTagString) pages.tagAt(i);
				// Don't operate on a null page, that'll just break things
				if ((page == null) || (page.toString() == null)) {
					DEBUG.fine("Null page found, terminating loop");
					break;
				}
				// Debug again; current page index (zero-based) and the content
				DEBUG.fine("Current page (" + i + "): " + page.data);
				// Another bit of debug...
				DEBUG.fine("Appending page to serialized string");
				// The string "--PAGEBREAK--" is used to indicate that a new page should be started.
				// It makes the serialized form a bit more human-readable.
				data.append((i > 0 ? sep + "--PAGEBREAK--" + sep : "") + page.data);
			}
			// NBTTagList directly uses a List (untyped), and does no bounds checking,
			// so catch Index OOB here, just in case. Never actually happened to me,
			// but better safe than sorry.
			catch (IndexOutOfBoundsException e) {
				DEBUG.fine("Index " + i + " OOB, terminating");
				break;
			}
			// Same deal, if we get a null pointer, don't explode.
			catch (NullPointerException e) {
				DEBUG.fine("NPE on index " + i + ", terminating");
				break;
			}
			catch (ClassCastException e) {
				DEBUG.fine("Cannot retrieve an NBTTagString from the NBTTagList at index " + i + " - wtf?");
				break;
			}
		}
		// End of serialization, print a message, return the result
		DEBUG.fine("Serialization complete");
		return data.toString();
	}
	/**
	 * Initialize a WrittenBookTag with the default author and title
	 */
	public WrittenBookTag() {
		this(DEF_AUTHOR, DEF_TITLE);
	}
	/**
	 * Initialize a WrittenBookTag with the given author and the default title
	 * 
	 * @param writer
	 *            - the author name
	 */
	public WrittenBookTag(String writer) {
		this(writer, DEF_TITLE);
	}
	/**
	 * Initialize a WrittenBookTag with the given author and title
	 * 
	 * @param writer
	 *            - the author name
	 * @param name
	 *            - the title
	 */
	public WrittenBookTag(String writer, String name) {
		this.author = writer;
		this.title = name;
	}
	/**
	 * Add another page to the book
	 * 
	 * @param content
	 *            - the text for the new page
	 * @return the WrittenBookTag
	 */
	public WrittenBookTag addPage(String content) {
		this.pages.add(content.intern());
		return this;
	}
	/**
	 * Remove a page from the book by content
	 * 
	 * @param content
	 *            - the text to remove
	 * @return the WrittenBookTag
	 * @deprecated Use removePage(int index) instead
	 */
	@Deprecated
	public WrittenBookTag removePage(String content) {
		this.pages.remove(content.intern());
		return this;
	}
	/**
	 * Remove a page from the book by position
	 * 
	 * @param index
	 *            - the zero-based index of the page you want to remove
	 * @return the WrittenBookTag
	 */
	public WrittenBookTag removePage(int index) {
		this.pages.remove(index);
		return this;
	}
	/**
	 * Add multiple pages
	 * 
	 * @param content
	 *            - the text for the new pages (array or vararg)
	 * @return the WrittenBookTag
	 */
	public WrittenBookTag append(String... content) {
		this.pages.addAll(Arrays.asList(content));
		return this;
	}
	/**
	 * Add multiple pages
	 * 
	 * @param content
	 *            - the text for the new pages (List of strings)
	 * @return the WrittenBookTag
	 */
	public WrittenBookTag append(List<? extends String> content) {
		this.pages.addAll(content);
		return this;
	}
	/**
	 * Add multiple pages
	 * 
	 * @param content
	 *            - the text for the new pages (NBTTagList of strings)
	 * @return the WrittenBookTag
	 */
	public WrittenBookTag append(NBTTagList content) {
		if (content.tagCount() > 0) {
			if ( !content.NBTTypes[content.tagAt(0).getId()].equals("STRING")) {
				return this;
			}
			int tags = content.tagCount();
			try {
				for (int i = 0; i < tags; i++ ) {
					NBTTagString tag = (NBTTagString) content.tagAt(0);
					this.pages.add(tag.toString());
				}
			}
			catch (ClassCastException e) {
				return this;
			}
		}
		return this;
	}
	/**
	 * Remove all pages
	 * 
	 * @return the WrittenBookTag
	 */
	public WrittenBookTag clear() {
		this.pages.clear();
		return this;
	}
	/**
	 * Overwrite existing pages
	 * 
	 * @param content
	 *            - the text for the new pages (array or varargs)
	 * @return the WrittenBookTag
	 */
	public WrittenBookTag setPages(String... content) {
		return this.clear().append(content);
	}
	/**
	 * Overwrite existing pages
	 * 
	 * @param content
	 *            - the text for the new pages (List of strings)
	 * @return the WrittenBookTag
	 */
	public WrittenBookTag setPages(List<? extends String> content) {
		return this.clear().append(content);
	}
	/**
	 * Overwrite existing pages
	 * 
	 * @param content
	 *            - the text for the new pages (NBTTagList of strings)
	 * @return the WrittenBookTag
	 */
	public WrittenBookTag setPages(NBTTagList content) {
		return this.clear().append(content);
	}
	/**
	 * @return the author of this book
	 */
	public String getAuthor() {
		return this.author;
	}
	/**
	 * @return the title of this book
	 */
	public String getTitle() {
		return this.title;
	}
	/**
	 * @return the pages as an ArrayList
	 */
	public ArrayList<String> getPagesAsList() {
		return (ArrayList<String>) this.pages.clone();
	}
	/**
	 * @return the pages as a <code>String</code> array
	 */
	public String[] getPagesAsArray() {
		return this.pages.toArray(new String[] {});
	}
	/**
	 * @return the pages in an NBT list tag
	 */
	public NBTTagList getPagesAsNBT() {
		NBTTagList tag = new NBTTagList("pages");
		for (String page : this.pages) {
			tag.appendTag(new NBTTagString("", page));
		}
		return tag;
	}
	/**
	 * @return the complete NBTTagCompound required to represent this book
	 */
	public NBTTagCompound getCompleteTag() {
		NBTTagCompound tag = new NBTTagCompound();
		tag.setString("author", this.author);
		tag.setString("title", this.title);
		tag.setTag("pages", getPagesAsNBT());
		return tag;
	}
	/**
	 * Merge the given tag with the tag required to represent this book
	 * 
	 * @param tag
	 *            - the tag to merge into
	 */
	public void applyToNBT(NBTTagCompound tag) {
		tag.setString("author", getAuthor());
		tag.setString("title", getTitle());
		tag.setTag("pages", getPagesAsNBT());
	}
	/**
	 * @return an ItemStack containing the book represented by this object
	 */
	public ItemStack create() {
		ItemStack book = ItemStackHelper.fix(new ItemStack(Item.writtenBook));
		this.applyToNBT(book.getTagCompound());
		return book;
	}
	/**
	 * @return the serialized form of this book
	 */
	public final String serialize() {
		return serialize(this);
	}
	/**
	 * @return the serialized form of this book
	 */
	@Override
	public String toString() {
		return this.serialize();
	}
}
