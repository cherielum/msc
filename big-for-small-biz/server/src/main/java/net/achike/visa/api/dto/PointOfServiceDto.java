package net.achike.visa.api.dto;

public class PointOfServiceDto {

    private String motoECIIndicator;
    private String panEntryMode;
    private String posConditionCode;
    
    public String getMotoECIIndicator() {
        return motoECIIndicator;
    }
    public void setMotoECIIndicator(String motoECIIndicator) {
        this.motoECIIndicator = motoECIIndicator;
    }
    public String getPanEntryMode() {
        return panEntryMode;
    }
    public void setPanEntryMode(String panEntryMode) {
        this.panEntryMode = panEntryMode;
    }
    public String getPosConditionCode() {
        return posConditionCode;
    }
    public void setPosConditionCode(String posConditionCode) {
        this.posConditionCode = posConditionCode;
    }
}
