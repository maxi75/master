package de.hausknecht.master;

import javafx.util.Duration;

import java.util.ArrayList;

public class ConstantProvider {
    public static ArrayList<String> getCSSFiles() {
        ArrayList<String> cssFiles = new ArrayList<>();
        cssFiles.add("/ui/css/overlay.css");
        cssFiles.add("/ui/css/menu.css");
        cssFiles.add("/ui/css/batch.css");
        cssFiles.add("/ui/css/splitPane.css");
        cssFiles.add("/ui/css/theory.css");
        cssFiles.add("/ui/css/simulation.css");
        return cssFiles;
    }

    public static final String APPLICATION_NAME = "Automata Theory";
    public static final String MAIN_ICON_PATH = "/packaging/icons/icon.png";
    public static final String MAIN_FXML_PATH = "/ui/fxml/Main.fxml";

    public static final String THEORY_JSON_PATH = "theory/json/";
    public static final String THEORY_IMAGES_PATH = "packaging/theory/";

    public static final String BATCH_TIME_NOVICE_PATH = "/packaging/batches/time/TimeNovice.png";
    public static final String BATCH_TIME_INTERMEDIATE_PATH = "/packaging/batches/time/TimeIntermediate.png";
    public static final String BATCH_TIME_MASTER_PATH = "/packaging/batches/time/TimeMaster.png";
    public static final String BATCH_TIME_KING_PATH = "/packaging/batches/time/TimeKing.png";
    public static final String BATCH_EXERCISE_NOVICE_PATH = "/packaging/batches/exercise/ExerciseNovice.png";
    public static final String BATCH_EXERCISE_INTERMEDIATE_PATH = "/packaging/batches/exercise/ExerciseIntermediate.png";
    public static final String BATCH_EXERCISE_MASTER_PATH = "/packaging/batches/exercise/ExerciseMaster.png";
    public static final String BATCH_EXERCISE_KING_PATH = "/packaging/batches/exercise/ExerciseKing.png";
    public static final String BATCH_LEGEND_PATH = "/packaging/batches/legend/Legend.png";
    public static final String BATCH_LEGEND_WITHOUT_SATURATION_PATH = "/packaging/batches/legend/LegendWithoutSaturation.png";

    public static final Duration ANIMATION_DURATIONS = Duration.seconds(4);
    public static final Duration TRANSITION_DURATIONS = Duration.millis(350);

    public static final int POINTS_SPECIAL_EXERCISE = 500;

    public static final double GRAPH_ZOOM_MIN = 0.2;
    public static final double GRAPH_ZOOM_MAX = 5.0;
    public static final double GRAPH_ZOOM_STEP = 0.1;

    public static final String EMPTY_STRING = "";
    public static final String SPACE = " ";
    public static final String EQUALS = " = ";
    public static final String SEMICOLON = ";   ";
    public static final String DOUBLE_MINUS = " --";
    public static final String ARROW = "--> ";
    public static final String PLUS = "+ ";
    public static final String MINUS = "_ ";
    public static final String COMMA = ", ";

    public static final String CHECK_CSS_STYLE = "hboxCheck";
    public static final String SOLVE = "Lösen";
    public static final String SOLVE_CSS_ID = "buttonSolve";
    public static final String CHECK_SOLUTION = "Prüfe Lösung";
    public static final String CHECK_SOLUTION_CSS_ID = "buttonCheck";
    public static final String WRONG_ANSWER_CSS_ID = "wrongAnswer";
    public static final String CORRECT_ANSWER_CSS_ID = "correctAnswer";

    public static final int ADD_POINTS_CORRECT_CHECK = 5;
    public static final int SUBTRACT_POINTS_WRONG_CHECK = 10;
    public static final int SUBTRACT_POINTS_ON_SOLVE = 20;
    public static final int MULTIPLE_CHOICE_WRONG_ANSWER_POINTS = 2;
    public static final int MULTIPLE_CHOICE_CORRECT_ANSWER_POINTS = 2;

    public static final String INTRODUCTION = "Gegeben ist folgende Automatendefinition: \n A=({%s}, {%s}, δ, %s, {%s}) \n\n";
    public static final String DEFINITION = "mit der Zustandsübergangsfunktion: \n %s \n\n";
    public static final String TERM = "δ(%s,%s)";

    public static final String SPLIT_BY_REGEX = "\\s+";

}
