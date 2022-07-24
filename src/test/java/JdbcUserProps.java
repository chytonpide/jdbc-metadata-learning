import java.util.Properties;

public class JdbcUserProps {

    public static Properties definesWith(String username, String password) {
        Properties props = new Properties();
        props.setProperty("user", username);
        props.setProperty("password", password);
        return props;
    }

}
