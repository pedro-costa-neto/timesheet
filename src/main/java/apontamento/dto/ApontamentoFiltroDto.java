package apontamento.dto;

import java.io.Serializable;
import java.util.Date;

public class ApontamentoFiltroDto implements Serializable {
	private static final long serialVersionUID = 1L;

	private String empresaCodigo;
	private Date dataInicial;
	private Date dataFinal;
	
	public String getEmpresaCodigo() {
		return empresaCodigo;
	}
	
	public void setEmpresaCodigo(String empresaCodigo) {
		this.empresaCodigo = empresaCodigo;
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
}
