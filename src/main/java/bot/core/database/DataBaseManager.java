package bot.core.database;

import bot.core.Logger;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import javax.sql.DataSource;

public abstract class DataBaseManager implements Logger
{
    private static String DB_CONNECTION_USER_NAME;
    private static String DB_CONNECTION_PASSWORD;
    private static String DB_CONNECTION_PORT;
    private static String DB_CONNECTION_HOST;
    private static String DB_CONNECTION_DB_NAME;
    private static final byte DB_MAX_POOL_SIZE = 10;
    private static final boolean DB_AUTO_COMMIT = true;
    private static final boolean DB_CACHE_PREP_STMTS = true;
    private static final short DB_PREP_STMT_CACHE_SIZE = 20;
    private static final short DB_PREP_STMT_CACHE_SQL_LIMIT = 2048;

    private static DataSource datasource;

    public static DataSource getDataSource ()
    {
        if (datasource == null)
        {
            HikariConfig config = new HikariConfig ();
            config.setJdbcUrl ("jdbc:postgresql://" + DB_CONNECTION_HOST + ":" + DB_CONNECTION_PORT + "/" + DB_CONNECTION_DB_NAME);
            config.setUsername (DB_CONNECTION_USER_NAME);
            config.setPassword (DB_CONNECTION_PASSWORD);
            config.setMaximumPoolSize (DB_MAX_POOL_SIZE);
            config.setAutoCommit (DB_AUTO_COMMIT);
            config.addDataSourceProperty ("cachePrepStmts", DB_CACHE_PREP_STMTS);
            config.addDataSourceProperty ("prepStmtCacheSize", DB_PREP_STMT_CACHE_SIZE);
            config.addDataSourceProperty ("prepStmtCacheSqlLimit", DB_PREP_STMT_CACHE_SQL_LIMIT);
            datasource = new HikariDataSource (config);
        }
        return datasource;
    }

    public static void setDbConnectionUserName (String dbConnectionUserName)
    {
        DB_CONNECTION_USER_NAME = dbConnectionUserName;
    }

    public static void setDbConnectionPassword (String dbConnectionPassword)
    {
        DB_CONNECTION_PASSWORD = dbConnectionPassword;
    }

    public static void setDbConnectionPort (String dbConnectionPort)
    {
        DB_CONNECTION_PORT = dbConnectionPort;
    }

    public static void setDbConnectionHost (String dbConnectionHost)
    {
        DB_CONNECTION_HOST = dbConnectionHost;
    }

    public static void setDbConnectionDbName (String dbConnectionDbName)
    {
        DB_CONNECTION_DB_NAME = dbConnectionDbName;
    }
}