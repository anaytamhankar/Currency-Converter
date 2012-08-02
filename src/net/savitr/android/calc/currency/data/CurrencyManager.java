package net.savitr.android.calc.currency.data;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;

import net.savitr.android.calc.currency.R;
import net.savitr.android.calc.currency.webservice.WebCommunicator;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class CurrencyManager {

	// some constants
	private static final String TAG = "CurrencyManager";

	// static variables
	private static CurrencyManager instance = null;
	private static boolean mInitialized = false;

	// member variables
	private Hashtable<String, Currency> mCurrencyList = null;
	private long mTimeStamp = 0;

	public static CurrencyManager getInstance() {
		if (instance == null) {
			instance = new CurrencyManager();
		}
		return instance;
	}

	public static boolean initialize() {
		if (mInitialized) {
			return true;
		}

		final CurrencyManager manager = getInstance();

		// load currency list
		if (!manager.loadCurrencies()) {
			return false;
		}

		if (!manager.loadRates()) {
			return false;
		}

		manager.printCurrencyList();

		mInitialized = true;
		return mInitialized;
	}

	private void printCurrencyList() {
		Log.i(TAG, "Currencies Loaded");
		final Collection<Currency> list = mCurrencyList.values();
		for (Currency currency : list) {
			Log.i(TAG, currency.toString());
		}
	}

	public CurrencyManager() {
		mCurrencyList = new Hashtable<String, Currency>();
		
	}

	public long getTimeStamp() {
		return mTimeStamp;
	}

	public Currency getCurrency(String symbol) {
		return mCurrencyList.get(symbol);
	}

	public double convert(Currency from, Currency to, String amt) {
		try {
			Double amount = Double.parseDouble(amt);
			return (double)(to.getRate() / from.getRate()) * amount;
		}
		catch(Exception e) {
			Log.e(TAG, e.getMessage(), e);
			return -1;
		}
	}

	public List<Currency> getCurrencies() {
		final ArrayList<Currency> data = new ArrayList<Currency>(mCurrencyList.values());
		Collections.sort((List<Currency>) data);
		return data;
	}

	private boolean loadCurrencies() {
		final String data = WebCommunicator.loadCurrencyList();
		if (data != null) {
			return parseCurrencies(data);
		} else {
			return false;
		}
	}

	private boolean loadRates() {
		final String data = WebCommunicator.loadRates();
		if (data != null) {
			return parseRates(data);
		} else {
			return false;
		}
	}

	private boolean parseCurrencies(String data) {
		try {
			final JSONObject currencyList = new JSONObject(data);
			@SuppressWarnings("rawtypes")
			final Iterator iterator = currencyList.keys();

			while (iterator.hasNext()) {
				final String symbol = (String) iterator.next();
				if(getDrawable(symbol) != 0){
					final String name = currencyList.getString(symbol);
					final Currency currency = new Currency(symbol, name);
					mCurrencyList.put(symbol, currency);
				}
			}
			return true;
		} catch (JSONException e) {
			Log.e(TAG, e.getMessage(), e);
			return false;
		}
	}

	private boolean parseRates(String data) {
		try {
			final JSONObject rateList = new JSONObject(data);
			mTimeStamp = rateList.getLong("timestamp");
			final String baseSymbol = rateList.getString("base");
			final Currency baseCurrency = mCurrencyList.get(baseSymbol);
			if (baseCurrency != null) {
				baseCurrency.setBaseCurrency(true);
			}

			final JSONObject rateArray = rateList.getJSONObject("rates");
			@SuppressWarnings("rawtypes")
			final Iterator iterator = rateArray.keys();
			while (iterator.hasNext()) {
				final String symbol = (String) iterator.next();
				final double rate = rateArray.getDouble(symbol);
				final Currency currency = mCurrencyList.get(symbol);
				if (currency != null) {
					currency.setCurrencyRate(rate);
				}
			}
			return true;
		} catch (JSONException e) {
			Log.e(TAG, e.getMessage(), e);
			return false;
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
