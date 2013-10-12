package com.skula.lybreria.models;

import java.io.Serializable;

import com.skula.lybreria.definitions.Definitions;

public class Command implements Serializable {
	private static final long serialVersionUID = -6911980748348568315L;

	private int type;
	private String argument;

	public Command() {
	}
	
	public String getMessage(){
		switch(type){
		case Definitions.TYPE_EXPLORE:
			return "Ouvrir " + argument;
		case Definitions.TYPE_PLAY_SINGLE:
			return "Jouer "+ argument;
		case Definitions.TYPE_PLAY_DIR:
			return "Jouer "+ argument;
		case Definitions.TYPE_QUEUE_SINGLE:
			return "Mettre en queue "+ argument;
		case Definitions.TYPE_QUEUE_DIR:
			return "Mettre en queue "+ argument;
		case Definitions.TYPE_EXEC_CMD:
			return "Executer commande";
		default:
			return "";
		}
	}

	public Command(int type, String argument) {
		this.type = type;
		this.argument = argument;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getArgument() {
		return argument;
	}

	public void setArgument(String argument) {
		this.argument = argument;
	}
}
