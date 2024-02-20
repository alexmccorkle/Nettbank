package oslomet.testing.enhetstester;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.mock.web.MockHttpSession;
import oslomet.testing.DAL.BankRepository;
import oslomet.testing.Sikkerhet.Sikkerhet;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull; // Adds the assertNull method for 
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class EnhetstestSikkerhet {

    @InjectMocks
    // denne skal testes
    private Sikkerhet sikkerhetsController;

    @Mock
    // denne skal Mock'es
    private BankRepository repository;

    @Mock
    // denne skal Mock'es
    private MockHttpSession session;

    // Checks that log in method works
    // Done by Professor
    @Test 
    public void test_sjekkLoggetInn() {
        // arrange
        when(repository.sjekkLoggInn(anyString(),anyString())).thenReturn("OK");

        // setningen under setter ikke attributten, dvs. at det ikke er mulig å sette en attributt i dette oppsettet
        session.setAttribute("Innlogget", "12345678901");

        // act
        String resultat = sikkerhetsController.sjekkLoggInn("12345678901","HeiHeiHei");
        // assert
        assertEquals("OK", resultat);
    }

    // Test to see log out method works correctly and sets the session attribute to null
    @Test 
    public void test_loggUt() {
      // arrange
      session.setAttribute("Innlogget", "12345678901"); // simulates a user logging in

      // act
      sikkerhetsController.loggUt(); // if log out works, the session attribute is set to null

      // assert
      assertNull(session.getAttribute("Innlogget")); // if the session attribute is null, the user is logged out
    }

    // Test for logging in as admin with correct/incorrect credentials
    @Test 
    public void test_loggInnAdminOK() {
      // arrange
      String mockBruker = "Admin"; // mock username and passwordd
      String mockPassord = "Admin";

      MockHttpSession session = new MockHttpSession(); // creates a mock session for the user
      sikkerhetsController.setSession(session);
      
      // act
      String resultat = sikkerhetsController.loggInnAdmin(mockBruker, mockPassord); 
        // if the username and password are correct, the user is logged in and shoudl return "Logget inn"

      // assert

      assertEquals("Admin", session.getAttribute("Innlogget")); 
      assertEquals("Logget inn", resultat); 
        // checks that user is logged in and attribute is correctly set to "Admin"
    }

    @Test
    public void test_loggInnAdminFeil() {
      // arrange
      String mockFeilBruker = "jeg er ikke Admin"; // mock username and password
      String mockFeilPassord = "jeg er ikke Admin";

      // act
      String result = sikkerhetsController.loggInnAdmin(mockFeilBruker, mockFeilPassord);
        // if user and password is wrong, should return "Ikke logget inn"

      // assert
      assertEquals("Ikke logget inn", result);
      assertNull(session.getAttribute("Innlogget"));
        // checks that user is not logged in and attribute is correctly set to null
    }

    @Test
    public void test_loggetInn() {
      // arramge


      // act

      
      // assert
    }




      // YES DO THE TESTS FOR THIS CLASS TOO
}