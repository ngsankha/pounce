/*
 *  Pounce allows you to manage quick lists of text snippets from the command line
 *  Copyright (C) 2012  Sankha Narayan Guria
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */

package sngforge.pounce;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Properties;

public class Pounce {
	
	File home;
	
	public static void main(String args[]){
		Pounce p=new Pounce();
		p.init();
		if(args.length==0)
			p.showHelp();
	}
	
	void showHelp(){
		System.out.println("Pounce usage: pounce <command> <parameters>");
		System.out.println("Commands can be one of the following");
		System.out.println("new <listname> : Create a new list");
		System.out.println("add <listname> <item> <value> : Add <item> containing <value> to the list <listname>");
		System.out.println("show <listname> <item> : Print the value of a item to the standard item. If <item> is not specified the full list will be printed.");
		System.out.println("get <listname> <item> : Copy the value of the item to the system clipboard");
		System.out.println("delete <listname> <item> : Delete <item> from <listname> list. If <item> is not specified the full list will be deleted.");
		System.out.println("list : List all the lists");
		System.out.println("help : Show this message");
		System.out.println("about : Information about Panther");
	}
	
	void init(){
		home=new File(System.getProperty("user.home")+"/.pounce");
		if(!home.exists())
			home.mkdirs();
	}
	
	void newList(String listname)throws Exception{
		Properties p=new Properties();
		FileOutputStream fout=new FileOutputStream(System.getProperty("user.home")+"/.pounce/"+listname);
		p.storeToXML(fout, "Pounce List");
		System.out.println("Created that list for you!!");
	}
	
	void add(String listname, String name, String val) throws Exception{
		Properties p=new Properties();
		if(!new File(System.getProperty("user.home")+"/.pounce/"+listname).exists()){
			System.out.println("The list doesn't exist. Create that list before you use!");
			return;
		}
		FileInputStream fin=new FileInputStream(System.getProperty("user.home")+"/.pounce/"+listname);
		p.loadFromXML(fin);
		p.put(name, val);
		FileOutputStream fout=new FileOutputStream(System.getProperty("user.home")+"/.pounce/"+listname);
		p.storeToXML(fout, "Pounce List");
		System.out.println("Added "+name+" to "+listname+"!!");
	}
	
	void show(String listname, String name) throws Exception{
		Properties p=new Properties();
		if(!new File(System.getProperty("user.home")+"/.pounce/"+listname).exists()){
			System.out.println("The list doesn't exist. Create that list before you use!");
			return;
		}
		FileInputStream fin=new FileInputStream(System.getProperty("user.home")+"/.pounce/"+listname);
		p.loadFromXML(fin);
		System.out.println(p.get(name));
	}
	
	void get(String listname, String name) throws Exception{
		Properties p=new Properties();
		if(!new File(System.getProperty("user.home")+"/.pounce/"+listname).exists()){
			System.out.println("The list doesn't exist. Create that list before you use!");
			return;
		}
		FileInputStream fin=new FileInputStream(System.getProperty("user.home")+"/.pounce/"+listname);
		p.loadFromXML(fin);
		String val=p.getProperty(name);
		if(val.isEmpty())
			System.out.println("That item doesn't exist in the list!");
		else{
			StringSelection data = new StringSelection(val);
			Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
			clipboard.setContents(data, data);
			System.out.println(val+" is on the clipboard now!");
		}
	}
	
	void delete(String listname, String name) throws Exception{
		Properties p=new Properties();
		if(!new File(System.getProperty("user.home")+"/.pounce/"+listname).exists()){
			System.out.println("The list doesn't exist. Create that list before you use!");
			return;
		}
		FileInputStream fin=new FileInputStream(System.getProperty("user.home")+"/.pounce/"+listname);
		p.loadFromXML(fin);
		p.remove(name);
		FileOutputStream fout=new FileOutputStream(System.getProperty("user.home")+"/.pounce/"+listname);
		p.storeToXML(fout, "Pounce List");
		System.out.println("Removed "+name+" from the list "+listname);
	}
}
