package me.leeeaf.oakclient.event;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
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

    private final HashMap<Class<?>, List<Object>> listenerMap = new HashMap<>();
    private EventBus(){}


    public <T> T post(T event){
        List<Object> listeners = listenerMap.get(event.getClass());
        if (listeners != null) {
            for (Object listener : listeners){
                for(Method method : listener.getClass().getDeclaredMethods()){
                    if(method.isAnnotationPresent(EventListener.class) && method.getParameterTypes().length > 0 && method.getParameterTypes()[0].equals(event.getClass())) {
                        try {
                            method.invoke(listener, event);
                        } catch (IllegalAccessException e) {
                            throw new RuntimeException(e);
                        } catch (InvocationTargetException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
            }
        }
        return event;
    }

    public boolean subscribe(Object eventListener){
        boolean subscribed = false;
        for(Method method : eventListener.getClass().getDeclaredMethods()){
            if(method.isAnnotationPresent(EventListener.class) && method.getParameterTypes().length > 0){
                insert(listenerMap.computeIfAbsent(method.getParameterTypes()[0], k -> new ArrayList<>()), eventListener);
                subscribed = true;
            }
        }
        return subscribed;
    }

    private void insert(List<Object> listeners, Object listener){
        listeners.add(listener);
    }

    public void unsubscribe(Object eventListener){
        for(Method method : eventListener.getClass().getDeclaredMethods()){ //TODO non funzia?
            if(method.isAnnotationPresent(EventListener.class) && method.getParameterTypes().length > 0){
                listenerMap.get(method.getParameterTypes()[0]).remove(eventListener);
            }
        }
    }
}
