package it.unibo.deathnote.impl;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import it.unibo.deathnote.api.DeathNote;

public class DeathNoteImplementation implements DeathNote {

    /*
     * TODO: Try to implement the nested class "Death"
     * 
     * Death has:
     * - name
     * - cause
     * - details
     * - time of write
     * 
     * DeathNoteImplementation will have:
     * - a List of Deaths.
     * - the most recent Death.
     */
    private static final String DEFAULT_DEATH_CAUSE = "heart attack";
    private static final int DEATH_CAUSE_MAX_CHANGE_MILLIS = 40;
    private static final int DETAILS_MAX_CHANGE_MILLIS = 6040;

    private final Map<String, String> deathCauseMap = new HashMap<>();
    private final Map<String, String> detailsMap = new HashMap<>();

    private String latestNameWritten;
    private long latestWriteTime;

    /**
     * {@inheritDoc}
     */
    @Override
    public String getRule(final int ruleNumber) {
        if (ruleNumber <= 0 || ruleNumber > RULES.size()) {
            throw new IllegalArgumentException("Rule " + ruleNumber + "is not a valid rule.");
        }
        return RULES.get(ruleNumber - 1);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void writeName(final String name) {
        Objects.requireNonNull(name);

        this.latestNameWritten = name;

        this.deathCauseMap.put(this.latestNameWritten, null);
        this.detailsMap.put(this.latestNameWritten, null);
        this.latestWriteTime = System.currentTimeMillis();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean writeDeathCause(final String cause) {
        if (this.latestNameWritten == null) {
            throw new IllegalStateException("No name was written while attempting to write death cause.");
        }
        if (cause == null) {
            throw new IllegalStateException("cause is null.");
        }

        if (getElapsedTime() <= DEATH_CAUSE_MAX_CHANGE_MILLIS) {
            this.deathCauseMap.put(this.latestNameWritten, cause);
            return true;
        }

        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean writeDetails(final String details) {
        if (this.latestNameWritten == null) {
            throw new IllegalStateException("No name was written while attempting to write details: " + details);
        }
        if (details == null) {
            throw new IllegalStateException("details is null.");
        }

        if (getElapsedTime() <= DETAILS_MAX_CHANGE_MILLIS) {
            this.detailsMap.put(latestNameWritten, details);
            return true;
        }

        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getDeathCause(final String name) {
        final String deathCause = this.deathCauseMap.get(name);

        return deathCause != null ? deathCause : DEFAULT_DEATH_CAUSE;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getDeathDetails(final String name) {
        final String details = this.detailsMap.get(name);

        return details != null ? details : "";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isNameWritten(final String name) {
        return this.deathCauseMap.containsKey(name);
    }

    private long getElapsedTime() {
        return System.currentTimeMillis() - this.latestWriteTime;
    }
}