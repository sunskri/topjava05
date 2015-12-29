package ru.javawebinar.topjava;

import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

/**
 * Created by master on 29.12.2015.
 */
public class TimeRule implements TestRule {
    @Override
    public Statement apply(Statement base, Description description) {
        return new Statement() {
            @Override
            public void evaluate() throws Throwable {
                long startTime = System.currentTimeMillis();
                base.evaluate();
                long endTime = System.currentTimeMillis();
                System.out.println("Test time: " + (endTime - startTime) + "ms");
            }
        };
    }
}
