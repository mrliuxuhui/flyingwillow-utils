package com.flyingwillow.utils.refactor.branch;

import java.util.function.Supplier;

@FunctionalInterface
public interface VoidSupplier extends Supplier<Void> {
    @Override
    default Void get() {
        apply();
        return null;
    }

    void apply();
}
