import java.sql.*;
import java.lang.String;

/*
 * connector class to your MySQL database
 */

public class Trans{
  static String url = "jdbc:mysql://localhost/******";  //replace stars with database
  static String user = "";  //enter in username	
  static String password = "";  //enter in password

  public static boolean isNumeric(String s) {
      return java.util.regex.Pattern.matches("\\d+", s);
  }
  //********************************************************************8 */
  
  public static String[] listTables(){  // I believe it should return a string[] though  //t
    try{
      String str = "show tables";
      Class.forName( "com.mysql.jdbc.Driver" );

      Connection cx = DriverManager.getConnection( url, user, password );
      Statement st = cx.createStatement();
      ResultSet rs = st.executeQuery(str);

      //ResultSetMetaData metaData = rs.getMetaData();  //this is if we want the meta data of the result set
      System.out.println();
      System.out.println();
      //System.out.println("Table: " + rs);
      int size = 0;
      String list = "";
      while(rs.next()){
        size++;
        String obj = "Table: " + (rs.getObject(1)+"").toUpperCase() + "";  //why is this not printing the table
        list += rs.getObject(1)+" " ;
        System.out.println(obj);
      }
      System.out.println("Size: " + size);

      String[] arr = list.split(" ");  //get the array needed to be returned
      for (int i = 0; i < arr.length; i++) {
        System.out.println(arr[i] + ": " + i);
      }

      return arr;
    }
    catch( Exception x ){
      System.out.println("table reading interrupted by " + x);
      System.exit(1);  //program should break if it cannot read the tables properly
      return null;
    }
  }

  public static boolean exist(String table){  //check to see if a table exists
        boolean flag = false;
        try {
          String str = "show tables;";
          Class.forName( "com.mysql.jdbc.Driver" ); 		
      
          Connection cx = DriverManager.getConnection( url, user, password );
          Statement st = cx.createStatement();
          ResultSet rs = st.executeQuery(str);

          ResultSetMetaData metaData = rs.getMetaData();  //what is the points of creating this variable if it is not used?
  
          table = table.toUpperCase();

          while (rs.next()){
            if(table.equals((rs.getObject(1)+"").toUpperCase()))  //if we find table in ResultSet return True;
              flag = true;
          }
        }
        catch( Exception x ) {
            System.err.println( "table existance reading interrupted by "+x );
        }
        return flag;
  } 
  
  public static String [] read(String table, String column, String condition){
        // e.g., read("abc", "qty", "order by qty") for "select qty from abc order by qty;"
        String [] results = {""};
        try {
              String str = "select "+column+" from "+table+" "+condition+";";

              Class.forName( "com.mysql.jdbc.Driver" ); 		
              Connection cx = DriverManager.getConnection( url, user, password );
              Statement st = cx.createStatement();
              ResultSet rs = st.executeQuery(str);

              ResultSetMetaData metaData = rs.getMetaData();
              int numberOfColumns = metaData.getColumnCount();
              
              results = new String [numberOfColumns];
              for(int i = 0; i<numberOfColumns; i++){
                results[i]="";
              }
              
  /*
            for(int i =1 ; i<=numberOfColumns;i++)
              results[i-1]=metaData.getColumnName(i)+"\n";
  */ 
              while (rs.next()){
                for(int i =1; i<=numberOfColumns; i++)
                    results[i-1]+=rs.getObject(i)+"\n";
                }
          }
          catch( Exception x ) {
              System.err.println( "remote reading interrupted by "+x );
          }
          return results;
  }
  public static String [][] readAll(String table){
    String[][] result;
    try {
          String str = "select * from "+table+ ";";

          Class.forName( "com.mysql.jdbc.Driver" ); 		
          Connection cx = DriverManager.getConnection( url, user, password );
          Statement st = cx.createStatement();
          ResultSet rs = st.executeQuery(str);

          ResultSetMetaData metaData = rs.getMetaData();
          int numberOfRows = getQueryRowCount(table);
          int numberOfColumns = metaData.getColumnCount();  //helps create the size of the 2d array


          
          result = new String[numberOfRows][numberOfColumns];  //How do i get rows
      
          int row = 0;
          while(rs.next()){  //every time i hits 1 update the row
            for (int i = 1; i <= numberOfColumns; i++) {
              result[row][i-1] = "" +  rs.getObject(i);
              //System.out.println("(" + row + ", " + (i - 1) + ") : " + rs.getObject(i));
            }
            row++;
          }
      }
      catch( Exception x ) {
          System.err.println( "remote reading interrupted by "+x );
          System.exit(1);
          result = null;
      }
      return result;
}

