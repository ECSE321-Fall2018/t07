package ca.mcgill.ecse321.ecse321_group7;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
    }

    //Testing some helper methods to ensure database is filled with content that can be easily searched or is properly formatted
    //Or helping to make sure content from that database is formatted nicely when pulled out for display
    @Test
    public void testPhoneNumber(){
        String samplePhoneNumber = "123-456-7890";
        //Note that false is returned if it is not rejected and true if it is rejected
        assertFalse(User_Signup.checkPhoneNumber(samplePhoneNumber));

        samplePhoneNumber = "1234567890";
        assertFalse(User_Signup.checkPhoneNumber(samplePhoneNumber));

        samplePhoneNumber = "abcdefghij";
        assertTrue(User_Signup.checkPhoneNumber(samplePhoneNumber));

        samplePhoneNumber = "123456";
        assertTrue(User_Signup.checkPhoneNumber(samplePhoneNumber));
    }

    @Test
    public void testCapitalize(){
        String sampleName = "john smith";
        assertEquals("John Smith", TripSearchResult.capitalizeFirstLetter(sampleName));

        sampleName = "name";
        assertEquals("Name", TripSearchResult.capitalizeFirstLetter(sampleName));
    }
}