package com.cloud.provider.api.model;

import javax.annotation.Generated;


import org.springframework.data.aerospike.mapping.Document;
import org.springframework.data.aerospike.mapping.Field;
import org.springframework.data.annotation.Id;

import com.aerospike.client.Key;

@Document
public class Server {

	@Id
	private String key;


		@Field
	    private String name;
		@Field
	    private int ram; 
	    @Field
	    private int freeMemory;
	    
	    
	    
	    
	    public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
	
		public int getFreeMemory() {
			return freeMemory;
		}
		public void setFreeMemory(int freeMemory) {
			this.freeMemory = freeMemory;
		}
	
		public int getRam() {
			return ram;
		}
		public void setRam(int ram) {
			this.ram = ram;
		}
		public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
		

	
	
	
	
}
