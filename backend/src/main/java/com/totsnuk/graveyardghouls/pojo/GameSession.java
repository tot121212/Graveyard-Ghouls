package com.totsnuk.graveyardghouls.pojo;

import com.totsnuk.graveyardghouls.dto.JoinDto;
import com.totsnuk.graveyardghouls.dto.PlayerActionDto;
import com.totsnuk.graveyardghouls.enums.LifecycleState;
import com.totsnuk.graveyardghouls.enums.result.JoinResult;
import com.totsnuk.graveyardghouls.events.GameEvent;
import com.totsnuk.graveyardghouls.websocket.MessageRouter;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

/**
 * Owns the Game and is responsible for:
 * 
 * - Routing player actions securely
 * 
 * - Basic validation of actions
 * 
 * - Performing basic join and leave actions that are not a part of the
 * GameState
 */
@Getter
@Slf4j
public class GameSession extends ManagedEntity {
    /*** Duration in which session is allowed to be inactive before deletion */
    public static final int GAME_SESSION_INACTIVE_DURATION_IN_MIN = 2;

    private final Game game = new Game();
    private final MessageRouter messageRouter = new MessageRouter();
    private final ParticipantRegistry participantRegistry = new ParticipantRegistry();

    public GameSession() {
        super();
    }

    /**
     * Connect to the session, specifying which seat
     * 
     * @return Participant or null
     */
    public synchronized JoinDto connect() {
        final SeatRegistry seatRegistry = game.getSeatRegistry();

        switch (game.getGameState().getLifecycle()) {

            case LifecycleState.LOBBY, LifecycleState.READY -> {
                final Participant participant = new Participant();

                Seat seat = seatRegistry.occupy(participant);
                if (seat == null)
                    break;

                participant.setSeat(seat);
                participantRegistry.add(participant);
                return new JoinDto(JoinResult.SUCCESS, participant.getId(), participant.getPrivateToken());
            }

            default -> {
                break;
            }

        }
        return null;
    }

    /**
     * Allows users to reconnect to a paused game
     * 
     * @param id    Id of participant
     * @param token Private token used for auth of user
     */
    public synchronized JoinDto reconnect(String participantId,
            String privateToken) {
        if (participantId == null
                || privateToken == null)
            return null;
        Participant participant = participantRegistry.getByPrivate(participantId, privateToken);
        if (participant == null)
            return null;

        if (game.getGameState().getLifecycle() != LifecycleState.PAUSED) {
            return null;
        }

        // listeners will take care of reactions to mutating participant.connection
        return new JoinDto(JoinResult.SUCCESS, participant.getId(), participant.getPrivateToken());
    }

    /**
     * Disconnect a user from a session
     * 
     * @param GameConnection
     * @return
     */
    public synchronized boolean disconnect(String publicId, String privateToken) {
        // use id and token to auth that it is the right person
        // get existing participant
        Participant participant = participantRegistry.getByPrivate(publicId, privateToken);
        if (participant == null) {
            log.warn("Participant could not be found");
            return false;
        }

        switch (game.getGameState().getLifecycle()) {
            // Player who leaves spot can be filled by anyone
            case LifecycleState.LOBBY -> {
                // TODO:
                // remove connection
                // tell seatRegistry to unoccupy seat
                // no need to tell game that player disconnect because player does not exist yet
                // delete participant
                participantRegistry.remove(participant);
            }
            case LifecycleState.PAUSED -> {
                // fire the disconnect event
            }
            default -> {
                break;
            }
        }
        return false;
    }

    /**
     * Constructs a PlayerActionDto and safely transfers to enqueueAction within the
     * Game
     */
    public boolean executeGameEvent(
            String participantId,
            String privateToken,
            GameEvent<?> gameEvent) {

        if (participantId == null
                || privateToken == null
                || gameEvent == null) {
            return false;
        }

        Participant participant = participantRegistry.getByPrivate(participantId, privateToken);
        if (participant == null)
            return false;

        Player player = participant.getPlayer();
        if (player == null)
            return false;

        // ensure connection and seat exist
        if (participant.getSeat() == null)
            return false;

        // pass call along but with player instead of privateToken
        return game.enqueue(gameEvent);
    }
}
