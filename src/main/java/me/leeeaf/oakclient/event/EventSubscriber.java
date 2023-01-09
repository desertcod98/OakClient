package me.leeeaf.oakclient.event;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class EventSubscriber {
    private final Method method;
    private final Object listenerInstance;

    public EventSubscriber(Method method, Object listenerInstance) {
        this.method = method;
        this.listenerInstance = listenerInstance;
    }

    public <T> void call(T event){
        try {
            method.invoke(listenerInstance, event);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    public Object getListenerInstance(){
        return listenerInstance;
    }

    public int getEventPriorityValue(){
        return method.getAnnotation(EventSubscribe.class).priority().getValue();
    }
}
