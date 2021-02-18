package com.cloud.provider.api.Services;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.statemachine.StateMachine;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;

import com.aerospike.client.Key;
import com.aerospike.client.command.Buffer;
import com.cloud.provider.api.Repository.ServerRepository;
import com.cloud.provider.api.model.Server;

@Service
@EnableTransactionManagement
public class ServerService {

	@Autowired
	private StateMachine<String, String> stateMachine;
	@Autowired
	public ServerRepository serverRepository;

	
	public Server createServer(int size) {

		Server server = new Server();
		stateMachine.start();
		
		if (stateMachine.getState().getId() == "create") {    // to make sure that the first request will allocate the memory first
			LocalDateTime myObj = LocalDateTime.now();
			Key key = new Key("test", "test", myObj.toString());
			server.setKey(Buffer.bytesToHexString(key.digest));
			server.setName("New");
			server.setRam(100);
			server.setFreeMemory(100-size);
			serverRepository.save(server);
				
		}
		stateMachine.sendEvent("wait");
		if (stateMachine.getState().getId() == "active") {
			return server;
		}
		return null;
	}

	public List<Server> getAllServers() {

		List<Server> servers = (List<Server>) serverRepository.findAll();

		return servers;
	}


	public Server allocateServer(int size) {

		Server server = serverRepository.findByFreeMemoryGreaterThanEqual(size);
		if (server != null) {
			server.setFreeMemory(server.getFreeMemory() - size);
			serverRepository.save(server);

		} else {

			if (stateMachine.getState().getId() == "create") {  // if another request come while creating a new one and there is already no space it will wait to make sure that the new server may have space too 
				
				while(true) {
					if (stateMachine.getState().getId() == "active") {
						Server LastCreatedServer = serverRepository.findByFreeMemoryGreaterThanEqual(size); // new server still have space or not ? 
						if (LastCreatedServer != null) {
							LastCreatedServer.setFreeMemory(LastCreatedServer.getFreeMemory() - size);
							serverRepository.save(LastCreatedServer);
							break;
						}
						else {			
							server = createServer(size);
							server.setFreeMemory(100 - size);
							serverRepository.save(server);
							break;
						}					
					}					
				}				
			}else {
				
				server = createServer(size);
				server.setFreeMemory(100 - size);
				serverRepository.save(server);
			}
			
		}

		return server;
	}

}
