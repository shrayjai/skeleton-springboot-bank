package com.shrayjai.bank;

import com.shrayjai.bank.exception.AccountNotFoundException;
import com.shrayjai.bank.exception.InsufficientBalanceException;
import com.shrayjai.bank.exception.InvalidRequestException;
import com.shrayjai.bank.model.*;
import com.shrayjai.bank.service.BankService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.math.BigDecimal;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = BankApplication.class)
@SpringBootTest
public class ApplicationControllerTests {

    private final Logger LOG = LoggerFactory.getLogger(ApplicationControllerTests.class);

    @Mock
    private BankService bankService;

    @Autowired
    private WebApplicationContext context;

    private MockMvc mockMvc;

    private final String BALANCE_URL = "/api/v1/balance";

    private final String DEPOSIT_URL = "/api/v1/deposit";

    private final String WITHDRAW_URL = "/api/v1/withdraw";

    private final String TRANSFER_URL = "/api/v1/transfer";

    private final String ACCOUNT_NUMBER = "900000000001";

    private final String BENEFICIARY = "900000000003";

    private final String INVALID_ACCOUNT_NUMBER = "900000000005";

    private final String TRANSACTION_REQUEST = "{\"accountNumber\": \"900000000001\", \"amount\": 500}";

    private final String HIGH_TRANSACTION_REQUEST = "{\"accountNumber\": \"900000000001\", \"amount\": 3000}";

    private final String TRANSFER_REQUEST = "{\"benefactor\": \"900000000001\", \"beneficiary\": \"900000000003\", \"amount\": 1000}";

    private final String INVALID_TRANSFER_REQUEST = "{\"benefactor\": \"900000000001\", \"beneficiary\": \"900000000001\", \"amount\": 1000}";

    private final String SUCCESS = "success";

    private final String ACCOUNT_NOT_FOUND = "account does not exist";

    private final String SAME_ACCOUNT = "benefactor and beneficiary must be different";

