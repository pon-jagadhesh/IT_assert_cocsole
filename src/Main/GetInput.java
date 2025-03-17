package Main;

import java.util.Scanner;
import java.util.InputMismatchException;

public class GetInput {

    static Scanner sc = new Scanner(System.in);

    public static String getInputString(String description) {
        System.out.println(description);
        String input;
        while (true) {
            try {
                return sc.nextLine();
            } catch (Exception e) {
                System.out.println("Error: Invalid input. Please try again.");
                sc.nextLine();
            }
        }
    }

    public static int getInputNumbers(String description) {
        System.out.println(description);
        while (true) {
            try {
                return sc.nextInt();
            } catch (InputMismatchException e) {
                System.out.println("Error: Please enter a valid integer.");
                sc.nextLine();
            }
        }
    }

    public static float getInputFloat(String description) {
        System.out.println(description);
        while (true) {
            try {
                return sc.nextFloat();
            } catch (InputMismatchException e) {
                System.out.println("Error: Please enter a valid float value.");
                sc.nextLine();
            }
        }
    }
}
