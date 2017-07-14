package com.milesseventh.dsa;

import java.io.File;
import java.util.Arrays;
import java.util.Comparator;


public class Main {	
	private static class Entry{
		public String name;
		public long length;
	}

	final static short BAR_MAX_LENGTH = 32;
	
	public static void main(String[] args) {
		if (args.length == 0)
			System.out.println("Use path to analyze as program parameter");
		else
			for (int i = 0; i < args.length; i++)
				printTree(new File(args[i]));
	}

	public static void printTree(File victim){
		if (!victim.exists()) return;
		System.out.println("\n\n" + victim.getAbsolutePath() + "\n" + repeatChar(54 + BAR_MAX_LENGTH, '_'));
		
		Entry[] list = new Entry[victim.listFiles().length];
		long sumtotal = 0;
		int i = 0;
		
		for (File horsey: victim.listFiles()){
			list[i] = new Entry();
			list[i].name = String.format("%.27s", String.format("%-27s", (horsey.isDirectory()?"/":"") + horsey.getName()));
			sumtotal += list[i].length = (horsey.isDirectory()?getDirLength(horsey):horsey.length());
			i++;
		}
		
		final long st = sumtotal;
		Arrays.sort(list, new Comparator<Entry>(){
			public int compare(Entry a, Entry b){
				return (int)((b.length - a.length) / (float)st * 100000);
			}
		});
		
		for (Entry horsey: list)
			System.out.println(horsey.name + " | " + String.format("%20s", String.format("%.3f", horsey.length / 1024L / 1024f) + " MiB") + " | " + generateBar(horsey.length / (float)sumtotal));
	}
	
	public static long getDirLength(File victim){
		long result = 0;
		if (victim == null || victim.isHidden()) return 0;
		for (File horsey: victim.listFiles())
			if (horsey.isDirectory())
				result += getDirLength(horsey);
			else if (horsey.isFile())
				result += horsey.length();
		return result;
	}
	
	public static String generateBar(double victim){
		short barlength = (short)Math.round(BAR_MAX_LENGTH * victim);
		return "[" + repeatChar(barlength, '█') + repeatChar((short)(BAR_MAX_LENGTH - barlength), '-') + "]";
	}
	
	public static String repeatChar(int len, char ch){
		String goddamn = "";
		for (short i = 0; i < len; i++)
			goddamn += ch;
		return goddamn;
	}
}