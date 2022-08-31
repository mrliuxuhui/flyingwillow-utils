package com.flyingwillow.utils.refactor.branch;

import org.junit.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class BranchBuilderTest {

    @Test
    public void 无返回值排序_无else() {

        final String[] r = new String[1];
        BranchBuilder.nullReturnBuilder()
                .on(() -> 3 > 5)
                .then(() -> r[0] = "a")
                .on(() -> 6 > 5)
                .then(() -> r[0] = "b")
                .apply();
        assertThat(r[0]).isEqualTo("b");
    }

    @Test
    public void 有返回值排序_无else() {

        Optional optional = new BranchBuilder<String>().on(() -> 3 > 5)
                .then(() -> "if")
                .otherwise(() -> "else")
                .get();
        assertThat(optional).isNotNull();
        assertThat(optional.get()).isEqualTo("else");
    }

    @Test
    public void 直接接收值_无else() {

        String a = new BranchBuilder<String>().on(() -> 3 > 5)
                .then(() -> "if")
                .otherwise(() -> "else")
                .get().get();
        assertThat(a).isEqualTo("else");
    }

}
