package curso.springboot.model;


import java.sql.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Entity
public class Despesa {
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;
	
	@NotEmpty(message="Nome não pode ser vazio!")
	@NotNull(message="Nome não pode ser nulo!")
	private String nome;
	
	
	
	
	private Date data;
	
	
	
	@Min(value = 1, message="Valor não pode ser nulo ou inferior a 0.")
	private double valor;
	
	@OneToMany(mappedBy = "carro")
	private List<Despesa> despesas;
	
	@SuppressWarnings("deprecation")
	@org.hibernate.annotations.ForeignKey(name="carro_id")
	@ManyToOne
	private Carro carro;
	

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

	public Date getData() {
		return data;
	}

	public void setData(Date data) {
		this.data = data;
	}

	public double getValor() {
		return valor;
	}

	public void setValor(double valor) {
		this.valor = valor;
	}

	public Carro getCarro() {
		return carro;
	}

	public void setCarro(Carro carro) {
		this.carro = carro;
	}
	
	
}
