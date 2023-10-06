import java.util.ArrayList;
import javax.swing.*;
import java.awt.event.*;
import java.awt.*;

//置换还有S盒的设置
public class des {
public static String key1;
public static String key2;
public static int[] IP = new int[] { 2, 6, 3, 1, 4, 8, 5, 7 };
public static int[] EP = new int[] { 4, 1, 2, 3, 2, 3, 4, 1 };
public static int[] P10 = new int[] { 3, 5, 2, 7, 4, 10, 1, 9, 8, 6 };
public static int[] P8 = new int[] { 6, 3, 7, 4, 8, 5, 10, 9 };
public static int[] P4 = new int[] { 2, 4, 3, 1 };
public static int[] IP_1 = new int[] { 4, 1, 3, 5, 7, 2, 8, 6 };
public static String[][] S1_box = new String[][] {
		{ "01", "00", "11", "10" }, { "11", "10", "01", "00" },
		{ "00", "10", "01", "11" }, { "11", "01", "00", "10" } };
public static String[][] S2_box = new String[][] {
		{ "00", "01", "10", "11" }, { "10", "11", "01", "00" },
		{ "11", "00", "01", "10" }, { "10", "01", "00", "11" } };

JFrame f1,f2,f3,f4;
JButton b1,b2,b3,b4,b5,b6,b7,b8,b9;
JTextField text1,text2,text3,text4,text5,text6;
JLabel label1,label2,label3,label4,label5,label6,label7,label8,label9,label10;
myListener listener1,listener5,listener6;
ActionListener listener2,listener3,listener4,listener7,listener8,listener9;

public static String substitue(String str, int[] P) { //进行置换操作    
	StringBuilder sb = new StringBuilder();
	for (int i = 0; i < P.length; i++) {
		sb.append(str.charAt((P[i]) - 1));
	}
	return new String(sb);
}

public static String xor(String str, String key) { //进行异或操作
	StringBuilder sb = new StringBuilder();
	for (int i = 0; i < str.length(); i++) {
		if (str.charAt(i) == key.charAt(i)) {
			sb.append("0");
		} else {
			sb.append("1");
		}
	}
	return new String(sb);
}

public static String searchSbox(String str, int n) { //S盒的查找
	StringBuilder sb = new StringBuilder();
	sb.append(str.charAt(0));
	sb.append(str.charAt(3));
	String ret = new String(sb);
	StringBuilder sb1 = new StringBuilder();
	sb1.append(str.charAt(1));
	sb1.append(str.charAt(2));
	String ret1 = new String(sb1);
	String retu = new String();
	if (n == 1) {
		retu = S1_box[Integer.parseInt(ret, 2)][Integer.parseInt(ret1, 2)];
	} else {
		retu = S2_box[Integer.parseInt(ret, 2)][Integer.parseInt(ret1, 2)];
	}
	return retu;
}

public static void getkey(String key) { //获得key1和key2
	//System.out.println("-----请输入主密钥(10位)------");
	//Scanner sc = new Scanner(System.in);
	//String mainkey = sc.nextLine();
    String mainkey = key;
	mainkey = substitue(mainkey, P10);
	String Ls11 = mainkey.substring(0, 5);
	Ls11 = move(Ls11, 1);//移位后
	String Ls12 = mainkey.substring(5, 10);
	Ls12 = move(Ls12, 1);//移位后
	key1 = Ls11 + Ls12;
	key1 = substitue(key1, P8);
	//System.out.println("key1= " + key1);
	String Ls21 = move(Ls11, 1);
	String Ls22 = move(Ls12, 1);
	key2 = Ls21 + Ls22;
	key2 = substitue(key2, P8);
	//System.out.println("key2= " + key2);
}

public static void rgetkey(String key) { //获得key1和key2
	//System.out.println("-----请输入主密钥(10位)------");
	//Scanner sc = new Scanner(System.in);
	//String mainkey = sc.nextLine();
    String mainkey = key;
	mainkey = substitue(mainkey, P10);
	String Ls11 = mainkey.substring(0, 5);
	Ls11 = move(Ls11, 1);//移位后
	String Ls12 = mainkey.substring(5, 10);
	Ls12 = move(Ls12, 1);//移位后
	key2 = Ls11 + Ls12;
	key2 = substitue(key2, P8);
	//System.out.println("key1= " + key1);
	String Ls21 = move(Ls11, 1);
	String Ls22 = move(Ls12, 1);
	key1 = Ls21 + Ls22;
	key1 = substitue(key1, P8);
	//System.out.println("key2= " + key2);
}

public static String move(String str, int n) { //进行移位操作，只能1位或者2位
	char[] ch = str.toCharArray();
	char[] copy_ch = new char[5];
	for (int i = 0; i < ch.length; i++) {
		int a = ((i - n) % ch.length);
		if (a < 0) {
			if (n == 1) {
				copy_ch[ch.length - 1] = ch[i];
			}
			if (n == 2) {
				if (i == 0) {
					copy_ch[ch.length - 2] = ch[i];
				} else {
					copy_ch[ch.length - 1] = ch[i];
				}

			}
		} else {
			copy_ch[a] = ch[i];
		}
	}
	return new String(copy_ch);
}

public static String encipher(String s1, String s2) { //加密主体
	//System.out.println("-----请输入要加密的信息(8位)------");
	//Scanner sc = new Scanner(System.in);
	//String plaintext = sc.nextLine();
    getkey(s1);
    String plaintext = s2;
	plaintext = substitue(plaintext, IP);
	String L0 = plaintext.substring(0, 4);
	String R0 = plaintext.substring(4, 8);
	String R0E = substitue(R0, EP);
	R0E = xor(R0E, key1);
	String S1 = R0E.substring(0, 4);
	String S2 = R0E.substring(4, 8);
	S1 = searchSbox(S1, 1);
	S2 = searchSbox(S2, 2);
	String SS = S1 + S2;
	String f1 = substitue(SS, P4);
	String L1 = R0;
	String R1 = xor(f1, L0);
	//这里求出L1,R1
	//-----------------第二轮-------------
	String R11 = substitue(R1, EP);
	R11 = xor(R11, key2);
	S1 = R11.substring(0, 4);
	S2 = R11.substring(4, 8);
	S1 = searchSbox(S1, 1);
	S2 = searchSbox(S2, 2);
	SS = S1 + S2;
	String f2 = substitue(SS, P4);
	String L2 = xor(f2, L1);
	String R2 = R1;
	//这里求出L2,R2
	String ciphertext = L2 + R2;
	ciphertext = substitue(ciphertext, IP_1);
    return ciphertext;
	//System.out.println("密文: " + ciphertext);
}

public static String decipher(String s1, String s2) { //解密主体
    rgetkey(s1);
    String plaintext = s2;
	plaintext = substitue(plaintext, IP);
	String L0 = plaintext.substring(0, 4);
	String R0 = plaintext.substring(4, 8);
	String R0E = substitue(R0, EP);
	R0E = xor(R0E, key1);
	String S1 = R0E.substring(0, 4);
	String S2 = R0E.substring(4, 8);
	S1 = searchSbox(S1, 1);
	S2 = searchSbox(S2, 2);
	String SS = S1 + S2;
	String f1 = substitue(SS, P4);
	String L1 = R0;
	String R1 = xor(f1, L0);
	//这里求出L1,R1
	//-----------------第二轮-------------
	String R11 = substitue(R1, EP);
	R11 = xor(R11, key2);
	S1 = R11.substring(0, 4);
	S2 = R11.substring(4, 8);
	S1 = searchSbox(S1, 1);
	S2 = searchSbox(S2, 2);
	SS = S1 + S2;
	String f2 = substitue(SS, P4);
	String L2 = xor(f2, L1);
	String R2 = R1;
	//这里求出L2,R2
	String ciphertext = L2 + R2;
	ciphertext = substitue(ciphertext, IP_1);
    return ciphertext;
}

static ArrayList<String> bf(String s1, String s2){
    ArrayList<String> key = new ArrayList<String>();
    for(int i=0; i<1024; i++){
        String x = Integer.toBinaryString(i);
        while(x.length()<10){
            x = "0"+x;
        }
        if(encipher(x, s1).equals(s2)){
            key.add(x);
        }
    }
    return key;
}
 
public des(){
    f1 = new JFrame("encipher");
    f2 = new JFrame("S-DES");
    f3 = new JFrame("decipher");
    f4 = new JFrame("brute force");
    f1.setLayout(new FlowLayout());
    f2.setLayout(new FlowLayout());
    f3.setLayout(new FlowLayout());
    f4.setLayout(new FlowLayout());
    f1.setBounds(800,400,500,200);
    f2.setBounds(800,400,500,150);
    f3.setBounds(800,400,500,200);
    f4.setBounds(800,400,800,200);
    f1.setVisible(false);
    f2.setVisible(true);
    f3.setVisible(false);
    f4.setVisible(false);

    //f2的设计
    b2 = new JButton("加密");
    b3 = new JButton("解密");
    b4 = new JButton("暴力破解");
    listener2 = new action2();
    listener3 = new action3();
    listener4 = new action4();
    b2.addActionListener(listener2);
    b3.addActionListener(listener3);
    b4.addActionListener(listener4);
    f2.add(b2);
    f2.add(b3);
    f2.add(b4);

    //f1的设计
    b1 = new JButton("开始加密");
    b7 = new JButton("返回");
    listener1 = new action1();
    b1.addActionListener(listener1);
    listener7 = new action7();
    b7.addActionListener(listener7);
    text1 = new JTextField(10);
    text2 = new JTextField(10);
    listener1.setJTextField1(text1);
    listener1.setJTextField2(text2);
    text1.addActionListener(listener1);
    text2.addActionListener(listener1);
    label1 = new JLabel("请输入密钥(10位):");
    label2 = new JLabel("请输入明文(8位):");
    label3 = new JLabel("本次加密的结果是：");
    f1.add(label1);
    f1.add(text1);
    f1.add(label2);
    f1.add(text2);
    f1.add(b1);
    f1.add(b7);
    f1.add(label3);

    //f3的设计
    b5 = new JButton("开始解密");
    b8 = new JButton("返回");
    listener5 = new action5();
    b5.addActionListener(listener5);
    listener8 = new action8();
    b8.addActionListener(listener8);
    text3 = new JTextField(10);
    text4 = new JTextField(10);
    listener5.setJTextField1(text3);
    listener5.setJTextField2(text4);
    text3.addActionListener(listener5);
    text4.addActionListener(listener5);
    label4 = new JLabel("请输入密钥(10位):");
    label5 = new JLabel("请输入密文(8位):");
    label6 = new JLabel("本次解密的结果是：");
    f3.add(label4);
    f3.add(text3);
    f3.add(label5);
    f3.add(text4);
    f3.add(b5);
    f3.add(b8);
    f3.add(label6);

    //f4的设计
    b6 = new JButton("开始破解");
    b9 = new JButton("返回");
    listener6 = new action6();
    b6.addActionListener(listener6);
    listener9 = new action9();
    b9.addActionListener(listener9);
    text5 = new JTextField(10);
    text6 = new JTextField(10);
    listener6.setJTextField1(text5);
    listener6.setJTextField2(text6);
    text5.addActionListener(listener6);
    text6.addActionListener(listener6);
    label7 = new JLabel("请输入明文(8位):");
    label8 = new JLabel("请输入密文(8位):");
    label9 = new JLabel("本次破解的结果是："); 
    label9.setForeground(Color.RED);
    label10 = new JLabel("破解的消耗的时间为：");
    f4.add(label7);
    f4.add(text5);
    f4.add(label8);
    f4.add(text6);
    f4.add(b6);
    f4.add(b9);
    f4.add(label9);
    f4.add(label10);
}

public interface myListener extends ActionListener{
    public void setJTextField1(JTextField text);
    public void setJTextField2(JTextField text);
}
public class action1 implements myListener{
    JTextField textInput1;
    JTextField textInput2;
    public void setJTextField1(JTextField text){
        textInput1 = text;
    }
    public void setJTextField2(JTextField text){
        textInput2 = text;
    }
    public void actionPerformed(java.awt.event.ActionEvent e){
        String s1 = textInput1.getText();
        String s2 = textInput2.getText();
        if(s1.length()!=10||s2.length()!=8){
            JOptionPane.showMessageDialog(f1,"请重新输入", "密钥或明文格式错误！", JOptionPane.WARNING_MESSAGE);
        }
        else{
            boolean flag = true;
            for(int i = 0; i<s1.length(); i++){
                if(s1.substring(i, i+1).equals("0")||s1.substring(i, i+1).equals("1")){}
                else{
                    JOptionPane.showMessageDialog(f1,"请重新输入", "密钥或明文格式错误！", JOptionPane.WARNING_MESSAGE);
                    flag = false;
                    break;
                }
            }
            if(flag){
                String ciphertext;
                ciphertext = encipher(s1, s2);
                label3.setText("本次加密的结果是："+ciphertext);
            }
        }
    }
}
    public class action2 implements ActionListener{
        public void actionPerformed(java.awt.event.ActionEvent e){
            f2.setVisible(false);
            f1.setVisible(true);
        }
    }

