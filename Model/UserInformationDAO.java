package Model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import Controller.DBUtility;
import Controller.MainController;

public class UserInformationDAO {
	public static ArrayList<UserInformation> UItbl = new ArrayList<>();

	// 1. ��������� ����ϴ� �Լ�
	public static int insertUserInformation(UserInformation ui) {
		int count = 0;

		// 1.1����Ÿ���̽��� ����������Է��ϴ� ������
		StringBuffer insertUserInformation = new StringBuffer();
		insertUserInformation.append("insert into userinformation ");
		insertUserInformation.append("(userid,password,cmbfavoritegroup,cmbfavoriteanswer,phonenumber) ");
		insertUserInformation.append("values ");
		insertUserInformation.append("(?,?,?,?,?) ");

		// 1.2����Ÿ���̽� Ŀ�ؼ��� �����;��Ѵ�
		Connection con = null;

		// 1.3�������� �����ؾ��� Statement�� �������Ѵ�.
		PreparedStatement psmt = null;
		try {
			con = DBUtility.getConnection();
			psmt = con.prepareStatement(insertUserInformation.toString());

			// 1.4�������� ��������Ÿ�� ����
			psmt.setString(1, ui.getUserid());
			psmt.setString(2, ui.getPassword());
			psmt.setString(3, ui.getCmbfavoritegroup());
			psmt.setString(4, ui.getCmbfavoriteanswer());
			psmt.setString(5, ui.getPhonenumber());

			// 1-5���������͸� ������ �������� �����Ѵ�.
			// executeUpdate(); �������� �����ؼ� ���̺� ������ �Ҷ� ����ϴ� ������
			count = psmt.executeUpdate();
			if (count == 0) {
				MainController.callAlert("���� �������� : ���� �������� ������ �ֽ��ϴ�.");
				return count;
			}
		} catch (Exception e) {

			MainController.callAlert("���� ���� : �����ͺ��̽� ���Կ� ������ �ֽ��ϴ�.");
		} finally {
			// 1-6. �ڿ���ü�� �ݾƾ��Ѵ�.
			try {
				if (psmt != null) {
					psmt.close();
				}
				if (con != null) {
					con.close();
				}
			} catch (Exception e) {
				MainController.callAlert("�ڿ� �ݱ� ���� : �ڿ� �ݱ⿡ ������ �ֽ��ϴ�.");
			}
		}
		return count;
	}

	// 2. ���̺��� ��ü������ ��� �������� �Լ�
	public static ArrayList<UserInformation> getUserInformation() {

		// 2-1. �����ͺ��̽� �����������̺� �ִ� ���ڵ带 ��� �������� ������
		String selectUserInformation = "select * from userinformation";
		// 2-2. �����ͺ��̽� Connection�� �����;� �Ѵ�.
		Connection con = null;
		// 2-3. �������� �����ؾ��� Statement�� ������ �Ѵ�.
		PreparedStatement psmt = null;
		// 2-4. �������� �����ϰ��� �����;��� ���ڵ带 ����ִ� ���ڱ� ��ü
		ResultSet rs = null;
		try {
			con = DBUtility.getConnection();
			psmt = con.prepareStatement(selectUserInformation);
			// 2-5. ���������͸� ������ �������� �����Ѵ�.(������ ġ�°�)
			// executeQuery(); �������� �����ؼ� ����� �����ö� ����ϴ� ������
			rs = psmt.executeQuery();
			if (rs == null) {
				MainController.callAlert("select ���� : select�� ������ �ֽ��ϴ�.");
				return null;
			}
			while (rs.next()) {
				UserInformation userInformation = new UserInformation(rs.getString(1), rs.getString(2), rs.getString(3),
						rs.getString(4), rs.getString(5));
				UItbl.add(userInformation);
			} // end of while
		} catch (SQLException e) {
			MainController.callAlert("���� ���� : �����ͺ��̽� ���Կ� ������ �ֽ��ϴ�.");
		} finally {
			// 2-6. �ڿ���ü�� �ݾƾ��Ѵ�.
			try {
				if (psmt != null) {
					psmt.close();
				}
				if (con != null) {
					con.close();
				}
			} catch (Exception e) {
				MainController.callAlert("�ڿ� �ݱ� ���� : �ڿ� �ݱ⿡ ������ �ֽ��ϴ�.");
			}
		}

		return UItbl;
	}

