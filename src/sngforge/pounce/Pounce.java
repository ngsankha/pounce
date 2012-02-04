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
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Iterator;
import java.util.Properties;
import java.util.Scanner;
import java.util.Set;

public class Pounce{
	
	File home;
	public static void main(String args[]) throws Exception{
		Pounce p=new Pounce();
		String[] arg={"","","",""};
		int min=args.length<4?args.length:4;
		for(int i=0;i<min;i++)
			arg[i]=args[i];
		p.init();
		if(args.length==0)
			p.showHelp();
		else{
			try{
				if(arg[0].equals("new"))
					p.newList(arg[1]);
				else if(arg[0].equals("add"))
					p.add(arg[1], arg[2], arg[3]);
				else if(arg[0].equals("show"))
					p.show(arg[1], arg[2]);
				else if(arg[0].equals("get"))
					p.get(arg[1], arg[2]);
				else if(arg[0].equals("delete"))
					p.delete(arg[1], arg[2]);
				else if(arg[0].equals("list"))
					p.list();
				else if(arg[0].equals("help"))
					p.showHelp();
				else if(arg[0].equals("about"))
					p.about();
				else
					System.out.println("Unrecognised option.");
			}catch(Exception e){
				System.out.println(e.getMessage());
			}
		}
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
		System.out.println("about : Information about Pounce");
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
			System.out.println("The list doesn't exist. Create that list before you pounce!");
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
			System.out.println("The list doesn't exist. Create that list before you pounce!");
			return;
		}
		FileInputStream fin=new FileInputStream(System.getProperty("user.home")+"/.pounce/"+listname);
		p.loadFromXML(fin);
		if(name.isEmpty()){
			Set<Object> keys=p.keySet();
			Iterator<Object> it=keys.iterator();
			while(it.hasNext()){
				String key=(String)it.next();
				System.out.println(key+"\t"+p.get(key));
			}
		}else
			System.out.println(p.get(name));
	}
	
	void get(String listname, String name) throws Exception{
		Properties p=new Properties();
		if(!new File(System.getProperty("user.home")+"/.pounce/"+listname).exists()){
			System.out.println("The list doesn't exist. Create that list before you pounce!");
			return;
		}
		FileInputStream fin=new FileInputStream(System.getProperty("user.home")+"/.pounce/"+listname);
		p.loadFromXML(fin);
		String val=p.getProperty(name);
		if(val.isEmpty())
			System.out.println("That item doesn't exist in the list!");
		else{
			Transferable data = new StringSelection(val);
			Toolkit.getDefaultToolkit().getSystemClipboard().setContents(data, null);
			System.out.println(val+" is on the clipboard now!");
			Scanner scanner = new Scanner(System.in);
	        scanner.nextLine();
		}
	}
	
	void delete(String listname, String name) throws Exception{
		Properties p=new Properties();
		if(!new File(System.getProperty("user.home")+"/.pounce/"+listname).exists()){
			System.out.println("The list doesn't exist. Create that list before you pounce!");
			return;
		}
		if(!name.isEmpty()){
			FileInputStream fin=new FileInputStream(System.getProperty("user.home")+"/.pounce/"+listname);
			p.loadFromXML(fin);
			p.remove(name);
			FileOutputStream fout=new FileOutputStream(System.getProperty("user.home")+"/.pounce/"+listname);
			p.storeToXML(fout, "Pounce List");
			System.out.println("Removed "+name+" from the list "+listname);
		}else{
			File f=new File(System.getProperty("user.home")+"/.pounce/"+listname);
			if(f.delete())
				System.out.println(listname+" was removed.");
			else
				System.out.println(listname+" could not be removed.");
		}
	}
	
	void list() throws Exception{
		File lists[]=home.listFiles();
		for(int i=0;i<lists.length;i++){
			Properties p=new Properties();
			FileInputStream fin=new FileInputStream(lists[i]);
			p.loadFromXML(fin);
			System.out.println(lists[i].getName()+"("+p.size()+")");
		}
	}
	
	void about(){
		System.out.println("Pounce, Programmed by Sankha Narayan Guria (sankha93@gmail.com)\n");
		System.out.println("Pounce is a simple command line utility that allows you to access quick lists from the command line and store key/value pairs in different quickly accessible lists. These can be accessed from the command line via simple commands (and can also be copied to the clipboard).");
		System.out.println("\nEmail: sankha93@gmail.com");
		System.out.println("URL: https://github.com/sankha93/pounce\n");
		System.out.println("This program is released under the Terms and Conditions of the GNU General Public License Version 3.");
	}
}
