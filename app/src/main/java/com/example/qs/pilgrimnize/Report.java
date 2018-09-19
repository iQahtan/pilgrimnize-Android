package com.example.qs.pilgrimnize;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Report implements Parcelable {


        String describe;
        int phnoeNumber;
        int officeNumber;

        List<Double> location;
        String locationName;

        String uid;
        int activicaion;
        String reportedUser;

        String userInCharge;
        String employReport;
        Long endDate;
        Long createionDate;

    public String getDescribe() {
        return describe;
    }

    public int getPhnoeNumber() {
        return phnoeNumber;
    }

    public int getOfficeNumber() {
        return officeNumber;
    }

    public List<Double> getLocation() {
        return location;
    }

    public String getLocationName() {
        return locationName;
    }

    public String getUid() {
        return uid;
    }

    public int getActivicaion() {
        return activicaion;
    }

    public String getReportedUser() {
        return reportedUser;
    }

    public String getUserInCharge() {
        return userInCharge;
    }

    public String getEmployReport() {
        return employReport;
    }

    public Long getEndDate() {
        return endDate;
    }

    public Long getCreateionDate() {
        return createionDate;
    }

    public static Creator<Report> getCREATOR() {
        return CREATOR;
    }


        public Report(int phoneNumber,String describe, int activetion) {
        this.phnoeNumber = phoneNumber;
        this.describe = describe;
        this.activicaion = activetion;
    }

    public Report(String uid , Map<String ,Object> map){
        this.uid = uid;

        this.createionDate = (Long) map.get("creationDate");
        this.location = (List) map.get("location");

        Long officeNumber = (Long) map.get("officeNumber");
        this.officeNumber = officeNumber.intValue();
//
        Long phone = (Long) map.get("phoneNum");
        this.phnoeNumber = phone.intValue();
//
        Long active = (Long) map.get("active");
        this.activicaion = active.intValue();

        this.describe = (String) map.get("describion");
        this.locationName = (String) map.get("locationName");
        this.reportedUser = (String) map.get("reportedUserUid");
        this.endDate = (Long) map.get("endDate");

    }
//    public Report( Map<String ,Object> map){
//        for (String k : map.keySet()) {
//            Map<String,Object> object = (Map<String, Object>) map.get(k);
//            this.createionDate = (Long) object.get("creationDate");
//            this.location = (List) object.get("location");
//
//            Long officeNumber = (Long) object.get("officeNumber");
//            this.officeNumber = officeNumber.intValue();
//
//            Long phone = (Long) object.get("phoneNum");
//            this.phnoeNumber = phone.intValue();
//
//            this.describe = (String) object.get("describion");
//            this.locationName = (String) object.get("locationName");
//
//        }
////        this.uid = uid;
//    }
    @Exclude
    /// REtrev Report from FIR
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
//        result.put("uid", uid);

        result.put("phoneNum", phnoeNumber);
        result.put("describion", describe);
        result.put("officeNumber", officeNumber);
        result.put("location", location);
        result.put("creationDate",createionDate);
        result.put("active",activicaion);
        result.put("reportedUserUid",reportedUser);
        result.put("locationName",locationName);

        return result;
    }
    ///=================PARCEL
    public Report(Parcel in) {
        describe = in.readString();
        phnoeNumber = in.readInt();
        officeNumber = in.readInt();
        reportedUser = in.readString();
        location = in.readArrayList(Double.class.getClassLoader());
        locationName = in.readString();
        uid = in.readString();
        reportedUser = in.readString();
        createionDate = in.readLong();
        endDate = in.readLong();

    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
// Write data in any order
        dest.writeString(describe);
        dest.writeInt(phnoeNumber);
        dest.writeInt(officeNumber);
        dest.writeString(reportedUser);
        dest.writeList(location);
        dest.writeString(locationName);
        dest.writeString(uid);
        dest.writeString(reportedUser);
        dest.writeLong(createionDate);
        dest.writeLong(endDate);
    }
    public static final Parcelable.Creator<Report> CREATOR = new Parcelable.Creator<Report>(){
        public Report createFromParcel(Parcel in) {
            return new Report(in);
        }

        public Report[] newArray(int size) {
            return new Report[size];
        }
    };
}
