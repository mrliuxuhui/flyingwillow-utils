package com.flyingwillow.utils.refactor.branch;

import java.util.function.BooleanSupplier;
import java.util.function.Supplier;

public class BranchCondition<T> {
    private BooleanSupplier condition;
    private Supplier<T> supplier;
    private BranchBuilder<T> builder;

    public BranchCondition(BooleanSupplier condition, BranchBuilder<T> builder) {
        this.condition = condition;
        this.builder = builder;
    }

    protected Boolean condition() {
        return condition.getAsBoolean();
    }

    public BranchBuilder<T> then(Supplier<T> supplier) {
        this.supplier = supplier;
        return this.builder;
    }

    public BranchBuilder<T> then(VoidSupplier supplier) {
        this.supplier = (Supplier<T>) supplier;
        return this.builder;
    }

    public BranchBuilder<T> thenForReturn(Supplier<T> supplier) {
        this.supplier = supplier;
        return this.builder;
    }
    protected T get() {
        return this.supplier.get();
    }
}
