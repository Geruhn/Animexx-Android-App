package de.meisterfuu.animexx.other;

import com.actionbarsherlock.app.ActionBar;

import android.app.Activity;
import android.content.Intent;

import de.meisterfuu.animexx.R;
import de.meisterfuu.animexx.about;
import de.meisterfuu.animexx.ENS.ENSActivity;
import de.meisterfuu.animexx.GB.GBViewList;
import de.meisterfuu.animexx.Home.HomeKontaktActivity;
import de.meisterfuu.animexx.Home.HomeKontaktFragment;
import de.meisterfuu.animexx.RPG.RPGViewList;
import de.meisterfuu.animexx.events.EventViewList;
import de.meisterfuu.animexx.other.SlideMenuInterface.OnSlideMenuItemClickListener;
import de.meisterfuu.animexx.overview.OverviewActivity;

public class SlideMenuHelper implements OnSlideMenuItemClickListener {

	private SlideMenu slidemenu;
	private Activity a;
	private int SlideSpeed = 200;


	public SlideMenuHelper(Activity A, ActionBar AC) {
		a = A;
		slidemenu = (SlideMenu) a.findViewById(R.id.slideMenu);
		slidemenu.init(a, R.menu.slide, this, SlideSpeed, AC);
	}


	public SlideMenu getSlideMenu() {
		return slidemenu;
	}


	public void onSlideMenuItemClick(int itemId) {

		switch (itemId) {
		case R.id.Settings:
			a.startActivity(new Intent().setClass(a.getApplicationContext(), Settings.class));
			break;
		case R.id.RPG:
			a.startActivity(new Intent().setClass(a.getApplicationContext(), RPGViewList.class));
			break;
		case R.id.ENS:
			a.startActivity(new Intent().setClass(a.getApplicationContext(), ENSActivity.class));
			break;
		case R.id.Home:
			//a.startActivity(new Intent().setClass(a.getApplicationContext(), PersonalHomeListAll.class));
			a.startActivity(new Intent().setClass(a.getApplicationContext(), HomeKontaktActivity.class));
			break;
		case R.id.About:
			a.startActivity(new Intent().setClass(a.getApplicationContext(), about.class));
			break;
		case R.id.Guestbook:
			a.startActivity(new Intent().setClass(a.getApplicationContext(), GBViewList.class));
			break;
		case R.id.Kontakte:
			a.startActivity(new Intent().setClass(a.getApplicationContext(), ContactList.class));
			break;
		case R.id.Events:
			a.startActivity(new Intent().setClass(a.getApplicationContext(), EventViewList.class));
			break;
		case R.id.Dashboard:
			a.startActivity(new Intent().setClass(a.getApplicationContext(), OverviewActivity.class));
			break;
		}

	}
}
