package com.flyingwillow.utils.refactor.branch;

import java.util.Optional;
import java.util.function.BooleanSupplier;
import java.util.function.Supplier;

public class BranchBuilder<T> {

    private BranchCompose<T> compose;

    public BranchBuilder() {
        this.compose = new BranchCompose<>();
    }

    public static BranchBuilder<Void> nullReturnBuilder() {
        return new BranchBuilder<>();
    }

    public BranchCondition<T> on(BooleanSupplier condition) {
        BranchCondition<T> branchCondition = new BranchCondition<>(condition, this);
        this.compose.addCondition(branchCondition);
        return branchCondition;
    }

    public BranchCompose<T> otherwise(Supplier<T> supplier) {
        return this.compose.setOtherwise(supplier);
    }

    public BranchCompose<T> otherwise(VoidSupplier supplier) {
        return this.compose.setOtherwise((Supplier<T>) supplier);
    }

    public void apply() {
        this.compose.apply();
    }

    public Optional<T> get() {
        return this.compose.get();
    }
}
