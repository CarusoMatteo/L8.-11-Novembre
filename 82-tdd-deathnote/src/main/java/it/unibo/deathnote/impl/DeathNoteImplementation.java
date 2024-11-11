package it.unibo.deathnote.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import it.unibo.deathnote.api.DeathNote;

public class DeathNoteImplementation implements DeathNote {
    private static final int DEATH_CAUSE_MAX_CHANGE_MILLIS = 40;
    private static final int DETAILS_MAX_CHANGE_MILLIS = 6040;

    private static final String DEFAULT_DEATH_CAUSE = "heart attack";
    private static final String DEFAULT_DETAILS = "";

    private final List<Death> deaths = new ArrayList<>();
    private Death latestDeath;

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

        this.latestDeath = new Death(name);
        this.deaths.add(latestDeath);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean writeDeathCause(final String cause) {
        if (this.latestDeath == null) {
            throw new IllegalStateException("No name was written while attempting to write death cause.");
        }
        if (cause == null) {
            throw new IllegalStateException("cause is null.");
        }

        if (this.latestDeath.getElapsedTime() <= DEATH_CAUSE_MAX_CHANGE_MILLIS) {
            this.latestDeath.deathCause = cause;
            return true;
        }

        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean writeDetails(final String details) {
        if (this.latestDeath == null) {
            throw new IllegalStateException("No name was written while attempting to write details: " + details);
        }
        if (details == null) {
            throw new IllegalStateException("details is null.");
        }

        if (latestDeath.getElapsedTime() <= DETAILS_MAX_CHANGE_MILLIS) {
            this.latestDeath.details = details;
            return true;
        }

        return false;
    }

    // Returns the first Death in this.deaths with specified name
    private Death getDeathFromName(final String name) {
        for (final Death death : deaths) {
            if (death.name.equals(name)) {
                return death;
            }
        }

        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getDeathCause(final String name) {
        final Death death = getDeathFromName(name);

        if (death == null) {
            throw new IllegalArgumentException(name + " is not in the Death Note.");
        }

        return death.deathCause != null ? death.deathCause : DEFAULT_DEATH_CAUSE;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getDeathDetails(final String name) {
        final Death death = getDeathFromName(name);

        if (death == null) {
            throw new IllegalArgumentException(name + " is not in the Death Note.");
        }

        return death.details != null ? death.details : DEFAULT_DETAILS;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isNameWritten(final String name) {
        return getDeathFromName(name) != null;
    }

    private class Death {
        private final String name;
        private String deathCause = null;
        private String details = null;
        private final long writeTime;

        public Death(final String name) {
            this.name = name;
            this.writeTime = System.currentTimeMillis();
        }

        public long getElapsedTime() {
            return System.currentTimeMillis() - this.writeTime;
        }
    }
}