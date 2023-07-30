import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Console Writer with personalized styles.
 */
public class ConsoleWriter {
    /**
     * Sets the default program date/time format.
     */
    private final SimpleDateFormat dateFormater;

    /**
     * User retrieved data
     */
    private final UserData userData;

    /**
     * Class constructor
     * @param dateFormater Sets the default program date/time format
     * @param userData User retrieved data
     */
    public ConsoleWriter(SimpleDateFormat dateFormater, UserData userData) {
        this.dateFormater = dateFormater;
        this.userData = userData;
    }

    /**
     * Prints an error message on the console and terminates the execution with code 0.
     * @param errorMessage Error detail.
     */
    public void replyWithError(String errorMessage){
        System.out.println("[" + dateFormater.format(new Date()) + "] \u001B[31mERROR:\u001B[0m " + errorMessage + " \u001B[31mTerminating Execution ...\u001B[0m");
        System.exit(0);
    }

    /**
     * Prints a Success message on the console and terminates the execution woth code 1.
     * @param successMessage Success message.
     */
    public void replyWithSuccess(String successMessage){
        System.out.println("[" + dateFormater.format(new Date()) + "] \u001B[32mSUCCESS:\u001B[0m " + successMessage + " Terminating Comparator ...");
        System.exit(1);
    }

    /**
     * Prints an Info message on the console.
     * @param message Info message.
     */
    public void printInfo(String message){
        System.out.println("[" + dateFormater.format(new Date()) + "] \u001B[34mINFO:\u001B[0m " + message);
    }


    /**
     * Generates the program banner by printing it on the console.
     */
    public void banner(){
        System.out.println("\n\u001B[42m### FileComparer (c) João Duarte ###\u001B[0m\n");
        System.out.println(">>> Computer: " + userData.getComputerName() + " <<<");
        System.out.println(">>> \u001B[32mWelcome, " + userData.getLoggedUser() + "\u001B[0m <<<\n");
    }
}
