package net.codejava;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
// import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

// public interface ProductRepository extends JpaRepository<Product, Long> {

public interface ProductRepository extends PagingAndSortingRepository<Product, Long> {

	@Query("SELECT p FROM Product p WHERE " + "CONCAT(p.id, ' ', p.name, ' ' , p.brand, ' ' , p.madein, ' ' , p.price)"
			+ "LIKE %?1%")
	public Page<Product> findAll(String keyword, Pageable pageable);

}
