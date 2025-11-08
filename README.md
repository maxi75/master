# Lernsoftware Automatenkonstruktion

## Übersicht
Dieses Repository enthält eine Applikation, um Schülern und Studierenden die theoretische Informatik, genauer die 
Automatentheorie beizubringen.
Die Applikation wurde im Rahmen einer Masterarbeit entwickelt und wird zukünftig nicht weiter gewartet.
Die Applikation kann (als Ganzes oder in Auszügen) von jedem unter Einhaltung der angegebenen Lizenzbedingungen 
heruntergeladen, überarbeitet und verbreitet werden.

Barrierefreiheit wird aktuell noch nicht unterstützt!

## Inhalt
1. **Theorie:** Die Grundlagen der Automatentheorie werden in mehreren Kapiteln separiert vermittelt. Darunter fällt 
die Einführung von Grammatiken, formalen Sprachen, DEAs und NEAs, Abschlusseigenschaften und der Automatenkonstruktion.
2. **Übungsaufgaben:** Die Applikation wurde mit diversen Übungsaufgaben zu den verschiedenen Kapiteln versehen, 
welche aufsteigend im Schwierigkeitsgrad gestaltet wurden. Die Übungsaufgaben wurden weitestgehend praxisorientiert 
gestaltet.
3. **Gamification:** Um die Motivation zum Lernen zu erhöhen wurden Mittel aus dem Bereich Gamification eingesetzt. 
Darunter zählt die Vergabe von Batches nach einer bestimmten Lerndauer oder dem Erreichen einer gewissen Punktzahl. 
Punkte können durch die korrekte Beantwortung von Übungsaufgaben erzielt werden und mit einer gewissen Mindestpunktzahl 
lassen sich neben Batches auch zusätzliche Übungsaufgaben freischalten.
4. **Simulation:** Eine Simulationsumgebung erlaubt das Anlegen von NEAs, sowie deren Umwandlung in DEAs. Zustände, 
Transitionen und generell Automatendefinitionen können so übersichtlich visuell dargestellt werden. Die 
Simulationsumgebung unterstützt auch die Eingabe mehrerer Zeichen und mittels verschiedener Media buttons kann geprüft 
werden, ob ein akzeptierender Endzustand mit der Eingabe erreicht wird.

## Voraussetzungen
1. **Entwicklung:** Für die Entwicklung wird eine lauffähige IDE, Java 25, sowie eine funktionierende Maven-Version 
vorausgesetzt.
2. **Bisherige Entwicklung:** Die bisherige Entwicklung wurde mit Java 25, der IntelliJ IDEA 2025.2.2 (Ultimate Edition) 
und Maven 3.9.11 auf einem Windows 11 PC durchgeführt.

3. **Betrieb:** Der Betrieb und die Installation der Applikation verlangen ca. 100 MB Speicherplatz und bis zu 500 MB 
Arbeitsspeicher. Weitere Hardware sollte ansatzweise aktuell sein, es bestehen jedoch keine spezifischen Anforderungen. 
Für die Ausführung wird Java 25 benötigt.

## Funktionalität - Fachlich
Die Applikation unterteilt sich in drei Bereiche. Auf der linken Seite das Menü, Mittig der Container in welchem die 
Theory gerendert wird und rechts die Simulator ansicht.

Das Menü lässt sich über ein Burger-Icon öffnen und mittels eines X-Icons schließen.
Über das Menü sind die bisher gesammelten Batches, die Theorie, Übungsaufgaben sowie Bonusaufgaben erreichbar.

Die Theorie wird immer an der gleichen Stelle gerendert und neu geladene Kapitel ersetzen zuvor angezeigte.
Die Theorie ist in mehrere Haupt- und Unterkapitel unterteilt, welche aufeinander aufbauend gestaltet wurden.

Die Simulationsansicht unterteilt sich vertikal in drei Segmente.
Unten befindet sich ein ein- und ausklappbares Konfigurationsfenster, in welchem Zustände hinzugefügt, Start- und 
Endzustände festgelegt und Transitionen spezifiziert werden können. Mittig ist ein großes, leeres Feld, in welchem der 
erstellte Automat gerendert wird. Oben in der Ansicht ist ein Overlay eingefügt worden, mit welchem sich Simulationen 
steuern lassen. Es lassen sich einzelne Eingabezeichen, Tokens oder eine Menge an Eingabezeichen, bzw. Tokens eingeben. 
Mit den Knöpfen Start, Stop, Vor- und Zurück lässt sich Schritt für Schritt oder direkt nachverfolgen, welche Zustände 
eines Automaten durchlaufen werden und ob ein Automat für einen Eingabestring in einem akzeptierenden Endzustand 
gelangt oder nicht. Die Simulation funktioniert sowohl in der DEA- als auch in der NEA-Ansicht.

Mit Strg + Mausrad lässt sich ein gerenderter Graph in der Simulationsansicht vergrößern und verkleinern.
Mit Text im Theorieteil funktioniert dies, Stand heute, noch nicht!

