package curso.springboot.model;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Entity
public class Carro implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@NotEmpty(message="Nome não pode ser vazio!")
	@NotNull(message="Nome não pode ser nulo!")
	private String nome;
	
	
	
	@NotEmpty(message="Marca não pode ser vazio!")
	@NotNull(message="Marca não pode ser nulo!")
	private String marca;
	
	@Min(value= 1000, message="Valor inválido!O valor minimo deve ser R$1000.")
	private Double valor;
	
	@Min(value=1960, message="Ano inválido!O ano deve ser superior a 1960.")
	private String ano;
	
	@OneToMany(mappedBy = "carro", orphanRemoval = false, cascade = CascadeType.ALL)
	private List<Despesa> despesas;
	
	
	
	public List<Despesa> getDespesas() {
		return despesas;
	}
	public void setDespesas(List<Despesa> despesas) {
		this.despesas = despesas;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public String getMarca() {
		return marca;
	}
	public void setMarca(String marca) {
		this.marca = marca;
	}
	public Double getValor() {
		return valor;
	}
	public void setValor(Double valor) {
		this.valor = valor;
	}
	public String getAno() {
		return ano;
	}
	public void setAno(String ano) {
		this.ano = ano;
	}
	
	

	

}