  public static boolean found(String table, String column, String condition){
        // e.g., read("abc", "qty", "order by qty") for "select qty from abc order by qty;"
        boolean flag = false;
        try {
              String str = "select "+column+" from "+table+" "+condition+";";
          Class.forName( "com.mysql.jdbc.Driver" ); 		
      
          Connection cx = DriverManager.getConnection( url, user, password );

            Statement st = cx.createStatement();

            ResultSet rs = st.executeQuery(str);

            ResultSetMetaData metaData = rs.getMetaData();
            int numberOfColumns = metaData.getColumnCount();
              
            if (rs.next()){
                if(!(((rs.getObject(1)+"").substring(0,5)).equals("Empty")))
                      flag = true;
          }
          }
          catch( Exception x ) {
              System.out.println("Found is interrupted by "+x);
          }
          return flag;
  }

  public static void create (String table, String values){
  // need to ensure not existing. 	   
  // values = "id VARCHAR(6), quiz INT, avg decimal(3,2)";

      try {
        Class.forName( "com.mysql.jdbc.Driver" ); 
        Connection cx = DriverManager.getConnection( url, user, password );
        Statement st = cx.createStatement();
  
        String sql_drop = "DROP TABLE IF EXISTS " + table;  //if the table exists already it will drop it
        st.executeUpdate( sql_drop );  //sound pretty bad to me should warn the user before dropping data from a database

        String sql_create = "CREATE TABLE " + table + "(" + values + ")";
        st.executeUpdate( sql_create );
      }
      catch( Exception x ) {
        System.err.println("table creation is interrupted by "+ x );
      }
  }

  public static void write (String table, String [] values){
  // insertion, e.g., write ("abc", { "1-56592-488-6", "Database Management System", "paper", "100" })
  //             for "insert into abc Values ('1-56592-488-6', 'Database Management System', 'paper', 100);"
      if(values.length <1) {
        System.out.println("Nothing to insert");
        return;
      }
      try {

      Class.forName( "com.mysql.jdbc.Driver" ); 		
      
      Connection cx = DriverManager.getConnection( url, user, password );

        Statement st = cx.createStatement();

      String sql_insert = "INSERT INTO " + table + " VALUES " + "("; 
        int i=0;
        for (; i< values.length-1; i++){
        if (isNumeric(values[i])) 
          sql_insert += values[i]+",";
        else 
          sql_insert += "'"+values[i]+"',";
        }
      
        if (isNumeric( values[i])) 
          sql_insert += values[i]+");";
        else 
          sql_insert += "'"+values[i]+"');";
  
      st.executeUpdate( sql_insert );
        }
      catch( Exception x ) {
        System.err.println( "Insertion is interrupted by "+x );
      }
  }

  public static int write (String table, String column, String condition){
  // update, e.g., write("abc", "qty=qty+1", "where type = 'paper'")
  // for "UPDATE abc SET qty=qty+1 WHERE type='paper';"
      int n=0;
      try {

      Class.forName( "com.mysql.jdbc.Driver" ); 		
      
      Connection cx = DriverManager.getConnection( url, user, password );


        Statement st = cx.createStatement();

        String sql_update = "UPDATE " + table + " SET "+ column + " "+condition+";";

        n = st.executeUpdate( sql_update );

      }
      catch( Exception x ) {
        System.err.println("online writing is interrupted by "+ x );
      }
      return n;
  }

  //TODO: Fix getLables incase it doesn't work and should test it
  public static String[] getColumns(String x){  //the input is going to be the table name
    try{
      String str = "describe " + x + ";";
      Class.forName( "com.mysql.jdbc.Driver" );

      Connection cx = DriverManager.getConnection( url, user, password );
      Statement st = cx.createStatement();
      ResultSet rs = st.executeQuery(str);

      //ResultSetMetaData metaData = rs.getMetaData();  //this is if we want the meta data of the result set
      
      int size = 0;
      String list = "";
      while(rs.next()){
        size++;
        String obj = "Lables: " + (rs.getObject(1)+"").toUpperCase() + "";  //why is this not printing the table
        System.out.println(obj);
        list += rs.getObject(1)+" " ;
      }
      //System.out.println("Size: " + size);

      String[] arr = list.split(" ");  //get the array needed to be returned
/*    For debugging purposes
      for (int i = 0; i < arr.length; i++) {
        System.out.println(arr[i] + ": " + i);
      }
*/
      return arr;
    }
    catch( Exception e ){
      System.out.println("table reading interrupted by " + e);
      System.exit(1);  //program should break if it cannot read the tables properly
      return null;
    }
  }

  public static int getQueryRowCount(String table){
    int count = 0;
    try{
        String str = "select * from "+table+ ";";

          Class.forName( "com.mysql.jdbc.Driver" ); 		
          Connection cx = DriverManager.getConnection( url, user, password );
          Statement st = cx.createStatement();
          ResultSet rs = st.executeQuery(str);

          while(rs.next()){
            count++;
          }
    }
    catch(Exception e){
      System.out.println("Error exception: " + e);
    }
    return count;
  }

}