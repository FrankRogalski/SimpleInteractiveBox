package main;

import java.io.File;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.Scanner;
import java.util.SimpleTimeZone;

import javax.sound.sampled.AudioInputStream;

import marytts.LocalMaryInterface;
import marytts.MaryInterface;
import marytts.util.data.audio.AudioPlayer;

public class Haupt{
	private Scanner sc = new Scanner(System.in);
	private final String path = "E:/test/data.txt"; //durch "/home/pi/Desktop/Termine" ersetzten
	private final File f = new File(path);
	
	private MaryInterface marytts;
	private AudioPlayer ap;
	
	public Haupt() {
		try {
			marytts = new LocalMaryInterface();
	        marytts.setLocale(Locale.GERMAN);
	        marytts.setVoice("dfki-pavoque-neutral-hsmm");
		} catch (Exception ex) {}
		if (!f.exists()) {
			try {
				f.createNewFile();
			} catch (Exception ex) {}
		}
	}
	
	public void start() {
		while (true) {
			boolean correct = false;
			byte ans = 0;
			
			do {
				try {
					System.out.println();
					System.out.println("Guten Tag. Was möchten sie tun: ");
					System.out.println("1. Neuen Termin festlegen");
					System.out.println("2. Neuen Dauertermin festlegen");
					System.out.println("3. Vorhandene Termine anzeigen");
					System.out.println("4. Termin Löschen");
					System.out.println("5. Heutige Termine");
					System.out.println("6. Nächster Termin");
					
					ans = Byte.parseByte(sc.nextLine());
					
					if (ans < 1 || ans > 6) {
						throw new Exception();
					}
					
					correct = true;
				} catch (Exception ex) {
					System.out.println("Falsche Eingabe bitte geben sie eine Zahl zwischen 1 und 5 ein!");
					System.out.println();
				}
			} while(!correct);
			
			switch (ans) {
				case 1:
					neuerTermin();
					break;
				case 2:
					neuerDauertermin();
					break;
				case 3:
					showAll();
					break;
				case 4:
					entferneTermin();
					break;
				case 5:
					info();
					break;
				case 6:
					nextTermin();
					break;
			}
		}
	}
	
	private void neuerTermin() {
		String [] werte = new String[3];
		boolean correct = false;
		
		do {
			System.out.println("'Abbrechen' eingeben um zu beenden");
			System.out.print("Bitte geben sie den Namen des Termins ein: ");
			werte[0] = sc.nextLine();
			if (werte[0].equalsIgnoreCase("abbrechen")) {
				return;
			}
			if (werte[0].contains(",")) {
				System.out.println("In der Benennung darf kein ',' vorkommen");
			}
		}  while (werte[0].contains(","));
		
		do {
			System.out.println("'Abbrechen' eingeben um zu beenden");
			System.out.print("Bitte geben sie das Datum des Termins im Format 'DD.MM.YYYY' an: ");
			werte[1] = sc.nextLine();
			if (werte[1].equalsIgnoreCase("abbrechen")) {
				return;
			}
			if (!isDate("dd.MM.yyyy", werte[1] )) {
				System.out.println("Falsche Eingabe!");
				continue;
			}
			
			System.out.println("'Abbrechen' eingeben um zu beenden");
			System.out.print("Bitte geben sie die Uhrzeit des Termins im Format 'hh:mm' an: ");
			werte[2] = sc.nextLine();
			if (werte[2].equalsIgnoreCase("abbrechen")) {
				return;
			}
			if (!isDate("hh:mm", werte[2] )) {
				System.out.println("Falsche Eingabe!");
				continue;
			}
			
			try {
				final FileWriter fw = new FileWriter(f, true);
				fw.append(werte[0] + "," + werte[1] + "," + werte[2] + System.getProperty("line.separator"));
				fw.close();
				
				System.out.println("Termin erfolgreich gespeichert!");
				correct = true;
			} catch (Exception ex) {
				System.out.println("Falsche Eingabe!");
			}
		} while (!correct);
	}
	
