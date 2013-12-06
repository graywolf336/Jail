package com.graywolf336.jail;

public class JailIO {
	private JailMain pl;
	private int storage; //0 = flatfile, 1 = sqlite, 2 = mysql
	
	public JailIO(JailMain plugin) {
		this.pl = plugin;
		
		String st = pl.getConfig().getString("storage.type", "flatfile");
		if(st.equalsIgnoreCase("sqlite")) {
			storage = 1;
		}else if(st.equalsIgnoreCase("mysql")) {
			storage = 2;
		}else {
			storage = 0;
		}
	}
	
	public void PrepareStorage() {
		switch(storage) {
			case 1:
				//prepare sqlite, I need to research this
				break;
			case 2:
				//prepare mysql, research this as well
				break;
			default:
				//File system, prepare it somehow.. Maybe load in the files
				break;
		}
	}
}
