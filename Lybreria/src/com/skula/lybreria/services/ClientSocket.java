package com.skula.lybreria.services;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import com.skula.lybreria.models.Command;
import com.skula.lybreria.models.ExplorerItem;

public class ClientSocket {

	public static List<ExplorerItem> sendInstruction(Command cmd) {
		Socket socket = null;
		ObjectOutputStream oos = null;
		ObjectInputStream ois = null;
		List<ExplorerItem> list = new ArrayList<ExplorerItem>();

		try {
			socket = new Socket("192.168.1.52", 8889);
			oos = new ObjectOutputStream(socket.getOutputStream());
			ois = new ObjectInputStream(socket.getInputStream());
			oos.writeObject(cmd);
			list = (ArrayList<ExplorerItem>) ois.readObject();
			oos.flush();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} finally {
			if (socket != null) {
				try {
					socket.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

			if (oos != null) {
				try {
					oos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

			if (ois != null) {
				try {
					ois.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return list;
	}
}
