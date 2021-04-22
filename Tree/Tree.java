import java.io.*; 
import java.util.*;
import java.util.HashMap;

class Help {

    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_CYAN = "\u001B[36m";
    public static final String ANSI_RESET = "\u001B[0m";

    public static void usage() {
        System.out.println("[dir]                     :: directory to start traversal [.]");
        System.out.println("-help                     :: display this help and exit [false]");
        System.out.println("-c true|false             :: show entries colorized [true]");
        System.out.println("-d                        :: list directories only [false]");
        System.out.println("-l n                      :: maximum display depth [1]");
        System.out.println("-a                        :: show hidden files [false]");
    }

    public static String empty(int diff) {
        String s = "";
        int i = 0;
        while (i < diff) {
            s += " ";
            i++;
        }
        return s;
    }

    public static void travel(File root, int[] para_list, int diff) {
        String a = Character.toString((char)0x251C);
        String b = Character.toString((char)0x2514);
        String c = Character.toString((char)0x2500);
        boolean col = para_list[1]==1 ? true : false;
        boolean dir_only = para_list[2]==1 ? true : false;
        boolean show_hidden = para_list[4]==1 ? true : false;
        int depth = para_list[3];

        if(depth >= 0) {

        File[] children = root.listFiles();
        Arrays.sort(children);
        int i = 0;
        int size = children.length;
        while(i<size) {
            File child = children[i];
            if(dir_only && (!child.isDirectory())) {
                i++;
                continue;
            }

            if(!show_hidden && child.isHidden()) {
                i++;
                continue;
            }
            String s="";
            if(diff>0) {
                s=Help.empty(diff);
                if(i==size-1) {
                    s+=(b+c);
                } else {
                    s+=(a+c);
                }
            }

            if(col) {
                if(child.canExecute() && !child.isDirectory()) {
                    System.out.println(s+ANSI_RED+child.getName()+ANSI_RESET);
                } else if(child.isDirectory()) {
                    System.out.println(s+ANSI_CYAN +child.getName()+ANSI_RESET);
                } else {
                    System.out.println(s+child.getName());
                }
            } else {
                System.out.println(s+child.getName());
            }
            if(child.isDirectory()){
                int[] para_list2 = new int[para_list.length];
                for(int j=0; j < para_list.length; j++) {
                    para_list2[j]=para_list[j];
                }
                int diff2 = diff;
                para_list2[3]-=1;
                diff2+=1;
                travel(child, para_list2, diff2);
            }

            i++;
        }
    }
    }
}


public class Tree {

    int[] para_list = {
                0, // help
                1, // color
                0, // dir only
                1, // max depth
                0  // show hiden files
            };
    int[] para_checking = {
                0, // help
                0, // color
                0, // dir only
                0, // max depth
                0  // show hiden files
            };



    public File CheckingArgs(String[] args) {

        int size = args.length;
        int i = 0;
        File f = new File(".");

        HashMap<Integer, String> errors = new HashMap<Integer, String>();

        int extra = 20;

        while(i < size) { 
            if(i==0) { //checking dir
                if(!args[0].substring(0,1).equals("-")) { //it is dir
                    File tempF = new File(args[0]);
                    if(tempF.isDirectory()) {
                        f = new File(args[0]);
                        i++;
                        continue;
                    } else {
                        errors.put(1,"\""+args[0]+"\"");
                        i++;
                        continue;
                    }
                } 
            }
            if(args[i].toLowerCase().equals("-help")) {
                para_list[0] = 1;
                para_checking[0]+=1;
                if(para_checking[0] > 1) {errors.put(8,"none");} //help more
            }
            else if(args[i].toLowerCase().equals("-d")) {
                para_list[2] = 1;
                para_checking[2]+=1;
                if(para_checking[2] > 1) {errors.put(9,"none");} //help more
            }
            else if(args[i].toLowerCase().equals("-a")) {
                para_list[4] = 1;
                para_checking[4]+=1;
                if(para_checking[4] > 1) {errors.put(10,"none");} //help more
            }
            else if(args[i].toLowerCase().equals("-c")) {
                para_checking[1]+=1;
                if(para_checking[1] > 1) {errors.put(11,"none");} //help more

                i++;
                if(i>=size){
                    errors.put(2, "none");
                    break;
                } else {
                    if(args[i].toLowerCase().equals("true")) {
                        para_list[1] = 1;
                    } else if(args[i].toLowerCase().equals("false")) {
                        para_list[1] = 0;
                    } else {
                        errors.put(3, "\""+args[i]+"\"");
                    }
                }
                i++;
                continue;
            }
            else if(args[i].toLowerCase().equals("-l")) {
                para_checking[3]+=1;
                if(para_checking[3] > 1) {errors.put(12,"none");} //help more
                i++;
                if(i>=size){
                    errors.put(4, "none");
                    break;
                } else if (!args[i].matches("-?\\d+")) {
                    errors.put(5, "\""+args[i]+"\"");
                } else {
                    para_list[3]=Integer.parseInt(args[i]);
                    if(para_list[3]<1) {
                        errors.put(6, "\""+args[i]+"\"");
                    }
                }
                i++;
                continue;
            }
            else {
                errors.put(extra, "\""+args[i]+"\"");
                extra +=1;
            }
            i++;
        }
        if(errors.isEmpty()) {}
        else {
            int jj = 0;
            System.out.println("\n### We meet some errors with your input:\n============================\n");
            for (Integer ii : errors.keySet()) {
                String Message = errors.get(ii);

            if(ii==1) {
                System.out.println("Error: " + Message + " is an invalid directory.");
            }
            if(ii==2) {
                System.out.println("Error: Missing true|false after option \"-c\"");
            }
            if(ii==3) {
                System.out.println("Error: " + Message + " is not \"true|false\", after option -c.");
            }
            if(ii==4) {
                System.out.println("Error: Missing stuff (integer) after option \"-l\"" );
            }
            if(ii==5) {
                System.out.println("Error: " + Message + " is not integer, after option -l.");
            }
            if(ii==6) {
                System.out.println("Error: " + Message + " is an invalid integer input, after option -l, since it is less than 1.");
            }
            if(ii==8) {
                System.out.println("Error: More than one \"-help\" option were found.");
            }
            if(ii==9) {
                System.out.println("Error: More than one \"-d\" option were found.");
            }
            if(ii==10) {
                System.out.println("Error: More than one \"-a\" option were found.");
            }
            if(ii==11) {
                System.out.println("Error: More than one \"-c\" option were found.");
            }
            if(ii==12) {
                System.out.println("Error: More than one \"-l\" option were found.");
            } 
            if(ii >= 20) {
                System.out.println("Error: " + Message + " is an invalid option");
            }
             jj++;
            }
            System.out.println("\n============================\n### We have " + jj + " errors in total. So, the program exits with error.");
            System.exit(1);
        }
        return f;
    }



    public static void main(String[] args){
        Tree T = new Tree();
        File root = T.CheckingArgs(args);
        if(T.para_list[0]==1) {
            Help.usage();
            System.exit(0);
        }
        T.para_list[3]-=1;
        Help.travel(root,T.para_list,0);
        System.exit(0);
    }
}