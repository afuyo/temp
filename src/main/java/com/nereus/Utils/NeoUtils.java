package com.nereus.Utils;

import com.nereus.AvroUtils.SchemaGeneratorUtils;
//import org.apache.commons.collections4.Predicate;
import org.javatuples.Sextet;
import org.javatuples.Triplet;
import org.json.JSONObject;
import org.neo4j.driver.v1.AuthTokens;
import org.neo4j.driver.v1.Driver;
import org.neo4j.driver.v1.GraphDatabase;
import org.neo4j.driver.v1.Session;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.*;
import static com.nereus.HLLDB.*;

public class NeoUtils {




   // public static void writeToNeo (HashMap<String,String> schemas )
   public static void writeToNeo ( )
    {
        //Driver driver = GraphDatabase.driver(
        //      "bolt+routing://10.84.4.12:7687", AuthTokens.basic("neo4j", ""));
       // Driver driver = GraphDatabase.driver("bolt://10.84.4.10:7687",
         //       AuthTokens.basic("neo4j", "blockchain"));
        Driver driver = GraphDatabase.driver(
               "bolt://localhost:7687", AuthTokens.basic("neo4j", "wat#rMel0n"));
        Session session = driver.session();
        session.run("MATCH(n) detach delete n");
        NODES.forEach((e)->{
            Map<String, Object> params = new HashMap<>();
            params.put("id", e);
            params.put("name", e);
            params.put("joinStart", getJoinStart(e));
            params.put("joinEnd",getJoinEnd(e));
            params.put("streamStart",getStreamStart(e));
            params.put("streamEnd",getStreamEnd(e));
            params.put("keyName",getKey(e));
            params.put("leftKey",getLeftKey(e));
            params.put("rightKey",getRightKey(e));

            /**System.out.println("&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&");
             System.out.println(e);
             System.out.println("id "+e);
             System.out.println("name "+e);
             System.out.println("joinStart "+getJoinStart(e));
             System.out.println("joinEnd "+getJoinEnd(e));
             System.out.println("streamStart "+getStreamStart(e));
             System.out.println("streamEnd "+getStreamEnd(e));
             System.out.println("keyName "+getKey(e));
             System.out.println("leftKey "+getLeftKey(e));
             System.out.println("rightKey "+getRightKey(e));
             //System.out.println("left key "+getLeftKey(e));**/
            //Result result = graphDb.execute("CREATE (baeldung:Company {name:$name}) " +
            //      "-[:owns]-> (tesla:Car {make: $make, model: $model})" +
            //    "RETURN baeldung, tesla", params);

            //CREATE (:Table {id:row.id, Name: row.Name,joinStart: row.joinStart,
            //      joinEnd: row.joinEnd,streamStart: row.streamStart, streamEnd: row.streamEnd, keyName: row.keyName, leftKey: row.leftKey, rightKey: row.rightKey});

            session.run("CREATE (a:Table {id:$id, name:$name,joinStart:$joinStart,joinEnd:$joinEnd,streamStart:$streamStart,streamEnd:$streamEnd,keyName:$keyName,"+
                    "leftKey:$leftKey,rightKey:$rightKey}) RETURN a ", params);


        });

        session.run("MATCH(m) match(st:Table{streamStart: m.streamStart}) match(est {streamStart: m.streamEnd}) MERGE (st)-[:GROUPBY]->(est)");
        session.run("match(m) match(s:Table {joinStart: m.joinStart}) match(e {joinStart: m.joinEnd}) merge (s)-[:JOIN]->(e)");

       //session.run("match (p1:Table) , (p2:Table) where p1.name= p2.joinStart OR p1.name= p2.joinEnd merge (p1)-[:Join]->(p2)return p1,p2 ");

        STREAMNODES.forEach((e)->{
            Map<String, Object> params = new HashMap<>();
            params.put("id", e);
            params.put("name", e);
            params.put("streamStart", e);
            params.put("streamEnd",e);

            session.run("CREATE(b:Stream{id:$id,name:$name,streamStart:$streamStart,streamEnd:$streamEnd}) RETURN b",params);

        });
        session.run("MATCH(a:Stream), (b:Table) where a.streamStart=b.streamStart create (a)-[r:GROUPBY]->(b)");

        /**Map<String, Object> params = new HashMap<>();
         params.put("id", "baeldung");
         params.put("name", "tesla");
         params.put("joinStart", "modelS");
         params.put("joinEnd","value");

         //Result result = graphDb.execute("CREATE (baeldung:Company {name:$name}) " +
         //      "-[:owns]-> (tesla:Car {make: $make, model: $model})" +
         //    "RETURN baeldung, tesla", params);

         session.run("CREATE (a:Company {name:$name}) " +
         "-[:owns]-> (b:Car {make: $make, model: $model})" +
         "RETURN a, b", params);**/

        // session.run("CREATE (baeldung:Company {name:\"Baeldung\"}) " +
        //       "-[:owns]-> (tesla:Car {make: 'tesla', model: 'modelX'})" +
        //      "RETURN baeldung, tesla");
        session.close();
        driver.close();

    }

