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

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.enterprise.inject.Model;
import javax.enterprise.inject.Produces;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import py.pol.una.ii.pw.data.ProductListProducer;
import py.pol.una.ii.pw.model.Compra;
import py.pol.una.ii.pw.model.CompraDetalle;
import py.pol.una.ii.pw.model.Product;
import py.pol.una.ii.pw.service.CompraRegistration;
import py.pol.una.ii.pw.service.ProductRegistration;

// The @Model stereotype is a convenience mechanism to make this a request-scoped bean that has an
// EL name
// Read more about the @Model stereotype in this FAQ:
// http://sfwk.org/Documentation/WhatIsThePurposeOfTheModelAnnotation
//@Model
@ViewScoped
@ManagedBean
public class CompraController {

    @Inject
    private FacesContext facesContext;
    
    @Inject
    private CompraDetalle detalle;
    
    @Inject
    private ProductListProducer productListProducer;
    
    @Inject
    private Logger log;

    @Inject
    private CompraRegistration compraRegistration;
    
    @Inject
    private ProductRegistration productRegistration;

    private Compra newcompra;
    
    private List<Compra> comprasmatched;
    
    private String namecompra;
    
    @Produces
    @Named
    public String getcompraName(){
    	return namecompra;
    }
    
    private Long someid;
    
    private Long proveedorId;
    private Long productoId;
    private Long cantidad;
    private int provint;
    private List<CompraDetalle> detallesCompra;

    @Produces
    @Named
    public Compra getNewcompra() {
        return newcompra;
    }
    
    @Produces
    @Named
    public List<Compra> getMatchescompras(){
    	return comprasmatched;
    }
    

    public void register() throws Exception {
		try {
            compraRegistration.register(newcompra);
            facesContext.addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Registered!", "Registration successful"));
            initNewcompra();
        } catch (Exception e) {
            String errorMessage = getRootErrorMessage(e);
            FacesMessage m = new FacesMessage(FacesMessage.SEVERITY_ERROR, errorMessage, "Registration Unsuccessful");
            facesContext.addMessage(null, m);
        }
    }
    
    public void delete(Long id) throws Exception {
    	try{
    		log.info("Borrando = " + id);
    		compraRegistration.delete(id);
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
    		newcompra = compraRegistration.getCompra(id);
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
    		log.info("newcompra = " + newcompra.getId());
    		compraRegistration.update(newcompra);
            facesContext.addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Modified!", "Modification successful"));
            initNewcompra();
        } catch (Exception e) {
            String errorMessage = getRootErrorMessage(e);
            FacesMessage m = new FacesMessage(FacesMessage.SEVERITY_ERROR, errorMessage, "Registration Unsuccessful");
            facesContext.addMessage(null, m);
        }
    }
    
    public void search(String name){
    	log.info("No llega = ");
    	log.info("Name: " + name);
    	comprasmatched = (List<Compra>)compraRegistration.search(name);
		
		if (!comprasmatched.isEmpty())
			log.info("List = " + comprasmatched.get(0));
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
    public void initNewcompra() {
        newcompra = new Compra();
    }
    
    /*public void setProveedorId(Long id){
    	this.proveedorId = id;
    }*/
    
    @Produces
    @Named
    public Long getProveedorId(){
    	return this.proveedorId;
    	//return Long.valueOf(1);
    }
    
    /*public void setProductoId(Long id){
    	this.productoId = id;
    }*/
    
    public Long getProductoId(){
    	return this.productoId;
    }
    
	public List<Product> getProductoByProveedor(int idprov) {
		proveedorId = Long.valueOf(idprov);
		log.info("getProducto proveedorId: "+ proveedorId);
		if(proveedorId != null) {
			return productListProducer.retrieveAllProductsByProvider(proveedorId.longValue());
		}

		return null;
	}
    
	public void addDetalleCompra() {

		if(detallesCompra == null) {
			detallesCompra = new ArrayList<>();
		}

		if(productoId != null) {
			detalle.setCantidad(cantidad);
			detalle.setProducto(productRegistration.getProduct(productoId));
			detallesCompra.add(detalle);
		}
		detalle = new CompraDetalle();
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

	public Long getCantidad() {
		return cantidad;
	}

	/*public void setCantidad(Long cantidad) {
		this.cantidad = cantidad;
	}*/
	
	/*public void setCompraDetalle(CompraDetalle detalle){
		this.detallesCompra.add(detalle);
	}*/
	
	public List<CompraDetalle> getCompraDetalle(){
		return this.detallesCompra;
	}

	public int getProvint() {
		return provint;
	}

	public void setProvint(int provint) {
		this.provint = provint;
	}
}
