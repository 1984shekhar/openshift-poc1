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
 * Agent
 */
@javax.annotation.Generated(value = "io.swagger.codegen.languages.JavaJerseyServerCodegen", date = "2017-07-06T15:55:24.977+10:00")
public class Agent   {
  @JsonProperty("agentCode")
  private String agentCode = null;

  @JsonProperty("agentCountry")
  private String agentCountry = null;

  public Agent agentCode(String agentCode) {
    this.agentCode = agentCode;
    return this;
  }

   /**
   * Get agentCode
   * @return agentCode
  **/
  @JsonProperty("agentCode")
  @ApiModelProperty(value = "")
  public String getAgentCode() {
    return agentCode;
  }

  public void setAgentCode(String agentCode) {
    this.agentCode = agentCode;
  }

  public Agent agentCountry(String agentCountry) {
    this.agentCountry = agentCountry;
    return this;
  }

   /**
   * Get agentCountry
   * @return agentCountry
  **/
  @JsonProperty("agentCountry")
  @ApiModelProperty(value = "")
  public String getAgentCountry() {
    return agentCountry;
  }

  public void setAgentCountry(String agentCountry) {
    this.agentCountry = agentCountry;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Agent agent = (Agent) o;
    return Objects.equals(this.agentCode, agent.agentCode) &&
        Objects.equals(this.agentCountry, agent.agentCountry);
  }

  @Override
  public int hashCode() {
    return Objects.hash(agentCode, agentCountry);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Agent {\n");
    
    sb.append("    agentCode: ").append(toIndentedString(agentCode)).append("\n");
    sb.append("    agentCountry: ").append(toIndentedString(agentCountry)).append("\n");
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
