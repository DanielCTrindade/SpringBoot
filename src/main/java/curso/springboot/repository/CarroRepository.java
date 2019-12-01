package curso.springboot.repository;

import java.util.List;


import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import curso.springboot.model.Carro;

@Repository
@Transactional
public interface CarroRepository extends CrudRepository<Carro, Long>{
	
	@Query("select c from Carro c where c.nome like %?1%")
	List<Carro> findCarroByName(String nome);
	
}
