package by.nata.servlet.commands;

import by.nata.dto.AccountDto;
import by.nata.service.api.IAccountService;
import by.nata.service.factory.AccountServiceSingleton;
import by.nata.servlet.FrontCommand;
import lombok.extern.log4j.Log4j2;

import java.io.IOException;
import java.io.PrintWriter;

@Log4j2
public class WithdrawalCommand extends FrontCommand {

    public static final IAccountService accountService;

    static {
        accountService = AccountServiceSingleton.getInstance();
    }

    @Override
    public void process() throws IOException {
        String account = request.getParameter("account");
        Double sum = Double.valueOf(request.getParameter("sum"));

        AccountDto withdrawal = accountService.withdrawal(account, sum);

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        out.println("<html>");
        out.println("<head><title>Withdrawal</title></head>");
        out.println("<body>");

        if (withdrawal != null) {
            request.setAttribute("withdrawal", withdrawal);
            out.println("<h1>Withdrawal Successful</h1>");
            out.println("<p>Withdrawal of " + sum + " from your account was successful.</p>");
        } else {
            out.println("<h1>Withdrawal Error</h1>");
            out.println("<p>There was an error while processing your withdrawal request.</p>");
        }

        out.println("</body>");
        out.println("</html>");
    }
}
