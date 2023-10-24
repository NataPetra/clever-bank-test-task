package by.nata.servlet.commands;

import by.nata.service.api.IAccountService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.io.StringWriter;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TransferCommandTest {

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private IAccountService accountService;

    @Mock
    private ServletContext servletContext;

    @InjectMocks
    private TransferCommand transferCommand;

    @BeforeEach
    void setUp() {
        TransferCommand.accountService = accountService;
    }

    @Test
    void processWithSuccessfulTransfer() throws Exception {
        when(request.getParameter("sourceAccount")).thenReturn("sourceAccount123");
        when(request.getParameter("destinationAccount")).thenReturn("destinationAccount456");
        when(request.getParameter("sum")).thenReturn("100.0");

        doNothing().when(accountService).transferWithinOneBank("sourceAccount123", "destinationAccount456", 100.0);

        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(printWriter);

        transferCommand.init(servletContext, request, response);
        transferCommand.process();

        String htmlResponse = stringWriter.toString();

        Assertions.assertThat(htmlResponse).contains("Transfer Successful");
    }

}