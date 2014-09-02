/**
 *
 */
package LanguageDetection.Cyzoku.util;

/**
 * @author Nakatani Shuyo
 *
 */
public class TagExtractorTest {

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
     * {@link com.cybozu.labs.langdetect.util.TagExtractor#TagExtractor(java.lang.String, int)}.
     */
    public final void testTagExtractor() {
        TagExtractor extractor = new TagExtractor(null, 0);

        TagExtractor extractor2 = new TagExtractor("abstract", 10);
    }

    /**
     * Test method for
     * {@link com.cybozu.labs.langdetect.util.TagExtractor#setTag(java.lang.String)}.
     */
    public final void testSetTag() {
        TagExtractor extractor = new TagExtractor(null, 0);
        extractor.setTag("");
        extractor.setTag(null);
    }

    /**
     * Test method for
     * {@link com.cybozu.labs.langdetect.util.TagExtractor#add(java.lang.String)}.
     */
    public final void testAdd() {
        TagExtractor extractor = new TagExtractor(null, 0);
        extractor.add("");
        extractor.add(null);    // ignore
    }

    /**
     * Test method for
     * {@link com.cybozu.labs.langdetect.util.TagExtractor#closeTag(com.cybozu.labs.langdetect.util.LangProfile)}.
     */
    public final void testCloseTag() {
        TagExtractor extractor = new TagExtractor(null, 0);
        LangProfile profile = null;
        extractor.closeTag(profile);    // ignore
    }

    /**
     * Scenario Test of extracting &lt;abstract&gt; tag from Wikipedia database.
     */
    public final void testNormalScenario() {
        TagExtractor extractor = new TagExtractor("abstract", 10);

        LangProfile profile = new LangProfile("en");

        // normal
        extractor.setTag("abstract");
        extractor.add("This is a sample text.");
        extractor.closeTag(profile);

        // too short
        extractor.setTag("abstract");
        extractor.add("sample");
        extractor.closeTag(profile);

        // other tags
        extractor.setTag("div");
        extractor.add("This is a sample text which is enough long.");
        extractor.closeTag(profile);
    }

    /**
     * Test method for
     * {@link com.cybozu.labs.langdetect.util.TagExtractor#clear()}.
     */
    public final void testClear() {
        TagExtractor extractor = new TagExtractor("abstract", 10);
        extractor.setTag("abstract");
        extractor.add("This is a sample text.");
        extractor.clear();
    }
}
