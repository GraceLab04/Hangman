
//Grace LaBelle

import java.util.*;
import java.io.File;
//import javax.swing.JFileChooser;


public class Hangman {
    
    //Main Method
    public static void main(String[] args) throws Exception{
      
    ArrayList<String> mysteryWordList = new ArrayList<String>();
    
    //variables
    String stringInput;
    String tempPreviousWord = "";
    String puzzleWord;
    String userGuess;
    int guessesRemaining = 7;
    int maxGuessCount = 7; //amount of guesses
    int incorrectGuessCount = 0;
    
        //create filechooser object
    java.io.File file = new File("src/wordlist.txt");
        //JFileChooser fileChooser = new JFileChooser();
    //if(fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
        
        //get the selected file
        //java.io.File file = fileChooser.getSelectedFile();
    
        Scanner input = new Scanner(file);
        
        //read text from the file
        while(input.hasNext()) {
            stringInput = input.nextLine();
            stringInput = stringInput.toUpperCase(); //convert to uppercase for comparison    
                if(!stringInput.isEmpty()) { //(stringInput.length() != 0){
                mysteryWordList.add(stringInput); //add input to arrayList
                }
        }
        input.close();//close input scanner
    
        
    System.out.printf("%77s\n", "+------------------------------------------+");
    System.out.printf("%77s\n", "|     Welcome to Grace's Hangman Game!     |");
    System.out.printf("%77s\n", "|       Type a letter or word to guess.    |");
    System.out.printf("%77s\n", "|  You may have up to 7 incorrect guesses. |");
    System.out.printf("%77s\n", "|  Type 'quit' at any time to end the game.|");
    System.out.printf("%77s\n", "+------------------------------------------+");
    
    Scanner userInput = new Scanner (System.in);
    int isValidUserInput;
    String playerOneOrTwo;
    char playAgain = 'Y';
    while(playAgain == 'Y' ) {
        do{
        System.out.print("\n* One or Two Players (Type: 1 or 2) * ");
        playerOneOrTwo = userInput.nextLine().toUpperCase();
        }
        while(!playerOneOrTwo.equals("P1") && !playerOneOrTwo.equals("P2") 
        && !playerOneOrTwo.equals("1") && !playerOneOrTwo.equals("2"));
        
        if(playerOneOrTwo.equals("P1") || playerOneOrTwo.equals("1")){
            //shuffle word list
            do{
                puzzleWord = mysteryWordShuffle(mysteryWordList);
            }
            while(puzzleWord.equals(tempPreviousWord)); //to prevent same word showing up twice in a row
            tempPreviousWord = puzzleWord;
        }
        else {
            do{

                System.out.print("\n      Player 1: please enter a mystery word." +
                "\n*single words only: no numbers, symbols, or spaces ");
                puzzleWord = userInput.nextLine().toUpperCase();
                isValidUserInput = isValidInput(puzzleWord);
                tempPreviousWord = puzzleWord;
                System.out.print("\n\n");
            }
            while(isValidUserInput == -1 && !puzzleWord.equals("QUIT"));
        }
        //System.out.print("The mystery word is: " + puzzleWord); //FOR TESTING
        
        int lengthOfWord = puzzleWord.length();
        char [] wordArray = new char [lengthOfWord];
        char [] hiddenWordArray = new char [lengthOfWord];
        
        //populate hiddenWordArray and wordArray with word letters for comparison
        for(int i = 0; i < lengthOfWord; i++){
            wordArray[i] = puzzleWord.charAt(i);
            hiddenWordArray[i]= '_'; //blank spaces for hidden word
        }
        
        System.out.print("\nThe Puzzle is: \n");
        printHangman(incorrectGuessCount, hiddenWordArray);
    
        
    
        //variables
        boolean rightOrWrongGuess;
        boolean isRepeatLetter = false;
        final int ALPHABET = 26;
        int isValid = 0;
        int correctGuessCount = 0;
        int incorrectLetterCount = 0;
        String userSolution = "";
        char [] correctLetterGuesses = new char [ALPHABET];
        char [] incorrectLetterGuesses = new char [ALPHABET];
        char userLetterGuess = ' ';
        
        System.out.print("\u25B6  Guesses remaining: " + guessesRemaining);
      
        do{
            System.out.print("\n\nPlease guess a letter or word. ");
            //if single char
            do{

                //check if valid input and not repeated letter
                if(isValid == -1){
                    System.out.print("Invalid Response! Please guess a letter ");
                }
                else if (isRepeatLetter){
                    System.out.print("Letter " + userLetterGuess + " has been guessed already. " +
                    "Please guess again ");
                }

                userGuess = userInput.nextLine().toUpperCase();
                userLetterGuess = userGuess.charAt(0);
                isValid = isValidInput(userGuess);
                isRepeatLetter = false;

                //if single letter guess, not word guess
                if(userGuess.length() < 2){
                isRepeatLetter = isRepeatLetter(userGuess, correctLetterGuesses, incorrectLetterGuesses);
                }
            }//close do while - guess input

            while(isValid == -1 || isRepeatLetter); //while invalid or repeated letter

            //determine if guess is correct or incorrect
            if(userGuess.length() < 2){
                rightOrWrongGuess = false;

                //comparison
                for(int i = 0; i < lengthOfWord; i++) {
                    if (userLetterGuess == wordArray[i]){
                        rightOrWrongGuess = true;
                        hiddenWordArray[i] = userLetterGuess;//populate word board
                        correctGuessCount++;
                    }
                }

                //if correct
                if (rightOrWrongGuess) {
                    correctLetterGuesses[correctGuessCount] = userLetterGuess;//correctly guessed letters

                    //print board and info: remaining guesses, letters guessed

                }

                //if wrong
                else {

                    incorrectLetterGuesses[incorrectGuessCount] = userLetterGuess;//incorrect letters stored
                    incorrectLetterCount++;//used to determine incorrect letters, not incorrect words
                    incorrectGuessCount++;
                    guessesRemaining = maxGuessCount - incorrectGuessCount;

                }
                printHangman(incorrectGuessCount, hiddenWordArray);
                printBoardInfo(rightOrWrongGuess, guessesRemaining, incorrectLetterCount, incorrectLetterGuesses, userSolution);
            }//end if - letter is guessed

            //if word is guessed
            if(!userGuess.equals("QUIT") && userGuess.length() > 1) {
                
                //determine if word guess is valid
                do{
                    if(isValid == -1){
                        System.out.print("Invalid Response! Please guess a valid word. ");
                    }
                userSolution = userGuess;
                isValid = isValidInput(userSolution);
                }
                while(isValid == -1);
                    
                //if incorrect word guess
                if (!userSolution.equals(puzzleWord)){
                    incorrectGuessCount++;

                    //"reset" guess variables
                    userGuess = "";
                    userLetterGuess = ' ';
                    guessesRemaining = maxGuessCount - incorrectGuessCount;
                    
                    printHangman(incorrectGuessCount, hiddenWordArray);
                    printBoardInfo(false, guessesRemaining, incorrectLetterCount, incorrectLetterGuesses, userSolution);
                    userSolution = "";
    
                }
            }//end if- word is guessed
            
            //if win or run out of guesses
            if (incorrectGuessCount == maxGuessCount) {
                lose();
            }
            else if(userSolution.equals(puzzleWord)){
                winner();
                userGuess = "QUIT"; //to end while loop
            }
            else if(correctGuessCount == lengthOfWord){
                winner();
            }
            } //end do while - guesses loop
        while(!userGuess.equals("QUIT") && correctGuessCount < lengthOfWord
        && incorrectGuessCount < maxGuessCount);
        
        System.out.print("\nThe mystery word was: " + puzzleWord);
        System.out.print("\nWould you like to play again? Y or N ");
        
        //reset variables
        incorrectGuessCount = 0;
        guessesRemaining = 7;
            
        //validate Y or N input
        do{
            if(isValid == -1 || playAgain != 'Y'){
                System.out.print("Invalid Response! Please enter Y or N ");
            }
            
            playAgain = userInput.nextLine().toUpperCase().charAt(0);
            isValid = isValidInput(playAgain);
        }
        while(isValid == -1 || (playAgain != 'Y' && playAgain != 'N'));
        
        System.out.println();
    }//close while loop - play again
    
    userInput.close(); //close input
    System.out.println("Goodbye");
    } //close main
    
