package challengeMastermind;

import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;

/**
 * This is my answer to Aaron Marshall's mastermind challenge
 * https://en.wikipedia.org/wiki/Mastermind_(board_game)
 * 
 * @author Peter Marley
 */
@SuppressWarnings("unused")
public class MastermindGame {

	/**
	 * main() steps:<br>
	 * 1) Generate int[] SETTINGS - mouse over selectDifficultySettings() method for details<br>
	 * 2) Generate int[] CODE - the dynamically lengthed code, different sizes for different difficulties, max digit at each position <= SETTINGS[3]<br>
	 * 3) Declare and size int[] scoreSheet - which will be used to house the scores for each round, the guess for that round and the 
	 * number of partial and complete correct guesses<br>
	 * 4) Set boolean win flag to false to begin round() loop<br>
	 * 5) When round() returns a true boolean, then finish rounds, print out results for win or lose condition
	 * 
	 * @param args
	 */
	public static void main(String[] args) {

		// delcare/ initialise variables

		// Dynamic settings & code
		final int[] SETTINGS = selectDifficultySettings(); // set settings[] array
		final int[] CODE = generateCode(SETTINGS); // generate code
		
		/*
		 * TEST SETTINGS, UNCOMMENT ONE BLOCK BELOW (AND COMMENT OUT ABOVE TWO LINES ALSO)
		 */
		//TEST SETTINGS & TEST CODE - NORMAL MODE
//		final int[] SETTINGS = { 2, 10, 4, 6 };
//		final int[] CODE = new int[] { 3, 1, 4, 1 };
		
		//TEST SETTINGS & TEST CODE - NORMAL MODE 2
//		final int[] SETTINGS = { 2, 10, 4, 6 };
//		final int[] CODE = new int[] { 2, 2, 1, 6 };

		//TEST SETTINGS & TEST CODE - EASY MODE
//		final int[] SETTINGS = { 1, 15, 3, 5 };
//		final int[] CODE = new int[] { 3, 1, 1 };
		
		int[][] scoreSheet = new int[SETTINGS[1]][3];

		boolean win = false;

		//System.out.println("TEST PRINT Code: " + Arrays.toString(CODE)); // test print code
		//System.out.println("TEST PRINT Settings: " + Arrays.toString(SETTINGS));


		// simulate rounds
		int round = 1;
		for (; round <= SETTINGS[1] && !win; round++) {
			win = round(SETTINGS, CODE, scoreSheet, round);
		}

		// print actual code generated
		System.out.print("The code was: ");
		for (int i = 0; i < CODE.length; i++) {
			System.out.print(CODE[i]);
		}
		System.out.println();
		
		// print win or lose message
		if (win) {
			System.out.println("Congratulations, you won in " + (round - 1) + " moves!");
		} else {
			System.out.println("Sorry you didn't win this time, better luck next time!");
		}
	}

