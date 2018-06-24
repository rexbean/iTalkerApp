package net.rex.italker.factory;

import net.rex.italker.common.app.Application;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class Factory {
    // singleton pattern
    private static final Factory instance;
    // thread pool
    private final Executor executor;

    // ???这是什么？
    static {
        instance = new Factory();
    }

    /**
     * return global Application
     * @return Application
     */
    public static Application app(){
        return Application.getInstance();
    }

    private Factory(){
        executor =  Executors.newFixedThreadPool(4);
    }

    /**
     * running Aynchronous
     */
    public static void runOnAsync(Runnable runnable){
        instance.executor.execute(runnable);

    }
}
