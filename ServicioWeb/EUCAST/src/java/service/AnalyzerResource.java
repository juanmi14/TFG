/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.thoughtworks.xstream.XStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletContext;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.PUT;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.GET;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.xml.namespace.QName;
import javax.xml.xquery.XQConnection;
import javax.xml.xquery.XQException;
import javax.xml.xquery.XQExpression;
import javax.xml.xquery.XQSequence;
import logic.*;
import oracle.xml.xquery.OXQDataSource;

/**
 * REST Web Service
 *
 * @author juanmi
 */
@Path("analyzer")
public class AnalyzerResource {

    @Context
    private UriInfo context;
    @Context
    private ServletContext ctx; 

    /**
     * Creates a new instance of AnalyzerResource
     */
    public AnalyzerResource() {
    }

    /**
     * Retrieves representation of an instance of service.AnalyzerResource
     * @return an instance of java.lang.String
     */
    @GET
    @Path("families")
    @Produces(MediaType.APPLICATION_JSON)
    public String getFamilies() {
        try {
            String filePath = ctx.getRealPath(ctx.getInitParameter("EUCAST_file"));
            if(!new File(filePath).exists()){//si no existe el parámetro intento cargar por defecto
                filePath = ctx.getRealPath("/webresources/EUCAST_Breakpoints.xml");
            }
            InputStreamReader is = new InputStreamReader(new FileInputStream(new File(filePath)), "UTF-8");
            OXQDataSource ds = new OXQDataSource();
            XQConnection xqc = ds.getConnection();
            XQExpression xqe;
            XQSequence xqs;
            xqe = xqc.createExpression();
            String query = "declare variable $doc external; ";
            query += "data($doc//Family/@name)";
            xqe.bindDocument(new QName("doc"), is, null, null);
            xqs = xqe.executeQuery(query);
            List<String> fm = new ArrayList();
            while(xqs.next()){
                fm.add(xqs.getAtomicValue());
            }
            //ahora los antibióticos
            query = "declare variable $doc external; ";
            query += "distinct-values(data($doc//Antibiotic/@name))";
            xqs = xqe.executeQuery(query);
            List<String> afm = new ArrayList();
            while(xqs.next()){
                afm.add(xqs.getAtomicValue());
            }
            Gson gson = new Gson();
            JsonElement jFamilies = gson.toJsonTree(fm, new TypeToken<List<String>>(){}.getType());
            JsonElement jAntimicroFamilies = gson.toJsonTree(afm, new TypeToken<List<String>>(){}.getType());
            JsonObject jMain = new JsonObject();
            jMain.add("families", jFamilies);
            jMain.add("antibiotics", jAntimicroFamilies); 
            return gson.toJson(jMain);
        } catch (Exception e) {
            return "{'families':'error'}";
        }
    }
    
    @GET
    @Produces(MediaType.TEXT_HTML)
    public String getHtml() {
        return "<html><head><title>EUCASTBreakpoint</title></head><body>" 
                + "EUCAST Breakpoints analyzer" + "</body></html>";
    }
    
