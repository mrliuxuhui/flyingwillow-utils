package com.flyingwillow.utils.refactor.branch;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

public class BranchCompose<T> {
    private List<BranchCondition<T>> conditions = new ArrayList<>(10);
    private Supplier<T> otherwise;

    protected void addCondition(BranchCondition<T> branchCondition) {
        this.conditions.add(branchCondition);
    }

    public void apply() {
        get();
    }

    public Optional<T> get() {
        Optional<BranchCondition<T>> first = conditions.stream().filter(BranchCondition::condition).findFirst();
        if (first.isPresent()) {
            return Optional.ofNullable(first.get().get());
        }
        if (otherwise != null) {
            return Optional.ofNullable(otherwise.get());
        }
        return Optional.ofNullable(null);
    }

    public BranchCompose<T> setOtherwise(Supplier<T> supplier) {
        this.otherwise = supplier;
        return this;
    }

    public BranchCompose<T> setOtherwise(VoidSupplier supplier) {
        this.otherwise = (Supplier<T>) supplier;
        return this;
    }
}
