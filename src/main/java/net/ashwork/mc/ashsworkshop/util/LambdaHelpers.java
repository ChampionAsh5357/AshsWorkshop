package net.ashwork.mc.ashsworkshop.util;

import java.util.function.Supplier;

public class LambdaHelpers {

    private LambdaHelpers() {}

    public static <T> Supplier<T> throwOnInit() {
        return () -> {
            throw new IllegalStateException("This object should never be initialized in this manner.");
        };
    }
}
