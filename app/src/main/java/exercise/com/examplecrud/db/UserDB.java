package exercise.com.examplecrud.db;

import com.orm.SugarRecord;

public class UserDB extends SugarRecord {

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getBirthplace() {
        return birthplace;
    }

    public void setBirthplace(String birthplace) {
        this.birthplace = birthplace;
    }

    public String getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(String birthdate) {
        this.birthdate = birthdate;
    }

    private String name;
    private String gender;
    private String birthplace;
    private String birthdate;
    public UserDB() {
    }

    public UserDB(String name, String gender, String birthplace, String birthdate) {
        this.name = name;
        this.gender = gender;
        this.birthplace = birthplace;
        this.birthdate = birthdate;
    }

}
