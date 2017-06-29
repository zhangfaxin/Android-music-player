package com.douniao.music.Utils;

import java.io.Serializable;

public class MusicDemo implements Serializable{
	
	private static final long serialVersionUID = 1L;
	private String name;
	private String path;
	private String artist;
	
	public MusicDemo() {
		// TODO Auto-generated constructor stub
	}
	

	public MusicDemo(String name, String path ,String artist) {
		super();
		this.name = name;
		this.path = path;
		this.artist = artist;
	}
	
	@Override
	public String toString() {
		return "MusicDemo [name=" + name + ", path=" + path + ", artist=" + artist + "]";
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public String getPath() {
		return path;
	}


	public void setPath(String path) {
		this.path = path;
	}


	public String getArtist() {
		return artist;
	}


	public void setArtist(String artist) {
		this.artist = artist;
	}

	
}
