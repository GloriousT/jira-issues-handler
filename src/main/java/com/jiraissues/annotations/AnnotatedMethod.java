package com.jiraissues.annotations;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.lang.reflect.Method;
import java.util.Optional;

import static java.util.Objects.nonNull;

@AllArgsConstructor
public class AnnotatedMethod implements AnnotatedEntities {

    @Getter
    private Method method;

    @Override
    public boolean isAnnotated() {
        Bug bug = getBugAnnotation();
        WorkInProgress workInProgress = getWorkInProgressAnnotation();

        return (nonNull(bug) || nonNull(workInProgress));
    }

    private WorkInProgress getWorkInProgressAnnotation() {
        return method.getAnnotation(WorkInProgress.class);
    }

    private Bug getBugAnnotation() {
        return method.getAnnotation(Bug.class);
    }

    public String[] getAnnotationValue() {
        Optional<Bug> maybeBug = Optional.ofNullable(getBugAnnotation());
        Optional<WorkInProgress> maybeWorkInProgress = Optional.ofNullable(getWorkInProgressAnnotation());

        if (maybeBug.isPresent()){
            return maybeBug.get().value();
        } else if (maybeWorkInProgress.isPresent()){
            return maybeWorkInProgress.get().value();
        }
        return null;
    }

}
