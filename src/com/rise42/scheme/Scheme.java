package com.rise42.scheme;

import com.rise42.module.Module;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rise42 on 24/03/17.
 */
public abstract class Scheme {

    protected List<Module> includedModules;

    Scheme(){
        includedModules = new ArrayList<Module>();
    }

    public abstract void addModule(Module module);


}
