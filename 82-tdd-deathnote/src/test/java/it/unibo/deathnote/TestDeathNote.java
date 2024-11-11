package it.unibo.deathnote;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import it.unibo.deathnote.api.DeathNote;
import static it.unibo.deathnote.api.DeathNote.RULES;
import it.unibo.deathnote.impl.DeathNoteImplementation;

class TestDeathNote {

    private static final int DEATH_CAUSE_FAIL_MILLIS = 100;
    private static final int DETAILS_FAIL_MILLIS = 6100;
    private DeathNote dn;

    @BeforeEach
    public void setupTest() {
        dn = new DeathNoteImplementation();
    }

    @Test
    public void testIllegalRules() {
        for (var rule : List.of(-1, 0, RULES.size() + 1, RULES.size() + 2)) {
            testIllegalRule(rule);
        }
    }

    private void testIllegalRule(int ruleNumber) {
        try {
            dn.getRule(ruleNumber);
            fail("Allowed rule: " + ruleNumber);
        } catch (IllegalArgumentException e) {
        } catch (Exception e) {
            fail("Wrong Exception was thrown: " + e);
        }
    }

    @Test
    public void testLegalRules() {
        for (int i = 1; i <= RULES.size(); i++) {
            var rule = dn.getRule(i);
            assertNotNull(rule);
            assertFalse(rule.isBlank());
        }
    }

    @Test
    public void testWriteName() {
        final String name = "Name";
        final String anotherName = "Name2";
        final String emptyString = "";

        assertFalse(dn.isNameWritten(name));
        dn.writeName(name);
        assertTrue(dn.isNameWritten(name));
        assertFalse(dn.isNameWritten(anotherName));
        assertFalse(dn.isNameWritten(emptyString));
    }

    @Test
    public void testCauseOfDeath() throws InterruptedException {
        final String name = "Name";
        final String heartAttackCause = "heart attack";

        final String otherName = "Name2";
        final String kartingAccidentCause = "karting accident";
        final String otherCause = "car crash";

        try {
            dn.writeDeathCause(kartingAccidentCause);
            fail("Allowed writing death cause without writing a name.");
        } catch (IllegalStateException e) {
        } catch (Exception e) {
            fail("Wrong Exception was thrown: " + e);
        }

        dn.writeName(name);
        assertEquals(heartAttackCause, dn.getDeathCause(name));

        dn.writeName(otherName);
        assertTrue(dn.writeDeathCause(kartingAccidentCause));
        assertEquals(kartingAccidentCause, dn.getDeathCause(otherName));

        Thread.sleep(DEATH_CAUSE_FAIL_MILLIS);

        assertFalse(dn.writeDeathCause(otherCause));
        assertEquals(kartingAccidentCause, dn.getDeathCause(otherName));
    }

    @Test
    public void testWriteDeathDetails() throws InterruptedException {
        final String name = "Name";
        final String ranForTooLongDetails = "ran for too long";

        final String otherName = "Name2";

        try {
            dn.writeDetails(ranForTooLongDetails);
            fail("Allowed writing details without writing a name.");
        } catch (IllegalStateException e) {
        } catch (Exception e) {
            fail("Wrong Exception was thrown: " + e);
        }

        dn.writeName(name);
        assertTrue(dn.getDeathDetails(name).isBlank());

        assertTrue(dn.writeDetails(ranForTooLongDetails));
        assertEquals(ranForTooLongDetails, dn.getDeathDetails(name));

        dn.writeName(otherName);
        Thread.sleep(DETAILS_FAIL_MILLIS);

        assertFalse(dn.writeDetails(ranForTooLongDetails));
        assertTrue(dn.getDeathDetails(otherName).isEmpty());
    }
}