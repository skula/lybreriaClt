package com.skula.lybreria.models;
import java.io.Serializable;

public class ExplorerItem implements Serializable , Comparable<ExplorerItem> {
	private static final long serialVersionUID = 9017710895900248365L;

	private boolean isDirectory;
	private String path;
	private String name;
	private boolean subtitled;

	public ExplorerItem() {
	}

	public ExplorerItem(String path, String name, boolean isDirectory, boolean subtitled) {
		this.isDirectory = isDirectory;
		this.name = name;
		this.path = path;
		this.subtitled = subtitled;
	}

	public boolean isDirectory() {
		return isDirectory;
	}

	public void setDirectory(boolean isDirectory) {
		this.isDirectory = isDirectory;
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

	public boolean isSubtitled() {
		return subtitled;
	}

	public void setSubtitled(boolean subtitled) {
		this.subtitled = subtitled;
	}

	@Override
	public String toString() {
		return path + " (" + isDirectory + "," + subtitled + ")";
	}

	@Override
	public int compareTo(ExplorerItem o) {
		return name.compareTo(o.getName());
	}
}