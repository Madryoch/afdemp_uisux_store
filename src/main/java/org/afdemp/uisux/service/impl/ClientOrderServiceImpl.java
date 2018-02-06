package org.afdemp.uisux.service.impl;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.afdemp.uisux.domain.AbstractSale;
import org.afdemp.uisux.domain.ClientOrder;
import org.afdemp.uisux.domain.security.UserRole;
import org.afdemp.uisux.repository.ClientOrderRepository;
import org.afdemp.uisux.repository.RoleRepository;
import org.afdemp.uisux.repository.UserRoleRepository;
import org.afdemp.uisux.service.AccountService;
import org.afdemp.uisux.service.ClientOrderService;
import org.afdemp.uisux.service.TransactionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ClientOrderServiceImpl implements ClientOrderService{
	
	private static final Logger LOG = LoggerFactory.getLogger(ClientOrderService.class);
	
	@Autowired
	private ClientOrderRepository clientOrderRepository;
	
	@Autowired
	private UserRoleRepository userRoleRepository;
	
	@Autowired
	private RoleRepository roleRepository;
	
	@Autowired
	private TransactionService transactionService;
	
	@Autowired
	private AccountService accountService;
	
	@Override
	public ClientOrder createClientOrder(ClientOrder clientOrder)
	{
		
		Date submittedDate=Date.valueOf(LocalDate.now());
		clientOrder.setSubmittedDate(submittedDate);
		clientOrder.setOrderStatus("Processing");
		
		clientOrderRepository.save(clientOrder);
		return(clientOrder);
	}
	
	@Override
	public ClientOrder updateOrderStatusToShipped(ClientOrder clientOrder)
	{
		clientOrder=clientOrderRepository.findOne(clientOrder.getId());
		if(clientOrder!=null)
		{
			clientOrder.setOrderStatus("Shipped");
			clientOrderRepository.save(clientOrder);
			LOG.info("SUCCESS: Order Status changed to Shipped.");
		}
		else
		{
			LOG.info("FAILURE: Unable to find Client Order");
		}
		return clientOrder;
	}
	
	@Override
	public ClientOrder updateOrderStatusToDelivered(ClientOrder clientOrder)
	{
		clientOrder=clientOrderRepository.findOne(clientOrder.getId());
		if(clientOrder!=null)
		{
			clientOrder.setOrderStatus("Delivered");
			clientOrderRepository.save(clientOrder);
			LOG.info("SUCCESS: Order Status changed to Delivered.");
		}
		else
		{
			LOG.info("FAILURE: Unable to find Client Order");
		}
		return clientOrder;
	}
	
	
	@Override
	public List<ClientOrder> fetchOrdersByPeriod(Timestamp fromTimestamp, Timestamp toTimestamp)
	{
		List<ClientOrder> clientOrders=new ArrayList<ClientOrder>();
		
		clientOrders=clientOrderRepository.findOrdersFromTo(fromTimestamp, toTimestamp);
		return clientOrders;
	}

	@Override
	public ClientOrder updateShippingMethod(ClientOrder clientOrder, String method) {
		clientOrder=clientOrderRepository.findOne(clientOrder.getId());
		if(clientOrder!=null)
		{
			clientOrder.setShippingMethod(method);
			clientOrderRepository.save(clientOrder);
			LOG.info("SUCCESS: Shipping method changed to " + method  + ".");
		}
		else
		{
			LOG.info("FAILURE: Unable to find Client Order");
		}
		return clientOrder;
	}

	@Override
	public ClientOrder updateShippingDate(ClientOrder clientOrder, Date date) {
		clientOrder=clientOrderRepository.findOne(clientOrder.getId());
		if(clientOrder!=null)
		{
			clientOrder.setShippingDate(date);
			clientOrderRepository.save(clientOrder);
			LOG.info("SUCCESS: Shipping date changed to " + date  + ".");
		}
		else
		{
			LOG.info("FAILURE: Unable to find Client Order");
		}
		return clientOrder;
	}

	@Override
	public List<ClientOrder> findAllUndistributedEarnings()
	{
		return clientOrderRepository.findByDistributedFalse();	
	}
	
	@Override
	public void distributeEarningsToAllMembers(Long clientOrderId) {

		ArrayList<UserRole> members=userRoleRepository.findByRole(roleRepository.findByName("ROLE_MEMBER"));
		int divisor=members.size();
		ClientOrder clientOrder=clientOrderRepository.findOne(clientOrderId);
		if (divisor!=0)
		{
			BigDecimal amount=clientOrder.getTotal().divide(BigDecimal.valueOf(2)).divide(BigDecimal.valueOf(divisor));
			for(UserRole usr:members)
			{
				transactionService.twoWayTransaction(amount, accountService.findAdminAccount(), usr.getAccount(), clientOrder);
			}
			
			clientOrder.setDistributed(true);
			clientOrderRepository.save(clientOrder);
		}
	}

	@Override
	public ClientOrder findOne(Long id) {
		return clientOrderRepository.findOne(id);
	}

}
