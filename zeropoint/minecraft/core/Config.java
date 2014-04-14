package zeropoint.minecraft.core;


import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;

import net.minecraftforge.common.Configuration;
import net.minecraftforge.common.Property;


public class Config {
	protected Configuration c;
	public Config(Configuration cfg) {
		c = cfg;
		c.load();
	}
	public Config save() {
		if (c.hasChanged()) {
			c.save();
		}
		return this;
	}
	public Config save(Logger logger) {
		if (c.hasChanged()) {
			c.save();
			logger.config("Saved configuration to disk");
		}
		else {
			logger.config("Configuration unchanged");
		}
		return this;
	}
	public boolean hasChanged() {
		return c.hasChanged();
	}
	public String string(String section, String key, Object def, String cmnt) {
		Property p = c.get(section, key, String.valueOf(def));
		p.comment = cmnt;
		return p.getString();
	}
	public String[] strings(String section, String key, String cmnt) {
		Property p = c.get(section, key, "");
		p.comment = cmnt;
		return p.getStringList();
	}
	public String dir(String section, String key, Object def, String cmnt) {
		Property p = c.get(section, key, String.valueOf(def));
		p.comment = cmnt;
		File path = new File(p.getString());
		path.mkdirs();
		return path.toPath().toAbsolutePath().toString();
	}
	public String[] dirs(String section, String key, String cmnt) {
		Property p = c.get(section, key, "");
		p.comment = cmnt;
		String[] paths = p.getStringList();
		for (String spath : paths) {
			File path = new File(spath);
			path.mkdirs();
		}
		return paths;
	}
	public String file(String section, String key, Object def, String cmnt) {
		Property p = c.get(section, key, String.valueOf(def));
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
	public String[] files(String section, String key, String cmnt) {
		Property p = c.get(section, key, "");
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
	public boolean bool(String category, String key, Object def, String cmnt) {
		boolean _default = Boolean.parseBoolean(String.valueOf(def));
		Property p = c.get(category, key, _default);
		p.comment = cmnt;
		return p.getBoolean(_default);
	}
	public int integer(String category, String key, Object def, String cmnt) {
		int _default = Integer.parseInt(String.valueOf(def));
		Property p = c.get(category, key, _default);
		p.comment = cmnt;
		return p.getInt();
	}
	public double decimal(String category, String key, Object def, String cmnt) {
		double _default = Double.parseDouble(String.valueOf(def));
		Property p = c.get(category, key, _default);
		p.comment = cmnt;
		return p.getDouble(_default);
	}
	public int item(String key, Object def, String cmnt) {
		int _def = Integer.parseInt(String.valueOf(def));
		Property p = c.getItem(key, _def);
		p.comment = cmnt;
		return p.getInt();
	}
	public int block(String key, Object def, String cmnt) {
		int _def = Integer.parseInt(String.valueOf(def));
		Property p = c.getBlock(key, _def);
		p.comment = cmnt;
		return p.getInt();
	}
}
