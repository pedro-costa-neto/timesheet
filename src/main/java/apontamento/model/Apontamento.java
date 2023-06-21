package apontamento.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.google.common.base.Strings;
import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;

import apontamento.util.FuncoesUtil;

@Entity(name = "apontamento")
public class Apontamento implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@ManyToOne
	@JoinColumn(name = "id_empresa", nullable = false)
	private Empresa empresa;
	
	@ManyToOne
	@JoinColumn(name = "id_projeto", nullable = false)
	private Projeto projeto;
	
	@DatabaseField(dataType = DataType.DATE_STRING)
	@Column(name = "data_inicial", nullable = false)
	private Date dataInicial;
	
	@DatabaseField(dataType = DataType.DATE_STRING)
	@Column(name = "data_final", nullable = false)
	private Date dataFinal;
	
	@Column(name = "descricao")
	@DatabaseField(dataType = DataType.LONG_STRING)
	private String descricao;
	
	@Column(name = "total_minutos")
	private Long totalMinutos;
	
	public Apontamento() {
		// ORMLite precisa de um construtor sem argumento
	}
	
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	public Empresa getEmpresa() {
		return empresa;
	}
	
	public void setEmpresa(Empresa empresa) {
		this.empresa = empresa;
	}
	
	public Projeto getProjeto() {
		return projeto;
	}
	
	public void setProjeto(Projeto projeto) {
		this.projeto = projeto;
	}
	
	public Date getDataInicial() {
		return dataInicial;
	}
	
	public void setDataInicial(Date dataInicial) {
		this.dataInicial = dataInicial;
	}
	
	public Date getDataFinal() {
		return dataFinal;
	}
	
	public void setDataFinal(Date dataFinal) {
		this.dataFinal = dataFinal;
	}
	
	public String getDescricao() {
		if(descricao == null) return "";
		return descricao;
	}
	
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	
	public Long getTotalMinutos() {
		return totalMinutos;
	}

	public void setTotalMinutos(Long totalMinutos) {
		this.totalMinutos = totalMinutos;
	}
	
	public String getHoraFormatada() {
		return FuncoesUtil.qtdHorasMinutosFormatado(totalMinutos);
	}

	public String getCodigoFantasiaEmpresa() {
		if(empresa == null) return "";
		
		if(Strings.isNullOrEmpty(empresa.getFantasia())) {
			return empresa.getCodigo() + " - " + empresa.getRazao();
		}
		
		return empresa.getCodigo() + " - " + empresa.getFantasia();	
	}

	public String getCodigoDescricaoProjeto() {
		if(projeto == null) return "";
		return projeto.getCodigo() + " - " + projeto.getDescricao();	
	}
}
