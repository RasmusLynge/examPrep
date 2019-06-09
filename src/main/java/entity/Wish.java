package entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import org.mindrot.jbcrypt.BCrypt;

@Entity
@Table(name = "wishes")
public class Wish implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Basic(optional = false)
    @NotNull
    @Column(name = "wish_id", length = 10)
    private String wishID;
    
    @ManyToOne
    private User user;

    public Wish() {

    }

    public Wish(String wishID) {
        this.wishID = wishID;
    }

    public Wish(String wishID, User user) {
        this.wishID = wishID;
        this.user = user;
    }

    public String getWishID() {
        return wishID;
    }

    public void setWishID(String wishID) {
        this.wishID = wishID;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "Wish{" + "wishID=" + wishID + ", user=" + user + '}';
    }

}