    public static void writeToNeo2 ()
    {
        //Driver driver = GraphDatabase.driver(
        //      "bolt+routing://10.84.4.12:7687", AuthTokens.basic("neo4j", ""));
        //  Driver driver = GraphDatabase.driver("bolt://10.84.4.10:7687",
        //        AuthTokens.basic("neo4j", "blockchain"));
        Driver driver = GraphDatabase.driver(
                "bolt://localhost:7687", AuthTokens.basic("neo4j", "wat#rMel0n"));
        Session session = driver.session();
        session.run("MATCH(n) detach delete n");
        NODES.forEach((e)->{
            Map<String, Object> params = new HashMap<>();
            params.put("id", e);
            params.put("name", e);
            //TODO name rename to outputLabel
            params.put("joinStart", getJoinStart2(e));
            params.put("joinEnd",getJoinEnd2(e));
            params.put("streamStart",getStreamStart2(e));
            params.put("streamEnd",getStreamEnd(e));
            params.put("keyName",getKey(e));
            params.put("leftKey",getLeftKey(e));
            params.put("rightKey",getRightKey(e));

            /**System.out.println("&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&");
             System.out.println(e);
             System.out.println("id "+e);
             System.out.println("name "+e);
             System.out.println("joinStart "+getJoinStart(e));
             System.out.println("joinEnd "+getJoinEnd(e));
             System.out.println("streamStart "+getStreamStart(e));
             System.out.println("streamEnd "+getStreamEnd(e));
             System.out.println("keyName "+getKey(e));
             System.out.println("leftKey "+getLeftKey(e));
             System.out.println("rightKey "+getRightKey(e));
             //System.out.println("left key "+getLeftKey(e));**/
            //Result result = graphDb.execute("CREATE (baeldung:Company {name:$name}) " +
            //      "-[:owns]-> (tesla:Car {make: $make, model: $model})" +
            //    "RETURN baeldung, tesla", params);

            //CREATE (:Table {id:row.id, Name: row.Name,joinStart: row.joinStart,
            //      joinEnd: row.joinEnd,streamStart: row.streamStart, streamEnd: row.streamEnd, keyName: row.keyName, leftKey: row.leftKey, rightKey: row.rightKey});

            session.run("CREATE (a:Table {id:$id, name:$name,joinStart:$joinStart,joinEnd:$joinEnd,streamStart:$streamStart,streamEnd:$streamEnd,keyName:$keyName,"+
                    "leftKey:$leftKey,rightKey:$rightKey}) RETURN a ", params);


        });

        //   session.run("MATCH(m) match(st:Table{streamStart: m.streamStart}) match(est {streamStart: m.streamEnd}) MERGE (st)-[:GROUPBY]->(est)");
        //  session.run("match(m) match(s:Table {joinStart: m.joinStart}) match(e {joinStart: m.joinEnd}) merge (s)-[:JOIN]->(e)");
        session.run("MATCH (p1:Table)" + "MATCH (p2:Table) where p1.name= p2.joinStart OR p1.name= p2.joinEnd merge (p1)-[:Join]->(p2)return p1,p2");
        session.run("MATCH (st:Table)\n" +
                "MATCH (est:Table) where st.name = est.streamStart\n" +
                "MERGE (st)-[:GROUPBY]->(est);");

        STREAMNODES.forEach((e)->{
            Map<String, Object> params = new HashMap<>();
            params.put("id", e);
            params.put("name", e);
            params.put("streamStart", null);
            params.put("streamEnd",null);

            session.run("CREATE(b:Stream{id:$id,name:$name,streamStart:$streamStart,streamEnd:$streamEnd}) RETURN b",params);

        });
        // session.run("MATCH(a:Stream), (b:Table) where a.name=b.streamStart create (a)-[r:GROUPBY]->(b)");

        session.run("MATCH(st:Stream) MATCH(est) where st.name=est.streamStart MERGE(st)-[:GROUPBY]->(est)");

        // session.run("CREATE (baeldung:Company {name:\"Baeldung\"}) " +
        //       "-[:owns]-> (tesla:Car {make: 'tesla', model: 'modelX'})" +
        //      "RETURN baeldung, tesla");
        session.close();
        driver.close();

    }

