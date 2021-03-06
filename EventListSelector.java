package gb.esac.eventlist;

import cern.colt.list.DoubleArrayList;
import cern.colt.list.IntArrayList;
import gb.esac.binner.BinningException;
import gb.esac.timeseries.TimeSeries;
import gb.esac.timeseries.TimeSeriesException;
import gb.esac.timeseries.TimeSeriesMaker;
import gb.esac.tools.Converter;
import gb.esac.tools.DistributionFunc;
import hep.aida.ref.histogram.Histogram1D;
import java.util.Arrays;
import org.apache.log4j.Logger;


public final class EventListSelector {

    private static Logger logger  = Logger.getLogger(EventListSelector.class);

    public static EventList selectEventsOnFlag(EventList evlist, int min, int max) throws EventListException {

	logger.info("Selecting events on flag values between "+min+" and "+max+" (inclusively)");
	logger.info("There are "+evlist.nEvents()+" events");

	if ( evlist.flagsAreSet() ) {

	    int[] flags = evlist.getFlags();
	    IntArrayList selectedFlags = new IntArrayList();

	    double[] times = evlist.getArrivalTimes();
	    double[] energies = evlist.getEnergies();
	    int[] xCoords = evlist.getXCoords();
	    int[] yCoords = evlist.getYCoords();
	    DoubleArrayList selectedTimes = new DoubleArrayList();
	    DoubleArrayList selectedEnergies = new DoubleArrayList();
	    IntArrayList selectedXCoords = new IntArrayList();
	    IntArrayList selectedYCoords = new IntArrayList();

	    if ( evlist.patternsAreSet() ) {

		int[] patterns = evlist.getPatterns();
		IntArrayList selectedPatterns = new IntArrayList();
		for ( int i=0; i < evlist.nEvents(); i++ ) {
		    if ( flags[i] >= min && flags[i] <= max ) {
			selectedTimes.add(times[i]);
			selectedEnergies.add(energies[i]);
			selectedXCoords.add(xCoords[i]);
			selectedYCoords.add(yCoords[i]);
			selectedFlags.add(flags[i]);
			selectedPatterns.add(patterns[i]);
		    }
		}
		selectedTimes.trimToSize();
		selectedEnergies.trimToSize();
		selectedXCoords.trimToSize();
		selectedYCoords.trimToSize();
		selectedFlags.trimToSize();
		selectedPatterns.trimToSize();
		logger.info(selectedTimes.size()+" events were selected. Fraction selected is "+((double) selectedTimes.size()/evlist.nEvents()));
		return new EventList(selectedTimes.elements(), selectedEnergies.elements(), selectedXCoords.elements(), selectedYCoords.elements(), selectedFlags.elements(), selectedPatterns.elements());
	    }
	    else {
		for ( int i=0; i < evlist.nEvents(); i++ ) {
		    if ( flags[i] >= min && flags[i] <= max ) {
			selectedTimes.add(times[i]);
			selectedEnergies.add(energies[i]);
			selectedXCoords.add(xCoords[i]);
			selectedYCoords.add(yCoords[i]);
			selectedFlags.add(flags[i]);
		    }
		}
		selectedTimes.trimToSize();
		selectedEnergies.trimToSize();
		selectedXCoords.trimToSize();
		selectedYCoords.trimToSize();
		selectedFlags.trimToSize();
		logger.info(selectedTimes.size()+" events were selected. Fraction selected is "+((double) selectedTimes.size()/evlist.nEvents()));
		return new EventList(selectedTimes.elements(), selectedEnergies.elements(), selectedXCoords.elements(), selectedYCoords.elements(), selectedFlags.elements());	    
	    }
	}
	else {
	    throw new EventListException("Flags are not defined: Cannot select on flag.");	
	}
    }


