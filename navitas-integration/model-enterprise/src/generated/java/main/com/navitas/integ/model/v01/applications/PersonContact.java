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
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.*;

/**
 * PersonContact
 */
@javax.annotation.Generated(value = "io.swagger.codegen.languages.JavaJerseyServerCodegen", date = "2017-07-06T15:55:24.977+10:00")
public class PersonContact   {
  @JsonProperty("personalEmailAddress")
  private String personalEmailAddress = null;

  @JsonProperty("homePhone")
  private String homePhone = null;

  @JsonProperty("mobile")
  private String mobile = null;

  public PersonContact personalEmailAddress(String personalEmailAddress) {
    this.personalEmailAddress = personalEmailAddress;
    return this;
  }

   /**
   * Get personalEmailAddress
   * @return personalEmailAddress
  **/
  @JsonProperty("personalEmailAddress")
  @ApiModelProperty(value = "")
  public String getPersonalEmailAddress() {
    return personalEmailAddress;
  }

  public void setPersonalEmailAddress(String personalEmailAddress) {
    this.personalEmailAddress = personalEmailAddress;
  }

  public PersonContact homePhone(String homePhone) {
    this.homePhone = homePhone;
    return this;
  }

   /**
   * Get homePhone
   * @return homePhone
  **/
  @JsonProperty("homePhone")
  @ApiModelProperty(value = "")
  public String getHomePhone() {
    return homePhone;
  }

  public void setHomePhone(String homePhone) {
    this.homePhone = homePhone;
  }

  public PersonContact mobile(String mobile) {
    this.mobile = mobile;
    return this;
  }

   /**
   * Get mobile
   * @return mobile
  **/
  @JsonProperty("mobile")
  @ApiModelProperty(value = "")
  public String getMobile() {
    return mobile;
  }

  public void setMobile(String mobile) {
    this.mobile = mobile;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    PersonContact personContact = (PersonContact) o;
    return Objects.equals(this.personalEmailAddress, personContact.personalEmailAddress) &&
        Objects.equals(this.homePhone, personContact.homePhone) &&
        Objects.equals(this.mobile, personContact.mobile);
  }

  @Override
  public int hashCode() {
    return Objects.hash(personalEmailAddress, homePhone, mobile);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class PersonContact {\n");
    
    sb.append("    personalEmailAddress: ").append(toIndentedString(personalEmailAddress)).append("\n");
    sb.append("    homePhone: ").append(toIndentedString(homePhone)).append("\n");
    sb.append("    mobile: ").append(toIndentedString(mobile)).append("\n");
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

