package commons;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TotalDebtTest {
    Participant receiver =
            new Participant("Emma", "Emma@hotmail.com",
                    "ES9121000418450200051332", "CAIXESBBXXX");
    Participant p1 =
            new Participant("Alice", "Alice@domain.com",
                    "GB33BUKB20201555555555", "ZUOBJEO6XXX");
    Participant p2 =
            new Participant("Bob", "Bob@example.com",
                    "US12200066123456789001", "BOFAUS3NXXX");
    Participant p3 =
            new Participant("Charlie", "Charlie@gmail.com",
                    "DE89370400440532013000", "COBADEFFXXX");
    Participant p4 =
            new Participant("David", "David@yahoo.com",
                    "FR1420041010050500013M02606", "BOUSFRPPXXX");
    Currency c1 = new Currency("EUR");
    List<Participant> l1 = new ArrayList<>();
    TotalDebt t1;

    @BeforeEach
    void setUp() {
        l1.add(p1);
        l1.add(p2);
        l1.add(p3);
        l1.add(p4);
        t1 = new TotalDebt(BigDecimal.valueOf(172), c1, receiver, l1.size());
    }


    @Test
    void getToBePaid() {
        assertEquals(BigDecimal.valueOf(172), t1.getToBePaid(), "Incorrect amount to be paid");
    }

    @Test
    void setToBePaid() {
        t1.setToBePaid(BigDecimal.valueOf(200));
        assertEquals(BigDecimal.valueOf(200), t1.getToBePaid(), "Setting to be paid amount failed");
    }

    @Test
    void getCurrency() {
        assertEquals(c1, t1.getCurrency(), "Incorrect currency");
    }

    @Test
    void setCurrency() {
        Currency newCurrency = new Currency("USD");
        t1.setCurrency(newCurrency);
        assertEquals(newCurrency, t1.getCurrency(), "Setting currency failed");
    }

    @Test
    void getReceiver() {
        assertEquals(receiver, t1.getReceiver(), "Incorrect receiver");
    }

    @Test
    void setReceiver() {
        Participant newReceiver = new Participant("John", "john@example.com", "GB33BUKB20201555555555",
                "ZUOBJEO6XXX");
        t1.setReceiver(newReceiver);
        assertEquals(newReceiver, t1.getReceiver(), "Setting receiver failed");
    }

    @Test
    void getNoParticipants() {
        assertEquals(4, t1.getNoParticipants(), "Incorrect number of participants");
    }

    @Test
    void setNoParticipants() {
        t1.setNoParticipants(3);
        assertEquals(3, t1.getNoParticipants(), "Setting number of participants failed");
    }

    @Test
    void testEquals() {
        Participant receiver1= new Participant("Emma", "Emma@hotmail.com",
                "ES9121000418450200051332", "CAIXESBBXXX");
        TotalDebt t2 = new TotalDebt(new BigDecimal(172), new Currency("EUR"), receiver1, 4);
        assertFalse(t1.equals(t2), "Two totaldebts with the same params should not be equal, " +
                "the reference is most important");
    }

    @Test
    void testHashCode() {
        TotalDebt t2 = new TotalDebt(new BigDecimal(172), c1 , receiver, 4);
        assertEquals(t1.hashCode(), t2.hashCode(), "Hash codes should be equal");
    }

    @Test
    void testToString() {
        String expectedString = "TotalDebt{toBePaid=172, currency=Currency{name='null'}, " +
                "receiver=Participant{name='Emma', email='Emma@hotmail.com', iban='ES9121000418450200051332', " +
                "bic='CAIXESBBXXX'}, noParticipants=4}";
        assertEquals(expectedString, t1.toString(), "Incorrect toString implementation");
    }
}
