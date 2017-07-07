/*
 * Navitas Partner API
 * This is a description of the Navitas Partner API and the available RESTful endpoints within. From here you can discover what 
 *
 * OpenAPI spec version: 1.0.0
 * Contact: d.bastide@leonardo.com.au
 *
 * NOTE: This class is auto generated by the swagger code generator program.
 * https://github.com/swagger-api/swagger-codegen.git
 * Do not edit the class manually.
 */


package com.navitas.integ.model.v01.applications;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.navitas.integ.model.v01.applications.PersonContact;
import com.navitas.integ.model.v01.applications.PersonDemographic;
import com.navitas.integ.model.v01.applications.PersonHomeAddress;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.*;

/**
 * Person
 */
@javax.annotation.Generated(value = "io.swagger.codegen.languages.JavaJerseyServerCodegen", date = "2017-07-06T15:55:24.977+10:00")
public class Person   {
  @JsonProperty("personIdentifier")
  private String personIdentifier = null;

  @JsonProperty("Demographic")
  private PersonDemographic demographic = null;

  @JsonProperty("Contact")
  private PersonContact contact = null;

  @JsonProperty("HomeAddress")
  private PersonHomeAddress homeAddress = null;

  @JsonProperty("profilePhoto")
  private String profilePhoto = null;

  public Person personIdentifier(String personIdentifier) {
    this.personIdentifier = personIdentifier;
    return this;
  }

   /**
   * Get personIdentifier
   * @return personIdentifier
  **/
  @JsonProperty("personIdentifier")
  @ApiModelProperty(value = "")
  public String getPersonIdentifier() {
    return personIdentifier;
  }

  public void setPersonIdentifier(String personIdentifier) {
    this.personIdentifier = personIdentifier;
  }

  public Person demographic(PersonDemographic demographic) {
    this.demographic = demographic;
    return this;
  }

   /**
   * Get demographic
   * @return demographic
  **/
  @JsonProperty("Demographic")
  @ApiModelProperty(value = "")
  public PersonDemographic getDemographic() {
    return demographic;
  }

  public void setDemographic(PersonDemographic demographic) {
    this.demographic = demographic;
  }

  public Person contact(PersonContact contact) {
    this.contact = contact;
    return this;
  }

   /**
   * Get contact
   * @return contact
  **/
  @JsonProperty("Contact")
  @ApiModelProperty(value = "")
  public PersonContact getContact() {
    return contact;
  }

  public void setContact(PersonContact contact) {
    this.contact = contact;
  }

  public Person homeAddress(PersonHomeAddress homeAddress) {
    this.homeAddress = homeAddress;
    return this;
  }

   /**
   * Get homeAddress
   * @return homeAddress
  **/
  @JsonProperty("HomeAddress")
  @ApiModelProperty(value = "")
  public PersonHomeAddress getHomeAddress() {
    return homeAddress;
  }

  public void setHomeAddress(PersonHomeAddress homeAddress) {
    this.homeAddress = homeAddress;
  }

  public Person profilePhoto(String profilePhoto) {
    this.profilePhoto = profilePhoto;
    return this;
  }

   /**
   * Get profilePhoto
   * @return profilePhoto
  **/
  @JsonProperty("profilePhoto")
  @ApiModelProperty(value = "")
  public String getProfilePhoto() {
    return profilePhoto;
  }

  public void setProfilePhoto(String profilePhoto) {
    this.profilePhoto = profilePhoto;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Person person = (Person) o;
    return Objects.equals(this.personIdentifier, person.personIdentifier) &&
        Objects.equals(this.demographic, person.demographic) &&
        Objects.equals(this.contact, person.contact) &&
        Objects.equals(this.homeAddress, person.homeAddress) &&
        Objects.equals(this.profilePhoto, person.profilePhoto);
  }

  @Override
  public int hashCode() {
    return Objects.hash(personIdentifier, demographic, contact, homeAddress, profilePhoto);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Person {\n");
    
    sb.append("    personIdentifier: ").append(toIndentedString(personIdentifier)).append("\n");
    sb.append("    demographic: ").append(toIndentedString(demographic)).append("\n");
    sb.append("    contact: ").append(toIndentedString(contact)).append("\n");
    sb.append("    homeAddress: ").append(toIndentedString(homeAddress)).append("\n");
    sb.append("    profilePhoto: ").append(toIndentedString(profilePhoto)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String toIndentedString(java.lang.Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }
}
