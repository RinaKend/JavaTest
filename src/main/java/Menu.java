import com.github.javafaker.Faker;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.util.Random;
import java.util.Scanner;
import static java.lang.System.exit;
import java.sql.*;

public class Menu {
    static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    static final String DB_URL = "jdbc:mysql://localhost:3306/testbd";
    public static String CreateName() {
        Faker faker = new Faker();
        String fName = faker.name().firstName();
        String lName = faker.name().lastName();
        String FI=lName+" "+ fName;
        return FI;
    }

    public  static String CreateDate(Random random) {
        int minDay = (int) LocalDate.of(1910, 1, 1).toEpochDay();
        int maxDay = (int) LocalDate.of(2020, 1, 1).toEpochDay();
        long randomDay = minDay + random.nextInt(maxDay - minDay);
        LocalDate randomBirthDate = LocalDate.ofEpochDay(randomDay);
        String d=randomBirthDate.toString();
        return d;
    }

    public static String CreateSex(Random random) {
        char[] chars2 = "FM".toCharArray();
        String s=chars2[random.nextInt(chars2.length)]+"";
        return s;
    }

    //  Database credentials
    static final String USER = "root";
    static final String PASS = "1";

    public static void printMenu(String[] options){
        for (String option : options){
            System.out.println(option);
        }
        System.out.print("Choose your option : ");
    }
    Connection conn = null;
    Statement stmt = null;

    public static void main(String[] args) {
        String[] options = {"Option 1",
                "Option 2",
                "Option 3",
                "Option 4",
                "Option 5",
                "Option 6",
                "Exit 7",
        };

        Scanner scanner = new Scanner(System.in);
        int option = 0;
        while (option!=7){
            printMenu(options);
            try {
                option = scanner.nextInt();
                switch (option){
                    case 1: option1(); break;
                    case 2: option2(); break;
                    case 3: option3(); break;
                    case 4: option4(); break;
                    case 5: option5(); break;
                    case 6: option6(); break;

                    case 7: exit(0);
                }
            }
            catch (Exception ex){
                System.out.println("Please enter an integer value between 1 and " + options.length);
                scanner.next();
            }
        }
    }
    // Options
    private static void option1() {
        Connection conn = null;
        Statement stmt = null;
        try{

            Class.forName("com.mysql.jdbc.Driver");
            System.out.println("Connecting to a selected database...");
            conn = DriverManager.getConnection(DB_URL, USER, PASS);
            System.out.println("Connected database successfully...");
            System.out.println("Creating table in given database...");
            stmt = conn.createStatement();

            String sql = "CREATE TABLE Users " +
                    "(id INTEGER not NULL AUTO_INCREMENT, " +
                    " FIO VARCHAR(255), " +
                    " BirthDate date, " +
                    " Sex VARCHAR(1), " +
                    " PRIMARY KEY ( id ))";

            stmt.executeUpdate(sql);
            System.out.println("Table was created.");

        }catch(SQLException se){
            //Handle errors for JDBC
            se.printStackTrace();
        }catch(Exception e){
            //Handle errors for Class.forName
            e.printStackTrace();
        }finally{
            //finally block used to close resources
            try{
                if(stmt!=null)
                    conn.close();
            }catch(SQLException se){
            }// do nothing
            try{
                if(conn!=null)
                    conn.close();
            }catch(SQLException se){
                se.printStackTrace();
            }
        }
        System.out.println("Done!");
    }

