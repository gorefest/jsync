package net.mantucon.jsync.handler;

import net.mantucon.jsync.Configuration;

import java.io.File;

/**
 * Created by marcus on 15.04.14.
 */
public class HandlerFactory {
    Class<?> handlerClass = null;

    final Configuration configuration;

    public HandlerFactory(Configuration configuration) {
        this.configuration = configuration;
    }

    public Handler produceHandlerInstance() {
        if (handlerClass == null) {
            try {
                handlerClass = (Class.forName(configuration.getHandlerClassName()));
                return (Handler) handlerClass.getConstructor(Configuration.class).newInstance(configuration);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        } else {
            try {
                return (Handler) handlerClass.getConstructor(Configuration.class).newInstance(configuration);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

}
