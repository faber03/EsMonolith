package it.unisannio.controller;


import java.net.URI;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.RollbackException;
import javax.transaction.SystemException;
import javax.transaction.Transactional;
import javax.transaction.Transactional.TxType;
import javax.transaction.UserTransaction;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

import it.unisannio.model.Account;
import it.unisannio.model.Customer;
import it.unisannio.model.CustomerModel;
import it.unisannio.service.BranchLocal;


@Consumes("text/plain")
@Produces("text/plain")
@Path("/bank")
public class BankController  {

	@EJB
	private BranchLocal branch;

	@Resource UserTransaction utx; // To handle user transactions from a Web component


	public BankController() {
		super();

	}


	@POST
	@Path("/accounts/{accountId}/deposits")
	public Response deposit(@PathParam("accountId") int accountNum, double amount) {
		try {

			branch.deposit(accountNum, amount);

			return Response.ok().build();
		} catch (Exception e) {
			System.out.println(e);
			return Response.status(500).build();
		}
	}


	@POST
	@Path("/accounts/{accountId}/withdraws")
	public Response withdraw(@PathParam("accountId") int accountNum, double amount) {
		try {
			branch.withdraw(accountNum, amount);

			return Response.ok().build();
		} catch (Exception e) {
			System.out.println(e);
			return Response.status(500).build();
		}
	}

	@GET
	@Path("/accounts/{accountId}/")
	public Response getBalance(@PathParam("accountId") int accountNum) {
		Account a = branch.getAccount(accountNum);
		if (a == null) return Response.status(204).build();
		try {
			return Response.ok(a.getBalance()).lastModified(a.getLastModified()).build();
		} catch (Exception e) {
			return Response.status(500).build();
		}
	}

	@PUT
	@Path("accounts/{accountId}/")
	public Response setBalance(@PathParam("accountId") int accountNum, double amount, @Context Request request) {
		Account a = branch.getAccount(accountNum);
		ResponseBuilder builder = null;
		try {
			
			Date lastModified = a.getLastModified();
			
			//es. request header:
			//KEY: If-Modified-Since - VALUE: Sun, 27 Dec 2020 00:00:00 GMT
			//la modifica sarà consentita solo se VALUE è maggiore della data di ultima modifica della risorsa		
			builder = request.evaluatePreconditions(lastModified);
			
			if (builder != null) {
				utx.begin();
				branch.getAccount(accountNum).setBalance(amount);
				utx.commit();
				return builder.status(204).build();
			}
			return Response.status(412).build();


		} catch (Exception e) {
			return builder.status(500).build();
		}
	}

	@POST
	@Path("accounts")
	public Response createAccount(@QueryParam("cf") String custCF, double amount) {
		try {

			return Response.created(new URI("/accounts/"+branch.createAccount(custCF, amount))).build();
		} catch (Exception e) {
			return Response.status(500).build();
		}
	}

	@POST
	@Path("accounts/transfers")
	public Response transfer(@QueryParam("source") int srcAccount, @QueryParam("destination") int dstAccount, double amount) {
		try {
			utx.begin();
			branch.getAccount(srcAccount).withdraw(amount);
			branch.getAccount(dstAccount).deposit(amount);
			utx.commit();

			return Response.ok().build();
		} catch (Exception e) {
			try { utx.rollback(); } catch (SystemException ee) {}
			return Response.status(500).build();
		}
	}

	@POST
	@Path("customers/{custCF}/accounts")
	public Response createAccountOfCustomer(@PathParam("custCF") String custCF, double amount) {
		try {

			return Response.created(new URI("/customers/"+custCF+"/accounts/"+branch.createAccount(custCF, amount))).build();
		} catch (Exception e) {
			return Response.status(500).build();
		}
	}


	@POST
	@Path("customers")
	@Consumes("application/json")
	public Response createCustomer(Customer c) {
		try {
			branch.createCustomer(c.getCF(), c.getFirstName(), c.getLastName());
			return Response.created(new URI("/customers/"+c.getCF())).build();
		} catch (Exception e) {
			return Response.status(500).build();
		}
	}

	@GET
	@Path("customers/{custCF}")
	@Produces(MediaType.APPLICATION_JSON)
	//@Transactional(TxType.REQUIRED)
	public Response getCustomer(@PathParam("custCF") String cf) {
		try {
			
			CustomerModel c = branch.getCustomer(cf);
			
			if (c == null) Response.status(404).build();
									
			Response response = Response.ok(c, MediaType.APPLICATION_JSON).build();
			
			return response;
		
		} catch (Exception e) {
			return Response.status(500).build();
		}
	}

}
