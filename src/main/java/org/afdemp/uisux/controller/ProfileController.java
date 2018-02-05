package org.afdemp.uisux.controller;

import java.security.Principal;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.afdemp.uisux.domain.User;
import org.afdemp.uisux.domain.security.PasswordResetToken;
import org.afdemp.uisux.domain.security.Role;
import org.afdemp.uisux.domain.security.UserRole;
import org.afdemp.uisux.domain.Address;
import org.afdemp.uisux.domain.Category;
import org.afdemp.uisux.domain.CreditCard;
import org.afdemp.uisux.domain.Product;
import org.afdemp.uisux.service.CategoryService;
import org.afdemp.uisux.service.ProductService;
import org.afdemp.uisux.service.UserRoleService;
import org.afdemp.uisux.service.UserService;
import org.afdemp.uisux.service.impl.UserSecurityService;
import org.afdemp.uisux.utility.SecurityUtility;
import org.afdemp.uisux.utility.ΜailConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ProfileController {

	@Autowired
	private UserService userService;
	
	@Autowired
	private ProductService productService;
	
	@Autowired
	private CategoryService categoryService;

	@Autowired
	private ΜailConstructor mailConstructor;
	
	@Autowired
	private JavaMailSender mailSender;

	@Autowired
	private UserSecurityService userSecurityService;

	@Autowired
	private UserRoleService userRoleService;
	
	@RequestMapping("/myProfile")
	public String myProfile(Model model, Principal principal) {
		User user = userService.findByUsername(principal.getName());
		UserRole userRole = userRoleService.findByUserAndRole(user, "ROLE_CLIENT");
		model.addAttribute("user", user);
		model.addAttribute("userCreditCartList", userRole.getCreditCardList());
		model.addAttribute("userShippingAddressList", userRole.getUserShippingAddressList());
		model.addAttribute("abstractSaleList", userRole.getAbstractSaleList());
		
		Address userShipping = new Address();
		model.addAttribute("userShipping", userShipping);
		
		model.addAttribute("listOfCreditCards", true);
		model.addAttribute("listOfShippingAddresses", true);
		
//		List<String> stateList = USConstants.listOfUSStatesCode;
//		Collections.sort(stateList);
//		model.addAttribute("stateList", stateList);
		model.addAttribute("classActiveEdit", true);
		
		return "myProfile";
	}
	
	@RequestMapping("/listOfCreditCards")
	public String listOfCreditCards(
			Model model, Principal principal, HttpServletRequest request
			) {
		User user = userService.findByUsername(principal.getName());
		UserRole userRole = userRoleService.findByUserAndRole(user, "ROLE_CLIENT");
		model.addAttribute("user", user);
		model.addAttribute("userCreditCartList", userRole.getCreditCardList());
		model.addAttribute("userShippingAddressList", userRole.getUserShippingAddressList());
		model.addAttribute("abstractSaleList", userRole.getAbstractSaleList());
		
		model.addAttribute("listOfCreditCards", true);
		model.addAttribute("classActiveBilling", true);
		model.addAttribute("listOfShippingAddresses", true);
		
		return "myProfile";
	}
	
	@RequestMapping("/listOfShippingAddresses")
	public String listOfShippingAddresses(
			Model model, Principal principal, HttpServletRequest request
			) {
		User user = userService.findByUsername(principal.getName());
		UserRole userRole = userRoleService.findByUserAndRole(user, "ROLE_CLIENT");
		model.addAttribute("user", user);
		model.addAttribute("userCreditCartList", userRole.getCreditCardList());
		model.addAttribute("userShippingAddressList", userRole.getUserShippingAddressList());
		model.addAttribute("abstractSaleList", userRole.getAbstractSaleList());
		
		model.addAttribute("listOfCreditCards", true);
		model.addAttribute("classActiveBilling", true);
		model.addAttribute("listOfShippingAddresses", true);
		
		return "myProfile";
	}
	
	@RequestMapping("/addNewCreditCard")
	public String addNewCreditCard(
			Model model, Principal principal
			){
		User user = userService.findByUsername(principal.getName());
		UserRole userRole = userRoleService.findByUserAndRole(user, "ROLE_CLIENT");
		model.addAttribute("user", user);
		
		model.addAttribute("addNewCreditCard", true);
		model.addAttribute("classActiveBilling", true);
		model.addAttribute("listOfShippingAddresses", true);
		
		Address billingAddress = new Address();
		CreditCard creditCard = new CreditCard();
		
		
		model.addAttribute("billingAddress", billingAddress);
		model.addAttribute("creditCard", creditCard);
		
//		List<String> stateList = USConstants.listOfUSStatesCode;
//		Collections.sort(stateList);
//		model.addAttribute("stateList", stateList);
		model.addAttribute("userCreditCartList", userRole.getCreditCardList());
		model.addAttribute("userShippingAddressList", userRole.getUserShippingAddressList());
		model.addAttribute("abstractSaleList", userRole.getAbstractSaleList());
		
		return "myProfile";
	}
	
	@RequestMapping(value="/addNewCreditCard", method=RequestMethod.POST)
	public String addNewCreditCard(
			@ModelAttribute("CreditCard") CreditCard creditCard,
			@ModelAttribute("billingAddress") Address billingAddress,
			Principal principal, Model model
			){
		User user = userService.findByUsername(principal.getName());
		UserRole userRole = userRoleService.findByUserAndRole(user, "ROLE_CLIENT");
		
		userRoleService.updateBillingAddress(billingAddress, creditCard, userRole);
		
		model.addAttribute("user", user);
		model.addAttribute("userCreditCartList", userRole.getCreditCardList());
		model.addAttribute("userShippingAddressList", userRole.getUserShippingAddressList());
		model.addAttribute("abstractSaleList", userRole.getAbstractSaleList());
		model.addAttribute("listOfCreditCards", true);
		model.addAttribute("classActiveBilling", true);
		model.addAttribute("listOfShippingAddresses", true);
		
		return "myProfile";
	}
	
	@RequestMapping("/addNewShippingAddress")
	public String addNewShippingAddress(
			Model model, Principal principal
			){
		User user = userService.findByUsername(principal.getName());
		UserRole userRole = userRoleService.findByUserAndRole(user, "ROLE_CLIENT");
		model.addAttribute("user", user);
		
		model.addAttribute("addNewShippingAddress", true);
		model.addAttribute("classActiveShipping", true);
		model.addAttribute("listOfCreditCards", true);
		
		Address shippingAddress = new Address();
		
		model.addAttribute("shippingAddress", shippingAddress);
		
		model.addAttribute("userCreditCartList", userRole.getCreditCardList());
		model.addAttribute("userShippingAddressList", userRole.getUserShippingAddressList());
		model.addAttribute("abstractSaleList", userRole.getAbstractSaleList());
		
		return "myProfile";
	}
	
	@RequestMapping(value="/addNewShippingAddress", method=RequestMethod.POST)
	public String addNewShippingAddressPost(
			@ModelAttribute("shippingAddress") Address shippingAddress,
			Principal principal, Model model
			){
		User user = userService.findByUsername(principal.getName());
		UserRole userRole = userRoleService.findByUserAndRole(user, "ROLE_CLIENT");
		
		userRoleService.updateShippingAddress(shippingAddress, userRole);
		
		model.addAttribute("user", user);
		model.addAttribute("userCreditCartList", userRole.getCreditCardList());
		model.addAttribute("userShippingAddressList", userRole.getUserShippingAddressList());
		model.addAttribute("abstractSaleList", userRole.getAbstractSaleList());
		model.addAttribute("listOfShippingAddresses", true);
		model.addAttribute("classActiveShipping", true);
		model.addAttribute("listOfCreditCards", true);
		
		return "myProfile";
	}
	
}



