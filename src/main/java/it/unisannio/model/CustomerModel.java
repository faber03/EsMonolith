package it.unisannio.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class CustomerModel implements Serializable {

	private String CF;
	private String firstName;
	private String lastName;
	private List<AccountModel> accounts;

	public CustomerModel(String cf, String firstName , String lastName) {
		this.CF = cf;
		this.firstName = firstName;
		this.lastName = lastName;
		accounts = new ArrayList<AccountModel>();
	}
	
	public void AddAccount(AccountModel account)
	{
		accounts.add(account);
	}
	
	public CustomerModel() {

	}

	public String getCF() {
		return this.CF;
	}

	public void setCF(String CF) {
		this.CF = CF;
	}
	public String getFirstName() {
		return this.firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	
	public String getLastName() {
		return this.lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
		
	public List<AccountModel> getAccounts() {
		accounts.size();
		return this.accounts;
	}

	public void setAccount(List<AccountModel> accounts) {
		this.accounts = accounts;
	}
}
