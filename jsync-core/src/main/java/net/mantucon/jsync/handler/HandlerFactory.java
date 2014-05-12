package net.mantucon.jsync.handler;

import net.mantucon.jsync.Configuration;

import java.io.File;

/**
 * Created by marcus on 15.04.14.
 */
public class HandlerFactory {
    static ThreadLocal<Class<?>> handlerClass = new ThreadLocal<>();

    public static Handler produceHandlerInstance() {
        if (handlerClass.get() == null) {
            try {
                handlerClass.set(Class.forName(Configuration.getHandlerClassName()));
                return (Handler) handlerClass.get().newInstance();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        } else {
            try {
                return (Handler) handlerClass.get().newInstance();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }
}