    @Before
    public void setup() {

        MockitoAnnotations.initMocks(this);
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.context).build();
    }

    @Test
    public void getBalance_Success() throws Exception {

        this.LOG.info("------------------------------------------------------------------------------");
        this.LOG.info("--- TEST: {}", Thread.currentThread().getStackTrace()[1].getMethodName());
        this.LOG.info("------------------------------------------------------------------------------");

        BalanceResponse balanceResponse = new BalanceResponse(ACCOUNT_NUMBER, new BigDecimal("1000.000"));

        Mockito.when(this.bankService.viewAccountBalance(ACCOUNT_NUMBER)).thenReturn(balanceResponse);

        MvcResult result = mockMvc.perform(get(BALANCE_URL).header("accountNumber", ACCOUNT_NUMBER).accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk()).andReturn();

        String response = result.getResponse().getContentAsString();

        assertThat(response).isNotNull();
    }

    @Test
    public void getBalance_AccountNotFoundException() throws Exception {

        this.LOG.info("------------------------------------------------------------------------------");
        this.LOG.info("--- TEST: {}", Thread.currentThread().getStackTrace()[1].getMethodName());
        this.LOG.info("------------------------------------------------------------------------------");

        Mockito.when(this.bankService.viewAccountBalance(INVALID_ACCOUNT_NUMBER)).thenThrow(new AccountNotFoundException(ACCOUNT_NOT_FOUND));

        MvcResult result = mockMvc.perform(get(BALANCE_URL).header("accountNumber", INVALID_ACCOUNT_NUMBER).accept(MediaType.APPLICATION_JSON)).andExpect(status().isNotFound()).andReturn();

        int status = result.getResponse().getStatus();

        assertEquals(404, status);
    }

    @Test
    public void postDeposit_Success() throws Exception {

        this.LOG.info("------------------------------------------------------------------------------");
        this.LOG.info("--- TEST: {}", Thread.currentThread().getStackTrace()[1].getMethodName());
        this.LOG.info("------------------------------------------------------------------------------");

        TransactionRequest txnRequest = new TransactionRequest(ACCOUNT_NUMBER, new BigDecimal("500"));

        TransactionResponse txnResponse = new TransactionResponse(ACCOUNT_NUMBER, new BigDecimal("1000.000"), new BigDecimal("1500.000"));

        Mockito.when(this.bankService.depositCash(txnRequest)).thenReturn(txnResponse);

        MvcResult result = mockMvc.perform(post(DEPOSIT_URL).contentType(MediaType.APPLICATION_JSON).content(TRANSACTION_REQUEST).accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk()).andReturn();

        String response = result.getResponse().getContentAsString();

        assertThat(response).isNotNull();
    }

    @Test
    public void postWithdraw_Success() throws Exception {

        this.LOG.info("------------------------------------------------------------------------------");
        this.LOG.info("--- TEST: {}", Thread.currentThread().getStackTrace()[1].getMethodName());
        this.LOG.info("------------------------------------------------------------------------------");

        TransactionRequest txnRequest = new TransactionRequest(ACCOUNT_NUMBER, new BigDecimal("500.000"));

        TransactionResponse txnResponse = new TransactionResponse(ACCOUNT_NUMBER, new BigDecimal("1000.000"), new BigDecimal("500.000"));

        Mockito.when(this.bankService.withdrawCash(txnRequest)).thenReturn(txnResponse);

        MvcResult result = mockMvc.perform(post(WITHDRAW_URL).contentType(MediaType.APPLICATION_JSON).content(TRANSACTION_REQUEST).accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk()).andReturn();

        String response = result.getResponse().getContentAsString();

        assertThat(response).isNotNull();
    }

    @Test
    public void postWithdraw_InsufficientBalanceException() throws Exception {

        this.LOG.info("------------------------------------------------------------------------------");
        this.LOG.info("--- TEST: {}", Thread.currentThread().getStackTrace()[1].getMethodName());
        this.LOG.info("------------------------------------------------------------------------------");

        TransactionRequest txnRequest = new TransactionRequest(ACCOUNT_NUMBER, new BigDecimal("5000.000"));

        Mockito.when(this.bankService.withdrawCash(txnRequest)).thenThrow(new InsufficientBalanceException("low balance"));

        MvcResult result = mockMvc.perform(post(WITHDRAW_URL).contentType(MediaType.APPLICATION_JSON).content(HIGH_TRANSACTION_REQUEST).accept(MediaType.APPLICATION_JSON)).andExpect(status().isBadRequest()).andReturn();

        int status = result.getResponse().getStatus();

        assertEquals(400, status);
    }

    @Test
    public void postTransfer_Success() throws Exception {

        this.LOG.info("------------------------------------------------------------------------------");
        this.LOG.info("--- TEST: {}", Thread.currentThread().getStackTrace()[1].getMethodName());
        this.LOG.info("------------------------------------------------------------------------------");

        TransferRequest trfRequest = new TransferRequest(ACCOUNT_NUMBER, BENEFICIARY, new BigDecimal("500.000"));

        TransferResponse trfResponse = new TransferResponse(BENEFICIARY, new BigDecimal("500.000"), SUCCESS);

        Mockito.when(this.bankService.transferMoney(trfRequest)).thenReturn(trfResponse);

        MvcResult result = mockMvc.perform(post(TRANSFER_URL).contentType(MediaType.APPLICATION_JSON).content(TRANSFER_REQUEST).accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk()).andReturn();

        String response = result.getResponse().getContentAsString();

        assertThat(response).isNotNull();
    }

    @Test
    public void postTransfer_InvalidRequestException() throws Exception {

        this.LOG.info("------------------------------------------------------------------------------");
        this.LOG.info("--- TEST: {}", Thread.currentThread().getStackTrace()[1].getMethodName());
        this.LOG.info("------------------------------------------------------------------------------");

        TransferRequest trfRequest = new TransferRequest(ACCOUNT_NUMBER, ACCOUNT_NUMBER, new BigDecimal("500.000"));

        Mockito.when(this.bankService.transferMoney(trfRequest)).thenThrow(new InvalidRequestException(SAME_ACCOUNT));

        MvcResult result = mockMvc.perform(post(TRANSFER_URL).contentType(MediaType.APPLICATION_JSON).content(INVALID_TRANSFER_REQUEST).accept(MediaType.APPLICATION_JSON)).andExpect(status().isBadRequest()).andReturn();

        int status = result.getResponse().getStatus();

        assertEquals(400, status);
    }
}
