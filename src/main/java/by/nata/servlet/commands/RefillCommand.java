package by.nata.servlet.commands;

import by.nata.dto.AccountDto;
import by.nata.service.api.IAccountService;
import by.nata.service.factory.AccountServiceSingleton;
import by.nata.servlet.FrontCommand;

import java.io.IOException;
import java.io.PrintWriter;

public class RefillCommand extends FrontCommand {

    public static IAccountService accountService;

    static {
        accountService = AccountServiceSingleton.getInstance();
    }
    @Override
    public void process() throws IOException {
        AccountDto refill = accountService.refill(
                request.getParameter("account"),
                Double.valueOf(request.getParameter("sum")));
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        out.println("<html>");
        out.println("<head><title>Refill</title></head>");
        out.println("<body>");

        if (refill != null) {
            request.setAttribute("refill", refill);
            out.println("<h1>Refill Successful</h1>");
            out.println("<p>Your account has been successfully refilled.</p>");
        } else {
            out.println("<h1>Refill Error</h1>");
            out.println("<p>There was an error while refilling your account.</p>");
        }

        out.println("</body>");
        out.println("</html>");
    }
}
