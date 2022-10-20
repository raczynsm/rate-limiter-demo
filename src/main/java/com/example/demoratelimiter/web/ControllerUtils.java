package com.example.demoratelimiter.web;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.function.Supplier;

import static java.util.concurrent.CompletableFuture.supplyAsync;

public class ControllerUtils {

    private static final Executor EXECUTOR = Executors.newSingleThreadExecutor();

    public static <T> CompletableFuture<T> doAsync(Supplier<T> supplier) {
        return supplyAsync(supplier, EXECUTOR);
    }
}