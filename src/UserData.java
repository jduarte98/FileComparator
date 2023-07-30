import java.net.InetAddress;

public class UserData {
    private String loggedUser, computerName;

    public UserData(String loggedUser) {
        this.loggedUser = loggedUser;
        this.computerName = resolveComputerName();
    }

    public String getLoggedUser() {
        return loggedUser;
    }

    public String getComputerName() {
        return computerName;
    }

    private String resolveComputerName(){
        try{
            return InetAddress.getLocalHost().getHostName();
        }catch (Exception e){
            return "-";
        }
    }
}
