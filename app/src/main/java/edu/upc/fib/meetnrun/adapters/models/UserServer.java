package edu.upc.fib.meetnrun.adapters.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import edu.upc.fib.meetnrun.models.User;

public class UserServer implements IServerModel {

  @SerializedName("id")
  @Expose
  private Integer id;
  @SerializedName("username")
  @Expose
  private String username;
  @SerializedName("first_name")
  @Expose
  private String firstName;
  @SerializedName("last_name")
  @Expose
  private String lastName;
  @SerializedName("postal_code")
  @Expose
  private String postalCode;
  @SerializedName("question")
  @Expose
  private String question;
  @SerializedName("level")
  @Expose
  private Integer level;

  /**
   * No args constructor for use in serialization
   */
  public UserServer() {
  }

  public UserServer(String username, String firstName, String lastName, String postalCode, String question, Integer level) {
    this.username = username;
    this.firstName = firstName;
    this.lastName = lastName;
    this.postalCode = postalCode;
    this.question = question;
    this.level = level;
  }

  public UserServer(Integer id, String username, String firstName, String lastName, String postalCode, String question, Integer level) {
    this.id = id;
    this.username = username;
    this.firstName = firstName;
    this.lastName = lastName;
    this.postalCode = postalCode;
    this.question = question;
    this.level = level;
  }

  public UserServer(User u) {
    super();
    this.id = u.getId();
    this.username = u.getUsername();
    this.firstName = u.getFirstName();
    this.lastName = u.getLastName();
    this.postalCode = u.getPostalCode();
    this.question = u.getQuestion();
    this.level = u.getLevel();
  }


  public User toGenericModel() {
    return new User(id, username, firstName, lastName, postalCode, question, level);
  }

  public String getUsername() {
    return username;
  }
}
