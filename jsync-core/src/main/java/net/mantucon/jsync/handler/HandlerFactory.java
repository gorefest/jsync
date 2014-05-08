package net.mantucon.jsync.handler;

import net.mantucon.jsync.Configuration;

import java.io.File;

/**
 * Created by marcus on 15.04.14.
 */
public class HandlerFactory {
    static Class<?> handlerClass;

    public static Handler produceHandlerInstance() {
        if (handlerClass == null) {
            try {
                handlerClass = Class.forName(Configuration.getHandlerClassName());
                return (Handler) handlerClass.newInstance();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        } else {
            try {
                return (Handler) handlerClass.newInstance();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }
}
