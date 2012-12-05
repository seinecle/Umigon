/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Classifier;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author C. Levallois
 */
public class HeuristicsLoader {

    BufferedReader br;
    FileReader fr;
    String folderPath = "heuristics//";
    String string;
    Heuristic heuristic;
    Map<String, Heuristic> mapHeuristics;
    Map<String, Heuristic> mapH1;
    Map<String, Heuristic> mapH2;
    Map<String, Heuristic> mapH3;
    Map<String, Heuristic> mapH4;
    Map<String, Heuristic> mapH5;
    Map<String, Heuristic> mapH6;
    Map<String, Heuristic> mapH7;
    Map<String, Heuristic> mapH8;
    Map<String, Heuristic> mapH9;
    Set<String> setNegations;

    public void load() throws FileNotFoundException, IOException {

        File folder = new File("D:\\Docs Pro Clement\\NetBeansProjects\\TwitterCollection\\heuristics\\");
        System.out.println("folder is: " + folder.getCanonicalPath());
        File[] arrayFiles = folder.listFiles();
        mapHeuristics = new HashMap();
        setNegations = new HashSet();

        mapH1 = new HashMap();
        mapH2 = new HashMap();
        mapH4 = new HashMap();
        mapH3 = new HashMap();
        mapH5 = new HashMap();
        mapH6 = new HashMap();
        mapH7 = new HashMap();
        mapH8 = new HashMap();
        mapH9 = new HashMap();

        for (File file : arrayFiles) {
            br = new BufferedReader(new FileReader(file));
            br.readLine();
            int map = Integer.parseInt(StringUtils.left(file.getName(), file.getName().indexOf("_")));


            while ((string = br.readLine()) != null) {
//                System.out.println("string: "+string);
                String term = string.split("\t", -1)[0].trim();
                String feature = string.split("\t", -1)[1].trim();
//                if (term.equals("I was wondering")){
//                    System.out.println("HERE!!!!");
//                }
//                System.out.println("feature: "+feature);
                heuristic = new Heuristic(term, feature, map);
                mapHeuristics.put(term, heuristic);
                if (map == 0) {
                    setNegations.add(term);
                    continue;
                }
                if (map == 1) {
                    mapH1.put(term, heuristic);
                    continue;
                }
                if (map == 2) {
                    mapH2.put(term, heuristic);
                    continue;
                }
                if (map == 3) {
                    mapH3.put(term, heuristic);
                    continue;
                }
                if (map == 4) {
                    mapH4.put(term, heuristic);
                    continue;
                }
                if (map == 5) {
                    mapH5.put(term, heuristic);
                    continue;
                }
                if (map == 6) {
                    mapH6.put(term, heuristic);
                    continue;
                }
                if (map == 7) {
                    mapH7.put(term, heuristic);
                    continue;
                }
                if (map == 8) {
                    mapH8.put(term, heuristic);
                    continue;
                }
                if (map == 9) {
                    mapH9.put(term, heuristic);
                    continue;
                }


            }
        }
        System.out.println("total number heuristics: " + mapHeuristics.keySet().size());
        System.out.println("--------------------------------------------");

        System.out.println("humor_or_light: " + mapH4.keySet().size());
        System.out.println("time: " + mapH8.keySet().size());
        System.out.println("question: " + mapH6.keySet().size());
        System.out.println("positive tone: " + mapH1.keySet().size());
        System.out.println("negative tone: " + mapH2.keySet().size());
        System.out.println("direct address: " + mapH5.keySet().size());
        System.out.println("self turned: " + mapH7.keySet().size());
        System.out.println("strength of opinion: " + mapH3.keySet().size());

    }

    public Map<String, Heuristic> getMapHeuristics() {
        return mapHeuristics;
    }

    public void setMapHeuristics(Map<String, Heuristic> mapHeuristics) {
        this.mapHeuristics = mapHeuristics;
    }

    public Map<String, Heuristic> getMapH1() {
        return mapH1;
    }

    public void setMapH1(Map<String, Heuristic> mapH1) {
        this.mapH1 = mapH1;
    }

    public Map<String, Heuristic> getMapH2() {
        return mapH2;
    }

    public void setMapH2(Map<String, Heuristic> mapH2) {
        this.mapH2 = mapH2;
    }

    public Map<String, Heuristic> getMapH3() {
        return mapH3;
    }

    public void setMapH3(Map<String, Heuristic> mapH3) {
        this.mapH3 = mapH3;
    }

    public Map<String, Heuristic> getMapH4() {
        return mapH4;
    }

    public void setMapH4(Map<String, Heuristic> mapH4) {
        this.mapH4 = mapH4;
    }

    public Map<String, Heuristic> getMapH5() {
        return mapH5;
    }

    public void setMapH5(Map<String, Heuristic> mapH5) {
        this.mapH5 = mapH5;
    }

    public Map<String, Heuristic> getMapH6() {
        return mapH6;
    }

    public void setMapH6(Map<String, Heuristic> mapH6) {
        this.mapH6 = mapH6;
    }

    public Map<String, Heuristic> getMapH7() {
        return mapH7;
    }

    public void setMapH7(Map<String, Heuristic> mapH7) {
        this.mapH7 = mapH7;
    }

    public Map<String, Heuristic> getMapH8() {
        return mapH8;
    }

    public void setMapH8(Map<String, Heuristic> mapH8) {
        this.mapH8 = mapH8;
    }

    public Map<String, Heuristic> getMapH9() {
        return mapH9;
    }

    public void setMapH9(Map<String, Heuristic> mapH9) {
        this.mapH9 = mapH9;
    }
    
    

    public Set<String> getSetNegations() {
        return setNegations;
    }

    public void setSetNegations(Set<String> setNegations) {
        this.setNegations = setNegations;
    }
}
