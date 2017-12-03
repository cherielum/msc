package net.achike.visa.entity;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "card")
public class Card implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 711688273836654445L;

    @Id
    @GeneratedValue
    private Integer id;
    
    @Column(name="card_number")
    private String cardNumber;
    
    @Column(name="card_expiration_month")
    private String cardExpirationMonth;
    
    @Column(name="card_expiration_year")
    private String cardExpirationYear;

    @ManyToOne(fetch = FetchType.EAGER, targetEntity=User.class)
    @JoinColumn(name="user_id")
    private User user;
    
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getCardExpirationMonth() {
        return cardExpirationMonth;
    }

    public void setCardExpirationMonth(String cardExpirationMonth) {
        this.cardExpirationMonth = cardExpirationMonth;
    }

    public String getCardExpirationYear() {
        return cardExpirationYear;
    }

    public void setCardExpirationYear(String cardExpirationYear) {
        this.cardExpirationYear = cardExpirationYear;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
