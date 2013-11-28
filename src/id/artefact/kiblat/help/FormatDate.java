package id.artefact.kiblat.help;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class FormatDate {
	
	public String convert(String oldDateString){
		final String OLD_FORMAT = "yyyy-MM-dd";
		final String NEW_FORMAT = "dd MMM yyyy";

		String newDateString = null;

		SimpleDateFormat sdf = new SimpleDateFormat(OLD_FORMAT);
		Date d;
		try {
			d = sdf.parse(oldDateString);
			sdf.applyPattern(NEW_FORMAT);
			newDateString = sdf.format(d);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		
		return newDateString;
	}
	
	public String convertTime(String oldDateString){
		final String OLD_FORMAT = "yyyy-MM-dd HH:mm:ss";
		final String NEW_FORMAT = "dd MMM yyyy";
		final String TIME_FORMAT = "HH:mm";

		String newDateString = null;

		SimpleDateFormat sdf = new SimpleDateFormat(OLD_FORMAT);
		SimpleDateFormat xdf = new SimpleDateFormat(OLD_FORMAT);
		Date d,f;
		try {
			d = sdf.parse(oldDateString);
			sdf.applyPattern(NEW_FORMAT);
			
			f = xdf.parse(oldDateString);
			xdf.applyPattern(TIME_FORMAT);
			
			newDateString = sdf.format(d)+" at "+xdf.format(f);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		
		return newDateString;
	}
	
}
