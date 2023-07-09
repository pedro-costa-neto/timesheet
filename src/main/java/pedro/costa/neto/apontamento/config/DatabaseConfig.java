package pedro.costa.neto.apontamento.config;

import java.sql.SQLException;

import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;

public class DatabaseConfig {
	public static ConnectionSource getConnection() throws SQLException {
		String directory = System.getProperty("user.dir");
        String databaseUrl = "jdbc:sqlite:" + directory + "\\database.db";
		return new JdbcConnectionSource(databaseUrl);
	}
}
