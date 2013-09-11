package de.meisterfuu.animexx.cards;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.fima.cardsui.objects.Card;

import de.meisterfuu.animexx.R;

public class BirthdayCard extends Card {

	public BirthdayCard(String title, String description){
		super(title, description);
	}

	@Override
	public View getCardContent(Context context) {
		View view = LayoutInflater.from(context).inflate(R.layout.card_birthday, null);
		
		((TextView) view.findViewById(R.id.description)).setText(desc);
		((TextView) view.findViewById(R.id.title)).setText(title);
		
		return view;
	}

	
	
	
}