    private static void option2() {
        Connection conn = null;

        try{

            Class.forName("com.mysql.jdbc.Driver");
            System.out.println("Connecting to a selected database...");
            conn = DriverManager.getConnection(DB_URL, USER, PASS);
            System.out.println("Enter the user");
            InputStreamReader reader = new InputStreamReader(System.in);
            BufferedReader br = new BufferedReader(reader);
            String x = br.readLine();
            String[] user = x.split(",");

            String query = "INSERT Users(FIO, BirthDate, SEX) VALUES (?, ?, ?)";
            PreparedStatement stmt =conn.prepareStatement(query);
            stmt.setString(1, user[0]);
            stmt.setString(2,user[1]);
            stmt.setString(3,user[2]);
            stmt.execute();
            System.out.println("Row was inserted.");

        }catch(SQLException se){
            //Handle errors for JDBC
            se.printStackTrace();
        }catch(Exception e){
            //Handle errors for Class.forName
            e.printStackTrace();
        }finally{
            //finally block used to close resources
            try{
                conn.close();
            }catch(SQLException se){
            }// do nothing
            try{
                if(conn!=null)
                    conn.close();
            }catch(SQLException se){
                se.printStackTrace();
            }
        }
        System.out.println("Done!");
    }
    private static void option3() {
        Connection conn = null;
        Statement stmt = null;
        try{

            Class.forName("com.mysql.jdbc.Driver");
            System.out.println("Connecting to a selected database...");
            conn = DriverManager.getConnection(DB_URL, USER, PASS);
            System.out.println("Connected database successfully...");
            stmt = conn.createStatement();
            ResultSet resultSet = stmt.executeQuery("SELECT FIO, BirthDate,Sex,timestampdiff( YEAR, BirthDate, now() ) FROM Users group by FIO, BirthDate order by FIO");
            while(resultSet.next()){

                String FIO = resultSet.getString(1);
                Date BirthDate = resultSet.getDate(2);
                String Sex = resultSet.getString(3);
                int Age=resultSet.getInt(4);
                System.out.printf("%s %s %s %d\n", FIO, BirthDate, Sex, Age);
            }


        }catch(SQLException se){
            //Handle errors for JDBC
            se.printStackTrace();
        }catch(Exception e){
            //Handle errors for Class.forName
            e.printStackTrace();
        }finally{
            //finally block used to close resources
            try{
                if(stmt!=null)
                    conn.close();
            }catch(SQLException se){
            }// do nothing
            try{
                if(conn!=null)
                    conn.close();
            }catch(SQLException se){
                se.printStackTrace();
            }
        }
        System.out.println("Done!");
    }
    private static void option4() {
        Connection conn = null;
        Random random = new Random();
        try{

            Class.forName("com.mysql.jdbc.Driver");
            System.out.println("Connecting to a selected database...");
            conn = DriverManager.getConnection(DB_URL, USER, PASS);
            System.out.println("Connected database successfully...");

            for(int i = 0; i < 1000000; i++) {
                String query = "INSERT Users(FIO, BirthDate, SEX) VALUES (?, ?, ?)";
                PreparedStatement stmt =conn.prepareStatement(query);
                stmt.setString(1,CreateName());
                stmt.setString(2,CreateDate(random));
                stmt.setString(3,CreateSex(random));
                }

            for(int i = 0; i < 100; i++) {
                String query = "INSERT Users(FIO, BirthDate, SEX) VALUES (?, ?, ?)";
                PreparedStatement stmt =conn.prepareStatement(query);
                stmt.setString(1,"F"+CreateName());
                stmt.setString(2,CreateDate(random));
                stmt.setString(3,"M");
                //int rows = stmt.executeUpdate("INSERT Users(FIO, BirthDate, SEX) VALUES ("?");
                stmt.execute();

            }

        }catch(SQLException se){
            //Handle errors for JDBC
            se.printStackTrace();
        }catch(Exception e){
            //Handle errors for Class.forName
            e.printStackTrace();
        }finally{
            //finally block used to close resources
            try{
                conn.close();
            }catch(SQLException se){
            }// do nothing
            try{
                if(conn!=null)
                    conn.close();
            }catch(SQLException se){
                se.printStackTrace();
            }
        }
        System.out.println("Done!");
    }
    private static void option5() {
        Connection conn = null;
        Statement stmt = null;
        try{

            Class.forName("com.mysql.jdbc.Driver");
            System.out.println("Connecting to a selected database...");
            conn = DriverManager.getConnection(DB_URL, USER, PASS);
            System.out.println("Connected database successfully...");
            stmt = conn.createStatement();

            long start = System.nanoTime();
            ResultSet resultSet = stmt.executeQuery("SELECT FIO, BirthDate,Sex FROM Users where Sex='M'and FIO like 'F%'");
            long end = System.nanoTime();
            long execution = end - start;
            System.out.println("Execution time: " + execution + " nanoseconds");

            while(resultSet.next()){

                String FIO = resultSet.getString(1);
                Date BirthDate = resultSet.getDate(2);
                String Sex = resultSet.getString(3);

                //System.out.printf("%s %s %s\n", FIO, BirthDate, Sex);
            }


        }catch(SQLException se){
            //Handle errors for JDBC
            se.printStackTrace();
        }catch(Exception e){
            //Handle errors for Class.forName
            e.printStackTrace();
        }finally{
            //finally block used to close resources
            try{
                if(stmt!=null)
                    conn.close();
            }catch(SQLException se){
            }// do nothing
            try{
                if(conn!=null)
                    conn.close();
            }catch(SQLException se){
                se.printStackTrace();
            }
        }
        System.out.println("Done!");
    }
    private static void option6() {
        System.out.println("Switching on column indexes helps to filter rows faster. Dimensions were on 200000 rows only. Result without index = 71*10^6 ns, with index=63*10^6 ns.");
    }
}
