package net.savitr.android.calc.currency.activity;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import net.savitr.android.calc.currency.R;
import net.savitr.android.calc.currency.data.Currency;
import net.savitr.android.calc.currency.data.CurrencyManager;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class CalculatorActivity extends Activity {
	private static final int REQUEST_CODE_SELECT_CURRENCY_FROM = 1;
	private static final int REQUEST_CODE_SELECT_CURRENCY_TO = 2;
	private static final String CURRENCY_USE_COUNT = "currencyUsesCounter";
	private static final String CURRENCY_STATES = "currencyState";

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.calc);

		final CurrencyManager currencyManager = CurrencyManager.getInstance();

		final View fromLayout = findViewById(R.id.fromLayout);
		fromLayout.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				final Intent intent = new Intent(CalculatorActivity.this,
						CurrencyListActivity.class);
				startActivityForResult(intent, REQUEST_CODE_SELECT_CURRENCY_FROM);
			}
		});

		final View toLayout = findViewById(R.id.toLayout);
		toLayout.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				final Intent intent = new Intent(CalculatorActivity.this,
						CurrencyListActivity.class);
				startActivityForResult(intent, REQUEST_CODE_SELECT_CURRENCY_TO);
			}
		});

		setDefaultCurrency();

		final TextView timestampView = (TextView) findViewById(R.id.timestamp);
		timestampView.setText(getFormattedTimestamp(currencyManager
				.getTimeStamp()));

		final EditText amount = (EditText) findViewById(R.id.amount);
		int width = amount.getWidth();
		amount.setMaxWidth(width);
		final EditText resultText = (EditText) findViewById(R.id.resultText);	
		width = amount.getWidth();
		resultText.setMaxWidth(width);

		this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

		amount.setOnTouchListener(new OnTouchListener() {

			public boolean onTouch(View v, MotionEvent event) {
				v.requestFocus();
				return true;
			}
		});
		
		resultText.setOnTouchListener(new OnTouchListener() {

			public boolean onTouch(View v, MotionEvent event) {
				v.requestFocus();
				return true;
			}
		});
		
				final LinearLayout buttons = (LinearLayout) findViewById(R.id.buttons);
		final Button convert = (Button) findViewById(R.id.convert);
		
		convert.setWidth((buttons.getWidth())/2);
		convert.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				final Transaction transaction = new Transaction();
				if (!transaction.validateTransaction()) {
					Toast.makeText(CalculatorActivity.this,
							R.string.validation_error, Toast.LENGTH_SHORT)
							.show();
				} else {
					final String result = transaction.result();
					if (result == null) {
						resultText.setText(R.string.conversion_error);
						resultText.setTextColor(getResources().getColor(
								R.color.red));
					} else {
						resultText.setText(result);
						resultText.setTextColor(getResources().getColor(
								R.color.purple));
					}
				}
			}
		});
		
		final Button reset = (Button) findViewById(R.id.reset);
		reset.setWidth((buttons.getWidth())/2);
		reset.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				amount.setText("");
				resultText.setText("");
				setDefaultCurrency();
			}
		});

		ImageView swap = (ImageView) findViewById(R.id.swap);
		swap.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				final TextView fromText = (TextView) findViewById(R.id.fromText);
				final TextView toText = (TextView) findViewById(R.id.toText);
				final ImageView fromImage = (ImageView) findViewById(R.id.fromImage);
				final ImageView toImage = (ImageView) findViewById(R.id.toImage);
				
				String tempCurrency = fromText.getText().toString();
				fromText.setText(toText.getText().toString());
				toText.setText(tempCurrency);
				
				fromImage.setImageDrawable(getResources().getDrawable(getDrawable(fromText.getText().toString())));
				toImage.setImageDrawable(getResources().getDrawable(getDrawable(tempCurrency)));
				
				convert.performClick();
			}
		});		

		final GridView keyboarGrid = (GridView) findViewById(R.id.keyboardGrid);
		String keys[] = {"1","2","3","4","5","6","7","8","9",".","0","Cl"};
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,R.layout.keyboarditem,R.id.keyboardKey,keys);
		keyboarGrid.setAdapter(adapter);
		
		keyboarGrid.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> adapterView, View view, int index,long arg3) {
				String str = (String) adapterView.getItemAtPosition(index);
				
				resultText.setText("");
				
				EditText tempEdit;
				if(amount.isFocused()){
					tempEdit = amount;
				}else{
					tempEdit = resultText;
				}
				String temp = tempEdit.getText().toString();
				if(str.equalsIgnoreCase("Cl")){
					if(temp.length()!=0)
						tempEdit.setText(temp.substring(0, temp.length()-1));
				}else if(str.equalsIgnoreCase(".")){
					if(!temp.contains("."))
						tempEdit.setText(temp+str);
				}else
					tempEdit.setText(temp+str);				
				tempEdit.setSelection(tempEdit.getText().length());//sets cursor to end of text
			}
		});

		keyboarGrid.setOnItemLongClickListener(new OnItemLongClickListener() {

			public boolean onItemLongClick(AdapterView<?> adapterView, View view,
					int index, long arg3) {
				String str = (String) adapterView.getItemAtPosition(index);			
				if(str.equalsIgnoreCase("Cl")){
					amount.setText("");
					resultText.setText("");
				}
				return false;
			}
		});
		
		keyboarGrid.setOnTouchListener(new OnTouchListener() {
			
			public boolean onTouch(View v, MotionEvent event) {
				if(event.getAction() == MotionEvent.ACTION_MOVE)
					return true;
				return false;
			}
		});
	}

	private CharSequence getFormattedTimestamp(long timeStamp) {
		final SimpleDateFormat format = new SimpleDateFormat("KK:mm aa");
		format.setTimeZone(Calendar.getInstance().getTimeZone());
		return getResources().getString(R.string.timestamp) + " "
		+ format.format(new Date(timeStamp * 1000));
	}

	
	private void setDefaultCurrency() {
		SharedPreferences ref = getSharedPreferences(CURRENCY_STATES, 0);
		String fromCurrency = ref.getString("fromCurrency", "USD");
		String toCurrency = ref.getString("toCurrency", "USD");

		final ImageView fromImage = (ImageView) findViewById(R.id.fromImage);
		if(getDrawable(fromCurrency) != 0)
			fromImage.setImageDrawable(getResources().getDrawable(getDrawable(fromCurrency)));
		else
			fromImage.setImageDrawable(getResources().getDrawable(R.drawable.empty));
		final TextView fromText = (TextView) findViewById(R.id.fromText);
		fromText.setText(CurrencyManager.getInstance().getCurrency(fromCurrency)
				.getCurrencySymbol());

		final ImageView toImage = (ImageView) findViewById(R.id.toImage);
		if(getDrawable(toCurrency) != 0)
			toImage.setImageDrawable(getResources().getDrawable(getDrawable(toCurrency)));
		else
			toImage.setImageDrawable(getResources().getDrawable(R.drawable.empty));
		final TextView toText = (TextView) findViewById(R.id.toText);
		toText.setText(CurrencyManager.getInstance().getCurrency(toCurrency)
				.getCurrencySymbol());
	}

	private class Transaction {
		public String mAmount = null;
		private EditText amount = (EditText) findViewById(R.id.amount);
		private TextView from = (TextView) findViewById(R.id.fromText);
		private TextView to =(TextView) findViewById(R.id.toText);
		private DecimalFormat mFormat = new DecimalFormat("##.##");

		public String result() {
			final CurrencyManager manager = CurrencyManager.getInstance();
			final Currency fromCurrency = manager.getCurrency(from.getText().toString());
			final Currency toCurrency = manager.getCurrency(to.getText().toString());
			final Double result = manager.convert(fromCurrency, toCurrency,
					mAmount);
			if (result == -1) {
				return null;
			} else {
				return mFormat.format(result);
			}
		}

		public boolean validateTransaction() {
			boolean retVal = true;
			retVal = retVal && validateAmount(amount.getText());
			return retVal;
		}

		private boolean validateAmount(Editable text) {
			if (text != null && text.toString() != null
					&& text.toString().trim().length() > 0) {
				mAmount = text.toString().trim();
				return true;
			}
			return false;
		}
	}


	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		SharedPreferences ref = getSharedPreferences(CURRENCY_USE_COUNT, 0);		
		SharedPreferences.Editor editor = ref.edit();

		SharedPreferences state = getSharedPreferences(CURRENCY_STATES, 0);
		SharedPreferences.Editor stateEditor = state.edit();

		if(requestCode == REQUEST_CODE_SELECT_CURRENCY_FROM && resultCode == CurrencyListActivity.RESULT_CODE_SUCCESS) {
			final String currencySymbol = data.getExtras().getString("SELECTED_SYMBOL");

			stateEditor.putString("fromCurrency", currencySymbol);

			int co = ref.getInt(currencySymbol, 0);
			editor.putInt(currencySymbol,++co);

			final ImageView fromImage = (ImageView) findViewById(R.id.fromImage);
			/**
			 * If no image is returned
			 */
			if(getDrawable(currencySymbol) != 0)
				fromImage.setImageDrawable(getResources().getDrawable(getDrawable(currencySymbol)));
			else{
				fromImage.setImageDrawable(getResources().getDrawable(R.drawable.empty));
			}

			final TextView fromText = (TextView) findViewById(R.id.fromText);
			fromText.setText(currencySymbol);
		}
		else if(requestCode == REQUEST_CODE_SELECT_CURRENCY_TO && resultCode == CurrencyListActivity.RESULT_CODE_SUCCESS) {
			final String currencySymbol = data.getExtras().getString("SELECTED_SYMBOL");

			stateEditor.putString("toCurrency", currencySymbol);

			int co = ref.getInt(currencySymbol, 0);
			editor.putInt(currencySymbol,++co);

			final ImageView toImage = (ImageView) findViewById(R.id.toImage);
			if(getDrawable(currencySymbol)!=0){
				int tempId = getDrawable(currencySymbol);
				toImage.setImageDrawable(getResources().getDrawable(tempId));
			}else{
				Log.e("Coutry Flag not found",currencySymbol);
				toImage.setImageDrawable(getResources().getDrawable(R.drawable.empty));
			}
			final TextView toText = (TextView) findViewById(R.id.toText);
			toText.setText(currencySymbol);
		}
		// Commit the flag count
		editor.commit();
		stateEditor.commit();

		if(((EditText)findViewById(R.id.amount)).getText().length() != 0){
			((Button)findViewById(R.id.convert)).performClick();
		}
	}

	private int getDrawable(String symbol) {
		if (symbol.equalsIgnoreCase("aed")) {
			return R.drawable.aed;
		} else if (symbol.equalsIgnoreCase("all")) {
			return R.drawable.all;
		} else if (symbol.equalsIgnoreCase("ars")) {
			return R.drawable.ars;
		} else if (symbol.equalsIgnoreCase("aud")) {
			return R.drawable.aud;
		} else if (symbol.equalsIgnoreCase("awg")) {
			return R.drawable.awg;
		} else if (symbol.equalsIgnoreCase("bbd")) {
			return R.drawable.bbd;
		} else if (symbol.equalsIgnoreCase("bdt")) {
			return R.drawable.bdt;
		} else if (symbol.equalsIgnoreCase("bgn")) {
			return R.drawable.bgn;
		} else if (symbol.equalsIgnoreCase("bhd")) {
			return R.drawable.bhd;
		} else if (symbol.equalsIgnoreCase("bif")) {
			return R.drawable.bif;
		} else if (symbol.equalsIgnoreCase("bmd")) {
			return R.drawable.bmd;
		} else if (symbol.equalsIgnoreCase("bnd")) {
			return R.drawable.bnd;
		} else if (symbol.equalsIgnoreCase("bob")) {
			return R.drawable.bob;
		} else if (symbol.equalsIgnoreCase("brl")) {
			return R.drawable.brl;
		} else if (symbol.equalsIgnoreCase("bsd")) {
			return R.drawable.bsd;
		} else if (symbol.equalsIgnoreCase("btn")) {
			return R.drawable.btn;
		} else if (symbol.equalsIgnoreCase("byr")) {
			return R.drawable.byr;
		} else if (symbol.equalsIgnoreCase("bzd")) {
			return R.drawable.bzd;
		} else if (symbol.equalsIgnoreCase("cad")) {
			return R.drawable.cad;
		} else if (symbol.equalsIgnoreCase("chf")) {
			return R.drawable.chf;
		} else if (symbol.equalsIgnoreCase("cis")) {
			return R.drawable.cis;
		} else if (symbol.equalsIgnoreCase("clp")) {
			return R.drawable.clp;
		} else if (symbol.equalsIgnoreCase("cny")) {
			return R.drawable.cny;
		} else if (symbol.equalsIgnoreCase("cop")) {
			return R.drawable.cop;
		} else if (symbol.equalsIgnoreCase("crc")) {
			return R.drawable.crc;
		} else if (symbol.equalsIgnoreCase("czk")) {
			return R.drawable.czk;
		} else if (symbol.equalsIgnoreCase("dkk")) {
			return R.drawable.dkk;
		} else if (symbol.equalsIgnoreCase("dop")) {
			return R.drawable.dop;
		} else if (symbol.equalsIgnoreCase("dzd")) {
			return R.drawable.dzd;
		} else if (symbol.equalsIgnoreCase("eek")) {
			return R.drawable.eek;
		} else if (symbol.equalsIgnoreCase("egp")) {
			return R.drawable.egp;
		} else if (symbol.equalsIgnoreCase("etb")) {
			return R.drawable.etb;
		} else if (symbol.equalsIgnoreCase("eur")) {
			return R.drawable.eur;
		} else if (symbol.equalsIgnoreCase("fjd")) {
			return R.drawable.fjd;
		} else if (symbol.equalsIgnoreCase("gbp")) {
			return R.drawable.gbp;
		} else if (symbol.equalsIgnoreCase("gmd")) {
			return R.drawable.gmd;
		} else if (symbol.equalsIgnoreCase("gnf")) {
			return R.drawable.gnf;
		} else if (symbol.equalsIgnoreCase("gtq")) {
			return R.drawable.gtq;
		} else if (symbol.equalsIgnoreCase("hkd")) {
			return R.drawable.hkd;
		} else if (symbol.equalsIgnoreCase("hnl")) {
			return R.drawable.hnl;
		} else if (symbol.equalsIgnoreCase("hrk")) {
			return R.drawable.hrk;
		} else if (symbol.equalsIgnoreCase("htg")) {
			return R.drawable.htg;
		} else if (symbol.equalsIgnoreCase("huf")) {
			return R.drawable.huf;
		} else if (symbol.equalsIgnoreCase("idr")) {
			return R.drawable.idr;
		} else if (symbol.equalsIgnoreCase("ils")) {
			return R.drawable.ils;
		} else if (symbol.equalsIgnoreCase("inr")) {
			return R.drawable.inr;
		} else if (symbol.equalsIgnoreCase("iqd")) {
			return R.drawable.iqd;
		} else if (symbol.equalsIgnoreCase("irr")) {
			return R.drawable.irr;
		} else if (symbol.equalsIgnoreCase("isk")) {
			return R.drawable.isk;
		} else if (symbol.equalsIgnoreCase("jmd")) {
			return R.drawable.jmd;
		} else if (symbol.equalsIgnoreCase("jod")) {
			return R.drawable.jod;
		} else if (symbol.equalsIgnoreCase("jpy")) {
			return R.drawable.jpy;
		} else if (symbol.equalsIgnoreCase("kes")) {
			return R.drawable.kes;
		} else if (symbol.equalsIgnoreCase("kmf")) {
			return R.drawable.kmf;
		} else if (symbol.equalsIgnoreCase("krw")) {
			return R.drawable.krw;
		} else if (symbol.equalsIgnoreCase("kwd")) {
			return R.drawable.kwd;
		} else if (symbol.equalsIgnoreCase("kyd")) {
			return R.drawable.kyd;
		} else if (symbol.equalsIgnoreCase("kzt")) {
			return R.drawable.kzt;
		} else if (symbol.equalsIgnoreCase("lbp")) {
			return R.drawable.lbp;
		} else if (symbol.equalsIgnoreCase("lkr")) {
			return R.drawable.lkr;
		} else if (symbol.equalsIgnoreCase("lsl")) {
			return R.drawable.lsl;
		} else if (symbol.equalsIgnoreCase("ltl")) {
			return R.drawable.ltl;
		} else if (symbol.equalsIgnoreCase("lvl")) {
			return R.drawable.lvl;
		} else if (symbol.equalsIgnoreCase("mad")) {
			return R.drawable.mad;
		} else if (symbol.equalsIgnoreCase("mdl")) {
			return R.drawable.mdl;
		} else if (symbol.equalsIgnoreCase("mkd")) {
			return R.drawable.mkd;
		} else if (symbol.equalsIgnoreCase("mnt")) {
			return R.drawable.mnt;
		} else if (symbol.equalsIgnoreCase("mop")) {
			return R.drawable.mop;
		} else if (symbol.equalsIgnoreCase("mro")) {
			return R.drawable.mro;
		} else if (symbol.equalsIgnoreCase("mur")) {
			return R.drawable.mur;
		} else if (symbol.equalsIgnoreCase("mvr")) {
			return R.drawable.mvr;
		} else if (symbol.equalsIgnoreCase("mwk")) {
			return R.drawable.mwk;
		} else if (symbol.equalsIgnoreCase("mxn")) {
			return R.drawable.mxn;
		} else if (symbol.equalsIgnoreCase("myr")) {
			return R.drawable.myr;
		} else if (symbol.equalsIgnoreCase("nad")) {
			return R.drawable.nad;
		} else if (symbol.equalsIgnoreCase("ngn")) {
			return R.drawable.ngn;
		} else if (symbol.equalsIgnoreCase("nio")) {
			return R.drawable.nio;
		} else if (symbol.equalsIgnoreCase("nok")) {
			return R.drawable.nok;
		} else if (symbol.equalsIgnoreCase("npr")) {
			return R.drawable.npr;
		} else if (symbol.equalsIgnoreCase("nzd")) {
			return R.drawable.nzd;
		} else if (symbol.equalsIgnoreCase("omr")) {
			return R.drawable.omr;
		} else if (symbol.equalsIgnoreCase("pab")) {
			return R.drawable.pab;
		} else if (symbol.equalsIgnoreCase("pen")) {
			return R.drawable.pen;
		} else if (symbol.equalsIgnoreCase("pgk")) {
			return R.drawable.pgk;
		} else if (symbol.equalsIgnoreCase("php")) {
			return R.drawable.php;
		} else if (symbol.equalsIgnoreCase("pkr")) {
			return R.drawable.pkr;
		} else if (symbol.equalsIgnoreCase("pln")) {
			return R.drawable.pln;
		} else if (symbol.equalsIgnoreCase("pyg")) {
			return R.drawable.pyg;
		} else if (symbol.equalsIgnoreCase("qar")) {
			return R.drawable.qar;
		} else if (symbol.equalsIgnoreCase("ron")) {
			return R.drawable.ron;
		} else if (symbol.equalsIgnoreCase("rub")) {
			return R.drawable.rub;
		} else if (symbol.equalsIgnoreCase("rwf")) {
			return R.drawable.rwf;
		} else if (symbol.equalsIgnoreCase("sar")) {
			return R.drawable.sar;
		} else if (symbol.equalsIgnoreCase("sbd")) {
			return R.drawable.sbd;
		} else if (symbol.equalsIgnoreCase("scr")) {
			return R.drawable.scr;
		} else if (symbol.equalsIgnoreCase("sek")) {
			return R.drawable.sek;
		} else if (symbol.equalsIgnoreCase("sgd")) {
			return R.drawable.sgd;
		} else if (symbol.equalsIgnoreCase("skk")) {
			return R.drawable.skk;
		} else if (symbol.equalsIgnoreCase("sll")) {
			return R.drawable.sll;
		} else if (symbol.equalsIgnoreCase("svc")) {
			return R.drawable.svc;
		} else if (symbol.equalsIgnoreCase("szl")) {
			return R.drawable.szl;
		} else if (symbol.equalsIgnoreCase("thb")) {
			return R.drawable.thb;
		} else if (symbol.equalsIgnoreCase("tnd")) {
			return R.drawable.tnd;
		} else if (symbol.equalsIgnoreCase("top")) {
			return R.drawable.top;
		} else if (symbol.startsWith("try") || symbol.startsWith("TRY")) {
			return R.drawable.trys;
		} else if (symbol.equalsIgnoreCase("ttd")) {
			return R.drawable.ttd;
		} else if (symbol.equalsIgnoreCase("twd")) {
			return R.drawable.twd;
		} else if (symbol.equalsIgnoreCase("tzs")) {
			return R.drawable.tzs;
		} else if (symbol.equalsIgnoreCase("uah")) {
			return R.drawable.uah;
		} else if (symbol.equalsIgnoreCase("ugx")) {
			return R.drawable.ugx;
		} else if (symbol.equalsIgnoreCase("usd")) {
			return R.drawable.usd;
		} else if (symbol.equalsIgnoreCase("uyu")) {
			return R.drawable.uyu;
		} else if (symbol.equalsIgnoreCase("vuv")) {
			return R.drawable.vuv;
		} else if (symbol.equalsIgnoreCase("wst")) {
			return R.drawable.wst;
		} else if (symbol.equalsIgnoreCase("xaf")) {
			return R.drawable.xaf;
		} else if (symbol.equalsIgnoreCase("yer")) {
			return R.drawable.yer;
		} else if (symbol.equalsIgnoreCase("zar")) {
			return R.drawable.zar;
		} else if (symbol.equalsIgnoreCase("zmk")) {
			return R.drawable.zmk;
		}
		return 0;
	}
}
