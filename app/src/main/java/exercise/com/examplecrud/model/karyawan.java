package exercise.com.examplecrud.model;

import java.util.List;

public class karyawan {
    private boolean status;
    private List<Data> data;
    private String message;
    public boolean isStatus() {
        return status;
    }

    public karyawan setStatus(boolean status) {
        this.status = status;
        return this;
    }


    public List<Data> getData() {
        return data;
    }

    public Data getDataItem() {
        return new Data();
    }


    public karyawan setData(List<Data> data) {
        this.data = data;
        return this;
    }


    public String getMessage() {
        return message;
    }

    public karyawan setMessage(String message) {
        this.message = message;
        return this;
    }


    public class Data {
        private String id="";
        private String name="";
        private String gender="";
        private String birthplace="";
        private String birthdate="";


        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

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



        public Data() {
        }

    }

    public karyawan() {
    }



}
