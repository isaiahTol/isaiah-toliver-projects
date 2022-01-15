package com.techelevator.tenmo;

import com.techelevator.tenmo.model.AuthenticatedUser;
import com.techelevator.tenmo.model.TransferObject;
import com.techelevator.tenmo.model.User;
import com.techelevator.tenmo.model.UserCredentials;
import com.techelevator.tenmo.services.AuthenticationService;
import com.techelevator.tenmo.services.AuthenticationServiceException;
import com.techelevator.tenmo.services.TenmoService;

import com.techelevator.tenmo.services.TransferObjectService;
import com.techelevator.view.ConsoleService;
import org.springframework.web.client.HttpServerErrorException;

import java.math.BigDecimal;
import java.util.List;
import java.util.Scanner;

public class App {

private static final String API_BASE_URL = "http://localhost:8080/";
    
    private static final String MENU_OPTION_EXIT = "Exit";
    private static final String LOGIN_MENU_OPTION_REGISTER = "Register";
	private static final String LOGIN_MENU_OPTION_LOGIN = "Login";
	private static final String[] LOGIN_MENU_OPTIONS = { LOGIN_MENU_OPTION_REGISTER, LOGIN_MENU_OPTION_LOGIN, MENU_OPTION_EXIT };
	private static final String MAIN_MENU_OPTION_VIEW_BALANCE = "View your current balance";
	private static final String MAIN_MENU_OPTION_SEND_BUCKS = "Send TE bucks";
	private static final String MAIN_MENU_OPTION_VIEW_PAST_TRANSFERS = "View your past transfers";
	private static final String MAIN_MENU_OPTION_LOGIN = "Login as different user";
	private static final String[] MAIN_MENU_OPTIONS = { MAIN_MENU_OPTION_VIEW_BALANCE, MAIN_MENU_OPTION_SEND_BUCKS, MAIN_MENU_OPTION_VIEW_PAST_TRANSFERS, MAIN_MENU_OPTION_LOGIN, MENU_OPTION_EXIT };
	
    private AuthenticatedUser currentUser;
    private ConsoleService console;
    private AuthenticationService authenticationService;
    private TenmoService tenmoService;
    private TransferObjectService transferObjectService;

    public static void main(String[] args) {
    	App app = new App(new ConsoleService(System.in, System.out), new AuthenticationService(API_BASE_URL));
    	app.run();
    }

    public App(ConsoleService console, AuthenticationService authenticationService) {
		this.console = console;
		this.authenticationService = authenticationService;
		this.tenmoService = new TenmoService();
		this.transferObjectService = new TransferObjectService();
	}

	public void run() {
		System.out.println("*********************");
		System.out.println("* Welcome to TEnmo! *");
		System.out.println("*********************");
		
		registerAndLogin();
		mainMenu();
	}

	private void mainMenu() {
		while(true) {
			String choice = (String)console.getChoiceFromOptions(MAIN_MENU_OPTIONS);
			if(MAIN_MENU_OPTION_VIEW_BALANCE.equals(choice)) {
				viewCurrentBalance();
			} else if(MAIN_MENU_OPTION_VIEW_PAST_TRANSFERS.equals(choice)) {
				viewTransferHistory();
			}  else if(MAIN_MENU_OPTION_SEND_BUCKS.equals(choice)) {
				sendBucks();
			} else if(MAIN_MENU_OPTION_LOGIN.equals(choice)) {
				login();
			} else {
				// the only other option on the main menu is to exit
				exitProgram();
			}
		}
	}

	private void viewCurrentBalance() {
		System.out.println("$" + tenmoService.retrieveBalance());
	}

	private void viewTransferHistory() {

		TransferObject [] userTransfers = transferObjectService.userTransfers();

		for(TransferObject transferObject : userTransfers) {
			String stringToReplace = null;


			if(transferObject.getAccountFromName().equals(currentUser.getUser().getUsername())){
				stringToReplace = "To: " + transferObject.getAccountToName();
			} else if(transferObject.getAccountToName().equals(currentUser.getUser().getUsername())){
				stringToReplace = "From: " + transferObject.getAccountFromName();
			}
			String finalString = transferObject.getTransferID() + "    " + stringToReplace + "    $" + transferObject.getAmountToTransfer();

			System.out.println(finalString);
		}
		Scanner scanner = new Scanner(System.in);
		System.out.println("Please enter transfer ID to view details (0 to cancel):");
		int transferID = Integer.parseInt(scanner.nextLine());
		if(transferID != 0) {
			TransferObject detailedReport = transferObjectService.viewTransactionReport(transferID);

			System.out.println("ID: " + detailedReport.getTransferID());
			System.out.println("From: " + detailedReport.getAccountFromName());
			System.out.println("To: " + detailedReport.getAccountToName());
			System.out.println("Type: Send");
			System.out.println("Status: Approved");
			System.out.println("Amount: $" + detailedReport.getAmountToTransfer());
		} else {
			mainMenu();
		}
	}



