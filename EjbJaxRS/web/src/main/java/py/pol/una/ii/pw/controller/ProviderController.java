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

import py.pol.una.ii.pw.model.Provider;
import py.pol.una.ii.pw.service.ProviderRegistration;

// The @Model stereotype is a convenience mechanism to make this a request-scoped bean that has an
// EL name
// Read more about the @Model stereotype in this FAQ:
// http://sfwk.org/Documentation/WhatIsThePurposeOfTheModelAnnotation
@Model
public class ProviderController {

    @Inject
    private FacesContext facesContext;
    
    @Inject
    private Logger log;

    @Inject
    private ProviderRegistration providerRegistration;

    private Provider newProvider;
    
    private List<Provider> providersmatched;
    
    private String nameProvider;
    
    @Produces
    @Named
    public String getProviderName(){
    	return nameProvider;
    }
    
    private Long someid;

    @Produces
    @Named
    public Provider getNewProvider() {
        return newProvider;
    }
    
    @Produces
    @Named
    public List<Provider> getMatchesProviders(){
    	return providersmatched;
    }
    

    public void register() throws Exception {
		try {
            providerRegistration.register(newProvider);
            facesContext.addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Registered!", "Registration successful"));
            initNewProvider();
        } catch (Exception e) {
            String errorMessage = getRootErrorMessage(e);
            FacesMessage m = new FacesMessage(FacesMessage.SEVERITY_ERROR, errorMessage, "Registration Unsuccessful");
            facesContext.addMessage(null, m);
        }
    }
    
    public void delete(Long id) throws Exception {
    	try{
    		log.info("Borrando = " + id);
    		providerRegistration.delete(id);
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
    		newProvider = providerRegistration.getProvider(id);
    		someid = id;
    		log.info("Some Id = " + id);
    	}catch (Exception e){
    		String errorMessage = getRootErrorMessage(e);
            FacesMessage m = new FacesMessage(FacesMessage.SEVERITY_ERROR, errorMessage, "Registration Unsuccessful");
            facesContext.addMessage(null, m);
    	}
    }
    
    public void registerMod() throws Exception{
    	try {
    		log.info("newProvider = " + newProvider.getId());
    		providerRegistration.update(newProvider);
            facesContext.addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Modified!", "Modification successful"));
            initNewProvider();
        } catch (Exception e) {
            String errorMessage = getRootErrorMessage(e);
            FacesMessage m = new FacesMessage(FacesMessage.SEVERITY_ERROR, errorMessage, "Registration Unsuccessful");
            facesContext.addMessage(null, m);
        }
    }
    
    public void search(String name){
    	log.info("No llega = ");
    	log.info("Name: " + name);
    	providersmatched = (List<Provider>)providerRegistration.search(name);
		
		if (!providersmatched.isEmpty())
			log.info("List = " + providersmatched.get(0));
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
    public void initNewProvider() {
        newProvider = new Provider();
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
