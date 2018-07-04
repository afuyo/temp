package com.nereus;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class GraphNode {

    public String name;
    public Integer cardinality;
    public String identifier;
    public Set<String> identifiers;  //these are supersets
    public Set<String> subidentifiers;
    public Set<String> subsets;
    public HashMap<String,HashMap<String,String>> supersets = new HashMap<>();
    public HashMap<String,HashMap<String,String>> sub = new HashMap<>();
    public HashMap<String,HashMap<String,String>> overlaps = new HashMap<>();


    public GraphNode(String name, Set<String> identifiers, Set<String> subsets, Set<String> subidentifiers)
    {
        this.name=name;
        this.identifiers=identifiers;
        this.subsets = subsets;
        this.subidentifiers=subidentifiers;
        this.identifier= setIdentifier();
    }
    public GraphNode(String name){

        this.name=name;
        this.identifiers=new HashSet<>();
        this.subsets= new HashSet<>();
        this.subidentifiers= new HashSet<>();
        this.identifier= setIdentifier();
    }

    public void print ()
    {
        System.out.println("###############GRAPHNODE##############");
        System.out.println("NODE NAME -----> "+name);
        System.out.println("Cardinality "+this.cardinality);
        System.out.println("Identifier is -->" + identifier);
        System.out.println("Unique identifier supersets---->");
        identifiers.forEach((a) -> {
            System.out.println(a);
        });
        System.out.println("Unique identifier subsets------>");
        subidentifiers.forEach((subid)->
        {
            System.out.println(subid);
        });
        System.out.println("List of supersets/subsets/overlaps --->");
        subsets.forEach((a)->{
            System.out.println(a);
        });

    }
    public String setIdentifier(){
        String id = new String();
        if (identifiers.iterator().hasNext())
        {
            id=identifiers.iterator().next();
        } else {id="-1";}
        return id;
    }
}
