package com.zzc.micro.stat.core.config.event;

import com.zzc.micro.stat.biz.events.Event;
import com.zzc.micro.stat.core.supports.ReflectionHelper;
import org.reflections.Reflections;
import org.reflections.util.Utils;

import java.lang.reflect.Modifier;
import java.util.Set;

/**
 * @author Administrator
 */
public final class EventManager {

    private static final Set<Class<? extends Event>> eventClasses;

    static {
        Reflections reflections = ReflectionHelper.subTypeScanReflections();
        eventClasses = Utils.filter(reflections.getSubTypesOf(Event.class), c -> !Modifier.isAbstract(c.getModifiers()));
    }

    @SuppressWarnings("unused")
    public static Set<Class<? extends Event>> allEventClasses() {
        return eventClasses;
    }

    public static Class<? extends Event> find(final String eventClassName) {
        return eventClasses.stream()
                .filter(c -> c.getName().equals(eventClassName) || c.getSimpleName().equalsIgnoreCase(eventClassName))
                .findFirst()
                .orElseThrow(() -> new EventNotFoundException(eventClassName));
    }
}