    public static void writeToNeo3 ()
    {
        //Driver driver = GraphDatabase.driver(
        //      "bolt+routing://10.84.4.12:7687", AuthTokens.basic("neo4j", ""));
        // Driver driver = GraphDatabase.driver("bolt://10.84.4.10:7687",
        //        AuthTokens.basic("neo4j", "blockchain"));
        Driver driver = GraphDatabase.driver(
                "bolt://localhost:7687", AuthTokens.basic("neo4j", "wat#rMel0n"));
        Session session = driver.session();
        session.run("MATCH(n) detach delete n");
        Map<String, Object> params = new HashMap<>();
        Iterator<String> iter = NODES.iterator();
        while (iter.hasNext()){
            String e = iter.next();

            params.put("id", e);
            params.put("outputLabel", e);
            params.put("joinStart", getJoinStart2(e));
            params.put("joinEnd",getJoinEnd2(e));
            params.put("streamStart",getStreamStart2(e));
            params.put("streamEnd",getStreamEnd(e));
            params.put("keyName",getKey(e));
            params.put("leftSource",getLeftKey(e));
            params.put("rightSource",getRightKey(e));
            if(!iter.hasNext())
            {params.put("finalOutput","TRUE");}
            else {params.put("finalOutput","");}


            session.run("CREATE (a:Table {id:$id, outputLabel:$outputLabel,joinStart:$joinStart,joinEnd:$joinEnd,streamStart:$streamStart,streamEnd:$streamEnd,keyName:$keyName,"+
                    "leftSource:$leftSource,rightSource:$rightSource,finalOutput:$finalOutput}) RETURN a ", params);

        }
        //   session.run("MATCH(m) match(st:Table{streamStart: m.streamStart}) match(est {streamStart: m.streamEnd}) MERGE (st)-[:GROUPBY]->(est)");
        //  session.run("match(m) match(s:Table {joinStart: m.joinStart}) match(e {joinStart: m.joinEnd}) merge (s)-[:JOIN]->(e)");
        session.run("MATCH (p1:Table)" + "MATCH (p2:Table) where p1.outputLabel= p2.joinStart OR p1.outputLabel= p2.joinEnd merge (p1)-[:Join]->(p2)return p1,p2");
        session.run("MATCH (st:Table)\n" +
                "MATCH (est:Table) where st.outputLabel = est.streamStart\n" +
                "MERGE (st)-[:GROUPBY]->(est);");
        params.clear();
        STREAMNODES.forEach((e)->{
           // Map<String, Object> params = new HashMap<>();

            params.put("id", e);
            params.put("outputLabel", e);
            params.put("streamStart", null);
            params.put("streamEnd",null);

            session.run("CREATE(b:Stream{id:$id,outputLabel:$outputLabel,streamStart:$streamStart,streamEnd:$streamEnd}) RETURN b",params);

        });

        // session.run("MATCH(a:Stream), (b:Table) where a.name=b.streamStart create (a)-[r:GROUPBY]->(b)");

        session.run("MATCH(st:Stream) MATCH(est) where st.outputLabel=est.streamStart MERGE(st)-[:GROUPBY]->(est)");

        // session.run("CREATE (baeldung:Company {name:\"Baeldung\"}) " +
        //       "-[:owns]-> (tesla:Car {make: 'tesla', model: 'modelX'})" +
        //      "RETURN baeldung, tesla");
        session.close();
        driver.close();

    }

