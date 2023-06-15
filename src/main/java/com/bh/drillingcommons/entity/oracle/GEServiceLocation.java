package com.bh.drillingcommons.entity.oracle;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class GEServiceLocation implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	@Column(name="LOCATIONID")
	private Long id;
	
	@Column(name="FACILITYNAME")
	private String facilityName;
	
	@Column(name="REGION")
	private String region;
	
	@Column(name="LOCADDR1")
	private String addressLine1;
	
	@Column(name="LOCADDR2")
	private String addressLine2;
	
	@Column(name="LOCADDR3")
	private String addressLine3;
	
	@Column(name="CITY")
	private String city;
	
	@Column(name="ZIP")
	private String zip;
	
	@Column(name="STATE")
	private String state;
	
	@Column(name="COUNTRY")
	private String country;
	
	@Column(name="FIRSTNAME")
	private String contactFirstName;
	
	@Column(name="LASTNAME")
	private String contactLastName;
	
	@Column(name="ROLE")
	private String contactRole;
	
	@Column(name="TELEPHONENUMBER")
	private String contactTelephone;
	
	@Column(name="MOBILENUMBER")
	private String contactMobile;
	
	@Column(name="FAX")
	private String contactFax;
	
	@Column(name="EMAIL")
	private String contactEmail;
	
	@Column(name="GROUPCODE")
	private String groupCode;
	
	@Column(name="LATITUDE")
	private String latitude;
	
	@Column(name="LONGITUDE")
	private String longitude;
	
	@Column(name="LOCALTIME")
	private String localTime;
	
	@Column(name="HRSOFOP")
	private Long hrsOfOp;
	
	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}
	/**
	 * @return the facilityName
	 */
	public String getFacilityName() {
		return facilityName;
	}
	/**
	 * @param facilityName the facilityName to set
	 */
	public void setFacilityName(String facilityName) {
		this.facilityName = facilityName;
	}
	/**
	 * @return the region
	 */
	public String getRegion() {
		return region;
	}
	/**
	 * @param region the region to set
	 */
	public void setRegion(String region) {
		this.region = region;
	}
	/**
	 * @return the addressLine1
	 */
	public String getAddressLine1() {
		return addressLine1;
	}
	/**
	 * @param addressLine1 the addressLine1 to set
	 */
	public void setAddressLine1(String addressLine1) {
		this.addressLine1 = addressLine1;
	}
	/**
	 * @return the addressLine2
	 */
	public String getAddressLine2() {
		return addressLine2;
	}
	/**
	 * @param addressLine2 the addressLine2 to set
	 */
	public void setAddressLine2(String addressLine2) {
		this.addressLine2 = addressLine2;
	}
	/**
	 * @return the addressLine3
	 */
	public String getAddressLine3() {
		return addressLine3;
	}
	/**
	 * @param addressLine3 the addressLine3 to set
	 */
	public void setAddressLine3(String addressLine3) {
		this.addressLine3 = addressLine3;
	}
	/**
	 * @return the city
	 */
	public String getCity() {
		return city;
	}
	/**
	 * @param city the city to set
	 */
	public void setCity(String city) {
		this.city = city;
	}
	/**
	 * @return the state
	 */
	public String getState() {
		return state;
	}
	/**
	 * @param state the state to set
	 */
	public void setState(String state) {
		this.state = state;
	}
	/**
	 * @return the country
	 */
	public String getCountry() {
		return country;
	}
	/**
	 * @param country the country to set
	 */
	public void setCountry(String country) {
		this.country = country;
	}
	/**
	 * @return the contactFirstName
	 */
	public String getContactFirstName() {
		return contactFirstName;
	}
	/**
	 * @param contactFirstName the contactFirstName to set
	 */
	public void setContactFirstName(String contactFirstName) {
		this.contactFirstName = contactFirstName;
	}
	/**
	 * @return the contactLastName
	 */
	public String getContactLastName() {
		return contactLastName;
	}
	/**
	 * @param contactLastName the contactLastName to set
	 */
	public void setContactLastName(String contactLastName) {
		this.contactLastName = contactLastName;
	}
	/**
	 * @return the contactRole
	 */
	public String getContactRole() {
		return contactRole;
	}
	/**
	 * @param contactRole the contactRole to set
	 */
	public void setContactRole(String contactRole) {
		this.contactRole = contactRole;
	}
	/**
	 * @return the contactTelephone
	 */
	public String getContactTelephone() {
		return contactTelephone;
	}
	/**
	 * @param contactTelephone the contactTelephone to set
	 */
	public void setContactTelephone(String contactTelephone) {
		this.contactTelephone = contactTelephone;
	}
	/**
	 * @return the contactMobile
	 */
	public String getContactMobile() {
		return contactMobile;
	}
	/**
	 * @param contactMobile the contactMobile to set
	 */
	public void setContactMobile(String contactMobile) {
		this.contactMobile = contactMobile;
	}
	/**
	 * @return the contactFax
	 */
	public String getContactFax() {
		return contactFax;
	}
	/**
	 * @param contactFax the contactFax to set
	 */
	public void setContactFax(String contactFax) {
		this.contactFax = contactFax;
	}
	/**
	 * @return the contactEmail
	 */
	public String getContactEmail() {
		return contactEmail;
	}
	/**
	 * @param contactEmail the contactEmail to set
	 */
	public void setContactEmail(String contactEmail) {
		this.contactEmail = contactEmail;
	}
	/**
	 * @return the group
	 */
	public String getGroupCode() {
		return groupCode;
	}
	/**
	 * @param group the group to set
	 */
	public void setGroupCode(String groupCode) {
		this.groupCode = groupCode;
	}
	
	public String getZip() {
		return zip;
	}
	public void setZip(String zip) {
		this.zip = zip;
	}
	public String getLatitude() {
		return latitude;
	}
	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}
	public String getLongitude() {
		return longitude;
	}
	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}
	public String getLocalTime() {
		return localTime;
	}
	public void setLocalTime(String localTime) {
		this.localTime = localTime;
	}
	public Long getHrsOfOp() {
		return hrsOfOp;
	}
	public void setHrsOfOp(Long hrsOfOp) {
		this.hrsOfOp = hrsOfOp;
	}
}