    public class action3 implements ActionListener{
        public void actionPerformed(java.awt.event.ActionEvent e){
            f2.setVisible(false);
            f3.setVisible(true);
        }
    }

    public class action4 implements ActionListener{
        public void actionPerformed(java.awt.event.ActionEvent e){
            f2.setVisible(false);
            f4.setVisible(true);
        }
    }

    public class action5 implements myListener{
        JTextField textInput1;
        JTextField textInput2;
        public void setJTextField1(JTextField text){
            textInput1 = text;
        }
        public void setJTextField2(JTextField text){
            textInput2 = text;
        }
        public void actionPerformed(java.awt.event.ActionEvent e){
            String s1 = textInput1.getText();
            String s2 = textInput2.getText();
            if(s1.length()!=10||s2.length()!=8){
                JOptionPane.showMessageDialog(f3,"请重新输入", "密钥或密文格式错误！", JOptionPane.WARNING_MESSAGE);
            }
            else{
                boolean flag = true;
                for(int i = 0; i<s1.length(); i++){
                    if(s1.substring(i, i+1).equals("0")||s1.substring(i, i+1).equals("1")){}
                    else{
                        JOptionPane.showMessageDialog(f3,"请重新输入", "密钥或密文格式错误！", JOptionPane.WARNING_MESSAGE);
                        flag = false;
                        break;
                    }
                }
                if(flag){
                    String plaintext;
                    plaintext = decipher(s1, s2);
                    label6.setText("本次解密的结果是："+plaintext);
                }
            }
        }
    }

