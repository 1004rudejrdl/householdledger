package Controller;

import java.sql.Connection;
import java.sql.DriverManager;

public class DBUtility {
	public static Connection getConnection() {
		Connection con = null;
		// 1. MySql database class �ε��Ѵ�.
		try {
			Class.forName("com.mysql.jdbc.Driver");
			// 2. �ּ� ���̵� ��й�ȣ�� ���� ���ӿ�û�Ѵ�.
			con = DriverManager.getConnection("jdbc:mysql://192.168.0.207/householdledgerdb", "root", "123456");
			// MainController.callAlert("���Ἲ�� : �����ͺ��̽� ���Ἲ��");
		} catch (Exception e) {
			MainController.callAlert("�������: ����Ÿ���̽� �������� ���˹ٶ�");
			return null;
		}
		return con;
	}
}
