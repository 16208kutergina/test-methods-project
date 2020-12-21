package org.nsu.fit.tm_backend.manager;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.nsu.fit.tm_backend.database.data.ContactPojo;
import org.nsu.fit.tm_backend.database.data.TopUpBalancePojo;
import org.nsu.fit.tm_backend.manager.auth.data.AuthenticatedUserDetails;
import org.nsu.fit.tm_backend.shared.Globals;
import org.slf4j.Logger;
import org.nsu.fit.tm_backend.database.IDBService;
import org.nsu.fit.tm_backend.database.data.CustomerPojo;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

// Лабораторная 2: покрыть юнит тестами класс CustomerManager на 100%.
class CustomerManagerTest {
    private Logger logger;
    private IDBService dbService;
    private CustomerManager customerManager;

    private CustomerPojo createCustomerInput;

    @BeforeEach
    void init() {
        // Создаем mock объекты.
        dbService = mock(IDBService.class);
        logger = mock(Logger.class);

        // Создаем класс, методы которого будем тестировать,
        // и передаем ему наши mock объекты.
        customerManager = new CustomerManager(dbService, logger);
    }

    @Test
    void testCreateCustomer() {
        // настраиваем mock.
        createCustomerInput = new CustomerPojo();
        createCustomerInput.firstName = "John";
        createCustomerInput.lastName = "Wick";
        createCustomerInput.login = "john_wick@example.com";
        createCustomerInput.password = "Baba_Jaga";
        createCustomerInput.balance = 0;

        CustomerPojo createCustomerOutput = new CustomerPojo();
        createCustomerOutput.id = UUID.randomUUID();
        createCustomerOutput.firstName = "John";
        createCustomerOutput.lastName = "Wick";
        createCustomerOutput.login = "john_wick@example.com";
        createCustomerOutput.password = "Baba_Jaga";
        createCustomerOutput.balance = 0;

        when(dbService.createCustomer(createCustomerInput)).thenReturn(createCustomerOutput);
        when(dbService.getCustomers()).thenReturn(new ArrayList<>());
        // Вызываем метод, который хотим протестировать
        CustomerPojo customer = customerManager.createCustomer(createCustomerInput);

        // Проверяем результат выполенния метода
        assertEquals(customer.id, createCustomerOutput.id);

        // Проверяем, что метод по созданию Customer был вызван ровно 1 раз с определенными аргументами
        verify(dbService, times(1)).createCustomer(createCustomerInput);
        verify(dbService, times(1)).getCustomers();

        // Проверяем, что другие методы не вызывались...
        verifyNoMoreInteractions(dbService);
    }


    // Как не надо писать тест...
    @Test
    void testCreateCustomerWithNullArgument_Wrong() {
        try {
            customerManager.createCustomer(null);
        } catch (IllegalArgumentException ex) {
            assertEquals("Argument 'customer' is null.", ex.getMessage());
        }
    }

