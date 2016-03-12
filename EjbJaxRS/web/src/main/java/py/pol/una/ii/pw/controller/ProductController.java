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
package py.pol.una.ii.pw.controller;

import java.util.List;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.enterprise.inject.Model;
import javax.enterprise.inject.Produces;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import py.pol.una.ii.pw.model.Product;
import py.pol.una.ii.pw.service.ProductRegistration;

// The @Model stereotype is a convenience mechanism to make this a request-scoped bean that has an
// EL name
// Read more about the @Model stereotype in this FAQ:
// http://sfwk.org/Documentation/WhatIsThePurposeOfTheModelAnnotation
@Model
public class ProductController {

    @Inject
    private FacesContext facesContext;
    
    @Inject
    private Logger log;

    @Inject
    private ProductRegistration productRegistration;

    private Product newProduct;
    
    private List<Product> productsmatched;
    
    private String nameProduct;
    
    @Produces
    @Named
    public String getProductName(){
    	return nameProduct;
    }
    
    private Long someid;

    @Produces
    @Named
    public Product getNewProduct() {
        return newProduct;
    }
    
    @Produces
    @Named
    public List<Product> getMatches(){
    	return productsmatched;
    }
    

    public void register() throws Exception {
		try {
            productRegistration.register(newProduct);
            facesContext.addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Registered!", "Registration successful"));
            initNewProduct();
        } catch (Exception e) {
            String errorMessage = getRootErrorMessage(e);
            FacesMessage m = new FacesMessage(FacesMessage.SEVERITY_ERROR, errorMessage, "Registration Unsuccessful");
            facesContext.addMessage(null, m);
        }
    }
    
    public void delete(Long id) throws Exception {
    	try{
    		log.info("Borrando = " + id);
    		productRegistration.delete(id);
    		facesContext.addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Deleted!", "Delete successful"));
    	}catch (Exception e){
    		String errorMessage = getRootErrorMessage(e);
            FacesMessage m = new FacesMessage(FacesMessage.SEVERITY_ERROR, errorMessage, "Registration Unsuccessful");
            facesContext.addMessage(null, m);
    	}
    }
    
    public void modify(Long id) throws Exception{
    	try{
    		newProduct = productRegistration.getProduct(id);
    		someid = id;
    		log.info("Some Id = " + id);
    		//newProduct.setId(modProduct.getId());
    		//newProduct.setCantidad(modProduct.getCantidad());
    		//newProduct.setName(modProduct.getName());
    	}catch (Exception e){
    		String errorMessage = getRootErrorMessage(e);
            FacesMessage m = new FacesMessage(FacesMessage.SEVERITY_ERROR, errorMessage, "Registration Unsuccessful");
            facesContext.addMessage(null, m);
    	}
    }
    
    public void registerMod() throws Exception{
    	try {
    		log.info("newproduct = " + newProduct.getId());
    		productRegistration.update(newProduct);
            facesContext.addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Modified!", "Modification successful"));
            initNewProduct();
        } catch (Exception e) {
            String errorMessage = getRootErrorMessage(e);
            FacesMessage m = new FacesMessage(FacesMessage.SEVERITY_ERROR, errorMessage, "Registration Unsuccessful");
            facesContext.addMessage(null, m);
        }
    }
    
    public void search(String name){
    	log.info("No llega = ");
    	log.info("Name: " + name);
    	productsmatched = (List<Product>)productRegistration.search(name);
		
		if (!productsmatched.isEmpty())
			log.info("List = " + productsmatched.get(0));
		else
			log.info("Vacio = ");
		facesContext.addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_INFO, "Found!", "Search successful"));
    	try{
    		
    	}catch (Exception e){
    		e.printStackTrace();
    	}
    }

    @PostConstruct
    public void initNewProduct() {
        newProduct = new Product();
    }
    
    
    

    private String getRootErrorMessage(Exception e) {
        // Default to general error message that registration failed.
        String errorMessage = "Registration failed. See server log for more information";
        if (e == null) {
            // This shouldn't happen, but return the default messages
            return errorMessage;
        }

        // Start with the exception and recurse to find the root cause
        Throwable t = e;
        while (t != null) {
            // Get the message from the Throwable class instance
            errorMessage = t.getLocalizedMessage();
            t = t.getCause();
        }
        // This is the root cause message
        return errorMessage;
    }
}
