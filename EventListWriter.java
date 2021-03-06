package gb.esac.eventlist;

import gb.esac.io.AsciiDataFileWriter;
import java.io.IOException;
import org.apache.log4j.Logger;

/**
 * The class <code>EventListWriter</code> is used to simulate red noise. 
 *
 * @author <a href="mailto: guilaume.belanger@esa.int">Guillaume Belanger, ESA, ESAC</a>
 * @version April 2017 (last modified)
 */
final class EventListWriter {

    private static Logger logger  = Logger.getLogger(EventListWriter.class);
    
    static void writeTimesAsQDP(EventList evlist, String filename) throws IOException {
	double[] y = new double[evlist.nEvents()];
	for (int i = 0; i < evlist.nEvents(); i++) { 
	    y[i] = 0.15;
	}
	AsciiDataFileWriter out = new AsciiDataFileWriter(filename);
	String[] header = new String[] {
	    "DEV /XS",
	    "LAB T", "LAB F",
	    "TIME OFF",
	    "LINE OFF",
	    "MA 39 ON 1", "MA SIZE 5",
	    "LW 4", "CS 1.3",
	    "LAB X Time (s)",
	    "VIEW 0.1 0.2 0.9 0.8",
	    "SKIP SINGLE",
	    "!"
	};
	out.writeData(header, evlist.getArrivalTimes(), y);
	logger.info("Event list arrival times written to "+filename);
    }

    static void writeEnergiesVsTimeAsQDP(EventList evlist, String filename) throws IOException, EventListException {
	AsciiDataFileWriter out = new AsciiDataFileWriter(filename);
	String[] header = new String[] {
	    "DEV /XS",
	    "LAB T", "LAB F",
	    "TIME OFF",
	    "LINE OFF",
	    "MA 2 ON", "MA SIZE 3",
	    "LW 4", "CS 1.3",
	    "LAB X Time (s)",
	    "LAB Y Energy",
	    "VIEW 0.1 0.2 0.9 0.8",
	    "SKIP SINGLE",
	    "!"
	};
	out.writeData(header, evlist.getArrivalTimes(), evlist.getEnergies());
	logger.info("Event list energies vs time written to "+filename);
    }

    static void writeXYCoordsAsQDP(EventList evlist, String filename) throws IOException, EventListException {
	AsciiDataFileWriter out = new AsciiDataFileWriter(filename);
	String[] header = new String[] {
	    "DEV /XS",
	    "LAB T", "LAB F",
	    "TIME OFF",
	    "LINE OFF",
	    "MA 2 ON", "MA SIZE 0.5",
	    "LW 3", "CS 1.3",
	    "LAB X Time (s)",
	    "LAB Y Energy",
	    "VIEW 0.2 0.1 0.8 0.9",
	    "SKIP SINGLE",
	    "!"
	};
	out.writeData(header, evlist.getXCoords(), evlist.getYCoords());
	logger.info("Event detector coordinates written as Y vs X to "+filename);
    }

    

}
