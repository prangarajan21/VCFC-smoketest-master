VCFC testsuite

Mandatory parameters 
 - in testng.xml file
    <parameter name="switchName" value="leo-vcf-3"></parameter>
    <parameter name="mgmtIp" value="10.9.21.50"></parameter>
    <parameter name="dataNodeHost" value="10.9.8.85"></parameter>
 - through command line
     vcfIp: This is the IP of the VCF instance
     browser: Browser to run test on

Optional attributes:
	1. This is required if a different password (not the default is desired for test purposes) for your VCF instance
	<parameter name = "password" value="value_to_be_set_for_vcfc_instance"></parameter>
	2. To use your browserstack credentials:
        <parameter name= "bsUserId" value="BS_uid"></parameter>
        <parameter name= "bsKey", value="BS_Key"></parameter>
	3. If this value is set to 0 (default is 1), then the VCFC session will not be cleaned up and test will start in the existing system
        <parameter name= "cleanBeforeTest", value=0/1></parameter>

How to invoke:
Run shell script in directory (OR)
mvn -Dvcfp=IP_addr -Dbrowser=Chrome/Firefox/IE -Dgroups=Smoke clean install
# VCFC-smoketest-master
# VCFC-smoketest-master