	/**
	 * This method simulates a round of mastermind, checking for correct and partially correct guesses
	 * @param SETTINGS An int[] - containing the raw difficulty, guesses allowed, code length and max digits in code
	 * @param CODE An int[] - containing the generated random code of SETTINGS[2] length
	 * @param scoreSheet An int[][] - each row is a round, each row contains turn #, round guess, correct guesses, partially correct guesses
	 * @param round An int - denoting the current round
	 * @return A boolean denoting if the round was a win
	 */
	public static boolean round(int[] SETTINGS, int[] CODE, int[][] scoreSheet, int round) {
		int guess = validateGuess(SETTINGS, round);
		int totalCorrect = 0;
		int partialCorrect = 0;
		boolean win = false;

		boolean[] isCorrect = new boolean[SETTINGS[2]];
		int[] partials = new int[10];
		int[] userGuessArray = new int[SETTINGS[2]];

		/*  
		 * -calculate the number of each digit in CODE[]
		 */
		for (int i = 0; i < CODE.length; i++) {
			userGuessArray[i] = ((int) String.valueOf(guess).charAt(i)) - 48;
			partials[CODE[i] - 1]++;
		}

		/*
		 *  -iterate through CODE and userGuessArray checking for totally correct guesses
		 */
		for (int i = 0; i < CODE.length; i++) {
			if (CODE[i] == userGuessArray[i]) { // if digit is correct
				totalCorrect++;
				isCorrect[i] = true;
				partials[userGuessArray[i] - 1]--; // remove from the partial pool
			}
		}

		/*
		 * Iterate through CODE and userGuessArray checking for partially correct guesses
		 */
		for (int i = 0; i < CODE.length; i++) {
			if (partials[userGuessArray[i] - 1] > 0 && !isCorrect[i]) {
				partials[userGuessArray[i] - 1]--;
				partialCorrect++;
			}
		}

		// append to scoreSheet
		scoreSheet[round - 1][0] = guess; // store guess in ledger
		scoreSheet[round - 1][1] = totalCorrect; // store total correct number in score sheet
		scoreSheet[round - 1][2] = partialCorrect; // store total partially correct in score sheet

		// print round result
		System.out.printf("You got %d exactly right, and %d other correct number(s) in the wrong position%n%n", totalCorrect, partialCorrect);

		// print summary
		System.out.printf("%s%n", "Current progress:");
		System.out.printf("%4s | %5s | %14s | %18s%n", "Turn", "Guess", "#Fully correct", "#Partially correct");
		for (int i = 0; i < round; i++) {
			System.out.printf("%4d | %5d | %-14d | %-18d%n", i + 1, scoreSheet[i][0], scoreSheet[i][1], scoreSheet[i][2]);
		}
		System.out.println();
		
		// decide if game was won
		if (totalCorrect == CODE.length) {
			win = true;
		}
		
		// TEST PRINTS
		//		System.out.println("TEST PRINT CODE " + Arrays.toString(CODE));
		//		System.out.println("TEST PRINT contents of userGuessArray " + Arrays.toString(userGuessArray));
		//		System.out.println("TEST PRINT isCorrect: " + Arrays.toString(isCorrect));
		//		System.out.println("TEST PRINT partial correct: " + partialCorrect);
		//		System.out.println("TEST PRINT totalCorrect: " + totalCorrect);
		return win;
	}

	/**
	 * This method validates user input guess is within the specs of difficulty. IE, positive, certain length, certain digits
	 * 
	 * @param SETTINGS An int[] containing the difficult settings
	 * @param round    An int representing which round we are on
	 * @return a validated int within specs for current difficulty
	 */
	public static int validateGuess(int[] SETTINGS, int round) {
		// declare variables
		int userGuess = 0;
		int userGuessIterator;
		String errorMessage;

		boolean isAccepted = false;
		boolean lengthAccepted = false;
		boolean isPositive = false;
		boolean digitsInRange = true;

		System.out.printf("Turn %d - enter your guess (%d numbers from 1-%d)%n", round, SETTINGS[2], SETTINGS[3]);
		errorMessage = "Invalid input. Please enter " + SETTINGS[2] + " numbers from 1-" + SETTINGS[3] + "%n";

		while (!isAccepted) {
			// reset flags
			isAccepted = false;
			lengthAccepted = false;
			isPositive = false;
			digitsInRange = true;

			// get int
			userGuess = get_int(errorMessage);
			userGuessIterator = userGuess;

			// check is positive number
			if (userGuess > 0) {
				isPositive = true;
			}

			// check length
			if (String.valueOf(userGuess).length() == SETTINGS[2]) {
				lengthAccepted = true;
			}

			// check max digits
			for (int i = 1; i <= SETTINGS[2] && digitsInRange; i++) {
				//System.out.println(((userGuessIterator % tenths) <= settings[3]));
				if (!((userGuessIterator % 10) <= SETTINGS[3])) {
					digitsInRange = false;
				}
				userGuessIterator /= 10;

			}

			// is number accepted?
			if (lengthAccepted && isPositive && digitsInRange) {
				isAccepted = true;
				//System.out.println("number accepted");
			} else {
				System.out.printf(errorMessage);
			}

			// TEST PRINTS
			/*System.out.println("isPositive " + isPositive);
			System.out.println("lengthAccepted " + lengthAccepted);
			System.out.println("digitsInRange " + digitsInRange);
			System.out.println("isAccepted " + isAccepted);*/
		}

		return userGuess;
	}

