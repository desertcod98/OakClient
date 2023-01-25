package me.leeeaf.oakclient.event;

import java.lang.reflect.Method;
import java.util.*;

public class EventBus {
    private static EventBus eventBus;

    public static EventBus getEventBus() {
        if (eventBus == null) {
            eventBus = new EventBus();
        }
        return eventBus;
    }

    private final HashMap<Class<?>, List<EventSubscriber>> subscribersMap = new HashMap<>();

    Comparator<EventSubscriber> subscriberComparator;

    private EventBus() {
        subscriberComparator = Comparator.comparingInt(EventSubscriber::getEventPriorityValue).reversed();

    }


    public <T> T post(T event) {
        List<EventSubscriber> subscribers = subscribersMap.get(event.getClass());
        if (subscribers != null) {
            for (EventSubscriber subscriber : subscribers) {
                subscriber.call(event);
            }
        }
        return event;
    }

    public boolean subscribe(Object eventListener) {
        boolean subscribed = false;
        for (Method method : eventListener.getClass().getDeclaredMethods()) {
            if (method.isAnnotationPresent(EventSubscribe.class) && method.getParameterTypes().length > 0) {
                insert(subscribersMap.computeIfAbsent(method.getParameterTypes()[0], k -> new ArrayList<>()), new EventSubscriber(method, eventListener));
                subscribed = true;
            }
        }
        return subscribed;
    }

    private void insert(List<EventSubscriber> subscribers, EventSubscriber subscriber) {
        int index = Collections.binarySearch(subscribers, subscriber, subscriberComparator);
        if (index < 0) {
            index = -index - 1;
        }
        subscribers.add(index, subscriber);
    }

    public void unsubscribe(Object eventListener) {
        subscribersMap.values().forEach(subscribers -> subscribers.removeIf(subscriber -> subscriber.getListenerInstance().equals(eventListener)));
    }
}
