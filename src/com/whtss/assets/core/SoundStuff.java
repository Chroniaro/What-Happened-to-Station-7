package com.whtss.assets.core;

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
	AudioInputStream audioStream;
	AudioInputStream aS;
	public SoundStuff() throws UnsupportedAudioFileException, IOException, LineUnavailableException {
		File audioFile = new File("na_sweden.wav");
	    audioStream = AudioSystem.getAudioInputStream(audioFile);
		AudioFormat format = audioStream.getFormat();
		DataLine.Info info = new DataLine.Info(Clip.class, format);
		audioClip = (Clip) AudioSystem.getLine(info);
		File aF = new File("David_Bowie_-_Starman_1_.wav");
	    aS = AudioSystem.getAudioInputStream(aF);
		AudioFormat f = audioStream.getFormat();
		DataLine.Info i = new DataLine.Info(Clip.class, f);
		aC = (Clip) AudioSystem.getLine(i);
	}

	public void swnat() throws UnsupportedAudioFileException, IOException, LineUnavailableException {
		try {
			aC.close();
			aS.close();
			audioClip.open(audioStream);
			audioClip.start();
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

	public void dbc() throws UnsupportedAudioFileException, IOException, LineUnavailableException {
		aC.close();
		aS.close();
	}
}