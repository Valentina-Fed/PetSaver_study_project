import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.DefaultParser;

import java.util.Arrays;


public class AnalyzeArg {
	
	//Initialisation des attributs correspondant aux options
	final static String FOR_OPTION = "for";
	final static String WHAT_OPTION = "what";
	final static String INTO_OPTION = "into";
	final static String FILE_OPTION = "file";
    
	
	//Création des options
    static Options generateOptions()
    {
    	
    	//Option prenant en argument le nom du dump
    	final Option fileOption = Option.builder("file")
    			.required()
    			.hasArg()
    			.longOpt("FILE_OPTION")
    			.desc("Which dump.")
    			.build();
    	
    	//Option prenant en argument le mot dont on veut des informations
    	final Option forOption = Option.builder("for")
    		   .required()
               .hasArg()
               .longOpt("FOR_OPTION")
               .desc("Which word.")
               .build();
       
    	//Option prenant en argument quelle information on veut à propos de ce mot
    	final Option whatOption = Option.builder("what")
    		   .required()
    		   .hasArg()
    		   .longOpt("WHAT_OPTION")
    		   .desc("What about the word.")
    		   .build();
       
    	//Option pas nécessaire prenant en argument en quelle langue on veut traduire le mot
    	//(Si l'argument pris par what demande la traduction)
    	//(Si pas de into dans ce cas, affiche toutes les traductions)
    	final Option intoOption = Option.builder("into")
    		   .required(false)
    		   .longOpt("INTO_OPTION")
    		   .hasArg()
    		   .desc("Translates into which language.")
    		   .build();
       
    	final Options options = new Options();
    	options.addOption(fileOption);
    	options.addOption(forOption);
    	options.addOption(whatOption);
    	options.addOption(intoOption);
    	return options;
    }
    
    //Création de la ligne de commande à analyser
    static CommandLine generateCommandLine(
    		   final Options options, final String[] commandLineArguments)
    {
    	final CommandLineParser cmdLineParser = new DefaultParser();
    	CommandLine commandLine = null;
    	try
    	{
    		commandLine = cmdLineParser.parse(options, commandLineArguments);
    	}
    	catch (ParseException parseException)
    	{
    		System.out.println(
    				"ERROR: Unable to parse command-line arguments "
    				+ Arrays.toString(commandLineArguments) + " due to: "
    		        + parseException);
    	}
    	return commandLine;
    }	
    
}
