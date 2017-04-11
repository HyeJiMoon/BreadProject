/*하위 카테고리와 그 카테고리에 등록된 상품의 수 정보를 제공하는 모델
 * 앞으로 데이터베이스 연동은 
 * 
 * */

package com.paris.main;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Vector;

import javax.sound.midi.MetaEventListener;
import javax.swing.table.AbstractTableModel;


public class UpModel extends AbstractTableModel{
	Vector<String> columnName=new Vector<String>();
	Vector<Vector> data = new Vector<Vector>();
	Connection con;
	
	
	public UpModel(Connection con) {
		this.con=con;
		getList(); 
	}
	//목록 가져오기 
	public void getList(){
		PreparedStatement pstmt=null;
		ResultSet rs=null;
		StringBuffer sql=new StringBuffer();
		
		
		sql.append("select s.subcategory_id as subcategory_id ,sub_name as 카테고리명, count(product_id) as 갯수");
		sql.append(" from subcategory s left outer join product p ");
		sql.append(" on s.subcategory_id=p.subcategory_id ");
		sql.append(" group by s.subcategory_id,sub_name");

		try {
			pstmt=con.prepareStatement(sql.toString());
			rs=pstmt.executeQuery();
		
			//벡터들을 초기화 (누적되는 상태로 갱신되니까 )
			columnName.removeAll(columnName);
			data.removeAll(data);
			
			
			//rs 죽기전에 컬럼명 추출
			
			//subcategory_id sub_name 얘네 이름 출력(밑에 for문)
			ResultSetMetaData meta=rs.getMetaData(); 
			for(int i=1;i<=meta.getColumnCount();i++){
				columnName.add(meta.getColumnName(i));
				
			}
			
			//죽기전에 뽑아야해
			
			while(rs.next()){
				//Vector 를 DTO로
				//레코드 1건을 벡터에 옮겨심자! 여기에서 벡터는 DTO역할
				Vector vec = new Vector(); //자료형 string 넣어도 되고 안넣어도 되고
				 //전체 데이터 (밑에 세줄) 쿼리결과 전체
				vec.add(rs.getString("subcategory_id"));
				vec.add(rs.getString("카테고리명"));
				vec.add(rs.getString("갯수"));
				
				data.add(vec);
				
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
		return data.size(); 
	}


	public int getColumnCount() {
		return columnName.size(); //컬럼명의 길이 meta 로 받은 것 
	}

	//컬럼이름 바꾸기 
	public String getColumnName(int col) {
		return columnName.get(col);
	}
	
	
	public Object getValueAt(int row, int col) {
	
		return data.get(row).get(col); //row번째가져오고, col번째 가져온다! 
	}
	
	

}
