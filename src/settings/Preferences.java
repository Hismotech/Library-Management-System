package settings;

import notification.AlertMaker;
import com.google.gson.Gson;
import org.apache.commons.codec.digest.DigestUtils;

import java.io.*;

public class Preferences {
    public static final String configFile  = "Config.txt";

    int bookDays;
    float finePerDay;
    String userName;
    String passWord;

    public Preferences() {
        bookDays   = 7;
        finePerDay =100;
        userName   = "admin";
      setPassWord("admin");
    }

    public int getBookDays() {
        return bookDays;
    }

    public void setBookDays(int bookDays) {
        this.bookDays = bookDays;
    }

    public float getFinePerDay() {
        return finePerDay;
    }

    public void setFinePerDay(float finePerDay) {
        this.finePerDay = finePerDay;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassWord() {
        return passWord;
    }

    public void setPassWord(String passWord) {
        if(passWord.length()<16) {
            this.passWord = DigestUtils.sha1Hex((passWord));
        }
        else{
            this.passWord =  passWord;
        }
    }


    public static void initConfig() {
        Writer wr = null;
        try {
            Preferences preferences = new Preferences();
            Gson        gson        = new Gson();
            wr = new FileWriter(configFile);
            gson.toJson(preferences, wr);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            try {
                wr.close();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
        }



public static Preferences getPreferences() {
    Gson   gson       = new Gson();
    Preferences preference = new Preferences();
    try {
        preference = gson.fromJson(new FileReader(configFile), Preferences.class);
    }
    catch (FileNotFoundException e) {
        initConfig();
        e.printStackTrace();
    }
    return preference;
}

    public static void updatePreference(Preferences preference) {
        Writer wr = null;
        try {
            Gson        gson        = new Gson();
            wr = new FileWriter(configFile);
            gson.toJson(preference, wr);
            AlertMaker.showInformation("Success","Settings Updated Successfully");
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            try {
                wr.close();
            }
            catch (IOException e) {
               AlertMaker.showErrorMessage(e,"Failure","Error in Saving File");
            }
        }
    }

}


