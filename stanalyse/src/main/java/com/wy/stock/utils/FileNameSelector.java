/**
 * 
 */
package com.wy.stock.utils;

import java.io.File;
import java.io.FilenameFilter;

/**
 * @author leslie
 *
 */
public class FileNameSelector implements FilenameFilter{
	 String prefix = "";
	 String post = "";
	 
	 public FileNameSelector(String fileNamePrefix, String fileNamePost)
	 {
		 prefix += fileNamePrefix;
		 post += fileNamePost;
	 }
	 
	public boolean accept(File dir, String name) {
		return name.startsWith(prefix) && name.endsWith(post);
	}

	public String getPrefix() {
		return prefix;
	}

	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}

	public String getPost() {
		return post;
	}

	public void setPost(String post) {
		this.post = post;
	}
	
}
