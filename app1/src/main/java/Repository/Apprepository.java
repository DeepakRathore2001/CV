package Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import Entity.Appentity;
import jakarta.persistence.Entity;

public interface Apprepository extends JpaRepository<Appentity, Entity> {

}
