package io.github.cshencode.support;

import java.util.concurrent.ConcurrentHashMap;

/**
 * <p>
 * -
 * </p>
 *
 * @author css
 * @since 2024/8/22
 */
public class JarUtil {
    private static final ConcurrentHashMap<String, Boolean> jarExistsMap = new ConcurrentHashMap<>();

    public static boolean exists(String classPath) {
        return jarExistsMap.computeIfAbsent(classPath, JarUtil::tryLoadClass);
    }

    public static Boolean tryLoadClass(String classPath) {
        try {
            Class.forName(classPath);
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }

}
