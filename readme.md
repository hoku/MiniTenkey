概要
==============
Android用のテンキー（numeric keypad）です。  
半透明なテンキーを表示します。

■■■

サンプルを動かした状態。  
↓

![alt text](https://raw.github.com/wiki/hoku/MiniTenkey/images/c1.png)

■■■

MiniTenkeyを使うと、こんな感じでテンキーが表示されます。  
画面がリサイズされない、背景が透けて見える、サクサク入力できる、という特徴があります。  
↓

![alt text](https://raw.github.com/wiki/hoku/MiniTenkey/images/c2.png)

■■■

見た目をカスタマイズできます。  
↓

![alt text](https://raw.github.com/wiki/hoku/MiniTenkey/images/c3.png)

- - -

使い方
==============

ファイル
----------
以下２ファイルを、自身のプロジェクトに追加してください。

* src/in/hoku/minitenkey/MiniTenkey.java
* res/layout/dialog_minitenkey.xml

呼び出し方
----------
これだけ。

    MiniTenkey tenkey = new MiniTenkey(this, new OnTenkeyEnteredListener() {
    	@Override
    	public void onEntered(String raw, double dblVal) {
    		// 入力された値が渡ってくる。
    		// あとは煮るなり焼くなり。
    		TextView tv = (TextView) findViewById(textViewResId);
    		tv.setText(raw);
    	}
    });
    tenkey.show();

まぁ、

* src/in/hoku/minitenkey/DemoActivity.java

を見ればきっと分かります。たぶん。  
詳しい説明は後で追記します。   
とりあえず今はソースを動かしてみて！

- - -

ライセンス
==============
本ソフトはMIT Licenseです。
