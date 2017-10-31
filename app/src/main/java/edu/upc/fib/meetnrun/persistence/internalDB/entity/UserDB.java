package edu.upc.fib.meetnrun.persistence.internalDB.entity;

/**
 * Created by Awais Iqbal on 31/10/2017.
 */

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import android.provider.BaseColumns;

import edu.upc.fib.meetnrun.models.User;

@Entity(indices = {@Index(value = {"username"},
        unique = true)}, tableName = UserDB.TABLE_NAME)
public class UserDB {

    /** The name of the Cheese table. */
    public static final String TABLE_NAME = "users";

    /** The name of the ID column. */
    public static final String COLUMN_ID = BaseColumns._ID;

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo( name = COLUMN_ID)
    private Integer id;
    protected String username;
    protected String firstName;
    protected String lastName;
    protected String postalCode;
    protected String question;
    private String answer;
    private String password;

    public UserDB( String username, String firstName, String lastName, String postalCode, String question, String answer, String password) {
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.postalCode = postalCode;
        this.question = question;
        this.answer = answer;
        this.password = password;
    }

    public UserDB(User u){
        super();
        this.id=u.getId();
        this.username=u.getUsername();
        this.firstName=u.getFirstName();
        this.lastName=u.getLastName();
        this.postalCode=u.getPostalCode();
        this.question=u.getQuestion();
    }


    public User toGenericModel(){
        return new User(id,username,firstName,lastName,postalCode,question);
    }


    public Integer getId() {
        return id;
    }


    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "UserDB{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", postalCode='" + postalCode + '\'' +
                ", question='" + question + '\'' +
                ", answer='" + answer + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
