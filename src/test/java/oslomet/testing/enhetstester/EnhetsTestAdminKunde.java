package oslomet.testing.enhetstester;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import oslomet.testing.Sikkerhet.Sikkerhet;
import oslomet.testing.Models.Kunde;
import oslomet.testing.DAL.AdminRepository;
import oslomet.testing.API.AdminKundeController;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull; // Adds the assertNull method for 
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class EnhetsTestAdminKunde {

    @InjectMocks
    // denne skal testes
    private AdminKundeController controller;

    @Mock
    // denne skal Mock'es
    private AdminRepository repository;

    @Mock
    // Mock'es
    private Sikkerhet sjekk;


  // Test to see if hentAlle method returns all customers if logged in
  @Test 
  public void test_hentAlleLoggetInn() {
    // arrange
    String mockPNr = "12345678901"; // mock personnummer
    List<Kunde> mockKunder = new ArrayList<>();
    when(sjekk.loggetInn()).thenReturn(mockPNr); //mocks logging in
    when(repository.hentAlleKunder()).thenReturn(mockKunder); // mocks returning all customers

    // act
    List<Kunde> result = controller.hentAlle(); // returns all customers

    // assert
    assertEquals(mockKunder, result); // Should be the same if it worked
  }

  // Test to see if hentAlle method returns null if not logged in
  @Test
  public void test_hentAlleIkkeLoggetInn() {
    // arrange
    when(sjekk.loggetInn()).thenReturn(null); // mocks not being logged in (returning null means not logged in)

    // act
    List<Kunde> result = controller.hentAlle(); // (tries) to return all customers

    // assert
    assertNull(result); // Should be null if not logged in
  }


  // Test to see if lagreKunde method works if logged in
  @Test
  public void test_lagreKundeLoggetInn() {
    // arrange
    String mockPNr = "12345678901"; // mock personnummer
    Kunde mockKunde = new Kunde("12345678901",
    "Lene", "Jensen", "Askerveien 22", "3270",
    "Asker", "22224444", "HeiHei");

    when(sjekk.loggetInn()).thenReturn(mockPNr); // mocks logging in
    when(repository.registrerKunde(mockKunde)).thenReturn("OK"); // mocks registering a customer

    // act
    String result = controller.lagreKunde(mockKunde); // registers a customer

    // assert
    assertEquals("OK", result); // Should be "OK" if it worked
  }

  // Test to see if lagreKunde method returns "Ikke logget inn" if not logged in
  @Test
  public void test_lagreKundeIkkeLoggetInn() {
    // arrange
    when(sjekk.loggetInn()).thenReturn(null); // mocks not being logged in

    // act
    String result = controller.lagreKunde(new Kunde());

    // assert
    assertEquals("Ikke logget inn", result); // Should be "Ikke logget inn" if not logged in
  }


  // Test to see if endre method works if logged in
  @Test
  public void test_endreLoggetInn() {
    // arrange
    String mockPNr = "12345678901"; // mock pnummer
    Kunde mockKunde = new Kunde("12345678901",
    "Lene", "Jensen", "Askerveien 22", "3270",
    "Asker", "22224444", "HeiHei");

    when(sjekk.loggetInn()).thenReturn(mockPNr); // mock log in
    when(repository.endreKundeInfo(mockKunde)).thenReturn("OK"); // mocks endring

    // act
    String result = controller.endre(mockKunde);

    // assert
    assertEquals("OK", result); // Should be "OK" if it worked

  }

  // Test to see if endre method returns "Ikke logget inn" if not logged in
  @Test
  public void test_endreIkkeLoggetInn() {
    // arrange
    Kunde mockKunde = new Kunde("12345678901",
    "Lene", "Jensen", "Askerveien 22", "3270",
    "Asker", "22224444", "HeiHei");
    when(sjekk.loggetInn()).thenReturn(null); // null = not logged in

    // act
    String result = controller.endre(mockKunde);

    // assert
    assertEquals("Ikke logget inn", result); // Should be "Ikke logget inn" if not logged in
  }


  // Test to see if slett method works if logged in
  @Test
  public void test_slettLoggetInn() {
    // arramge
    String mockPNr = "12345678901"; // mock pnummer

    when(sjekk.loggetInn()).thenReturn(mockPNr); // mock log in
    when(repository.slettKunde(mockPNr)).thenReturn("OK"); // mocks slett


    // act
    String result = controller.slett(mockPNr);

    // assert
    assertEquals("OK", result); // If it worked, it should return "OK"
  }

  // Test to see if slett method returns "Ikke logget inn" if not logged in
  @Test
  public void test_slettIkkeLoggetInn() {
    // arrange
    String mockPNr = "69696969696"; // mock personnummer
    when(sjekk.loggetInn()).thenReturn(null);  // null = not logged in


    // act
    String result = controller.slett(mockPNr);

    //assert
    assertEquals("Ikke logget inn", result); // If not logged in, it should return "Ikke logget inn"
  }
}