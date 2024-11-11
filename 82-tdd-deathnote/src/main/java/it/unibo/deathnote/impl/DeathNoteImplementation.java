package it.unibo.deathnote.impl;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import it.unibo.deathnote.api.DeathNote;

public class DeathNoteImplementation implements DeathNote {

    private static final String DEFAULT_DEATH_CAUSE = "heart attack";

    private final Map<String, String> deathCauses = new HashMap<>();
    private final Map<String, String> details = new HashMap<>();

    private String latestNameWritten;
    private long latestWriteTime;

    /**
     * {@inheritDoc}
     */
    @Override
    public String getRule(final int ruleNumber) {
        try {
            return RULES.get(ruleNumber - 1);
        } catch (final Exception e) {
            throw new IllegalArgumentException("Rule " + ruleNumber + "is not a valid rule.");
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void writeName(final String name) {
        Objects.requireNonNull(name);

        this.latestNameWritten = name;

        this.deathCauses.put(this.latestNameWritten, null);
        this.details.put(this.latestNameWritten, null);
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

        if (getElapsedTime() <= 40) {
            this.deathCauses.put(this.latestNameWritten, cause);
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

        if (getElapsedTime() <= 6040) {
            this.details.put(latestNameWritten, details);
            return true;
        }

        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getDeathCause(final String name) {
        final String deathCause = this.deathCauses.get(name);

        return deathCause != null ? deathCause : DEFAULT_DEATH_CAUSE;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getDeathDetails(final String name) {
        final String details = this.details.get(name);

        return details != null ? details : "";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isNameWritten(final String name) {
        return this.deathCauses.containsKey(name);
    }

    private long getElapsedTime() {
        return System.currentTimeMillis() - this.latestWriteTime;
    }
}