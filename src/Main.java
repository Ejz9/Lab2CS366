import java.sql.*;
import java.util.Scanner;

public class Main {
    public static Connection DatabaseConnection() {
        Connection con = null;
        try {
            Class.forName("org.mariadb.jdbc.Driver");

            // Create a connection URL
            String dbUser = System.getenv().get("DB_USER"); // Replace with your username
            String dbPassword = System.getenv().get("DB_PASSWORD"); // Replace with your password
            String dbURL = System.getenv().get("DB_URL"); // Replace with your database URL
            System.out.println("databaseURL: " + dbURL);

            // Get a connection
            con = DriverManager.getConnection(dbURL, dbUser, dbPassword);
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
            //getTotalFaculty(con);
            //getStudentInfo(con);
            getClassInformation(con);
            //runSQLQuery(statement, "select * from Student");
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void runSQLQuery(Statement statement, String query) throws SQLException {
        ResultSet resultSet = statement.executeQuery(query);
        ResultSetMetaData metaData = resultSet.getMetaData();
        int columns = metaData.getColumnCount();

        for (int i = 1; i <= columns; i++) {
            System.out.print(metaData.getColumnName(i) + "\t");
        }

        System.out.println();

        while (resultSet.next()) {
            for (int i = 1; i <= columns; i++) {
                System.out.print(resultSet.getObject(i) + "\t");
            }
            System.out.println();
        }
    }

    private static void getTotalFaculty(Connection con) throws SQLException {
            CallableStatement myCallStmt = con.prepareCall("call getTotalFaculty(?)");
            myCallStmt.registerOutParameter(1,Types.BIGINT);
            myCallStmt.execute();
            int total = myCallStmt.getInt(1);
            System.out.println("The total Faculty = "+ total);
    }

    private static void getStudentInfo(Connection con) throws SQLException {
        CallableStatement myCallStmt = con.prepareCall("{call getStudentInfo(?)}");

        Scanner input = new Scanner(System.in);
        System.out.print("Please enter a studentID: ");
        int studentID = input.nextInt();
        myCallStmt.setInt(1, studentID);

        boolean hasResults = myCallStmt.execute();

        if (hasResults) {
            try (ResultSet rs = myCallStmt.getResultSet()) {
                while (rs.next()) {
                    int snum = rs.getInt("snum");
                    String sname = rs.getString("sname");
                    String major = rs.getString("major");
                    String level = rs.getString("level");
                    int age = rs.getInt("age");

                    System.out.println("Student Number: " + snum);
                    System.out.println("Name: " + sname);
                    System.out.println("Major: " + major);
                    System.out.println("Level: " + level);
                    System.out.println("Age: " + age);
                }
            }
        } else {
            System.out.println("No data found for student ID: " + studentID);
        }
        input.close();
        myCallStmt.close();
    }

    private static void getClassInformation(Connection con) throws SQLException {
        CallableStatement myCallStmt = con.prepareCall("{call getClassInfo(?)}");
        myCallStmt.setString(1,"");
        myCallStmt.registerOutParameter(1,Types.VARCHAR);
        myCallStmt.execute();
        String classList = myCallStmt.getString(1);
        System.out.println("Class Information:\n" + classList);
        myCallStmt.close();
    }
}