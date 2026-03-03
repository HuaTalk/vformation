package io.github.huatalk.vformation.internal;

/**
 * Represents the execution state of a {@link java.util.concurrent.Future}.
 * <p>
 * This enum replaces the external cffu2 dependency's {@code CffuState},
 * providing Java 8 compatible future state inspection without requiring
 * {@code java.util.concurrent.Future.State} (Java 19+).
 *
 * @author Eric Lin (linqinghua4 at gmail dot com)
 */
public enum FutureState {

    /** The task completed successfully. */
    SUCCESS,

    /** The task completed with an exception. */
    FAILED,

    /** The task was cancelled. */
    CANCELLED,

    /** The task has not yet completed. */
    RUNNING
}
