package by.nata.servlet.commands;

import by.nata.servlet.FrontCommand;

import java.io.IOException;
import java.io.PrintWriter;

public class UnknownCommand extends FrontCommand {
    @Override
    public void process() throws IOException {
        PrintWriter out = response.getWriter();
        out.println("<html>");
        out.println("<head><title>Unknown</title></head>");
        out.println("<body>");
        out.println("<h1>Unknown command</h1>");
        out.println("</body>");
        out.println("</html>");
    }
}
