package basic;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Scanner;

public class StartGame {
	
	private String insertPins (String scoreLine, int frameNumber) {
		
		
		return scoreLine;
	}
    public static int getPinCount() {
        return getPinCount(System.in);
    }
    public static int getPinCount(InputStream inputStream) {
        Scanner scanner = new Scanner(inputStream);
        int userInput = 0; 

        boolean isValidInput = false;
        while (!isValidInput) {
            System.out.print("Enter the number of pins: ");
            if (scanner.hasNextInt()) {
                userInput = scanner.nextInt();
                if (userInput > 0 && userInput < 11) {
                	isValidInput = true; 
                }
                else {
                    System.out.println("Invalid input. Please enter a valid pin count.");
                    scanner.nextLine();
                }
            } else {
                System.out.println("Invalid input. Please enter a valid pin count.");
                scanner.nextLine();
            }
        }

        return userInput;
    }

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		System.out.println("Start the game");
		int pins;
		SingleGame newGame = new SingleGame();
		String borderLine = "=============================================================";
		while (!((SingleGame) newGame).isGameover()) {
			pins  = getPinCount();
			newGame.addShot(pins);
			System.out.println(borderLine);
			System.out.println(newGame.getPinsInFrame());
			System.out.println(newGame.getPointsInFrame());
			System.out.println(borderLine);

		}
		System.out.println("Game over!");			

		
	}

}
