import java.sql.*;

public class Main {
    public static Connection DatabaseConnection() {
        Connection con = null;
        try {
            Class.forName("org.mariadb.jdbc.Driver");

            // Create a connection URL
            String url = "jdbc:mariadb://washington.uww.edu:3306/cs366-2247_zurakowsej30"; // Replace with your database URL
            String username = "zurakowsej30"; // Replace with your username
            String password = "ez4854"; // Replace with your password
            System.out.println("databaseURL: " + url);

            // Get a connection
            con = DriverManager.getConnection(url, username, password);
            System.out.println("Connected to database");
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        return con;
    }

    public static void main(String[] args) {
        Connection con = DatabaseConnection();
        try {
            Statement statement = con.createStatement();
            //ResultSet resultSet = statement.executeQuery("call getTotalFaculty(@total)");
            String spName = "getTotalFaculty";
            CallableStatement myCallStmt = con.prepareCall("call getTotalFaculty(?)");
            myCallStmt.registerOutParameter(1,Types.BIGINT);
            myCallStmt.execute();
            int total = myCallStmt.getInt(1);
            System.out.println("The total Faculty ="+ total);
            /* ResultSetMetaData metaData = resultSet.getMetaData();
            int columns = metaData.getColumnCount();

            for (int i = 1; i <= columns; i++) {
                System.out.print(metaData.getColumnName(i) + "\t");
            }

            System.out.println();

            while (resultSet.next()) {

                for (int i = 1; i <= columns; i++) {
                    System.out.print(resultSet.getObject(i) + "\t\t");
                }
                System.out.println();
            }*/
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }
}