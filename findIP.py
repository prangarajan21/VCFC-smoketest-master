import sys,ssl
from pprint import  pprint
from pysphere import VIServer

server = VIServer()
VCENTER_IP="10.9.34.204"
VCENTER_USER="administrator@lab.pluribus"
VCENTER_PASS="MyTest-456"

#ova_name="VCFC-2.2.0-jenkins-3108"
ova_name=sys.argv[1]

server.connect(VCENTER_IP,VCENTER_USER,VCENTER_PASS)

vm = server.get_vm_by_name(ova_name)
net_info = vm.get_property('net',False)
vcfc_ip=[ x['ip_addresses'][0]  for  x  in  net_info if x['network'] == 'VM Network' ][0]
print vcfc_ip
