package com.pluribus.vcf.helper;

import com.jcabi.ssh.SSHByPassword;
import com.jcabi.ssh.Shell;
import java.io.IOException;
import java.net.UnknownHostException;

public class SwitchMethods {
	private String switchIp;
	private String switchUser = "root";
	private String switchPwd = "test123";
	
	public SwitchMethods(String switchName) {
		this.switchIp = switchName;
	}
	
	public Shell getSwitchSession() {
		Shell serverSession = null;
		try {
				serverSession = new Shell.Verbose(
					new SSHByPassword(
							switchIp,
							22,
							switchUser,
							switchPwd
						)
				);
		} catch (UnknownHostException e) {
			System.out.println(e.toString());
		}
		return serverSession;
	}
	
	public void clearSessions() {
		Shell session = getSwitchSession();
		String out1;
		try {
			out1 = new Shell.Plain(session).exec("cli --no-login-prompt --quiet -c connection-clear");
			out1 = new Shell.Plain(session).exec("cli --no-login-prompt --quiet -c connection-clear-history");
		} catch (IOException e) {
			System.out.println(e.toString());
		}
	}
	
	public int getConnectionCount(String destIp) {
		int connCount = 0;
		Shell session = getSwitchSession();
		String out1 = null;
		try {
			out1 = new Shell.Plain(session).exec("cli --no-login-prompt --quiet -c \"connection-show dst-ip "+destIp+"\"");
		} catch(IOException e) {
			System.out.println(e.toString());
		}
		String[] connLines = out1.split("\n");
		for(int x =0; x < connLines.length; x++) {
			if(connLines[x].contains(destIp)) {
				connCount += 1;
			}
		}
		return connCount;
	}
}
