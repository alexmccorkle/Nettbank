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

    @Test // Checks if the user is logged in
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

    @Test // Check if user is logged out
    public void test_sjekkLoggetUt() {
      sikkerhetsController.loggUt(); // This method should set the session attribute to null if the user logs out and it works
      assertNull(session.getAttribute("Innlogget")); 
      // This method should check if the user is logged out by checking if the corresponding session attribute is null
      // Could also be written as assertEquals(null, session.getAttribute("Innlogget")); but assertNull is more readable
    }

    @Test // Check if admin and valid
    public void test_sjekkAdminLoggetInn() {
    }

      // YES DO THE TESTS FOR THIS CLASS TOO
}