package curso.springboot.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import curso.springboot.model.Despesa;

@Repository
@Transactional
public interface DespesaRepository extends CrudRepository<Despesa, Long> {
	
	@Query("select d from Despesa d where d.carro.id=?1")
	public List<Despesa> getDespesas(Long carroid);
	
	@Query("select sum(valor) from Despesa where carro.id=?1")
	public List<Double> getValorTotal(Long carroid);
	
	@Query("select d from Despesa d where d.nome like %?1%")
	List<Despesa> findDespesaByName(String nome);
}

