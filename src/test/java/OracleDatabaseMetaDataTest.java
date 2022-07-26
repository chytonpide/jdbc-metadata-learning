import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.nio.charset.Charset;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;

/*
 * Please register the user in the DB before run the test.
 */
public class OracleDatabaseMetaDataTest {

    private final static String USERNAME = "c##scott";
    private final static String PASSWORD = "tiger";
    private final static String URL = "jdbc:oracle:thin:@localhost:1523:";
    private final static String DB_NAME = "ORCLCDB";

    private final static String TABLE_NAME = "TABL_1";
    private final static Properties USER_PROPS = JdbcUserProps.definesWith(USERNAME, PASSWORD);

    
    @BeforeEach
    public void setup() throws SQLException {
        try { 
            Connection conn = DriverManager.getConnection(URL + DB_NAME, USER_PROPS);
            conn.createStatement().execute("DROP TABLE " + TABLE_NAME);
            conn.close();
        } catch (SQLSyntaxErrorException e) {
            if(e.getMessage().contains("ORA-00942")) {
                //ignore the case that the table does not exist
            } else {
                throw e;
            }
        }
    } 

    @Test
    public void getCatalogs_returnsNothing() throws SQLException {
        //given
        Connection connToDb1 = DriverManager.getConnection(URL + DB_NAME , USER_PROPS);
        connToDb1.createStatement().execute("CREATE TABLE " + TABLE_NAME + "(id INTEGER PRIMARY KEY)");
        connToDb1.close();

        //when
        connToDb1 = DriverManager.getConnection(URL + DB_NAME, USER_PROPS);
        DatabaseMetaData db1MetaData = connToDb1.getMetaData();
        ResultSet rs1 = db1MetaData.getCatalogs();
        
        List<String> catalogNames = new ArrayList<>();

        while(rs1.next()) {
            catalogNames.add(rs1.getString(1));
        }
        connToDb1.close();

        //then
        assertEquals(0, catalogNames.size()); 
    }

    @Test
    public void getTables_withNoCatalog_returnsTablesOnCurrentlyConnectedDB() throws SQLException {
        //given
        Connection connToDb1 = DriverManager.getConnection(URL + DB_NAME , USER_PROPS);
        connToDb1.createStatement().execute("CREATE TABLE " + TABLE_NAME + "(id INTEGER PRIMARY KEY)");
        connToDb1.close();

        //when
        connToDb1 = DriverManager.getConnection(URL + DB_NAME , USER_PROPS);
        DatabaseMetaData db1MetaData = connToDb1.getMetaData();
        ResultSet rs = db1MetaData.getTables(null, null, TABLE_NAME, null);
        

        int tableCount = 0;
        while(rs.next()) {
            tableCount ++;
        }
        connToDb1.close();

        //then
        assertEquals(1, tableCount );
    }

    @Test
    public void getTables_withCatalog_returnsTablesOnCurrentlyConnectedDB() throws SQLException {
        //given
        Connection connToDb1 = DriverManager.getConnection(URL + DB_NAME , USER_PROPS);
        connToDb1.createStatement().execute("CREATE TABLE " + TABLE_NAME + "(id INTEGER PRIMARY KEY)");

        //when
        connToDb1 = DriverManager.getConnection(URL + DB_NAME , USER_PROPS);
        DatabaseMetaData db1MetaData = connToDb1.getMetaData();
        ResultSet rs1 = db1MetaData.getTables(DB_NAME, null, TABLE_NAME, null);

        int tableCount1 = 0;
        while(rs1.next()) {
            tableCount1 ++;
        }
        connToDb1.close();

        //then
        assertEquals(1, tableCount1 );
    }

    @Test
    public void getTables_withCatalog_catalogIsIgnored() throws SQLException {
        //given
        Connection connToDb1 = DriverManager.getConnection(URL + DB_NAME , USER_PROPS);
        connToDb1.createStatement().execute("CREATE TABLE " + TABLE_NAME + "(id INTEGER PRIMARY KEY)");
        connToDb1.close();
        String catalog = randomString();

        //when
        connToDb1 = DriverManager.getConnection(URL + DB_NAME , USER_PROPS);
        DatabaseMetaData db1MetaData = connToDb1.getMetaData();
        ResultSet rs1 = db1MetaData.getTables(catalog, null, TABLE_NAME, null);

        int tableCount1 = 0;
        while(rs1.next()) {
            tableCount1 ++;
        }
        connToDb1.close();

        //then
        assertEquals(1, tableCount1 );
    }

    private static String randomString() {
        byte[] array = new byte[8];
        new Random().nextBytes(array);
        String generatedString = new String(array, Charset.forName("UTF-8"));
        return generatedString;
    }
}
