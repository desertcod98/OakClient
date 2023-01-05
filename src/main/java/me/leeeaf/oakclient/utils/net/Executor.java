package me.leeeaf.oakclient.utils.net;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Executor {
    //used to execute tasks on another thread to achieve asynchronization (use this instead of creating multiple
    //async methods in HttpManager)
    private static final ExecutorService executorService = Executors.newSingleThreadExecutor();

    public static void execute(Runnable runnable){
        executorService.execute(runnable);
    }
}
