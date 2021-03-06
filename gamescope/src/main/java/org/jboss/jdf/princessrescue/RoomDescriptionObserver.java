package org.jboss.jdf.princessrescue;

import javax.enterprise.event.Observes;

import org.jboss.jdf.princessrescue.observerorder.ObserverOrder;
import org.jboss.jdf.princessrescue.xmlbeans.Room;

public class RoomDescriptionObserver {
	
	public void aroomDescriptionObserver(@Observes @ObserverOrder(0) PlayerEnteredRoomEvent event, GameMessage gameMessage, @Current Room currentRoom, @Current Player currentPlayer) {
		gameMessage.add(currentRoom.getDescription());
	}
	
	public void playerSmellObserver(@Observes @ObserverOrder(2) PlayerEnteredRoomEvent event, GameMessage gameMessage, @Current Room currentRoom, @Current Player currentPlayer) {
		boolean smellsPlayer = false;
		
		if (currentRoom.getNorth() != null) {
			if (currentRoom.getNorth().getPlayers().size() > 0) {
				smellsPlayer = true;
			}
		}
		if (currentRoom.getSouth() != null) {
			if (currentRoom.getSouth().getPlayers().size() > 0) {
				smellsPlayer = true;
			}
		}
		if (currentRoom.getEast() != null) {
			if (currentRoom.getEast().getPlayers().size() > 0) {
				smellsPlayer = true;
			}
		}
		if (currentRoom.getWest() != null) {
			if (currentRoom.getWest().getPlayers().size() > 0) {
				smellsPlayer = true;
			}
		}
		
		if (smellsPlayer) {
			gameMessage.add("You smell another player nearby.");
		}
	}
	
	public void playerSightObserver(@Observes @ObserverOrder(1) PlayerEnteredRoomEvent event, GameMessage gameMessage, @Current Room currentRoom, @Current Player currentPlayer) {
		for (Player player : currentRoom.getPlayers()) {
			if (player != currentPlayer) {
				gameMessage.add(player.getName() + " is in the room.");
			}
		}
	}
}
