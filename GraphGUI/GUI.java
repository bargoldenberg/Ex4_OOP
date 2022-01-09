package GraphGUI;

import Graph.MyDWG;
import Graph.MyDWG_Algo;
import Graph.MyNode;
import api.EdgeData;
import api.NodeData;
import src.Agents;
import src.Pokemons;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Line2D;
import java.util.Iterator;

public class GUI extends JFrame {
    MyDWG_Algo g1;
    MyDWG_Algo og;
    Pokemons pokemons;
    Agents agents;
    double score =0;
    double time = 0;
    int FLAG=0;
    public GUI(MyDWG gr, Pokemons poke, Agents agen) {
        Dimension size = Toolkit.getDefaultToolkit().getScreenSize();
        JPanel p = new JPanel(new BorderLayout());//3,1
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLocation((size.width/2-this.getSize().width)/2, (size.height/2-this.getSize().height)/3);
        this.setTitle("Ex4-Pokemon-GUI");
        int width = (int) (size.width/1.2);
        int height = (int) (size.height/1.2);
        this.setSize(width , height );
        this.setResizable(true);
        GraphP gp = new GraphP(gr,poke,agen);
        this.add(gp);
        this.setVisible(true);
        this.setTitle("Ex4 - UI");



    }
    public int getFLAG(){
        return FLAG;
    }
    public void updateScreen(Pokemons poke, Agents agents, double score, double time){
        this.pokemons=poke;
        this.agents=agents;
        this.score=score;
        this.time=time;
        repaint();
    }
    public class GraphP extends JPanel implements ActionListener {
        double minx;
        double miny;
        double maxx;
        double maxy;
        double scalefactor1 = 8;
        JButton stop;
        public GraphP(MyDWG gr,Pokemons poke,Agents agen) {
            g1 = new MyDWG_Algo();
            og = new MyDWG_Algo();
            pokemons =poke;
            agents=agen;
            stop = new JButton();
            stop.setText("STOP");
            stop.setBounds(5,30,400,200);
            stop.addActionListener(this);
            this.add(stop);
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
            Toolkit t1 = Toolkit.getDefaultToolkit();
            Image im1;
            im1= t1.getImage("GraphGUI/background.jpg");
            g.drawImage(im1, 0, 0, getWidth(), getHeight(), this);
            double ABSx = Math.abs(minx - maxx);
            double ABSy = Math.abs(miny - maxy);
            double scalex = (getWidth() / ABSx) * 0.8;
            double scaley = (getHeight() / ABSy) * 0.8;
            Font f = new Font("ariel", Font.BOLD, 20);
            g.setFont(f);
            g.drawString("Score: "+score , (int) 5, (int) 20);
            g.drawString("Time to end: "+time,5,45);
            MyNode prev = null;
            try {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON);
                double theta;
                Iterator<NodeData> it = g1.getGraph().nodeIter();
                while (it.hasNext()) {
                    NodeData n = it.next();
                    g.setColor(new Color(0, 19, 115, 255));
                    double x = (n.getLocation().x() - minx) * scalex * 0.97 + 70;
                    double y = (n.getLocation().y() - miny) * scaley * 0.97 + 70;
                    String xs = "" + n.getLocation().x();
                    String ys = "" + n.getLocation().y();
                    String coord = "(" + xs + "," + ys + ")" + ", id:" + n.getKey();
                    g.fillOval((int) x-10, (int) y-10, 20, 20);
                    g.setColor(new Color(0, 0, 0));
                    f = new Font("ariel", Font.BOLD, 16);
                    g.setFont(f);
                    g.drawString(n.getKey() + "", (int) x, (int) y - 10);

                }
                Iterator<EdgeData> eiter = g1.getGraph().edgeIter();
                while (eiter.hasNext()) {
                    EdgeData e = eiter.next();
                    double srcx = (g1.getGraph().getNode(e.getSrc()).getLocation().x() - minx) * scalex*0.97 + 70;
                    double srcy = (g1.getGraph().getNode(e.getSrc()).getLocation().y() - miny) * scaley*0.97 + 70;
                    double destx = (g1.getGraph().getNode(e.getDest()).getLocation().x() - minx) * scalex*0.97 + 70;
                    double desty = (g1.getGraph().getNode(e.getDest()).getLocation().y() - miny) * scaley*0.97 + 70;
                    g.setColor(new Color(0, 0, 0));
                    int x1 = (int) srcx;
                    int y1 = (int) srcy;
                    int x2 = (int) destx;
                    int y2 = (int) desty;
                    g2.setStroke(new BasicStroke(1));
                    g2.draw(new Line2D.Double(x1, y1, x2, y2));
                    //theta = Math.atan2(y2 - y1, x2 - x1);
                    g.setColor(new Color(127, 30, 30));
                   // drawArrow(g2, theta, x2, y2);
                    x1 = (int) srcx + (int) (scalex / scalefactor1);
                    y1 = (int) srcy + (int) (scaley / scalefactor1);
                    x2 = (int) destx + (int) (scalex / scalefactor1);
                    y2 = (int) desty + (int) (scaley / scalefactor1);
                }
                if(pokemons!=null) {
                    Toolkit t = Toolkit.getDefaultToolkit();
                    Image im;
                    for (int i = 0; i < pokemons.GetPokeList().size(); i++) {
                        g.setColor(new Color(255, 0, 0, 255));
                        double x = (pokemons.GetPokeList().get(i).getPosition().x() - minx) * scalex * 0.97 + 70;
                        double y = (pokemons.GetPokeList().get(i).getPosition().y() - miny) * scaley * 0.97 + 70;
                        //g.fillOval((int) x - 10, (int) y - 10, 20, 20);
                        if(pokemons.GetPokeList().get(i).getType()==-1){
                            im = t.getImage("GraphGUI/bulbasour.png");
                            g.drawImage(im,(int)(x-10),(int)(y-10),40,40,this);
                        }else {
                            im = t.getImage("GraphGUI/charizard.png");
                            g.drawImage(im, (int) (x - 35), (int) (y - 35), 70, 70, this);
                        }
                    }
                    im = t.getImage("GraphGUI/pokeball.png");
                    for (int i = 0; i < agents.GetAgentList().size(); i++) {
                        g.setColor(new Color(0, 34, 255, 255));
                        double x = (agents.GetAgentList().get(i).getPos().x() - minx) * scalex * 0.97 + 70;
                        double y = (agents.GetAgentList().get(i).getPos().y() - miny) * scaley * 0.97 + 70;
                        g.drawImage(im,(int)(x-15),(int)(y-15),30,30,this);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        @Override
        public void actionPerformed(ActionEvent e) {
            if(e.getSource()==stop){
                FLAG=1;
            }
        }
    }

    public static void runGUI(MyDWG gr, Pokemons p, Agents a) {
        new GUI(gr,p,a);
    }

}
