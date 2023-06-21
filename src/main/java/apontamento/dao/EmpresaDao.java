package apontamento.dao;

import java.sql.SQLException;

import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import apontamento.model.Empresa;
import apontamento.model.Projeto;

public class EmpresaDao extends BaseDaoImpl<Empresa, Long>{

	public EmpresaDao(ConnectionSource connectionSource) throws SQLException {
		super(connectionSource, Empresa.class);
		TableUtils.createTableIfNotExists(connectionSource, Projeto.class);
	}

}
