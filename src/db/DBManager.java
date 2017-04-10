/*1.정보를 한곳에 두기
 * 데이터베이스 계쩡 정보를 중복해서 기재하지 않기 위함(db연동을 하는 각각의 클래스에서)
 * 
 * 2. 인스턴스의 갯수를 한개만 둬보기
 * -어플리케이션 가동 중 생성되는 Connection 객체를 하나로 통일하기 위함
 * */

package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBManager {
	static private DBManager instance;
	private String driver="oracle.jdbc.driver.OracleDriver";
	private String url="jdbc:oracle:thin:@localhost:1521:XE";
	private String user="bread";
	private String password="bread";
	private Connection con;
	
	
	private DBManager(){
		/*1. 드라이버 로드
		 *2. 접속
		 *3. 쿼리문 수행
		 *4. 반납 , 해제
		 * */
		 	try {
				Class.forName(driver);//1.드라이버로드  // 하나의 자료형인 클래스 위에는 예약어 ... 여기 자료형클래스 (클래스에대한 정보)
				con=DriverManager.getConnection(url,user,password);
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
	}
	static public DBManager getInstance() {
		if(instance==null){
			instance= new DBManager();
		
		}
		return instance;
	}
	public Connection getConnection(){
		return con;
		
	}
	public void disConnect(Connection con){
		try {
			if(con!=null){
				con.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
