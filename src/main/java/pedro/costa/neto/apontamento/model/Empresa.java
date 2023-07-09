package pedro.costa.neto.apontamento.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.google.common.base.Strings;

@Entity(name = "empresa")
public class Empresa implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@Column(name = "codigo", unique = true, nullable = false)
	private String codigo;
	
	@Column(name = "razao", nullable = false)
	private String razao;
	
	@Column(name = "fantasia")
	private String fantasia;
	
	@Column(name = "cnpj", nullable = false, length = 19)
	private String cnpj;
	
	public Empresa() {
		// ORMLite precisa de um construtor sem argumento
	}

	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	public String getCodigo() {
		if(codigo == null) return "";
		return codigo.toUpperCase();
	}
	
	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}
	
	public String getRazao() {
		if(razao == null) return "";
		return razao.toUpperCase();
	}
	
	public void setRazao(String razao) {
		this.razao = razao;
	}
	
	public String getFantasia() {
		if(fantasia == null) return "";
		return fantasia.toUpperCase();
	}
	
	public void setFantasia(String fantasia) {
		this.fantasia = fantasia;
	}
	
	public String getCnpj() {
		if(cnpj == null) return "";
		return cnpj;
	}
	
	public void setCnpj(String cnpj) {
		this.cnpj = cnpj.toUpperCase();
	}

	public String getDescricao() {
		return Strings.isNullOrEmpty(fantasia) ? getRazao() : getFantasia();	
	}
	
	@Override
	public String toString() {
		if(Strings.isNullOrEmpty(fantasia)) {
			return getCodigo() + " - " + getRazao();
		}
		
		return getCodigo() + " - " + getFantasia();	
	}
}
