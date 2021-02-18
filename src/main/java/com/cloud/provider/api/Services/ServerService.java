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

	
	public Server createServer() {

		Server server = new Server();
		stateMachine.start();
		stateMachine.sendEvent("wait");
		if (stateMachine.getState().getId() == "active") {
			LocalDateTime myObj = LocalDateTime.now();
			Key key = new Key("test", "test", myObj.toString());
			server.setKey(Buffer.bytesToHexString(key.digest));
			server.setName("New");
			server.setRam(100);
			server.setFreeMemory(100);
			serverRepository.save(server);
		}
		return server;
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

			server = createServer();
			server.setFreeMemory(100 - size);
			serverRepository.save(server);

		}

		return server;
	}

}
