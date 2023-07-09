package pedro.costa.neto.apontamento.dto;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

import pedro.costa.neto.apontamento.model.Apontamento;
import pedro.costa.neto.apontamento.util.FuncoesUtil;

public class ApontamentoDto implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private Long id;
	private String empresaDescricao;
	private String projetoDescricao;
	private String data;
	private String horaInicial;
	private String horaFinal;
	private String descricao;
	private String horaFormatada; 
	private Long qtdMinutos;
	
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	public String getEmpresaDescricao() {
		return empresaDescricao;
	}
	
	public void setEmpresaDescricao(String empresaDescricao) {
		this.empresaDescricao = empresaDescricao;
	}
	
	public String getProjetoDescricao() {
		return projetoDescricao;
	}
	
	public void setProjetoDescricao(String projetoDescricao) {
		this.projetoDescricao = projetoDescricao;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public String getHoraInicial() {
		return horaInicial;
	}

	public void setHoraInicial(String horaInicial) {
		this.horaInicial = horaInicial;
	}

	public String getHoraFinal() {
		return horaFinal;
	}

	public void setHoraFinal(String horaFinal) {
		this.horaFinal = horaFinal;
	}

	public Long getQtdMinutos() {
		return qtdMinutos;
	}

	public void setQtdMinutos(Long qtdMinutos) {
		this.qtdMinutos = qtdMinutos;
	}

	public String getHoraFormatada() {
		return horaFormatada;
	}

	public void setHoraFormatada(String horaFormatada) {
		this.horaFormatada = horaFormatada;
	}

	public String getDescricao() {
		return descricao;
	}
	
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	
	public static ApontamentoDto construir(Apontamento obj) {
		var objDto = new ApontamentoDto();
		
		objDto.setId(obj.getId());
		objDto.setDescricao(obj.getDescricao());
		objDto.setQtdMinutos(obj.getTotalMinutos());
		objDto.setHoraFormatada(obj.getHoraFormatada());
		
		if(obj.getEmpresa() != null) {
			objDto.setEmpresaDescricao(obj.getCodigoFantasiaEmpresa());
		}
		
		if(obj.getProjeto() != null) {
			objDto.setProjetoDescricao(obj.getCodigoDescricaoProjeto());
		}
		
		String data = FuncoesUtil.formatarData(obj.getDataInicial(), "dd/MM/yyyy");
		objDto.setData(data);
		
		String horaInicial = FuncoesUtil.formatarData(obj.getDataInicial(), "HH:mm");
		objDto.setHoraInicial(horaInicial);
		
		String horaFinal = FuncoesUtil.formatarData(obj.getDataFinal(), "HH:mm");
		objDto.setHoraFinal(horaFinal);
		
		return objDto;
	}
	
	public static List<ApontamentoDto> construir(List<Apontamento> apontamentos) {
		if(apontamentos == null || apontamentos.isEmpty()) return Collections.emptyList();
		return apontamentos.stream().map(apontamento -> {
			return construir(apontamento);
		}).toList();
	}
}
