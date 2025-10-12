package de.hausknecht.master.entity.service.automata.registries;

import de.hausknecht.master.TestDataGenerator;
import de.hausknecht.master.entity.domain.automata.TransitionTriple;
import de.hausknecht.master.entity.domain.eventdata.GraphChangedEvent;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationEventPublisher;

import java.util.List;

import static de.hausknecht.master.TestDataGenerator.*;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TransitionRegistryTest {

    private final ApplicationEventPublisher applicationEventPublisherMock = mock();
    private final TransitionRegistry classUnderTest =  new TransitionRegistry(applicationEventPublisherMock);

    @Nested
    class AddTransition {

        @Test
        void withValues() {
            TransitionTriple transitionTriple = new TransitionTriple(NODE_0, NODE_1, CHAR_A);

            boolean actual = classUnderTest.addTransition(transitionTriple);

            verify(applicationEventPublisherMock, times(1)).publishEvent(new GraphChangedEvent());
            assertTrue(actual);
        }

        @Test
        void transitionIsNull() {
            boolean actual = classUnderTest.addTransition(null);

            verify(applicationEventPublisherMock, times(0)).publishEvent(new GraphChangedEvent());
            assertFalse(actual);
        }

        @Test
        void fromNodeIsBlank() {
            TransitionTriple transitionTriple = new TransitionTriple(" ", NODE_1, CHAR_A);

            boolean actual = classUnderTest.addTransition(transitionTriple);

            verify(applicationEventPublisherMock, times(0)).publishEvent(new GraphChangedEvent());
            assertFalse(actual);
        }

        @Test
        void toNodeIsBlank() {
            TransitionTriple transitionTriple = new TransitionTriple(NODE_0, " ", CHAR_A);

            boolean actual = classUnderTest.addTransition(transitionTriple);

            verify(applicationEventPublisherMock, times(0)).publishEvent(new GraphChangedEvent());
            assertFalse(actual);
        }

        @Test
        void transitionWordIsBlank() {
            TransitionTriple transitionTriple = new TransitionTriple(NODE_0, NODE_1, " ");

            boolean actual = classUnderTest.addTransition(transitionTriple);

            verify(applicationEventPublisherMock, times(0)).publishEvent(new GraphChangedEvent());
            assertFalse(actual);
        }

        @Test
        void transitionWordContainsWhitespace() {
            TransitionTriple transitionTriple = new TransitionTriple(NODE_0, NODE_1, CHAR_A + " " + CHAR_A);

            boolean actual = classUnderTest.addTransition(transitionTriple);

            verify(applicationEventPublisherMock, times(0)).publishEvent(new GraphChangedEvent());
            assertFalse(actual);
        }

        @Test
        void transitionAlreadyExists() {
            TransitionTriple transitionTriple = new TransitionTriple(NODE_0, NODE_1, CHAR_A);
            classUnderTest.addTransition(transitionTriple);

            boolean actual = classUnderTest.addTransition(transitionTriple);

            verify(applicationEventPublisherMock, times(1)).publishEvent(new GraphChangedEvent());
            assertFalse(actual);
        }
    }

    @Nested
    class RemoveAllNodesThatContainNode {

        @Test
        void withValuesAndSthToRemove() {
            TestDataGenerator.getTransitions().forEach(classUnderTest::addTransition);

            classUnderTest.removeAllTransitionsThatContainNode(NODE_1);

            List<TransitionTriple> transitions = classUnderTest.getTransitions();
            verify(applicationEventPublisherMock, times(7)).publishEvent(new GraphChangedEvent());
            assertThat(transitions.size()).isEqualTo(3);
        }

        @Test
        void withValuesAndNthToRemove() {
            classUnderTest.removeAllTransitionsThatContainNode(NODE_1);
            verify(applicationEventPublisherMock, times(0)).publishEvent(new GraphChangedEvent());
        }

        @Test
        void inputIsNull() {
            classUnderTest.removeAllTransitionsThatContainNode(null);
            verify(applicationEventPublisherMock, times(0)).publishEvent(new GraphChangedEvent());

        }

        @Test
        void inputIsBlank() {
            classUnderTest.removeAllTransitionsThatContainNode(" ");
            verify(applicationEventPublisherMock, times(0)).publishEvent(new GraphChangedEvent());
        }
    }

    @Nested
    class RemoveNode {

        @Test
        void withValuesAndSthToRemove() {
            TestDataGenerator.getTransitions().forEach(classUnderTest::addTransition);

            classUnderTest.removeTransition(TestDataGenerator.getTransitions().getFirst());

            List<TransitionTriple> transitions = classUnderTest.getTransitions();
            verify(applicationEventPublisherMock, times(6)).publishEvent(new GraphChangedEvent());
            assertThat(transitions.size()).isEqualTo(4);
        }

        @Test
        void withValuesAndNthToRemove() {
            classUnderTest.removeTransition(TestDataGenerator.getTransitions().getFirst());

            verify(applicationEventPublisherMock, times(0)).publishEvent(new GraphChangedEvent());
        }

        @Test
        void inputIsNull() {
            classUnderTest.removeTransition(null);
            verify(applicationEventPublisherMock, times(0)).publishEvent(new GraphChangedEvent());

        }

        @Test
        void fromNodeIsBlank() {
            classUnderTest.removeTransition(new TransitionTriple(" ", NODE_1, CHAR_A));
            verify(applicationEventPublisherMock, times(0)).publishEvent(new GraphChangedEvent());
        }

        @Test
        void toNodeIsBlank() {
            classUnderTest.removeTransition(new TransitionTriple(NODE_0, " ", CHAR_A));
            verify(applicationEventPublisherMock, times(0)).publishEvent(new GraphChangedEvent());
        }

        @Test
        void transitionWordIsBlank() {
            classUnderTest.removeTransition(new TransitionTriple(NODE_0, NODE_1, " "));
            verify(applicationEventPublisherMock, times(0)).publishEvent(new GraphChangedEvent());
        }
    }
}