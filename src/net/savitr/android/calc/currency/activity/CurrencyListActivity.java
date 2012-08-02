package net.savitr.android.calc.currency.activity;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.savitr.android.calc.currency.R;
import net.savitr.android.calc.currency.data.Currency;
import net.savitr.android.calc.currency.data.CurrencyManager;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class CurrencyListActivity extends ListActivity {

	public static final int RESULT_CODE_SUCCESS = 1;
	private static final String CURRENCY_USE_COUNT = "currencyUsesCounter";

	private List<Currency> mItems = null;
	private ArrayList<Currency> mCurrencyCount = null;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// this list is meant to store ONLY 5 elements initially
		mCurrencyCount = new ArrayList<Currency>(5);
		mItems = CurrencyManager.getInstance().getCurrencies();
		addStandardCurrencies();

		final CurrencyAdapter currencyAdapter = new CurrencyAdapter(this, 0,
				mItems);
		setListAdapter(currencyAdapter);

		final ListView lv = getListView();
		lv.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				final Currency currency = mItems.get(position);
				final Intent intent = new Intent();
				intent.putExtra("SELECTED_SYMBOL", currency.getCurrencySymbol());
				setResult(RESULT_CODE_SUCCESS, intent);
				finish();
			}
		});
	}

	private void addStandardCurrencies(){
		SharedPreferences ref = getSharedPreferences(CURRENCY_USE_COUNT, 0);		
		Iterator<Currency> iterator = mItems.listIterator();
		int temp = 0;
		int first=0,second=0,third=0,fourth=0,fifth=0;
		
		while (iterator.hasNext()) {
			Currency currency = iterator.next();
			String symbol = currency.getCurrencySymbol(); 
			if(ref.contains(symbol)){
				temp = ref.getInt(symbol, 0);
				if(temp > first){
					mCurrencyCount.add(0, currency);
					fifth= fourth;
					fourth=third;
					third = second;
					second=first;
					first = temp;
				}else if(temp>second){
					mCurrencyCount.add(1, currency);
					fifth= fourth;
					fourth=third;
					third = second;
					second = temp;
				}else if(temp > third){
					mCurrencyCount.add(2, currency);
					fifth= fourth;
					fourth=third;
					third = temp;
				}else if(temp>fourth){
					mCurrencyCount.add(3, currency);
					fifth= fourth;
					fourth = temp;
				}else if (temp>fifth) {
					mCurrencyCount.add(4, currency);
					fifth = temp;
				}		
			}
			//Keeps maximum size of mCurrencyCount as 5 
			if(mCurrencyCount.size()>5)
				mCurrencyCount.remove(5);
		}
		
		/*
		 * In mCurrencyCount 0th position is the most frequently used currency
		 * Therefore have to loop in reverse so that 0th position of mItems contains
		 * the most frequently used currency
		 */
		for(int i=mCurrencyCount.size() - 1;i >= 0;i--){
			mItems.add(0, mCurrencyCount.get(i));
		}
	}

	private class CurrencyAdapter extends ArrayAdapter<Currency> implements Filterable {
		private List<Currency> mItems = null;
		private Context mContext = null;

		public CurrencyAdapter(Context context, int textViewResourceId,
				List<Currency> objects) {
			super(context, textViewResourceId, objects);
			mItems = objects;
			mContext = context;
		}
		
		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView != null && convertView instanceof LinearLayout) {
				setView(convertView, position, parent);
				return convertView;
			} else {
				LayoutInflater vi = (LayoutInflater) mContext
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				convertView = vi.inflate(R.layout.currency_item, null);
				setView(convertView, position, parent);
				return convertView;
			}
		}

		private void setView(View convertView, int position, ViewGroup parent) {
			if(mItems.size() == 0) {
				return;
			}
			final Currency currency = mItems.get(position);
			if (currency != null) {
				final TextView name = (TextView) convertView
						.findViewById(R.id.currencyText);
				final int resourceId = getDrawable(currency.getCurrencySymbol());
				
				if (resourceId != 0) {
					final ImageView image = (ImageView) convertView.findViewById(R.id.currencyImage);
					image.setBackgroundDrawable(mContext.getResources()
							.getDrawable(resourceId));
				}
				name.setText(currency.getCurrencyName());
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

		public void notifyDataSetChanged() {
			super.notifyDataSetChanged();
		}
	}
}
