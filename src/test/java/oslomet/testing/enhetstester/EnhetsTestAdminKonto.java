package oslomet.testing.enhetstester;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import oslomet.testing.Sikkerhet.Sikkerhet;
import oslomet.testing.Models.Konto;
import oslomet.testing.DAL.AdminRepository;
import oslomet.testing.API.AdminKontoController;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull; // Adds the assertNull method for 
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class EnhetsTestAdminKonto {
  // TEST ALL OF THE METHODS IN THE AdminKontoController CLASS
  @InjectMocks
  private AdminKontoController controller;

  @Mock
  private AdminRepository repository;

  @Mock
  private Sikkerhet sjekk;

  // Tests for hentAlle method both if logged in and not logged in
  @Test
  public void test_hentAlleKontiLoggetInn() {
    // arrange
    String mockPNr = "12345678901"; // mock personnummer
    List<Konto> mockKonti = new ArrayList<>();
    when(sjekk.loggetInn()).thenReturn(mockPNr); // mock log in
    when(repository.hentAlleKonti()).thenReturn(mockKonti); // mocks returning accounts

    // act
    List<Konto> result = controller.hentAlleKonti(); // actual return of accounts

    // assert
    assertEquals(mockKonti, result); // Should be the same if it worked
  }

  @Test
  public void test_hentAlleIkkeLoggetInn() {
    // arrange
    when(sjekk.loggetInn()).thenReturn(null); // Returns null because null means not logged in

    // act
    List<Konto> result = controller.hentAlleKonti(); // tries to get all accounts when not logged in

    // assert
    assertNull(result); // Should be null since user is not logged in
  }

  // Tests for registrerKonto methods for the different cases: logged in, not logged in, and error/missing PNr
  @Test
  public void test_registrerKonto_LoggedIn() {
    // arrange
    Konto mockKonto = new Konto("12345678901", "12345", 1000, "Sparing", "NOK", new ArrayList<>());
    when(sjekk.loggetInn()).thenReturn(mockKonto.getPersonnummer()); // mock log in
    when(repository.registrerKonto(mockKonto)).thenReturn("OK"); // mock registering account

    // act
    String result = controller.registrerKonto(mockKonto); // actual attempt to register account

    // assert
    assertEquals("OK", result); // Should be OK if it worked
  }

  @Test
  public void test_registrerKonto_Error() {
    // arrange
    Konto mockKonto = new Konto("12345678901", "12345", 1000, "Sparing", "NOK", new ArrayList<>());
    when(sjekk.loggetInn()).thenReturn(mockKonto.getPersonnummer()); // mock log in
    when(repository.registrerKonto(mockKonto)).thenReturn("Feil"); // mock error :
    // enten ingen pnr eller hvis det er noe feil med SQL query returnerer begge
    // "Feil"

    // act
    String result = controller.registrerKonto(mockKonto);

    // assert
    assertEquals("Feil", result); // Should be "Feil" since we arranged an error
  }

  @Test
  public void test_registrerKonto_NotLoggedIn() {
    // arrange
    Konto mockKonto = new Konto("12345678901", "12345", 1000, "Sparing", "NOK", new ArrayList<>());
    when(sjekk.loggetInn()).thenReturn(null); // mock not being logged in

    // act
    String result = controller.registrerKonto(mockKonto); // should return "Ikke innlogget"

    // assert
    assertEquals("Ikke innlogget", result); // Should be null since user is not logged in
  }



  // Tests endreKonto method for all the different cases: logged in, not logged in, error, missing PNr and missing KontoNr
  @Test
  public void test_endreKonto_loggetInn() {
    // arrange
    Konto mockKonto = new Konto("12345678901", "12345", 1000, "Sparing", "NOK", new ArrayList<>());
    when(sjekk.loggetInn()).thenReturn(mockKonto.getPersonnummer()); // mock log in
    when(repository.endreKonto(mockKonto)).thenReturn("OK"); // mocks successful change

    // act
    String result = controller.endreKonto(mockKonto); // actual attempt to change account

    // assert
    assertEquals("OK", result); // Should be OK if it worked
  }


  @Test
  public void test_endreKonto_ikkeLoggetInn() {
    // arrange
    Konto mockKonto = new Konto(); // doens't matter cause we're not logged in
    when(sjekk.loggetInn()).thenReturn(null); // mock not being logged in

    // act
    String result = controller.endreKonto(mockKonto); // should return "Ikke innlogget"

    // assert
    assertEquals("Ikke innlogget", result);
  }


  @Test
  public void test_endreKonto_FeilPNr() {
    // arrange
    Konto mockKonto = new Konto("", "12345", 1000, "Sparing", "NOK", new ArrayList<>());
    when(sjekk.loggetInn()).thenReturn(mockKonto.getPersonnummer()); // mock log in
    when(repository.endreKonto(mockKonto)).thenReturn("Feil i personnummer"); // mocks error

    // act
    String result = controller.endreKonto(mockKonto); // attempt to change account with missing pnr

    // assert
    assertEquals("Feil i personnummer", result); // Should be "Feil i personnummer" since we arranged an error
  }

  @Test
  public void test_endreKonto_feilKontoNr() {
    // arrange
    Konto mockKonto = new Konto("12345678901", "", 1000, "Sparing", "NOK", new ArrayList<>());
    // purposely no kontonummer
    when(sjekk.loggetInn()).thenReturn(mockKonto.getPersonnummer()); // mock log in
    when(repository.endreKonto(mockKonto)).thenReturn("Feil i kontonummer"); // mocks error

    // act
    String result = controller.endreKonto(mockKonto); // attempt to change account without kontonr

    // assert
    assertEquals("Feil i kontonummer", result); // Same as above test but with kontonr
  }

  @Test
  public void test_endreKonto_error () {
    // arrange
    Konto mockKonto = new Konto("12345678901", "12345", 1000, "Sparing", "NOK", new ArrayList<>());
    when(sjekk.loggetInn()).thenReturn(mockKonto.getPersonnummer()); // mock log in
    when(repository.endreKonto(mockKonto)).thenReturn("Feil"); // mocks exception catch return

    // act
    String result = controller.endreKonto(mockKonto); // attempt to change account

    // assert
    assertEquals("Feil", result); // Exception e return is "Feil" so it should match
  }

  @Test
  public void test_slettKonto_loggetInn() {
    // arrange
    String KontoNr = "12345";
    when(sjekk.loggetInn()).thenReturn("12345678901"); // mock log-in
    when(repository.slettKonto(KontoNr)).thenReturn("OK"); // mock successful delete

    // act
    String result = controller.slettKonto(KontoNr); // attempt to delete account

    // assert
    assertEquals("OK", result); // Should be OK if it worked
  }

  @Test
  public void test_slettKonto_ikkeLoggetInn() {
    // arrange
    String kontoNr = "12345";
    when(sjekk.loggetInn()).thenReturn(null); // mock not being logged in

    // act
    String result = controller.slettKonto(kontoNr); // attempt to delete account when not logged in

    // assert
    assertEquals("Ikke innlogget", result); //
  }

  @Test
  public void test_slettKonto_error() {
    // arrange
    String kontoNr = "12345";
    when(sjekk.loggetInn()).thenReturn("12345678901"); // mock log-in
    when(repository.slettKonto(kontoNr)).thenReturn("Feil kononummer"); // mocks error with the elusive typo

    // act
    String result = controller.slettKonto(kontoNr); // attempts to delete

    // assert
    assertEquals("Feil kononummer", result); // We love typos
  }


}
