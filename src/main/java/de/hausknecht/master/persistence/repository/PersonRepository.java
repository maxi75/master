package de.hausknecht.master.persistence.repository;

import de.hausknecht.master.persistence.entity.Person;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PersonRepository extends JpaRepository<Person, Long> {}