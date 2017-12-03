package net.achike.visa.client;

public class SuccessfulTransaction {
    
    private String transactionIdentifier;
    
    private String actionCode;
    private String approvalCode;
    
    private String responseCode;
    private String transmissionDateTime;
    
    public String getTransactionIdentifier() {
        return transactionIdentifier;
    }
    public void setTransactionIdentifier(String transactionIdentifier) {
        this.transactionIdentifier = transactionIdentifier;
    }
    public String getActionCode() {
        return actionCode;
    }
    public void setActionCode(String actionCode) {
        this.actionCode = actionCode;
    }
    public String getApprovalCode() {
        return approvalCode;
    }
    public void setApprovalCode(String approvalCode) {
        this.approvalCode = approvalCode;
    }
    public String getResponseCode() {
        return responseCode;
    }
    public void setResponseCode(String responseCode) {
        this.responseCode = responseCode;
    }
    public String getTransmissionDateTime() {
        return transmissionDateTime;
    }
    public void setTransmissionDateTime(String transmissionDateTime) {
        this.transmissionDateTime = transmissionDateTime;
    }
}
