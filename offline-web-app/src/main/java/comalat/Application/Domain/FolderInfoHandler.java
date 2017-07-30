package comalat.Application.Domain;

import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author SyleSakis
 */
public interface FolderInfoHandler {

    public long getSize();
    public int getNoOfUnits();
    @XmlTransient
    public long getLastUpdate();
    public Object readFromFolder(String sourcePath);
    
}
