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
service {
	extend "../../../services/mysql"
	customCommands ([
		/* 
			This custom command enables users to create a database snapshot (mysqldump).
			Usage :  invoke mysql mysqldump actionUser dumpPrefix [dbName]
			Example: invoke mysql mysqldump root myPrefix_ myDbName
		*/
	
		"mysqldump" : "mysql_dump.groovy" , 
			
		/* 
			This custom command enables users to invoke an SQL statement
			Usage :  invoke mysql query actionUser [puserPassword] dbName query
			Examples: 			
				1. invoke mysql query root myDbName "update users set city=\"NY\" where uid=15"
				2. invoke mysql query root pmyRootPassword myDbName "update users set city=\"NY\" where uid=15"			
			
		*/			
		"query" : "mysql_query.groovy" ,
		
		/* 
			This custom command enables users to add a slave to the master.
	        It should be invoked only on a master instance (by a remote slave) 
			and only if masterSlaveMode is set to true on both the slave and master.
	        As a result, the following will be invoked :  
	        mysql -u root -D dbName -e 
			  "GRANT REPLICATION SLAVE, REPLICATION CLIENT ON *.* TO slaveUser@'slaveHostIP' IDENTIFIED BY 'slavePassword';"
	
	       Usage :  invoke mysqlmaster addSlave actionUser dbName slaveUser slavePassword slaveHostIP 			
			
		*/
		"addSlave": "mysql_addSlave.groovy" , 
		
		/* 
			This custom command enables users to show the master's status.
	        It should be invoked only on a master instance (either by the master or by a remote slave) 
			and only if masterSlaveMode is set to true.
	        As a result, the following will be invoked :  
	        mysql -u root -D dbName -e "show master status;" 
		    and the mysql-bin will be stored in context.attributes.thisApplication["masterBinLogFile"] 
		    and the master's log's position will be stored in context.attributes.thisApplication["masterBinLogPos"]  
		
	       Usage :  invoke mysqlmaster showMasterStatus actionUser dbName  			
			
		*/
		"showMasterStatus": "mysql_showMasterStatus.groovy" , 
		
		/* 
			This custom command enables users to import a zipped file to a database
			Usage :  invoke mysql import actionUser dbName zipFileURL
			Example: invoke mysql import root myDbName http://www.mysite.com/myFile.zip
		*/
		
		"import" : "mysql_import.groovy" , 
		
		/* 
			This custom command enables users to invoke a Drupal command on the database.
			Usage :   invoke mysql drupalCommand currentCommandName
			Example1: invoke mysql drupalCommand activateSite
			Example2: invoke mysql drupalCommand siteOffline
			Example3: invoke mysql drupalCommand deleteCache
		*/
		"drupalCommand" : "mysql_drupalCommands.groovy"
	])
}