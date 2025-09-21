package de.hausknecht.master.frameworksanddrivers.persistence.repository;

import de.hausknecht.master.frameworksanddrivers.persistence.entity.Person;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PersonRepository extends JpaRepository<Person, Long> {}