    public static void writeToNeo3 (HashMap<String,String> schemas )
    {
        createAvroSchemas(schemas);
        //Driver driver = GraphDatabase.driver(
        //      "bolt+routing://10.84.4.12:7687", AuthTokens.basic("neo4j", ""));
       // Driver driver = GraphDatabase.driver("bolt://10.84.4.10:7687",
         //     AuthTokens.basic("neo4j", "blockchain"));
        Driver driver = GraphDatabase.driver(
               "bolt://localhost:7687", AuthTokens.basic("neo4j", "wat#rMel0n"));
        Session session = driver.session();
        session.run("MATCH(n) detach delete n");
        Map<String, Object> params = new HashMap<>();
        Iterator<String> iter = NODES.iterator();
        while (iter.hasNext()){
            String e = iter.next();

            params.put("id", e);
            params.put("outputLabel", getObjectName(e));
            params.put("objectName",e);
            params.put("joinStart", getJoinStart2(e));
            params.put("joinEnd",getJoinEnd2(e));
            params.put("streamStart",getStreamStart2(e));
            params.put("streamEnd",getStreamEnd(e));
            params.put("keyName",getKey(e));
            params.put("leftSource",getLeftKey(e));
            params.put("rightSource",getRightKey(e));
            if(!iter.hasNext())
            {params.put("finalOutput","TRUE");}
            else {params.put("finalOutput","");}
            params.put("schema",getSchema(e));


            session.run("CREATE (a:Table {id:$id, outputLabel:$outputLabel,joinStart:$joinStart,joinEnd:$joinEnd,streamStart:$streamStart,streamEnd:$streamEnd,keyName:$keyName,"+
                    "leftSource:$leftSource,rightSource:$rightSource,finalOutput:$finalOutput,schema:$schema,objectName:$objectName}) RETURN a ", params);

        }

        session.run("MATCH (p1:Table)" + "MATCH (p2:Table) where p1.objectName= p2.joinStart OR p1.objectName= p2.joinEnd merge (p1)-[:Join]->(p2)return p1,p2");
        session.run("MATCH (st:Table)\n" +
                "MATCH (est:Table) where st.objectName = est.streamStart\n" +
                "MERGE (st)-[:GROUPBY]->(est);");
        params.clear();
        STREAMNODES.forEach((e)->{
            // Map<String, Object> params = new HashMap<>();

            params.put("id", e);
            params.put("outputLabel", e);
            params.put("streamStart", null);
            params.put("streamEnd",null);
            params.put("topicName",streamTableMapping.get(e));

            session.run("CREATE(b:Stream{id:$id,outputLabel:$outputLabel,streamStart:$streamStart,streamEnd:$streamEnd,topicName:$topicName}) RETURN b",params);

        });

        // session.run("MATCH(a:Stream), (b:Table) where a.name=b.streamStart create (a)-[r:GROUPBY]->(b)");

        session.run("MATCH(st:Stream) MATCH(est) where st.outputLabel=est.streamStart MERGE(st)-[:GROUPBY]->(est)");
        //session.run("MATCH(st:Stream) MATCH(tbl:Table) where st.outputLabel=tbl.objectName MERGE(st)-[:GROUPBY]->(est)");

        // session.run("CREATE (baeldung:Company {name:\"Baeldung\"}) " +
        //       "-[:owns]-> (tesla:Car {make: 'tesla', model: 'modelX'})" +
        //      "RETURN baeldung, tesla");

        /***Manual update */
        session.run("MATCH(n{objectName:'STATPEJ.POC_ADDRESS2'}) set n.keyName='[[ADDRESSTIME]]'");
        session.run("MATCH(n{objectName:'STATPEJ.POC_CUSTOMER2'}) set n.keyName='[[CUSTOMERTIME]]'");
        session.run("MATCH(n{objectName:'STATPEJ.POC_POLICY2'}) set n.keyName='[[POLICYSTARTTIME]]'");
        session.run("MATCH(n{objectName:'STATPEJ.POC_CLAIM2'}) set n.keyName='[[CLAIMCOUNTER, CLAIMNUMBER]]'");
        //local
        session.run("MATCH(n{objectName:'Address'}) set n.keyName='[[addresstime]]'");
        session.run("MATCH(n{objectName:'Customer'}) set n.keyName='[[customertime]]'");
        session.run("MATCH(n{objectName:'Policy'}) set n.keyName='[[policystarttime]]'");
        session.run("MATCH(n{objectName:'Claim'}) set n.keyName='[[claimcounter, claimnumber]]'");
        //TODO
        session.close();
        driver.close();

    }
    public static void writeToNeo4  ()
    {
        //createAvroSchemas(schemas);
        //Driver driver = GraphDatabase.driver(
        //      "bolt+routing://10.84.4.12:7687", AuthTokens.basic("neo4j", ""));
        // Driver driver = GraphDatabase.driver("bolt://10.84.4.10:7687",
        //     AuthTokens.basic("neo4j", "blockchain"));
        Driver driver = GraphDatabase.driver(
                "bolt://localhost:7687", AuthTokens.basic("neo4j", "wat#rMel0n"));
        Session session = driver.session();
        session.run("MATCH(n) detach delete n");
        Map<String, Object> params = new HashMap<>();
        Iterator<String> iter = NODES.iterator();
        while (iter.hasNext()){
            String e = iter.next();

            params.put("id", e);
            params.put("outputLabel", e);
            params.put("objectName",e);
            params.put("joinStart", getJoinStart2(e));
            params.put("joinEnd",getJoinEnd2(e));
            params.put("streamStart",getStreamStart2(e));
            params.put("streamEnd",getStreamEnd(e));
            params.put("keyName",getKey(e));
            params.put("leftSource",getLeftKey(e));
            params.put("rightSource",getRightKey(e));
            if(!iter.hasNext())
            {params.put("finalOutput","TRUE");}
            else {params.put("finalOutput","");}
            //params.put("schema",getSchema(e));
            params.put("schema",null);


            session.run("CREATE (a:Table {id:$id, outputLabel:$outputLabel,joinStart:$joinStart,joinEnd:$joinEnd,streamStart:$streamStart,streamEnd:$streamEnd,keyName:$keyName,"+
                    "leftSource:$leftSource,rightSource:$rightSource,finalOutput:$finalOutput,schema:$schema,objectName:$objectName}) RETURN a ", params);

        }

        session.run("MATCH (p1:Table)" + "MATCH (p2:Table) where p1.objectName= p2.joinStart OR p1.objectName= p2.joinEnd merge (p1)-[:Join]->(p2)return p1,p2");
        session.run("MATCH (st:Table)\n" +
                "MATCH (est:Table) where st.objectName = est.streamStart\n" +
                "MERGE (st)-[:GROUPBY]->(est);");
        params.clear();
        STREAMNODES.forEach((e)->{
            // Map<String, Object> params = new HashMap<>();

            params.put("id", e);
            params.put("outputLabel", e);
            params.put("streamStart", null);
            params.put("streamEnd",null);
            params.put("topicName",streamTableMapping.get(e));

            session.run("CREATE(b:Stream{id:$id,outputLabel:$outputLabel,streamStart:$streamStart,streamEnd:$streamEnd,topicName:$topicName}) RETURN b",params);

        });

        // session.run("MATCH(a:Stream), (b:Table) where a.name=b.streamStart create (a)-[r:GROUPBY]->(b)");

        session.run("MATCH(st:Stream) MATCH(est) where st.outputLabel=est.streamStart MERGE(st)-[:GROUPBY]->(est)");
        //session.run("MATCH(st:Stream) MATCH(tbl:Table) where st.outputLabel=tbl.objectName MERGE(st)-[:GROUPBY]->(est)");

        // session.run("CREATE (baeldung:Company {name:\"Baeldung\"}) " +
        //       "-[:owns]-> (tesla:Car {make: 'tesla', model: 'modelX'})" +
        //      "RETURN baeldung, tesla");

        /***Manual update */
        session.run("MATCH(n{objectName:'STATPEJ.POC_ADDRESS2'}) set n.keyName='[[ADDRESSTIME]]'");
        session.run("MATCH(n{objectName:'STATPEJ.POC_CUSTOMER2'}) set n.keyName='[[CUSTOMERTIME]]'");
        session.run("MATCH(n{objectName:'STATPEJ.POC_POLICY2'}) set n.keyName='[[POLICYSTARTTIME]]'");
        session.run("MATCH(n{objectName:'STATPEJ.POC_CLAIM2'}) set n.keyName='[[CLAIMCOUNTER, CLAIMNUMBER]]'");
        //local
        session.run("MATCH(n{objectName:'Address'}) set n.keyName='[[addresstime]]'");
        session.run("MATCH(n{objectName:'Customer'}) set n.keyName='[[customertime]]'");
        session.run("MATCH(n{objectName:'Policy'}) set n.keyName='[[policystarttime]]'");
        session.run("MATCH(n{objectName:'Claim'}) set n.keyName='[[claimcounter, claimnumber]]'");
        //TODO
        session.close();
        driver.close();

    }

