package net.stawrul;

import net.stawrul.model.Book;
import net.stawrul.model.Order;
import net.stawrul.services.OrdersService;
import net.stawrul.services.exceptions.OutOfStockException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import javax.persistence.EntityManager;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.times;

@RunWith(MockitoJUnitRunner.class)
public class OrdersServiceTest {

    @Mock
    EntityManager em;

    @Test(expected = OutOfStockException.class)
    public void whenOrderedBookNotAvailable_placeOrderThrowsOutOfStockEx() {
        //Arrange
        Order order = new Order();
        Book book = new Book();
        book.setAmount(0);
        order.getBooks().add(book);

        Mockito.when(em.find(Book.class, book.getId())).thenReturn(book);

        OrdersService ordersService = new OrdersService(em);

        //Act
        ordersService.placeOrder(order);

        //Assert - exception expected
    }

    @Test
    public void whenOrderedBookAvailable_placeOrderDecreasesAmountByOne() {
        //Arrange
        Order order = new Order();
        Book book = new Book();
        book.setAmount(1);
        order.getBooks().add(book);

        Mockito.when(em.find(Book.class, book.getId())).thenReturn(book);

        OrdersService ordersService = new OrdersService(em);

        //Act
        ordersService.placeOrder(order);

        //Assert
        //dostępna liczba książek zmniejszyła się:
        assertEquals(0, (int)book.getAmount());
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
