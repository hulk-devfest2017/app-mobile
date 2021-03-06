package sqli.com.hulkchallenge.model;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created by renaud on 16/06/17.
 */

public class Player implements Serializable{

    private String lastName;
    private String firstName;
    private String email;
    private String twitter;
    private String company;
    private boolean genderMale = true;

    public Player() {
    }

    public Player(String lastName, String firstName, String email, String twitter, String company, boolean genderMale) {
        this.lastName = lastName;
        this.firstName = firstName;
        this.email = email;
        this.twitter = twitter;
        this.company = company;
        this.genderMale = genderMale;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTwitter() {
        return twitter;
    }

    public void setTwitter(String twitter) {
        this.twitter = twitter;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public boolean isGenderMale() {
        return genderMale;
    }

    public void setGenderMale(boolean genderMale) {
        this.genderMale = genderMale;
    }

    public JSONObject toJson(){
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("firstName",getFirstName());
            jsonObject.put("lastName",getLastName());
            jsonObject.put("companyName",getCompany());
            jsonObject.put("emailAddress",getEmail());
            jsonObject.put("twitterAccount",getTwitter());
            jsonObject.put("genderMale", isGenderMale());
        } catch (JSONException e) {
            System.err.print("erreur de serialisation json");
        }
        return jsonObject;
    }
}