    public static void createAvroSchemas(HashMap<String,String> schemas){
        SOURCE_NODES.forEach((e)->
        {
            String tempSchema = SchemaGeneratorUtils.generateGroupBySchema(schemas.get(e),e).toString();
            AVRO_SCHEMAS.put(e,tempSchema);
            //System.out.println(SchemaGeneratorUtils.generateGroupBySchema(schemas.get(e),e));
        });

        JOINORREGROUPTRIPLET.forEach((k,v)->{
           if((boolean) v.getValue2())
            {
                String tmpSchema = SchemaGeneratorUtils.generateGroupBySchema(AVRO_SCHEMAS.get(v.getValue0()),k).toString();
                AVRO_SCHEMAS.put(k,tmpSchema);

            } else {
                String leftNode = v.getValue0().toString();
                String rightNode = v.getValue1().toString();

               String leftSchema = AVRO_SCHEMAS.get(leftNode);
               String rightSchema = AVRO_SCHEMAS.get(rightNode);
               String tmpSchema = SchemaGeneratorUtils.generateJoinedSchema(leftSchema,leftNode,rightSchema,rightNode).toString();
               AVRO_SCHEMAS.put(k,tmpSchema);
            }
        });
       /** System.out.println("JoinTriplet");
        JOINTRIPLET.forEach((k,v)->{
            if(k.equals("STATPEJ.POC_CLAIMPAYMENT21AndSTATPEJ.POC_CLAIM21")){
            System.out.println("Key "+k);
            System.out.println("Value "+v);}
        });
        System.out.println("AVRO_SCHEMAS");
        AVRO_SCHEMAS.forEach((k,v)->{
            System.out.println("Key "+k);
            System.out.println("Value "+v);
        });
        System.out.println("AVRO_SCHEMA "+AVRO_SCHEMAS.size());**/

    }

