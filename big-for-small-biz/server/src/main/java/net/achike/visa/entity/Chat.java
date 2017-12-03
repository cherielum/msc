package net.achike.visa.entity;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "chat")
public class Chat implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = -3077893948127977642L;

    @Id
    @GeneratedValue
    private Integer id;
    
    @Column(name="request")
    private String request;
    
    @Column(name="response")
    private String response;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getRequest() {
        return request;
    }

    public void setRequest(String request) {
        this.request = request;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }
}
