package com.example.qs.pilgrimnize;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class User implements Parcelable {

    int userType;
    String userClass;
    String uid;
    String username;
    List<Double> location;
    int officeNumber;

    public User() {

    }
    public User(int userType){
        this.userType = userType;

        switch (userType) {
            case 0: this.userClass = "Admin";
                break;
            case 1: this.userClass = "Employ";
                break;
            case 2: this.userClass = "Customer";
                break;
        }
    }

    public User(String uid , Map<String ,Object> map){
        this.uid = uid;
        this.username = (String) map.get("username");
        this.location = (List) map.get("location");
        Long officeNumber = (Long) map.get("officeNumber");
        this.officeNumber = officeNumber.intValue();
        Long userType = (Long) map.get("userType");
        this.userType = userType.intValue();
    }

    @Override
    public String toString() {
        return "User{" +
                "userType=" + userType +
                ", userClass='" + userClass + '\'' +
                ", uid='" + uid + '\'' +
                ", username='" + username + '\'' +
                ", location=" + location +
                ", officeNumber=" + officeNumber +
                '}';
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
//        result.put("uid", uid);
        result.put("userType", userType);
        result.put("username", username);
        result.put("officeNumber", officeNumber);
        result.put("location", location);
        return result;
    }
    ///=================PARCEL
//    int userType;
//    String userClass;
//    String uid;
//    String username;
//    List<Double> location;
//    int officeNumber;
    public User(Parcel in) {
        userType = in.readInt();
        userClass = in.readString();
        uid = in.readString();
        username = in.readString();
        location = in.readArrayList(Double.class.getClassLoader());
        officeNumber = in.readInt();
    }
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
// Write data in any order
        dest.writeInt(userType);
        dest.writeString(userClass);
        dest.writeString(uid);
        dest.writeString(username);
        dest.writeList(location);
        dest.writeInt(officeNumber);
    }
    public static final Parcelable.Creator<User> CREATOR = new Parcelable.Creator<User>(){
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        public User[] newArray(int size) {
            return new User[size];
        }
    };
}
//public class Main {
//    public static void main(String[] args) {
//        String filename = "time.ser";
////        User p = new User("Lars", "Vogel");
//
//        // save the object to file
//        FileOutputStream fos = null;
//        ObjectOutputStream out = null;
//        try {
//            fos = new FileOutputStream(filename);
//            out = new ObjectOutputStream(fos);
//            out.writeObject(p);
//
//            out.close();
//        } catch (Exception ex) {
//            ex.printStackTrace();
//        }
//        // read the object from file
//        // save the object to file
//        FileInputStream fis = null;
//        ObjectInputStream in = null;
//        try {
//            fis = new FileInputStream(filename);
//            in = new ObjectInputStream(fis);
//            p = (User) in.readObject();
//            in.close();
//        } catch (Exception ex) {
//            ex.printStackTrace();
//        }
//        System.out.println(p);
//    }
//}