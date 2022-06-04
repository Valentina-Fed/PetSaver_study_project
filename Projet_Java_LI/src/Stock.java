import java.io.Serializable;
import java.util.*;
import java.util.regex.*;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.stream.XMLStreamException;
import javax.xml.transform.TransformerException;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

import java.util.ArrayList;

@SuppressWarnings("serial")
public class Stock implements Serializable {
	
    String word;
    List<String> pronunciation;
    List<String> morphosyntax;
    HashMap<String, String> translations;
    List<String> synonyms;
    List<String> antonyms;

    // constructeur par defaut, si le mot n'as pas de paramètre.
    public Stock() {
        word = new String();
        pronunciation = Arrays.asList("Inconnu");
        morphosyntax = Arrays.asList("Inconnu");
        translations.put("Inconnu", "Inconnu");
        synonyms = Arrays.asList("Inconnu");
        antonyms = Arrays.asList("Inconnu");
    }

    public Stock(String lexeme, List<String> phon, List<String> morph, HashMap<String, String> transl, List<String> syn, List<String> ant) {
        word = lexeme;
        pronunciation = phon;
        morphosyntax = morph;
        translations = transl;
        synonyms = syn;
        antonyms = ant;
    }

    //ici j'ai décidé d'extraire que la première prononciation. Elles sont en général les mêmes.
    public static List<String> getPronunciation (ArrayList<ArrayList<String>> word) {
        List<String> prononciation = new ArrayList<>();
        Pattern p = Pattern.compile("pron");
        Pattern pattern = Pattern.compile("S\\|\\w+\\|fr");
        for (ArrayList<String> line : word) {
            Matcher m = pattern.matcher((Arrays.asList(line).get(0)).get(0).toString());
            if (m.find()) {
                for (Object unit : line) {
                    Matcher matcher = p.matcher(unit.toString());
                    if (matcher.find()) {
                        prononciation.add(Arrays.asList(unit.toString().split("\\|")).get(1));
                        break;
                    }
                }
                break;
            }
        }
        return prononciation;
    }

    public static List<String> getMorphosyntax(ArrayList<ArrayList<String>> word) {
        List<String> morphosyntax = new ArrayList<String>();
        Pattern pattern = Pattern.compile("S\\|\\w+\\|fr");
        for (ArrayList<String> line : word) {
            Matcher matcher = pattern.matcher((Arrays.asList(line).get(0)).get(0).toString());
            if (matcher.find()) {
                morphosyntax.add(Arrays.asList((line).get(0)).get(0).toString().replaceAll("S\\|", "").replaceAll("\\|fr", ""));
            }
        }
        return morphosyntax;
    }

    public static HashMap<String, String> getTranslations(ArrayList<ArrayList<String>> word) {
        HashMap<String, String> translations = new HashMap<>();
        Pattern pattern = Pattern.compile("S\\|traductions");
        
        for (ArrayList<String> line : word) {
            Matcher matcher = pattern.matcher((Arrays.asList(line).get(0)).get(0).toString());
            
            if (matcher.find()) {
                line.remove(0);
                
                //Nettoyage des traductions afin de garder les codes de langue et les traductions
                for (int i = 0; i < line.size(); i++) {
                	String el = line.get(i);
                	el = el.substring(2);
                	el = el.replaceAll("trad[\\+\\-]\\|..\\|", "");
                	el = el.replaceAll("\\|.", "");
                	
                	String key = el.split(" : ")[0];
                	String value = el.split(" : ")[1];
                	
                	if (translations.containsKey(key)) {
                		translations.put(key, translations.get(key) + ", " + value);
                	} else {
                		translations.put(key, value);
                	}
                }
            }
        }
        return translations;
    }

    public static List<String> getSynonyms(ArrayList<ArrayList<String>> word) {
        List<String> synonyms = new ArrayList<String>();
        Pattern pattern = Pattern.compile("S\\|synonymes");
        for (ArrayList<String> line : word) {
            Matcher matcher = pattern.matcher((Arrays.asList(line).get(0)).get(0).toString());
            if (matcher.find()) {
                line.remove(0);
                synonyms.add(line.toString());
            }
        }
        return synonyms;
    }
    public static List<String> getAntonyms(ArrayList<ArrayList<String>> word) {
        List<String> antonyms = new ArrayList<String>();
        Pattern pattern = Pattern.compile("S\\|antonymes");
        for (ArrayList<String> line : word) {
            Matcher matcher = pattern.matcher((Arrays.asList(line).get(0)).get(0).toString());
            if (matcher.find()) {
                line.remove(0);
                antonyms.add(line.toString());
            }
        }
        return antonyms;
    }
    
    public static void serializeInfos(String word, String filename)
    		throws IOException, XMLStreamException, ParserConfigurationException, TransformerException {
    	
    	ArrayList<ArrayList<String>> infos = AnalyzeDump.informations(word, filename);
    	List<String> pronunciation = getPronunciation(infos);
    	HashMap<String, String> translations = getTranslations(infos);
    	List<String> morphosyntax = getMorphosyntax(infos);
    	List<String> synonyms = getSynonyms(infos);
    	List<String> antonyms = getAntonyms(infos);
        final Stock stockInfos = new Stock(word, pronunciation, morphosyntax, translations, synonyms, antonyms);
        ObjectOutputStream oos = null;

        try {
          final FileOutputStream fileInfos = new FileOutputStream("infos.ser");
          oos = new ObjectOutputStream(fileInfos);
          oos.writeObject(stockInfos);
          oos.flush();
        } catch (final java.io.IOException e) {
          e.printStackTrace();
        } finally {
          try {
            if (oos != null) {
              oos.flush();
              oos.close();
            }
          } catch (final IOException ex) {
            ex.printStackTrace();
          }
        }
      }
}
