import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class MysqlDatabaseMetaDataTest {

    private final static String USERNAME = "root";
    private final static String PASSWORD = "a123456";
    private final static String URL = "jdbc:mysql://localhost:3307/";

    private final static String DB_NAME_1 = "test_db_1";
    private final static String DB_NAME_2 = "test_db_2";
    private final static String TABLE_NAME = "tbl_1";
    private final static Properties USER_PROPS = JdbcUserProps.definesWith(USERNAME, PASSWORD);

    @BeforeEach
    public void setup() throws SQLException {
        Connection conn = DriverManager.getConnection(URL, USER_PROPS);
        conn.createStatement().execute("DROP DATABASE IF EXISTS " + DB_NAME_1);
        conn.createStatement().execute("DROP DATABASE IF EXISTS " + DB_NAME_2);

        conn.createStatement().execute("CREATE DATABASE " + DB_NAME_1);
        conn.createStatement().execute("CREATE DATABASE " + DB_NAME_2);
        conn.close();
    }

    @Test
    public void getCatalogs_returnsAllDBsOnMysqlInstance() throws SQLException {
        //given
        Connection connToDb1 = DriverManager.getConnection(URL + DB_NAME_1, USER_PROPS);
        connToDb1.createStatement().execute("CREATE TABLE " + TABLE_NAME + "(id BIGINT PRIMARY KEY)");
        connToDb1.close();

        connToDb1 = DriverManager.getConnection(URL + DB_NAME_1, USER_PROPS);
        DatabaseMetaData db1MetaData = connToDb1.getMetaData();

        //when
        ResultSet rs1 = db1MetaData.getCatalogs();
        //ResultSetUtils.print(rs1);

        List<String> catalogNames = new ArrayList<>();

        while(rs1.next()) {
            catalogNames.add(rs1.getString(1));
        }
        connToDb1.close();

        //then
        assertTrue(catalogNames.contains(DB_NAME_1));
        assertTrue(catalogNames.contains(DB_NAME_2));
    }


    @Test
    public void getTables_withNoCatalog_returnsTablesOnAllDBs() throws SQLException {
        //given
        Connection connToDb1 = DriverManager.getConnection(URL + DB_NAME_1, USER_PROPS);
        connToDb1.createStatement().execute("CREATE TABLE " + TABLE_NAME + "(id INTEGER PRIMARY KEY)");
        connToDb1.close();

        Connection connToDb2 = DriverManager.getConnection(URL + DB_NAME_2, USER_PROPS);
        connToDb2.createStatement().execute("CREATE TABLE " + TABLE_NAME + "(id INTEGER PRIMARY KEY)");
        connToDb2.close();

        //when
        connToDb1 = DriverManager.getConnection(URL + DB_NAME_1, USER_PROPS);
        DatabaseMetaData db1MetaData = connToDb1.getMetaData();
        ResultSet rs = connToDb1.getMetaData().getTables(null, null, TABLE_NAME, null);

        int tableCount = 0;
        while(rs.next()) {
            tableCount ++;
        }
        connToDb1.close();

        //then
        assertEquals(2, tableCount);
    }

    @Test
    public void getTables_withCatalog_returnsTablesOnCurrentlyConnectedDB() throws SQLException {
        //given
        Connection connToDb1 = DriverManager.getConnection(URL + DB_NAME_1, USER_PROPS);
        connToDb1.createStatement().execute("CREATE TABLE " + TABLE_NAME + "(id INTEGER PRIMARY KEY)");
        connToDb1.close();

        Connection connToDb2 = DriverManager.getConnection(URL + DB_NAME_2, USER_PROPS);
        connToDb2.createStatement().execute("CREATE TABLE " + TABLE_NAME + "(id INTEGER PRIMARY KEY)");
        connToDb2.close();

        //when
        connToDb1 = DriverManager.getConnection(URL + DB_NAME_1, USER_PROPS);
        DatabaseMetaData db1MetaData = connToDb1.getMetaData();
        ResultSet rs1 = db1MetaData.getTables(DB_NAME_1, null, TABLE_NAME, null);

        int tableCount1 = 0;
        while(rs1.next()) {
            tableCount1 ++;
        }
        connToDb1.close();


        connToDb2 = DriverManager.getConnection(URL + DB_NAME_2, USER_PROPS);
        DatabaseMetaData db2MetaData = connToDb2.getMetaData();
        ResultSet rs2 = db2MetaData.getTables(DB_NAME_2, null, TABLE_NAME, null);

        int tableCount2 = 0;
        while(rs2.next()) {
            tableCount2 ++;
        }
        connToDb2.close();


        //then
        assertEquals(1, tableCount1 );
        assertEquals(1, tableCount2 );
    }
}
