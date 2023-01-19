package library;

import java.util.Scanner;

public class Menu {
    static Scanner sc = new Scanner(System.in);

    public static void menu() {
        int choice;
        do {
            System.out.println("1. Go to Section");
            System.out.println("2. Return a Book");
            System.out.println("3. View Library Card");
            System.out.println("4. Exit");
            System.out.print("Enter choice(1-4): ");
            choice = sc.nextInt();

            if (choice < 1 || choice > 4) {
                System.out.println("Wrong choice entered!");
                menu();
            } else if (choice == 1) {

                // SECTION
            } else if (choice == 2) {
                // RETURN BOOK
            } else if (choice == 3) {
                // VIEW CARD
            } else {
                return;
            }
        } while (choice != 4);
    }

    public static void main(String[] args) {

    }
}