	/**
	 * selects the difficulty and settings for the game stored in a int[3].<br>
	 * 0 - raw difficulty level<br>
	 * 1 - guesses allowed<br>
	 * 2 - code length<br>
	 * 3 - digit max (inclusive)<br>
	 * @return An int[] - which includes the above information at each index
	 */
	public static int[] selectDifficultySettings() {
		// declare variables
		int[] settings = new int[3];
		int difficultyNum = -1;
		String errorMessage = "Invalid input. Choose a difficulty (1-3):%n";
		String difficultyString = "";
		boolean inputIsValid = false;
		System.out.printf("Welcome to Code Cracker, please choose a difficulty (1-3):%n"
				+ "1) Easy - You have 15 guesses to crack a 3 digit code. Possible digits are 1-5.%n"
				+ "2) Normal - You have 10 guesses to crack a 4 digit code. Possible digits are 1-6.%n"
				+ "3) Hard - You have 10 guesses to crack a 5 digit code. Possible digits are 1-8.%n");

		// get int between 1 and 3 representing the difficulty levels
		while (!inputIsValid) {
			inputIsValid = false;
			difficultyNum = get_int(errorMessage);
			if (difficultyNum >= 1 && difficultyNum <= 3) {
				inputIsValid = true;
			} else {
				System.out.println(errorMessage);
			}
		}

		// populate int[] settings and difficultyString
		/*
		 settings[0] = raw difficulty level (1-3 inclusive)
		 settings[1] = guesses remaining (15 or 10)
		 settings[2] = length of code to generate (3-5 inclusive)
		 settings[3] = maximum number allowed in each code position (5-8 inclusive) 
		*/
		switch (difficultyNum) {
		case 1:
			settings = new int[] { 1, 15, 3, 5 };
			difficultyString = "Easy";
			break;
		case 2:
			settings = new int[] { 2, 10, 4, 6 };
			difficultyString = "Normal";
			break;
		case 3:
			settings = new int[] { 3, 10, 5, 8 };
			difficultyString = "Hard";
			break;
		default:
			settings = new int[] { 2, 10, 4, 6 };
			difficultyString = "Normal";
			System.out.println("DIFFICULTY DEFAULTED TO NORMAL");
		}

		// print users difficulty selection
		System.out.printf("You have selected %s. You have %d guesses to crack a %d digit code. Possible digits are 1-%d.%n", difficultyString, settings[1], settings[2], settings[3]);

		// return int[] settings
		return settings;
	}
	
	/**
	 * This method returns a validated int from scanner. displays an error message String when validation fails.
	 * @param A String containing the error message. It must be formatted for printf
	 * @return a validated int of any value
	 */
	public static int get_int(String errorMessage) {
		@SuppressWarnings("resource")
		Scanner scanner = new Scanner(System.in);
		int userInt;
		
		// validate as int
		for (int i = 0; !scanner.hasNextInt(); i++) {
			//if (i > 0) {
				System.out.printf(errorMessage);
			//}
			scanner.nextLine();
		}
		userInt = scanner.nextInt();
		return userInt;
	}

	/**
	 * This method will generate a random code of varying length depending on difficulty settings
	 * 
	 * @param SETTINGS An int[] storing game settings
	 * @return An int[] storing the dynamically sized code
	 */
	public static int[] generateCode(int[] SETTINGS) {
		// declare vars
		int[] code = new int[SETTINGS[2]];
		Random rand = new Random();
		//populate code array
		for (int i = 0; i < SETTINGS[2]; i++) {
			code[i] = rand.nextInt(SETTINGS[3]) + 1;
		}
		// return int[]
		return code;
	}

}
