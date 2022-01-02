package GraphGUI;

import Graph.MyDWG;
import Graph.MyDWG_Algo;
import Graph.MyNode;
import api.EdgeData;
import api.NodeData;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Line2D;
import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;

public class GUI extends JFrame implements ActionListener {
    JPanel functionPanel;
    JButton centerb;
    JButton clear;
    JButton save;
    JButton shortestpathb;
    JButton removenode;
    JButton selectFile;
    JButton TSPb;
    JButton Generateb;
    JButton enter;
    JButton isConnected;
    JTextField tsplist;
    JTextField src;
    JTextField dst;
    JTextField node;
    JTextField nodessize;
    JButton enterNodes;
    JPanel pop;
    JFrame popup;
    JPanel pop1;
    JFrame popup1;
    JFrame connectedpop;
    MyDWG_Algo g1;
    MyDWG_Algo og;
    int source;
    int destination;
    NodeData center;
    NodeData startpoint;
    NodeData endpoint;
    ArrayList<NodeData> path;
    ArrayList<NodeData> tsppath;
    int centercounter = 0;


    public GUI(MyDWG gr) {
        Dimension size = Toolkit.getDefaultToolkit().getScreenSize();
        JPanel p = new JPanel(new BorderLayout());//3,1
        functionPanel = new JPanel();
        functionPanel.setBackground(Color.lightGray);
        functionPanel.setBounds(size.width / 2 - 150, 0, 150, size.width / 2);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLocation((size.width/2-this.getSize().width)/2, (size.height/2-this.getSize().height)/3);
        this.setTitle("Ex2-Graph-GUI");
        int width = (int) size.width;
        int height = (int) size.height;
        this.setSize(width / 2, width / 2);
        this.setResizable(false);
        src = new JTextField();
        src.setPreferredSize(new Dimension(50, 25));
        dst = new JTextField();
        dst.setPreferredSize(new Dimension(50, 25));
        node = new JTextField();
        node.setPreferredSize(new Dimension(75, 25));
        clear = new JButton("Clear");
        save = new JButton("Save Graph");
        centerb = new JButton("Center");
        removenode = new JButton("Remove Node");
        shortestpathb = new JButton("Shortest Path");
        selectFile = new JButton("Load Graph");
        Generateb = new JButton("Generate Graph");
        TSPb = new JButton("TSP");
        isConnected = new JButton("isConnected");
        isConnected.addActionListener(this);
        selectFile.addActionListener(this); // Adding select button to actionPreformed.
        shortestpathb.addActionListener(this);
        centerb.addActionListener(this);
        removenode.addActionListener(this);
        clear.addActionListener(this);
        save.addActionListener(this);
        TSPb.addActionListener(this);
        Generateb.addActionListener(this);
        this.setLayout(new BorderLayout(1,3));
        this.add(functionPanel);
        this.add(p, BorderLayout.WEST);
        functionPanel.add(shortestpathb);
        functionPanel.add(src,BorderLayout.EAST);
        functionPanel.add(dst,BorderLayout.WEST);
        functionPanel.add(removenode);
        functionPanel.add(node);
        functionPanel.add(centerb);
        functionPanel.add(selectFile);
        functionPanel.add(save);
        functionPanel.add(clear);
        functionPanel.add(TSPb);
        functionPanel.add(isConnected);
        functionPanel.add(Generateb);
        this.add(new GraphP(gr)); //// FIX
        this.setVisible(true);
        this.setTitle("Ex2 - UI");
        this.setResizable(false);


    }

    public class GraphP extends JPanel {
        double minx;
        double miny;
        double maxx;
        double maxy;
        double scalefactor = 1;
        double scalefactor1 = 8;

        public GraphP(MyDWG gr) {
            g1 = new MyDWG_Algo();
            og = new MyDWG_Algo();

            g1.init(gr);
            og.init(g1.copy());
            try {
                setminxy();
            } catch (Exception e) {
                e.printStackTrace();
            }
            repaint();

        }

        public void setminxy() throws Exception {
            minx = Integer.MAX_VALUE;
            miny = Integer.MAX_VALUE;
            maxx = Integer.MIN_VALUE;
            maxy = Integer.MIN_VALUE;
            Iterator<NodeData> minxit = g1.getGraph().nodeIter();
            while (minxit.hasNext()) {
                NodeData n = minxit.next();
                if (n.getLocation().x() < minx) {
                    minx = n.getLocation().x();
                }
            }
            Iterator<NodeData> minyit = g1.getGraph().nodeIter();
            while (minyit.hasNext()) {
                NodeData m = minyit.next();
                if (m.getLocation().y() < miny) {
                    miny = m.getLocation().y();
                }
            }
            Iterator<NodeData> maxxit = g1.getGraph().nodeIter();
            while (maxxit.hasNext()) {
                NodeData n = maxxit.next();
                if (n.getLocation().x() > maxx) {
                    maxx = n.getLocation().x();
                }
            }
            Iterator<NodeData> maxyit = g1.getGraph().nodeIter();
            while (maxyit.hasNext()) {
                NodeData m = maxyit.next();
                if (m.getLocation().y() > maxy) {
                    maxy = m.getLocation().y();
                }
            }


        }

        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            double ABSx = Math.abs(minx - maxx);
            double ABSy = Math.abs(miny - maxy);
            double scalex = (getWidth() / ABSx) * 0.7;
            double scaley = (getHeight() / ABSy) * 0.7;

