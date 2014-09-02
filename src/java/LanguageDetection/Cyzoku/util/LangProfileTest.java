/**
 *
 */
package LanguageDetection.Cyzoku.util;

/**
 * @author Nakatani Shuyo
 *
 */
public class LangProfileTest {

    /**
     * @throws java.lang.Exception
     */
    public static void setUpBeforeClass() throws Exception {
    }

    /**
     * @throws java.lang.Exception
     */
    public static void tearDownAfterClass() throws Exception {
    }

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
     * Test method for
     * {@link com.cybozu.labs.langdetect.util.LangProfile#LangProfile()}.
     */
    public final void testLangProfile() {
        LangProfile profile = new LangProfile();
    }

    /**
     * Test method for
     * {@link com.cybozu.labs.langdetect.util.LangProfile#LangProfile(java.lang.String)}.
     */
    public final void testLangProfileStringInt() {
        LangProfile profile = new LangProfile("en");
    }

    /**
     * Test method for
     * {@link com.cybozu.labs.langdetect.util.LangProfile#add(java.lang.String)}.
     */
    public final void testAdd() {
        LangProfile profile = new LangProfile("en");
        profile.add("a");
        profile.add("a");
        profile.omitLessFreq();
    }

    /**
     * Illegal call test for {@link LangProfile#add(String)}
     */
    public final void testAddIllegally1() {
        LangProfile profile = new LangProfile(); // Illegal ( available for only JSONIC ) but ignore  
        profile.add("a"); // ignore
    }

    /**
     * Illegal call test for {@link LangProfile#add(String)}
     */
    public final void testAddIllegally2() {
        LangProfile profile = new LangProfile("en");
        profile.add("a");
        profile.add("");  // Illegal (string's length of parameter must be between 1 and 3) but ignore
        profile.add("abcd");  // as well
    }

    /**
     * Test method for
     * {@link com.cybozu.labs.langdetect.util.LangProfile#omitLessFreq()}.
     */
    public final void testOmitLessFreq() {
        LangProfile profile = new LangProfile("en");
        String[] grams = "a b c \u3042 \u3044 \u3046 \u3048 \u304a \u304b \u304c \u304d \u304e \u304f".split(" ");
        for (int i = 0; i < 5; ++i) {
            for (String g : grams) {
                profile.add(g);
            }
        }
        profile.add("\u3050");

        profile.omitLessFreq();
    }

    /**
     * Illegal call test for
     * {@link com.cybozu.labs.langdetect.util.LangProfile#omitLessFreq()}.
     */
    public final void testOmitLessFreqIllegally() {
        LangProfile profile = new LangProfile();
        profile.omitLessFreq();  // ignore
    }
}
