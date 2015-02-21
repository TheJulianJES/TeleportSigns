package de.zh32.teleportsigns.bukkit;

import de.zh32.teleportsigns.ServerTeleporter;
import de.zh32.teleportsigns.TeleportSign.TeleportSignLocation;
import de.zh32.teleportsigns.utility.MessageHelper;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import org.bukkit.Bukkit;
import org.bukkit.block.Sign;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.Plugin;

/**
 *
 * @author zh32
 */
public class BukkitServerTeleporter extends ServerTeleporter implements Listener {
	private static final String TELEPORT_PERMISION = "teleportsigns.use";
	private final Plugin bukkitPlugin;

	public BukkitServerTeleporter(BukkitTeleportSigns teleportSigns, Plugin bukkitPlugin) {
		super(teleportSigns);
		this.bukkitPlugin = bukkitPlugin;
	}

	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent event) {
		if (!isTeleportSignAction(event)) {
			return;
		}
		if (!event.getPlayer().hasPermission(TELEPORT_PERMISION)) {
			event.getPlayer().sendMessage(MessageHelper.getMessage("teleport.nopermission"));
			return;
		}
		teleportPlayer(
				event.getPlayer().getName(),
				new TeleportSignLocation(
						event.getClickedBlock().getX(),
						event.getClickedBlock().getX(),
						event.getClickedBlock().getX(),
						event.getClickedBlock().getWorld().getName()
				)
		);
	}
	
	private boolean isTeleportSignAction(PlayerInteractEvent event) {
		return event.hasBlock()
				&& event.getClickedBlock().getState() instanceof Sign
				&& event.getAction() == Action.RIGHT_CLICK_BLOCK;
	}

	@Override
	public void teleportToServer(String player, String serverName) {
		ByteArrayOutputStream b = new ByteArrayOutputStream();
		DataOutputStream out = new DataOutputStream(b);
		try {
			out.writeUTF("Connect");
			out.writeUTF(serverName);
		} catch (IOException eee) {
			Bukkit.getLogger().info("You'll never see me!");
		}
		Bukkit.getPlayer(player).sendPluginMessage(bukkitPlugin, "BungeeCord", b.toByteArray());
	}

}
