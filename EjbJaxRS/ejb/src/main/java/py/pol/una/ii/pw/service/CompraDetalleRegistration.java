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

import py.pol.una.ii.pw.model.CompraDetalle;

import javax.ejb.Stateless;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import javax.persistence.EntityManager;

import java.util.List;
import java.util.logging.Logger;

// The @Stateless annotation eliminates the need for manual transaction demarcation
@Stateless
public class CompraDetalleRegistration {

    @Inject
    private Logger log;

    @Inject
    private EntityManager em;

    @Inject
    private Event<CompraDetalle> CompraDetalleEventSrc;

    public void register(CompraDetalle CompraDetalle) {
		//log.info("Registering " + CompraDetalle.getName());
        em.persist(CompraDetalle);
        CompraDetalleEventSrc.fire(CompraDetalle);
        
    }
    
    public void delete(Long id){
    	CompraDetalle CompraDetalle = new CompraDetalle();
    	CompraDetalle = em.find(CompraDetalle.class, id);
    	//log.info("Deleting " + CompraDetalle.getName());
    	em.remove(CompraDetalle);
    	CompraDetalleEventSrc.fire(CompraDetalle);
    }
    
    /*public void update(CompraDetalle newCompraDetalle) throws Exception{
    	
    	try{
    		log.info("Updating " + newCompraDetalle.getId());
    		CompraDetalle oldCompraDetalle = getCompraDetalle(newCompraDetalle.getId());
    		em.merge(oldCompraDetalle);
    		oldCompraDetalle.setName(newCompraDetalle.getName());
    		oldCompraDetalle.setInformacion(newCompraDetalle.getInformacion());
    		oldCompraDetalle.setPhoneNumber(newCompraDetalle.getPhoneNumber());
    		CompraDetalleEventSrc.fire(oldCompraDetalle);
    	}catch(Exception e){
    		e.printStackTrace();
    	}
    	
    }*/
    
    public List<CompraDetalle> search(String name){
    	log.info("Searching " + name);
    	List<CompraDetalle> result = (List<CompraDetalle>)em.createQuery("SELECT p FROM CompraDetalle p WHERE p.name LIKE :pname")
    		.setParameter("pname", "%"+name+"%")
    		.setMaxResults(10)
    		.getResultList();
    	if (result.isEmpty())
    		log.info("Esta vacio nio");
    	return result;
    }
    public CompraDetalle getCompraDetalle(Long id){
    	CompraDetalle CompraDetalle = new CompraDetalle();
    	CompraDetalle = em.find(CompraDetalle.class, id);
    	return CompraDetalle;
    }
    
    
}
