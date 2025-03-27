package BankingManagementSystem;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class User {
    private Connection conn;
    private Scanner sc;

    public User(Connection conn, Scanner sc){
        this.conn = conn;
        this.sc = sc;
    }

    public void register(){
        System.out.println();
        System.out.println("Enter full name:");
        String full_name = sc.nextLine();
        System.out.println("Enter Email Address:");
        String email = sc.nextLine();
        System.out.println("Enter Password:");
        String password = sc.nextLine();

        if(user_exists(email)){
            System.out.println("User Already Exists for this Email Address.");
            return;
        }

        String query = "INSERT INTO user(full_name,email,password) VALUES(?,?,?)";

        try{
            PreparedStatement preparedStatement = conn.prepareStatement(query);
            preparedStatement.setString(1,full_name);
            preparedStatement.setString(2,email);
            preparedStatement.setString(3,password);
            int affectedRows = preparedStatement.executeUpdate();
            if(affectedRows>0){
                System.out.println("Registration successful!!!");
            }
            else{
                System.out.println("Registration failed!!!!!");
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public String login(){

        System.out.println();
        System.out.println("Enter Email Address:");
        String email = sc.nextLine();
        System.out.println("Enter Password:");
        String password = sc.nextLine();
        String login_query = "SELECT * FROM user WHERE email = ? AND password = ?";

        try{
            PreparedStatement preparedStatement = conn.prepareStatement(login_query);
            preparedStatement.setString(1,email);
            preparedStatement.setString(2,password);
            ResultSet rs = preparedStatement.executeQuery();
            if(rs.next()){
                return email;
            }
            else{
                return null;
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

            return null;




    }

    public boolean user_exists( String email){
        String query = "SELECT * FROM user WHERE email=?";

        try{
            PreparedStatement preparedStatement = conn.prepareStatement(query);
            preparedStatement.setString(1,email);
            ResultSet rs = preparedStatement.executeQuery();
            if(rs.next()){
                return true;
            }
            else{
                return  false;
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return false;
    }






}

