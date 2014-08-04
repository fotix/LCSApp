package com.trappz.app.api.messages;

import com.squareup.otto.Bus;

/**
 * Created by Filipe Oliveira on 30-06-2014.
 */
public class EventBusManager {
    private static Bus bus;

    public synchronized static Bus getInstance() {
        if (null == bus) {
            bus = new Bus();
        }

        return bus;
    }

    public static void post(Object message) {
        Bus instance = getInstance();
        instance.post(message);
    }

    public static void register(Object listener) {
        Bus instance = getInstance();
        instance.register(listener);
    }

    public static void unregister(Object listener) {
        Bus instance = getInstance();
        instance.unregister(listener);
    }
}
