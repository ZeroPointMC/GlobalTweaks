package zeropoint.minecraft.core;


import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;

import net.minecraftforge.common.Configuration;
import net.minecraftforge.common.Property;


/**
 * This class makes it slightly easier to use Forge Configuration objects
 * 
 * @author Zero Point
 */
public class Config {
	/**
	 * The wrapped {@link Configuration} object
	 */
	protected Configuration c;
	/**
	 * Wrap the given config
	 * 
	 * @param cfg
	 *            - the {@link Configuration} object to wrap
	 */
	public Config(Configuration cfg) {
		this.c = cfg;
		this.c.load();
	}
	/**
	 * Save the configuration, if it has changed
	 * 
	 * @return this
	 */
	public Config save() {
		if (this.c.hasChanged()) {
			this.c.save();
		}
		return this;
	}
	/**
	 * Save the config, if it has changed. Also outputs a message to the logger
	 * 
	 * @param logger
	 *            - the {@link Logger} to use write to
	 * @return this
	 */
	public Config save(Logger logger) {
		if (this.c.hasChanged()) {
			this.c.save();
			logger.config("Saved configuration to disk");
		}
		else {
			logger.config("Configuration unchanged");
		}
		return this;
	}
	/**
	 * @return <code>true</code> if the configuration has been changed, else <code>false</code>
	 */
	public boolean hasChanged() {
		return this.c.hasChanged();
	}
	/**
	 * Retrieve a <code>String</code> value from the config
	 * 
	 * @param section
	 *            - the section of the config file to read from
	 * @param key
	 *            - the key to get the value from
	 * @param def
	 *            - the default value to return, if the value does not exist
	 * @param cmnt
	 *            - the comment to label the config value with
	 * @return the requested <code>String</code> value from the config
	 */
	public String string(String section, String key, Object def, String cmnt) {
		Property p = this.c.get(section, key, String.valueOf(def));
		p.comment = cmnt;
		return p.getString();
	}
	/**
	 * Retrieve an array <code>String</code> value from the config
	 * 
	 * @param section
	 *            - the section of the config file to read from
	 * @param key
	 *            - the key to get the value from
	 * @param cmnt
	 *            - the comment to label the config value with
	 * @return the requested <code>String</code> array from the config
	 */
	public String[] strings(String section, String key, String cmnt) {
		Property p = this.c.get(section, key, "");
		p.comment = cmnt;
		return p.getStringList();
	}
	/**
	 * Retrieve a folder path as a <code>String</code> value from the config, creating all needed folders
	 * 
	 * @param section
	 *            - the section of the config file to read from
	 * @param key
	 *            - the key to get the value from
	 * @param def
	 *            - the default value to return, if the value does not exist
	 * @param cmnt
	 *            - the comment to label the config value with
	 * @return the requested folder path <code>String</code> value from the config
	 */
	public String dir(String section, String key, Object def, String cmnt) {
		Property p = this.c.get(section, key, String.valueOf(def));
		p.comment = cmnt;
		File path = new File(p.getString());
		path.mkdirs();
		return path.toPath().toAbsolutePath().toString();
	}
	/**
	 * Retrieve a list of folder path as a <code>String</code> value from the config, creating the full path if it doesn't exist
	 * 
	 * @param section
	 *            - the section of the config file to read from
	 * @param key
	 *            - the key to get the value from
	 * @param cmnt
	 *            - the comment to label the config value with
	 * @return the requested folder paths as a <code>String</code> array from the config
	 */
	public String[] dirs(String section, String key, String cmnt) {
		Property p = this.c.get(section, key, "");
		p.comment = cmnt;
		String[] paths = p.getStringList();
		for (String spath : paths) {
			File path = new File(spath);
			path.mkdirs();
		}
		return paths;
	}
	/**
	 * Retrieve a file path as a <code>String</code> value from the config, creating the entire folder structure and the file itself, if needed
	 * 
	 * @param section
	 *            - the section of the config file to read from
	 * @param key
	 *            - the key to get the value from
	 * @param def
	 *            - the default value to return, if the value does not exist
	 * @param cmnt
	 *            - the comment to label the config value with
	 * @return the requested file path as a a <code>String</code> value from the config
	 */
	public String file(String section, String key, Object def, String cmnt) {
		Property p = this.c.get(section, key, String.valueOf(def));
		p.comment = cmnt;
		File path = new File(p.getString());
		path.getParentFile().mkdirs();
		try {
			path.createNewFile();
		}
		catch (IOException e) {
			return null;
		}
		return path.toPath().toAbsolutePath().toString();
	}
	/**
	 * Retrieve an array of file paths as a <code>String</code> value from the config, creating the entire folder structure and the files themselves, if needed
	 * 
	 * @param section
	 *            - the section of the config file to read from
	 * @param key
	 *            - the key to get the value from
	 * @param cmnt
	 *            - the comment to label the config value with
	 * @return the requested file paths as a a <code>String</code> array from the config
	 */
	public String[] files(String section, String key, String cmnt) {
		Property p = this.c.get(section, key, "");
		p.comment = cmnt;
		String[] paths = p.getStringList();
		for (String spath : paths) {
			File path = new File(spath);
			path.getParentFile().mkdirs();
			try {
				path.createNewFile();
			}
			catch (IOException e) {}
		}
		return paths;
	}
	/**
	 * Retrieve a <code>boolean</code> value from the config
	 * 
	 * @param category
	 *            - the section of the config file to read from
	 * @param key
	 *            - the key to get the value from
	 * @param def
	 *            - the default value to return, if the value does not exist
	 * @param cmnt
	 *            - the comment to label the config value with
	 * @return the requested <code>boolean</code> value from the config
	 */
	public boolean bool(String category, String key, Object def, String cmnt) {
		boolean _default = Boolean.parseBoolean(String.valueOf(def));
		Property p = this.c.get(category, key, _default);
		p.comment = cmnt;
		return p.getBoolean(_default);
	}
	/**
	 * Retrieve an <code>int</code> value from the config
	 * 
	 * @param category
	 *            - the section of the config file to read from
	 * @param key
	 *            - the key to get the value from
	 * @param def
	 *            - the default value to return, if the value does not exist
	 * @param cmnt
	 *            - the comment to label the config value with
	 * @return the requested <code>int</code> value from the config
	 */
	public int integer(String category, String key, Object def, String cmnt) {
		int _default = Integer.parseInt(String.valueOf(def));
		Property p = this.c.get(category, key, _default);
		p.comment = cmnt;
		return p.getInt();
	}
	/**
	 * Retrieve a <code>double</code> value from the config
	 * 
	 * @param category
	 *            - the section of the config file to read from
	 * @param key
	 *            - the key to get the value from
	 * @param def
	 *            - the default value to return, if the value does not exist
	 * @param cmnt
	 *            - the comment to label the config value with
	 * @return the requested <code>double</code> value from the config
	 */
	public double decimal(String category, String key, Object def, String cmnt) {
		double _default = Double.parseDouble(String.valueOf(def));
		Property p = this.c.get(category, key, _default);
		p.comment = cmnt;
		return p.getDouble(_default);
	}
	/**
	 * Retrieve an item ID value from the config
	 * 
	 * @param category
	 *            - the section of the config file to read from
	 * @param key
	 *            - the key to get the value from
	 * @param def
	 *            - the default value to return, if the value does not exist
	 * @param cmnt
	 *            - the comment to label the config value with
	 * @return the requested item ID value from the config
	 */
	public int item(String category, String key, Object def, String cmnt) {
		int _def = Integer.parseInt(String.valueOf(def));
		Property p = this.c.getItem(category, key, _def);
		p.comment = cmnt;
		return p.getInt();
	}
	/**
	 * Retrieve an item ID value from the config
	 * 
	 * @param key
	 *            - the key to get the value from
	 * @param def
	 *            - the default value to return, if the value does not exist
	 * @param cmnt
	 *            - the comment to label the config value with
	 * @return the requested item ID value from the config
	 */
	public int item(String key, Object def, String cmnt) {
		int _def = Integer.parseInt(String.valueOf(def));
		Property p = this.c.getItem(key, _def);
		p.comment = cmnt;
		return p.getInt();
	}
	/**
	 * Retrieve an item ID value from the config
	 * 
	 * @param category
	 *            - the section of the config file to read from
	 * @param key
	 *            - the key to get the value from
	 * @param def
	 *            - the default value to return, if the value does not exist
	 * @param cmnt
	 *            - the comment to label the config value with
	 * @return the requested item ID value from the config
	 */
	public int block(String category, String key, Object def, String cmnt) {
		int _def = Integer.parseInt(String.valueOf(def));
		Property p = this.c.getBlock(category, key, _def);
		p.comment = cmnt;
		return p.getInt();
	}
	/**
	 * Retrieve an item ID value from the config
	 * 
	 * @param key
	 *            - the key to get the value from
	 * @param def
	 *            - the default value to return, if the value does not exist
	 * @param cmnt
	 *            - the comment to label the config value with
	 * @return the requested item ID value from the config
	 */
	public int block(String key, Object def, String cmnt) {
		int _def = Integer.parseInt(String.valueOf(def));
		Property p = this.c.getBlock(key, _def);
		p.comment = cmnt;
		return p.getInt();
	}
}
