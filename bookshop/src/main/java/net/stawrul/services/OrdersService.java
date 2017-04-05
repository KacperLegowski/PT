package net.stawrul.services;

import net.stawrul.model.Book;
import net.stawrul.model.Order;
import net.stawrul.services.exceptions.OutOfStockException;
import net.stawrul.services.exceptions.PriceException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;

/**
 * Komponent (serwis) biznesowy do realizacji operacji na zamówieniach.
 */
@Service
public class OrdersService extends EntityService<Order> {

    //Instancja klasy EntityManger zostanie dostarczona przez framework Spring
    //(wstrzykiwanie zależności przez konstruktor).
    public OrdersService(EntityManager em) {

        //Order.class - klasa encyjna, na której będą wykonywane operacje
        //Order::getId - metoda klasy encyjnej do pobierania klucza głównego
        super(em, Order.class, Order::getId);
    }

    /**
     * Pobranie wszystkich zamówień z bazy danych.
     *
     * @return lista zamówień
     */
    public List<Order> findAll() {
        return em.createQuery("SELECT o FROM Order o", Order.class).getResultList();
    }

    /**
     * Złożenie zamówienia w sklepie.
     * <p>
     * Zamówienie jest akceptowane, jeśli wszystkie objęte nim produkty są dostępne (przynajmniej 1 sztuka). W wyniku
     * złożenia zamówienia liczba dostępnych sztuk produktów jest zmniejszana o jeden. Metoda działa w sposób
     * transakcyjny - zamówienie jest albo akceptowane w całości albo odrzucane w całości. W razie braku produktu
     * wyrzucany jest wyjątek OutOfStockException.
     *
     * @param order zamówienie do przetworzenia
     */
    @Transactional
    public void placeOrder(Order order) {
        int full_price = 0;
            for (Book bookStub : order.getBooks()) {
                     Book book = em.find(Book.class, bookStub.getId());
                     full_price += book.getCena();
            }
            if(full_price<50){
                throw new PriceException("za mala cena zamowienia");
            }
            
            if(order.getBooks().size()<1){
                throw new PriceException("za malo ksiazek");
             }
            
        for (Book bookStub : order.getBooks()) {
            Book book = em.find(Book.class, bookStub.getId());
            if (book.getAmount() < 1) {
                //wyjątek z hierarchii RuntineException powoduje wycofanie transakcji (rollback)
                throw new OutOfStockException();
            } else {
                int newAmount = book.getAmount() - 1;
                book.setAmount(newAmount);
            }
        }

        //jeśli wcześniej nie został wyrzucony wyjątek OutOfStockException, zamówienie jest zapisywane w bazie danych
        save(order);
    }
}
