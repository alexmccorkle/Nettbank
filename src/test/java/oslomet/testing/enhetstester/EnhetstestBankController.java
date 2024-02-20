package oslomet.testing.enhetstester;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

// Not used:
// import org.springframework.web.bind.annotation.GetMapping; 
// import org.springframework.web.bind.annotation.PostMapping;
// import org.springframework.web.bind.annotation.RequestBody;

import oslomet.testing.API.BankController;
import oslomet.testing.DAL.BankRepository;
import oslomet.testing.Models.Konto;
import oslomet.testing.Models.Kunde;
import oslomet.testing.Models.Transaksjon;
import oslomet.testing.Sikkerhet.Sikkerhet;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class EnhetstestBankController {

    @InjectMocks
    // denne skal testes
    private BankController bankController;

    @Mock
    // denne skal Mock'es
    private BankRepository repository;

    @Mock
    // denne skal Mock'es
    private Sikkerhet sjekk;

    @Test
    public void hentKundeInfo_loggetInn() { // Tests that method returns correct info if the user is logged in
        // arrange
        Kunde enKunde = new Kunde("01010110523",
                "Lene", "Jensen", "Askerveien 22", "3270",
                "Asker", "22224444", "HeiHei");

        when(sjekk.loggetInn()).thenReturn("01010110523");

        when(repository.hentKundeInfo(anyString())).thenReturn(enKunde);
        // act
        Kunde resultat = bankController.hentKundeInfo();
        // assert
        assertEquals(enKunde, resultat);
    }

    @Test
    public void hentKundeInfo_IkkeloggetInn() { // Tests that the method returns null if the user is not logged in
        // arrange
        when(sjekk.loggetInn()).thenReturn(null);
        // act
        Kunde resultat = bankController.hentKundeInfo();
        // assert
        assertNull(resultat);
    }

    // Tests that the method returns the konti (accounts) if the user is logged
    // in/logged out
    @Test
    public void hentKonti_LoggetInn() {
        // arrange
        List<Konto> konti = new ArrayList<>(); // Creates a list of accounts to test, these
        Konto konto1 = new Konto("105010123456", "01010110523",
                720, "Lønnskonto", "NOK", null);
        Konto konto2 = new Konto("105010123456", "12345678901",
                1000, "Lønnskonto", "NOK", null);
        konti.add(konto1);
        konti.add(konto2);

        when(sjekk.loggetInn()).thenReturn("01010110523");

        when(repository.hentKonti(anyString())).thenReturn(konti);

        // act
        List<Konto> resultat = bankController.hentKonti();

        // assert
        assertEquals(konti, resultat);
    }

    @Test
    public void hentKonti_IkkeLoggetInn() {
        // arrange

        when(sjekk.loggetInn()).thenReturn(null);

        // act
        List<Konto> resultat = bankController.hentKonti();

        // assert
        assertNull(resultat);
    }

    // Tests the method that returns transactions if the user is logged in/logged
    // out
    @Test
    public void hentTransaksjoner_LoggetInn() {
        // arrange : here we are setting up the test
        String kontoNr = "12345678901", fraDato = "2023-01-01", tilDato = "2023-12-31";
        // The mock account number and dates to test since these are the parameters for
        // the method

        Konto mockKonto = new Konto("105010123456", "01010110523",
                720, "Lønnskonto", "NOK", null);

        when(sjekk.loggetInn()).thenReturn("12345678901"); // Simulates a user being logged in
        when(repository.hentTransaksjoner(kontoNr, fraDato, tilDato)).thenReturn(mockKonto);
        // Simulates the account being returned

        // act : here we are calling the method we want to test
        Konto result = bankController.hentTransaksjoner(kontoNr, fraDato, tilDato); // Expected result

        // assert : here we are checking if the method returns the expected result
        assertEquals(mockKonto, result); // Checks if the expected result is equal to the actual result
    }

    @Test
    public void hentTransaksjoner_ikkeLoggetInn() {
        // arrange
        String kontoNr = "12345678901", fraDato = "2023-01-01", tilDato = "2023-12-31";
        // Same as loggetInn test

        when(sjekk.loggetInn()).thenReturn(null); // The user is not logged in
        // act
        Konto result = bankController.hentTransaksjoner(kontoNr, fraDato, tilDato);

        // assert
        assertNull(result);
        // The result should be null if the user is not logged in
        // as the method should not return any account details if the user is not logged
        // in
    }

    // Tests the method that returns saldi (balances) if the user is logged
    // in/logged out
    @Test
    public void hentSaldi_LoggetInn() {
        // arrange
        List<Konto> mockKonti = new ArrayList<>(); // Creates a list of konti to test
        Konto konto1 = new Konto("105010123456", "01010110523",
                720, "Lønnskonto", "NOK", null);
        Konto konto2 = new Konto("105010123456", "12345678901",
                1000, "Lønnskonto", "NOK", null);
        mockKonti.add(konto1);
        mockKonti.add(konto2);

        when(sjekk.loggetInn()).thenReturn("12345678901");
        when(repository.hentSaldi(anyString())).thenReturn(mockKonti); // Simulates the account(s) being returned

        // act
        List<Konto> result = bankController.hentSaldi();

        // assert
        assertEquals(mockKonti, result);
    }

    @Test
    public void hentSaldi_IkkeLoggetInn() {
        // arrange
        when(sjekk.loggetInn()).thenReturn(null); // Not logged in

        // act
        List<Konto> result = bankController.hentSaldi();

        // arrange
        assertNull(result); // The result should be null if the user is not logged in
    }

    // Tests the method that registers a payment if the user is logged in/logged out
    // ALso tests if if betaling(transaksjon) is registered successfully or not
    @Test
    public void registrerBetalingOK_LoggetInn() {
        // arrange
        Transaksjon mockTransaksjon = new Transaksjon(); // Mock transaction
        when(sjekk.loggetInn()).thenReturn("12345678901"); // Logged in
        when(repository.registrerBetaling(any(Transaksjon.class))).thenReturn("OK");
        // Simulates the transaction being registered

        // act
        String result = bankController.registrerBetaling(mockTransaksjon); // Simulating a registration

        // assert
        assertEquals("OK", result); // The result should be "OK" if the transaction is registered
    }

    @Test
    public void registrerBetalingFeil_LoggetInn() {
        // arrange
        Transaksjon mockTransaksjon = new Transaksjon(); // Mock transaction
        when(sjekk.loggetInn()).thenReturn("12345678901"); // Logged in
        when(repository.registrerBetaling(any(Transaksjon.class))).thenReturn("Feil");
        // ^^ Simulates the transaction not being registered or something going wrong

        // act
        String result = bankController.registrerBetaling(mockTransaksjon);

        // assert
        assertEquals("Feil", result); // The result of a failed transaction when logged in should be "Feil"
    }

    @Test
    public void registrerBetaling_ikkeLoggetInn() {
        // arrange
        Transaksjon mock = new Transaksjon(); // Mock transaction
        when(sjekk.loggetInn()).thenReturn(null); // Not logged in

        // act
        String result = bankController.registrerBetaling(mock);

        // assert
        assertNull(result); // The result should be null if the user is not logged in
    }

    // Tests the method that returns list of betalinger(transactions) if the user is
    // logged in/logged out
    @Test
    public void hentBetalinger_loggetInn() {
        // arrange

        int txID = 1;
        String fraTilKontonummer = "12345678901";
        double belop = 1000;
        String dato = "2023-01-01";
        String melding = "Test";
        String avventer = "OK";
        String kontonummer = "12345678901";

        List<Transaksjon> mock = new ArrayList<>(); // Mock list of transactions
        Transaksjon mockTransaksjon = new Transaksjon(txID, fraTilKontonummer, belop, dato, melding, avventer, kontonummer);
        // int txID, String fraTilKontonummer, double belop, String dato, String
        // melding, String avventer,
        // String kontonummer
        mock.add(mockTransaksjon);


        when(sjekk.loggetInn()).thenReturn("12345678901");
        when(repository.hentBetalinger(anyString())).thenReturn(mock);

        // act
        List<Transaksjon> result = bankController.hentBetalinger(); // Should return a list of transactions

        // assert
        assertEquals(mock, result);
    }

    @Test
    public void hentBetalinger_ikkeLoggetInn() {
        // arrange
        when(sjekk.loggetInn()).thenReturn(null);

        // act
        List<Transaksjon> result = bankController.hentBetalinger();

        // assert
        assertNull(result);
    }

    // Tests the method that performs a betaling(transaction) if the user is logged
    // in/logged out
    @Test
    public void utforBetaling_LoggetInn() {
        // arrange
        int txID = 1; // Mocks a transaction ID


        List<Transaksjon> mock = new ArrayList<>(); // Mock list of transactions

        when(sjekk.loggetInn()).thenReturn("12345678901");
        when(repository.utforBetaling(txID)).thenReturn("OK"); // Simulates a successful transaction being performed
        // Even though theres no test for if theres enough money in the account to
        // perform the transaction, we assume there is

        // act
        List<Transaksjon> result = bankController.utforBetaling(txID);

        // assert
        assertEquals(mock, result); // The result should be a list of transactions
    }

    @Test
    public void utforBetaling_ikkeLoggetInn() {
        // arrange
        int txID = 1;
        when(sjekk.loggetInn()).thenReturn(null); // as per usual, not logged in

        // act
        List<Transaksjon> result = bankController.utforBetaling(txID);

        // assert
        assertNull(result); // as per, the result should be null if the user is not logged in
    }

    // Tests the method that updates the customer's info if the user is logged
    // in/logged out
    @Test
    public void endreKundeInfo_loggetInn() {
        // arrange
        Kunde mockKunde = new Kunde("12345678901",
        "Lene", "Jensen", "Askerveien 22", "3270",
        "Asker", "22224444", "HeiHei");

        when(sjekk.loggetInn()).thenReturn("12345678901");
        when(repository.endreKundeInfo(any(Kunde.class))).thenReturn("OK");
        // Simulates successfully updating their info since OK is only returned if
        // successful

        // act
        String result = bankController.endre(mockKunde);

        // assert
        assertEquals("OK", result);
        // The result should be "OK" if the user is logged in and the info is
        // successfully updated

    }

    @Test
    public void endreKundeInfo_ikkeLoggetInn() {
        // arrange
        Kunde mockKunde = new Kunde("12345678901",
        "Lene", "Jensen", "Askerveien 22", "3270",
        "Asker", "22224444", "HeiHei");
        when(sjekk.loggetInn()).thenReturn(null); // Not logged in

        // act
        String result = bankController.endre(mockKunde);

        // assert
        assertNull(result);
    }
}
