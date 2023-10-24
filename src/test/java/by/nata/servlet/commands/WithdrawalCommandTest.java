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
class WithdrawalCommandTest {

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private IAccountService accountService;

    @Mock
    private ServletContext servletContext;

    @InjectMocks
    private WithdrawalCommand withdrawalCommand;

    @BeforeEach
    void setUp() {
        WithdrawalCommand.accountService = accountService;
    }

    @Test
    void testProcessWithSuccessfulWithdrawal() throws Exception {
        when(request.getParameter("account")).thenReturn("123456");
        when(request.getParameter("sum")).thenReturn("50.0");

        when(accountService.withdrawal("123456", 50.0)).thenReturn(new AccountDto(1L, "123456", BigDecimal.valueOf(50.0)));

        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(printWriter);

        withdrawalCommand.init(servletContext, request, response);
        withdrawalCommand.process();

        String htmlResponse = stringWriter.toString();

        Assertions.assertThat(htmlResponse).contains("Withdrawal Successful");
    }

    @Test
    void testProcessWithFailedWithdrawal() throws Exception {
        when(request.getParameter("account")).thenReturn("123456");
        when(request.getParameter("sum")).thenReturn("50.0");

        when(accountService.withdrawal("123456", 50.0)).thenReturn(null);

        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(printWriter);

        withdrawalCommand.init(servletContext, request, response);
        withdrawalCommand.process();

        String htmlResponse = stringWriter.toString();

        Assertions.assertThat(htmlResponse).contains("Withdrawal Error");
    }
}