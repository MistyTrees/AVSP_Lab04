import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

/**
 * Created by Luka on 30/05/2017.
 */
public class ClosestBlackNode {
    private static final String TESTPATH = "tests/partB/btest2/";
    private static int n;
    private static int e;
    private static ArrayList<Integer> nodes = new ArrayList<>();;

    //Probably using BFS
    public static void main(String[] args) throws IOException {
        //byte[] text = Files.readAllBytes(Paths.get(TESTPATH+"R.in"));
        BufferedReader rd = new BufferedReader(new InputStreamReader(System.in));//new ByteArrayInputStream(text)));//

        String[] lineArr = rd.readLine().split(" ");
        n = Integer.parseInt(lineArr[0]);
        e = Integer.parseInt(lineArr[1]);

        // i-ti redak pokazuje i-tog cvora
        for(int i=0; i<n; i++){
            nodes.add(Integer.parseInt(rd.readLine()));
        }

        HashMap<Integer, ArrayList<Integer>> nodesConnections = new HashMap<Integer, ArrayList<Integer>>();
        Point pair;
        ArrayList<Integer> ls;
        for(int i=0; i<e; i++){
            lineArr= rd.readLine().split(" ");
            pair = new Point(Integer.parseInt(lineArr[0]),Integer.parseInt(lineArr[1]));
            if(!nodesConnections.containsKey(pair.x)){
                nodesConnections.put(pair.x, new ArrayList<>());
            }
            ls = nodesConnections.get(pair.x);
            if(!ls.contains(pair.y)){
                ls.add(pair.y);
            }

            if(!nodesConnections.containsKey(pair.y)){
                nodesConnections.put(pair.y, new ArrayList<>());
            }
            ls = nodesConnections.get(pair.y);
            if(!ls.contains(pair.x)){
                ls.add(pair.x);
            }
        }

        StringBuilder sb = new StringBuilder();
        ArrayList<Node> open, visited, succ;
        Node selected = null;
        int distance, destNode;
        for(int i=0; i<n; i++){
            visited = new ArrayList<>();
            open = new ArrayList<>();
            open.add(new Node(i,0));
            distance = -1;
            destNode = -1;

            while(!open.isEmpty()){
                selected = open.get(0);
                open.remove(0);
                visited.add(selected);

                if(nodes.get(selected.id)==1){
                    distance = selected.dist;
                    destNode = selected.id;
                    break;
                }

                succ = selected.getSucc(nodesConnections);
                for(Node newNodes: succ){
                    if(!open.contains(newNodes) && !visited.contains(newNodes)){
                        open.add(newNodes);
                    }
                    if(open.contains(newNodes)){
                        int ind = open.indexOf(newNodes);
                        if(open.get(ind).dist > newNodes.dist){
                            open.get(ind).dist = newNodes.dist;
                        }
                    }
                }
                Collections.sort(open);
            }
            sb.append(destNode+" "+distance+"\n");
        }

        //String readOut = new String(Files.readAllBytes(Paths.get(TESTPATH+"R.out")));
        //compareResults(sb.toString(),readOut);
        System.out.print(sb.toString());
    }

    private static void compareResults(String calc, String recieved) {
        String[] calculatedLines = calc.split("\n");
        String[] recievedLines = recieved.split("\n");

        //System.out.println("Calculated lines: "+calculatedLines.length+"\nRecieved lines: "+recievedLines.length);
        for(int i=0; i<calculatedLines.length; i++){
            if(calculatedLines[i].compareTo(recievedLines[i]) != 0){
                System.out.println("Node no."+i+" and diff is calc:"+calculatedLines[i]+" vs. rec:"+recievedLines[i]);
            }
        }

        System.out.print(calc.compareTo(recieved.toString())==0 ? "Izlazi su isti" : "Ima greski");
    }

    private static class Node implements Comparable<Node>{
        int dist;
        int id;

        public Node(int id, int dist ) {
            this.dist = dist;
            this.id = id;
        }

        public ArrayList<Node> getSucc(HashMap<Integer, ArrayList<Integer>> nodesConnections){
            ArrayList<Node> res = new ArrayList<>();
            ArrayList<Integer> succs = nodesConnections.get(id);
            for(Integer i : succs){
                res.add(new Node(i,this.dist+1));
            }
            return res;
        }

        @Override
        public boolean equals(Object obj) {
            Node other = (Node) obj;
            return this.id == other.id;
        }

        @Override
        public String toString() {
            return "Node: "+this.id+ ", depth: "+ this.dist;
        }

        @Override
        public int compareTo(Node o) {
            if(this.dist == o.dist){
                return Integer.compare(this.id, o.id);
            }
            return Integer.compare(this.dist, o.dist);
        }
    }
}
