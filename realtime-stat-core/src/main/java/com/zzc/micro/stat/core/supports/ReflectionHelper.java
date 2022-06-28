package com.zzc.micro.stat.core.supports;

import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;

/**
 * @author Administrator
 */
public class ReflectionHelper {

    private static final Reflections reflections = new Reflections(new ConfigurationBuilder().setUrls(ClasspathHelper.forPackage("com.zzc.micro"))
            .setScanners(new SubTypesScanner()));

    public static Reflections subTypeScanReflections() {
        return reflections;
    }
}
