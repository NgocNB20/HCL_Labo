package jp.co.itechh.quad.admin.base.exception;

import jp.co.itechh.quad.admin.base.application.FacesMessage;

public class ValidatorException extends FacesException {

    private static final long serialVersionUID = 1L;

    private FacesMessage facesMessage = null;

    private String messageId;

    private Object[] args;

    public ValidatorException(FacesMessage facesMessage) {
        this(facesMessage, null);
    }

    public ValidatorException(FacesMessage facesMessage, Throwable cause) {
        super(facesMessage.getDetail(), cause);
        this.facesMessage = facesMessage;
    }

    public ValidatorException(FacesMessage facesMessage, String messageId, Object[] args) {
        this(facesMessage);
        this.messageId = messageId;
        this.args = args;
    }

    public ValidatorException(FacesMessage facesMessage, Throwable cause, String messageId, Object[] args) {
        this(facesMessage, cause);
        this.messageId = messageId;
        this.args = args;
    }

    public FacesMessage getFacesMessage() {
        return facesMessage;
    }

    public String getMessageId() {
        return messageId;
    }

    public Object[] getArgs() {
        return args;
    }

}