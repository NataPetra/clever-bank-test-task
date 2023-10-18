package by.nata;

import by.nata.service.api.IAccountService;
import by.nata.service.factory.AccountServiceSingleton;
import org.quartz.SchedulerException;

import java.util.Scanner;

public class Main {

    public static final IAccountService accountService;

    static {
        accountService = AccountServiceSingleton.getInstance();
    }

    public static void main(String[] args) throws SchedulerException {

//        SchedulerInterest.getScheduler().start();

        Scanner sc = new Scanner(System.in);
        int choice;
        double userAmount;
        String account;
        String sAccount;
        String bAccount;

        while (true) {
            System.out.println("\n----------------------");
            System.out.println("CLEVER  BANK OF  JAVA");
            System.out.println("----------------------\n");
            System.out.println("1. Refill.");
            System.out.println("2. Withdrawal.");
            System.out.println("3. Transfer to your bank.");
            System.out.println("4. Transfer to other bank.");
            System.out.println("5. Exit.");
            System.out.print("\nEnter your choice : ");
            choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {
                case 1:
                    System.out.print("Enter amount : ");
                    while (!sc.hasNextDouble()) {
                        System.out.println("Invalid amount. Enter again :");
                        sc.nextLine();
                    }
                    userAmount = sc.nextDouble();
                    sc.nextLine();
                    System.out.print("Enter beneficiary account : ");
                    account = sc.next();
                    sc.nextLine();
                    try {
                        accountService.refill(account, userAmount);
                    } catch (RuntimeException e){
                        System.out.println(e.getMessage());
                        break;
                    }
                    break;
                case 2:
                    System.out.print("Enter amount : ");
                    while (!sc.hasNextDouble()) {
                        System.out.println("Invalid amount. Enter again :");
                        sc.nextLine();
                    }
                    userAmount = sc.nextDouble();
                    sc.nextLine();
                    System.out.print("Enter account : ");
                    account = sc.next();
                    sc.nextLine();
                    try {
                        accountService.withdrawal(account, userAmount);
                    } catch (RuntimeException e){
                        System.out.println(e.getMessage());
                        break;
                    }
                    break;
                case 3:
                    System.out.print("Enter amount : ");
                    while (!sc.hasNextDouble()) {
                        System.out.println("Invalid amount. Enter again :");
                        sc.nextLine();
                    }
                    userAmount = sc.nextDouble();
                    sc.nextLine();
                    System.out.print("Enter sender account : ");
                    sAccount = sc.next();
                    sc.nextLine();
                    System.out.print("Enter beneficiary account : ");
                    bAccount = sc.next();
                    sc.nextLine();
                    try {
                        accountService.transferWithinOneBank(sAccount, bAccount, userAmount);
                    } catch (RuntimeException e){
                        System.out.println(e.getMessage());
                        break;
                    }
                    break;
                case 4:
                    System.out.print("Enter amount : ");
                    while (!sc.hasNextDouble()) {
                        System.out.println("Invalid amount. Enter again :");
                        sc.nextLine();
                    }
                    userAmount = sc.nextDouble();
                    sc.nextLine();
                    System.out.print("Enter sender account : ");
                    sAccount = sc.next();
                    sc.nextLine();
                    System.out.print("Enter beneficiary account : ");
                    bAccount = sc.next();
                    sc.nextLine();
                    try {
                        accountService.transferWithinDifferentBanks(sAccount, bAccount, userAmount);
                    } catch (RuntimeException e){
                        System.out.println(e.getMessage());
                        break;
                    }
                    break;
                case 5:
                    System.out.println("\nThank you for choosing Clever Bank Of Java.");
                    System.exit(1);
                    break;
                default:
                    System.out.println("Wrong choice!");
            }
        }
    }
}