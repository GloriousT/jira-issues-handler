package com.jiraissues;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static com.jiraissues.Strategy.*;
import static org.assertj.core.api.Assertions.assertThat;

public class StrategyTest {

    @DataProvider
    Object[][] strategyNames() {
        return new Object[][]{
                {"skip", SKIP},
                {"Skip", SKIP},
                {"fail", FAIL},
                {"Fail", FAIL},
                {"execute", EXECUTE},
                {"Execute", EXECUTE}
        };
    }

    @Test(dataProvider = "strategyNames")
    public void shouldReturnStrategyValue(String strategyName, Strategy expectedStrategy) {
        //when
        Strategy strategy = fromValue(strategyName);

        //then
        assertThat(strategy).isEqualTo(expectedStrategy);
    }

    @Test
    public void shouldReturnNull() {
        //when
        Strategy strategy = fromValue("nonsense");

        //then
        assertThat(strategy).isNull();
    }
}