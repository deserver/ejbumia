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
import java.util.Map;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.enterprise.inject.Model;
import javax.enterprise.inject.Produces;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import py.pol.una.ii.pw.model.Product;
import py.pol.una.ii.pw.model.Provider;
import py.pol.una.ii.pw.service.ProductRegistration;
import py.pol.una.ii.pw.data.ProductListProducer;

// The @Model stereotype is a convenience mechanism to make this a request-scoped bean that has an
// EL name
// Read more about the @Model stereotype in this FAQ:
// http://sfwk.org/Documentation/WhatIsThePurposeOfTheModelAnnotation
@Model
@ViewScoped
@ManagedBean
public class ProductController {

    @Inject
    private FacesContext facesContext;
    
    @Inject
    private Logger log;

    @Inject
    private ProductRegistration productRegistration;
    
    @Inject
    private ProductListProducer productListProducer;

    private Product newProduct;
    
    private List<Product> matches;
    private List<Product> matchesByProv;
    
    private String nameProduct;
    
    private Boolean show; 
    private Boolean nothingMatched = false;
    
    private Long providerId;
    
    @ManagedProperty(value="#{param.id}")
    private Long productId;
    
    @Produces
    @Named
    public String getProductName(){
    	return nameProduct;
    }
    
    @Produces
    @Named
    public Long getIdProvider(){
    	productListProducer.setProvId(providerId);
    	return providerId;
    }
    
    @Produces
    @Named
    public Long getProductId(){
    	return productId = newProduct.getId();
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
    	return matches;
    }
    /*
    @Produces
    @Named
    public List<Product> getMatchesByProv(){
    	log.info("getMatchesbyprov = "+ providerId);
    	listarProductos(providerId.toString());
    	return matches;
    }*/
    
    @Produces
    @Named
    public Boolean getBoolShow(){
    	return show;
    }
    
    @Produces
    @Named
    public Boolean getNothingMatched(){
    	return nothingMatched;
    }
    

    public void register(String idprov) throws Exception {
		try {
			log.info("proveedor id = " + idprov);
            productRegistration.register(newProduct, Long.valueOf(idprov));
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
            FacesMessage m = new FacesMessage(FacesMessage.SEVERITY_ERROR, errorMessage, 
            		"Registration Unsuccessful");
            facesContext.addMessage(null, m);
    	}
    }
    
    public void modify(Long id) throws Exception{
    	try{
    		log.info("modify " + id);
    		Map<String,String> params = 
    		            facesContext.getExternalContext().getRequestParameterMap();
    		String idString = params.get("id");
    		id = Long.parseLong(idString);
    		
    		//String trxNo = facesContext.getExternalContext().getRequestParameterMap().get("id");
    		log.info("modify " + id);
    		newProduct = productRegistration.getProduct(id);
    		providerId = newProduct.getProvider().getId();
    		someid = id;
    		log.info("Some Id = " + id);
    		//newProduct.setId(modProduct.getId());
    		//newProduct.setCantidad(modProduct.getCantidad());
    		//newProduct.setName(modProduct.getName());
    	}catch (Exception e){
    		String errorMessage = getRootErrorMessage(e);
            FacesMessage m = new FacesMessage(FacesMessage.SEVERITY_ERROR, errorMessage, 
            		"Registration Unsuccessful");
            facesContext.addMessage(null, m);
    	}
    }
    
    public void modify() throws Exception{
    	try{
    		log.info("modify " + productId);
    		newProduct = productRegistration.getProduct(productId);
    		providerId = newProduct.getProvider().getId();
    		someid = productId;
    		log.info("Some Id = " + productId);
    		//newProduct.setId(modProduct.getId());
    		//newProduct.setCantidad(modProduct.getCantidad());
    		//newProduct.setName(modProduct.getName());
    	}catch (Exception e){
    		String errorMessage = getRootErrorMessage(e);
            FacesMessage m = new FacesMessage(FacesMessage.SEVERITY_ERROR, errorMessage, 
            		"Registration Unsuccessful");
            facesContext.addMessage(null, m);
    	}
    }
    
    public void registerMod(String idprov) throws Exception{
    	try {
    		log.info("newproduct = " + newProduct.getId());
    		
    		productRegistration.update(newProduct, Long.valueOf(idprov));
            facesContext.addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Modified!", "Modification successful"));
            initNewProduct();
        } catch (Exception e) {
            String errorMessage = getRootErrorMessage(e);
            FacesMessage m = new FacesMessage(FacesMessage.SEVERITY_ERROR, errorMessage, 
            		"Registration Unsuccessful");
            facesContext.addMessage(null, m);
        }
    }
    
    public void search(String name){
    	log.info("Name: " + name);
    	matches = (List<Product>)productRegistration.search(name);
		
		if (!matches.isEmpty()){
			log.info("List = " + matches.get(0));
    	}else{
			log.info("Vacio = ");
			nothingMatched = true;
		}
    }
    
    public void getProdProv(String idprov){
    	
    	log.info("idprov getProdProv=" + idprov);
    	providerId = Long.valueOf(idprov);
    	try {
    		matches = productRegistration.getProductsbyProv(Long.valueOf(idprov));
    	}catch (Exception e) {
			e.printStackTrace();
		}
    }
    
    
    public List<Product> listarProductos(String idprov){
    	
    	log.info("idprov listarproductos=" + idprov);
    	try {
    		matches = productRegistration.getProductsbyProv(Long.valueOf(idprov));
    		return matches;
    	}catch (Exception e) {
			e.printStackTrace();
			return null;
		}
    }
    
    public Long getIdProd(){
    	String value = facesContext.getCurrentInstance().getExternalContext() .getRequestParameterMap().get(productId.toString());
    	productId =  Long.valueOf(productId);
    	return productId;
    }
    
    
    
    public void showList() throws Exception{
    	try{
    		show = true;
    	}catch (Exception e){
    		e.printStackTrace();
    	}
    }
    
    public void init(Long idprod) {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        if (!facesContext.isPostback() && !facesContext.isValidationFailed()) {
            log.info("Llego esto: " + idprod);
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
