package ru.t022.c2h5oh;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.PowerManager;
import android.preference.PreferenceManager;
import android.view.ContextMenu;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity
{
	// это будет именем файла настроек
	public static final String APP_PREFERENCES = "C2H5OH.conf";
	private SharedPreferences mSettings;
	public static final String APP_PREFERENCES_V0 = "V0";
	public static final String APP_PREFERENCES_N0 = "N0";
	public static final String APP_PREFERENCES_V1 = "V1";
	public static final String APP_PREFERENCES_N1 = "N1";

	TextView text20C;
	Button btnRes;
	TextView txtV;
	TextView txtV1;

	EditText txtV0;
	EditText txtN0;
	EditText txtN1;
	EditText etxtV1;
	
	SeekBar sBarV0;

	int N0, V0, N1;
	int V1; // полученный раствор
	int X; // вода

	int defaultColor;

	PowerManager pm;
	PowerManager.WakeLock wl;

	@SuppressLint("InvalidWakeLockTag")
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
		wl = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK | PowerManager.ON_AFTER_RELEASE,"Screen OFF");

		setContentView(R.layout.activity_fullscreen);
		text20C = findViewById(R.id.textView20C);
		btnRes = findViewById(R.id.button1);
		txtV = findViewById(R.id.textViewV);
		txtV1 = findViewById(R.id.textViewV1);
		txtV0 = findViewById(R.id.editTextV0);
		txtN0 = findViewById(R.id.editTextN0);
		txtN1 = findViewById(R.id.editTextN1);
		etxtV1 = findViewById(R.id.editTextV1);
		
		sBarV0 = findViewById(R.id.seekBarV0);
		sBarV0.setEnabled(false);
		sBarV0.setVisibility(View.INVISIBLE);

		mSettings = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
		onRead();
		defaultColor=txtV0.getCurrentTextColor();
		registerForContextMenu(text20C);

		@SuppressLint("DefaultLocale")
		OnClickListener oclBtnRes = v -> {
			// X = 100NP/M — 100P
			// Где N0 – начальная крепость спирта;
			// V0 — объем изначального спирта в миллилитрах ;
			// N1 – крепость конечного (требуемого раствора);
			// X — количество мл воды, которые следует добавить к изначальному раствору.
			try
			{
				V0 = Integer.parseInt(txtV0.getText().toString());
				if(V0>10000) {V0 = 10000; txtV0.setText(String.valueOf(V0));}

				if(etxtV1.isFocused())
				{
					V1 = Integer.parseInt(etxtV1.getText().toString());
					if(V1>20000) {V1 = 20000; etxtV1.setText(String.valueOf(V1));}
				}
				N0 = Integer.parseInt(txtN0.getText().toString());
				N1 = Integer.parseInt(txtN1.getText().toString());
				if ((N0 < 35) || (N0 > 95))
				{
					showToast(v, "КРЕПОСТЬ НАЧАЛЬНОГО ВНЕ ДИАПАЗОНА");
					return;
				}
				else
					if (N1 > N0)
					{
						showToast(v, "КРЕПОСТЬ НАЧАЛЬНОГО ДОЛЖНА бЫТЬ ВЫШЕ КОНЕЧНОГО");
						return;
					}
			}
			catch (Exception e)
			{
				showToast(v, "НЕЧИСЛОВЫЕ ПАРАМЕТРЫ");
				return;
			}

			try
			{
				if(etxtV1.isFocused())
				{
					X = Fertman.V0(V1, N0, N1);
					V1 = Fertman.V1;
					txtV0.setText(String.valueOf(Fertman.V0));
					txtV0.setTextColor(Color.GREEN);
				}
				else
				{
					X = Fertman.V1(V0, N0, N1);
					V1 = Fertman.V1;
					txtV0.setTextColor(defaultColor);
				}

				txtV.setText(String.format("%d мл", V1));
				if(X!=-1)
				{
					txtV1.setText(String.format("%d мл", X));
				}
				else
				{
					txtV1.setText("нет в\nтаблице");
				}

			}
			catch (ArithmeticException e)
			{
				showToast(v, "ВЫХОД ИЗ ДИАПАЗОНА");
				return;
			}
			catch (ArrayIndexOutOfBoundsException e)
			{
				showToast(v, "ВЫХОД ЗA ТАБЛИЦУ ФЕРТМАНА");
				return;
			}
			catch (Exception e)
			{
				showToast(v, "КАКОЕ-ТО ИСКЛЮЧЕНИЕ");
				return;
			}
			sBarV0.setProgress(V0);
			onSave();
		};
		btnRes.setOnClickListener(oclBtnRes);

		sBarV0.setOnSeekBarChangeListener( new OnSeekBarChangeListener()
		{
			@Override
        	public void onProgressChanged(SeekBar seekBar, int progresValue, boolean fromUser)
			{
				txtV0.setText(String.valueOf(progresValue));
				btnRes.callOnClick();
			}
			@Override
			public void onStartTrackingTouch(SeekBar arg0)
			{
				// TODO Автоматически созданная заглушка метода
				
			}
			@Override
			public void onStopTrackingTouch(SeekBar arg0)
			{
			}
		});
	}

	public void showToast(View v, String s)
	{
		//создаем и отображаем текстовое уведомление
		Toast toast = Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG);
		toast.setGravity(Gravity.CENTER, 0, 0);
		toast.show();

		txtV.setText("");
		txtV1.setText("");
	}

	protected void onSave()
	{
		// Запоминаем данные
		SharedPreferences.Editor editor = mSettings.edit();
		editor.putInt(APP_PREFERENCES_N0, N0);
		editor.putInt(APP_PREFERENCES_V0, V0);
		editor.putInt(APP_PREFERENCES_N1, N1);
		editor.putInt(APP_PREFERENCES_V1, V1);
		editor.apply();
	}

	protected void onRead()
	{
		// Получаем число из настроек
		V0 = mSettings.getInt(APP_PREFERENCES_V0, 1000);
		N0 = mSettings.getInt(APP_PREFERENCES_N0, 70);
		N1 = mSettings.getInt(APP_PREFERENCES_N1, 40);
		V1 = mSettings.getInt(APP_PREFERENCES_V1, 1750);
		// Выводим на экран данные из настроек
		txtV0.setText(String.valueOf(V0));
		txtN0.setText(String.valueOf(N0));
		txtN1.setText(String.valueOf(N1));
		etxtV1.setText(String.valueOf(V1));
		
		sBarV0.setProgress(V0);
	}
	public void ShowAbout()
	{
		//Toast.makeText(this, "© Константин Тагиев\nt022@mail.ru", Toast.LENGTH_LONG).show();
		Dialog dialog = new Dialog(MainActivity.this);

		// Установите заголовок
		dialog.setTitle("О программе");
		// Передайте ссылку на разметку
		dialog.setContentView(R.layout.about);
		// Найдите элемент TextView внутри вашей разметки
		// и установите ему соответствующий текст
		TextView text = (TextView) dialog.findViewById(R.id.textAbout);
		text.setText("© Константин Тагиев\\nt022@mail.ru\\nВерсия 2023");
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
									   ContextMenu.ContextMenuInfo menuInfo)
	{
		MenuItem mi = menu.add(0, 2, 0, "Настройки");
		Intent intent = new Intent(this, PrefActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
		mi.setIntent(intent);
		menu.add(0, 1, 0, "О программе");
	}
	@Override
	public boolean onContextItemSelected(MenuItem item)
	{
		int id = item.getItemId();

		// Операции для выбранного пункта меню
		switch (id)
		{
			case 1:
				ShowAbout();
				return true;
			case 2:
				//
				break;
		}
		return super.onContextItemSelected(item);
	}

	@Override
	protected void onStart()
	{
		super.onStart();
	}
	@Override
	protected void onResume()
	{
		super.onResume();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this); 
	        // читаем установленное значение из CheckBoxPreference

	    if (prefs.getBoolean(getString(R.string.sleep), false))
	    {// Уходить в сон
			if (wl.isHeld())
				wl.release();
	    	Toast.makeText(this, "Уходить в сон", Toast.LENGTH_SHORT).show();
	    }
	    else
	    {
	    	// не уходить в сон
			if (wl.isHeld())
				wl.acquire();
	    	Toast.makeText(this, "НЕ Уходить в сон", Toast.LENGTH_SHORT).show();
	    }

	}

	@Override
	protected void onPause()
	{
		super.onPause();
	}

	@Override
	protected void onStop()
	{
		super.onStop();
	}

	@Override
	protected void onDestroy()
	{
		super.onDestroy();
	}

}
