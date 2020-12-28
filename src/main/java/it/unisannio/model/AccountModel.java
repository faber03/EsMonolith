package it.unisannio.model;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

public class AccountModel implements Serializable {
	
	private int number;
	private double balance;
	private Date lastModified;
	
	private CustomerModel customer;

	public AccountModel(int number, double balance, Date lastModified, CustomerModel customer) {
		this.number = number;
		this.balance = balance;
		this.customer = customer;
		this.lastModified = lastModified;
	}
	
	public AccountModel() {

	}
		
	public double getBalance() {
		return this.balance;
	}

	public void setBalance(double balance) {
		this.balance = balance;
	}
	
	public int getNumber() {
		return this.number;
	}

	public void setNumber(int number) {
		this.number = number;
	}
	
	public CustomerModel getCustomer () {
		return customer;
	}
	
	public void setCustomer(CustomerModel c) {
		customer = c;
	}
	
	public void setLastModified(Date t) {
		lastModified = t;
	}
	
	@JsonFormat(shape=JsonFormat.Shape.STRING, pattern="dd-MM-yyy HH:mm:ss", timezone = JsonFormat.DEFAULT_TIMEZONE)
	public Date getLastModified() {
		return lastModified;
	}
}
