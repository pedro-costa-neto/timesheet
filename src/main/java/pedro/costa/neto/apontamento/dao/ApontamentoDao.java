package pedro.costa.neto.apontamento.dao;

import java.sql.SQLException;

import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import pedro.costa.neto.apontamento.model.Apontamento;

public class ApontamentoDao extends BaseDaoImpl<Apontamento, Long>{

	public ApontamentoDao(ConnectionSource connectionSource) throws SQLException {
		super(connectionSource, Apontamento.class);
		TableUtils.createTableIfNotExists(connectionSource, Apontamento.class);
	}

}
