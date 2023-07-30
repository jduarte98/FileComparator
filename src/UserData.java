import java.net.InetAddress;

/**
 * UserData Model Class
 */
public class UserData {
    /**
     * Logged User
     */
    private final String loggedUser;

    /**
     * Computer Name
     */
    private final String computerName;

    /**
     * Class Constructor
     * @param loggedUser Logged User.
     */
    public UserData(String loggedUser) {
        this.loggedUser = loggedUser;
        this.computerName = resolveComputerName();
    }

    /**
     * Gets the Logged User.
     * @return Logged User.
     */
    public String getLoggedUser() {
        return loggedUser;
    }

    /**
     * Gets the Machine Name.
     * @return Computer Name.
     */
    public String getComputerName() {
        return computerName;
    }

    /**
     * Obtains, from the system, the computer name.
     * @return Computer Name.
     */
    private String resolveComputerName(){
        try{
            return InetAddress.getLocalHost().getHostName();
        }catch (Exception e){
            return "-";
        }
    }
}