## Funktionalität - Technisch
Die Applikation ist technisch an der Clean Architecture orientiert und setzt auf bekannte Praktiken aus dem Bereich 
Clean Code. Auf Kommentare wird weitestgehend verzichtet, stattdessen wurde versucht, Methoden, Klassen und Variablen 
kurz zu halten, bzw. passend zu benennen.

Um darzustellenden Content von Code zu separieren, wurde ein eigenes Rendering gebaut, welches Dateien im JSON-Format 
entgegen nimmt und den Inhalt im Theorie-Container visualisiert. Dabei ist es unabhängig, ob Übungsaufgaben oder 
theoretische Inhalte visualisiert werden sollen. Ziel dieser Trennung ist die einfachere Erweiterung oder Änderung 
theoretischer Inhalte.

Die parsende Java-Klasse verdeutlicht, welche Möglichkeiten in dem übergebenen JSON-Format geboten werden:
- **Parsende Java-Klasse**:
```java
@Data
public class TheoryPageData {
    private String mainHeading;
    private List<Section> content;

    @Data
    public static class Section {
        private String heading;
        private String text;
        private String image;
        private String imageAltText;
        private String imageSubText;
        private List<Exercise> exercises;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Exercise {
        private ExerciseType kind;
        private String question;
        private List<Integer> correctAnswers;
        private List<String> answers;
        private List<String> nodes;
        private String startingNode;
        private List<String> endingNodes;
        private List<String> transitions;
    }
}
```

Jedem Kapitel können eine Hauptüberschrift, sowie mehrere Sektionen zuteilwerden.
Eine Sektion wiederum erlaubt das Einfügen einer eigenen Überschrift, erklärendem Text, Bildern oder Übungsaufgaben.
Sollten Bilder nicht geladen werden, kann ein alternativer Text vergeben werden und auch eine Bildunterschrift ist 
möglich. Keine der Optionen ist zwingend erforderlich. Soll beispielsweise ein Text erzeugt werden, welcher über eine 
Hauptüberschrift, eine Sektionsüberschrift, und zwei Absätze verfügt, können mainHeading ausgefüllt und zwei Sektionen 
angelegt werden. In der ersten Sektion befindet sich die Überschrift und der erste Absatz, während in Sektion zwei der 
zweite Absatz enthalten ist. Durch die vordefinierten Formatierungen und den Variablen Einsatz der Sektionen ist eine 
relativ freie und gleichzeitig einfache, optisch gleichbleibende Gestaltung des Inhalts möglich.

Die Übungsaufgaben müssen mit einem speziellen Enum, dem ExerciseType (kind) angegeben sein. Aktuell stehen zur Auswahl:
- **Parsende Java-Klasse**:
```java
public enum ExerciseType {
    MULTIPLE_CHOICE,
    WORD,
    DEA_NEA, NEA_DEA,
    SOLUTION
}
```

In Zusammenhang mit Multiple-Choice-Fragen müssen der String für die Fragestellung, der Index der korrekten Antwort 
(Achtung, es fängt bei 0 an!) sowie die möglichen Antworten angegeben werden.

Die Fragestellung WORD erstellt eine Aufgabe, bei der eine Automatendefinition gegeben ist und ein von dem Automaten 
akzeptiertes Eingabewort in ein Textfeld eingegeben werden muss. Hierfür müssen die Informationen bzgl. des 
Aufgabentyps, der verfügbaren Zustände des Automaten, des Start- und der Endzustände sowie der Transitionen angegeben 
werden.

DEA_NEA und NEA_DEA erstellen anhand der gleichen übergebenen Informationen wie bei dem Aufgabentyp WORD eine 
Automatendefinition in der Fragestellung und fordern den Lernenden dazu auf, einen entsprechenden DEA, bzw. NEA daraus
in der Simulationsansicht zu konstruieren. Die Umwandlung von NEA zu DEA ist möglich und sinnvoll, die Aufgabenstellung
zur Umwandlung von DEA zu NEA wird ebenfalls unterstützt, dürfte in der Praxis jedoch relativ sinnfrei sein, da jeder 
DEA ein Spezialfall eines NEAs ist.

Aufgaben des Typs SOLUTION erlauben eine individuelle Fragestellung, deren Ergebnis ein Automat ist, welcher über die
Felder der Zustände, des Start- und der Endzustände sowie der Transitionen angegeben wird.

Beispiele für die Aufgabentypen Multiple-Choice, NEA_DEA und Solution:
- **Beispiel MULTIPLE_CHOICE**:
```json
{
  "kind": "MULTIPLE_CHOICE",
  "question": "Ein Modell erlaubt die Eingabe von 1€, 2€ und 5€. Kann ein gültiger Endzustand irgendwie erreicht werden, wenn ein Nutzer 10€ eingibt?",
  "correctAnswers": [1],
  "answers": ["Ja", "Nein"]
}
```

- **Beispiel NEA_DEA**:
```json
{
  "kind": "NEA_DEA",
  "nodes": ["a", "b", "c", "d", "e"],
  "startingNode": "a",
  "endingNodes": ["b", "c"],
  "transitions": [
    "a --x--> b",
    "a --y--> d",
    "b --x--> c",
    "c --y--> e",
    "d --x--> e",
    "e --y--> b",
    "e --y--> c"
  ]
}
```

