package com.paris.main;


import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Choice;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;

import db.DBManager;
import db.SubCategory;
import db.TopCategory;

public class MainWindow extends JFrame implements ItemListener{
	JPanel p_west, p_east, p_center;
	JPanel p_up, p_down;
	JTable table_up, table_down;
	JScrollPane scroll_up, scroll_down;
	
	//���ʿ���
	Choice ch_top, ch_sub;
	JTextField t_name, t_price;
	Canvas can_west;//���־�������Ʈ�� ������ ������ ������ �� �Ű�����
	JButton bt_regist;
	
	//���ʿ���
	Canvas can_east;
	JTextField t_name2,t_price2;
	JButton bt_edit, bt_delete;
	
	DBManager manager;
	Connection con;
	
	//���� ī�װ� list
	ArrayList<TopCategory> topList=new ArrayList<TopCategory>();
	
	ArrayList<SubCategory> subList=new ArrayList<SubCategory>();
	BufferedImage image=null;
	
	public MainWindow() {
		p_west=new JPanel();
		p_center=new JPanel();
		p_east=new JPanel();
		p_up=new JPanel();
		p_down=new JPanel();
		table_up=new JTable(3,6);
		table_down=new JTable(3,4);
		scroll_up=new JScrollPane(table_up);
		scroll_down=new JScrollPane(table_down);
		ch_top=new Choice();
		ch_sub=new Choice();
		t_name=new JTextField(10);
		t_price=new JTextField(10);
		
		
		
		try {
			URL url=this.getClass().getResource("/default.png");
			image=ImageIO.read(url);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		can_west=new Canvas(){//can�� �����͸� ->canvas�� �׳� �׸��� ���Ŵϱ�!!! 
			public void paint(Graphics g) {
				g.drawImage(image, 0, 0, 135,135,this); //image �ȵǴ� ���� ?? BufferedImage �� ���������� �����־ 
				//�����͸��� �������� final�� �ؾ� ���ٰ��� 
			}
			
			
		};
		can_west.setPreferredSize(new Dimension(135, 135));
		bt_regist=new JButton("�˻�");
		//����
		can_east=new Canvas();
		t_name2=new JTextField(10);
		t_price2=new JTextField(10);
		bt_edit=new JButton("����");
		bt_delete=new JButton("����");
		
		ch_top.setPreferredSize(new Dimension(135, 40));
		ch_sub.setPreferredSize(new Dimension(135, 40));
		ch_top.add("�����ī�װ� ����");
		ch_sub.add("������ī�װ� ����");
		
		//���гε��� ũ�� ����
		p_west.setPreferredSize(new Dimension(150, 700));
		p_center.setPreferredSize(new Dimension(550, 700));
		p_east.setPreferredSize(new Dimension(150, 700));
		
		//������ �׸��� �����ϰ� �� �Ʒ� ����
		p_center.setLayout(new GridLayout(2, 1));
		p_center.add(p_up);
		p_center.add(p_down);
		
		//��ũ�� ���̱�
		p_up.setLayout(new BorderLayout()); //���� ƴ����ϱ� !! !!
		p_down.setLayout(new BorderLayout());
		p_up.add(scroll_up);
		p_down.add(scroll_down);
		
		//���г��� ����
		p_west.setBackground(Color.PINK);
		p_east.setBackground(Color.blue);
		p_center.setBackground(Color.YELLOW);
		p_up.setBackground(Color.GRAY);
		p_down.setBackground(Color.LIGHT_GRAY);
		
		//�г� ���̱�
		add(p_west, BorderLayout.WEST);
		add(p_center);
		add(p_east,BorderLayout.EAST);
		
		p_west.add(ch_top);
		p_west.add(ch_sub);
		p_west.add(t_name);
		p_west.add(t_price);
		p_west.add(can_west);
		p_west.add(bt_regist);
		
		p_east.add(can_east);
		p_east.add(t_name2);
		p_east.add(t_price2);
		p_east.add(bt_edit);
		p_east.add(bt_delete);
		
		//���̽��� ������ ����
		ch_top.addItemListener(this);
				
		setSize(850,700);
		setVisible(true);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		init();
		getTop();
	}
	
	//�����ͺ��̽� Ŀ�ؼ� ���
	public void init(){
		manager=DBManager.getInstance();
		con=manager.getConnection();
		
		System.out.println(con);
	}
	
	//�ֻ��� ī�װ� ��� 
	public void getTop(){
		PreparedStatement pstmt=null;
		ResultSet rs=null;
		
		String sql="select * from topcategory order by topcategory_id asc";
		try {
			pstmt=con.prepareStatement(sql);
			rs=pstmt.executeQuery();
		
			//������ ���ڵ� �Ѱ��� �繰�� ǥ���ϴ� �ν��Ͻ��� ǥ�� (cf) ���̺��� 3���� DTO ������ �ʿ� 
			while(rs.next()){
				TopCategory dto=new TopCategory();
				dto.setTopcategory_id(rs.getInt("topcategory_id"));
				dto.setTop_name(rs.getString("top_name"));
				
				topList.add(dto); //����Ʈ�� ž�� ����Ʈ�� ������ rs �������� �ȴ�
				ch_top.add(dto.getTop_name());
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			if(rs!=null){
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
	}
	//����ī�װ� �� ���ϱ�
	public void getSub(){
		PreparedStatement pstmt=null;
		ResultSet rs=null; //select �� rs�ʿ�! 
		String sql="select * from subcategory where topcategory_id=?";//����ǥ ���� �ֿ� = ���ε庯��	
		
		try {
			pstmt=con.prepareStatement(sql);
			//���ε庯���� ����
			int index=ch_top.getSelectedIndex();
			if((index-1)>=0){
				TopCategory dto=topList.get(index-1); // top �����ϸ� 
				pstmt.setInt(1,dto.getTopcategory_id()); //(ù��° �߰ߵ� ���ε庯��=1, ��������top)  -->ù��°�߰ߵ� ����ǥ�� ������ ������ žī�װ�
				rs=pstmt.executeQuery(); //rs �ø��� 
				
				//������� ������ (���̴ϱ�)
				subList.removeAll(subList); // �޸� ����� 
				ch_sub.removeAll();//���̽� ����� (������)
				//����ī�װ�ä���
				while(rs.next()){
					
					SubCategory vo=new SubCategory(); //while�� ��ŭ subcategory�� �ʿ���
					
					vo.setSubcategory_id(rs.getInt("subcategory_id"));
					vo.setTopcategory_id(rs.getInt("topcategory_id"));
					vo.setSub_name(rs.getString("sub_name"));
					
					subList.add(vo);
					ch_sub.add(vo.getSub_name());
					
				}
				
				
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void itemStateChanged(ItemEvent e) {
		//����ī�װ� ���ϱ� 
		getSub();
	}
	
	public static void main(String[] args) {
		new MainWindow();
	}

}
