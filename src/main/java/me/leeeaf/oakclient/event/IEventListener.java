package me.leeeaf.oakclient.event;


public interface IEventListener {
    void call(Object event); //TODO maybe Event event?

    Class<?> getTarget();

}
