/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package LanguageDetection;

import com.cybozu.labs.langdetect.Detector;
import com.cybozu.labs.langdetect.DetectorFactory;
import com.cybozu.labs.langdetect.LangDetectException;

/**
 *
 * @author C. Levallois
 */
public class LanguageDetector {

    Detector detector;
    String lang;

    public LanguageDetector() throws LangDetectException {
        DetectorFactory.loadProfile("D:\\Docs Pro Clement\\NetBeansProjects\\Umigon\\profiles");

    }

    public boolean detectEnglish(String status) throws LangDetectException {
        detector = DetectorFactory.create();
        detector.append(status);
        try {
            lang = detector.detect();
            if (!lang.equals("en")) {
                return false;
            } else {
                return true;
            }
        } catch (LangDetectException e) {
//            System.out.println("tweet without language detected: " + status);
            return false;
        }

    }
}
