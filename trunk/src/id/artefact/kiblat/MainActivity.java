package id.artefact.kiblat;


import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu.CanvasTransformer;

import android.os.Bundle;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Canvas;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Interpolator;
import android.widget.TextView;

public class MainActivity extends CustomAnimation {
	
	public MainActivity() {
		// see the class CustomAnimation for how to attach 
		// the CanvasTransformer to the SlidingMenu
		super(R.string.anim_slide, new CanvasTransformer() {
			@Override
			public void transformCanvas(Canvas canvas, float percentOpen) {
				canvas.translate(0, canvas.getHeight()*(1-interp.getInterpolation(percentOpen)));
			}			
		});
	}

	private Fragment mContent;

	private static Interpolator interp = new Interpolator() {
		@Override
		public float getInterpolation(float t) {
			t -= 1.0f;
			return t * t * t + 1.0f;
		}		
	};
	


	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// set the Above View
		if (savedInstanceState != null)
			mContent = getSupportFragmentManager().getFragment(
					savedInstanceState, "mContent");
		if (mContent == null)
			mContent = new AboveFragment("terkini");

		// set the Above View
		setContentView(R.layout.content_frame);
		getSupportFragmentManager().beginTransaction()
				.replace(R.id.content_frame, mContent).commit();

		// set the Behind View
		setBehindContentView(R.layout.behind_frame);
		getSupportFragmentManager().beginTransaction()
				.replace(R.id.menu_frame, new BehindFragment()).commit();

		// customize the SlidingMenu
		getSlidingMenu().setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
	}
	
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		getSupportFragmentManager().putFragment(outState, "mContent", mContent);
	}
	
	public void switchContent(Fragment fragment) {
		mContent = fragment;
		getSupportFragmentManager()
		.beginTransaction()
		.replace(R.id.content_frame, fragment)
		.commit();
		getSlidingMenu().showContent();
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if ((keyCode == KeyEvent.KEYCODE_BACK)) {
			LayoutInflater inflater = (LayoutInflater) getLayoutInflater();
			final View header = inflater.inflate(R.layout.dialog_customtitle, null);
			TextView tx = (TextView) header.findViewById(R.id.text_title);
			tx.setText("Exit ?");
			new AlertDialog.Builder(MainActivity.this)
			.setCustomTitle(header)
			.setMessage("Apakah anda yakin akan keluar?")
			.setPositiveButton("Ya",
					new DialogInterface.OnClickListener() {
						@TargetApi(11)
						public void onClick(DialogInterface dialog, int id) {
							finish();
						}
					})
			.setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
				@TargetApi(11)
				public void onClick(DialogInterface dialog, int id) {
					dialog.cancel();
				}
			}).show();
		}
		return super.onKeyDown(keyCode, event);
	}

}
