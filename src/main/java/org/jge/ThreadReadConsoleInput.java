package org.jge;

import java.util.Scanner;

public class ThreadReadConsoleInput extends Thread
{

	private boolean running;

	public ThreadReadConsoleInput()
	{
		super("Read Console commands");
		this.running = true;
	}
	
	public void run()
	{
		Scanner sc = new Scanner(System.in);
		while(running)
		{
			try
			{
    			String line = sc.nextLine();
    			Console.executeStringCommand(line);
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
		}
		sc.close();
	}
}