            MyNode prev = null;
            try {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON);
                double theta;
                Iterator<NodeData> it = g1.getGraph().nodeIter();
                while (it.hasNext()) {
                    NodeData n = it.next();
                    g.setColor(new Color(11, 148, 56, 255));
                    double x = (n.getLocation().x() - minx) * scalex * 0.97 + 30;
                    double y = (n.getLocation().y() - miny) * scaley * 0.97 + 30;
                    String xs = "" + n.getLocation().x();
                    String ys = "" + n.getLocation().y();
                    String coord = "(" + xs + "," + ys + ")" + ", id:" + n.getKey();
                    g.fillOval((int) x - 2, (int) y - 2, 20, 20);
                    g.setColor(new Color(0, 0, 0));
                    Font f = new Font("ariel", Font.BOLD, 16);
                    g.setFont(f);
                    g.drawString(n.getKey() + "", (int) x, (int) y - 10);

                }
                if (center != null) {
                    double x = (center.getLocation().x() - minx) * scalex * 0.97 + 30;
                    double y = (center.getLocation().y() - miny) * scaley * 0.97 + 30;
                    g.setColor(new Color(255, 0, 243));
                    g.fillOval((int) x - 2, (int) y - 2, 20, 20);
                    g.setColor(new Color(0, 0, 0));
                    g.drawString("CENTER", (int) x - 10, (int) y + 50);
                }
                if (startpoint != null) {
                    double x = (startpoint.getLocation().x() - minx) * scalex * 0.97 + 30;
                    double y = (startpoint.getLocation().y() - miny) * scaley * 0.97 + 30;
                    g.setColor(new Color(25, 0, 255));
                    g.fillOval((int) x - 2, (int) y - 2, 20, 20);
                    g.setColor(new Color(0, 0, 0));
                    g.drawString("START POINT", (int) x - 10, (int) y + 50);
                }
                if (endpoint != null) {
                    double x = (endpoint.getLocation().x() - minx) * scalex * 0.97 + 30;
                    double y = (endpoint.getLocation().y() - miny) * scaley * 0.97 + 30;
                    g.setColor(new Color(25, 0, 255));
                    g.fillOval((int) x - 2, (int) y - 2, 20, 20);
                    g.setColor(new Color(0, 0, 0));
                    g.drawString("END POINT", (int) x - 10, (int) y + 50);
                }
                Iterator<EdgeData> eiter = g1.getGraph().edgeIter();
                while (eiter.hasNext()) {
                    EdgeData e = eiter.next();
                    double srcx = (g1.getGraph().getNode(e.getSrc()).getLocation().x() - minx) * scalex + 30;
                    double srcy = (g1.getGraph().getNode(e.getSrc()).getLocation().y() - miny) * scaley + 30;
                    double destx = (g1.getGraph().getNode(e.getDest()).getLocation().x() - minx) * scalex + 30;
                    double desty = (g1.getGraph().getNode(e.getDest()).getLocation().y() - miny) * scaley + 30;
                    g.setColor(new Color(0, 0, 0));
                    int x1 = (int) srcx;
                    int y1 = (int) srcy;
                    int x2 = (int) destx;
                    int y2 = (int) desty;
                    g2.setStroke(new BasicStroke(1));
                    g2.draw(new Line2D.Double(x1, y1, x2, y2));
                    theta = Math.atan2(y2 - y1, x2 - x1);
                    g.setColor(new Color(127, 30, 30));
                    drawArrow(g2, theta, x2, y2);
                    x1 = (int) srcx + (int) (scalex / scalefactor1);
                    y1 = (int) srcy + (int) (scaley / scalefactor1);
                    x2 = (int) destx + (int) (scalex / scalefactor1);
                    y2 = (int) desty + (int) (scaley / scalefactor1);
                }
                if (path != null) {
                    for (int i = 0; i < path.size() - 1; i++) {
                        EdgeData curredge = g1.getGraph().getEdge(path.get(i).getKey(), path.get(i + 1).getKey());
                        double srcx = (g1.getGraph().getNode(curredge.getSrc()).getLocation().x() - minx) * scalex + 30;
                        double srcy = (g1.getGraph().getNode(curredge.getSrc()).getLocation().y() - miny) * scaley + 30;
                        double destx = (g1.getGraph().getNode(curredge.getDest()).getLocation().x() - minx) * scalex + 30;
                        double desty = (g1.getGraph().getNode(curredge.getDest()).getLocation().y() - miny) * scaley + 30;
                        int x1 = (int) srcx;
                        int y1 = (int) srcy;
                        int x2 = (int) destx;
                        int y2 = (int) desty;
                        g.setColor(new Color(65, 255, 0));
                        g2.draw(new Line2D.Double(x1, y1, x2, y2));
                        g.setColor(new Color(0, 0, 0));
                        ((Graphics2D) g).setStroke(new BasicStroke(5));
                        g2.drawString(curredge.getWeight() + "", (x1 + x2) / 2, (y1 + y2) / 2);

                    }
                    g2.drawString("Shortest Path Distance: "+g1.shortestPathDist(source,destination),5,this.getWidth()-50);
                }
                if (tsppath != null) {
                    for (int i = 0; i < tsppath.size() - 1; i++) {
                        EdgeData curredge = g1.getGraph().getEdge(tsppath.get(i).getKey(), tsppath.get(i + 1).getKey());
                        double srcx = (g1.getGraph().getNode(curredge.getSrc()).getLocation().x() - minx) * scalex + 30;
                        double srcy = (g1.getGraph().getNode(curredge.getSrc()).getLocation().y() - miny) * scaley + 30;
                        double destx = (g1.getGraph().getNode(curredge.getDest()).getLocation().x() - minx) * scalex + 30;
                        double desty = (g1.getGraph().getNode(curredge.getDest()).getLocation().y() - miny) * scaley + 30;
                        int x1 = (int) srcx;
                        int y1 = (int) srcy;
                        int x2 = (int) destx;
                        int y2 = (int) desty;
                        g.setColor(new Color(255, 0, 0));
                        g2.draw(new Line2D.Double(x1, y1, x2, y2));
                        g.setColor(new Color(0, 0, 0));
                        ((Graphics2D) g).setStroke(new BasicStroke(5));
                        //g2.drawString(curredge.getWeight() + "", (x1 + x2) / 2, (y1 + y2) / 2);

                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == centerb) {
            try {
                if (centercounter % 2 == 0) {
                    NodeData a = g1.center();
                    this.center = a;
                    centercounter++;
                    repaint();
                } else {
                    this.center = null;
                    centercounter++;
                    repaint();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } else if (e.getSource() == shortestpathb) {
            if (src.getText().length() == 0 || dst.getText().length() == 0) {
                path = null;
                repaint();
            } else {
                source = Integer.parseInt(src.getText());
                destination = Integer.parseInt(dst.getText());
                path = (ArrayList<NodeData>) g1.shortestPath(source, destination);
                repaint();
            }
        } else if (e.getSource() == removenode) {
            if (node.getText().length() == 0) {
                g1.init(og.copy());
                repaint();
            } else {
                int rmvnode = Integer.parseInt(node.getText());
                this.g1.getGraph().removeNode(rmvnode);
                if(center!=null&&center.getKey()==rmvnode){
                    center=null;
                    centercounter=0;
                }
                repaint();
            }
        } else if (e.getSource() == selectFile) {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setCurrentDirectory(new File("."));
            int response = fileChooser.showOpenDialog(null); // select file to Open.
            if (response == JFileChooser.APPROVE_OPTION) {
                String jsonPath = fileChooser.getSelectedFile().getAbsolutePath();
                this.g1.load(jsonPath);
                try {
                    runGUI((MyDWG) this.g1.getGraph());
                    setVisible(false); //you can't see me!
                    dispose();

                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        } else if (e.getSource() == clear) {
            this.center = null;
            centercounter = 0;
            path = null;
            g1.init(og.copy());
            tsppath=null;
            this.startpoint=null;
            this.endpoint=null;
            repaint();
        } else if (e.getSource() == save) {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setCurrentDirectory(new File("."));
            int response = fileChooser.showSaveDialog(null); // select file to Open.
            if (response == JFileChooser.APPROVE_OPTION) {
                String jsonPath = fileChooser.getSelectedFile().getAbsolutePath();
                this.g1.save(jsonPath);
            }
        } else if (e.getSource()==TSPb) {
            popup = new JFrame("TSP");

            pop = new JPanel();
            popup.setContentPane(pop);
            pop.setBackground(Color.lightGray);
            popup.setBackground(Color.lightGray);
            popup.setLocationRelativeTo(null);
            //popup.pack();
            Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
            popup.setSize((int)d.getWidth()/3,(int)d.getHeight()/5);
            popup.setResizable(false);
            popup.setVisible(true);
            tsplist = new JTextField();
            enter = new JButton("Enter Cities");
            enter.setPreferredSize(new Dimension(400,40));
            JLabel inst = new JLabel("enter cities in this format 1,2,3,4");
            popup.add(enter);
            popup.add(inst);
            enter.addActionListener(this);
            popup.setResizable(true);
            tsplist.setPreferredSize(new Dimension(150, 30));
            popup.add(tsplist);
        }else if(e.getSource()==enter){
            String cities = tsplist.getText();
            popup.setVisible(false);
            popup.dispose();
            String[]list = cities.split(",");
            tsppath = new ArrayList<>();

            ArrayList<NodeData> Cities = new ArrayList<>();
            for(int i=0;i<list.length;i++){
                Cities.add(this.g1.getGraph().getNode(Integer.parseInt(list[i])));
            }
            Cities=(ArrayList)this.g1.tsp(Cities);

            for(int i =0;i<Cities.size()-1;i++){
                ArrayList<NodeData> tmp =(ArrayList)this.g1.shortestPath(Cities.get(i).getKey(),Cities.get(i+1).getKey());
                if(i!=Cities.size()-2){
                    tmp.remove(tmp.get(tmp.size()-1));
                }
                for(int k=0;k<tmp.size();k++){
                    tsppath.add(tmp.get(k));
                }
                startpoint = tsppath.get(0);
                endpoint = tsppath.get(tsppath.size()-1);
            }
            repaint();

        }else if(e.getSource()==isConnected){
            connectedpop = new JFrame();
            Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
            connectedpop.setSize((int)d.getWidth()/3,(int)d.getHeight()/10);
            connectedpop.setResizable(false);
            try {
                if(this.g1.isConnected()){
                    JLabel message = new JLabel("This graph is strongly connected");
                    JPanel a = new JPanel();
                    message.setFont(new Font("Verdana", Font.BOLD, 25));
                    connectedpop.setContentPane(a);
                    message.setSize(400,400);
                    connectedpop.setLocationRelativeTo(null);
                    a.add(message);
                }else{
                    JLabel message = new JLabel("This graph is not strongly connected");
                    JPanel a = new JPanel();
                    connectedpop.setContentPane(a);
                    message.setFont(new Font("Verdana", Font.BOLD, 25));
                    message.setSize(400,400);
                    connectedpop.setLocationRelativeTo(null);
                    a.add(message);
                }
                connectedpop.setVisible(true);
            } catch (Exception ex) {
                ex.printStackTrace();
            }


        }else if(e.getSource()==Generateb){
            popup1 = new JFrame("Generate Graph");
            pop1 = new JPanel();
            popup1.setContentPane(pop1);
            pop1.setBackground(Color.lightGray);
            popup1.setBackground(Color.lightGray);
            popup1.setLocationRelativeTo(null);
            //popup.pack();
            Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
            popup1.setSize((int)d.getWidth()/3,(int)d.getHeight()/5);
            popup1.setResizable(false);
            popup1.setVisible(true);
            nodessize = new JTextField();
            enterNodes = new JButton("Generate");
            enterNodes.setPreferredSize(new Dimension(400,40));
            JLabel inst = new JLabel("Enter Vertex Size");
            popup1.add(enterNodes);
            popup1.add(inst);
            enterNodes.addActionListener(this);
            popup1.setResizable(true);
            nodessize.setPreferredSize(new Dimension(150, 30));
            popup1.add(nodessize);

        }else if(e.getSource()==enterNodes){
            this.center = null;
            centercounter = 0;
            path = null;
            this.g1.generateGraph(Integer.parseInt(nodessize.getText()),1);
            tsppath=null;
            this.startpoint=null;
            this.endpoint=null;
            this.setVisible(false);
            this.dispose();
            new GUI((MyDWG) this.g1.getGraph());
            popup1.setVisible(false);
            popup1.dispose();

        }
    }

    // based on code from https://coderanch.com/t/339505/java/drawing-arrows
    private void drawArrow(Graphics2D g2, double theta, double x0, double y0) {
        double barb = 20;
        double phi = Math.PI / 6;
        double x = x0 - barb * Math.cos(theta + phi);
        double y = y0 - barb * Math.sin(theta + phi);
        g2.setStroke(new BasicStroke(3));
        g2.draw(new Line2D.Double(x0, y0, x, y));
        x = x0 - barb * Math.cos(theta - phi);
        y = y0 - barb * Math.sin(theta - phi);
        g2.draw(new Line2D.Double(x0, y0, x, y));
    }

    public static void runGUI(MyDWG gr) {
        new GUI(gr);
    }

    public static void main(String[] args){
        if(args.length!=0){
            MyDWG_Algo alg = new MyDWG_Algo();
            alg.load(args[0]);
            runGUI((MyDWG)alg.getGraph());
        }else{
            runGUI(null);
        }
    }
}
