package de.hausknecht.master.frameworksanddrivers.ui.content.simulation.simulationOverlay;

import de.hausknecht.master.entity.domain.automata.AutomataSimulation;
import de.hausknecht.master.entity.domain.eventdata.GraphChangedEvent;
import de.hausknecht.master.entity.domain.eventdata.SimulationEvent;
import de.hausknecht.master.usecase.GraphAdministrator;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;

import static de.hausknecht.master.ConstantProvider.*;

@Setter
@Component
@RequiredArgsConstructor
public class SimulationOverlay {
    @FXML TextField word;
    @FXML Button startBtn;
    @FXML Button fastBackwordBtn;
    @FXML Button backwardBtn;
    @FXML Button forwardBtn;
    @FXML Button fastForwardBtn;
    @FXML Button stopBtn;
    @FXML ComboBox<AutomataSimulation> graphBox;

    String alreadySimulatedWord = EMPTY_STRING;
    boolean alreadyStarted = false;

    private final ApplicationEventPublisher applicationEventPublisher;
    private final GraphAdministrator graphAdministrator;

    @FXML
    void initialize(){
        word.textProperty().addListener(_ -> alreadySimulatedWord = EMPTY_STRING);
        forwardBtn.setOnAction(_ -> simulateForward());
        backwardBtn.setOnAction(_ -> simulateBack());
        fastBackwordBtn.setOnAction(_ -> simulateFirst());
        fastForwardBtn.setOnAction(_ -> simulateLast());
        startBtn.setOnAction(_ -> simulateFirst());
        stopBtn.setOnAction(_ -> simulateStop());

        graphBox.getItems().setAll(AutomataSimulation.values());
        graphBox.setValue(AutomataSimulation.NEA);
        graphBox.setOnAction(_ -> graphAdministrator.changeSelectedGraph(graphBox.getValue()));
    }

    public void setComboBox(AutomataSimulation simulation){
        graphBox.setValue(simulation);
        graphAdministrator.changeSelectedGraph(graphBox.getValue());
    }

    private void simulateForward() {
        if (alreadyStarted) { calculateNextInput(); }
        if (!alreadyStarted) { alreadyStarted = true; }
        applicationEventPublisher.publishEvent(new SimulationEvent(this.alreadySimulatedWord));
    }

    private void calculateNextInput(){
        List<String> allWordsInTextField = List.of(word.getText().trim().split(SPLIT_BY_REGEX));
        List<String> alreadyCalculatedWords = alreadySimulatedWord == null || alreadySimulatedWord.isBlank() ?
                List.of() :
                List.of(alreadySimulatedWord.trim().split(SPLIT_BY_REGEX));

        if (allWordsInTextField.isEmpty() ||
                Objects.equals(alreadySimulatedWord, word.getText()) ||
                alreadyCalculatedWords.size() >= allWordsInTextField.size()) return;

        String nextWord = allWordsInTextField.get(alreadyCalculatedWords.size());
        alreadySimulatedWord = alreadySimulatedWord + nextWord + SPACE;
    }

    private void simulateBack() {
        if (!alreadyStarted) return;

        calculateLastInput();
        applicationEventPublisher.publishEvent(new SimulationEvent(this.alreadySimulatedWord));
    }

    private void calculateLastInput(){
        List<String> alreadyCalculatedWords = alreadySimulatedWord == null || alreadySimulatedWord.isBlank() ?
                List.of() :
                List.of(alreadySimulatedWord.trim().split(SPLIT_BY_REGEX));

        alreadySimulatedWord = (alreadyCalculatedWords.size() <= 1) ?
                EMPTY_STRING :
                String.join(SPACE, alreadyCalculatedWords.subList(0, alreadyCalculatedWords.size() - 1)) + SPACE;
    }

    private void simulateFirst() {
        alreadySimulatedWord = EMPTY_STRING;
        applicationEventPublisher.publishEvent(new SimulationEvent(this.alreadySimulatedWord));
    }

    private void simulateLast() {
        alreadyStarted = true;
        alreadySimulatedWord = word.getText();
        applicationEventPublisher.publishEvent(new SimulationEvent(this.alreadySimulatedWord));
    }

    private void simulateStop() {
        alreadyStarted = false;
        alreadySimulatedWord = EMPTY_STRING;
        applicationEventPublisher.publishEvent(new GraphChangedEvent());
    }
}
