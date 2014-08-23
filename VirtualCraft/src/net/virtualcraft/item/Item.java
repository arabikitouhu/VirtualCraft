package net.virtualcraft.item;

public class Item {

	protected int intID;
	/** getter(識別ID) */
	public int getItemID() { return intID; }

	protected String strDictionaryRegisteredName;
	/** getter(辞書登録名) */
	public String getDictionaryRegisteredName() { return null; }

	protected Item(Item source) {
		this.intID = source.getItemID();
		this.strDictionaryRegisteredName = source.getDictionaryRegisteredName();
	}

	public Item(int id) {
		this.intID = id;
		this.strDictionaryRegisteredName = "net.virtualcraft.item.noName";
	}

	public Item(int id, String dicRegistName) {
		this.intID = id;
		this.strDictionaryRegisteredName = dicRegistName;
	}

	/** 複製をする */
	public Item clone() { return new Item(this); }








}
