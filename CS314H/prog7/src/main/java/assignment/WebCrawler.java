package assignment;

import java.io.*;
import java.net.*;
import java.util.*;

import org.attoparser.ParseException;
import org.attoparser.simple.*;
import org.attoparser.config.ParseConfiguration;

/**
 * The entry-point for WebCrawler; takes in a list of URLs to start crawling from and saves an index
 * to index.db.
 */
public class WebCrawler {

    /**
    * The WebCrawler's main method starts crawling a set of pages.  You can change this method as
    * you see fit, as long as it takes URLs as inputs and saves an Index at "index.db".
    */
    public static void main(String[] args) throws FileNotFoundException {

        PrintStream err = new PrintStream("logs.txt");

        // Basic usage information
        if (args.length == 0) {
            err.println("Error: No URLs specified.");
            return;
        }

        // We'll throw all the args into a queue for processing.
        Queue<URL> remaining = new LinkedList<>();

        for (String url : args) {
            try {
                remaining.add(new URL(url));
            } catch (MalformedURLException e) {
                // Throw this one out!
                err.printf("Error: URL '%s' was malformed and will be ignored!%n", url);
            }
        }

        // Create a parser from the attoparser library, and our handler for markup.
        ISimpleMarkupParser parser = new SimpleMarkupParser(ParseConfiguration.htmlConfiguration());
        CrawlingMarkupHandler handler = new CrawlingMarkupHandler();

        handler.setErrorStream(err);


        // Try to start crawling, adding new URLS as we see them.
        while (true) {
            URL currentUrl = null;
            try {
                while (!remaining.isEmpty()) {

                    // Parse the next URL's page
                    currentUrl = remaining.poll();

                    // Set the currentURL for the handler
                    handler.setCurrentURL(currentUrl);

                    parser.parse(new InputStreamReader(currentUrl.openStream()), handler);

                    // Add any new URLs
                    remaining.addAll(handler.newURLs());
                }

                handler.getIndex().save("index.db");
                break;
            } catch (FileNotFoundException e) {
                err.println("File at " + currentUrl + " was not found.");
                e.printStackTrace(err);
            } catch (ParseException e) {
               err.println("Could not parse file element at " + handler.getCurrentURL());
                e.printStackTrace(err);
            } catch (IOException e) {
                err.println("Input/Output Failure: Check site streams");
                e.printStackTrace(err);
            }
        }
    }
}
