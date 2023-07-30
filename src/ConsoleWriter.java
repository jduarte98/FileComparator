import javax.annotation.processing.Generated;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;

public class ConsoleWriter {
    private final SimpleDateFormat dateFormater;
    private UserData userData;

    public ConsoleWriter(SimpleDateFormat dateFormater, UserData userData) {
        this.dateFormater = dateFormater;
        this.userData = userData;
    }

    public void replyWithError(String errorMessage){
        System.out.println("[" + dateFormater.format(new Date()) + "] \u001B[31mERROR:\u001B[0m " + errorMessage + " \u001B[31mTerminating Execution ...\u001B[0m");
        System.exit(0);
    }

    public void replyWithSuccess(){
        System.out.println("[" + dateFormater.format(new Date()) + "] \u001B[32mSUCCESS:\u001B[0m File Assertion Report Generated ! File Comparisson Terminated! Terminating Comparator ...");
        System.exit(1);
    }

    public void printInfo(String message){
        System.out.println("[" + dateFormater.format(new Date()) + "] \u001B[34mINFO:\u001B[0m " + message);
    }

    public void banner(){
        System.out.println("\n\u001B[42m### FileComparer (c) João Duarte ###\u001B[0m\n");
        System.out.println(">>> Computer: " + userData.getComputerName() + " <<<");
        System.out.println(">>> \u001B[32mWelcome, " + userData.getLoggedUser() + "\u001B[0m <<<\n");
    }
}
