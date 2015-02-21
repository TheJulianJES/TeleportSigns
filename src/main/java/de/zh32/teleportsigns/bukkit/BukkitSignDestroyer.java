package de.zh32.teleportsigns.bukkit;

import de.zh32.teleportsigns.SignDestroyer;
import de.zh32.teleportsigns.TeleportSign;
import de.zh32.teleportsigns.TeleportSigns;
import de.zh32.teleportsigns.utility.MessageHelper;
import org.bukkit.block.Sign;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

/**
 *
 * @author zh32
 */
public class BukkitSignDestroyer extends SignDestroyer implements Listener {
	private static final String DESTROY_PERMISSION = "teleportsigns.destroy";

	public BukkitSignDestroyer(TeleportSigns teleportSigns) {
		super(teleportSigns);
	}
	
	@EventHandler
	public void onBlockBreak(BlockBreakEvent event) {
		if (!(event.getBlock().getState() instanceof Sign)) {
			return;
		}
		TeleportSign.TeleportSignLocation teleportSignLocation = new TeleportSign.TeleportSignLocation(event.getBlock().getX(), event.getBlock().getY(), event.getBlock().getZ(), event.getBlock().getWorld().getName());
		TeleportSign sign = getTeleportSigns().signAtLocation(teleportSignLocation);
		if (sign == null) {
			return;
		}
		if (!event.getPlayer().hasPermission(DESTROY_PERMISSION)) {
			return;
		}
		destroySign(sign);
		event.getPlayer().sendMessage(MessageHelper.getMessage("sign.destroyed"));
	}

}