    //el método devolverá un json bien formado si todo es correcto
    //si hay errores de sintaxis o falta de parámetros devolverá el error html correspondiente
    //si no encuentra los parámetros suministrados devolverá un json {"resistance":"not found"}
    //el cliente deberá controlar este caso (se ha elegido pasar el error por json para agilizar
    //las operaciones del cliente)
    @POST
    @Consumes("application/x-www-form-urlencoded")
    @Produces(MediaType.APPLICATION_JSON)
    public Response checkSensibility(@FormParam("family") String familiaBacteriana, 
            @FormParam("antibiotic") String antibiotico,
            @FormParam("diameter") float diametroHalo){
        try {
            //compruebo la existencia del documento xml en webresources
            String filePath = ctx.getRealPath(ctx.getInitParameter("EUCAST_file"));
            if(!new File(filePath).exists()){//si no existe el parámetro intento cargar por defecto
                filePath = ctx.getRealPath("/webresources/EUCAST_Breakpoints.xml");
                if(!new File(filePath).exists())//si no, error
                    return Response.status(404).entity("Source file not found").build();
            }
            //comprobar los parámetros
            if(familiaBacteriana == null || familiaBacteriana.isEmpty() || antibiotico == null || antibiotico.isEmpty())
                return Response.status(400).entity("Incorrect parameters").build();
            
            //creo el objeto Gson que transformará el restultado
            Gson gson = new GsonBuilder()/*.setPrettyPrinting()*/.create();
            //hago la consulta XQuery de la que tengo que obtener:
            //-resistance: resistant, sensitive, neutral, nd
            //-group: [name, link, {notes []}
            //-antimicrobial: [name, link, {notes []}
            //-?family: disk difution?
            OXQDataSource ds = new OXQDataSource();
            XQConnection xqc = ds.getConnection();
            XQExpression xqe;
            XQSequence xqs;
            //query para obtener la sensibilidad
            String query = "declare namespace functx = 'http://www.functx.com'; " 
                    + "declare function functx:is-a-number( $value as xs:anyAtomicType? )  as xs:boolean "
                    + "{ string(number($value)) != 'NaN' }; " 
                    + "declare variable $doc external; ";
            
            query +=  "let $x := $doc//Family[@name='" + familiaBacteriana + "']/AntibioticFamily"
                    + "/Antibiotic[@name='" + antibiotico + "']"
                    + "/ZoneDiameterBreakpoint return "
                    + "if(functx:is-a-number(data($x/S/Value))) "
                        + "then if(" + String.valueOf(diametroHalo)+ " >= data($x/S/Value)) "
                        + "then 'Sensitive' "
                        + "else "
                            + "if(" + String.valueOf(diametroHalo)+ " < data($x/R/Value)) "
                            + "then 'Resistant' "
                            + "else 'Intermedial' "
                    + "else 'Not Defined'";
            System.out.println("------Query: " + query);
            xqe = xqc.createExpression();
//            InputStream is = new FileInputStream(new File(ctx.getRealPath(filePath)));
            InputStreamReader is = new InputStreamReader(new FileInputStream(new File(filePath)), "UTF-8");
            xqe.bindDocument(new QName("doc"), is, null, null);
            xqs = xqe.executeQuery(query);
            String sensibilidad = "ND";
            if(xqs.next()){
//                System.out.println(xqs.getItem().getAtomicValue());
                sensibilidad = xqs.getItemAsString(null);
//                System.out.println(xqs.getAtomicValue());
            }else
                return Response.ok("{\"sensitivity\":\"not found\"}", MediaType.APPLICATION_JSON).build();
            
            //obtengo el grupo de la familia (únicamente con la familia nombrada)
            //es más óptimo hacer dos consultas al xml
            //1-datos de grupo
            //2-datos del antibiótico
            //si no debería parsear tooodo el grupo y luego ignorar los otros antibióticos
            query = "declare variable $doc external; ";
            query += "let $g := $doc//Family[@name='" + familiaBacteriana + "']/AntibioticFamily[Antibiotic/@name='" + antibiotico + "'] ";
            query += "return (data($g/@name), $g/OwnNotes/Note)";
            xqs = xqe.executeQuery(query);
            XStream xstream = new XStream();
            xstream.autodetectAnnotations(Boolean.TRUE);
            xstream.alias("Note", Note.class);
            List<Note> ownNotes = new ArrayList();
            String name = "";
            int i = 0;
            while(xqs.next()){
                i++;
//                System.out.println("["+i+"] - "+xqs.getItemAsString(null));
                if(i==1)
                   name = xqs.getItemAsString(null);
                else{
                    String note = xqs.getItemAsString(null);
                    System.out.println("note["+i+"] - "+note);
                    ownNotes.add((Note)xstream.fromXML(note));
                }
//                grupo = xqs.getItemAsString(null);
            }
            if(i == 0)
                return Response.ok("{\"sensitivity\":\"not found\"}", MediaType.APPLICATION_JSON).build();
            
            //ahora extraigo el antibiótico
            query = "declare variable $doc external; ";
            query += "$doc//Family[@name='" + familiaBacteriana + "']/AntibioticFamily/Antibiotic[@name='" + antibiotico + "'] ";
            xqs = xqe.executeQuery(query);
            String micro = "";
            if(xqs.next())
                micro = xqs.getItemAsString(null);
            else
                return Response.ok("{\"sensitivity\":\"not found\"}", MediaType.APPLICATION_JSON).build();
            xstream = new XStream();
            xstream.autodetectAnnotations(Boolean.TRUE);
            xstream.alias("Antibiotic", Antibiotic.class);
            Antibiotic antimicro = (Antibiotic)xstream.fromXML(micro);
//            System.out.println("ANTIMICRO: " + antimicro.getName() + " - " + antimicro.getLink());
//            
//            System.out.println("Resistencia: " + resistencia);
//            System.out.println("Name: " + name);
//            System.out.println("Notes: " + ownNotes);
            is.close();
            xqc.close();
            
            JsonObject jMain = new JsonObject();
            JsonObject jGrupo = new JsonObject();
            jGrupo.addProperty("name", name);
            JsonElement jownNotes = gson.toJsonTree(ownNotes, new TypeToken<List<Note>>(){}.getType());
            JsonElement jMicro = gson.toJsonTree(antimicro, Antibiotic.class);
            jGrupo.add("ownNotes", jownNotes);
            jMain.add("antibioticFamily", jGrupo);
            jMain.add("antibiotic", jMicro);
            jMain.addProperty("sensitivity", sensibilidad);
            return Response.ok(gson.toJson(jMain), MediaType.APPLICATION_JSON).build();
        } catch (Exception e) {
            System.out.println(">>error>>>" + e.getMessage());
            return Response.status(404).entity("Source file not found").build();
        }
    }
    
    
//    public Response calcularResistencia(@FormParam("name") String name){
//        if(name == null || name.isEmpty())
//            return Response.status(Response.Status.BAD_REQUEST).build();
//        else
//            return Response.ok("{\"name\":\""+ name +"\"}", MediaType.APPLICATION_JSON).build();
//    }

//    /**
//     * PUT method for updating or creating an instance of AnalyzerResource
//     * @param content representation for the resource
//     */
//    @PUT
//    @Consumes(MediaType.APPLICATION_XML)
//    public void putXml(String content) {
//    }
}
