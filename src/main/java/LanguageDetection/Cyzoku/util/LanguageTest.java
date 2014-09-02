/**
 * 
 */
package LanguageDetection.Cyzoku.util;



/**
 * @author Nakatani Shuyo
 *
 */
public class LanguageTest {

    /**
     * @throws java.lang.Exception
     */
    public void setUp() throws Exception {
    }

    /**
     * @throws java.lang.Exception
     */
    public void tearDown() throws Exception {
    }

    /**
     * Test method for {@link com.cybozu.labs.langdetect.Language#Language(java.lang.String, double)}.
     */
    public final void testLanguage() {
        Language lang = new Language(null, 0);
        
        Language lang2 = new Language("en", 1.0);
        
    }

}