	private void neuerDauertermin() {
		String [] werte = new String[4];
		boolean correct = false;
		
		do {
			System.out.println("'Abbrechen' eingeben um zu beenden");
			System.out.print("Bitte geben sie den Namen des Termins ein: ");
			werte[0] = sc.nextLine();
			if (werte[0].equalsIgnoreCase("abbrechen")) {
				return;
			}
			if (werte[0].contains(",")) {
				System.out.println("In der Benennung darf kein ',' vorkommen");
			}
		}  while (werte[0].contains(","));
		
		correct = false;
		do {
			try {
				System.out.println("'Abbrechen' eingeben um zu beenden");
				System.out.print("Bitte geben sie das Datum des Termins im Format 'DD.MM.YYYY' an: ");
				werte[1] = sc.nextLine();
				if (werte[1].equalsIgnoreCase("abbrechen")) {
					return;
				}
				if (!isDate("dd.MM.yyyy", werte[1] )) {
					System.out.println("Falsche Eingabe!");
					continue;
				}
				
				System.out.println("'Abbrechen' eingeben um zu beenden");
				System.out.print("Bitte geben sie die Uhrzeit des Termins im Format 'hh:mm' an: ");
				werte[2] = sc.nextLine();
				if (werte[2].equalsIgnoreCase("abbrechen")) {
					return;
				}
				if (!isDate("hh:mm", werte[2] )) {
					System.out.println("Falsche Eingabe!");
					continue;
				}
				
				do {
					try {
						System.out.println("'Abbrechen' eingeben um zu beenden");
						System.out.print("Bitte geben sie an in welchen Abständen sich dieser Termin wiederholt: ");
						werte[3] = sc.nextLine();
						if (werte[3].equalsIgnoreCase("abbrechen")) {
							return;
						}
						@SuppressWarnings("unused")
						final int test = Integer.parseInt(werte[3]);
						correct = true;
					} catch (Exception ex) {
						System.out.println("Falsche Eingabe!");
					}
				} while (!correct);
				
				final FileWriter fw = new FileWriter(f, true);
				fw.append(werte[0] + "," + werte[1] + "," + werte[2] + "," + werte[3] + System.getProperty("line.separator"));
				fw.close();
				
				System.out.println("Termin erfolgreich gespeichert!");
			} catch (Exception ex) {
				System.out.println("Falsche Eingabe!");
			}
		} while (!correct);
		
		correct = false;
		
		
		try {
			
		} catch (Exception ex) {
			System.out.println("Fehler beim Abspeichern des Termins!");
		}
	}
	
	private ArrayList<String> showAll() {
		try {
			int i = 1;
			final Scanner fsc = new Scanner(f);
			ArrayList<String> zeilen = new ArrayList<String>();
			String[] werte;
			
			while (fsc.hasNextLine()) {
				zeilen.add(fsc.nextLine());
				werte = zeilen.get(zeilen.size() - 1).split(",");
				System.out.print(i + " ");
				for (int n = 0;n < 3;n++) {
					System.out.print(werte[n] + " ");
				}
				System.out.println();
				i++;
			}
			
			fsc.close();
			return zeilen;
		} catch (Exception ex) {}
		return null;
	}
	
	private void entferneTermin() {
		ArrayList<String> termine = showAll();
		boolean correct = false;
		ArrayList<String> entf = new ArrayList<String>();
		ArrayList<String> rep = new ArrayList<String>();
		do {
			try {
				System.out.println("'Abbrechen' eingeben um zu beenden");
				System.out.print("Welchen Termin wollen sie entfernen: ");
				String ent = sc.nextLine();
				if (ent.equalsIgnoreCase("abbrechen")) {
					return;
				}
				entf.add(termine.get(Integer.parseInt(ent) - 1));
				rep.add(null);
				correct = true;
			} catch (Exception ex) {
				System.out.println("Falsche Eingabe!");
			}
		} while (!correct);
		
		try {
			changeLine(entf, rep);
		} catch (Exception ex) {}
	}
	
