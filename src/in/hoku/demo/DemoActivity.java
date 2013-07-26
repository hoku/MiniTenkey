package in.hoku.demo;

import in.hoku.minitenkey.MiniTenkey;
import in.hoku.minitenkey.MiniTenkey.OnTenkeyEnteredListener;
import in.hoku.minitenkey.R;
import android.os.Bundle;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.util.SparseArray;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class DemoActivity extends Activity implements OnClickListener {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		findViewById(R.id.button0).setOnClickListener(this);
		findViewById(R.id.button1).setOnClickListener(this);
		findViewById(R.id.button2).setOnClickListener(this);
		findViewById(R.id.button3).setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.button0:
			tenkeyInput(R.id.textView0, -1);
			break;
		case R.id.button1:
			tenkeyInput(R.id.textView1, MiniTenkey.FLG_HIDE_MINUS);
			break;
		case R.id.button2:
			tenkeyInput(R.id.textView2, MiniTenkey.FLG_HIDE_DOT);
			break;
		case R.id.button3:
			tenkeyCustomInput(R.id.textView3, MiniTenkey.FLG_HIDE_MINUS | MiniTenkey.FLG_HIDE_DOT);
			break;
		}
	}

	public void tenkeyInput(final int textViewResId, int flg) {
		MiniTenkey ten = new MiniTenkey(this, new OnTenkeyEnteredListener() {
			@Override
			public void onEntered(String raw, double dblVal) {
				TextView tv = (TextView) findViewById(textViewResId);
				tv.setText(raw);
			}
		});
		if (flg > 0) {
			ten.setHideFlg(flg);
		}
		ten.show();
	}

	public void tenkeyCustomInput(final int textViewResId, int flg) {
		MiniTenkey ten = new MiniTenkey(this, new OnTenkeyEnteredListener() {
			@Override
			public void onEntered(String raw, double dblVal) {
				TextView tv = (TextView) findViewById(textViewResId);
				tv.setText(raw);
			}
		}) {
			@Override
			protected void setLabel(ViewGroup root, SparseArray<String> tenkeyValues) {
				super.setLabel(root, tenkeyValues);
				root.setBackgroundColor(Color.RED);
				for (int i = 0; i < tenkeyValues.size(); i++) {
					int color = (i % 2 == 0) ? Color.YELLOW : Color.GREEN;
					root.findViewById(tenkeyValues.keyAt(i)).setBackgroundColor(color);
				}
				BitmapDrawable drawable = createGradientDrawable();
				root.findViewById(R.id.tenkey_enter).setBackgroundDrawable(drawable);
			}
		};
		if (flg > 0) {
			ten.setHideFlg(flg);
		}
		ten.setGravity(Gravity.RIGHT);
		ten.show();
	}

	private BitmapDrawable createGradientDrawable() {
		Paint paint = new Paint();
		Rect rect = new Rect(0, 0, 10, 20);
		Shader shader = new LinearGradient(0, 0, 0, 20, Color.MAGENTA, Color.CYAN, Shader.TileMode.CLAMP);
		Bitmap bmp = Bitmap.createBitmap(10, 20, Config.ARGB_8888);
		Canvas cvs = new Canvas(bmp);
		paint.setShader(shader);
		cvs.drawRect(rect, paint);
		BitmapDrawable drawable = new BitmapDrawable(bmp);
		return drawable;
	}

}