//
//	
//	

//	

//	
//	

//	
//	
//	@RequestMapping("/updateCreditCard")
//	public String updateCreditCard(
//			@ModelAttribute("id") Long creditCardId, Principal principal, Model model
//			) {
//		User user = userService.findByUsername(principal.getName());
//		UserPayment userPayment = userPaymentService.findById(creditCardId);
//		
//		if(user.getId() != userPayment.getUser().getId()) {
//			return "badRequestPage";
//		} else {
//			model.addAttribute("user", user);
//			UserBilling userBilling = userPayment.getUserBilling();
//			model.addAttribute("userPayment", userPayment);
//			model.addAttribute("userBilling", userBilling);
//			
//			List<String> stateList = USConstants.listOfUSStatesCode;
//			Collections.sort(stateList);
//			model.addAttribute("stateList", stateList);
//			
//			model.addAttribute("addNewCreditCard", true);
//			model.addAttribute("classActiveBilling", true);
//			model.addAttribute("listOfShippingAddresses", true);
//			
//			model.addAttribute("userPaymentList", user.getUserPaymentList());
//			model.addAttribute("userShippingList", user.getUserShippingList());
//			model.addAttribute("orderList", user.getOrderList());
//			
//			return "myProfile";
//		}
//	}
//	
//	@RequestMapping("/updateUserShipping")
//	public String updateUserShipping(
//			@ModelAttribute("id") Long shippingAddressId, Principal principal, Model model
//			) {
//		User user = userService.findByUsername(principal.getName());
//		UserShipping userShipping = userShippingService.findById(shippingAddressId);
//		
//		if(user.getId() != userShipping.getUser().getId()) {
//			return "badRequestPage";
//		} else {
//			model.addAttribute("user", user);
//			
//			model.addAttribute("userShipping", userShipping);
//			
//			List<String> stateList = USConstants.listOfUSStatesCode;
//			Collections.sort(stateList);
//			model.addAttribute("stateList", stateList);
//			
//			model.addAttribute("addNewShippingAddress", true);
//			model.addAttribute("classActiveShipping", true);
//			model.addAttribute("listOfCreditCards", true);
//			
//			model.addAttribute("userPaymentList", user.getUserPaymentList());
//			model.addAttribute("userShippingList", user.getUserShippingList());
//			model.addAttribute("orderList", user.getOrderList());
//			
//			return "myProfile";
//		}
//	}
//	
//	@RequestMapping(value="/setDefaultPayment", method=RequestMethod.POST)
//	public String setDefaultPayment(
//			@ModelAttribute("defaultUserPaymentId") Long defaultPaymentId, Principal principal, Model model
//			) {
//		User user = userService.findByUsername(principal.getName());
//		userService.setUserDefaultPayment(defaultPaymentId, user);
//		
//		model.addAttribute("user", user);
//		model.addAttribute("listOfCreditCards", true);
//		model.addAttribute("classActiveBilling", true);
//		model.addAttribute("listOfShippingAddresses", true);
//		
//		model.addAttribute("userPaymentList", user.getUserPaymentList());
//		model.addAttribute("userShippingList", user.getUserShippingList());
//		model.addAttribute("orderList", user.getOrderList());
//		
//		return "myProfile";
//	}
//	
//	@RequestMapping(value="/setDefaultShippingAddress", method=RequestMethod.POST)
//	public String setDefaultShippingAddress(
//			@ModelAttribute("defaultShippingAddressId") Long defaultShippingId, Principal principal, Model model
//			) {
//		User user = userService.findByUsername(principal.getName());
//		userService.setUserDefaultShipping(defaultShippingId, user);
//		
//		model.addAttribute("user", user);
//		model.addAttribute("listOfCreditCards", true);
//		model.addAttribute("classActiveShipping", true);
//		model.addAttribute("listOfShippingAddresses", true);
//		
//		model.addAttribute("userPaymentList", user.getUserPaymentList());
//		model.addAttribute("userShippingList", user.getUserShippingList());
//		model.addAttribute("orderList", user.getOrderList());
//		
//		return "myProfile";
//	}
//	
//	@RequestMapping("/removeCreditCard")
//	public String removeCreditCard(
//			@ModelAttribute("id") Long creditCardId, Principal principal, Model model
//			){
//		User user = userService.findByUsername(principal.getName());
//		UserPayment userPayment = userPaymentService.findById(creditCardId);
//		
//		if(user.getId() != userPayment.getUser().getId()) {
//			return "badRequestPage";
//		} else {
//			model.addAttribute("user", user);
//			userPaymentService.removeById(creditCardId);
//			
//			model.addAttribute("listOfCreditCards", true);
//			model.addAttribute("classActiveBilling", true);
//			model.addAttribute("listOfShippingAddresses", true);
//			
//			model.addAttribute("userPaymentList", user.getUserPaymentList());
//			model.addAttribute("userShippingList", user.getUserShippingList());
//			model.addAttribute("orderList", user.getOrderList());
//			
//			return "myProfile";
//		}
//	}
//	
//	@RequestMapping("/removeUserShipping")
//	public String removeUserShipping(
//			@ModelAttribute("id") Long userShippingId, Principal principal, Model model
//			){
//		User user = userService.findByUsername(principal.getName());
//		UserShipping userShipping = userShippingService.findById(userShippingId);
//		
//		if(user.getId() != userShipping.getUser().getId()) {
//			return "badRequestPage";
//		} else {
//			model.addAttribute("user", user);
//			
//			userShippingService.removeById(userShippingId);
//			
//			model.addAttribute("listOfShippingAddresses", true);
//			model.addAttribute("classActiveShipping", true);
//			
//			model.addAttribute("userPaymentList", user.getUserPaymentList());
//			model.addAttribute("userShippingList", user.getUserShippingList());
//			model.addAttribute("orderList", user.getOrderList());
//			
//			return "myProfile";
//		}
//	}
//	
//	@RequestMapping(value="/newUser", method = RequestMethod.POST)
//	public String newUserPost(
//			HttpServletRequest request,
//			@ModelAttribute("email") String userEmail,
//			@ModelAttribute("username") String username,
//			Model model
//			) throws Exception{
//		model.addAttribute("classActiveNewAccount", true);
//		model.addAttribute("email", userEmail);
//		model.addAttribute("username", username);
//		
//		if (userService.findByUsername(username) != null) {
//			model.addAttribute("usernameExists", true);
//			
//			return "myAccount";
//		}
//		
//		if (userService.findByEmail(userEmail) != null) {
//			model.addAttribute("emailExists", true);
//			
//			return "myAccount";
//		}
//		
//		User user = new User();
//		user.setUsername(username);
//		user.setEmail(userEmail);
//		
//		String password = SecurityUtility.randomPassword();
//		
//		String encryptedPassword = SecurityUtility.passwordEncoder().encode(password);
//		user.setPassword(encryptedPassword);
//		
//		Role role = new Role();
//		role.setRoleId(1);
//		role.setName("ROLE_USER");
//		Set<UserRole> userRoles = new HashSet<>();
//		userRoles.add(new UserRole(user, role));
//		userService.createUser(user, userRoles);
//		
//		String token = UUID.randomUUID().toString();
//		userService.createPasswordResetTokenForUser(user, token);
//		
//		String appUrl = "http://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath();
//		
//		SimpleMailMessage email = mailConstructor.constructResetTokenEmail(appUrl, request.getLocale(), token, user, password);
//		
//		mailSender.send(email);
//		
//		model.addAttribute("emailSent", "true");
//		model.addAttribute("orderList", user.getOrderList());
//		
//		return "myAccount";
//	}
//	
//

