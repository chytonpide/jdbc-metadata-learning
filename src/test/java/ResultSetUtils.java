import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

public class ResultSetUtils {
    public static void print(ResultSet rs) throws SQLException {
        System.out.println("-------result set--------");
        ResultSetMetaData resultSetMetaData = rs.getMetaData();
        int columnsNumber = resultSetMetaData.getColumnCount();
        while(rs.next()) {
            for (int i = 1; i <= columnsNumber; i++) {
                if (i > 1)
                    System.out.print(",  ");

                String columnValue = rs.getString(i);
                System.out.print(resultSetMetaData.getColumnName(i) + ": " + columnValue);
            }
            System.out.println();
        }
        System.out.println("-------------------------");
    }
}
