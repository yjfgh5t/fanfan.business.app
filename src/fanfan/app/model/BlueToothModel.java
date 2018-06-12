package fanfan.app.model;

import java.util.List;
import java.util.UUID;

public class BlueToothModel {
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	private  String name;
	
	private String address;
	
	private  List<UUID> uuids;

	public BlueToothModel() {}
	
	public BlueToothModel(String name,String address,List<UUID> uuids) {
		this.name=name;
		this.address=address;
		this.uuids = uuids;
	}
	
}
