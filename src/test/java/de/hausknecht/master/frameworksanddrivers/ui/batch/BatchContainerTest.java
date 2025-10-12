package de.hausknecht.master.frameworksanddrivers.ui.batch;

import de.hausknecht.master.entity.domain.eventdata.PointsChangedEvent;
import de.hausknecht.master.entity.domain.eventdata.UpdatedTimeEvent;
import de.hausknecht.master.frameworksanddrivers.ui.UITest;
import de.hausknecht.master.frameworksanddrivers.ui.overlay.BatchOverlayContainer;
import de.hausknecht.master.usecase.PointSystemAdministrator;
import de.hausknecht.master.usecase.SessionTimeAccessor;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.util.Duration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static de.hausknecht.master.ConstantProvider.*;
import static de.hausknecht.master.TestDataGenerator.*;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

class BatchContainerTest extends UITest {

    private final SessionTimeAccessor sessionTimeAccessorMock = mock();
    private final BatchOverlayContainer batchOverlayContainerMock = mock();
    private final PointSystemAdministrator pointSystemAdministratorMock = mock();

    private final BatchContainer classUnderTest = new BatchContainer(sessionTimeAccessorMock, batchOverlayContainerMock, pointSystemAdministratorMock);

    @BeforeEach
    void setUp() {
        classUnderTest.batchPage = new HBox();
        classUnderTest.timeNovice = new ImageView(new Image(BATCH_TIME_NOVICE_PATH_WITHOUT_SATURATION, false));
        classUnderTest.timeIntermediate = new ImageView(new Image(BATCH_TIME_INTERMEDIATE_PATH_WITHOUT_SATURATION, false));
        classUnderTest.timeMaster = new ImageView(new Image(BATCH_TIME_MASTER_PATH_WITHOUT_SATURATION, false));
        classUnderTest.timeKing = new ImageView(new Image(BATCH_TIME_KING_PATH_WITHOUT_SATURATION, false));
        classUnderTest.exerciseNovice = new ImageView(new Image(BATCH_EXERCISE_NOVICE_PATH_WITHOUT_SATURATION, false));
        classUnderTest.exerciseIntermediate = new ImageView(new Image(BATCH_EXERCISE_INTERMEDIATE_PATH_WITHOUT_SATURATION, false));
        classUnderTest.exerciseMaster = new ImageView(new Image(BATCH_EXERCISE_MASTER_PATH_WITHOUT_SATURATION, false));
        classUnderTest.exerciseKing = new ImageView(new Image(BATCH_EXERCISE_KING_PATH_WITHOUT_SATURATION, false));
        classUnderTest.legend = new ImageView(new Image(BATCH_LEGEND_WITHOUT_SATURATION_PATH, false));

        when(sessionTimeAccessorMock.getSessionTime()).thenReturn(Duration.minutes(25));
        when(pointSystemAdministratorMock.getPoints()).thenReturn(57);
    }

    @Nested
    class Initialize {

        @Test
        void callInitialize() {
            classUnderTest.batchPage.setVisible(true);

            classUnderTest.initialize();
            fxWait(250);

            assertFalse(classUnderTest.timeNovice.getImage().getUrl().contains(BATCH_TIME_NOVICE_PATH_WITHOUT_SATURATION));
            assertFalse(classUnderTest.timeIntermediate.getImage().getUrl().contains(BATCH_TIME_INTERMEDIATE_PATH_WITHOUT_SATURATION));
            assertTrue(classUnderTest.timeMaster.getImage().getUrl().contains(BATCH_TIME_MASTER_PATH_WITHOUT_SATURATION));
            assertTrue(classUnderTest.timeKing.getImage().getUrl().contains(BATCH_TIME_KING_PATH_WITHOUT_SATURATION));
            assertFalse(classUnderTest.exerciseNovice.getImage().getUrl().contains(BATCH_EXERCISE_NOVICE_PATH_WITHOUT_SATURATION));
            assertTrue(classUnderTest.exerciseIntermediate.getImage().getUrl().contains(BATCH_EXERCISE_INTERMEDIATE_PATH_WITHOUT_SATURATION));
            assertTrue(classUnderTest.exerciseMaster.getImage().getUrl().contains(BATCH_EXERCISE_MASTER_PATH_WITHOUT_SATURATION));
            assertTrue(classUnderTest.exerciseKing.getImage().getUrl().contains(BATCH_EXERCISE_KING_PATH_WITHOUT_SATURATION));
            assertTrue(classUnderTest.legend.getImage().getUrl().contains(BATCH_LEGEND_WITHOUT_SATURATION_PATH));

            assertTrue(classUnderTest.timeNovice.getImage().getUrl().contains(BATCH_TIME_NOVICE_PATH));
            assertTrue(classUnderTest.timeIntermediate.getImage().getUrl().contains(BATCH_TIME_INTERMEDIATE_PATH));
            assertTrue(classUnderTest.exerciseNovice.getImage().getUrl().contains(BATCH_EXERCISE_NOVICE_PATH));

        }