	private void sendBucks() {
		// TODO Auto-generated method stub
		User[] allUsers = tenmoService.getAllUsers();

		for(User user : allUsers) {
			String userInfo = user.getId() + "    " + user.getUsername();
			System.out.println(userInfo);
		}
		TransferObject transferObject = createTransferObject();
		BigDecimal currentUserBalance = tenmoService.retrieveBalance();
		boolean keepGoing = true;

		while(keepGoing) {
			if (transferObject.getAmountToTransfer().compareTo(currentUserBalance) == -1 || transferObject.getAmountToTransfer().compareTo(currentUserBalance) == 0) {
				TransferObject returnedTransferObject = transferObjectService.makeTransaction(transferObject);
				keepGoing = false;
			} else {
				System.out.println("Not enough money!");
				keepGoing = false;
				sendBucks();
			}
		}
	}

	private void exitProgram() {
		System.exit(0);
	}

	private void registerAndLogin() {
		while(!isAuthenticated()) {
			String choice = (String)console.getChoiceFromOptions(LOGIN_MENU_OPTIONS);
			if (LOGIN_MENU_OPTION_LOGIN.equals(choice)) {
				login();
			} else if (LOGIN_MENU_OPTION_REGISTER.equals(choice)) {
				register();
			} else {
				// the only other option on the login menu is to exit
				exitProgram();
			}
		}
	}

	private boolean isAuthenticated() {
		return currentUser != null;
	}

	private void register() {
		System.out.println("Please register a new user account");
		boolean isRegistered = false;
        while (!isRegistered) //will keep looping until user is registered
        {
            UserCredentials credentials = collectUserCredentials();
            try {
            	authenticationService.register(credentials);
            	isRegistered = true;
            	System.out.println("Registration successful. You can now login.");
            } catch(AuthenticationServiceException e) {
            	System.out.println("REGISTRATION ERROR: "+e.getMessage());
				System.out.println("Please attempt to register again.");
            }
        }
	}

	private void login() {
		System.out.println("Please log in");
		currentUser = null;
		while (currentUser == null) //will keep looping until user is logged in
		{
			UserCredentials credentials = collectUserCredentials();
		    try {
				currentUser = authenticationService.login(credentials);

				tenmoService.setAuthToken(currentUser.getToken());
				transferObjectService.setAuthToken(currentUser.getToken());  //Added this to get the token to a newly created service

			} catch (AuthenticationServiceException e) {
				System.out.println("LOGIN ERROR: "+e.getMessage());
				System.out.println("Please attempt to login again.");
			}
		}
	}
	
	private UserCredentials collectUserCredentials() {
		String username = console.getUserInput("Username");
		String password = console.getUserInput("Password");
		return new UserCredentials(username, password);
	}

	public TransferObject createTransferObject(){
		int fromUserID = currentUser.getUser().getId();
    	Scanner scanner = new Scanner(System.in);
		System.out.println("Enter ID of user you are sending to (0 to cancel): ");
		int toUserID = Integer.parseInt(scanner.nextLine());
		if(toUserID != 0) {
			try {
				System.out.println("Enter amount: ");
				BigDecimal transferredAmount = new BigDecimal(scanner.nextLine());


				TransferObject transferObjectToSend = new TransferObject();
				transferObjectToSend.setTransferStatusID(2);
				transferObjectToSend.setAmountToTransfer(transferredAmount);
				transferObjectToSend.setTransferTypeID(2);
				transferObjectToSend.setUserFromID(fromUserID);
				transferObjectToSend.setUserToID(toUserID);

				return transferObjectToSend;
			} catch (Exception e) {
				System.out.println("Enter a valid account number");
				createTransferObject();
				TransferObject transferObject = new TransferObject();
				return null;
			}
		} else{
			mainMenu();
			return null;
		}
    }
}

