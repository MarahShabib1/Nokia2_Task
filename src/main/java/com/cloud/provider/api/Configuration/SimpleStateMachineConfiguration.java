package com.cloud.provider.api.Configuration;

import java.util.concurrent.TimeUnit;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.action.Action;
import org.springframework.statemachine.config.EnableStateMachine;
import org.springframework.statemachine.config.EnableStateMachineFactory;
import org.springframework.statemachine.config.StateMachineConfigurerAdapter;
import org.springframework.statemachine.config.builders.StateMachineStateConfigurer;
import org.springframework.statemachine.config.builders.StateMachineTransitionConfigurer;
import org.springframework.transaction.annotation.EnableTransactionManagement;



@Configuration
@EnableTransactionManagement
//@EnableStateMachine
@EnableStateMachineFactory
public class SimpleStateMachineConfiguration 
  extends StateMachineConfigurerAdapter<String, String> {
	
    @Override
    public void configure(StateMachineStateConfigurer<String, String> states) 
      throws Exception {
 
        states
          .withStates()
          .initial("create").stateEntry("create", new Action<String,String> (){

           
			@Override
			public void execute(StateContext<String,String > context  ) {
			
				try {
					
					
					System.out.println("Inside create");
					
					Thread.sleep(20000);
				//	TimeUnit.SECONDS.sleep(20);
				
				} catch (InterruptedException e) {
					
					e.printStackTrace();
				}
			
			}
			
		
        	  
          })
          .end("active");
      

    }


    @Override
    public void configure(
      StateMachineTransitionConfigurer<String, String> transitions) 
      throws Exception {
 
    	 transitions.withExternal()
         .source("create").target("active").event("wait");
    }
}
