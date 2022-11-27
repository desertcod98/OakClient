package me.leeeaf.oakclient.event;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class EventBus {
    private static EventBus eventBus;

    public static EventBus getEventBus(){
        if(eventBus == null){
            eventBus = new EventBus();
        }
        return eventBus;
    }

    private final HashMap<Class<?>, List<IEventListener>> listenerMap = new HashMap<>();
    private EventBus(){}


    public <T> T post(T event){
        List<IEventListener> listeners = listenerMap.get(event.getClass());
        if (listeners != null) {
            for (IEventListener listener : listeners) listener.call(event);
        }
        return event;
    }

    public void subscribe(IEventListener IEventListener){
        for(Class<?> target : IEventListener.getTargets()){
            insert(listenerMap.computeIfAbsent(target, k -> new ArrayList<>()), IEventListener);
        }
    }

    private void insert(List<IEventListener> listeners, IEventListener listener){
        listeners.add(listener);
    }

    public void unsubscribe(IEventListener IEventListener){
        for(Class<?> target : IEventListener.getTargets()){
            listenerMap.get(target).remove(IEventListener);
        }

    }


}
