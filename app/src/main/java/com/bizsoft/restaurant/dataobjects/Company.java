package com.bizsoft.restaurant.dataobjects;

/**
 * Created by GopiKing on 23-11-2017.
 */

public class Company {
    Long Id;
    String CompanyName;
    String CompanyType;

    String AddressLine1;
    String AddressLine2;
    String PostalCode;
    String TelephoneNo;
    String EMailId;

    public Company() {

        CompanyName = "Denariu Soft BHD";

        AddressLine1 = "Suite no:25 - 10,Centro Mall,Batu Tiga Lama,";
        AddressLine2 = "41300 Klang Selango,Darul Ehsan,Malaysia ";
        PostalCode = "41300";
        TelephoneNo = "+60 14-648 8869";
        this.EMailId = "info@denariusoft.com";
        this.GSTNo = "4564 4564 7894 ";
    }

    public Long getId() {
        return Id;
    }

    public void setId(Long id) {
        Id = id;
    }

    public String getCompanyName() {
        return CompanyName;
    }

    public void setCompanyName(String companyName) {
        CompanyName = companyName;
    }

    public String getCompanyType() {
        return CompanyType;
    }

    public void setCompanyType(String companyType) {
        CompanyType = companyType;
    }

    public String getAddressLine1() {
        return AddressLine1;
    }

    public void setAddressLine1(String addressLine1) {
        AddressLine1 = addressLine1;
    }

    public String getAddressLine2() {
        return AddressLine2;
    }

    public void setAddressLine2(String addressLine2) {
        AddressLine2 = addressLine2;
    }

    public String getPostalCode() {
        return PostalCode;
    }

    public void setPostalCode(String postalCode) {
        PostalCode = postalCode;
    }

    public String getTelephoneNo() {
        return TelephoneNo;
    }

    public void setTelephoneNo(String telephoneNo) {
        TelephoneNo = telephoneNo;
    }

    public String getEMailId() {
        return EMailId;
    }

    public void setEMailId(String EMailId) {
        this.EMailId = EMailId;
    }

    public String getGSTNo() {
        return GSTNo;
    }

    public void setGSTNo(String GSTNo) {
        this.GSTNo = GSTNo;
    }

    String GSTNo;
}