    public static String getObjectName(String nodeName){

            String line = AVRO_SCHEMAS.get(nodeName);
            JSONObject jsonObject = new JSONObject(line);
           String objectName = jsonObject.getString("name");

        return objectName;
    }
    public static String getSchema(String nodeName)
    {
        return AVRO_SCHEMAS.get(nodeName);
    }

    public static String getJoinStart2(String name){

        String leftName = new String();

        if(JOINSSTARTEND2.get(name)!=null)
        {
            //defaultName = joinsStartEnd.keySet().stream().filter(e->e.equals(name)).findFirst().get().toString();
            leftName = JOINSSTARTEND2.get(name).getValue0().toString();
        }

        return leftName;
    }

    public static String getJoinEnd2(String name){

        String rightName = new String();

        if(JOINSSTARTEND2.get(name)!=null)
        {
            //defaultName = joinsStartEnd.keySet().stream().filter(e->e.equals(name)).findFirst().get().toString();
            rightName = JOINSSTARTEND2.get(name).getValue1().toString();
        }

        return rightName;
    }

    public static String getStreamStart2(String name){

        String start = new String();

        if(TRANSFORMSTARTEND2.get(name)!=null)
        {
            start = TRANSFORMSTARTEND2.get(name);
        } else if (!JOINSSTARTEND2.containsKey(name))
        {
            /**has no groupby relations pointing towards it (first  condition)
             * and not part of join (this condition)
             * should be a source node strem->ktable
             */
            //start=name;
            start= streamTableMapping.get(name);
        }

        return start;
    }



