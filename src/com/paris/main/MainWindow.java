/*
 * Join 문이란 
 * -정규화에 의해 물리적으로 분리된 테이블을 마치 하나의 테이블처럼 보여줄 수 있는 쿼리 기법
 * 
 *inner 조인 (조인 대상이 되는 테이블간 공통적인 레코드만 가져온다.)
 *우리가 지금까지 사용해왔던 조인이다
 *주의할특징) 공통적인 레코드가 아닌 경우 누락된다( ex operation 같이)
 *
 *outer 조인  
 *조인대상이 되는 테이블간 공통된 레코드 뿐만아니라, 지정한 테이블의 레코드는 일단 무조건 다 가져오는 조인
 * 
 * 
 * 
 * 
 * */

package com.paris.main;


import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Choice;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Vector;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;

import db.DBManager;
import db.SubCategory;
import db.TopCategory;

public class MainWindow extends JFrame implements ItemListener ,ActionListener {
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
	JTextField t_id,t_name2,t_price2;
	JButton bt_edit, bt_delete;
	
	DBManager manager;
	Connection con;
	
	//상위 카테고리 list
	ArrayList<TopCategory> topList=new ArrayList<TopCategory>();
	
	ArrayList<SubCategory> subList=new ArrayList<SubCategory>();
	BufferedImage image=null;
	