//	
//	@RequestMapping(value="/updateUserInfo", method=RequestMethod.POST)
//	public String updateUserInfo(
//			@ModelAttribute("user") User user,
//			@ModelAttribute("newPassword") String newPassword,
//			Model model
//			) throws Exception {
//		User currentUser = userService.findById(user.getId());
//		
//		if(currentUser == null) {
//			throw new Exception ("User not found");
//		}
//		
//		/*check email already exists*/
//		if (userService.findByEmail(user.getEmail())!=null) {
//			if(userService.findByEmail(user.getEmail()).getId() != currentUser.getId()) {
//				model.addAttribute("emailExists", true);
//				return "myProfile";
//			}
//		}
//		
//		/*check username already exists*/
//		if (userService.findByUsername(user.getUsername())!=null) {
//			if(userService.findByUsername(user.getUsername()).getId() != currentUser.getId()) {
//				model.addAttribute("usernameExists", true);
//				return "myProfile";
//			}
//		}
//		
////		update password
//		if (newPassword != null && !newPassword.isEmpty() && !newPassword.equals("")){
//			BCryptPasswordEncoder passwordEncoder = SecurityUtility.passwordEncoder();
//			String dbPassword = currentUser.getPassword();
//			if(passwordEncoder.matches(user.getPassword(), dbPassword)){
//				currentUser.setPassword(passwordEncoder.encode(newPassword));
//			} else {
//				model.addAttribute("incorrectPassword", true);
//				
//				return "myProfile";
//			}
//		}
//		
//		currentUser.setFirstName(user.getFirstName());
//		currentUser.setLastName(user.getLastName());
//		currentUser.setUsername(user.getUsername());
//		currentUser.setEmail(user.getEmail());
//		
//		userService.save(currentUser);
//		
//		model.addAttribute("updateSuccess", true);
//		model.addAttribute("user", currentUser);
//		model.addAttribute("classActiveEdit", true);
//		
//		model.addAttribute("listOfShippingAddresses", true);
//		model.addAttribute("listOfCreditCards", true);
//		
//		UserDetails userDetails = userSecurityService.loadUserByUsername(currentUser.getUsername());
//
//		Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, userDetails.getPassword(),
//				userDetails.getAuthorities());
//		
//		SecurityContextHolder.getContext().setAuthentication(authentication);
//		model.addAttribute("orderList", user.getOrderList());
//		
//		return "myProfile";
//	}
//	
//	@RequestMapping("/orderDetail")
//	public String orderDetail(
//			@RequestParam("id") Long orderId,
//			Principal principal, Model model
//			){
//		User user = userService.findByUsername(principal.getName());
//		Order order = orderService.findOne(orderId);
//		
//		if(order.getUser().getId()!=user.getId()) {
//			return "badRequestPage";
//		} else {
//			List<CartItem> cartItemList = cartItemService.findByOrder(order);
//			model.addAttribute("cartItemList", cartItemList);
//			model.addAttribute("user", user);
//			model.addAttribute("order", order);
//			
//			model.addAttribute("userPaymentList", user.getUserPaymentList());
//			model.addAttribute("userShippingList", user.getUserShippingList());
//			model.addAttribute("orderList", user.getOrderList());
//			
//			UserShipping userShipping = new UserShipping();
//			model.addAttribute("userShipping", userShipping);
//			
//			List<String> stateList = USConstants.listOfUSStatesCode;
//			Collections.sort(stateList);
//			model.addAttribute("stateList", stateList);
//			
//			model.addAttribute("listOfShippingAddresses", true);
//			model.addAttribute("classActiveOrders", true);
//			model.addAttribute("listOfCreditCards", true);
//			model.addAttribute("displayOrderDetail", true);
//			
//			return "myProfile";
//		}
//	}
//	
//	
//	
//	
//}