package lib.database;

import java.sql.Connection;
import java.sql.SQLException;

import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;

public class MySqlConnection {
    public MysqlDataSource dataSource;
    private String url;
    private String username;
    private String password;


    public MySqlConnection(MysqlDataSource dataSource) throws ClassNotFoundException, IllegalAccessException, InstantiationException, SQLException {

        readConfigurationFile();

        this.dataSource = dataSource;
        this.dataSource.setServerName("localhost");
        this.dataSource.setPortNumber(3306);
        this.dataSource.setDatabaseName("StockBuddy");
        this.dataSource.setUser("root");
        this.dataSource.setPassword("aztewe");
    }


    private void readConfigurationFile() {
        // TODO cant find resource file
//
//        FileInputStream input = null;
//        try {
//            input = new FileInputStream("config.properties");
//            // load a properties file
//            config.load(input);
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        this.url = "jdbc:mysql://localhost:3306/Crystalpure";
        this.username = "root";
        this.password = "aztewe";
    }

    public Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }
}


