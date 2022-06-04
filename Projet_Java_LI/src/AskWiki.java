import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.stream.XMLStreamException;
import javax.xml.transform.TransformerException;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;


public class AskWiki {
	
	  public static void main(final String args[])
			  throws IOException, XMLStreamException, ParserConfigurationException, TransformerException {
		  
		final Options options = AnalyzeArg.generateOptions();
	    final CommandLine commandLine = AnalyzeArg.generateCommandLine(options, args);
	    	
	    	//Récupération des arguments de la ligne de commande
	    	if (commandLine != null)
	    		{
	    		final String file =
	    				commandLine.getOptionValue(AnalyzeArg.FILE_OPTION);
	 			final String f =
	 					commandLine.getOptionValue(AnalyzeArg.FOR_OPTION);
	 	      	final String w =
	 	      			commandLine.getOptionValue(AnalyzeArg.WHAT_OPTION);
	 	      	String i = "";
	 	      	if (commandLine.hasOption(AnalyzeArg.INTO_OPTION)) {
	 	     		i =
	 	     				commandLine.getOptionValue(AnalyzeArg.INTO_OPTION);
	 	      	}
	 	      	
	 	      	//Sérialisation et désérialisation des données
	 	      	Stock.serializeInfos(f, file);
	 	      	
	 	      	ObjectInputStream ois = null;

		    	try {
		    		final FileInputStream fichier = new FileInputStream("infos.ser");
		    		ois = new ObjectInputStream(fichier);
		    		final Stock stock = (Stock) ois.readObject();
		    		
		    		//Affichage de la prononciation
		    		if ((w.equals("pron"))
		    				| (w.equals("prononciation"))
		    				| (w.equals("pronunciation"))) {
		    			System.out.println(stock.pronunciation);
		    		} else {
		    			//Affichage de la/des catégorie(s) morphosyntaxique(s)
		    			if ((w.equals("morph"))
		    					| (w.equals("morphosyntax"))
		    					| (w.equals("morphosyntaxe"))) {
		    				System.out.println(stock.morphosyntax);
		    			}
		    			//Affichage des traductions/de la traduction désirée
		    			if ((w.equals("tr"))
		    					| (w.equals("translation"))
		    					| (w.equals("translations"))
		    					| (w.equals("traduction"))
		    					| (w.equals("traductions"))) {
		    				if (!i.equals("")) {
		    					if (stock.translations.containsKey(i)) {
		    						System.out.println(stock.translations.get(i));
		    					} else {
		    						System.out.println("No translation in this language.");
		    					}
		    				} else {
		    					System.out.println(stock.translations);
		    				}
		    			}
		    			//Affichage des synonymes
		    			if ((w.equals("syn"))
		    					| (w.equals("synonym"))
		    					| (w.equals("synonyms"))
		    					| (w.equals("synonyme"))
		    					| (w.equals("synonymes"))) {
		    				System.out.println(stock.synonyms);
		    			}
		    			//Affichage des antonymes
		    			if ((w.equals("ant"))
		    					| (w.equals("antonym"))
		    					| (w.equals("antonyms"))
		    					| (w.equals("antonyme"))
		    					| (w.equals("antonymes"))) {
		    				System.out.println(stock.antonyms);
		    			}
		    		}
			      
		    	} catch (final java.io.IOException e) {
		    		e.printStackTrace();
		    	} catch (final ClassNotFoundException e) {
		    		e.printStackTrace();
		    	} finally {
		    		try {
		    			if (ois != null) {
		    				ois.close();
		    			}
		    		} catch (final IOException ex) {
		    			ex.printStackTrace();
		    		}
		    	}
	 	   }
	    	
	  }
}
