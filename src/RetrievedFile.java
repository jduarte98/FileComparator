/**
 * RetirvedFile Model Class
 */
public class RetrievedFile {
    /**
     * File Name
     */
    private final String fileName;

    /**
     * File Last Modification Date
     */
    private final String lastModifiedDate;

    /**
     * File Size
     */
    private final double fileSize;

    /**
     * Class Constructir
     * @param fileName File Name.
     * @param lastModifiedDate Last Modified Date.
     * @param fileSize File Size.
     */
    public RetrievedFile(String fileName, String lastModifiedDate, double fileSize) {
        this.fileName = fileName;
        this.lastModifiedDate = lastModifiedDate;
        this.fileSize = fileSize;
    }

    /**
     * Get the file name.
     * @return File name.
     */
    public String getFileName() {
        return fileName;
    }

    /**
     * Gets the last modified date.
     * @return File Last Modified Date.
     */
    public String getLastModifiedDate() {
        return lastModifiedDate;
    }

    /**
     * Gets the File Size.
     * @return File Size.
     */
    public double getFileSize() {
        return fileSize;
    }

    /**
     * Prints the class as a String.
     * @return Class RetrievedFile as a String.
     */
    @Override
    public String toString() {
        return "\nFile:\n  Name: " + this.fileName + "\n  Last Modified: " + this.lastModifiedDate + "\n  Size: " + this.fileSize;
    }
}
