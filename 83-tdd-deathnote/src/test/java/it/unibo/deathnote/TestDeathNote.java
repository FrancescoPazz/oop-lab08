package it.unibo.deathnote;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import it.unibo.deathnote.api.DeathNote;
import it.unibo.deathnote.impl.DeathNoteImpl;

class TestDeathNote {
    private final static String EMPTY_STRING = "";
    private final static String TEST_HUMAN1 = "Luca";
    private final static String TEST_HUMAN2 = "Francesco";
    private final static String TEST_DEATH_CAUSE = "Test cause";
    private final static String TEST_DEATH_DETAILS = "Test details";

    private DeathNoteImpl deathNote;

    @BeforeEach
    public void setUp(){
        this.deathNote = new DeathNoteImpl();
    }

    @Test
    public void testZeroNegativeRules(){
        try {
            deathNote.getRule(-1);
        } catch (IllegalArgumentException e) {
            assertFalse(e.getMessage() == EMPTY_STRING);
            assertEquals("The role number must be between 1 and the rules size.", e.getMessage());
        }
        try {
            deathNote.getRule(0);
        } catch (IllegalArgumentException e) {
            assertFalse(e.getMessage() == EMPTY_STRING);
            assertEquals("The role number must be between 1 and the rules size.", e.getMessage());
        }
    }

    @Test
    public void testEmptyNullRules(){
        List<String> rules = DeathNote.RULES;
        String rule;
        for (int i = 0; i < rules.size(); i++) {
            rule = rules.get(i);
            assertFalse(rule == EMPTY_STRING || rule == null);
        }
    }

    @Test
    public void testHumanDeath(){
        assertFalse(deathNote.isNameWritten(TEST_HUMAN1));
        deathNote.writeName(TEST_HUMAN1);
        assertTrue(deathNote.isNameWritten(TEST_HUMAN1));

        assertFalse(deathNote.isNameWritten(TEST_HUMAN2));
        assertFalse(deathNote.isNameWritten(EMPTY_STRING));

    }

    @Test
    public void testMillisecondsCause() throws InterruptedException{
        try {
            deathNote.writeDeathCause(TEST_DEATH_CAUSE);
            Assertions.fail();
        } catch (IllegalStateException e) {
            assertFalse(e.getMessage() == EMPTY_STRING);
            assertEquals("There is no name written in this DeathNote, or the cause/detail was null.", e.getMessage());
        }
        
        deathNote.writeName(TEST_HUMAN1);
        deathNote.writeDeathCause("heart attack");
        assertEquals(deathNote.getDeathCause(TEST_HUMAN1), "heart attack");


        deathNote.writeName(TEST_HUMAN2);
        assertTrue(deathNote.writeDeathCause("karting accident"));

        Thread.sleep(100);
        assertFalse(deathNote.writeDeathCause("fell from a high place"));

        assertEquals(deathNote.getDeathCause(TEST_HUMAN2), "karting accident");

    }

    @Test
    public void testMillisecondsDetails() throws InterruptedException{
        try {
            deathNote.writeDetails(TEST_DEATH_DETAILS);
            Assertions.fail();
        } catch (IllegalStateException e) {
            assertFalse(e.getMessage() == EMPTY_STRING);
            assertEquals("There is no name written in this DeathNote, or the cause/detail was null.", e.getMessage());
        }

        deathNote.writeName(TEST_HUMAN1);
        deathNote.writeDeathCause(TEST_DEATH_CAUSE);
        assertEquals(deathNote.getDeathDetails(TEST_HUMAN1), EMPTY_STRING);

        assertTrue(deathNote.writeDetails("ran for too long"));
        assertEquals(deathNote.getDeathDetails(TEST_HUMAN1), "ran for too long");
    
        deathNote.writeName(TEST_HUMAN2);
        deathNote.writeDeathCause(TEST_DEATH_CAUSE);
        Thread.sleep(6100);
        assertFalse(deathNote.writeDetails("ran for too long"));
        assertEquals(deathNote.getDeathDetails(TEST_HUMAN2), EMPTY_STRING);

    }

}