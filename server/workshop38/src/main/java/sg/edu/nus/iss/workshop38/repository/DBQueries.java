package sg.edu.nus.iss.workshop38.repository;

public class DBQueries {

    public static final String SQL_VERIFY_USER = """
            select * from users where username = ? and user_password = ?;
            """;
}
