package de.hausknecht.master.frameworksanddrivers.ui.content.simulation.simulationOverlay;

import de.hausknecht.master.entity.domain.eventdata.GraphChanged;
import de.hausknecht.master.entity.domain.eventdata.SimulationEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;

@Setter
@Component
@RequiredArgsConstructor
public class SimulationOverlay {
    @FXML private TextField word;

    @FXML private Button startBtn;
    @FXML private Button fastBackwordBtn;
    @FXML private Button backwardBtn;
    @FXML private Button forwardBtn;
    @FXML private Button fastForwardBtn;
    @FXML private Button stopBtn;

    private String alreadySimulatedWord = "";
    private boolean alreadyStarted = false;

    private final ApplicationEventPublisher applicationEventPublisher;

    @FXML
    public void initialize(){
        word.textProperty().addListener(_ -> alreadySimulatedWord = "");
        forwardBtn.setOnAction(_ -> simulateForward());
        backwardBtn.setOnAction(_ -> simulateBack());
        fastBackwordBtn.setOnAction(_ -> simulateFirst());
        fastForwardBtn.setOnAction(_ -> simulateLast());
        startBtn.setOnAction(_ -> simulateFirst());
        stopBtn.setOnAction(_ -> simulateStop());
    }

    private void simulateForward() {
        if (alreadyStarted) { calculateNextInput(); }
        if (!alreadyStarted) { alreadyStarted = true; }
        applicationEventPublisher.publishEvent(new SimulationEvent(this.alreadySimulatedWord));
    }

    private void simulateBack() {
        if (!alreadyStarted) return;

        calculateLastInput();
        applicationEventPublisher.publishEvent(new SimulationEvent(this.alreadySimulatedWord));
    }

    private void simulateFirst() {
        if (!alreadyStarted) return;

        alreadySimulatedWord = "";
        applicationEventPublisher.publishEvent(new SimulationEvent(this.alreadySimulatedWord));
    }

    private void simulateLast() {
        alreadyStarted = true;
        alreadySimulatedWord = word.getText();
        applicationEventPublisher.publishEvent(new SimulationEvent(this.alreadySimulatedWord));
    }

    private void simulateStop() {
        alreadyStarted = false;
        alreadySimulatedWord = "";
        applicationEventPublisher.publishEvent(new GraphChanged());
    }

    private void calculateNextInput(){
        List<String> allWordsInTextField = List.of(word.getText().trim().split("\\s+"));
        List<String> alreadyCalculatedWords = alreadySimulatedWord == null || alreadySimulatedWord.isBlank() ?
                List.of() :
                List.of(alreadySimulatedWord.trim().split("\\s+"));

        if (allWordsInTextField.isEmpty() ||
                Objects.equals(alreadySimulatedWord, word.getText()) ||
                alreadyCalculatedWords.size() >= allWordsInTextField.size()) return;

        String nextWord = allWordsInTextField.get(alreadyCalculatedWords.size());
        alreadySimulatedWord = alreadySimulatedWord + nextWord + " ";
    }

    private void calculateLastInput(){
        List<String> alreadyCalculatedWords = alreadySimulatedWord == null || alreadySimulatedWord.isBlank() ?
                List.of() :
                List.of(alreadySimulatedWord.trim().split("\\s+"));

        alreadySimulatedWord = (alreadyCalculatedWords.size() <= 1) ?
                "" :
                String.join(" ", alreadyCalculatedWords.subList(0, alreadyCalculatedWords.size() - 1)) + " ";
    }
}
