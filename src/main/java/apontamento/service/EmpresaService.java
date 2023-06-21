package apontamento.service;

import java.sql.SQLException;
import java.util.List;

import org.apache.log4j.Logger;

import com.j256.ormlite.dao.Dao.CreateOrUpdateStatus;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.support.ConnectionSource;

import apontamento.dao.EmpresaDao;
import apontamento.model.Empresa;

public class EmpresaService {
	
	private final static Logger log = Logger.getLogger(EmpresaService.class);
	
	private EmpresaDao empresaRep;

	public EmpresaService(ConnectionSource connectionSource) throws SQLException {
		this.empresaRep = new EmpresaDao(connectionSource);
	}
	
	public EmpresaService(EmpresaDao empresaRep) throws SQLException {
		this.empresaRep = empresaRep;
	}

	public Empresa buscarPorId(Long id) {
		try {
			return empresaRep.queryForId(id);
		}
		catch (SQLException e) {
			log.error("Falha ao buscar empresa por id " + id, e);
			e.printStackTrace();
		}
		
		return null;
	}

	public Empresa buscarPorCodigo(String codigo) {
		try {
			QueryBuilder<Empresa, Long> queryBuilder = empresaRep.queryBuilder();
			queryBuilder.where().eq("codigo", codigo);
			return empresaRep.queryForFirst(queryBuilder.prepare());
		}
		catch (SQLException e) {
			log.error("Falha ao buscar empresa por codigo " + codigo, e);
			e.printStackTrace();
		}
		
		return null;
	}

	public List<Empresa> listar() throws SQLException {
		return empresaRep.queryForAll();
	}

	public Integer salvar(Empresa empresa) {
		try {
			CreateOrUpdateStatus status = empresaRep.createOrUpdate(empresa);
			return status.getNumLinesChanged();
		}
		catch (SQLException e) {
			log.error("Falha ao salvar empresa", e);
			e.printStackTrace();
			
			if(e.getCause() != null) {
				Throwable throwable = e.getCause();
				if(throwable.getMessage() != null && throwable.getMessage().contains("UNIQUE constraint failed: empresa.codigo")) {
					// Já existe um registro cadastrado com o código empresa.codigo
				}
			}
			else {
				log.error("Falha não mapeada ao salvar empresa", e);
			}
		}
		
		return null;
	}
	
	public QueryBuilder<Empresa, Long> queryBuilder() {
		return empresaRep.queryBuilder();
	}
}
