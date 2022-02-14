package com.zzc.micro.stat.core.accumulator;

import com.zzc.micro.stat.core.supports.ReflectionHelper;
import org.apache.commons.lang3.StringUtils;
import org.reflections.Reflections;
import org.reflections.util.Utils;

import java.lang.reflect.Modifier;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Administrator
 */
@SuppressWarnings("rawtypes")
public class AccumulatorImplFactory {

    private static final Map<String, Class<? extends Accumulator>> C;

    static {
        final Reflections reflections = ReflectionHelper.subTypeScanReflections();
        final Set<Class<? extends Accumulator>> impls = Utils.filter(reflections.getSubTypesOf(Accumulator.class), c -> !Modifier.isAbstract(c.getModifiers()));
        C = new ConcurrentHashMap<>();
        final String classNameSuffix = Accumulator.class.getSimpleName();

        impls.forEach(impl -> {
            final String name = impl.getSimpleName();
            C.put(lowerCase(name), impl);
            if (StringUtils.endsWith(name, classNameSuffix) && StringUtils.length(name) > StringUtils.length(classNameSuffix)) {
                String simpleName = StringUtils.substringBeforeLast(name, classNameSuffix);
                C.put(lowerCase(simpleName), impl);
            }
        });
    }

    public static Optional<Class<? extends Accumulator>> find(final String name) {
        return Optional.ofNullable(C.get(lowerCase(name)));
    }

    private static String lowerCase(final String str) {
        return StringUtils.lowerCase(str, Locale.US);
    }
}
