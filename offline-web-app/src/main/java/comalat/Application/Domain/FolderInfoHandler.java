package comalat.Application.Domain;

/**
 *
 * @author SyleSakis
 */
public interface FolderInfoHandler {

    public long getSize();
    public Object readFromFolder(String sourcePath);
}