    public static EventList selectEventsOnEnergy(EventList evlist, int min, int max) throws EventListException {
	logger.info("Selecting events on energy values between "+min+" and "+max+" (inclusively)");
	logger.info("There are "+evlist.nEvents()+" events");
	if ( evlist.energiesAreSet() ) {
	    double[] times = evlist.getArrivalTimes();
	    double[] energies = evlist.getEnergies();
	    DoubleArrayList selectedTimes = new DoubleArrayList();
	    DoubleArrayList selectedEnergies = new DoubleArrayList();
	    if ( evlist.patternsAreSet() ) { // This means everything that can be is defined
		int[] xCoords = evlist.getXCoords();
		int[] yCoords = evlist.getYCoords();
		int[] flags = evlist.getFlags();
		int[] patterns = evlist.getPatterns();
		IntArrayList selectedXCoords = new IntArrayList();
		IntArrayList selectedYCoords = new IntArrayList();
		IntArrayList selectedFlags = new IntArrayList();
		IntArrayList selectedPatterns = new IntArrayList();
		for ( int i=0; i < evlist.nEvents(); i++ ) {
		    if ( energies[i] >= min && energies[i] <= max ) {
			selectedTimes.add(times[i]);
			selectedEnergies.add(energies[i]);
			selectedXCoords.add(xCoords[i]);
			selectedYCoords.add(yCoords[i]);
			selectedFlags.add(flags[i]);
			selectedPatterns.add(patterns[i]);
		    }
		}
		selectedTimes.trimToSize();
		selectedEnergies.trimToSize();
		selectedXCoords.trimToSize();
		selectedYCoords.trimToSize();
		selectedFlags.trimToSize();
		selectedPatterns.trimToSize();
		logger.info(selectedTimes.size()+" events were selected. Fraction selected is "+((double) selectedTimes.size()/evlist.nEvents()));
		return new EventList(selectedTimes.elements(), selectedEnergies.elements(), selectedXCoords.elements(), selectedYCoords.elements(), selectedFlags.elements(), selectedPatterns.elements());
	    }
	    else { 
		if ( evlist.flagsAreSet() ) {  // This means that everything is defined except the pattern
		    int[] xCoords = evlist.getXCoords();
		    int[] yCoords = evlist.getYCoords();
		    int[] flags = evlist.getFlags();
		    IntArrayList selectedXCoords = new IntArrayList();
		    IntArrayList selectedYCoords = new IntArrayList();
		    IntArrayList selectedFlags = new IntArrayList();
		    for ( int i=0; i < evlist.nEvents(); i++ ) {
			if ( energies[i] >= min && energies[i] <= max ) {
			    selectedTimes.add(times[i]);
			    selectedEnergies.add(energies[i]);
			    selectedXCoords.add(xCoords[i]);
			    selectedYCoords.add(yCoords[i]);
			    selectedFlags.add(flags[i]);
			}
		    }
		    selectedTimes.trimToSize();
		    selectedEnergies.trimToSize();
		    selectedXCoords.trimToSize();
		    selectedYCoords.trimToSize();
		    selectedFlags.trimToSize();
		    logger.info(selectedTimes.size()+" events were selected. Fraction selected is "+((double) selectedTimes.size()/evlist.nEvents()));
		    return new EventList(selectedTimes.elements(), selectedEnergies.elements(), selectedXCoords.elements(), selectedYCoords.elements(), selectedFlags.elements());
		}
		else { //  This means that the coords could be set but not necessarily
		    if ( evlist.coordsAreSet() ) {
			int[] xCoords = evlist.getXCoords();
			int[] yCoords = evlist.getYCoords();
			IntArrayList selectedXCoords = new IntArrayList();
			IntArrayList selectedYCoords = new IntArrayList();
			for ( int i=0; i < evlist.nEvents(); i++ ) {
			    if ( energies[i] >= min && energies[i] <= max ) {
				selectedTimes.add(times[i]);
				selectedEnergies.add(energies[i]);
				selectedXCoords.add(xCoords[i]);
				selectedYCoords.add(yCoords[i]);
			    }
			}
			selectedTimes.trimToSize();
			selectedEnergies.trimToSize();
			selectedXCoords.trimToSize();
			selectedYCoords.trimToSize();
			logger.info(selectedTimes.size()+" events were selected. Fraction selected is "+((double) selectedTimes.size()/evlist.nEvents()));
			return new EventList(selectedTimes.elements(), selectedEnergies.elements(), selectedXCoords.elements(), selectedYCoords.elements());
		    }
		    else {  //  This means that only the times and energies are defined
			for ( int i=0; i < evlist.nEvents(); i++ ) {
			    if ( energies[i] >= min && energies[i] <= max ) {
				selectedTimes.add(times[i]);
				selectedEnergies.add(energies[i]);
			    }
			}
			selectedTimes.trimToSize();
			selectedEnergies.trimToSize();
			logger.info(selectedTimes.size()+" events were selected. Fraction selected is "+((double) selectedTimes.size()/evlist.nEvents()));
			return new EventList(selectedTimes.elements(), selectedEnergies.elements());
		    }

		}
	    }
	}
	else {
	    throw new EventListException("Energies are not defined: Cannot select on energy.");
	}
    }


