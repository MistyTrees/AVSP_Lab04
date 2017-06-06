import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by Luka on 30/05/2017.
 */
public class NodeRank {
    private static final String TESTPATH = "tests/partA/btest2/";
    private static List<List<Integer>> nodes;
    private static List<Integer> degrees;
    private static int n;
    private static double beta;

    //NodeRank algorithm
    public static double[] calculateNodeRank(double[] r){
        double[] rnew = new double[n];
        for(int i=0; i<n; i++){
            rnew[i] = ((1-beta)/n);
        }

        int di;
        List<Integer> nodeEnds;
        for(int i=0; i<n; i++){
            di = degrees.get(i);
            nodeEnds = nodes.get(i);
            for(int j=0; j<di ; j++){
                rnew[nodeEnds.get(j)] += ((beta*r[i])/di);
            }
        }

        return rnew;
    }

    public static void main(String[] args) throws IOException {
        //byte[] text = Files.readAllBytes(Paths.get(TESTPATH+"R.in"));
        BufferedReader rd = new BufferedReader(new InputStreamReader(System.in));//new ByteArrayInputStream(text)));//

        String[] lineArr = rd.readLine().split(" ");
        n = Integer.parseInt(lineArr[0]);
        beta = Double.parseDouble(lineArr[1]);

        nodes = new ArrayList<>();
        degrees = new ArrayList<>();
        // i-ti redak pokazuje izlazne bridove i-tog cvora
        for(int i=0; i<n; i++){
            lineArr = rd.readLine().split(" ");
            degrees.add(lineArr.length);
            nodes.add(Stream.of(lineArr)
                            .map(String::trim)
                            .map(Integer::parseInt)
                            .collect(Collectors.toList()));
        }
        int Q = Integer.parseInt(rd.readLine());

        int ni,ti;
        double[] rnew;
        HashMap<Integer,ArrayList<Integer>> timelines = new HashMap<Integer,ArrayList<Integer>>();
        ArrayList<Double> linesvalues = new ArrayList<>();
        ArrayList<Integer> linesns = new ArrayList<>();
        long maxT = 0;
        ArrayList<Integer> lins;
        for(int i=0; i<Q; i++){
            lineArr = rd.readLine().split(" ");
            ni = Integer.parseInt(lineArr[0]);
            ti = Integer.parseInt(lineArr[1]);

            if(ti > maxT){
                maxT = ti;
            }
            if(!timelines.containsKey(ti)){
                timelines.put(ti,new ArrayList<>());
            }
            lins = timelines.get(ti);
            lins.add(i);

            linesvalues.add(0.0);
            linesns.add(ni);
        }

        rnew = new double[n];
        for(int j=0; j<n; j++){
            rnew[j] = (double)1/n;
        }

        for (int pon = 1; pon <= maxT; pon++){
            rnew = calculateNodeRank(rnew);
            if(timelines.containsKey(pon)){
                for(Integer l: timelines.get(pon)){
                    linesvalues.set(l,rnew[linesns.get(l)]);
                }
            }
        }

        NumberFormat f = new DecimalFormat("#0.0000000000");
        StringBuilder sb = new StringBuilder();
        for(int i=0;i<linesvalues.size();i++){
            sb.append(f.format(linesvalues.get(i))).append("\n");
        }

        //String readPut = new String(Files.readAllBytes(Paths.get(TESTPATH+"R.out")));
        //compareResults(sb.toString(), readPut);
        System.out.print(sb.toString());
    }

    private static void compareResults(String calc, String recieved) {
        String[] calculatedLines = calc.split("\n");
        String[] recievedLines = recieved.split("\n");

        //System.out.println("Calculated lines: "+calculatedLines.length+"\nRecieved lines: "+recievedLines.length);
        for(int i=0; i<calculatedLines.length; i++){
            if(calculatedLines[i].compareTo(recievedLines[i]) != 0){
                System.out.println("line no."+i+" and diff is calc:"+calculatedLines[i]+" vs. rec:"+recievedLines[i]);
            }
        }

        System.out.print(calc.compareTo(recieved.toString())==0 ? "Izlazi su isti" : "Ima greski");
    }
}
