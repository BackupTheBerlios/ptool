/*
 * Created on 31.05.2004
 * by Enrico Tröger
 */
package de.partysoke.psagent;

public class ConfigException extends Exception {
	
	String text;
	
	public ConfigException(String txt) {
		this.text = txt;
	}
	
	public String getText() {
		return text;
	}
}
