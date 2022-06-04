import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.stream.XMLStreamException;
import javax.xml.transform.TransformerException;

import wiktionary_extractor.Page;
import wiktionary_extractor.PageExtractor;

import java.util.ArrayList;


public class AnalyzeDump {
			
	//On fournit deux variables, le mot dont on veut analyser la page
	//et le fichier dont on doit extraire les données
	public static ArrayList<ArrayList<String>> informations(String word, String filename)
			throws IOException, XMLStreamException, ParserConfigurationException, TransformerException {
		
		ArrayList<ArrayList<String>> infos = new ArrayList<>();
		PageExtractor pages = new PageExtractor(filename);
		String content = "";
		
		//On cherche la page correspondant au mot et on garde son texte
		for (Page p : pages) {
			if (p.title.equals(word)) {
				content = p.content;
			}
		}
		
		//Dans le cas où il n'y a pas de page pour le mot
		if (content.length() == 0) {
			return infos;
		}
		
		//Mise des informations françaises dans une String
		String fr = "";
		
		for (String s : content.split("\n== ")) {
			if (s.contains("{{langue|fr}}")) {
				fr = s;
			}
		}
		
		//Suppression de la partie signalant qu'il s'agit des informations françaises
		fr = fr.substring(fr.indexOf("\n"), fr.length());
		
		//Split à chaque début de catégorie (signalée par plusieurs "=")
		for (String s : fr.split("\n=")) {
			
			ArrayList<String> category = new ArrayList<>();
			for (String subS : s.split("\n")) {
				
				/* Les lignes commençant par ces caractères ne semblent pas
				 * contenir d'informations qui nous soient utiles
				 * cependant | est toujours présent au sein des lignes utiles
				 * et par ailleurs les lignes comprenant "écouter|"
				 * dans la catégorie prononciation ne contiennent pas
				 * de prononciation, on peut donc les retirer
				 */
				
				if ((!subS.startsWith("#"))
						& (!subS.startsWith("|"))
						& (subS.contains("|"))
						& (!subS.contains("écouter|"))) {
					
					/* On se débarrasse des caractères superflus servant
					 * à la mise en page ou autre
					 * (Je n'arrive pas à faire fonctionner replaceAll
					 * ni replace alors je fais autrement)
					 * (Update : j'ai enfin compris comment cela fonctionne
					 * mais étant donné que ce bout de code fonctionne
					 * je préfère ne pas changer)
					 */
					String subSbis = "";
					for (int i = 0; i < subS.length(); i++) {
						if ((subS.charAt(i) != '=')
								& (subS.charAt(i) != '{')
								& (subS.charAt(i) != '}')
								& (subS.charAt(i) != '*')) {
							subSbis = subSbis + subS.charAt(i);
						}
					}
					subSbis = subSbis.trim();
					/* À ce stade, les lignes qui commencent par "trad-"
					 * ne contiennent pas de traduction, mais signalent
					 * simplement que ce qui suit est/sont une/des
				 	 * traductions
				 	 */
					if (!subSbis.startsWith("trad-")) {
						category.add(subSbis);
					}
				}
			}
			while (category.contains("")) {
				category.remove("");
			}
			
			boolean useful = true;
			
			//Mise à faux quand ne contient pas d'information utile (ou rien)
			if ((category.isEmpty()) | (category.size() == 1)) {
				useful = false;
			} else {
				if (category.get(0).contains("étymologie")
					| category.get(0).contains("notes")
					| category.get(0).contains("variantes")
					| category.get(0).contains("dérivés")
					| category.get(0).contains("phrases")
					| category.get(0).contains("abréviations")
					| category.get(0).contains("holonymes")
					| category.get(0).contains("vocabulaire")
					| category.get(0).contains("anagrammes")
					| category.get(0).contains("apparentés")
					| category.get(0).contains("références")
					| category.get(0).contains("voir aussi")) {
				useful = false;
				}
			}
			
			if (useful) {
				infos.add(category);
			}
		}
				
		return infos;
	}

    public static void main(String[] args)
    		throws IOException, XMLStreamException, ParserConfigurationException, TransformerException {
    	
	String filename = "small.xml";
	String word = "empirique";
	
	System.out.println(informations(word, filename));
    }
}
