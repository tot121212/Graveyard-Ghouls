package com.totsnuk.graveyardghouls.pojo;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ParticipantRegistry {
    /**
     * Main owner of participants
     */
    private final Map<String, Participant> participantsById = new ConcurrentHashMap<>();

    public void add(Participant participant) {
        participantsById.put(participant.getId(), participant);
    }

    public void remove(Participant participant) {
        participantsById.remove(participant.getId());
    }

    public Participant getByPublic(String id) {
        return participantsById.get(id);
    }

    /**
     * @return Participant or null
     */
    public Participant getByPrivate(String publicId, String privateToken) {
        if (publicId == null || privateToken == null)
            return null;

        Participant participant = getByPublic(publicId);
        if (participant == null)
            return null;

        String t = participant.getPrivateToken();
        if (!privateToken.equals(t)) {
            log.warn("Game authentication privateToken doesnt match");
            return null;
        }

        return participant;
    }

    public boolean has(String id) {
        return participantsById.containsKey(id);
    }

    /**
     * Checks if all players in seats are ready to start
     */
    public boolean allReady() {
        return participantsById.values().stream().allMatch((Participant p) -> {
            if (p == null)
                return false;

            Seat seat = p.getSeat();
            if (seat == null)
                return false;

            return seat.isReady();
        });
    }
}