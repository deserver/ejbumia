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

import py.pol.una.ii.pw.model.Product;
import py.pol.una.ii.pw.model.Provider;

import javax.ejb.Stateless;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import javax.persistence.EntityManager;

import java.util.List;
import java.util.logging.Logger;

// The @Stateless annotation eliminates the need for manual transaction demarcation
@Stateless
public class ProductRegistration {

    @Inject
    private Logger log;

    @Inject
    private EntityManager em;

    @Inject
    private Event<Product> productEventSrc;

    public void register(Product product, Long providerId) {
		log.info("Registering " + product.getName());
		log.info("ProviderId " + providerId);
		Provider provider = em.find(Provider.class, providerId);
		product.setProvider(provider);
        em.persist(product);
        productEventSrc.fire(product);
        
    }
    
    public void delete(Long id){
    	Product product = new Product();
    	product = em.find(Product.class, id);
    	log.info("Deleting " + product.getName());
    	em.merge(product);
    	em.remove(product);
    	em.getEntityManagerFactory().getCache().evictAll();
    	productEventSrc.fire(product);
    }
    
    public void update(Product newProduct, Long idprov) throws Exception{
    	
    	try{
    		log.info("Updating " + newProduct.getId());
    		Product oldProduct = getProduct(newProduct.getId());
    		em.merge(oldProduct);
    		oldProduct.setName(newProduct.getName());
    		oldProduct.setCantidad(newProduct.getCantidad());
    		oldProduct.setProvider(em.find(Provider.class, idprov ));
    		productEventSrc.fire(oldProduct);
    	}catch(Exception e){
    		e.printStackTrace();
    	}
    	
    }
    
    public List<Product> search(String name){
    	log.info("Searching " + name);
    	List<Product> result = (List<Product>)em.createQuery("SELECT p FROM Product p WHERE p.name LIKE :pname")
    		.setParameter("pname", "%"+name+"%")
    		.setMaxResults(10)
    		.getResultList();
    	if (result.isEmpty())
    		log.info("Esta vacio nio");
    	return result;
    }
    public Product getProduct(Long id){
    	Product product = new Product();
    	product = em.find(Product.class, id);
    	return product;
    }
    
    
}
