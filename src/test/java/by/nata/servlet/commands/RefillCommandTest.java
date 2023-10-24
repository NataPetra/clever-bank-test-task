package by.nata.servlet.commands;

import by.nata.dto.AccountDto;
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
import java.math.BigDecimal;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RefillCommandTest {

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private IAccountService accountService;

    @Mock
    private ServletContext servletContext;

    @InjectMocks
    private RefillCommand refillCommand;

    @BeforeEach
    void setUp() {
        RefillCommand.accountService = accountService;
    }

    @Test
    void processWithSuccessfulRefill() throws Exception {
        when(request.getParameter("account")).thenReturn("123456");
        when(request.getParameter("sum")).thenReturn("100.0");

        when(accountService.refill("123456", 100.0)).thenReturn(new AccountDto(1L, "123456", BigDecimal.valueOf(100.0)));

        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(printWriter);

        refillCommand.init(servletContext, request, response);
        refillCommand.process();

        String htmlResponse = stringWriter.toString();

        Assertions.assertThat(htmlResponse).contains("Refill Successful");
    }

    @Test
    void testProcessWithFailedRefill() throws Exception {
        when(request.getParameter("account")).thenReturn("123456");
        when(request.getParameter("sum")).thenReturn("100.0");

        when(accountService.refill("123456", 100.0)).thenReturn(null);

        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(printWriter);

        refillCommand.init(servletContext, request, response);
        refillCommand.process();

        String htmlResponse = stringWriter.toString();

        Assertions.assertThat(htmlResponse).contains("Refill Error");
    }
}