package BankingManagementSystem;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Scanner;


public class BankingApp {

   private static final String url = "jdbc:mysql://localhost:3306/banking_system" ;
    private static final String username = "root" ;
    private static final String password = "@MySql1234";


    public static void main(String[] args) throws ClassNotFoundException,SQLException {

        try{
            Class.forName("com.mysql.cj.jdbc.Driver");

            System.out.println("Driver Loaded Successfully");
        } catch (ClassNotFoundException e) {
            System.out.println(e.getMessage());
            System.out.println("Driver not found!!!");
        }

        try{
           Connection conn = DriverManager.getConnection(url,username,password);
            Scanner sc = new Scanner(System.in);
            User user = new User(conn,sc);
            Accounts accounts = new Accounts(conn,sc);
            AccountManager accountManager = new AccountManager(conn,sc);

            String email;
            long account_number;

            while(true){
                System.out.println("Welcome to the Banking System!!!");
                System.out.println("1. Register");
                System.out.println("2. Login");
                System.out.println("3. Exit");
                System.out.println("Enter Your Choice:");
                int choice = sc.nextInt();

                switch (choice){
                    case 1:
                        user.register();
                        break;

                    case 2:
                        email = user.login();
                        if(email != null ){
                            System.out.println("User Logged In!!");
                            if(!accounts.account_exist(email)){
                                System.out.println("1. Open a Bank Account");
                                System.out.println("2. Exit");
                                if(sc.nextInt()==1){
                                    account_number=accounts.open_account(email);
                                    System.out.println("Account Open Succesfully.");
                                    System.out.println("Your account number is "+account_number);
                                }
                                else{
                                    break;
                                }
                            }
                            account_number = accounts.getAccountNumber(email);
                            int choice1= 0;
                            while(choice1 != 5){
                                System.out.println();
                                System.out.println("1. Debit Money");
                                System.out.println("2. Credit Money");
                                System.out.println("3. Transfer Money");
                                System.out.println("4. Check Balance");
                                System.out.println("5. Logout");
                                System.out.println("Enter your choice:");
                                 choice1 = sc.nextInt();

                                 switch(choice1){
                                     case 1:
                                         accountManager.debit_money(account_number);
                                         break;
                                     case 2:
                                         accountManager.credit_money(account_number);
                                         break;
                                     case 3:
                                         accountManager.transfer_money(account_number);
                                         break;
                                     case 4:
                                         accountManager.get_balance(account_number);
                                         break;
                                     case 5:
                                         break;
                                     default:
                                         System.out.println("Enter the valid choice !!");
                                         break;
                                 }

                            }
                        }
                        else {
                            System.out.println("Incorrect email or password!!!");
                        }

                    case 3:
                        System.out.println("Thankyou for using banking system");

                        return;
                    default:
                        System.out.println("Enter valid choice!!!!");
                        break;
                }
            }



        } catch (SQLException e) {
           e.printStackTrace();
        }


    }








}