	// 3. ���̺�信�� ������ ���ڵ带 �����ͺ��̽����� �����ϴ� �Լ�
	public static int deleteUserInformationData(String userid) {

		// 3-1. �����ͺ��̽� �л����̺� �ִ� ���ڵ带 �����ϴ� ������
		String deleteUserInformation = "delete from userInformation where no =  ? ";
		// 3-2. �����ͺ��̽� Connection�� �����;� �Ѵ�.
		Connection con = null;
		// 3-3. �������� �����ؾ��� Statement�� ������ �Ѵ�.
		PreparedStatement psmt = null;
		// 3-4. �������� �����ϰ��� �����;��� ���ڵ带 ����ִ� ���ڱ� ��ü
		int count = 0;
		try {
			con = DBUtility.getConnection();
			psmt = con.prepareStatement(deleteUserInformation);
			psmt.setString(1, userid);
			// 3-5. ���������͸� ������ �������� �����Ѵ�.(������ ġ�°�)
			// executeQuery(); �������� �����ؼ� ����� �����ö� ����ϴ� ������
			count = psmt.executeUpdate();
			if (count == 0) {
				MainController.callAlert("delete ���� : delete�� ������ �ֽ��ϴ�.");
				return count;
			}
		} catch (SQLException e) {
			MainController.callAlert("delete ���� : delete�� ������ �ֽ��ϴ�.");
		} finally {
			// 3-6. �ڿ���ü�� �ݾƾ��Ѵ�.
			try {
				if (psmt != null) {
					psmt.close();
				}
				if (con != null) {
					con.close();
				}
			} catch (Exception e) {
				MainController.callAlert("�ڿ� �ݱ� ���� : �ڿ� �ݱ⿡ ������ �ֽ��ϴ�.");
			}
		}
		return count;
	}

	// 4. ���̺���Ϳ��� ������ ���ڵ带 �����ͺ��̽� ���̺� �����ϴ� �Լ�.
	public static int updateUserInformationData(UserInformation userinformation) {
		int count = 0;
		// 4-1. �����ͺ��̽��� �л����̺� �����ϴ� ������
		StringBuffer updateUserInformation = new StringBuffer();
		updateUserInformation.append("update userInformation set ");
		updateUserInformation.append("userid=?, password=?, Cmbfavoritegroup=?, Cmbfavoriteanswer=?, phonenumber=?");
		// 4-2. �����ͺ��̽� Connection�� �����;� �Ѵ�.
		Connection con = null;
		// 4-3. �������� �����ؾ��� Statement�� ������ �Ѵ�.
		PreparedStatement psmt = null;
		try {
			con = DBUtility.getConnection();
			psmt = con.prepareStatement(updateUserInformation.toString());
			// 4-4. �������� ���� �����͸� �����Ѵ�.
			psmt.setString(1, userinformation.getUserid());
			psmt.setString(2, userinformation.getPassword());
			psmt.setString(3, userinformation.getCmbfavoritegroup());
			psmt.setString(4, userinformation.getCmbfavoriteanswer());
			psmt.setString(5, userinformation.getPhonenumber());
			// 4-5. ���������͸� ������ �������� �����Ѵ�.
			// executeUpdate(); �������� �����ؼ� ���̺� ������ �Ҷ� ����ϴ� ������
			count = psmt.executeUpdate();
			if (count == 0) {
				MainController.callAlert("update �������� : update �������� ������ �ֽ��ϴ�.");
				return count;
			}
		} catch (SQLException e) {
			MainController.callAlert("update ���� : update �� ������ �ֽ��ϴ�.");
		} finally {
			// 4-6. �ڿ���ü�� �ݾƾ��Ѵ�.
			try {
				if (psmt != null) {
					psmt.close();
				}
				if (con != null) {
					con.close();
				}
			} catch (Exception e) {
				MainController.callAlert("�ڿ� �ݱ� ���� : �ڿ� �ݱ⿡ ������ �ֽ��ϴ�.");
			}
		}
		return count;
	}
}