    //print board information: remaining guesses, letters guessed, correct or incorrect response
    public static void printBoardInfo(boolean rightOrWrong, int guessesRemaining, int incorrectLetterCount, char[] letters, String userSolution){
        if(incorrectLetterCount > 0 && guessesRemaining > 0){
            System.out.print(" \u25B6  Letters Guessed: ");
            printArray(letters);
            }
        System.out.print("\u25B6 Guesses remaining: " + guessesRemaining);
        if(!rightOrWrong && !userSolution.isEmpty()){
            System.out.print("\n     -Incorrect Word Guess!-");
        }
        else if (rightOrWrong){
            System.out.print("\n          -Correct!- ");
        }
        else {
            System.out.print("\n         -Incorrect!-");
        }
        
    }
    
    //determine if letter is repeated
    public static boolean isRepeatLetter(String userGuess, char [] correctGuesses, char[] incorrectGuesses){
            char userGuessLetter = userGuess.charAt(0);
            if(!userGuess.equals("QUIT")){
                for(int i = 0; i < correctGuesses.length; i++){
                    if (userGuessLetter == correctGuesses[i] || userGuessLetter == incorrectGuesses[i]) {
                        return true;
                    }
                }
            }
            else{
                return false;
            }
            return false;
    }

    //determine valid string input
    public static int isValidInput(String userInput){
        for(int i = 0; i < userInput.length(); i++){
            
            char userInputChar = userInput.charAt(i);
            if(userInputChar >= 'A' && userInputChar <= 'Z' || userInputChar <= 9){
                return 1;
            }
            else {
                return -1;
            }
        }
        return -1;
    }
    