- **Beispiel SOLUTION**:
```json
        {
  "kind": "SOLUTION",
  "question": "Erstelle im Simulator einen NEA mit den drei Zuständen z0, z1 und z2. z0 ist ein Startzustand und z2 ein Endzustand. Von z0 führt die Eingabe a zu z1 und die Eingabe b führt von z1 zu z2. Mit einer Eingabe c ist eine Transition von z2 nach z1 möglich. Zusätzlich kann eine c-Eingabe auch von z2 nach z2 führen. Lasse dir im Simulator den passenden DEA zu dem NEA anzeigen.",
  "nodes": ["z0", "z1", "z2"],
  "startingNode": "z0",
  "endingNodes": ["z2"],
  "transitions": [
    "z0 --a--> z1",
    "z1 --b--> z2",
    "z2 --c--> z1",
    "z2 --c--> z2"
  ]
}
```

Das Hinzufügen eines gänzlich neuen Kapitels (egal ob gefüllt mit theoretischem Inhalt oder Übungsaufgaben) bedarf
einer Anlage einer passenden JSON-Datei im resource-Ordner unter `theory.json`. In der fxml-Datei MenuOverlay, welche
sich im Ordner `resources/ui/fxml/menu/` befindet, muss an der gewünschten Stelle ein Eintrag vorgenommen
werden:

- **Beispielhafter Menüeintrag**:
```xml
<Label styleClass="subchapter, secondRow">1.1 Begriffsdefinitionen</Label>
<Button styleClass="navButtons, thirdRow" onAction="#loadContent">
    <userData>chapterOne/alphabet.json</userData>
    1.1.1 Alphabet, formale Sprachen und Grammatiken
</Button>
```

Soll kein neues Unterkapitel eröffnet werden, kann auf das vorangestellte Label verzichtet werden.
Die CSS-Klasse thirdRow im Beispiel muss durch secondRow ersetzt oder ganz entfernt werden, je nachdem an welcher 
Positon im Menü der Eintrag gerendert werden soll.

Für die Speicherung persistenter Daten, wie die gesammelten Punkte oder die in der Applikation zugebrachte Zeit kommt
das von Spring Boot mitgelieferte Preference-System zum Einsatz. Die in der Applikation verbrachte Zeit wird mit dem
Neustart, bzw. dem Beenden der Applikation sowie im laufenden Betrieb alle 5 Sekunden aktualisiert.

## Bekannte Einschränkungen:
1. **Simulation Eingabe** Bei der Nutzung der Simulation sind viele Eingaben möglich, mit ein paar kleinen 
Einschränkungen. Als Elemente eines Alphabets können entweder einzelne Zeichen, aber auch ganze Tokens verwendet 
werden. Essenziell dabei ist, dass weder Zustände noch Eingabezeichen/Tokens nur aus Leerzeichen bestehen dürfen. 
Bei Eingabezeichen/Tokens besteht darüber hinaus auch die Einschränkung, dass generell keine Leerzeichen erlaubt sind.
2. **Simulation DEA<->NEA** Es wird empfohlen, Start- und Endzustände zu setzen. Ohne diese funktioniert die 
Aktualisierung in der DEA-Ansicht nicht ordnungsgemäß!
3. **Simulation DEA<->NEA** Bei der Verwendung der DEA-Ansicht verlieren die Zustände ihre Namen. Beim Zurückschalten 
in die NEA-Ansicht sind die Zustandsnamen wieder vorhanden. Dies hat technische Hintergründe, da der verwendeten 
Bibliothek keine Namen für Zustände übergeben werden können und bei der Potenzmengenkonstruktion keine Namen ohne 
eigene Abbildung der Logik mit den resultierenden Zuständen gematcht werden können.

## Wartung und Weiterentwicklung
Bei der Weiterentwicklung der Applikation ist darauf zu achten, dass die Lizenzinformationen aktualisiert werden, 
sollten sich Versionen oder Bibliotheken ändern. Lizenzinformationen können mittels `mvn license:add-third-party` 
generiert werden und befinden sich in der `target\generated-sources\license\THIRD-PARTY.txt`-Datei.

Eine neue .jar-Datei kann mit dem Befehl`mvn -DskipTests clean package` erzeugt werden und befindet sich nach
erfolgreicher Generierung im `target`-Ordner.

## Starten der Applikation
Für das Starten der .jar-Datei muss entweder eine CMD geöffnet und zu dem Speicherort navigiert oder alternativ direkt
am Speicherort eine CMD gestartet werden.

Innerhalb der CMD lässt sich die Java-Applikation mittels `java -jar {NAME}.jar` starten.
Alternativ ist auch `java -jar {PFAD}\{NAME}.jar` möglich.

## License
Dieses Projekt nutzt die **Apache License Version 2.0**.
Diese wird in der `LICENSE`-Datei näher erläutert.

Lizenzen transitiver Abhängigkeiten werden in der `NOTICE.txt`-Datei unter Nennung von Name, Artefakt, Version und
Lizenz erwähnt. Einen Einblick in die unterschiedlichen Lizenzen gibt der `licenses`-Ordner.
