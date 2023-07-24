public class RetrievedFile {
    private String fileName, lastModifiedDate;
    private double fileSize;

    public RetrievedFile(String fileName, String lastModifiedDate, double fileSize) {
        this.fileName = fileName;
        this.lastModifiedDate = lastModifiedDate;
        this.fileSize = fileSize;
    }

    public RetrievedFile() {
    }

    public String getFileName() {
        return fileName;
    }

    public String getLastModifiedDate() {
        return lastModifiedDate;
    }

    public double getFileSize() {
        return fileSize;
    }

    @Override
    public String toString() {
        return "\nFile:\n  Name: " + this.fileName + "\n  Last Modified: " + this.lastModifiedDate + "\n  Size: " + this.fileSize;
    }
}
