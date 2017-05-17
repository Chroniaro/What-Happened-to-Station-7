package com.whtss.assets.render;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class SoundStuff {
	
	Clip audioClip;
	Clip aC;
	Clip insane;
	Clip Phazer;
	Clip Lazer;
	Clip Lenin;
	Clip snope;
	AudioInputStream audioStream;
	AudioInputStream aS;
	AudioInputStream flames;
	AudioInputStream Paser;
	AudioInputStream Laser;
	AudioInputStream Marx;
	AudioInputStream csgo;
	public SoundStuff() throws UnsupportedAudioFileException, IOException, LineUnavailableException {
		File audioFile = new File("na_sweden.wav");
	    audioStream = AudioSystem.getAudioInputStream(audioFile);
		AudioFormat format = audioStream.getFormat();
		DataLine.Info info = new DataLine.Info(Clip.class, format);
		audioClip = (Clip) AudioSystem.getLine(info);
		
		File aF = new File("David_Bowie_-_Starman_1_.wav");
	    aS = AudioSystem.getAudioInputStream(aF);
		AudioFormat f = aS.getFormat();
		DataLine.Info i = new DataLine.Info(Clip.class, f);
		aC = (Clip) AudioSystem.getLine(i);
		
		File burn = new File("burnest.wav");
	    flames = AudioSystem.getAudioInputStream(burn);
		AudioFormat pyro = flames.getFormat();
		DataLine.Info mental = new DataLine.Info(Clip.class, pyro);
		insane = (Clip) AudioSystem.getLine(mental);
		
		File StarTrek = new File("Paz.wav");
		Paser = AudioSystem.getAudioInputStream(StarTrek);
		AudioFormat kirk = Paser.getFormat();
		DataLine.Info spock = new DataLine.Info(Clip.class, kirk);
		Phazer = (Clip) AudioSystem.getLine(spock);
		
		File StarTreker = new File("Laz.wav");
		Laser = AudioSystem.getAudioInputStream(StarTreker);
		AudioFormat bones = Paser.getFormat();
		DataLine.Info worf = new DataLine.Info(Clip.class, bones);
		Lazer = (Clip) AudioSystem.getLine(worf);
		
		File Putin = new File("CCCP.wav");
		Marx = AudioSystem.getAudioInputStream(Putin);
		AudioFormat Communism = Paser.getFormat();
		DataLine.Info Comrade = new DataLine.Info(Clip.class, Communism);
		Lenin = (Clip) AudioSystem.getLine(Comrade);
		
		File ct = new File("CCCP.wav");
		csgo = AudioSystem.getAudioInputStream(ct);
		AudioFormat ttp = csgo.getFormat();
		DataLine.Info ptp = new DataLine.Info(Clip.class, ttp);
		snope = (Clip) AudioSystem.getLine(ptp);
	}

	public void swnat() throws UnsupportedAudioFileException, IOException, LineUnavailableException {
		try {
			audioClip.open(audioStream);
			audioClip.start();
		} catch (MalformedURLException murle) {
			System.out.println(murle);
		}
	}
	public void Phazing() throws UnsupportedAudioFileException, IOException, LineUnavailableException {
		try {
			Phazer.open(Paser);
			Phazer.start();
		} catch (MalformedURLException murle) {
			System.out.println(murle);
		}
	}


	public void stnaw() throws UnsupportedAudioFileException, IOException, LineUnavailableException {
		audioClip.close();
		audioStream.close();
	}
	public void dbo() throws UnsupportedAudioFileException, IOException, LineUnavailableException {
		try {
			audioClip.close();
			audioStream.close();
			aC.open(aS);
			aC.start();
		} catch (MalformedURLException murle) {
			System.out.println(murle);
		}
	}
	public void dbol() throws UnsupportedAudioFileException, IOException, LineUnavailableException {
		try {
			audioClip.close();
			audioStream.close();
			aC.open(aS);
			aC.start();
			aC.loop(10);
		} catch (MalformedURLException murle) {
			System.out.println(murle);
		}
	}

	public void dbc() throws UnsupportedAudioFileException, IOException, LineUnavailableException {
		aC.close();
		aS.close();
	}
	public void flame() throws UnsupportedAudioFileException, IOException, LineUnavailableException {
		try {
			insane.open(flames);
			insane.start();
		} catch (MalformedURLException murle) {
			System.out.println(murle);
		}
	}
	public void CCCP() throws UnsupportedAudioFileException, IOException, LineUnavailableException {
		try {
			Lenin.open(Marx);
			Lenin.start();
		} catch (MalformedURLException murle) {
			System.out.println(murle);
		}
	}
	public void AWP() throws UnsupportedAudioFileException, IOException, LineUnavailableException {
		try {
			snope.open(csgo);
			snope.start();
		} catch (MalformedURLException murle) {
			System.out.println(murle);
		}
	}
}
