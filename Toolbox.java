package challengeMastermind;

import java.util.Random;
import java.util.Scanner;

public class Toolbox {

	/**
	 * This method returns a validated int from scanner. There is a prompt Prompt.
	 * 
	 * @param prompt A string to prompt user with
	 * @return a validated int of any value
	 */
//	public static int get_int(String prompt) {
//		@SuppressWarnings("resource")
//		Scanner scanner = new Scanner(System.in);
//		int userInt;
//		// prompt user
//		System.out.printf(prompt);
//		// validate as int
//		for (int i = 0; !scanner.hasNextInt(); i++) {
//			if (i > 0) {
//				System.out.println("You must provide an integer:");
//			}
//			scanner.nextLine();
//		}
//		userInt = scanner.nextInt();
//		return userInt;
//	}
	
	/**
	 * This method returns a validated int from scanner. No Prompt.
	 * 
	 * @return a validated int of any value
	 */
	public static int get_int(String errorMessage) {
		@SuppressWarnings("resource")
		Scanner scanner = new Scanner(System.in);
		int userInt;
		
		// validate as int
		for (int i = 0; !scanner.hasNextInt(); i++) {
			if (i > 0) {
				System.out.printf(errorMessage);
			}
			scanner.nextLine();
		}
		userInt = scanner.nextInt();
		return userInt;
	}

	/**
	 * This method will generate a random code of varying length depending on difficulty settings
	 * 
	 * @param settings An int[] storing game settings
	 * @return An int[] storing the dynamically sized code
	 */
	public static int[] generateCode(int[] settings) {
		// declare vars
		int[] code = new int[settings[2]];
		Random rand = new Random();
		//populate code array
		for (int i = 0; i < settings[2]; i++) {
			code[i] = rand.nextInt(settings[3]) + 1;
		}
		// return int[]
		return code;
	}
}

/**
 * This method returns an int between the lower and upper bounds (inclusive) from a Scanner
 * 
 * @param lower Lower bound
 * @param upper Upper bound
 * @return An int - validated as an int, and between the provided bounds (inclusive)
 */
//public static int get_int(int lower, int upper) {
//	@SuppressWarnings("resource")
//	Scanner scanner = new Scanner(System.in);
//	int number = -1;
//	boolean isInt = false;
//	boolean inRange = false;
//	boolean toPrintError = false;
//
//	while (!isInt || !inRange) {
//
//		isInt = false;
//		inRange = false;
//		toPrintError = false;
//
//		if (scanner.hasNextInt()) {
//			number = scanner.nextInt();
//			isInt = true;
//		} else {
//			toPrintError = true;
//		}
//		if (isInt && number >= lower && number <= upper) {
//			inRange = true;
//		} else {
//			toPrintError = true;
//		}
//		scanner.nextLine();
//		if (toPrintError) {
//			System.out.printf("You must select a number between %d and %d (inclusive):%n", lower, upper);
//		}
//
//	}
//	return number;
//}
