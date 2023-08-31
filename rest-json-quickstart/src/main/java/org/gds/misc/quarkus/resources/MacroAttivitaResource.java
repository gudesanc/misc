package org.gds.misc.quarkus.resources;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import org.gds.misc.quarkus.model.MacroAttivita;

import java.util.ArrayList;
import java.util.List;

@Path("/macroattivita")
public class MacroAttivitaResource {
    @GET
    public List<MacroAttivita> getAll(){
        return all;
    }

    @POST
    public List<MacroAttivita> add(MacroAttivita nuova){
        all.add(nuova);
        return all;
    }


    private static List<MacroAttivita> all = new ArrayList<>();
    static {
        all.add(new MacroAttivita("CAP","Commercio su Area Pubblica"));
        all.add(new MacroAttivita("EVI","Esercizio di Vicinato"));
        all.add(new MacroAttivita("ALB","Alberghi"));
        all.add(new MacroAttivita("AVT","Agenzie Viaggi e Turismo"));
    }
}
