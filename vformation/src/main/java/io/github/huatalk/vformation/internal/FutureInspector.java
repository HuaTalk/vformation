package io.github.huatalk.vformation.internal;

import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * Utility for inspecting the state of a {@link Future} without blocking.
 * <p>
 * Provides Java 8 compatible replacements for cffu2's
 * {@code CompletableFutureUtils.state()} and {@code CompletableFutureUtils.exceptionNow()},
 * which internally depend on {@code java.util.concurrent.Future.State} (Java 19+).
 *
 * @author Eric Lin (linqinghua4 at gmail dot com)
 */
public final class FutureInspector {

    private FutureInspector() {
    }

    /**
     * Returns the current state of the given future without blocking.
     *
     * @param future the future to inspect
     * @return the current {@link FutureState}
     */
    public static FutureState state(Future<?> future) {
        if (!future.isDone()) {
            return FutureState.RUNNING;
        }
        if (future.isCancelled()) {
            return FutureState.CANCELLED;
        }
        try {
            future.get();
            return FutureState.SUCCESS;
        } catch (ExecutionException e) {
            return FutureState.FAILED;
        } catch (CancellationException e) {
            return FutureState.CANCELLED;
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return FutureState.FAILED;
        }
    }

    /**
     * Returns the exception from a completed-exceptionally future.
     * <p>
     * Must only be called when the future is in the {@link FutureState#FAILED} state.
     *
     * @param future the failed future
     * @return the cause of the failure
     * @throws IllegalStateException if the future is not in a failed state
     */
    public static Throwable exceptionNow(Future<?> future) {
        if (!future.isDone()) {
            throw new IllegalStateException("Future has not completed yet");
        }
        if (future.isCancelled()) {
            throw new IllegalStateException("Future was cancelled, not failed");
        }
        try {
            future.get();
            throw new IllegalStateException("Future completed successfully, not failed");
        } catch (ExecutionException e) {
            return e.getCause();
        } catch (CancellationException e) {
            throw new IllegalStateException("Future was cancelled, not failed");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new IllegalStateException("Interrupted while inspecting future", e);
        }
    }
}
