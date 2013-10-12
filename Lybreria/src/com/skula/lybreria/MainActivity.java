package com.skula.lybreria;

import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.skula.lybreria.definitions.Definitions;
import com.skula.lybreria.models.Command;
import com.skula.lybreria.models.ExplorerItem;
import com.skula.lybreria.services.ClientSocket;
import com.skula.lybreria.services.DatabaseService;

public class MainActivity extends Activity {
	private ListView itemList;
	private DatabaseService dbService;
	private int lastpos;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		StrictMode.setThreadPolicy(policy);
		setContentView(R.layout.activity_main);

		dbService = new DatabaseService(this);
		//dbService.bouchon();
		itemList = (ListView) findViewById(R.id.itemList);
		updateList(dbService.getFavorites());

		itemList.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> arg0, View v, int position, long id) {
				ExplorerItem item = (ExplorerItem) itemList.getItemAtPosition(position);

				int type = item.isDirectory() ? Definitions.TYPE_EXPLORE : Definitions.TYPE_PLAY_SINGLE;
				Command cmd = new Command(type, item.getPath());

				Toast.makeText(v.getContext(), cmd.getMessage(), Toast.LENGTH_SHORT).show();
				
				//ProgressDialog progressDialog = ProgressDialog.show(v.getContext(), "", "Chargement...");
				List<ExplorerItem> list = ClientSocket.sendInstruction(cmd);
				if (!list.isEmpty()) {
					updateList(list);
					lastpos=position;
				}
				//progressDialog.dismiss();
			}
		});

		itemList.setOnItemLongClickListener(new OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> a, View v, int position, long id) {
				ExplorerItem item = (ExplorerItem) itemList.getItemAtPosition(position);
				if (item.isDirectory()) {
					Command cmd = new Command(Definitions.TYPE_PLAY_DIR, item.getPath());
					Toast.makeText(v.getContext(), cmd.getMessage(), Toast.LENGTH_SHORT).show();
					ClientSocket.sendInstruction(cmd);
				}
				return true;
			}
		});
		
		ImageView pictPrevious = (ImageView)findViewById(R.id.pictPrevious);
		pictPrevious.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				sendExecCommand(Definitions.CMD_PREVIOUS,"Morceau précédent");
			}
		});
		
		ImageView pictPlay = (ImageView)findViewById(R.id.pictPlay);
		pictPlay.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				sendExecCommand(Definitions.CMD_PLAYPAUSE,"Jouer");
			}
		});
		
		ImageView pictPause = (ImageView)findViewById(R.id.pictPause);
		pictPause.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				sendExecCommand(Definitions.CMD_PLAYPAUSE,"Pause");
			}
		});
		
		ImageView pictStop = (ImageView)findViewById(R.id.pictStop);
		pictStop.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				sendExecCommand(Definitions.CMD_STOP,"Stop");
			}
		});
		
		ImageView pictNext = (ImageView)findViewById(R.id.pictNext);
		pictNext.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				sendExecCommand(Definitions.CMD_NEXT,"Morceau suivant");
			}
		});
		
		ImageView pictSoundMinus = (ImageView)findViewById(R.id.pictSoundMinus);
		pictSoundMinus.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				sendExecCommand(Definitions.CMD_SOUND_MINUS,"Volume -");
			}
		});
		
		ImageView pictSoundPlus = (ImageView)findViewById(R.id.pictSoundPlus);
		pictSoundPlus.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				sendExecCommand(Definitions.CMD_SOUND_PLUS,"Volume +");
			}
		});
	}
	
	private void sendExecCommand(String cmdLine, String text){
		Command cmd = new Command(Definitions.TYPE_EXEC_CMD, cmdLine);
		Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
		ClientSocket.sendInstruction(cmd);
	}

	private void updateList(List<ExplorerItem> list) {
		ExplorerItem itemArray[] = (ExplorerItem[]) list.toArray(new ExplorerItem[list.size()]);
		ItemAdapter adapter = new ItemAdapter(this, R.layout.itemlayout, itemArray);
		itemList.setAdapter(adapter);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menu_settings:
			return true;
		case R.id.menu_favorites:
			updateList(dbService.getFavorites());
			return true;
		default:
			return false;
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			ExplorerItem item = (ExplorerItem) itemList.getItemAtPosition(0);
			Command cmd = new Command(Definitions.TYPE_EXPLORE, item.getPath());
			Toast.makeText(this, cmd.getMessage(), Toast.LENGTH_SHORT).show();
			List<ExplorerItem> list = ClientSocket.sendInstruction(cmd);
			if (!list.isEmpty()) {
				updateList(list);
				itemList.setSelection(lastpos);
			}
		}
		return true;
	}
}
