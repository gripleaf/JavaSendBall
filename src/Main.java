import java.awt.Button;
import java.awt.Checkbox;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Label;
import java.awt.Menu;
import java.awt.MenuBar;
import java.awt.MenuItem;
import java.awt.TextArea;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JPopupMenu;

class InShe {

    int num;
    private String str[];

    InShe() {
        str = new String[200];
        num = 0;
    }

    public void setM(String str, int c) {
        this.str[c] = str;
    }

    public String getM(int c) {
        return str[c];
    }

    public int To(String st) {
        int i;
        for (i = 0; i < num; i++) {
            if (str[i].compareTo(st) == 0) {
                return i;
            }
        }
        str[num] = st;
        num++;
        SaveMap();
        return num - 1;
    }

    public void SaveMap() {//sava map.txt over.
        PrintWriter pw = null;
        try {
            pw = new PrintWriter(new FileWriter("map.txt"));
            for (int i = 0; i < num; i++) {
                pw.println(str[i] + "       " + i);
            }
        } catch (IOException ex) {
            System.out.println("Error in SaveMap!");
        } finally {
            pw.close();
        }
    }
}

class Catch extends Frame {

    InShe Is;
    static boolean stop, isset;
    static boolean stuAC[][][] = new boolean[200][20][2];//stuAC[][][0]==true-> AC stuAC[][][1]==true  sended.
    static String seaid = new String("user_id=");//search the user_id
    static String seapr = new String("problem_id=");//search the problem_id
    static int col;
    Button bgt, bcl, bur, cnl;
    Label lur, lac;
    TextArea tf;
    TextField tur, tb, cnt_user, cnt_id, cnt_c;
    String urlName = new String("http://10.7.18.82/JudgeOnline/status?contest_id=1136&result=0");
    MenuBar mb;
    MenuItem mire, mihp;
    Menu mu;
    //
    Checkbox cb;
    Label cl;
    //
    JPopupMenu pop;

    Catch() {
        super("Balloon Robot");
        stop = true;
        isset = false;
        Is = new InShe();
        cb = new Checkbox();
        cl = new Label("AUTO");

        lur = new Label("Enter your web:");
        lac = new Label("The List of AC");

        tur = new TextField(urlName, 50);
        cnt_user = new TextField("", 12);
        cnt_id = new TextField("", 4);
        cnt_c = new TextField("", 4);
        tur.selectAll();
        tf = new TextArea(25, 70);
        tb = new TextField("1000", 4);

        bgt = new Button("Get");
        bcl = new Button("Balloon");
        bur = new Button("Set");
        cnl = new Button("Cancel");
        cnt_c.setForeground(Color.WHITE);
        createMenu();//I'm so lazy...and it is 1:22 now . I just want to sleep.
        setLayout(new FlowLayout());
        //add web name
        add(lur);
        add(tur);
        add(bur);
        add(cnl);
        //add cnt
        add(new Label("Current sending|User:"));
        add(cnt_user);
        add(new Label("Problem:"));
        add(cnt_id);
        add(new Label("Color:"));
        add(cnt_c);
        add(bcl);
        cnt_user.setEnabled(false);
        cnt_id.disable();
        cnt_c.disable();
        //add main
        //add(lac);
        add(bgt);
        //add(bcl);
        add(cl);
        add(tb);
        add(new Label("/ms"));
        add(cb);
        add(tf);
        //tf.hide();
        tf.setFont(new Font("", 13, 15));
        setVisible(true);
        setSize(600, 600);//change the size.
        //add actionListener
        bur.addActionListener(new Mint());
        bgt.addActionListener(new Mint());
        bcl.addActionListener(new Mint());
        cb.addItemListener(new IL());
        cnl.addActionListener(new Mint());

        addWindowListener(new WindowAdapter() {

            public void windowClosing(WindowEvent e) {//closing ,you should save the data.waiting...
                SaveData();
                Is.SaveMap();
                dispose();
                System.exit(0);
            }
        });
    }

    public void createMenu() {//only supply open , I'm so lazy, and I just want to sleep.quick !!!
        mb = new MenuBar();
        mu = new Menu("File");
        mire = new MenuItem("Resume");
        mihp = new MenuItem("Help");

        this.setMenuBar(mb);
        mu.add(mire);
        mu.add(mihp);
        mb.add(mu);

        mire.addActionListener(new MeL());
        mihp.addActionListener(new MeL());
    }

