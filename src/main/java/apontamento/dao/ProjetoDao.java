package apontamento.dao;

import java.sql.SQLException;

import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import apontamento.model.Empresa;
import apontamento.model.Projeto;

public class ProjetoDao extends BaseDaoImpl<Projeto, Long>{

	public ProjetoDao(ConnectionSource connectionSource) throws SQLException {
		super(connectionSource, Projeto.class);
		TableUtils.createTableIfNotExists(connectionSource, Empresa.class);
	}

}
