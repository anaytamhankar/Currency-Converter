package net.savitr.android.calc.currency.data;

public class Currency implements Comparable<Currency>{

	private String mCurrencySymbol = null;
	private String mCurrencyName = null;
	private double mRate = 0.0f;
	private boolean mBaseCurrency = false;

	public Currency() {
	}

	public Currency(String symbol, String name) {
		mCurrencySymbol = symbol;
		mCurrencyName = name;
	}

	public void setCurrencySymbol(String symbol) {
		mCurrencySymbol = symbol;
	}

	public void setCurrencyName(String name) {
		mCurrencyName = name;
	}

	public void setCurrencyRate(double rate) {
		mRate = rate;
	}

	public void setBaseCurrency(boolean flag) {
		mBaseCurrency = flag;
	}

	public String getCurrencySymbol() {
		return mCurrencySymbol;
	}

	public String getCurrencyName() {
		return mCurrencyName;
	}

	public double getRate() {
		return mRate;
	}

	public boolean isBaseCurrency() {
		return mBaseCurrency;
	}

	public String toString() {
		return mCurrencyName;
	}

	public int compareTo(Currency another) {
		return this.mCurrencyName.compareToIgnoreCase(another.mCurrencyName);
	}

}