    public void SaveData() {//this is finish
        File fd = new File("data.txt");
        PrintWriter pw = null;
        try {
            pw = new PrintWriter(new FileWriter(fd, false));
//            String Sa[] = tf.getText().split("\n");
//            for (int i = 0; i < Sa.length; i++) {
//                pw.println(Sa[i]);
//            }
            pw.println(tf.getText());
        } catch (IOException e) {
            System.out.println(e.getMessage() + "Save data Error!");
        } finally {
            pw.close();
        }
    }

    public void Find(String str) {//Find the needed match string
        int i, j, k, user, pro = 0;
        int len = str.length();
        for (i = 0; i < len - seaid.length(); i++) {
            if (str.substring(i).startsWith(seaid)) {//search user_id
                for (j = i + seaid.length(); j < len; j++) {
                    if (str.charAt(j) == '>') {
                        break;
                    }
                }
                user = -1;
                String st = "";
                for (j = j + 1; j < len; j++) {
                    if (str.charAt(j) == '<') {
                        break;
                    }
                    st += str.charAt(j) + "";
                }
                if (st != null) {
                    user = Is.To(st);//the same to map
                    st = st + "              ";//add the user_id
                }
                pro = -1;
                for (j = j + 1; j < len - seapr.length(); j++) {//search problem_id
                    if (str.substring(j).startsWith(seapr)) {
                        for (k = j + seapr.length();; k++) {
                            if (str.charAt(k) == '>') {
                                break;
                            }
                        }
                        pro = str.charAt(k + 1) - 'A';
                        st += str.charAt(k + 1) + "";
                        break;
                    }
                }
                if (user != -1 && pro != -1 && stuAC[user][pro][0] == false) {//Judge if there are same data.
                    tf.append(st + "\n");//add the problem_id
                    stuAC[user][pro][0] = true;
                }
            }
        }
    }

    public void GetColor(int a) throws IOException {
        URL url = Main.class.getClassLoader().getResource("color.txt");
        BufferedReader bufer = new BufferedReader(new InputStreamReader(url.openStream(), "UTF-8"));
        String str;
        while ((str = bufer.readLine()) != null) {
            String sa[] = str.split("\t");
            //System.out.println("***" + sa[0] + "****");
            try {
                if (Integer.parseInt(sa[0]) == a) {
                    str = sa[1];
                    break;
                }
            } catch (Exception e) {
                //nothing will happen!
            }
        }
        if (a == 1) {//淡绿
            cnt_c.setBackground(new Color(183, 247, 185));
        } else if (a == 3) {//红色
            cnt_c.setBackground(Color.RED);
        } else if (a == 7) {//橙色
            cnt_c.setBackground(new Color(216, 135, 26));
        } else if (a == 0) {//黄色
            cnt_c.setBackground(Color.YELLOW);
        } else if (a == 2) {//深紫色
            cnt_c.setBackground(new Color(94, 7, 181));
        } else if (a == 6) {//绿色
            cnt_c.setBackground(Color.GREEN);
        } else if (a == 9) {//白色
            cnt_c.setBackground(Color.WHITE);
        } else if (a == 8) {//粉红色
            cnt_c.setBackground(Color.PINK);
        } else if (a == 10) {//深蓝色
            cnt_c.setBackground(new Color(52, 155, 240));
        } else if (a == 5) {//墨绿色
            cnt_c.setBackground(new Color(57, 151, 135));
        } else {//淡紫色
            cnt_c.setBackground(new Color(177, 158, 210));
        }
        cnt_c.setText(str);
    }

    public void Get() {
        try {
            URL url = new URL(urlName);
            BufferedReader buf = new BufferedReader(new InputStreamReader(url.openStream()));
            String str;
            while ((str = buf.readLine()) != null) {
                //waiting ... I will come back and finish it.
                Find(str);
            }
        } catch (Exception ex) {
            System.out.println(ex.getClass() + " Get Error!");


        } finally {//if there are some bug ,we can save the data first.
            SaveData();
            Is.SaveMap();
        }
    }

