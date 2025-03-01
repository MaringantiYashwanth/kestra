package io.kestra.plugin.core.condition;

import com.google.common.collect.ImmutableMap;
import io.kestra.core.models.conditions.Condition;
import io.kestra.core.models.executions.Execution;
import io.kestra.core.models.flows.Flow;
import io.kestra.core.services.ConditionService;
import io.kestra.core.utils.TestsUtils;
import io.kestra.core.junit.annotations.KestraTest;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.time.DayOfWeek;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;
import jakarta.inject.Inject;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

@KestraTest
class OrTest {
    @Inject
    ConditionService conditionService;

    static Stream<Arguments> source() {
        return Stream.of(
            Arguments.of(
                Collections.singletonList(
                    DayWeek.builder()
                        .date("2013-09-08")
                        .dayOfWeek(DayOfWeek.SUNDAY)
                        .build()
                ),
                true
            ),
            Arguments.of(
                Arrays.asList(
                    DayWeek.builder()
                        .date("2013-09-08")
                        .dayOfWeek(DayOfWeek.SATURDAY)
                        .build(),
                    DayWeek.builder()
                        .date("2013-09-08")
                        .dayOfWeek(DayOfWeek.MONDAY)
                        .build()
                ),
                false
            ),
            Arguments.of(
                Arrays.asList(
                    DayWeek.builder()
                        .date("2013-09-08")
                        .dayOfWeek(DayOfWeek.SUNDAY)
                        .build(),
                    DayWeek.builder()
                        .date("2013-09-08")
                        .dayOfWeek(DayOfWeek.MONDAY)
                        .build()
                ),
                true
            )
        );
    }

    @ParameterizedTest
    @MethodSource("source")
    void valid(List<Condition> conditions, boolean result) {
        Flow flow = TestsUtils.mockFlow();
        Execution execution = TestsUtils.mockExecution(flow, ImmutableMap.of());

        Or build = Or.builder()
            .conditions(conditions)
            .build();

        boolean test = conditionService.isValid(build, flow, execution);

        assertThat(test, is(result));
    }
}