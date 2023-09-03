package by.nata.servlet.commands;

import by.nata.dto.AccountDto;
import by.nata.service.api.IAccountService;
import by.nata.service.fabrics.AccountServiceSingleton;
import by.nata.servlet.FrontCommand;

import javax.servlet.ServletException;
import java.io.IOException;

public class RefillCommand extends FrontCommand {

    public static final IAccountService accountService;

    static {
        accountService = AccountServiceSingleton.getInstance();
    }
    @Override
    public void process() throws ServletException, IOException {
        AccountDto refill = accountService.refill(
                request.getParameter("account"),
                Double.valueOf(request.getParameter("sum")));
        if (refill != null) {
            request.setAttribute("refill", refill);
            forward("refill-done");
        } else {
            forward("refill-is-not-done");
        }
    }
}
