package com.pluribus.vcf.helper;
import com.jcabi.ssh.SSHByPassword;
import com.jcabi.ssh.Shell;
import java.io.BufferedReader;
import java.io.InputStreamReader;

public class IperfSetup {
	private String clientIp;
	private String serverIp; 
	private String iperfUser = "iperf";
	private String iperfPwd = "test123";
	
	public IperfSetup(String clientAddr, String serverAddr) {
		this.clientIp = clientAddr;
		this.serverIp = serverAddr;
	}
	
	/*Start server */
	public void startServer() throws Exception {
		Shell serverSession = new Shell.Verbose(
	            new SSHByPassword(
	            	serverIp,
	            	22,
	                iperfUser,
	                iperfPwd
	            )
	        );
		
		String out1;
		killServer();
		Process p = Runtime.getRuntime().exec("src/test/resources/start_server.sh "+ serverIp +" "+iperfUser+" "+iperfPwd);
		p.waitFor();
		StringBuffer output = new StringBuffer();
		BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream())); 
		String line = ""; 
		while ((line = reader.readLine())!= null) { 
			output.append(line + "\n"); 
			} 
		com.jcabi.log.Logger.info("startIperfServer","output.toString()");
		com.jcabi.log.Logger.info("startIperfServer","iperf server started");
		return;
	}
	
	public void killServer() throws Exception {
		Shell session = new Shell.Verbose(
	            new SSHByPassword(
	            	serverIp,
	            	22,
	                iperfUser,
	                iperfPwd
	            )
	        );
		String out1 = new Shell.Plain(session).exec("ps aux| grep -E '[i]perf -s' | awk '{print $2}'");
		if(!out1.isEmpty()) {
			String[] pids = out1.split("\n");
			for(int i = 0; i < pids.length; i++) {
				new Shell.Plain(session).exec("kill -9 "+Integer.parseInt(pids[i].trim()));
			}
		}
	}
	
	public boolean sendTraffic(int numSessions,int timeVal, String destIp) throws Exception{
		boolean status = false;
		Shell clientSession = new Shell.Verbose(
	            new SSHByPassword(
		            	clientIp,
		            	22,
		                iperfUser,
		                iperfPwd
		            )
		        );

		    String command = "iperf -c " + destIp + " -P " + numSessions + " -t " +timeVal;
			String out1 = new Shell.Plain(clientSession).exec(command);
			com.jcabi.log.Logger.info("sendTraffic","Traffic sent from iperf client: number of sessions"+numSessions+"for "+timeVal+" seconds");
			Thread.sleep(1000); //Sleeping after traffic run so that stats can be updated
			return status; 
	}
}
