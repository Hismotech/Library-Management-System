package database;



import java.sql.ResultSet;

import javax.swing.*;
import java.sql.*;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.PieChart;
import listbook.BookListController.Book;
import listmember.ListMemberController.Member;


public class DatabaseHandler {
    private static DatabaseHandler handler = null;
    private static final String url = "jdbc:mysql://127.0.0.1:3306/library?useTimezone=true&serverTimezone=UTC";
    private static Connection connect = null;
    private static Statement statement = null;


     private DatabaseHandler(){
         createConnection();
         createBookTable();
         createMemberTable();
         createIssueTable();
     }

     public static DatabaseHandler getInstance(){
         if (handler==null){
             handler = new DatabaseHandler();
         }
         return handler;
     }


    void createConnection(){
        try {
            connect = DriverManager.getConnection(url,"root","bamidele1995");
            Class.forName("org.mysql.JDBC");
        }
        catch (SQLException se) {
            System.err.println(se.getMessage());
        }
        catch (ClassNotFoundException cnfe) {
            System.err.println(cnfe.getMessage());
        }
    }
    void createBookTable(){
       String tableName="Book";
        try {
            DatabaseMetaData dbm = connect.getMetaData();
            ResultSet dbTable = dbm.getTables(null, null, tableName.toUpperCase(),null);
            statement = connect.createStatement();
            if (dbTable.next()){
                System.out.println(tableName +" table Exist already,Please Continue ");
            }
            else {
            statement.execute("create table " + tableName +"(" + "id varchar(200) primary key,\n"
                               + "title varchar(200),\n" + "author varchar(200),\n" + "publisher varchar(100),\n"
                          + "isAvailable boolean default true" + ")");}
            if (true){
                System.out.println("Table Created Successfully in the Database");
            }
            else{System.out.println("Error in Creating Table");}
        }
            catch (SQLException se) {
                System.err.println(se.getMessage());
        }
    }

    void createMemberTable() {
         String memberTable = "Member";
         try{
            DatabaseMetaData dbm = connect.getMetaData();
            ResultSet mbTable = dbm.getTables(null,null,memberTable.toUpperCase(),null);
             if (mbTable.next()){
                 System.out.println(memberTable +" table Exist already,Please Continue ");
             }
             else {
                 statement.execute("create table " + memberTable +"(" + "students varchar(200),\n"
                          + "matric varchar(200) primary key,\n" + "department varchar(200),\n" +
                         "year varchar(100)\n" + ")" );}
             if (true){
                 System.out.println("Table Created Successfully in the Database");
             }
             else{System.out.println("Error in Creating Table");}
         }
         catch (SQLException se){
             System.err.println(se.getMessage());
         }
     }

    void createIssueTable() {
        String issueTable = "Issue";
        try{
            DatabaseMetaData dbm = connect.getMetaData();
            ResultSet mbTable = dbm.getTables(null,null,issueTable.toUpperCase(),null);
            if (mbTable.next()){
                System.out.println(issueTable +" table Exist already,Please Continue ");
            }
            else {
                statement.execute("create table " + issueTable +"(" + " id varchar(200) primary key,\n"
                        + " matric varchar(200),\n" + "IssueTime timestamp default " +
                        "CURRENT_TIMESTAMP ,\n" +
                        "Renewal integer default 0,\n" + "Foreign key (id) references Book(id),\n"
                                 +"Foreign key (matric) references Member(matric)"+ ")");
            }
            if (true){
                System.out.println("Table Created Successfully in the Database");
            }
            else{System.out.println("Error in Creating Table");}
        }
        catch (SQLException se){
            System.err.println(se.getMessage());
        }
    }

    public ResultSet executeQuery(String query){
         ResultSet result = null;
        try {
            statement = connect.createStatement();
            result = statement.executeQuery(query);
        }
        catch (SQLException se) {
          System.err.println(se.getMessage());
        }
        return result;
    }

    public boolean executeAction(String action){
        try {
            statement = connect.createStatement();
            statement.execute(action);
            return true;
        }
        catch (SQLException se) {
            JOptionPane.showMessageDialog(null,se.getMessage(),"Error Occured",JOptionPane.ERROR_MESSAGE);
            System.err.println(se.getLocalizedMessage());
            return false;
        }
    }

