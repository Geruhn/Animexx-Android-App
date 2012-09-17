package de.meisterfuu.animexx.ENS;

import java.util.ArrayList;
import java.util.Collections;

import org.json.JSONArray;
import org.json.JSONObject;

import com.actionbarsherlock.app.SherlockListFragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView.OnItemClickListener;
import de.meisterfuu.animexx.R;
import de.meisterfuu.animexx.Request;
import de.meisterfuu.animexx.other.UserObject;

public class ENSEingangFragment extends SherlockListFragment {

	ArrayList<ENSObject> Array = new ArrayList<ENSObject>();
	ArrayList<ENSObject> ENSArray = new ArrayList<ENSObject>();
	ArrayList<ENSObject> ORDNER = new ArrayList<ENSObject>();

	int page = 0;
	int mPrevTotalItemCount = 0;
	int loadCount = 0;
	long folder = 1;
	String typ = "an";
	boolean first = false;

	Context context = this.getSherlockActivity();

	ProgressDialog dialog;
	RelativeLayout Loading;
	Boolean loading;

	ENSAdapter adapter;
	boolean error = false;


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.listview_loading_bot, null);
		Loading = (RelativeLayout) view.findViewById(R.id.RPGloading);
		Loading.setVisibility(View.GONE);
		context = this.getSherlockActivity();
		return view;
	}


	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		adapter = new ENSAdapter(this.getSherlockActivity(), Array);
		Request.config = PreferenceManager.getDefaultSharedPreferences(this.getSherlockActivity());
		context = this.getSherlockActivity();

		setListAdapter(adapter);

		ListView lv = getListView();

		lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

			public boolean onItemLongClick(AdapterView<?> av, View v, int position, long id) {
				if (position >= ORDNER.size()) {
					position = position - ORDNER.size();
					ENSPopUp Menu = new ENSPopUp(context, ENSArray.get(position).getVon().getUsername(), ENSArray.get(position).getVon().getId(), ENSArray.get(position).getENS_id(), ENSArray
							.get(position).getBetreff(), typ, 1);
					Menu.PopUp();
				}

				return true;
			}
		});

		lv.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

				if (position < ORDNER.size()) {
					page = 0;
					mPrevTotalItemCount = 0;
					loadCount = 0;
					folder = ORDNER.get(position).getENS_id();
					typ = ORDNER.get(position).getAnVon();
					ENSArray.clear();
					//Request.doToast(""+page, context);
					Show();
					refresh();
				} else {
					Bundle bundle = new Bundle();
					bundle.putLong("id", ENSArray.get(position - ORDNER.size()).getENS_id());
					// if(t != null && t.getText().equalsIgnoreCase("") == false) bundle.putBoolean("sql", true);
					Intent newIntent = new Intent(context, ENSSingle.class);
					newIntent.putExtras(bundle);
					startActivity(newIntent);
				}

			}

		});

		lv.setOnScrollListener(new OnScrollListener() {

			public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
				if (view.getAdapter() != null && ((firstVisibleItem + visibleItemCount) >= totalItemCount) && totalItemCount != mPrevTotalItemCount) {
					mPrevTotalItemCount = totalItemCount;
					if (!error) refresh();
				}
			}


			public void onScrollStateChanged(AbsListView view, int scrollState) {
				// Useless Forced Method -.-
			}
		});

		refreshInit();
	}


	/* @Override
	public void onBackPressed() {
		if (folder == 1 || folder == 2) {
			// super.onBackPressed();
		} else {
			if (typ == "an") {
				folder = 1;
			} else {
				folder = 2;
			}
			page = 0;
			mPrevTotalItemCount = 0;
			loadCount = 0;
			ENSArray.clear();
			Show();
			refresh();
		}

	}
	*/

	public void DoError() {
		Request.doToast("Es ist ein Fehler aufgetreten.", this.getSherlockActivity());
	}


	public void LoadPlus() {
		loadCount++;
		Loading.setVisibility(View.VISIBLE);
	}


	public void LoadMinus() {
		loadCount--;
		if (loadCount == 0) {
			Loading.setVisibility(View.GONE);
			// Collections.sort(Array);
		}
	}


	public void refresh() {
		// ENSArray = new ArrayList<ENSObject>();
		getENS(ENSArray, folder, typ, page++);
	}


	public void refreshInit() {
		if(!first){
			first = !first;
		} else return;
		ORDNER = new ArrayList<ENSObject>();
		ENSArray = new ArrayList<ENSObject>();
		getFolder(ORDNER);
		getENS(ENSArray, folder, typ, page++);
	}


	public void Show() {
		Array.clear();
		Collections.sort(ENSArray);
		Array.addAll(ORDNER);
		Array.addAll(ENSArray);
		adapter.notifyDataSetChanged();
	}


	public void getFolder(final ArrayList<ENSObject> Liste) {
		LoadPlus();
		final ENSEingangFragment temp = this;
		new Thread(new Runnable() {

			public void run() {
				try {
					final String JSON = Request.makeSecuredReq("https://ws.animexx.de/json/ens/ordner_liste/?api=2");
					JSONArray list = null;

					JSONObject jsonResponse = new JSONObject(JSON);

					list = jsonResponse.getJSONObject("return").getJSONArray("an");
					// max = FolderList.getJSONObject(0).getInt("gesamt");
					if (list.length() != 0) {
						for (int i = 0; i < list.length(); i++) {
							if (list.getJSONObject(i).getLong("ordner_id") != 3L) {
								ENSObject temp = new ENSObject();

								temp.setBetreff(list.getJSONObject(i).getString("name"));
								temp.setENS_id(list.getJSONObject(i).getLong("ordner_id"));
								if (list.getJSONObject(i).has("ungelesen"))
									temp.setSignatur(list.getJSONObject(i).getString("ungelesen"));
								else
									temp.setSignatur("0");
								temp.setTyp(99);
								temp.setOrdner(1);
								temp.setAnVon("an");

								Liste.add(temp);
							}
						}
					}



					temp.getSherlockActivity().runOnUiThread(new Runnable() {

						public void run() {
							Show();
							LoadMinus();

							new Thread(new Runnable() {

								public void run() {
									ENSsql SQL = new ENSsql(temp.getSherlockActivity());
									SQL.open();
									SQL.clearFolder("1");
									for (int i = 0; i < Liste.size(); i++) {
										SQL.createFolder(Liste.get(i));
									}
									SQL.close();
								}

							}).start();
						}
					});

				} catch (Exception e) {
					temp.getSherlockActivity().runOnUiThread(new Runnable() {

						public void run() {
							Show();
							LoadMinus();
							DoError();
						}
					});
					e.printStackTrace();
				}

			}
		}).start();
	}


	public void getENS(final ArrayList<ENSObject> Liste, final long folder, final String typ, final int page) {
		LoadPlus();
		final ENSEingangFragment temp = this;
		new Thread(new Runnable() {

			public void run() {
				try {
					final String JSON = Request.makeSecuredReq("https://ws.animexx.de/json/ens/ordner_ens_liste/?ordner_id=" + folder + "&ordner_typ=" + typ + "&seite=" + page + "&api=2");
					JSONArray list = null;

					JSONObject jsonResponse = new JSONObject(JSON);

					list = jsonResponse.getJSONArray("return");
					// max = FolderList.getJSONObject(0).getInt("gesamt");
					for (int i = 0; i < list.length(); i++) {
						ENSObject tempENS = new ENSObject();

						tempENS.setBetreff(list.getJSONObject(i).getString("betreff"));
						tempENS.setTime(list.getJSONObject(i).getString("datum_server"));

						UserObject von = new UserObject();
						von.ParseJSON(list.getJSONObject(i).getJSONObject("von"));
						tempENS.setVon(von);

						for (int z = 0; z < list.getJSONObject(i).getJSONArray("an").length(); z++) {
							try{
								UserObject an = new UserObject();
								an.ParseJSON(list.getJSONObject(i).getJSONArray("an").getJSONObject(z));
								tempENS.addAnUser(an);
							} catch (Exception e) {

							}
						}

						tempENS.setFlags(list.getJSONObject(i).getInt("an_flags"));
						tempENS.setENS_id(list.getJSONObject(i).getLong("id"));
						tempENS.setTyp(list.getJSONObject(i).getInt("typ"));
						tempENS.setOrdner(folder);
						tempENS.setAnVon(typ);

						Liste.add(tempENS);
					}

					temp.getSherlockActivity().runOnUiThread(new Runnable() {

						public void run() {
							Show();
							LoadMinus();
						}
					});

				} catch (Exception e) {
					temp.getSherlockActivity().runOnUiThread(new Runnable() {

						public void run() {
							Show();
							LoadMinus();
							DoError();
						}
					});
					e.printStackTrace();
				}

			}
		}).start();
	}

}