	//Table 모델객체들 미리 올려놓자
	UpModel upModel;
	DownModel downModel;
	JFileChooser chooser;
	File file;
	
	
	public MainWindow() {
		p_west=new JPanel();
		p_center=new JPanel();
		p_east=new JPanel();
		p_up=new JPanel();
		p_down=new JPanel();
		table_up=new JTable();
		table_down=new JTable(3,4);
		scroll_up=new JScrollPane(table_up);
		scroll_down=new JScrollPane(table_down);
		ch_top=new Choice();
		ch_sub=new Choice();
		t_name=new JTextField(10);
		t_price=new JTextField(10);
		chooser=new JFileChooser("C:/html_workspace/images/shurek/");
		
		
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
		bt_regist=new JButton("등록");
		//동쪽
		can_east=new Canvas(){
			public void paint(Graphics g) {
				g.drawImage(image, 0, 0, 135,135,this);
			}
		};
		
		can_east.setPreferredSize(new Dimension(135, 135));
		t_id=new JTextField(10);
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

		t_id.setEditable(false);
		p_east.add(t_id);
		p_east.add(t_name2);
		p_east.add(t_price2);
		p_east.add(can_east);
		p_east.add(bt_edit);
		p_east.add(bt_delete);
		
		//초이스와 리스너 연결
		ch_top.addItemListener(this);
		//버튼과 리스너 연결
		bt_regist.addActionListener(this);
		
		//캔버스에 마우스리스너 연결(이미지바뀌께끔)
		can_west.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				preView();
			}
			
			
		});
		
		//다운 테이블과 리스너 연결 
		table_up.addMouseListener(new MouseAdapter() {
			
			public void mouseClicked(MouseEvent e) {
				int row=table_up.getSelectedRow();//???
				int col=0;
				
				String subcategory_id=(String)table_up.getValueAt(row, col);
				System.out.println(subcategory_id);
			
				//구해진 id를 아래의 모델에 적용하자  //유저가 클릭했을 때 
				downModel.getList(Integer.parseInt(subcategory_id));
				table_down.updateUI();
			
			}
			
			
		});
		
		table_down.addMouseListener(new MouseAdapter() {
			
			public void mouseClicked(MouseEvent e) {
				int row=table_down.getSelectedRow();
				//이차원 벡터에 들어있는 작은 벡터를 얻어오자 = 한줄이바로 레코드니까 
				Vector vec=downModel.data.get(row);
				System.out.println(vec.get(2));
				getDetail(vec);
			}
		});
		
		setSize(850,700);
		setVisible(true);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		init(); //DB여는거 
		getTop();
		getUpList(); //위쪽테이블 처리 
		getDownList(); //아래쪽 테이블 처리 
	
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
	//위쪽 테이블 데이터 처리(UpModel 에서 가져온 TableModel)
	public void getUpList(){
		table_up.setModel(upModel=new UpModel(con)); //setModel?
		table_up.updateUI();
		
	}

	public void getDownList(){
		table_down.setModel(downModel=new DownModel(con));
		table_down.updateUI();
		
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
	/*--------------
		상품등록
	----------------*/
	public void regist(){ //쿼리문을 적으면 일단 기준이 잡히니까 ex쿼리문 수행되려면 뭐가필요하지
		PreparedStatement pstmt=null;
		String sql="insert into product (product_id,subcategory_id,product_name, price, img)";
		sql+="values(seq_product.nextval,?,?,?,?)"; //여기도 바인드변수 가능 아니면 홑따옴표, ++ 복잡한 변수로 만들어져
		System.out.println(sql);
		
		
		try {
			pstmt=con.prepareStatement(sql);
			//초이스컴포넌트는 그냥 번호 DB 에는 키값을 가져와야해(primary key) --> rs 를죽이고 만들었던 arraylist
			//큰자료형 Arraylist 그 안에 자료형 Subcategory 
			int index=ch_sub.getSelectedIndex();  //
			SubCategory vo=subList.get(index);
			
			//바인드변수에 들어갈 값 결정을 먼저한다 그 값들은 ArrayLIst 안에 들어있는SubCategory DTO를 추출하여
			//PK 값을 넣어주자 
			
			pstmt.setInt(1,vo.getSubcategory_id()); //(첫번째,서브id)
			pstmt.setString(2, t_name.getText() ); //(두번쨰, 상품명)
			pstmt.setInt(3, Integer.parseInt(t_price.getText()) ); //
			pstmt.setString(4, file.getName()); // 파일의 
			
			//executeUpdate메서드는 쿼리문 수행 후 반영된 레코드의 갯수를 반환해준다
			//따라서 insert문의 경우 언제나 성공했다면 1건, update 1건이상 ,delete1건 
			//결론) insert시 반환값이 0이라면 insert 실패
			int result=pstmt.executeUpdate();
			if(result!=0){
				JOptionPane.showMessageDialog(this, "등록성공");
				//상품을 등록했을 때 전의 table 이라서 추가했는데도 변경이안돼
				//db를 새롭게 가져와 이차원 벡터변경하자 ! 
				upModel.getList(); //이차원 벡터라든지 바뀜 (갱신) 호출
				table_up.updateUI();//갱신 
				//이미지복사
				copy();
				
				
			}else{
				JOptionPane.showMessageDialog(this, "등록실패");
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			if(pstmt!=null){
				try {
					pstmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
	}
	//캔버스에 이미지 반영하기
	public void preView(){
		int result=chooser.showOpenDialog(this);
		if(result==JFileChooser.APPROVE_OPTION){
			//캔버스에 이미지 그리자 
			file=chooser.getSelectedFile();
			//얻어진 파일을 기존의 이미지로 대체하여 다시 그리기  image=파일과 관련한 새로운 이미지 
			try {
				image=ImageIO.read(file);
				can_west.repaint();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		}
	}
	
	
	//이미지 복사 메서드 정의
	public void copy(){
		FileInputStream fis=null;
		FileOutputStream fos=null;
		
		try {
			fis=new FileInputStream(file);
			fos=new FileOutputStream("C:/java_workspace2/BreadProject/data/"+file.getName());
			byte[] b=new byte[1024];  //회전은 3번 (-1까지 받아서) 
			
			int flag; //-1인지 여부 판단
			while(true){
				flag=fis.read(b);
				if(flag==-1)break;
				fos.write(b);
			}
			System.out.println("이미지 복사완료");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			if(fos!=null){
				try {
					fos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
				
			}
			if(fis!=null){
				try {
					fis.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	//상세정보 보여주기 //선택한 제품의 상세정보 보여주기
	public void getDetail(Vector vec){
		t_id.setText(vec.get(0).toString()); 
		t_name2.setText(vec.get(2).toString());
		t_price2.setText(vec.get(3).toString());
		
		try {
			image=ImageIO.read(new File("C:/java_workspace2/BreadProject/data/"+vec.get(4)));
			can_east.repaint();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	public void actionPerformed(ActionEvent e) {
		regist();
	}
	public static void main(String[] args) {
		new MainWindow();
	}

}