	private void changeLine(ArrayList<String> line, ArrayList<String> replace) throws Exception {
		if (line.size() != replace.size()) {
			throw new Exception();
		}
		try {
			File tf = new File(f.getAbsolutePath() + ".tmp");
			if (!tf.exists()) {
				tf.createNewFile();
			}
			
			Scanner fsc = new Scanner(f);
			String zeile;
			
			FileWriter fw = new FileWriter(tf);
			
			for (int i = 0;i < line.size();i++) {
				while (fsc.hasNextLine()) {
					zeile = fsc.nextLine();
					if (!zeile.equals(line.get(i))) {
						fw.append(zeile + System.getProperty("line.separator"));
					} else if (replace.get(i) != null){
						fw.append(replace.get(i) + System.getProperty("line.separator"));
					}
				}
			}
			
			
			fw.close();
			fsc.close();
			
			f.delete();
			tf.renameTo(f);
			System.out.println("Termin erfolgreich gelöscht!");
		} catch (Exception ex) {
			System.out.println("Fehler beim Löschen der Datei!");
		}
	}
	
	public void info() {
		try {
			Scanner fsc = new Scanner(f);
			ArrayList<String> zeilen = new ArrayList<String>();
			ArrayList<String> entf = new ArrayList<String>();
			ArrayList<String> rep = new ArrayList<String>();
			String[] werte;
			boolean looped = false;
			String zeile;
			
			talk("Heute ist");
			werte = new SimpleDateFormat("dd.MM.yyyy hh:mm aa").format(new Date()).split(" ");
			talk(getWochentag(new SimpleDateFormat("dd.MM.yyyy").parse(werte[0])));
			talk("der");
			
			String wert = werte[0].substring(0, 2);
			wert = wert.replaceAll("0", " ");
			wert = wert.replaceAll("1 ", "10");
			talk(wert + ".");
			
			wert = werte[0].substring(3, 5);
			wert = wert.replaceAll("0", " ");
			wert = wert.replaceAll("1 ", "10");
			talk(wert + ".");
			
			talk(werte[0].substring(6, 10));
			
			if (werte[2].equalsIgnoreCase("pm")) {
				werte[1] = String.valueOf((Integer.parseInt(werte[1].substring(0, 2)) + 12)) + werte[1].substring(2);
			}
			
			termin(" ," + werte[0] + "," + werte[1]);
			
			while(fsc.hasNextLine()) {
				zeile = fsc.nextLine();
				werte = zeile.split(",");
				
				try {
					Date verg = new SimpleDateFormat("dd.MM.yyyy hh:mm:ss").parse(werte[1] + " 23:59:59");
					if (verg.before(new Date())) {
						if (werte.length == 4) {
							while (verg.before(new Date())) {
								verg = incDate(verg, Integer.parseInt(werte[3]));
							}
							werte[1] = new SimpleDateFormat("dd.MM.yyyy").format(verg);
							entf.add(zeile);
							rep.add(werte[0] + "," + werte[1] + "," + werte[2] + "," + werte[3]);
							looped = true;
						} else {
							looped = true;
							entf.add(zeile);
							rep.add(null);
						}
						continue;
					}
				} catch (Exception ex) {}
				
				if (new SimpleDateFormat("dd.MM.yyyy").format(new Date()).equals(werte[1])) {
					zeilen.add(zeile);
				}
			}
			fsc.close();
			if (looped) {
				changeLine(entf, rep);
			}
			if (zeilen.size() == 1) {
				talk("Sie haben heute folgenden Termin: ");
				termin(zeilen.get(0));
			} else if (zeilen.size() > 1){
				talk("Sie haben heute folgende Termine: ");
				for (int i = 0;i < zeilen.size();i++) {
					termin(zeilen.get(i));
				}
			} else {
				talk("Sie haben heute keine Termine");
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	public void nextTermin() {
		try {
			Scanner fsc = new Scanner(f);
			SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy hh:mm");
			Date nearest = sdf.parse("31.12.9999 23:59");
			String[] clWerte = null;
			while(fsc.hasNextLine()) {
				String[] werte = fsc.nextLine().split(",");
				if(nearest.after(sdf.parse(werte[1] + " " + werte[2])) && sdf.parse(werte[1] + " " + werte[2]).after(new Date())) {
					nearest = sdf.parse(werte[1] + " " + werte[2]);
					clWerte = werte.clone();
				}
			}
			if(clWerte != null) {
				talk("Ihr nächster Termin ist am ");
				
				talk(getWochentag(new SimpleDateFormat("dd.MM.yyyy").parse(clWerte[1])));
				
				String wert = clWerte[1].substring(0, 2);
				wert = wert.replaceAll("0", " ");
				wert = wert.replaceAll("1 ", "10");
				talk(wert + ". n");
				
				wert = clWerte[1].substring(3, 5);
				wert = wert.replaceAll("0", " ");
				wert = wert.replaceAll("1 ", "10");
				talk(wert + ". n");
				
				talk(clWerte[1].substring(6, 10));
				
				if (clWerte.length == 4) {
					termin(clWerte[0] + "," + clWerte[1] + "," + clWerte[2] + "," + clWerte[3]);
				} else {
					termin(clWerte[0] + "," + clWerte[1] + "," + clWerte[2]);
				}
			}
			fsc.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	private boolean isDate(String format, String date) {
		try {
			@SuppressWarnings("unused")
			final Date d = new SimpleDateFormat(format).parse(date);
			return true;
		} catch(Exception ex) {
			return false;
		}
	}
	
	private String getWochentag(Date d) {
		new SimpleTimeZone(0,"CET");
		GregorianCalendar oCalendar = new GregorianCalendar(SimpleTimeZone.getDefault());
		String[] werte = new SimpleDateFormat("dd-MM-yyyy").format(d).split("-");
		oCalendar.set(Integer.parseInt(werte[2]), Integer.parseInt(werte[1]), Integer.parseInt(werte[0]));
		int wTag = oCalendar.get(GregorianCalendar.DAY_OF_WEEK);
		switch (wTag) {
		case 1:
			return "Sonntag";
		case 2:
			return "Montag";
		case 3:
			return "Dienstag";
		case 4:
			return "Mittwoch";
		case 5: 
			return "Donnerstag";
		case 6:
			return "Freitag";
		default:
			return "Samstag";
		}
	}
	
	private Date incDate(Date d, int inc) {
		Calendar c = Calendar.getInstance();
		c.setTime(d);
		c.add(Calendar.DATE, inc);
		d = c.getTime();
		return d;
	}
	
	private void termin(String z) {
		final String tageszeit;
		String[] werte = z.split(",");
		
		talk(werte[0]);
		talk("um");
		
		final int stunde = Integer.parseInt(werte[2].substring(0, 2));
		if(stunde >= 0 && stunde <= 6 || stunde <= 24 && stunde >= 22) {
			tageszeit = "nachts";
		} else if (stunde >= 7 && stunde <= 8) {
			tageszeit = "morgens";
		} else if (stunde >= 9 && stunde <= 11) {
			tageszeit = "vormittags";
		} else if (stunde >= 12 && stunde <= 14) {
			tageszeit = "mittags";
		} else if (stunde >= 15 && stunde <= 17) {
			tageszeit = "nachmittags";
		} else {
			tageszeit = "abends";
		}
		
		String z1 = werte[2].substring(0, 2);
		z1 = z1.replaceAll("01", "ein");
		if (z1.charAt(0) == '0') {
			z1 = z1.replaceFirst("0", " ");
		}
		talk(z1);
		
		talk("Uhr");
		
		z1 = werte[2].substring(3, 5);
		z1 = z1.replaceAll("0", " ");
		z1 = z1.replaceAll("1 ", "10");
		talk(z1);
		
		talk(tageszeit);
	}
	
	private void talk(String voiceMsg) {
	    try {
	    	voiceMsg = voiceMsg.trim();
    		if (ap != null) {
	    		ap.join();
	    	}
            AudioInputStream audio = marytts.generateAudio(voiceMsg);
            ap = new AudioPlayer();
            ap.setAudio(audio);
            ap.start();
        }
        catch (Exception ex){}
	}
}