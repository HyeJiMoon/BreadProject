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
	
	//서쪽영역
	Choice ch_top, ch_sub;
	JTextField t_name, t_price;
	Canvas can_west;//비주얼컴포넌트에 아이콘 정도로 넣으면 될 거같은데
	JButton bt_regist;
	
	//동쪽영역
	Canvas can_east;
	JTextField t_name2,t_price2;
	JButton bt_edit, bt_delete;
	
	DBManager manager;
	Connection con;
	
	//상위 카테고리 list
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
		
		can_west=new Canvas(){//can을 내부익명 ->canvas에 그냥 그리고 말거니까!!! 
			public void paint(Graphics g) {
				g.drawImage(image, 0, 0, 135,135,this); //image 안되는 이유 ?? BufferedImage 가 지역변수에 갖혀있어서 
				//내부익명은 지역변수 final로 해야 접근가능 
			}
			
			
		};
		can_west.setPreferredSize(new Dimension(135, 135));
		bt_regist=new JButton("검색");
		//동쪽
		can_east=new Canvas();
		t_name2=new JTextField(10);
		t_price2=new JTextField(10);
		bt_edit=new JButton("편집");
		bt_delete=new JButton("삭제");
		
		ch_top.setPreferredSize(new Dimension(135, 40));
		ch_sub.setPreferredSize(new Dimension(135, 40));
		ch_top.add("▼상위카테고리 선택");
		ch_sub.add("▼하위카테고리 선택");
		
		//각패널들의 크기 지정
		p_west.setPreferredSize(new Dimension(150, 700));
		p_center.setPreferredSize(new Dimension(550, 700));
		p_east.setPreferredSize(new Dimension(150, 700));
		
		//센터의 그리드 적용하고 위 아래 구성
		p_center.setLayout(new GridLayout(2, 1));
		p_center.add(p_up);
		p_center.add(p_down);
		
		//스크롤 붙이기
		p_up.setLayout(new BorderLayout()); //옆에 틈생기니까 !! !!
		p_down.setLayout(new BorderLayout());
		p_up.add(scroll_up);
		p_down.add(scroll_down);
		
		//각패널의 색상
		p_west.setBackground(Color.PINK);
		p_east.setBackground(Color.blue);
		p_center.setBackground(Color.YELLOW);
		p_up.setBackground(Color.GRAY);
		p_down.setBackground(Color.LIGHT_GRAY);
		
		//패널 붙이기
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
		
		//초이스와 리스너 연결
		ch_top.addItemListener(this);
				
		setSize(850,700);
		setVisible(true);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		init();
		getTop();
	}
	
	//데이터베이스 커넥션 얻기
	public void init(){
		manager=DBManager.getInstance();
		con=manager.getConnection();
		
		System.out.println(con);
	}
	
	//최상위 카테고리 얻기 
	public void getTop(){
		PreparedStatement pstmt=null;
		ResultSet rs=null;
		
		String sql="select * from topcategory order by topcategory_id asc";
		try {
			pstmt=con.prepareStatement(sql);
			rs=pstmt.executeQuery();
		
			//앞으로 레코드 한건은 사물을 표현하는 인스턴스로 표현 (cf) 테이블이 3개다 DTO 세개가 필요 
			while(rs.next()){
				TopCategory dto=new TopCategory();
				dto.setTopcategory_id(rs.getInt("topcategory_id"));
				dto.setTop_name(rs.getString("top_name"));
				
				topList.add(dto); //리스트에 탑재 리스트에 담으면 rs 없어져도 된다
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
	//하위카테고리 값 구하기
	public void getSub(){
		PreparedStatement pstmt=null;
		ResultSet rs=null; //select 문 rs필요! 
		String sql="select * from subcategory where topcategory_id=?";//물음표 쓸수 있오 = 바인드변수	
		
		try {
			pstmt=con.prepareStatement(sql);
			//바인드변수값 지정
			int index=ch_top.getSelectedIndex();
			if((index-1)>=0){
				TopCategory dto=topList.get(index-1); // top 선택하면 
				pstmt.setInt(1,dto.getTopcategory_id()); //(첫번째 발견된 바인드변수=1, 유저선택top)  -->첫번째발견된 물음표의 유저가 선택한 탑카테고리
				rs=pstmt.executeQuery(); //rs 올리기 
				
				//담기전에 지우자 (쌓이니까)
				subList.removeAll(subList); // 메모리 지우기 
				ch_sub.removeAll();//초이스 지우기 (디자인)
				//하위카테고리채우기
				while(rs.next()){
					
					SubCategory vo=new SubCategory(); //while문 만큼 subcategory가 필요햄
					
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
		//하위카테고리 구하기 
		getSub();
	}
	
	public static void main(String[] args) {
		new MainWindow();
	}

}
