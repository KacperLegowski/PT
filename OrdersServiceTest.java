package net.stawrul;

import net.stawrul.model.Lenses;
import net.stawrul.model.Order;
import net.stawrul.services.OrdersService;
import net.stawrul.services.exceptions.OutOfStockException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import javax.persistence.EntityManager;
import net.stawrul.services.exceptions.NotEnoughCostOrder;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.times;

@RunWith(MockitoJUnitRunner.class)
public class OrdersServiceTest {

    @Mock
    EntityManager em;

    @Test(expected = OutOfStockException.class)
    public void whenOrderIsNul_ThrowOutOfStockException() {
        //Arrange
        Order order = new Order();
     
        OrdersService ordersService = new OrdersService(em);
        //Act
        
        ordersService.placeOrder(order);

    }
    
    @Test(expected = NotEnoughCostOrder.class)
    public void whenCostIsLessThan50_PlaceOrderthrowsOutOfStockException() {
        //Arrange
        Order order = new Order();
        Lenses lenses = new Lenses();
        lenses.setAmount(1);
        lenses.setCost(45);
        order.getLenses().add(lenses);
        
        Mockito.when(em.find(Lenses.class, lenses.getId())).thenReturn(lenses);
        
        OrdersService ordersService = new OrdersService(em);
        //Act
        
        ordersService.placeOrder(order);
    }
    
    @Test
    public void whenOrderedLensesAvailableAndMoreThan1_placeOrderDecreasesAmountByOne() {
        //Arrange
        Order order = new Order();
        Lenses lenses = new Lenses();
        lenses.setAmount(6);
        lenses.setCost(100);
        order.getLenses().add(lenses);

        Mockito.when(em.find(Lenses.class, lenses.getId())).thenReturn(lenses);

        OrdersService ordersService = new OrdersService(em);
        //Act
        ordersService.placeOrder(order);
        //Assert
        //dostępna liczba książek zmniejszyła się:
        assertEquals(5, (int)lenses.getAmount());
        //nastąpiło dokładnie jedno wywołanie em.persist(order) w celu zapisania zamówienia:
        Mockito.verify(em, times(1)).persist(order);
    }

    @Test(expected = OutOfStockException.class)
    public void whenOrderedLensesNotAvailable_placeOrderThrowsOutOfStockEx() {
        //Arrange
        Order order = new Order();
        Lenses lenses = new Lenses();
        lenses.setAmount(0);
        lenses.setCost(100);
        order.getLenses().add(lenses);

        Mockito.when(em.find(Lenses.class, lenses.getId())).thenReturn(lenses);

        OrdersService ordersService = new OrdersService(em);

        //Act
        ordersService.placeOrder(order);

        //Assert - exception expected
    }
    
    
    
    @Test
    public void whenOrderedLensesAvailable_placeOrderDecreasesAmountByOne() {
        //Arrange
        Order order = new Order();
        Lenses lenses = new Lenses();
        lenses.setAmount(1);
        lenses.setCost(100);
        order.getLenses().add(lenses);

        Mockito.when(em.find(Lenses.class, lenses.getId())).thenReturn(lenses);

        OrdersService ordersService = new OrdersService(em);

        //Act
        ordersService.placeOrder(order);

        //Assert
        //dostępna liczba książek zmniejszyła się:
        assertEquals(0, (int)lenses.getAmount());
        //nastąpiło dokładnie jedno wywołanie em.persist(order) w celu zapisania zamówienia:
        Mockito.verify(em, times(1)).persist(order);
    }

    @Test
    public void whenGivenLowercaseString_toUpperReturnsUppercase() {

        //Arrange
        String lower = "abcdef";

        //Act
        String result = lower.toUpperCase();

        //Assert
        assertEquals("ABCDEF", result);
    }
}
