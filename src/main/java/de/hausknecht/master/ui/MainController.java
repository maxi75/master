package de.hausknecht.master.ui;

import de.hausknecht.master.persistence.entity.Person;
import de.hausknecht.master.persistence.repository.PersonRepository;
import javafx.event.ActionEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MainController {
    private final PersonRepository repo;
    public void onSave(ActionEvent e) {
        repo.save(new Person(null, "Ada", "Lovelace"));
    }
}
