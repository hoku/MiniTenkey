/**
 * MiniTenkey v1.0.0
 * http://hoku.in/
 *
 * Copyright 2013 hoku.
 * Released under the MIT license
 */

package in.hoku.minitenkey;

import android.app.Dialog;
import android.content.Context;
import android.util.DisplayMetrics;
import android.util.SparseArray;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.TranslateAnimation;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MiniTenkey extends Dialog implements android.view.View.OnClickListener {
	public static interface OnTenkeyEnteredListener {
		public void onEntered(String raw, double dblVal);
	}

	public static final int FLG_HIDE_MINUS = 0x0001;
	public static final int FLG_HIDE_DOT = 0x0002;

	private static final SparseArray<String> TENKEY_VALUES = new SparseArray<String>() {
		{
			put(R.id.tenkey_0, "0");
			put(R.id.tenkey_1, "1");
			put(R.id.tenkey_2, "2");
			put(R.id.tenkey_3, "3");
			put(R.id.tenkey_4, "4");
			put(R.id.tenkey_5, "5");
			put(R.id.tenkey_6, "6");
			put(R.id.tenkey_7, "7");
			put(R.id.tenkey_8, "8");
			put(R.id.tenkey_9, "9");
			put(R.id.tenkey_minus, "-/+");
			put(R.id.tenkey_dot, ".");
			put(R.id.tenkey_backspace, "BS");
			put(R.id.tenkey_clear, "C");
			put(R.id.tenkey_enter, "E");
		}
	};

	private static final int FP = ViewGroup.LayoutParams.FILL_PARENT;

	private OnTenkeyEnteredListener listenter = null;

	private int maxLength = 16;

	private int tenkeyGravity = Gravity.BOTTOM;

	public MiniTenkey(Context c, OnTenkeyEnteredListener l) {
		super(c, android.R.style.Theme_Panel);
		setContentView(R.layout.dialog_minitenkey);
		getWindow().setLayout(FP, FP);

		this.listenter = l;

		for (int i = 0; i < TENKEY_VALUES.size(); i++) {
			findViewById(TENKEY_VALUES.keyAt(i)).setOnClickListener(this);
		}

		ViewGroup rectLayout = (ViewGroup) findViewById(R.id.tenkey_board);
		setLabel(rectLayout, TENKEY_VALUES);
	}

	public void setHideFlg(int flg) {
		if ((flg & FLG_HIDE_MINUS) == FLG_HIDE_MINUS) {
			findViewById(R.id.tenkey_minus).setVisibility(View.INVISIBLE);
		}
		if ((flg & FLG_HIDE_DOT) == FLG_HIDE_DOT) {
			findViewById(R.id.tenkey_dot).setVisibility(View.INVISIBLE);
		}
	}

	public void setGravity(int gravity) {
		this.tenkeyGravity = gravity;
	}

	public void setMaxlength(int length) {
		this.maxLength = length;
	}

	@Override
	public void show() {
		ViewGroup viewGroup = (ViewGroup) findViewById(R.id.tenkey_root);
		if (!(viewGroup instanceof LinearLayout)) {
			return;
		}

		LinearLayout rootLayout = (LinearLayout) viewGroup;

		DisplayMetrics matrics = getContext().getResources().getDisplayMetrics();
		int moveSize = Math.min(matrics.widthPixels, matrics.heightPixels) / 8;
		TranslateAnimation translate = new TranslateAnimation(0, 0, 0, 0);

		switch (this.tenkeyGravity) {
		case Gravity.LEFT:
			rootLayout.setGravity(this.tenkeyGravity | Gravity.CENTER_VERTICAL);
			translate = new TranslateAnimation(-moveSize, 0, 0, 0);
			break;
		case Gravity.RIGHT:
			rootLayout.setGravity(this.tenkeyGravity | Gravity.CENTER_VERTICAL);
			translate = new TranslateAnimation(moveSize, 0, 0, 0);
			break;
		case Gravity.TOP:
			rootLayout.setGravity(this.tenkeyGravity | Gravity.CENTER_HORIZONTAL);
			translate = new TranslateAnimation(0, 0, -moveSize, 0);
			break;
		case Gravity.BOTTOM:
			rootLayout.setGravity(this.tenkeyGravity | Gravity.CENTER_HORIZONTAL);
			translate = new TranslateAnimation(0, 0, moveSize, 0);
			break;
		}

		translate.setDuration(150); // ms
		findViewById(R.id.tenkey_board).startAnimation(translate);

		super.show();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tenkey_0:
		case R.id.tenkey_1:
		case R.id.tenkey_2:
		case R.id.tenkey_3:
		case R.id.tenkey_4:
		case R.id.tenkey_5:
		case R.id.tenkey_6:
		case R.id.tenkey_7:
		case R.id.tenkey_8:
		case R.id.tenkey_9:
		case R.id.tenkey_dot:
			addChara(TENKEY_VALUES.get(v.getId()));
			break;
		case R.id.tenkey_minus:
			revarseMinus();
			break;
		case R.id.tenkey_backspace:
			backSpace();
			break;
		case R.id.tenkey_clear:
			clear();
			break;
		case R.id.tenkey_enter:
			enter();
			break;
		}
	}

	protected void setLabel(ViewGroup root, SparseArray<String> tenkeyValues) {
		for (int i = 0; i < tenkeyValues.size(); i++) {
			root.findViewById(tenkeyValues.keyAt(i)).setOnClickListener(this);
			TextView textView = (TextView) root.findViewById(tenkeyValues.keyAt(i));
			textView.setText(tenkeyValues.valueAt(i));
		}
	}

	private void addChara(String c) {
		if (c == null) {
			return;
		}
		TextView text = (TextView) findViewById(R.id.tenkey_text);
		String nowString = text.getText().toString();
		if (isNumber(nowString + c) && nowString.length() < this.maxLength) {
			text.setText(nowString + c);
		}
	}

	private void revarseMinus() {
		TextView text = (TextView) findViewById(R.id.tenkey_text);
		String nowStr = text.getText().toString();
		if (nowStr.length() > 0 && nowStr.substring(0, 1).equals("-")) {
			text.setText(nowStr.substring(1));
		} else {
			text.setText("-" + nowStr);
		}
	}

	private void backSpace() {
		TextView text = (TextView) findViewById(R.id.tenkey_text);
		String nowStr = text.getText().toString();
		if (nowStr.length() == 0) {
			return;
		}
		String bsStr = nowStr.substring(0, nowStr.length() - 1);
		text.setText(bsStr);
	}

	private void clear() {
		TextView text = (TextView) findViewById(R.id.tenkey_text);
		text.setText("");
	}

	private void enter() {
		TextView text = (TextView) findViewById(R.id.tenkey_text);
		String raw = text.getText().toString();
		if (!isNumber(raw)) {
			return;
		}
		if (raw.substring(raw.length() - 1).equals(".")) {
			raw = raw + "0";
		}
		dismiss();
		if (this.listenter != null) {
			double dblVal = Double.parseDouble(raw);
			this.listenter.onEntered(raw, dblVal);
			this.listenter = null;
		}
	}

	private boolean isNumber(String str) {
		try {
			Double.parseDouble(str);
			return true;
		} catch (NumberFormatException e) {
			;
		}
		return false;
	}
}
