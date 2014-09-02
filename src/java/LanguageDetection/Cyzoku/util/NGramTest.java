/**
 * 
 */
package LanguageDetection.Cyzoku.util;


/**
 * @author Nakatani Shuyo
 *
 */
public class NGramTest {

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
     * Test method for constants
     */
    public final void testConstants() {
    }

    /**
     * Test method for {@link NGram#normalize(char)} with Latin characters
     */
    public final void testNormalizeWithLatin() {
    }

    /**
     * Test method for {@link NGram#normalize(char)} with CJK Kanji characters
     */
    public final void testNormalizeWithCJKKanji() {
    }

    /**
     * Test method for {@link NGram#get(int)} and {@link NGram#addChar(char)}
     */
    public final void testNGram() {
        NGram ngram = new NGram();
        ngram.addChar(' ');
        ngram.addChar('A');
        ngram.addChar('\u06cc');
        ngram.addChar('\u1ea0');
        ngram.addChar('\u3044');

        ngram.addChar('\u30a4');
        ngram.addChar('\u3106');
        ngram.addChar('\uac01');
        ngram.addChar('\u2010');

        ngram.addChar('a');

    }
   
}