package org.jboss.jdf.princessrescue.scope;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.BeanManager;
import javax.inject.Inject;
import javax.inject.Named;

import org.jboss.jdf.princessrescue.Current;
import org.jboss.jdf.princessrescue.Player;
import org.jboss.jdf.princessrescue.PlayerJoinedGameEvent;
import org.jboss.jdf.princessrescue.PlayerLeftGameEvent;


@ApplicationScoped
public class GamesManager {
	
	@Inject
	BeanManager beanManager;
	
	private int counter = 0;
	
	Map<Integer, Game> games = new HashMap<Integer, Game>();
	
	private GameContext getGameContext() {
		return (GameContext)beanManager.getContext(GameScoped.class);
	}
	
	@Produces
	@Named
	public List<Game> getGames() {
		return new ArrayList<Game> (games.values());
	}
	
	public synchronized void removeGame(Game game) {	
		games.remove(game.getId());
		getGameContext().deleteGame(game.getId());
	}
	
	public synchronized Game createNewGame() {
		Game game = new Game();
		game.setId(++counter);
		
		games.put(game.getId(), game);
		getGameContext().createGame(game.getId());
		
		return game;
	}
	
	public synchronized void onPlayerJoined(@Observes PlayerJoinedGameEvent playerJoinedGameEvent, @CurrentGameId Integer gameId, @Current Player player) {
		
		System.out.println("XXX onPlayerJoined: " + gameId + " " + player.toString());
		
		Game game = games.get(gameId);
		if (game != null) {
			if (!game.getPlayers().contains(player)) {
				game.getPlayers().add(player);
			}
		}
	}
		
	public synchronized void onPlayerLeftGame(@Observes PlayerLeftGameEvent playerLeftGameEvent, @CurrentGameId Integer gameId, @Current Player player) {
		
		Game game = games.get(gameId);

		if (game != null) {
			game.getPlayers().remove(player);		
			
			if (game.getPlayers().isEmpty()) {
				// Remove the game if no players left
				games.remove(gameId);
				getGameContext().deleteGame(gameId);
			}
		}
	}
}
