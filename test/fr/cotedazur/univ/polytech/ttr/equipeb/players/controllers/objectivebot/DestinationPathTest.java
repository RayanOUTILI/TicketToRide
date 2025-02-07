package fr.cotedazur.univ.polytech.ttr.equipeb.players.controllers.objectivebot;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DestinationPathTest {
    @Test
    void testGetDestinationCard() {
        DestinationPath destinationPath = new DestinationPath(null, null, 0);
        assertNull(destinationPath.getDestinationCard());
    }

    @Test
    void testGetLength() {
        DestinationPath destinationPath = new DestinationPath(null, null, 0);
        assertEquals(0, destinationPath.getLength());
    }

    @Test
    void testGetRoutes() {
        DestinationPath destinationPath = new DestinationPath(null, null, 0);
        assertNull(destinationPath.getRoutes());
    }

    @Test
    void testCompareTo() {
        DestinationPath destinationPath = new DestinationPath(null, null, 0);
        DestinationPath destinationPath2 = new DestinationPath(null, null, 1);
        assertEquals(-1, destinationPath.compareTo(destinationPath2));
    }

}