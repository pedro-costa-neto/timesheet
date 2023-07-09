package pedro.costa.neto.apontamento.service;

import java.sql.SQLException;
import java.util.List;

import org.apache.log4j.Logger;

import com.j256.ormlite.dao.Dao.CreateOrUpdateStatus;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.support.ConnectionSource;

import pedro.costa.neto.apontamento.dao.ProjetoDao;
import pedro.costa.neto.apontamento.model.Empresa;
import pedro.costa.neto.apontamento.model.Projeto;

public class ProjetoService {
	
	private final static Logger log = Logger.getLogger(ProjetoService.class);
	
	private ProjetoDao projetoRep;
	private EmpresaService empresaService;

	public ProjetoService(ConnectionSource connectionSource) throws SQLException {
		this.projetoRep = new ProjetoDao(connectionSource);
		this.empresaService = new EmpresaService(connectionSource);
	}
	
	public ProjetoService(ProjetoDao projetoRep, EmpresaService empresaService) throws SQLException {
		this.projetoRep = projetoRep;
		this.empresaService = empresaService;
	}

	public Projeto buscarPorId(Long id) throws SQLException {
		return projetoRep.queryForId(id);
	}

	public Projeto buscarPorCodigo(String codigo) {
		try {
			QueryBuilder<Projeto, Long> queryBuilder = projetoRep.queryBuilder();
			queryBuilder.where().eq("codigo", codigo);
			Projeto projeto = projetoRep.queryForFirst(queryBuilder.prepare());
			carregarDados(projeto);
			return projeto;
		}
		catch (SQLException e) {
			log.error("Falha ao buscar projeto por codigo " + codigo, e);
			e.printStackTrace();
		}
		
		return null;
	}

	public Projeto buscarPorEmpresaECodigo(String codigoEmpresa, String codigoProjeto) {
		try {
			QueryBuilder<Projeto, Long> queryBuilder = projetoRep.queryBuilder();
			queryBuilder.where().eq("codigo", codigoProjeto);
			
			QueryBuilder<Empresa, Long> queryBuilderEmpresa = empresaService.queryBuilder();
			queryBuilderEmpresa.where().eq("codigo", codigoEmpresa);
			
			queryBuilder.join(queryBuilderEmpresa);
			
			return projetoRep.queryForFirst(queryBuilder.prepare());
		}
		catch (SQLException e) {
			log.error("Falha ao buscar projeto por codigo da empresa " + codigoEmpresa + " e codigo do projeto " + codigoProjeto, e);
			e.printStackTrace();
		}
		
		return null;
	}

	public List<Projeto> listar() throws SQLException {
		return projetoRep.queryForAll();
	}

	public Integer salvar(Projeto projeto) {
		try {
			CreateOrUpdateStatus status = projetoRep.createOrUpdate(projeto);
			return status.getNumLinesChanged();
		}
		catch (SQLException e) {
			log.error("Falha ao salvar projeto", e);
			e.printStackTrace();
			
			if(e.getCause() != null) {
				Throwable throwable = e.getCause();
				if(throwable.getMessage() != null && throwable.getMessage().contains("UNIQUE constraint failed: projeto.codigo")) {
					// Já existe um registro cadastrado com o código projeto.codigo
				}
			}
			else {
				log.error("Falha não mapeada ao salvar projeto", e);
			}
		}
		
		return null;
	}

	private void carregarDados(Projeto projeto) {
		if(projeto == null) return;
		
		Long idEmpresa = projeto.getEmpresa().getId();
		Empresa empresa = empresaService.buscarPorId(idEmpresa);
		projeto.setEmpresa(empresa);
	}
}
