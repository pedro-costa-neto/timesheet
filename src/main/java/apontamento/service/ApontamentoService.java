package apontamento.service;

import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

import org.apache.log4j.Logger;

import com.google.common.base.Strings;
import com.j256.ormlite.dao.Dao.CreateOrUpdateStatus;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.support.ConnectionSource;

import apontamento.dao.ApontamentoDao;
import apontamento.dto.ApontamentoFiltroDto;
import apontamento.model.Apontamento;
import apontamento.model.Empresa;
import apontamento.model.Projeto;

public class ApontamentoService {
	private final static Logger log = Logger.getLogger(ApontamentoService.class);
	
	private ApontamentoDao apontamentoRep;
	private EmpresaService empresaService;
	private ProjetoService projetoService;
	
	public ApontamentoService(ConnectionSource connectionSource) throws SQLException {
		this.apontamentoRep = new ApontamentoDao(connectionSource);
		this.empresaService = new EmpresaService(connectionSource);
		this.projetoService = new ProjetoService(connectionSource);
		
	}
	
	public ApontamentoService(ApontamentoDao apontamentoRep, EmpresaService empresaService, ProjetoService projetoService) throws SQLException {
		this.apontamentoRep = apontamentoRep;
		this.empresaService = empresaService;
		this.projetoService = projetoService;
	}
	
	public List<Apontamento> listarPorFiltro(ApontamentoFiltroDto filtro) {
		try {
			QueryBuilder<Apontamento, Long> queryBuilder = apontamentoRep.queryBuilder();
			queryBuilder.where().between("dataInicial", filtro.getDataInicial(), filtro.getDataFinal());
			queryBuilder.orderBy("dataInicial", false);
			
			QueryBuilder<Empresa, Long> queryBuilderEmpresa = empresaService.queryBuilder();
			
			if(!Strings.isNullOrEmpty(filtro.getEmpresaCodigo())) {
				queryBuilderEmpresa.where().eq("codigo", filtro.getEmpresaCodigo());
			}
			
			queryBuilder.join(queryBuilderEmpresa);
			
			
			List<Apontamento> apontamentos = apontamentoRep.query(queryBuilder.prepare());
			carregarDadosEmpresaProjeto(apontamentos);
			return apontamentos;
		}
		catch (SQLException e) {
			log.error("Falha ao listar apontamentos");
			e.printStackTrace();
		}
		
		return Collections.emptyList();
	}

	public int salvar(Apontamento apontamento) throws SQLException {
		CreateOrUpdateStatus status = apontamentoRep.createOrUpdate(apontamento);
		return status.getNumLinesChanged();
	}

	public Apontamento buscarPorId(Long idApontamento) {
		try {
			Apontamento apontamento = apontamentoRep.queryForId(idApontamento);
			carregarDadosEmpresaProjeto(apontamento);
			return apontamento;
		}
		catch (SQLException e) {
			log.error("Falha buscar o apontamento pelo ID " + idApontamento);
			e.printStackTrace();
		}
		
		return null;
	}
	
	private void carregarDadosEmpresaProjeto(Apontamento apontamento) throws SQLException {
		Long empresaId = apontamento.getEmpresa().getId();
		Empresa empresa = empresaService.buscarPorId(empresaId);
		apontamento.setEmpresa(empresa);
		
		Long projetoId = apontamento.getProjeto().getId();
		Projeto projeto = projetoService.buscarPorId(projetoId);
		apontamento.setProjeto(projeto);
	}
	
	private void carregarDadosEmpresaProjeto(List<Apontamento> apontamentos) throws SQLException {
		for(Apontamento apontamento : apontamentos) {
			carregarDadosEmpresaProjeto(apontamento);
		}
	}
}
