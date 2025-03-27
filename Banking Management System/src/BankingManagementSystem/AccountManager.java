package BankingManagementSystem;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class AccountManager {
    private Connection conn;
    private Scanner sc;

    public AccountManager(Connection conn, Scanner sc){
        this.conn=conn;
        this.sc = sc;
    }

    public void credit_money(long account_number) throws SQLException {
       sc.nextLine();
        System.out.println("Enter the amount:");
        double amount = sc.nextDouble();
        System.out.println("Enter the Security Pin:");
        String security_pin = sc.nextLine();

        try{
            conn.setAutoCommit(false);
            if(account_number !=0){
                PreparedStatement preparedStatement = conn.prepareStatement("SELECT * FROM accounts WHERE account_number = ? AND security_pin = ?");
                preparedStatement.setLong(1,account_number);
                preparedStatement.setString(2,security_pin);
                ResultSet rs= preparedStatement.executeQuery();

                if(rs.next()){
                    String credit_query = "UPDATE accounts SET balance = balance + ? WHERE account_number = ?";
                    PreparedStatement preparedStatement1 = conn.prepareStatement(credit_query);
                    preparedStatement1.setDouble(1,amount);
                    preparedStatement1.setLong(2,account_number);
                    int affectedRows = preparedStatement.executeUpdate();

                    if(affectedRows>0){
                        System.out.println("Rs."+amount+"credited Successfully.");
                        conn.commit();
                        conn.setAutoCommit(true);
                        return;


                    }
                    else{
                        System.out.println("Transaction failed");
                        conn.rollback();
                        conn.setAutoCommit(true);
                    }

                }
                else{
                    System.out.println("Invalid pin");
                }

            }


        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        conn.setAutoCommit(true);



    }

    public void debit_money(long account_number) throws SQLException {

        sc.nextLine();
        System.out.println("Enter the amount:");
        double amount = sc.nextDouble();
        System.out.println("Enter the security pin:");
        String security_pin = sc.nextLine();

        try{
            conn.setAutoCommit(false);
            if( account_number!=0){
                String query = "SELECT * FROM accounts WHERE account_number= ? AND security_pin = ?";
                PreparedStatement  preparedStatement = conn.prepareStatement(query);
                preparedStatement.setLong(1,account_number);
                preparedStatement.setString(2,security_pin);
                ResultSet rs = preparedStatement.executeQuery();

                if(rs.next()){
                    double current_balance = rs.getDouble("balance");
                    if(amount<=current_balance)
                    {
                        String debit_query ="UPDATE accounts SET balance = balance - ? WHERE account_number= ?";
                        PreparedStatement preparedStatement1 = conn.prepareStatement(debit_query);
                        preparedStatement1.setDouble(1,amount);
                        preparedStatement1.setLong(2,account_number);
                        int affectedRows = preparedStatement1.executeUpdate();

                        if(affectedRows>0){
                            System.out.println("Rs."+amount+"debited Successfully");
                            conn.commit();
                            conn.setAutoCommit(true);
                            return;
                        }
                        else{
                            System.out.println("Transaction failed!!!!");
                            conn.rollback();
                            conn.setAutoCommit(true);
                        }


                    }
                    else{
                        System.out.println("Insufficient Fund");
                    }

                }
                else {
                    System.out.println("Invalid pin!!!");
                }

            }





        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }


        conn.setAutoCommit(true);



    }

    public void transfer_money(long sender_account_number) throws SQLException {

        sc.nextLine();
        System.out.println("Enter the receiver account number:");
        long receiver_account_number = sc.nextLong();
        System.out.println("Enter the amount:");
        double amount = sc.nextDouble();
        System.out.println("Enter the security pin:");
        String security_pin = sc.nextLine();

        try{
            conn.setAutoCommit(false);
            if(sender_account_number!=0 && receiver_account_number!=0){
                PreparedStatement preparedStatement = conn.prepareStatement("SELECT * FROM accounts WHERE account_number=? AND security_pin = ?");
                preparedStatement.setLong(1,sender_account_number);
                preparedStatement.setString(2,security_pin);
                ResultSet rs=preparedStatement.executeQuery();
                if(rs.next()){
                    double current_balance = rs.getDouble("balance");
                    if(amount<=current_balance){
                        String debit_query = "UPDATE accounts SET balance = balance - ? WHERE account_number = ?";
                        String credit_query = "UPDATE accounts SET balance = balance + ? WHERE account_number = ?";
                        PreparedStatement preparedStatement1 = conn.prepareStatement(debit_query);
                        preparedStatement1.setDouble(1,amount);
                        preparedStatement1.setLong(2,sender_account_number);
                        int affectedRows = preparedStatement1.executeUpdate();
                        PreparedStatement preparedStatement2 = conn.prepareStatement(credit_query);
                        preparedStatement2.setDouble(1,amount);
                        preparedStatement1.setLong(2,receiver_account_number);
                        int affectedRows1 = preparedStatement2.executeUpdate();

                        if(affectedRows>0 && affectedRows1>0){
                            System.out.println("Rs."+amount+"transferred Successfully");
                            conn.commit();
                            conn.setAutoCommit(true);
                            return;

                        }
                        else{
                            System.out.println("Transfer fails!!!");
                            conn.rollback();
                            conn.setAutoCommit(true);
                        }

                    }
                    else{
                        System.out.println("Insufficient Balance");
                    }
                }
                else{
                    System.out.println("Invalid Security Pin");
                }


            }
            else{
                System.out.println("Invalid Account NUmber!!!");
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        conn.setAutoCommit(true);





    }



public void get_balance(long account_number){
        sc.nextLine();
    System.out.println("Enter the Security Pin:");
    String security_pin = sc.nextLine();

    String query = "SELECT balance FROM accounts WHERE account_number = ? AND security_pin = ?";

    try{
        PreparedStatement preparedStatement = conn.prepareStatement(query);
        preparedStatement.setLong(1,account_number);
        preparedStatement.setString(2,security_pin);
        ResultSet rs = preparedStatement.executeQuery();
        if(rs.next()){
            double balance = rs.getDouble("balance");

            System.out.println("Rs."+ balance + "is your balance");
        }
        else{
            System.out.println("Invalid Pin!!!!");
        }
    } catch (SQLException e) {
        throw new RuntimeException(e);
    }

}





}
