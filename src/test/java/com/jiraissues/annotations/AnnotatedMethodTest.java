package com.jiraissues.annotations;

import lombok.SneakyThrows;
import org.assertj.core.api.Assertions;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class AnnotatedMethodTest {

    @DataProvider(parallel = true)
    private Object[][] methodsAnnotatedWithImportantAnno() {
        return new Object[][]{
                {"annotatedWithBug"},
                {"annotatedWithWorkInProgress"}
        };
    }

    @Test(dataProvider = "methodsAnnotatedWithImportantAnno")
    @SneakyThrows
    public void shouldBeTrueForAnnotatedWithBugOrWipMethod(String methodName) {
        //given
        Class<? extends AnnotatedMethodTest> aClass = this.getClass();
        AnnotatedMethod method = new AnnotatedMethod(aClass.getMethod(methodName));

        //when
        boolean annotated = method.isAnnotated();

        //then
        Assertions.assertThat(annotated).isTrue();
    }

    @DataProvider(parallel = true)
    private Object[][] methodsAnnotatedWithNotImportantAnno() {
        return new Object[][]{
                {"annotatedWithSomethingElse"},
                {"notAnnotated"}
        };
    }

    @Test(dataProvider = "methodsAnnotatedWithNotImportantAnno")
    @SneakyThrows
    public void shouldBeFalseForAnnotatedWithOtherAnnotaionsOrNotAnnotatedMethod(String methodName) {
        //given
        Class<? extends AnnotatedMethodTest> aClass = this.getClass();
        AnnotatedMethod method = new AnnotatedMethod(aClass.getMethod(methodName));

        //when
        boolean annotated = method.isAnnotated();

        //then
        Assertions.assertThat(annotated).isFalse();
    }

    @Bug("anno")
    public void annotatedWithBug() {

    }

    @WorkInProgress("anno")
    public void annotatedWithWorkInProgress() {

    }

    @SneakyThrows
    public void annotatedWithSomethingElse() {

    }

    public void notAnnotated() {

    }
}