package nl.raymondloman.resume.domain.personal;

import java.time.LocalDate;
import java.util.Locale;

import nl.raymondloman.resume.Constants;
import nl.raymondloman.resume.domain.enums.DrivingLicense;
import nl.raymondloman.resume.domain.enums.MaritalStatus;
import nl.raymondloman.resume.domain.other.Node;
import nl.raymondloman.resume.visitor.Visitor;

public class Personalia extends Node {

   private String firstNameLastName;
   private LocalDate dateOfBirth;
   private String addressHouseNumber;
   private String zipCode;
   private String city;
   private String telephone;
   private String mobile;
   private String email;
   private String url;
   private MaritalStatus maritalStatus;
   private byte children;
   private DrivingLicense drivingLicense;
   private Locale country;

   public String getFirstNameLastName() {
      return this.firstNameLastName;
   }

   public LocalDate getDateOfBirth() {
      return this.dateOfBirth;
   }

   public void setDateOfBirth(LocalDate dateOfBirth) {
      this.dateOfBirth = dateOfBirth;
   }

   public String getAddressHouseNumber() {
      return this.addressHouseNumber;
   }

   public void setAddressHouseNumber(String addressHouseNumber) {
      this.addressHouseNumber = addressHouseNumber;
   }

   public void setFirstNameLastName(String firstNameLastName) {
      this.firstNameLastName = firstNameLastName;
   }

   public String getEmail() {
      return this.email;
   }

   public void setEmail(String email) {
      this.email = email;
   }

   public String getZipCode() {
      return this.zipCode;
   }

   public void setZipCode(String zipCode) {
      this.zipCode = zipCode;
   }

   public String getCity() {
      return this.city;
   }

   public void setCity(String city) {
      this.city = city;
   }

   public String getTelephone() {
      return this.telephone;
   }

   public void setTelephone(String telephone) {
      this.telephone = telephone;
   }

   public String getMobile() {
      return this.mobile;
   }

   public void setMobile(String mobile) {
      this.mobile = mobile;
   }

   public String getUrl() {
      return this.url;
   }

   public void setUrl(String url) {
      this.url = url;
   }

   public byte getChildren() {
      return this.children;
   }

   public void setChildren(byte children) {
      this.children = children;
   }

   public Locale getCountry() {
      return this.country;
   }

   public void setCountry(String country) {
      this.country = new Locale(Constants.EMPTYSTRING, country);
   }

   public MaritalStatus getMaritalStatus() {
      return this.maritalStatus;
   }

   public void setMaritalStatus(MaritalStatus maritalStatus) {
      this.maritalStatus = maritalStatus;
   }

   public DrivingLicense getDrivingLicense() {
      return this.drivingLicense;
   }

   public void setDrivingLicense(DrivingLicense drivingLicense) {
      this.drivingLicense = drivingLicense;
   }

   @Override
   public String toString() {
      return "- Personalia [firstNameLastName=" + this.firstNameLastName + ", addressHouseNumber=" + this.addressHouseNumber + ", zipCode=" + this.zipCode + ", city=" + this.city + "]";
   }

   @Override
   public void accept(Visitor visitor) {
      visitor.visit(this);
   }
}