    public static EventList selectEventsOnPattern(EventList evlist, int min, int max) throws EventListException {
	logger.info("Selecting events on pattern values between "+min+" and "+max+" (inclusively)");
	logger.info("There are "+evlist.nEvents()+" events");
	if ( evlist.patternsAreSet() ) {
	    int[] patterns = evlist.getPatterns();
	    IntArrayList selectedPatterns = new IntArrayList();
	    double[] times = evlist.getArrivalTimes();
	    double[] energies = evlist.getEnergies();
	    int[] xCoords = evlist.getXCoords();
	    int[] yCoords = evlist.getYCoords();
	    int[] flags = evlist.getFlags();
	    DoubleArrayList selectedTimes = new DoubleArrayList();
	    DoubleArrayList selectedEnergies = new DoubleArrayList();
	    IntArrayList selectedXCoords = new IntArrayList();
	    IntArrayList selectedYCoords = new IntArrayList();
	    IntArrayList selectedFlags = new IntArrayList();
	    for ( int i=0; i < evlist.nEvents(); i++ ) {
		if ( patterns[i] >= min && patterns[i] <= max ) {
		    selectedTimes.add(times[i]);
		    selectedEnergies.add(energies[i]);
		    selectedXCoords.add(xCoords[i]);
		    selectedYCoords.add(yCoords[i]);
		    selectedFlags.add(flags[i]);
		    selectedPatterns.add(patterns[i]);
		}
	    }
	    selectedTimes.trimToSize();
	    selectedEnergies.trimToSize();
	    selectedXCoords.trimToSize();
	    selectedYCoords.trimToSize();
	    selectedFlags.trimToSize();
	    selectedPatterns.trimToSize();
	    logger.info(selectedTimes.size()+" events were selected. Fraction selected is "+((double) selectedTimes.size()/evlist.nEvents()));
	    return new EventList(selectedTimes.elements(), selectedEnergies.elements(), selectedXCoords.elements(), selectedYCoords.elements(), selectedFlags.elements(), selectedPatterns.elements());
	}
	else {
	    throw new EventListException("Patterns are not defined: Cannot select on pattern.");
	}
    }


    public static double[] getArrivalTimesFromTo(EventList evlist, double t1, double t2) {
	DoubleArrayList selected = new DoubleArrayList();
	double[] arrivalTimes = evlist.getArrivalTimes();
	int i=0;
	if ( arrivalTimes[i] < t1 ) {
	    while ( i < evlist.nEvents() && arrivalTimes[i] < t1 ) {
		i++;
	    }
	}
	selected.add(arrivalTimes[i]);
	i++;
	while ( i < evlist.nEvents() && arrivalTimes[i] < t2 ) {
	    selected.add(arrivalTimes[i]);
	    i++;
	}
	selected.trimToSize();
	return selected.elements();
    }
    
