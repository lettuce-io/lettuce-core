package io.lettuce.core;

import io.lettuce.core.internal.LettuceAssert;

/**
 * Generic Cursor data structure.
 *
 * @author Mark Paluch
 * @since 3.0
 */
public class ScanCursor {

    /**
     * Finished cursor.
     */
    public static final ScanCursor FINISHED = new ImmutableScanCursor("0", true);

    /**
     * Initial cursor.
     */
    public static final ScanCursor INITIAL = new ImmutableScanCursor("0", false);

    private String cursor;

    private boolean finished;

    /**
     * Creates a new {@link ScanCursor}.
     */
    public ScanCursor() {
    }

    /**
     * Creates a new {@link ScanCursor}.
     *
     * @param cursor
     * @param finished
     */
    public ScanCursor(String cursor, boolean finished) {
        this.cursor = cursor;
        this.finished = finished;
    }

    /**
     *
     * @return cursor id
     */
    public String getCursor() {
        return cursor;
    }

    /**
     * Set the cursor
     *
     * @param cursor the cursor id
     */
    public void setCursor(String cursor) {
        LettuceAssert.notEmpty(cursor, "Cursor must not be empty");

        this.cursor = cursor;
    }

    /**
     *
     * @return true if the scan operation of this cursor is finished.
     */
    public boolean isFinished() {
        return finished;
    }

    public void setFinished(boolean finished) {
        this.finished = finished;
    }

    /**
     * Creates a Scan-Cursor reference.
     *
     * @param cursor the cursor id
     * @return ScanCursor
     */
    public static ScanCursor of(String cursor) {
        ScanCursor scanCursor = new ScanCursor();
        scanCursor.setCursor(cursor);
        return scanCursor;
    }

    private static class ImmutableScanCursor extends ScanCursor {

        public ImmutableScanCursor(String cursor, boolean finished) {
            super(cursor, finished);
        }

        @Override
        public void setCursor(String cursor) {
            throw new UnsupportedOperationException("setCursor not supported on " + getClass().getSimpleName());
        }

        @Override
        public void setFinished(boolean finished) {
            throw new UnsupportedOperationException("setFinished not supported on " + getClass().getSimpleName());
        }

    }

}
