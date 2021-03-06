package edu.utn.design.patterns.strategy;

import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.AppenderBase;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class HowToAttackStrategyTest {

    /**
     * Assembles test parameters.
     *
     * @return The test parameters for each cycle
     */
    static Collection<Object[]> dataProvider() {
        return Arrays.asList(
                new Object[]{
                        new HowToNotAttack(),
                        "You can't attack!"
                },
                new Object[]{
                        new HowToAttackWithSword(),
                        "You can attack with your Sword!"
                },
                new Object[]{
                        new HowToAttackWithBow(),
                        "You can attack with your Bow!"
                },
                new Object[]{
                        new HowToAttackWithAxe(),
                        "You can attack with your Axe!"
                }
        );
    }

    private InMemoryAppender appender;

    @BeforeEach
    public void setUp() {
        appender = new InMemoryAppender();
    }

    @AfterEach
    public void tearDown() {
        appender.stop();
    }

    /**
     * Test if executing the strategy gives the correct response.
     */
    @ParameterizedTest
    @MethodSource("dataProvider")
    @DisplayName(value = "Test Attack Method")
    void testAttack(HowToAttackStrategy strategy, String expectedResult) {
        strategy.attack();
        assertEquals(expectedResult, appender.getLastMessage());
        assertEquals(1, appender.getLogSize());
    }


    private static class InMemoryAppender extends AppenderBase<ILoggingEvent> {
        private final List<ILoggingEvent> log = new LinkedList<>();

        public InMemoryAppender() {
            ((Logger) LoggerFactory.getLogger("root")).addAppender(this);
            start();
        }

        @Override
        protected void append(ILoggingEvent eventObject) {
            log.add(eventObject);
        }

        public int getLogSize() {
            return log.size();
        }

        public String getLastMessage() {
            return log.get(log.size() - 1).getFormattedMessage();
        }
    }

}