    public class action6 implements myListener{
        JTextField textInput1;
        JTextField textInput2;
        public void setJTextField1(JTextField text){
            textInput1 = text;
        }
        public void setJTextField2(JTextField text){
            textInput2 = text;
        }
        public void actionPerformed(java.awt.event.ActionEvent e){
            String s1 = textInput1.getText();
            String s2 = textInput2.getText();
            if(s1.length()!=8||s2.length()!=8){
                JOptionPane.showMessageDialog(f4,"请重新输入", "密文或明文格式错误！", JOptionPane.WARNING_MESSAGE);
            }
            else{
                boolean flag = true;
                for(int i = 0; i<s1.length(); i++){
                    if(s1.substring(i, i+1).equals("0")||s1.substring(i, i+1).equals("1")){}
                    else{
                        JOptionPane.showMessageDialog(f4,"请重新输入", "密文或明文格式错误！", JOptionPane.WARNING_MESSAGE);
                        flag = false;
                        break;
                    }
                }
                if(flag){
                    long startTime = System.nanoTime();
                    ArrayList<String> key = new ArrayList<String>();
                    key = bf(s1, s2);
                    long endTime = System.nanoTime();
                    long duration = endTime - startTime;
                    double milliseconds = (double) duration / 1_000_000.0;
                    String text1 = "";
                    for (String str : key) {
                        text1=text1+str+",";
                    }
                    label9.setText("本次破解的结果是："+text1);
                    label10.setText("破解的消耗的时间为："+milliseconds+" 毫秒");
                }
            }
        }
    }

    public class action7 implements ActionListener{
        public void actionPerformed(java.awt.event.ActionEvent e){
            f1.setVisible(false);
            f2.setVisible(true);
        }
    }

    public class action8 implements ActionListener{
        public void actionPerformed(java.awt.event.ActionEvent e){
            f3.setVisible(false);
            f2.setVisible(true);
        }
    }

    public class action9 implements ActionListener{
        public void actionPerformed(java.awt.event.ActionEvent e){
            f4.setVisible(false);
            f2.setVisible(true);
        }
    }


    public static void main(String[] args){
        new des();
    }
}
