package LanguageDetection.Cyzoku.util;

import java.util.List;

/**
 * Unit test for {@link Detector} and {@link DetectorFactory}.
 * @author Nakatani Shuyo
 *
 */
public class DetectorTest {

    private static final String TRAINING_EN = "a a a b b c c d e";
    private static final String TRAINING_FR = "a b b c c c d d d";
    private static final String TRAINING_JA = "\u3042 \u3042 \u3042 \u3044 \u3046 \u3048 \u3048";

    public void setUp() throws Exception {
        DetectorFactory.clear();
        
        LangProfile profile_en = new LangProfile("en");
        for (String w : TRAINING_EN.split(" "))
            profile_en.add(w);
        DetectorFactory.addProfile(profile_en, 0, 3);

        LangProfile profile_fr = new LangProfile("fr");
        for (String w : TRAINING_FR.split(" "))
            profile_fr.add(w);
        DetectorFactory.addProfile(profile_fr, 1, 3);

        LangProfile profile_ja = new LangProfile("ja");
        for (String w : TRAINING_JA.split(" "))
            profile_ja.add(w);
        DetectorFactory.addProfile(profile_ja, 2, 3);
    }

    public void tearDown() throws Exception {
    }

    public final void testDetector1() throws LangDetectException {
    }

    public final void testDetector2() throws LangDetectException {
    }

    public final void testDetector3() throws LangDetectException {
    }

    public final void testDetector4() throws LangDetectException {
    }
    
    public final void testLangList() throws LangDetectException {
        List<String> langList = DetectorFactory.getLangList();
    }

    public final void testLangListException() throws LangDetectException {
        List<String> langList = DetectorFactory.getLangList();
        langList.add("hoge");
        //langList.add(1, "hoge");
    }
}