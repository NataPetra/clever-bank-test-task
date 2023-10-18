package by.nata.servlet.commands;

import by.nata.service.api.IAccountService;
import by.nata.service.factory.AccountServiceSingleton;
import by.nata.servlet.FrontCommand;
import lombok.extern.log4j.Log4j2;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@Log4j2
public class TransferCommand extends FrontCommand {

    public static final IAccountService accountService;

    static {
        accountService = AccountServiceSingleton.getInstance();
    }

    @Override
    public void process() throws IOException {
        String sourceAccount = request.getParameter("sourceAccount");
        String destinationAccount = request.getParameter("destinationAccount");
        Double sum = Double.valueOf(request.getParameter("sum"));

        try {
            accountService.transferWithinOneBank(sourceAccount, destinationAccount, sum);

            response.setContentType("text/html");
            PrintWriter out = response.getWriter();

            out.println("<html>");
            out.println("<head><title>Transfer</title></head>");
            out.println("<body>");

            out.println("<h1>Transfer Successful</h1>");
            out.println("<p>Transfer of " + sum + " from account " + sourceAccount + " to account " + destinationAccount + " was successful.</p>");

            out.println("</body>");
            out.println("</html>");
        } catch (Exception e) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Transfer failed: " + e.getMessage());
        }
    }
}
