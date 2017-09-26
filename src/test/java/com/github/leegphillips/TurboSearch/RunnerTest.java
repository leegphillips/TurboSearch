package com.github.leegphillips.TurboSearch;

import org.apache.commons.cli.ParseException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = TurboSearchApplication.class)
public class RunnerTest {
    @Autowired
    private Runner runner;

    @Test
    public void sadPath() throws ParseException {
        runner.run();
    }
}
