/*
 * JBoss, Home of Professional Open Source
 * Copyright 2013, Red Hat, Inc. and/or its affiliates, and individual
 * contributors by the @authors tag. See the copyright.txt in the
 * distribution for a full listing of individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package py.pol.una.ii.pw.service;

import py.pol.una.ii.pw.model.Provider;

import javax.ejb.Stateless;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import javax.persistence.EntityManager;

import java.util.List;
import java.util.logging.Logger;

// The @Stateless annotation eliminates the need for manual transaction demarcation
@Stateless
public class ProviderRegistration {

    @Inject
    private Logger log;

    @Inject
    private EntityManager em;

    @Inject
    private Event<Provider> providerEventSrc;

    public void register(Provider provider) {
		log.info("Registering " + provider.getName());
        em.persist(provider);
        providerEventSrc.fire(provider);
        
    }
    
    public void delete(Long id){
    	Provider provider = new Provider();
    	provider = em.find(Provider.class, id);
    	log.info("Deleting " + provider.getName());
    	em.remove(provider);
    	providerEventSrc.fire(provider);
    }
    
    public void update(Provider newProvider) throws Exception{
    	
    	try{
    		log.info("Updating " + newProvider.getId());
    		Provider oldProvider = getProvider(newProvider.getId());
    		em.merge(oldProvider);
    		oldProvider.setName(newProvider.getName());
    		oldProvider.setInformacion(newProvider.getInformacion());
    		oldProvider.setPhoneNumber(newProvider.getPhoneNumber());
    		providerEventSrc.fire(oldProvider);
    	}catch(Exception e){
    		e.printStackTrace();
    	}
    	
    }
    
    public List<Provider> search(String name){
    	log.info("Searching " + name);
    	List<Provider> result = (List<Provider>)em.createQuery("SELECT p FROM Provider p WHERE p.name LIKE :pname")
    		.setParameter("pname", "%"+name+"%")
    		.setMaxResults(10)
    		.getResultList();
    	if (result.isEmpty())
    		log.info("Esta vacio nio");
    	return result;
    }
    public Provider getProvider(Long id){
    	Provider provider = new Provider();
    	provider = em.find(Provider.class, id);
    	return provider;
    }
    
    
}