    public static String getKey(String name)
    {
        String key = new String("Missing");

        /** if(septet.get(name)!=null)
         {

         Collection<Sextet<String,Set<String>,Long,String,Set<String>,Long>> septetList= (Collection) septet.get(name);
         key= septetList.stream().findFirst().get().getValue1().toString();

         }
         else if(superset.get(name)!=null)
         {
         Collection<Sextet<String,Set<String>,Long,String,Set<String>,Long>> supersetList= (Collection) superset.get(name);
         key= supersetList.stream().findFirst().get().getValue1().toString();
         }
         else if (subset.get(name) != null)
         {
         Collection<Sextet<String,Set<String>,Long,String,Set<String>,Long>> subsetList= (Collection) subset.get(name);
         key= subsetList.stream().findFirst().get().getValue1().toString();
         }
         else */
        if (IDENTIFIEDBY.get(name) !=null)
        {
            String  idList=
                    IDENTIFIEDBY.get(name).toString();
                    //TODO Workaround to choose single column just to make it work
                    // if node has two keys it just before a join
                   // itwill be joined in the next step. Let's fetch the key from JOINS septet
          /**   ArrayList<Set<String>> listOfSets = (ArrayList<Set<String>>)identifiedBy.get(name);
             //Set<String> setOfStrings = (Set<String>) identifiedBy.get(name);
             int set1Size = listOfSets.size();
            Predicate<Sextet<String, Set<String>, Long, String, Set<String>, Long>> p1 = e->e.getValue0().equals(name);
            Predicate<Sextet<String, Set<String>, Long, String, Set<String>, Long>> p2 = e->e.getValue3().equals(name);
            //Predicate<Sextet<String, Set<String>, Long, String, Set<String>, Long>> p3 = e->setOfStrings.contains(e);

            Collection<Sextet<String, Set<String>, Long, String, Set<String>, Long>> col = (Collection) JOINS.values();
            Sextet<String, Set<String>, Long, String, Set<String>, Long> tmp2;
            col.forEach((e)->{
                System.out.println("###### e "+e.getValue1().toString());
                System.out.println("###### e "+e.getValue4().toString());
                System.out.println(" listOfStrings  "+listOfSets.contains(e.getValue1()));
                System.out.println(" listOfStrings  "+listOfSets.contains(e.getValue4()));
               // System.out.println(" listOfStrings  "+listOfSets.get(1));

                            });

             if(set1Size>1){
                 if (col.stream().anyMatch(p1) || col.stream().anyMatch(p2)) {
                     Sextet<String, Set<String>, Long, String, Set<String>, Long> tmp =
                            // col.stream().filter(e-> e.getValue1().equals(listOfSets.get(0))).findAny().get();
                             col.stream().filter(e->e.getValue0().equals(name)||e.getValue3().equals(name)).findFirst().get();
                    if( tmp.getValue0().equals(name) )
                    { key=tmp.getValue1().toString();}
                    if(tmp.getValue3().equals(name) && listOfSets.contains(tmp.getValue4()))
                    { key = tmp.getValue4().toString();}
                 }
             } else{ key= idList.toString();}**/

             //TODO need proper solution
            key=idList;


        }
      /**  if (identifiedBy2.get(name) !=null)
        {


            key= identifiedBy2.get(name).toString();

        }*/

        /**  if (identifiedBy.get(name) !=null)
         {


         key= identifiedBy.get(name).toString();

         }*/

        return key;
    }

    public static String getLeftKey(String name)
    {
        String key = new String();

        if (JOINS.get(name) != null)
        {
            Collection<Sextet<String,Set<String>,Long,String,Set<String>,Long>> subsetList= (Collection) JOINS.get(name);
            key= subsetList.stream().findAny().get().getValue4().toString();
        }




        return key;
    }

