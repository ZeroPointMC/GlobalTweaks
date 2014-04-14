package zeropoint.minecraft.commands;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;

import net.minecraft.command.ICommand;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.NetLoginHandler;
import net.minecraft.network.packet.NetHandler;
import net.minecraft.network.packet.Packet1Login;
import net.minecraft.server.MinecraftServer;
import zeropoint.minecraft.commands.com.Absorb;
import zeropoint.minecraft.commands.com.Air;
import zeropoint.minecraft.commands.com.Book;
import zeropoint.minecraft.commands.com.Clipboard;
import zeropoint.minecraft.commands.com.Enchlist;
import zeropoint.minecraft.commands.com.Extinguish;
import zeropoint.minecraft.commands.com.Feed;
import zeropoint.minecraft.commands.com.Fly;
import zeropoint.minecraft.commands.com.God;
import zeropoint.minecraft.commands.com.Gtweak;
import zeropoint.minecraft.commands.com.Halp;
import zeropoint.minecraft.commands.com.Heal;
import zeropoint.minecraft.commands.com.Health;
import zeropoint.minecraft.commands.com.Repair;
import zeropoint.minecraft.commands.com.Uuid;
import zeropoint.minecraft.commands.com.Vanish;
import zeropoint.minecraft.core.Config;
import zeropoint.minecraft.core.GTBaseCommand;
import zeropoint.minecraft.core.GTCore;
import zeropoint.minecraft.core.util.ChatMsg;
import zeropoint.minecraft.core.util.Log;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.network.IConnectionHandler;
import cpw.mods.fml.common.network.NetworkMod;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.Player;


@Mod(modid = GTCommands.modid, name = GTCommands.name, version = GTCommands.version, dependencies = "required-after:gtweaks-core")
@NetworkMod(clientSideRequired = true, serverSideRequired = false)
public final class GTCommands {
	public static final String modid = "gtweaks-commands";
	public static final String name = "GlobalTweaks|Commands";
	public static final String version = "public";
	protected static HashMap<String, GTBaseCommand> cmds = new HashMap<String, GTBaseCommand>();
	protected static GTBaseCommand[] cmdNames;
	private static Config cfg;
	private static final Logger LOG = Log.getLogger(name);
	@EventHandler
	public void preinit(FMLPreInitializationEvent event) {
		cfg = new Config(GTCore.getConfiguration());
	}
	@EventHandler
	public void load(FMLInitializationEvent event) {
		if ( !GTCore.Modules.commandsEnabled()) {
			LOG.warning("Module disabled!");
			return;
		}
		cmdNames = new GTBaseCommand[] {
			// [/absorb] Set absorption hearts
			new Absorb(),
			// [/air] Refill oxygen
			new Air(),
			// [/book] Multipurpose book editing command
			new Book(),
			// [/clipboard] Manipulate the user's clipboard
			new Clipboard(),
			// [/enchlist] List all enchantments to console
			new Enchlist(),
			// [/extinguish/ex] Remove fire
			new Extinguish(),
			// [/feed] Set hunger points
			new Feed(),
			// [/fly] Toggle flight ability
			new Fly(),
			// [/god] Toggle taking damage
			new God(),
			// [/halp] Get JCraft extended command help
			new Halp(),
			// [/heal] Add health
			new Heal(),
			// [/health] Set health
			new Health(),
			// [/repair] Repair hand/hotbar/inventory/armour/all
			new Repair(),
			// [/jcraft] General command
			new Gtweak(),
			// [/uuid] Generate UUIDs
			new Uuid(),
			// [/vanish] Toggle invisibility
			new Vanish()
		};
		for (GTBaseCommand com : cmdNames) {
			if (com.getCommandNamePlain().toLowerCase().equals("gtweak")) {
				register(com);
				continue;
			}
			if (cfg.bool("commands", com.getCommandNamePlain(), com.getDefaultState(), com.getCommandHelp(null))) {
				register(com);
				LOG.info("Registering" + (com.isFinished() ? "" : " unfinished") + " command: " + com.getCommandNameSlash());
			}
			else {
				LOG.info("Ignoring" + (com.isFinished() ? "" : " unfinished") + " command: " + com.getCommandNameSlash());
			}
		}
	}
	@EventHandler
	public void postinit(FMLPostInitializationEvent event) {
		NetworkRegistry.instance().registerConnectionHandler(new CommandsConnectionHandler());
		LOG.info("Registered connection handler");
	}
	@EventHandler
	public void serverStart(FMLServerStartingEvent event) {
		if ( !cfg.bool("enable", "commands.register", true, "Register commands on world load? This affects other mods that use GT|Commands to add commands!")) {
			LOG.severe("Command registration disabled! This may cause problems!");
			return;
		}
		Iterator<GTBaseCommand> iter = cmds.values().iterator();
		while (iter.hasNext()) {
			ICommand cmd = iter.next();
			event.registerServerCommand(cmd);
		}
	}
	public static void register(GTBaseCommand cmd) {
		cmds.put(cmd.getCommandNamePlain(), cmd);
	}
	public static GTBaseCommand getCommand(String pname) {
		return cmds.containsKey(pname) ? cmds.get(pname) : null;
	}
	public static String getCommandHelp(String pname) {
		return cmds.containsKey(pname) ? cmds.get(pname).getCommandHelp(null) : "";
	}
	public static String[] getCommands() {
		List<String> tmp = new ArrayList<String>();
		for (GTBaseCommand com : getCommandArray()) {
			tmp.add(com.getCommandName().replaceAll("^/", ""));
		}
		return tmp.toArray(new String[] {});
	}
	public static GTBaseCommand[] getCommandArray() {
		return cmds.values().toArray(new GTBaseCommand[] {});
	}
	protected static class CommandsConnectionHandler implements IConnectionHandler {
		public void playerLoggedIn(Player player, NetHandler netHandler, INetworkManager manager) {}
		public String connectionReceived(NetLoginHandler netHandler, INetworkManager manager) {
			return "";
		}
		public void connectionOpened(NetHandler netClientHandler, String server, int port, INetworkManager manager) {}
		public void connectionOpened(NetHandler netClientHandler, MinecraftServer server, INetworkManager manager) {}
		public void connectionClosed(INetworkManager manager) {}
		public void clientLoggedIn(NetHandler clientHandler, INetworkManager manager, Packet1Login login) {
			EntityPlayer player = clientHandler.getPlayer();
			if (player == null) {
				LOG.severe("CommandsConnectionHandler.clientLoggedIn(...): cannot get EntityPlayer!");
				return;
			}
			new ChatMsg(ChatMsg.SILVER + "You have " + ChatMsg.PURPLE + "GlobalTweaks|Commands" + ChatMsg.SILVER + " enabled!").send(player);
			new ChatMsg(ChatMsg.SILVER + "For a list of commands, use '" + ChatMsg.GOLD + "/gtweak list" + ChatMsg.SILVER + "'").send(player);
			if (GTCore.Modules.tomesEnabled()) {
				new ChatMsg(ChatMsg.SILVER + "Alternatively, use " + ChatMsg.PURPLE + "GlobalTweaks|Tomes" + ChatMsg.SILVER + ":").send(player);
				new ChatMsg(ChatMsg.SILVER + "Hold a book and use '" + ChatMsg.GOLD + "/tome load HELP-COMMANDS" + ChatMsg.SILVER + "'").send(player);
			}
		}
	}
}