    public static double[] getArrivalTimesRandomSegment(EventList evlist, double segmentLength) throws EventListException {
	double durationOfEventList = evlist.duration();
	if ( durationOfEventList < segmentLength ) {
	    throw new EventListException("The requested segment is longer than the entire event list");
	}
	int nWholeSegments = (int) Math.floor(durationOfEventList/segmentLength);
	double from = Math.random() * (nWholeSegments-1)*segmentLength;
	double to = from + segmentLength;
	return getArrivalTimesFromTo(evlist, from, to);
    }

    public static  double[] getRandomArrivalTimes(EventList evlist, int nEvents) throws EventListException, BinningException, TimeSeriesException  {
	double mean = evlist.nEvents()/evlist.duration();
	double binTime = 1/(2*mean);
	TimeSeries ts = TimeSeriesMaker.makeTimeSeries(evlist.getArrivalTimes(), binTime);
	double tzero = ts.tStart();
	Histogram1D lcHisto = Converter.array2histo("light curve", tzero, binTime, ts.getRates());
	Histogram1D cdfHisto = DistributionFunc.getCDFHisto(lcHisto);
	double[] times = DistributionFunc.getRandom(cdfHisto, nEvents);
	Arrays.sort(times);
	return times;
    }


    public static double[] getArrivalTimesInEnergyRange(EventList evlist, double min, double max) throws EventListException {
	if ( evlist.energiesAreSet() ) {
	    double[] arrivalTimes = evlist.getArrivalTimes();
	    double[] energies = evlist.getEnergies();
	    DoubleArrayList selected = new DoubleArrayList();
	    for ( int i=0; i < evlist.nEvents(); i++ ) {

		if ( energies[i] >= min && energies[i] <= max ) {
		    selected.add(arrivalTimes[i]);
		}
	    }
	    selected.trimToSize();
	    return selected.elements();
	}
	else {
	    throw new EventListException("Energies are not defined");
	}
    }

    public static double[] getArrivalTimesInCoordinateRange(EventList evlist, double xmin, double xmax, double ymin, double ymax) throws EventListException {
	if ( evlist.coordsAreSet() ) {
	    double[] arrivalTimes = evlist.getArrivalTimes();
	    int[] xCoords = evlist.getXCoords();
	    int[] yCoords = evlist.getYCoords();
	    DoubleArrayList selected = new DoubleArrayList();
	    for ( int i=0; i < evlist.nEvents(); i++ ) {

		int x = xCoords[i];
		int y = yCoords[i];
		if ( x >= xmin && x <= xmax && y >= ymin && y <= ymax ) {
		    selected.add(arrivalTimes[i]);
		}
	    }
	    selected.trimToSize();
	    return selected.elements();
	}
	else {
	    throw new EventListException("Coordinates are not defined");
	}
    }

    public static double[] getArrivalTimesInFlagRange(EventList evlist, int min, int max) throws EventListException {
	if ( evlist.flagsAreSet() ) {
	    double[] arrivalTimes = evlist.getArrivalTimes();
	    int[] flags = evlist.getFlags();
	    DoubleArrayList selected = new DoubleArrayList();
	    for ( int i=0; i < evlist.nEvents(); i++ ) {

		if ( flags[i] >= min && flags[i] <= max ) {
		    selected.add(arrivalTimes[i]);
		}
	    }
	    selected.trimToSize();
	    return selected.elements();
	}
	else {
	    throw new EventListException("Flags are not defined");
	}
    }

    public static double[] getArrivalTimesInPatternRange(EventList evlist, int min, int max) throws EventListException {
	if ( evlist.patternsAreSet() ) {
	    double[] arrivalTimes = evlist.getArrivalTimes();
	    int[] patterns = evlist.getPatterns();
	    DoubleArrayList selected = new DoubleArrayList();
	    for ( int i=0; i < evlist.nEvents(); i++ ) {

		if ( patterns[i] >= min && patterns[i] <= max ) {
		    selected.add(arrivalTimes[i]);
		}
	    }
	    selected.trimToSize();
	    return selected.elements();
	}
	else {
	    throw new EventListException("Patterns are not defined");
	}
    }



}