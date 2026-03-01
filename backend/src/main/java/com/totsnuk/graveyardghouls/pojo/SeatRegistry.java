package com.totsnuk.graveyardghouls.pojo;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * Registry for all seats
 */
@Getter
@Setter
@NoArgsConstructor
@Slf4j
public class SeatRegistry {
    /*** Lookup table for active connections mapped to the game seats */
    private final List<Seat> seats = new ArrayList<>();

    /*** The number of seats in the game */
    private int seatCount = 2;

    /**
     * Creates all seats based upon the value of maxSeats
     */
    public void createAll() {
        for (int i = 0; i < seatCount; i++) {
            seats.add(new Seat());
        }
    }

    /**
     * Wipes all seats and associations
     */
    public void reset() {
        for (Seat seat : seats) {
            seat.getParticipant().setSeat(null);
        }
        seats.clear();
    }

    /**
     * Occupy any seat
     * 
     * @return the seat that was occupied, or null
     */
    public Seat occupy(Participant participant) {
        return this.occupy(participant, -1);
    }

    /**
     * Occupy a specific seat
     * 
     * @return the seat that was occupied, or null
     */
    public Seat occupy(Participant participant, int seatIdx) {
        if (participant == null) {
            return null;
        }
        // occupy any seat
        if (seatIdx <= -1) {
            Seat seat = getAllUnoccupied().getFirst();
            seat.setParticipant(participant);
            return seat;
        }
        // occupy specific seat
        Seat seat = seats.get(seatIdx);
        if (seat == null) {
            return null;
        }

        if (seat.isEmpty()) {
            seat.setParticipant(participant);
            return seat;
        }
        return null;
    }

    /**
     * Unoccupy any seat (that is mapped to)
     * 
     * @return true if success, false if seat does not exist
     */
    public boolean unoccupy(Seat seat) {
        if (seat == null)
            return false;
        seat.setParticipant(null);
        return true;
    }

    /**
     * Gets all seats that do not have players
     */
    public List<Seat> getAllUnoccupied() {
        List<Seat> unoccupiedSeats = new ArrayList<>();
        for (Seat seat : seats) {
            if (seat.isEmpty()) {
                unoccupiedSeats.add(seat);
            }
        }
        return unoccupiedSeats;
    }
}
