package it.unisannio.controller;

import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;


@ApplicationPath("/api-v1")
public class BankApplication extends Application {

    public Set<Class<?>> getClasses() {
        Set<Class<?>> s = new HashSet<Class<?>>();
        s.add(it.unisannio.controller.BankController.class);

        return s;
    }
    public Set<Object> getSingletons() {
        Set<Object> s = new HashSet<Object>();
        s.add(new it.unisannio.controller.BankController());
        return s;
    }
}
