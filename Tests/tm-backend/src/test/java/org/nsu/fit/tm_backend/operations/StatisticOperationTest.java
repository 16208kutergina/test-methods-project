package org.nsu.fit.tm_backend.operations;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.nsu.fit.tm_backend.database.data.CustomerPojo;
import org.nsu.fit.tm_backend.database.data.SubscriptionPojo;
import org.nsu.fit.tm_backend.manager.CustomerManager;
import org.nsu.fit.tm_backend.manager.SubscriptionManager;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class StatisticOperationTest {
    // Лабораторная 2: покрыть юнит тестами класс StatisticOperation на 100%.
    CustomerManager customerManager;
    SubscriptionManager subscriptionManager;
    List<UUID> customerIds;

    StatisticOperation statisticOperation;

    @BeforeEach
    void init() {
        customerManager = mock(CustomerManager.class);
        subscriptionManager = mock(SubscriptionManager.class);
        customerIds = new ArrayList<>();

        statisticOperation = new StatisticOperation(customerManager, subscriptionManager, customerIds);
    }

    @Test
    void constructTest() {
        StatisticOperation statisticOperation = new StatisticOperation(customerManager, subscriptionManager, customerIds);

        assertNotNull(statisticOperation);

        verifyNoInteractions(customerManager, subscriptionManager);
    }

    @Test
    void constructTestNullCustomerManager() {
        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                new StatisticOperation(null, subscriptionManager, customerIds));
        assertEquals("customerManager", exception.getMessage());
    }

    @Test
    void constructTestNullSubscriptionManager() {
        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                new StatisticOperation(customerManager, null, customerIds));
        assertEquals("subscriptionManager", exception.getMessage());
    }

    @Test
    void constructTestNullCustomerIds() {
        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                new StatisticOperation(customerManager, subscriptionManager, null));
        assertEquals("customerIds", exception.getMessage());
    }

    @Test
    void ExecuteTest() {
        UUID uuid = UUID.randomUUID();
        customerIds.add(uuid);

        CustomerPojo customer = new CustomerPojo();
        customer.balance = 10;

        when(customerManager.getCustomer(uuid)).thenReturn(customer);

        SubscriptionPojo subscriptionPojo = new SubscriptionPojo();
        subscriptionPojo.planFee = 15;

        List<SubscriptionPojo> list = new ArrayList<>();
        list.add(subscriptionPojo);

        when(subscriptionManager.getSubscriptions(uuid)).thenReturn(list);

        StatisticOperation.StatisticOperationResult result = statisticOperation.Execute();

        assertNotNull(result);
        assertEquals(10, result.overallBalance);
        assertEquals(15, result.overallFee);

        verify(customerManager, times(1)).getCustomer(uuid);
        verify(subscriptionManager, times(1)).getSubscriptions(uuid);

        verifyNoMoreInteractions(customerManager, subscriptionManager);
    }
}
