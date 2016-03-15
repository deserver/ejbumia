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
package py.pol.una.ii.pw.data;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.event.Observes;
import javax.enterprise.event.Reception;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.Query;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import py.pol.una.ii.pw.model.Product;

@RequestScoped
public class ProductListProducer {

    @Inject
    private ProductRepository productRepository;

    private List<Product> products;
    
    private List<Product> productsByProv;
    
    private Long provId;
    
    @Inject
    private Logger log;
    


    // @Named provides access the return value via the EL variable name "members" in the UI (e.g.,
    // Facelets or JSP view)
    @Produces
    @Named
    public List<Product> getProducts() {
        return products;
    }
    
    @Produces
    @Named
    public List<Product> getProductsbyProv() {
        return productsByProv;
    }
    
    @Produces
    @Named
    public Long getProvId() {
        return provId;
    }

    

    public void onProductListChanged(@Observes(notifyObserver = Reception.IF_EXISTS) final Product product) {
        retrieveAllProductsOrderedByName();
    }
    
    public void onProductListbyProvChanged(@Observes(notifyObserver = Reception.IF_EXISTS) final Product product) {
    	try{
    		retrieveAllProductsByProvider(getProvId());
    	}catch (Exception e){
    		e.printStackTrace();
    	}
    	
    }

    @PostConstruct
    public void retrieveAllProductsOrderedByName() {
        products = productRepository.findAllOrderedByName();
    	try{
    		productsByProv = productRepository.findAllbyProvider(provId);
    	}catch (Exception e){
    		e.printStackTrace();
    	}
    }
    
    //@PostConstruct
    @SuppressWarnings("unchecked")
    public List<Product> retrieveAllProductsByProvider(Long idprov) {
    	log.info("LLego hasta aca!" + idprov);
    	try{
    		return productsByProv = productRepository.findAllbyProvider(idprov);
    	}catch (Exception e){
    		e.printStackTrace();
    		return new ArrayList<>();
    	}
    }
    

	public void setProvId(Long provId) {
		this.provId = provId;
	}
}
