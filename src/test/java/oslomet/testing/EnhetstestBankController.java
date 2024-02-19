package oslomet.testing;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.web.bind.annotation.GetMapping;

import oslomet.testing.API.BankController;
import oslomet.testing.DAL.BankRepository;
import oslomet.testing.Models.Konto;
import oslomet.testing.Models.Kunde;
import oslomet.testing.Sikkerhet.Sikkerhet;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class EnhetstestBankController {

    @InjectMocks // Mocks the BankRepository and Sikkerhet classes
    // denne skal testes
    private BankController bankController;

    @Mock // Mocks are used to simulate the behavior of the real objects
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
        //act
        Kunde resultat = bankController.hentKundeInfo();
        // assert
        assertNull(resultat);
    }

    @Test
    public void hentKonti_LoggetInn()  { // Tests that the method returns the correct accounts if the user is logged in
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
    public void hentKonti_IkkeLoggetInn()  { // Tests that the method returns null for the 'konto' details if the user is not logged in
        // arrange

        when(sjekk.loggetInn()).thenReturn(null);

        // act
        List<Konto> resultat = bankController.hentKonti();

        // assert
        assertNull(resultat);
    }

    @Test 
    public void hentTransaksjoner_LoggetInn() {
        // arrange : here we are setting up the test
        String kontoNr = "12345678901", fraDato = "2023-01-01", tilDato = "2023-12-31";
        // The mock account number and dates to test since these are the parameters for the method
        Konto mockKonto = new Konto(); 

        when(sjekk.loggetInn()).thenReturn("12345678901"); // Simulates a user being logged in
        when(repository.hentTransaksjoner(kontoNr, fraDato, tilDato)).thenReturn(mockKonto); // Simulates the account being returned

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
        // as the method should not return any account details if the user is not logged in
    }

    @Test 
    public void hentSaldi_LoggetInn() {
        // arrange
        List<Konto> mockKonti = new ArrayList<>(); // Creates a list of konti to test
        when(sjekk.loggetInn()).thenReturn("12345678901");
        when(repository.hentSaldi(anyString())).thenReturn(mockKonti); // Simulates the account(s) being returned
        
        // act
        List<Konto> result = bankController.hentSaldi();

        // assert
        assertEquals(mockKonti, result); // Checks if the expected result is equal to the actual result
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

// Tests to add: 
    // registrerBetaling_LoggetInn and registrerBetaling_IkkeLoggetInn
    // hentBetalinger_LoggetInn and hentBetalinger_IkkeLoggetInn
    // utforBetaling_LoggetInn and utforBetaling_IkkeLoggetInn
    // hentKundeInfo_LoggetInn and hentKundeInfo_IkkeLoggetInn
}
