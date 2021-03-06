package de.zh32.teleportsigns.sign;

import de.zh32.teleportsigns.DataContainer;
import de.zh32.teleportsigns.TestConfiguration;
import de.zh32.teleportsigns.event.EventAdapter;
import de.zh32.teleportsigns.event.ProxyTeleportEvent;
import de.zh32.teleportsigns.server.GameServer;
import org.junit.Before;
import org.junit.Test;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 *
 * @author zh32
 */
public class ServerTeleporterTest {

	private ServerTeleporter testee;
	private DataContainer plugin;
	private EventAdapter eventAdapter;

	@Before
	public void setup() {
		plugin = mock(DataContainer.class);
		when(plugin.getConfiguration()).thenReturn(new TestConfiguration());
		eventAdapter = mock(EventAdapter.class);
		testee = spy(new ServerTeleporter(plugin, eventAdapter) {

			@Override
			public void teleportToServer(String player, String server) {

			}
		});

	}

	@Test
	public void can_teleport_player() {
		TeleportSign.TeleportSignLocation teleportSignLocation = new TeleportSign.TeleportSignLocation(1, 1, 1, "world");
		final GameServer gameServer = new GameServer().setName("SERVER").setOnline(true);
		TeleportSign teleportSign = new TeleportSign(gameServer, null, teleportSignLocation);
		when(eventAdapter.callTeleportEvent(anyString(), any(GameServer.class))).thenReturn(new ProxyTeleportEvent(){

			@Override
			public boolean isCancelled() {
				return false;
			}

			@Override
			public String getPlayerName() {
				return "player";
			}

			@Override
			public GameServer getServer() {
				return gameServer;
			}
		});
		when(plugin.signAtLocation(teleportSignLocation)).thenReturn(teleportSign);
		testee.teleportPlayer("TESTER", teleportSignLocation);
		verify(testee, times(1)).teleportToServer("player", "SERVER");
	}

	@Test
	public void server_offline() {
		TeleportSign.TeleportSignLocation teleportSignLocation = new TeleportSign.TeleportSignLocation(1, 1, 1, "world");
		final GameServer gameServer = new GameServer().setName("SERVER").setOnline(false);
		TeleportSign teleportSign = new TeleportSign(gameServer, null, teleportSignLocation);
		when(eventAdapter.callTeleportEvent(anyString(), any(GameServer.class))).thenReturn(new ProxyTeleportEvent(){

			@Override
			public boolean isCancelled() {
				return false;
			}

			@Override
			public String getPlayerName() {
				return "player";
			}

			@Override
			public GameServer getServer() {
				return gameServer;
			}
		});
		when(plugin.signAtLocation(teleportSignLocation)).thenReturn(teleportSign);
		testee.teleportPlayer("TESTER", teleportSignLocation);
		verify(testee, never()).teleportToServer("player", "SERVER");
	}

	@Test
	public void player_has_cooldown() {
		TeleportSign.TeleportSignLocation teleportSignLocation = new TeleportSign.TeleportSignLocation(1, 1, 1, "world");
		final GameServer gameServer = new GameServer().setName("SERVER").setOnline(true);
		TeleportSign teleportSign = new TeleportSign(gameServer, null, teleportSignLocation);
		when(eventAdapter.callTeleportEvent(anyString(), any(GameServer.class))).thenReturn(new ProxyTeleportEvent(){

			@Override
			public boolean isCancelled() {
				return false;
			}

			@Override
			public String getPlayerName() {
				return "player";
			}

			@Override
			public GameServer getServer() {
				return gameServer;
			}
		});
		when(plugin.signAtLocation(teleportSignLocation)).thenReturn(teleportSign);
		testee.teleportPlayer("TESTER", teleportSignLocation);
		testee.teleportPlayer("TESTER", teleportSignLocation);
		verify(testee, times(1)).teleportToServer("player", "SERVER");
	}

}
