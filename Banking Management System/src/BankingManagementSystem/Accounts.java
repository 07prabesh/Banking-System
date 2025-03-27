package BankingManagementSystem;

import java.sql.*;
import java.util.Scanner;

public class Accounts {

    private Connection  conn;
    private Scanner sc;

    public Accounts(Connection conn,Scanner sc){
        this.conn= conn;
        this.sc = sc;
    }

public long open_account(String email){
        if(!account_exist(email)){
            String query = "INSERT INTO accounts WHERE(account_number,full_name,email,balance,security_pin) VALUES(?,?,?,?)";

            sc.nextLine();

            System.out.println("Enter full name:");
            String full_name = sc.nextLine();
            System.out.println("Enter Initial Balance:");
            double balance = sc.nextDouble();
            System.out.println("Enter the Security Pin:");
            String security_pin = sc.nextLine();

            try{
                long account_number = generateAccountNumber();
                PreparedStatement preparedStatement = conn.prepareStatement(query);
                preparedStatement.setLong(1,account_number);
                preparedStatement.setString(2,full_name);
                preparedStatement.setString(3,email);
                preparedStatement.setDouble(4,balance);
                preparedStatement.setString(5,security_pin);
                int affectedRows = preparedStatement.executeUpdate();
                if(affectedRows>0){
                    return account_number;
                }
                else{
                    throw new RuntimeException("Account Creation failed!!!!");
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
        throw new RuntimeException("Account Already Exists");
}




public long getAccountNumber(String email){
        String query = "SELECT account_number FROM accounts WHERE email = ?";

        try{
            PreparedStatement preparedStatement = conn.prepareStatement(query);
            preparedStatement.setString(1,email);
            ResultSet rs = preparedStatement.executeQuery();
            if(rs.next()){
                return rs.getLong("account_number");

            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        throw new RuntimeException("Account doesn't exist!!!!");
}

    public long generateAccountNumber(){
    try{
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT account_number FROM accounts ORDER BY account_number DESC LIMIT 1");
        if(rs.next()){
            long last_account_number = rs.getLong("account_number");
            return last_account_number +1;
        }
        else{
            return 10000100;
        }

    } catch (SQLException e) {
        System.out.println(e.getMessage());
    }
    return 10000100;


    }

    public boolean account_exist(String email){
        String query = "SELECT account_number FROM accounts WHERE email=?";

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
