/*
 * ���� ī�װ��� ��ϵ� ��ǰ���� ������ (�����ִ°��� ���̺����ϴ� ��)
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
		 //�÷��� �������� �����̴� ���� �ƴϴ� ���� �����ڿ� ���� 
		 columnName.add("product_id");
		 columnName.add("subcategory_id");
		 columnName.add("product_name");
		 columnName.add("price");
		 columnName.add("img");
	}
	
	//rs �� �ױ����� ���Ϳ� ���� 
	
	//���콺�� ������ Ŭ���Ҷ����� id���� �ٲ�Ƿ�, �Ʒ��� �޼��带 �׶����� ȣ������!
	public void getList(int subcategory_id){
		PreparedStatement pstmt=null;
		ResultSet rs=null;
		
		
		String sql="select * from product"; //������ �ڿ��� ��ĭ�ٰų� where �տ��� ��ĭ�ٰų�
		sql+=" where subcategory_id=?";   //����ڰ� ����ī�װ��� �����ϸ� �����ȴ� ���� �ʿ��Ҷ������� ȣ���ϸ� �Ǵϱ� �μ��ι���
		try {
			pstmt=con.prepareStatement(sql);
			pstmt.setInt(1, subcategory_id); //String �� ������µ� �ǹ̸� �츮����-- ���ε庯�� �����Ҷ��� �̸������� ����ī�װ����̵�ϱ� Int 
			rs=pstmt.executeQuery();
			
			//���͵� �ʱ�ȭ 
			//columnName.removeAll(columnName);
			data.removeAll(data);
			
			ResultSetMetaData meta=rs.getMetaData();
			for(int i=1;i<=meta.getColumnCount();i++){
				columnName.add(meta.getColumnName(i));
				
			}
			System.out.println("getLIst �÷��� ũ���"+columnName.size());
			
			while(rs.next()){
				Vector vec=new Vector();
				vec.add(rs.getInt("product_id")); //�⺻�ڷ���->��ü�ڷ���: boxing
				vec.add(rs.getString("subcategory_id"));
				vec.add(rs.getString("product_name"));
				vec.add(rs.getString("price")); //JTAble�� ������ �ڷ��� STring??
				vec.add(rs.getString("img"));
				
				data.add(vec);	
			}//���ڵ� ������ ����
			System.out.println("getList ���ڵ� ũ���"+data.size());
			
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
		System.out.println("���ڵ��� ������"+data.size());
		return data.size();
		
	
	}
	public int getColumnCount() { //����ī�װ��� �ѷ��� �� ���ϱ� ggetColumnCount�� ȣ����� �ʰ��ִ�
		System.out.println("�÷��� ������"+columnName.size());
		return columnName.size();
	}
//�÷����� ����
	public String getColumnName(int col) {
		return columnName.get(col);
	}
	public Object getValueAt(int row, int col) {
		Object value=data.get(row).get(col); //value �� object �� �޴� �� ? ���� ��ü�� ��´���� �� �������� object �� �޼��忡 toString �� ������ �־ �ڵ�����ȣ��
		System.out.println("getValue ȣ��"+value);
		return data.get(row).get(col);
		
	
	}
}


/*
 * Dog g=new Dog();
 * system.out.println(g);
 * 
 * Dog�� �ּҰ��� �ӳ���  ���� ��ü�� ��´���� �� �� ������ �ڵ�ȣ���������
 * 
 * 
 * 
 * 
 * 
 * �����ͺ��̽��� �÷���ü�� �������̴� 
 * */

