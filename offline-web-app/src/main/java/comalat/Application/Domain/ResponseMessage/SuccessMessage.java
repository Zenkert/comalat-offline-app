package comalat.Application.Domain.ResponseMessage;

import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author SyleSakis
 */
@XmlRootElement
public class SuccessMessage {
    
    private String message;
    private int code;
    private String documantation;

    public SuccessMessage() {
    }
    
    public SuccessMessage(String message, int code, String documantation) {
        this.message = message;
        this.code = code;
        this.documantation = documantation;
    }

    public String getDocumantation() {
        return documantation;
    }

    public void setDocumantation(String documantation) {
        this.documantation = documantation;
    }
    
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}