        @Test
        void callInitializeWithLegend() {
            when(sessionTimeAccessorMock.getSessionTime()).thenReturn(Duration.minutes(500));
            when(pointSystemAdministratorMock.getPoints()).thenReturn(500);

            classUnderTest.batchPage.setVisible(true);
            classUnderTest.initialize();
            fxWait(250);

            assertFalse(classUnderTest.batchPage.isVisible());
            assertTrue(classUnderTest.timeNovice.getImage().getUrl().contains(BATCH_TIME_NOVICE_PATH));
            assertTrue(classUnderTest.timeIntermediate.getImage().getUrl().contains(BATCH_TIME_INTERMEDIATE_PATH));
            assertTrue(classUnderTest.timeMaster.getImage().getUrl().contains(BATCH_TIME_MASTER_PATH));
            assertTrue(classUnderTest.timeKing.getImage().getUrl().contains(BATCH_TIME_KING_PATH));
            assertTrue(classUnderTest.exerciseNovice.getImage().getUrl().contains(BATCH_EXERCISE_NOVICE_PATH));
            assertTrue(classUnderTest.exerciseIntermediate.getImage().getUrl().contains(BATCH_EXERCISE_INTERMEDIATE_PATH));
            assertTrue(classUnderTest.exerciseMaster.getImage().getUrl().contains(BATCH_EXERCISE_MASTER_PATH));
            assertTrue(classUnderTest.exerciseKing.getImage().getUrl().contains(BATCH_EXERCISE_KING_PATH));
            assertTrue(classUnderTest.legend.getImage().getUrl().contains(BATCH_LEGEND_PATH));
        }
    }

    @Nested
    class Hide {

        @Test
        void callHide() {
            classUnderTest.batchPage.setVisible(true);

            classUnderTest.hide();
            fxWait(250);

            assertFalse(classUnderTest.batchPage.isVisible());
        }
    }

    @Nested
    class Show {

        @Test
        void callShow() {
            classUnderTest.batchPage.setVisible(false);

            classUnderTest.show();
            fxWait(250);

            assertTrue(classUnderTest.batchPage.isVisible());
        }
    }

    @Nested
    class OnTimerUpdated {

        @Test
        void withValue() {
            classUnderTest.onTimerUpdated(new UpdatedTimeEvent());
            fxWait(250);

            verify(sessionTimeAccessorMock, times(1)).getSessionTime();
        }

        @Test
        void eventInputIsNull() {
            classUnderTest.onTimerUpdated(null);
            fxWait(250);

            verify(sessionTimeAccessorMock, times(1)).getSessionTime();
        }
    }

    @Nested
    class OnPointsChanged {

        @Test
        void withValue() {
            classUnderTest.onPointsChanged(new PointsChangedEvent("Dummy Message", true));
            fxWait(250);

            verify(pointSystemAdministratorMock, times(1)).getPoints();
        }

        @Test
        void eventInputIsNull() {
            classUnderTest.onPointsChanged(null);
            fxWait(250);

            verify(pointSystemAdministratorMock, times(1)).getPoints();
        }
    }
}