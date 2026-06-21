package repository;

import entity.Fruit_Entity;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

import static io.quarkus.hibernate.orm.panache.PanacheEntityBase.list;

//Handles all direct database operations and queries using Quarkus Panache.
@ApplicationScoped
public class Fruit_Repository implements PanacheRepository<Fruit_Entity> {


}