    //determine valid char input
    public static int isValidInput (char userInput){
        
            if(userInput >= 'A' && userInput <= 'Z' || userInput <= 9){
                return 1;
            }
            else {
                return -1;
            }
        }
    
    //print array
    public static void printArray(char [] array){
        for (char c : array) {
            if (c != '\u0000') {
                System.out.print(c + " ");
            }
        }
    
    }
    
    //shuffle the list of words
    public static String mysteryWordShuffle(ArrayList<String> wordList){ 
        Collections.shuffle(wordList);
        return wordList.getFirst();
    }
    
    //print hangman board
    public static void printHangman(int numWrongGuesses, char [] puzzleBoard){
        
        //string variables
        String zeroLine = "         |      \n         |      \n         |      \n" + 
        "         |      \n";
        String firstSecondLine = "\n         /------\\\n         |      |\n";
        String fifthLine = "         |      \n         |      \n";
        String sixthLine = "         |      \n";
        String seventhEighthLine = "         |      \n       __|__  ";
        String wrong1 = "         |     (°°)\n";
        String wrong2 = "         |      |\n";
        String wrong3 = "         |    _/|\n";
        String wrong4 = "         |    _/|\\_\n";
        String wrong5 = "         |      Λ\n";
        String wrong6 = "         |    _/\n";
        String wrong7 = "         |    _/ \\_\n";
        
        //print
        System.out.print(firstSecondLine);
        switch(numWrongGuesses){
            case 0: System.out.print(zeroLine); break;
            case 1: System.out.print(wrong1 + fifthLine + sixthLine); break;
            case 2: System.out.print(wrong1 + wrong2 + fifthLine); break;
            case 3: System.out.print(wrong1 + wrong3 + fifthLine); break;
            case 4: System.out.print(wrong1 + wrong4 + fifthLine); break;
            case 5: System.out.print(wrong1 + wrong4 + wrong5 + sixthLine); break;
            case 6: System.out.print(wrong1 + wrong4 + wrong5 + wrong6); break;
            case 7: System.out.print(wrong1 + wrong4 + wrong5 + wrong7);break;
        }
        System.out.print(seventhEighthLine + "\n");
        System.out.print("\n  \u25B6 ");
        printArray(puzzleBoard);
    }
    
    //win and lose output
    public static void winner(){
        System.out.printf("\n\n%25s\n", "You Won!!!");
    }
    
    public static void lose(){
        System.out.printf("\n\n%25s\n", "You lost!!!");
    }

} //close class

