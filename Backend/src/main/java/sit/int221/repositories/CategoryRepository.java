package sit.int221.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import sit.int221.entities.Category;

public interface CategoryRepository extends JpaRepository<Category, Integer> {
}