    public boolean deleteBook(Book book){
         String delete = "delete from book where id = ?";
        try {
            PreparedStatement pstmt = connect.prepareStatement(delete);
            pstmt.setString(1,book.getId());
            int result = pstmt.executeUpdate();
            if(result == 1){
              return  true;
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
     return false;
    }
    public boolean deleteMember(Member member){
        String delete = "delete from member where matric = ?";
        try {
            PreparedStatement pstmt = connect.prepareStatement(delete);
            pstmt.setString(1,member.getMatric());
            int result = pstmt.executeUpdate();
            if(result == 1){
                return  true;
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean isBookAlreadyIssued(Book book){
         String checkBook = "select count(*) from issue where id = ?";//count * counts the row that has that id
        try {
            PreparedStatement pstmt = connect.prepareStatement(checkBook);
            pstmt.setString(1,book.getId());
            ResultSet resultSet =  pstmt.executeQuery();
            if (resultSet. next()){
                int count = resultSet.getInt(1);
                System.out.println(count);
               return (count>0);
            }
        }
        catch (SQLException se) {
            System.err.println(se.getMessage());
        }
        return false;
     }

     public boolean hasMemberAlreadyBorrowedABook(Member member){
         String checkMember = "select count(*) from issue where matric = ?";
         try{
             PreparedStatement pstmt = connect.prepareStatement(checkMember);
             pstmt.setString(1,member.getMatric());
             ResultSet result = pstmt.executeQuery();
             if(result.next()){
                 int count = result.getInt(1);
                 System.out.println(count);
                 return (count>0);
             }
         }
         catch(SQLException se){
         System.err.println(se.getMessage());
         }
         return false;
     }

     public boolean updateBook(Book book){
         String update = "Update book set title = ?, author = ?, publisher = ? where id = ?";
         try {
             PreparedStatement pstmt = connect.prepareStatement(update);
             pstmt.setString(1, book.getTitle());
             pstmt.setString(2,book.getAuthor());
             pstmt.setString(3,book.getPublisher());
             pstmt.setString(4,book.getId());
             int result = pstmt.executeUpdate();
             return(result>0);
         }
         catch (SQLException se) {
             System.err.println(se.getMessage());;
         }
         return false;
     }

     public boolean updateMember (Member member){
         try {
             String            update = "Update Member set students =?, department = ?, year = ? where matric = ?  ";
             PreparedStatement pstmt  = connect.prepareStatement(update);
             pstmt.setString(1,member.getStudents());
             pstmt.setString(2,member.getDepartment());
             pstmt.setString(3,member.getYear());
             pstmt.setString(4,member.getMatric());
             int result = pstmt.executeUpdate();
                 return(result>0);
         }
         catch (SQLException se) {
             System.err.println(se.getMessage());;
         }
         return false;
     }
     public static void main(String[] args){

     }
     public ObservableList<PieChart.Data> getBookStatistics(){
         ObservableList<PieChart.Data>data = FXCollections.observableArrayList();
         String query1 = "select count(*) from Book";
         String query2 = "select count(*) from Issue";
         ResultSet result = executeQuery(query1);
         try {
             if (result.next()) {
                 int count = result.getInt(1);
                 data.add(new PieChart.Data("Total Books (" +count+ ")",count));
             }
         }
         catch (SQLException se) {
             System.err.println(se.getMessage());
         }

          result = executeQuery(query2);
         try {
             if (result.next()) {
                 int count = result.getInt(1);
                 data.add(new PieChart.Data("Issued Books (" +count+ ")",count));
             }
         }
         catch (SQLException se) {
             System.err.println(se.getMessage());
         }
   return data;
     }

    public ObservableList<PieChart.Data> getMemberStatistics(){
        ObservableList<PieChart.Data>data = FXCollections.observableArrayList();
        String query1 = "select count(*) from Member";
        String query2 = "select count(distinct matric) from Issue";
        ResultSet result = executeQuery(query1);
        try {
            if (result.next()) {
                int count = result.getInt(1);
                data.add(new PieChart.Data("Total Students (" +count+ ")",count));
            }
        }
        catch (SQLException se) {
            System.err.println(se.getMessage());
        }

        result = executeQuery(query2);
        try {
            if (result.next()) {
                int count = result.getInt(1);
                data.add(new PieChart.Data("Student With Books (" +count+ ")",count));
            }
        }
        catch (SQLException se) {
            System.err.println(se.getMessage());
        }
        return data;
    }
}