    public static String getRightKey(String name)
    {
        String key = new String();

        if (JOINS.get(name) != null)
        {
            Collection<Sextet<String,Set<String>,Long,String,Set<String>,Long>> subsetList= (Collection) JOINS.get(name);
            key= subsetList.stream().findAny().get().getValue1().toString();
        }




        return key;
    }

    public static String getJoinStart(String name){

        String start = new String();
        String defaultName = name;

        if(JOINSSTARTEND.get(name)!=null)
        {
            start = JOINSSTARTEND.keySet().stream().filter(e->e.equals(name)).findFirst().get().toString();
        }

        return defaultName;
    }

    public static String getJoinEnd(String name){

        String start = new String();

        if(JOINSSTARTEND.get(name)!=null)
        {
            start = JOINSSTARTEND.get(name);
        }

        return start;
    }

    public static String getStreamStart(String name){

        String start = new String();
        String defaultName = name;

        if(TRANSFORMSTARTEND.get(name)!=null)
        {
            start = TRANSFORMSTARTEND.keySet().stream().filter(e->e.equals(name)).findFirst().get().toString();
        }

        return defaultName;
    }

    public static String getStreamEnd(String name){

        String end = new String();

        if(TRANSFORMSTARTEND.get(name)!=null)
        {
            end = TRANSFORMSTARTEND.get(name);
        }

        return end;
    }

    public static void writeToCSV  () throws FileNotFoundException
    {
        //write primary nodes first
        //Streams
        writeToCSVStreams();
        PrintWriter pw = new PrintWriter(new File("/tmp/tables2.csv"));

        HashMap<String, ArrayList> schema = new HashMap<>();
        ArrayList<HashMap<String,String>> props = new ArrayList<>();
        HashMap<String,String> map = new HashMap<>();

        //map.put("name","")


        StringBuilder sb = new StringBuilder();
        sb.append("id");
        sb.append(',');
        sb.append("Name");
        sb.append(',');
        sb.append("joinStart");
        sb.append(',');
        sb.append("joinEnd");
        sb.append(',');
        sb.append("streamStart");
        sb.append(',');
        sb.append("streamEnd");
        sb.append(',');
        sb.append("keyName");
        sb.append(',');
        sb.append("leftKey");
        sb.append(',');
        sb.append("rightKey");



        sb.append('\n');




        //Collections.reverse(nodes);
        NODES.forEach((e)->{
            sb.append(e.toString());
            sb.append(',');
            sb.append(e.toString());
            sb.append(',');
            sb.append(getJoinStart(e.toString()));
            sb.append(',');
            sb.append(getJoinEnd(e.toString()));
            sb.append(',');
            sb.append(getStreamStart(e.toString()));
            sb.append(',');
            sb.append(getStreamEnd(e.toString()));
            sb.append(',');
            sb.append(getKey(e.toString()).toString());
            sb.append(',');
            sb.append(getKey(e.toString()));
            sb.append(',');
            sb.append(getLeftKey(e.toString()));
            sb.append(',');
            sb.append(getRightKey(e.toString()));

            sb.append('\n');

        });


        pw.write(sb.toString());
        pw.close();
        System.out.println(sb.toString());

    }
    public static void writeToCSVStreams  () throws FileNotFoundException
    {
        PrintWriter pw = new PrintWriter(new File("/tmp/streams2.csv"));

        StringBuilder sb = new StringBuilder();
        sb.append("id");
        sb.append(',');
        sb.append("Name");
        sb.append(',');
        sb.append("streamStart");
        sb.append(',');
        sb.append("streamEnd");

        sb.append('\n');

        STREAMNODES.forEach((e)->{
            sb.append(e.toString());
            sb.append(',');
            sb.append(e.toString());
            sb.append(',');
            sb.append(e.toString());
            sb.append(',');
            sb.append(e.toString());
            sb.append('\n');
        });
        pw.write(sb.toString());
        pw.close();
    }

    private static void mapAvroSchemas(String node, Triplet transformedTuple) {
        //System.out.println("Key " + k);
        //System.out.println("Value " + v);

    }
}