    @Test
    void testCreateCustomerWithNullArgument_Right() {
        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                customerManager.createCustomer(null));
        assertEquals("Argument 'customer' is null.", exception.getMessage());
    }

    @Test
    void testCreateCustomerWithEasyPassword() {
        createCustomerInput = new CustomerPojo();
        createCustomerInput.firstName = "John";
        createCustomerInput.lastName = "Wick";
        createCustomerInput.login = "john_wick@example.com";
        createCustomerInput.password = "123qwe";
        createCustomerInput.balance = 0;

        Exception exception = assertThrows(IllegalArgumentException.class, () -> customerManager.createCustomer(createCustomerInput));
        assertEquals("Password is very easy.", exception.getMessage());
    }

    @Test
    void testCreateCustomerWithShortPassword() {
        createCustomerInput = new CustomerPojo();
        createCustomerInput.firstName = "John";
        createCustomerInput.lastName = "Wick";
        createCustomerInput.login = "john_wick@gmail.com";
        createCustomerInput.password = "12qwe";
        createCustomerInput.balance = 0;

        Exception exception = assertThrows(IllegalArgumentException.class, () -> customerManager.createCustomer(createCustomerInput));
        assertEquals("Password's length should be more or equal 6 symbols and less or equal 12 symbols.", exception.getMessage());
    }

    @Test
    void testCreateCustomerWithNullPassword() {
        createCustomerInput = new CustomerPojo();
        createCustomerInput.firstName = "John";
        createCustomerInput.lastName = "Wick";
        createCustomerInput.login = "john_wick@gmail.com";
        createCustomerInput.balance = 0;

        Exception exception = assertThrows(IllegalArgumentException.class, () -> customerManager.createCustomer(createCustomerInput));
        assertEquals("Field 'customer.pass' is null.", exception.getMessage());
    }

    @Test
    void testCreateCustomerWithLongPassword() {
        createCustomerInput = new CustomerPojo();
        createCustomerInput.firstName = "John";
        createCustomerInput.lastName = "Wick";
        createCustomerInput.login = "john_wick@gmail.com";
        createCustomerInput.password = "2345678qwerty";
        createCustomerInput.balance = 0;

        Exception exception = assertThrows(IllegalArgumentException.class, () -> customerManager.createCustomer(createCustomerInput));
        assertEquals("Password's length should be more or equal 6 symbols and less or equal 12 symbols.", exception.getMessage());
    }

    @Test
    void testCreateCustomerWithBusyLogin() {
        createCustomerInput = new CustomerPojo();
        createCustomerInput.firstName = "John";
        createCustomerInput.lastName = "Wick";
        createCustomerInput.login = "john_wick@gmail.com";
        createCustomerInput.password = "23456qwerty";
        createCustomerInput.balance = 0;

        CustomerPojo customerPojoFromDb = new CustomerPojo();
        customerPojoFromDb.login = createCustomerInput.login;
        ArrayList<CustomerPojo> arrayList = new ArrayList<>();
        arrayList.add(customerPojoFromDb);

        when(dbService.getCustomers()).thenReturn(arrayList);
        Exception exception = assertThrows(IllegalArgumentException.class, () -> customerManager.createCustomer(createCustomerInput));
        assertEquals("Login is already busy.", exception.getMessage());
    }

    @Test
    void getCustomersTest() {
        List<CustomerPojo> customerPojos = new ArrayList<>();
        when(dbService.getCustomers()).thenReturn(customerPojos);

        assertNotNull(customerManager.getCustomers());

        verify(dbService, times(1)).getCustomers();

        verifyNoMoreInteractions(dbService);
    }

    @Test
    void getCustomerTest() {
        UUID uuid = UUID.randomUUID();
        CustomerPojo customerPojo = new CustomerPojo();
        when(dbService.getCustomer(uuid)).thenReturn(customerPojo);

        CustomerPojo actual = customerManager.getCustomer(uuid);

        assertEquals(customerPojo, actual);

        verify(dbService, times(1)).getCustomer(uuid);

        verifyNoMoreInteractions(dbService);
    }

    @Test
    void lookupCustomerTest() {
        CustomerPojo customerPojo = new CustomerPojo();
        String login = "login";
        customerPojo.login = login;

        ArrayList<CustomerPojo> list = new ArrayList<>();
        list.add(customerPojo);

        when(dbService.getCustomers()).thenReturn(list);

        CustomerPojo actual = customerManager.lookupCustomer(login);
        assertEquals(customerPojo, actual);

        verify(dbService, times(1)).getCustomers();

        verifyNoMoreInteractions(dbService);
    }

    @Test
    void lookupCustomerNullTest() {
        String login = "login";

        ArrayList<CustomerPojo> list = new ArrayList<>();

        when(dbService.getCustomers()).thenReturn(list);

        CustomerPojo actual = customerManager.lookupCustomer(login);
        assertNull(actual);

        verify(dbService, times(1)).getCustomers();

        verifyNoMoreInteractions(dbService);
    }

    @Test
    void meAdminTest() {
        AuthenticatedUserDetails authenticatedUserDetails = mock(AuthenticatedUserDetails.class);
        when(authenticatedUserDetails.isAdmin()).thenReturn(true);

        ContactPojo admin = customerManager.me(authenticatedUserDetails);

        assertEquals(admin.login, Globals.ADMIN_LOGIN);

        verifyNoMoreInteractions(dbService);
    }

    @Test
    void meNotNullTest() {
        String name = "Ivan";

        CustomerPojo customerPojo = new CustomerPojo();
        customerPojo.password = "pass";

        AuthenticatedUserDetails authenticatedUserDetails = mock(AuthenticatedUserDetails.class);

        when(authenticatedUserDetails.isAdmin()).thenReturn(false);

        when(authenticatedUserDetails.getName()).thenReturn(name);
        when(dbService.getCustomerByLogin(name)).thenReturn(customerPojo);

        ContactPojo me = customerManager.me(authenticatedUserDetails);

        assertNotNull(me);

        assertNull(customerPojo.password);

        verify(dbService, times(1)).getCustomerByLogin(name);
        verifyNoMoreInteractions(dbService);
    }

    @Test
    void meNullTest() {
        String name = "Ivan";

        AuthenticatedUserDetails authenticatedUserDetails = mock(AuthenticatedUserDetails.class);

        when(authenticatedUserDetails.isAdmin()).thenReturn(false);

        when(authenticatedUserDetails.getName()).thenReturn(name);
        when(dbService.getCustomerByLogin(name)).thenReturn(null);

        ContactPojo me = customerManager.me(authenticatedUserDetails);

        assertNull(me);

        verify(dbService, times(1)).getCustomerByLogin(name);
        verifyNoMoreInteractions(dbService);
    }

    @Test
    void deleteCustomerTest(){
        UUID uuid = UUID.randomUUID();
        customerManager.deleteCustomer(uuid);

        verify(dbService, times(1)).deleteCustomer(uuid);
        verifyNoMoreInteractions(dbService);
    }

    @Test
    void topUpBalanceTest(){
        TopUpBalancePojo topUpBalancePojo = mock(TopUpBalancePojo.class);
        topUpBalancePojo.customerId = UUID.randomUUID();
        topUpBalancePojo.money = 10;

        CustomerPojo customerPojo = new CustomerPojo();
        customerPojo.balance = 0;

        when(dbService.getCustomer(topUpBalancePojo.customerId))
                .thenReturn(customerPojo);

        CustomerPojo customerPojo1 = customerManager.topUpBalance(topUpBalancePojo);

        assertNotNull(customerPojo1);

        assertEquals(10, customerPojo1.balance);

        verify(dbService, times(1)).getCustomer(topUpBalancePojo.customerId);
        verify(dbService, times(1)).editCustomer(customerPojo);

        verifyNoMoreInteractions(dbService);
    }

}
