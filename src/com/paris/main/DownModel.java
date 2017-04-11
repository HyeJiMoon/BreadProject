/*
 * 하위 카테고리에 등록된 상품정보 제공모델 (보여주는것은 테이블이하는 것)
 * 
 * */
package com.paris.main;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Vector;

import javax.swing.table.AbstractTableModel;

public class DownModel extends AbstractTableModel{
	Vector<String> columnName=new Vector<String>();
	Vector<Vector> data=new Vector<Vector>();
	Connection con;
	
	public DownModel(Connection con) {
		 this.con=con;
		 //컬럼은 동적으로 움직이는 것이 아니다 따라서 생성자에 고정 
		 columnName.add("product_id");
		 columnName.add("subcategory_id");
		 columnName.add("product_name");
		 columnName.add("price");
		 columnName.add("img");
	}
	
	//rs 는 죽기전에 벡터에 담자 
	
	//마우스로 유저가 클릭할때마다 id값이 바뀌므로, 아래의 메서드를 그때마다 호출하자!
	public void getList(int subcategory_id){
		PreparedStatement pstmt=null;
		ResultSet rs=null;
		
		
		String sql="select * from product"; //쿼리문 뒤에서 한칸뛰거나 where 앞에서 한칸뛰거나
		sql+=" where subcategory_id=?";   //사용자가 서브카테고리를 선택하면 결정된다 값만 필요할때ㅁ마다 호출하면 되니까 인수로받자
		try {
			pstmt=con.prepareStatement(sql);
			pstmt.setInt(1, subcategory_id); //String 도 상관없는데 의미를 살리고자-- 바인드변수 지정할때는 이름ㅇ따라서 서브카테고리아이디니까 Int 
			rs=pstmt.executeQuery();
			
			//벡터들 초기화 
			//columnName.removeAll(columnName);
			data.removeAll(data);
			
			ResultSetMetaData meta=rs.getMetaData();
			for(int i=1;i<=meta.getColumnCount();i++){
				columnName.add(meta.getColumnName(i));
				
			}
			System.out.println("getLIst 컬럼의 크기는"+columnName.size());
			
			while(rs.next()){
				Vector vec=new Vector();
				vec.add(rs.getInt("product_id")); //기본자료형->객체자료형: boxing
				vec.add(rs.getString("subcategory_id"));
				vec.add(rs.getString("product_name"));
				vec.add(rs.getString("price")); //JTAble은 어차피 자료형 STring??
				vec.add(rs.getString("img"));
				
				data.add(vec);	
			}//레코드 끝나는 시점
			System.out.println("getList 레코드 크기는"+data.size());
			
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			if(rs!=null){
				try {
					rs.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if(pstmt!=null){
				try {
					pstmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	
	public int getRowCount() {
		System.out.println("레코드의 갯수는"+data.size());
		return data.size();
		
	
	}
	public int getColumnCount() { //하위카테고리에 뿌려질 때 보니까 ggetColumnCount가 호출되지 않고있다
		System.out.println("컬럼의 갯수는"+columnName.size());
		return columnName.size();
	}
//컬럼네임 변경
	public String getColumnName(int col) {
		return columnName.get(col);
	}
	public Object getValueAt(int row, int col) {
		Object value=data.get(row).get(col); //value 를 object 로 받는 것 ? 원래 객체는 출력대상이 될 수없으나 object 의 메서드에 toString 을 가지고 있어서 자동으로호출
		System.out.println("getValue 호출"+value);
		return data.get(row).get(col);
		
	
	}
}


/*
 * Dog g=new Dog();
 * system.out.println(g);
 * 
 * Dog의 주소값ㅇ ㅣ나옴  원래 객체는 출력대상이 될 수 없으나 자동호출해줬던것
 * 
 * 
 * 
 * 
 * 
 * 데이터베이스의 컬럼자체는 고정적이다 
 * */