    class Mint implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == cnl) {
                tur.enable();
            } else if (e.getSource() == bur) {//set finish~
                urlName = tur.getText();
                try {
                    new URL(urlName);
                    isset = true;
                } catch (MalformedURLException ex) {
                    System.out.println("Connect failed!Check your url: " + urlName);
                } finally {
                    SaveData();
                }
                tur.disable();
            } else if (!isset) {
                tur.setText("here input your url first!");
                return;
            } else if (e.getSource() == bgt) {//get waiting... now is finished.
                Get();
            } else if (e.getSource() == bcl) {//Clear finished.
                int __t = 0;
                String ac[] = tf.getText().split("\n");
                tf.setText("");
                for (int i = 0; i < ac.length; i++) {
                    String two[] = ac[i].split(" +");
                    //System.out.println(ac[i]+"|"+two[0]+" "+two[1]);
                    if (i == col) {
                        stuAC[Is.To(two[0])][two[1].charAt(0) - 'A'][1] = true;
                        cnt_user.setText(two[0]);
                        cnt_id.setText(two[1]);
                        try {
                            GetColor(two[1].charAt(0) - 'A');
                        } catch (IOException ex) {
                            Logger.getLogger(Catch.class.getName()).log(Level.SEVERE, null, ex);
                            System.out.println("GetColor Error!!!");
                        }
                        tf.append(ac[i] + "          Sended\n");
                        __t = 1;
                    } else {
                        tf.append(ac[i] + "\n");
                    }
                }
                col = col + __t;
                SaveData();
                return;
            }
        }
    }

    public void updata() {
        col = 0;
        File f = new File("data.txt");
        File map = new File("map.txt");
        BufferedReader bur;
        Main.init();
        try {
            String str;
            bur = new BufferedReader(new FileReader(map));
            while ((str = bur.readLine()) != null) {
                String cas[] = str.split(" +");
                Is.setM(cas[0], Integer.parseInt(cas[1]));
            }
            bur = new BufferedReader(new FileReader(f));
            tf.setText("");
            while ((str = bur.readLine()) != null) {
                String cas[] = str.split(" +");
                if (cas.length == 3) {
                    col++;
                    stuAC[Is.To(cas[0])][cas[1].charAt(0) - 'A'][0] = true;
                    stuAC[Is.To(cas[0])][cas[1].charAt(0) - 'A'][1] = true;
                } else if (cas.length == 2) {
                    stuAC[Is.To(cas[0])][cas[1].charAt(0) - 'A'][0] = true;
                }
                tf.append(str + "\n");
            }
        } catch (Exception ex) {
            System.out.println(ex + "update Error");
        }
    }

    public class MeL implements ActionListener {//I finish the open menuItem.Congratulations!!!

        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == mire) {
                updata();
            } else if (e.getSource() == mihp) {
                try {
                    HelpYou helpYou = new HelpYou();
                } catch (Exception ex) {
                    Logger.getLogger(Catch.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    public class IL extends Thread implements ItemListener {//waiting to continue the thread

        public void itemStateChanged(ItemEvent e) {
            Clock c = new Clock("d");
            if (cb.getState() == true) {
                c = new Clock();
                c.start();
            } else {
                c.Stop();
            }
        }
    }

    class Clock extends Thread {

        boolean stat;
        Button btn;

        Clock() {
            stat = true;
            final Frame f = new Frame("clock");
            btn = new Button("Stop");
            btn.setSize(100, 200);//maybe useless!
            f.add(btn);
            btn.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    stat = false;
                    cb.setState(stat);
                    f.dispose();
                }
            });
            f.setLayout(new FlowLayout());
            f.setSize(100, 100);
            f.setVisible(true);
        }

        Clock(String str) {
        }

        public void run() {
            while (stat) {
                try {
                    this.sleep(Integer.parseInt(tb.getText()));//change the time!
                    Get();
                } catch (InterruptedException ex) {
                    System.out.println("run Error!");
                } finally {
                    SaveData();
                    Is.SaveMap();
                }
            }
        }

        public void Stop() {
            if (this.isAlive()) {
                this.Stop();
            }
        }
    }

    class HelpYou {

        HelpYou() throws FileNotFoundException, IOException {
            final Frame f = new Frame("Help you/me");
            TextArea Nkn = new TextArea(40, 80);

            Nkn.setText("");
            URL url = Main.class.getClassLoader().getResource("helpyou.txt");
            BufferedReader bufer = new BufferedReader(new InputStreamReader(url.openStream()));
            try {
                String str;
                while ((str = bufer.readLine()) != null) {
                    Nkn.append(str + "\n");
                }
            } finally {
                bufer.close();
            }
            f.add(Nkn);

            f.setLayout(new FlowLayout());
            f.setSize(600, 400);
            f.setVisible(true);
            f.addWindowListener(new WindowAdapter() {

                public void windowClosing(WindowEvent e) {//closing ,you should save the data.waiting...
                    f.dispose();
                }
            });
        }
    }
}

public class Main {

    public static void init() {
        for (int i = 0; i < 200; i++) {
            for (int j = 0; j < 20; j++) {
                Arrays.fill(Catch.stuAC[i][j], 0, Catch.stuAC[i][j].length, false);
            }
        }
        Catch.col = 0;
    }

    public static void main(String[] args) {
        init();
        Catch c = new Catch();
    }
}
