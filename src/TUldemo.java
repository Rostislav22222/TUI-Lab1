package com.mybank.tui;

import jexer.TAction;
import jexer.TApplication;
import jexer.TField;
import jexer.TText;
import jexer.TWindow;
import jexer.event.TMenuEvent;
import jexer.menu.TMenu;

public class TUIdemo extends TApplication {

    private static final int ABOUT_APP = 2000;
    private static final int CUST_INFO = 2010;

    private Bank bank;

    public static void main(String[] args) throws Exception {
        TUIdemo tdemo = new TUIdemo();
        (new Thread(tdemo)).start();
    }

    public TUIdemo() throws Exception {
        super(BackendType.SWING);

        addToolMenu();
        // Custom 'File' menu
        TMenu fileMenu = addMenu("&File");
        fileMenu.addItem(CUST_INFO, "&Customer Info");
        fileMenu.addDefaultItem(TMenu.MID_SHELL);
        fileMenu.addSeparator();
        fileMenu.addDefaultItem(TMenu.MID_EXIT);
        // End of 'File' menu

        addWindowMenu();

        // Custom 'Help' menu
        TMenu helpMenu = addMenu("&Help");
        helpMenu.addItem(ABOUT_APP, "&About...");
        // End of 'Help' menu

        setFocusFollowsMouse(true);

        // Initialize Bank and customers
        bank = new Bank();
        bank.addCustomer(new Customer("John Doe", "123 Main St"));
        bank.addCustomer(new Customer("Jane Smith", "456 Elm St"));
        bank.getCustomer(0).addAccount(new Account("Checking", 200.00));
        bank.getCustomer(1).addAccount(new Account("Savings", 500.00));

        // Customer window
        ShowCustomerDetails();
    }

    @Override
    protected boolean onMenu(TMenuEvent menu) {
        if (menu.getId() == ABOUT_APP) {
            messageBox("About", "\t\t\t\t\t   Just a simple Jexer demo.\n\nCopyright \u00A9 2019 Alexander \'Taurus\' Babich")
                    .show();
            return true;
        }
        if (menu.getId() == CUST_INFO) {
            ShowCustomerDetails();
            return true;
        }
        return super.onMenu(menu);
    }

    private void ShowCustomerDetails() {
        TWindow custWin = addWindow("Customer Window", 2, 1, 40, 10, TWindow.NOZOOMBOX);
        custWin.newStatusBar("Enter valid customer number and press Show...");

        custWin.addLabel("Enter customer number: ", 2, 2);
        TField custNo = custWin.addField(24, 2, 3, false);
        TText details = custWin.addText("", 2, 4, 38, 8);
        custWin.addButton("&Show", 28, 2, new TAction() {
            @Override
            public void DO() {
                try {
                    int custNum = Integer.parseInt(custNo.getText());
                    Customer customer = bank.getCustomer(custNum - 1);
                    if (customer != null) {
                        StringBuilder builder = new StringBuilder();
                        builder.append("Owner Name: ").append(customer.getName()).append(" (id=").append(custNum)
                                .append(")\n");
                        for (Account account : customer.getAccounts()) {
                            builder.append("Account Type: '").append(account.getType()).append("'\n");
                            builder.append("Account Balance: $").append(account.getBalance()).append("\n");
                        }
                        details.setText(builder.toString());
                    } else {
                        details.setText("Customer not found.");
                    }
                } catch (NumberFormatException e) {
                    messageBox("Error", "You must provide a valid customer number!").show();
                }
            }
        });
    }
}