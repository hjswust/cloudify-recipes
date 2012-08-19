/*******************************************************************************
* Copyright (c) 2011 GigaSpaces Technologies Ltd. All rights reserved
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
*       http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*******************************************************************************/
import org.hyperic.sigar.OperatingSystem
import org.cloudifysource.dsl.utils.ServiceUtils;
import org.cloudifysource.dsl.context.ServiceContextFactory
import static mysql_runner.*


/* 
	This file enables users to invoke an SQL statement
	Usage :  invoke mysql query actionUser dbName query
	Example: invoke mysql query root myDbName "update users set city=\"NY\" where uid=15"
*/	

config=new ConfigSlurper().parse(new File('mysql-service.properties').toURL())
osConfig=ServiceUtils.isWindows() ? config.win64 : config.linux
context = ServiceContextFactory.getServiceContext()
mysqlHost=context.attributes.thisInstance["dbHost"]
binFolder=context.attributes.thisInstance["binFolder"]
println "mysql_query.groovy: mysqlHost is ${mysqlHost} "

def currActionQuery 
def currOsName

def os = OperatingSystem.getInstance()
def currVendor=os.getVendor()
switch (currVendor) {
		case ["Ubuntu", "Debian", "Mint"]:			
			currOsName="unix"		
			break		
		case ["Red Hat", "CentOS", "Fedora", "Amazon",""]:			
			currOsName="unix"
			break					
		case ~/.*(?i)(Microsoft|Windows).*/:			
			currOsName="windows"
			break
		default: throw new Exception("Support for ${currVendor} is not implemented")
}


if (args.length < 3) {
	println "mysql_query.groovy: query error: Missing parameters\nUsage: invoke mysql query actionUser dbName query"
	System.exit(-1)
}


def currActionUser = args[0]
def currActionDbName = args[1]
def currQuery = "\"" + args[2] + "\""
def currDebugMsg = "Invoking query: ${currQuery}"

runMysqlQuery(binFolder,osConfig.mysqlProgram,currOsName,currQuery,currActionDbName,currActionUser,currDebugMsg)
							
println "mysql_query.groovy: End"
	

