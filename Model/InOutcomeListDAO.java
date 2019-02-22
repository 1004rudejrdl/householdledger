package Model;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import Controller.DBUtility;
import Controller.MainController;

public class InOutcomeListDAO {
	public static ArrayList<InOutcomeList> IOLtbl = new ArrayList<>();

	// 1. �������� ����ϴ� �Լ�
	public static int insertInOutcome(InOutcomeList iol) {
		int count = 0;

		// 1.1����Ÿ���̽��� ���������Է��ϴ� ������
		StringBuffer insertInOutcomeList = new StringBuffer();
		insertInOutcomeList.append("insert into inoutcomelist ");
		insertInOutcomeList
				.append("(no, userid, date ,hourminute, income, outcome, biggroup, smallgroup, payment, contents) ");
		insertInOutcomeList.append("values ");
		insertInOutcomeList.append("(?,?,?,?,?,?,?,?,?,?) ");

		// 1.2����Ÿ���̽� Ŀ�ؼ��� �����;��Ѵ�
		Connection con = null;

		// 1.3�������� �����ؾ��� Statement�� �������Ѵ�.
		PreparedStatement psmt = null;
		try {
			con = DBUtility.getConnection();
			psmt = con.prepareStatement(insertInOutcomeList.toString());
			// 1.4�������� ��������Ÿ�� ����

			psmt.setInt(1, 0);
			psmt.setString(2, iol.getUserid());
			psmt.setDate(3, iol.getDate());
			psmt.setString(4, iol.getHourminute());
			psmt.setString(5, iol.getIncome());
			psmt.setString(6, iol.getOutcome());
			psmt.setString(7, iol.getBiggroup());
			psmt.setString(8, iol.getSmallgroup());
			psmt.setString(9, iol.getPayment());
			psmt.setString(10, iol.getContents());

			// 1-5���������͸� ������ �������� �����Ѵ�.
			// executeUpdate(); �������� �����ؼ� ���̺� ������ �Ҷ� ����ϴ� ������
			count = psmt.executeUpdate();
			if (count == 0) {
				MainController.callAlert("���� �������� : ���� �������� ������ �ֽ��ϴ�.");
				return count;
			}
		} catch (SQLException e) {
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
	public static ArrayList<InOutcomeList> getInOutcomeList() {

		// 2-1. �����ͺ��̽� �����������̺� �ִ� ���ڵ带 ��� �������� ������
		String selectInOutcomeList = "select * from inoutcomelist ";
		// 2-2. �����ͺ��̽� Connection�� �����;� �Ѵ�.
		Connection con = null;
		// 2-3. �������� �����ؾ��� Statement�� ������ �Ѵ�.
		PreparedStatement psmt = null;
		// 2-4. �������� �����ϰ��� �����;��� ���ڵ带 ����ִ� ���ڱ� ��ü
		ResultSet rs = null;
		try {
			con = DBUtility.getConnection();
			psmt = con.prepareStatement(selectInOutcomeList);
			// 2-5. ���������͸� ������ �������� �����Ѵ�.(������ ġ�°�)
			// executeQuery(); �������� �����ؼ� ����� �����ö� ����ϴ� ������
			rs = psmt.executeQuery();
			if (rs == null) {
				MainController.callAlert("select ���� : select�� ������ �ֽ��ϴ�.");
				return null;
			}
			while (rs.next()) {
				InOutcomeList inOutcomeList = new InOutcomeList(rs.getInt(1), rs.getString(2), rs.getDate(3),
						rs.getString(4), rs.getString(5), rs.getString(6), rs.getString(7), rs.getString(8),
						rs.getString(9), rs.getString(10));
				IOLtbl.add(inOutcomeList);
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
		return IOLtbl;
	}

	// 3. ���̺�信�� ������ ���ڵ带 �����ͺ��̽����� �����ϴ� �Լ�
	public static int deleteInOutcomeListData(Integer no) {

		// 3-1. �����ͺ��̽� �л����̺� �ִ� ���ڵ带 �����ϴ� ������
		String deleteInOutcomeList = "delete from inOutcomeList where no =  ? ";
		// 3-2. �����ͺ��̽� Connection�� �����;� �Ѵ�.
		Connection con = null;
		// 3-3. �������� �����ؾ��� Statement�� ������ �Ѵ�.
		PreparedStatement psmt = null;
		// 3-4. �������� �����ϰ��� �����;��� ���ڵ带 ����ִ� ���ڱ� ��ü
		int count = 0;
		try {
			con = DBUtility.getConnection();
			psmt = con.prepareStatement(deleteInOutcomeList);
			psmt.setInt(1, no);
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

	// 4. ���̺��� ��ü������ ��� �������� �Լ�
	public static ArrayList<InOutcomeList> getDateList(String when, String userid) {
		IOLtbl.clear();
		// 4-1. �����ͺ��̽� �����������̺� �ִ� ���ڵ带 ��� �������� ������
		String selectInOutcomeList = "select * from inoutcomelist where date = '" + when + "' AND userid = '" + userid
				+ "'";
		// 4-2. �����ͺ��̽� Connection�� �����;� �Ѵ�.
		Connection con = null;
		// 4-3. �������� �����ؾ��� Statement�� ������ �Ѵ�.
		PreparedStatement psmt = null;
		// 4-4. �������� �����ϰ��� �����;��� ���ڵ带 ����ִ� ���ڱ� ��ü
		ResultSet rs = null;
		try {
			con = DBUtility.getConnection();
			psmt = con.prepareStatement(selectInOutcomeList);
			// 4-5. ���������͸� ������ �������� �����Ѵ�.(������ ġ�°�)
			// executeQuery(); �������� �����ؼ� ����� �����ö� ����ϴ� ������
			rs = psmt.executeQuery();
			if (rs == null) {
				MainController.callAlert("select ���� : select�� ������ �ֽ��ϴ�.");
				return null;
			}
			while (rs.next()) {
				InOutcomeList inOutcomeList = new InOutcomeList(rs.getInt(1), rs.getString(2), rs.getDate(3),
						rs.getString(4), rs.getString(5), rs.getString(6), rs.getString(7), rs.getString(8),
						rs.getString(9), rs.getString(10));
				IOLtbl.add(inOutcomeList);
			} // end of while
		} catch (SQLException e) {
			MainController.callAlert("���� ���� : �����ͺ��̽� ���Կ� ������ �ֽ��ϴ�.");
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
		return IOLtbl;
	}

	// 5. ���̺���Ϳ��� ������ ���ڵ带 �����ͺ��̽� ���̺� �����ϴ� �Լ�.
	public static int updateInOutcomeData(InOutcomeList inOutcomeList) {
		int count = 0;
		// 5-1. �����ͺ��̽��� �����������̺� �����ϴ� ������
		StringBuffer updateInOutcomeList = new StringBuffer();
		updateInOutcomeList.append("update inOutcomeList set ");
		updateInOutcomeList
				.append("hourminute=?, income=?, outcome=?, biggroup=?, smallgroup=?,payment=?,contents=? where no=? ");
		// 5-2. �����ͺ��̽� Connection�� �����;� �Ѵ�.
		Connection con = null;
		// 5-3. �������� �����ؾ��� Statement�� ������ �Ѵ�.
		PreparedStatement psmt = null;
		try {
			con = DBUtility.getConnection();
			psmt = con.prepareStatement(updateInOutcomeList.toString());
			// 5-4. �������� ���� �����͸� �����Ѵ�.

			psmt.setString(1, inOutcomeList.getHourminute());
			psmt.setString(2, inOutcomeList.getIncome());
			psmt.setString(3, inOutcomeList.getOutcome());
			psmt.setString(4, inOutcomeList.getBiggroup());
			psmt.setString(5, inOutcomeList.getSmallgroup());
			psmt.setString(6, inOutcomeList.getPayment());
			psmt.setString(7, inOutcomeList.getContents());
			psmt.setInt(8, inOutcomeList.getNo());
			// 5-5. ���������͸� ������ �������� �����Ѵ�.
			// executeUpdate(); �������� �����ؼ� ���̺� ������ �Ҷ� ����ϴ� ������
			count = psmt.executeUpdate();
			if (count == 0) {
				MainController.callAlert("update �������� : update �������� ������ �ֽ��ϴ�.");
				return count;
			}
		} catch (SQLException e) {
			MainController.callAlert("update ���� : update �� ������ �ֽ��ϴ�.");
		} finally {
			// 5-6. �ڿ���ü�� �ݾƾ��Ѵ�.
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

	// �׳⵵�� ���� ����
	public static int getInComeDataOfYears(String year, String month) {
		int result = 0;
		// �����ͺ��̽� �����������̺� �ִ� ���ڵ带 ��� �������� ������

		String selectInOutcomeList = "select sum(income) from inoutcomelist where date like '" + year + "-" + month
				+ "-%%'";

		// �����ͺ��̽� Connection�� �����;� �Ѵ�.
		Connection con = null;
		// �������� �����ؾ��� Statement�� ������ �Ѵ�.
		PreparedStatement psmt = null;
		// �������� �����ϰ��� �����;��� ���ڵ带 ����ִ� ���ڱ� ��ü
		ResultSet rs = null;
		try {
			con = DBUtility.getConnection();
			psmt = con.prepareStatement(selectInOutcomeList);
			// ���������͸� ������ �������� �����Ѵ�.(������ ġ�°�)
			// executeQuery(); �������� �����ؼ� ����� �����ö� ����ϴ� ������
			rs = psmt.executeQuery();
			if (rs == null) {
				MainController.callAlert("select ���� : select�� ������ �ֽ��ϴ�.");
				return 0;
			}
			while (rs.next()) {
				result = rs.getInt(1);
			} // end of while
		} catch (SQLException e) {
			MainController.callAlert("���� ���� : �����ͺ��̽� ���Կ� ������ �ֽ��ϴ�.");
		} finally {
			// �ڿ���ü�� �ݾƾ��Ѵ�.
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
		return result;
	}

	// �׳⵵�� ���� ����
	public static int getOutComeDataOfYears(String year, String month) {
		int result = 0;
		// �����ͺ��̽� �����������̺� �ִ� ���ڵ带 ��� �������� ������
		String selectInOutcomeList = "select sum(outcome) from inoutcomelist where date like '" + year + "-" + month
				+ "-%%' ";

		// �����ͺ��̽� Connection�� �����;� �Ѵ�.
		Connection con = null;
		// �������� �����ؾ��� Statement�� ������ �Ѵ�.
		PreparedStatement psmt = null;

		// �������� �����ϰ��� �����;��� ���ڵ带 ����ִ� ���ڱ� ��ü
		ResultSet rs = null;
		try {
			con = DBUtility.getConnection();
			psmt = con.prepareStatement(selectInOutcomeList);
			// ���������͸� ������ �������� �����Ѵ�.(������ ġ�°�)
			// executeQuery(); �������� �����ؼ� ����� �����ö� ����ϴ� ������
			rs = psmt.executeQuery();
			if (rs == null) {
				MainController.callAlert("select ���� : select�� ������ �ֽ��ϴ�.");
				return 0;
			}
			while (rs.next()) {
				result = rs.getInt(1);
			} // end of while
		} catch (SQLException e) {

			MainController.callAlert("���� ���� : �����ͺ��̽� ���Կ� ������ �ֽ��ϴ�.");
		} finally {
			// �ڿ���ü�� �ݾƾ��Ѵ�.
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
		return result;
	}

	// �׳⵵�� �з���
	public static int Categorize(String year, String Coutcome) {
		int result = 0;
		// �����ͺ��̽� �����������̺� �ִ� ���ڵ带 ��� �������� ������
		String selectInOutcomeList = "select count(biggroup) from inoutcomelist where biggroup like '" + Coutcome
				+ "' AND date like '" + year + "-%%-%%'";
		// �����ͺ��̽� Connection�� �����;� �Ѵ�.
		Connection con = null;
		// �������� �����ؾ��� Statement�� ������ �Ѵ�.
		PreparedStatement psmt = null;

		// �������� �����ϰ��� �����;��� ���ڵ带 ����ִ� ���ڱ� ��ü
		ResultSet rs = null;
		try {
			con = DBUtility.getConnection();
			psmt = con.prepareStatement(selectInOutcomeList);
			// ���������͸� ������ �������� �����Ѵ�.(������ ġ�°�)
			// executeQuery(); �������� �����ؼ� ����� �����ö� ����ϴ� ������
			rs = psmt.executeQuery();
			if (rs == null) {
				MainController.callAlert("select ���� : select�� ������ �ֽ��ϴ�.");
				return 0;
			}
			while (rs.next()) {
				result = rs.getInt(1);
			} // end of while
		} catch (SQLException e) {

			MainController.callAlert("���� ���� : �����ͺ��̽� ���Կ� ������ �ֽ��ϴ�.");
		} finally {
			// �ڿ���ü�� �ݾƾ��Ѵ�.
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
		return result;
	}

	// �������ܺ�-����
	public static int Payment0(String year, String Payment) {
		int result = 0;
		// �����ͺ��̽� �����������̺� �ִ� ���ڵ带 ��� �������� ������
		String selectInOutcomeList = "select count(income) from inoutcomelist where payment like '" + Payment
				+ "' AND date like '" + year + "-%%-%%'";
		// �����ͺ��̽� Connection�� �����;� �Ѵ�.
		Connection con = null;
		// �������� �����ؾ��� Statement�� ������ �Ѵ�.
		PreparedStatement psmt = null;

		// �������� �����ϰ��� �����;��� ���ڵ带 ����ִ� ���ڱ� ��ü
		ResultSet rs = null;
		try {
			con = DBUtility.getConnection();
			psmt = con.prepareStatement(selectInOutcomeList);
			// ���������͸� ������ �������� �����Ѵ�.(������ ġ�°�)
			// executeQuery(); �������� �����ؼ� ����� �����ö� ����ϴ� ������
			rs = psmt.executeQuery();
			if (rs == null) {
				MainController.callAlert("select ���� : select�� ������ �ֽ��ϴ�.");
				return 0;
			}
			while (rs.next()) {
				result = rs.getInt(1);
			} // end of while
		} catch (SQLException e) {

			MainController.callAlert("���� ���� : �����ͺ��̽� ���Կ� ������ �ֽ��ϴ�.");
		} finally {
			// �ڿ���ü�� �ݾƾ��Ѵ�.
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
		return result;
	}

	// �������ܺ�-����
	public static int Payment1(String year, String Payment) {
		int result = 0;
		// �����ͺ��̽� �����������̺� �ִ� ���ڵ带 ��� �������� ������
		String selectInOutcomeList = "select count(outcome) from inoutcomelist where payment like '" + Payment
				+ "' AND date like '" + year + "-%%-%%'";
		// �����ͺ��̽� Connection�� �����;� �Ѵ�.
		Connection con = null;
		// �������� �����ؾ��� Statement�� ������ �Ѵ�.
		PreparedStatement psmt = null;

		// �������� �����ϰ��� �����;��� ���ڵ带 ����ִ� ���ڱ� ��ü
		ResultSet rs = null;
		try {
			con = DBUtility.getConnection();
			psmt = con.prepareStatement(selectInOutcomeList);
			// ���������͸� ������ �������� �����Ѵ�.(������ ġ�°�)
			// executeQuery(); �������� �����ؼ� ����� �����ö� ����ϴ� ������
			rs = psmt.executeQuery();
			if (rs == null) {
				MainController.callAlert("select ���� : select�� ������ �ֽ��ϴ�.");
				return 0;
			}
			while (rs.next()) {
				result = rs.getInt(1);
			} // end of while
		} catch (SQLException e) {

			MainController.callAlert("���� ���� : �����ͺ��̽� ���Կ� ������ �ֽ��ϴ�.");
		} finally {
			// �ڿ���ü�� �ݾƾ��Ѵ�.
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
		return result;
	}

	// �ܾ�
	public static int balanceIncome(String income) {
		int result = 0;
		// �����ͺ��̽� �����������̺� �ִ� ���ڵ带 ��� �������� ������

		String selectInOutcomeList = "select sum(income) from inoutcomelist;";

		// �����ͺ��̽� Connection�� �����;� �Ѵ�.
		Connection con = null;
		// �������� �����ؾ��� Statement�� ������ �Ѵ�.
		PreparedStatement psmt = null;
		// �������� �����ϰ��� �����;��� ���ڵ带 ����ִ� ���ڱ� ��ü
		ResultSet rs = null;
		try {
			con = DBUtility.getConnection();
			psmt = con.prepareStatement(selectInOutcomeList);
			// ���������͸� ������ �������� �����Ѵ�.(������ ġ�°�)
			// executeQuery(); �������� �����ؼ� ����� �����ö� ����ϴ� ������
			rs = psmt.executeQuery();
			if (rs == null) {
				MainController.callAlert("select ���� : select�� ������ �ֽ��ϴ�.");
				return 0;
			}
			while (rs.next()) {
				result = rs.getInt(1);
			} // end of while
		} catch (SQLException e) {
			MainController.callAlert("���� ���� : �����ͺ��̽� ���Կ� ������ �ֽ��ϴ�.");
		} finally {
			// �ڿ���ü�� �ݾƾ��Ѵ�.
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
		return result;
	}

	public static int balanceOutcome(String outcome) {
		int result = 0;
		// �����ͺ��̽� �����������̺� �ִ� ���ڵ带 ��� �������� ������

		String selectInOutcomeList = "select sum(outcome) from inoutcomelist;";

		// �����ͺ��̽� Connection�� �����;� �Ѵ�.
		Connection con = null;
		// �������� �����ؾ��� Statement�� ������ �Ѵ�.
		PreparedStatement psmt = null;
		// �������� �����ϰ��� �����;��� ���ڵ带 ����ִ� ���ڱ� ��ü
		ResultSet rs = null;
		try {
			con = DBUtility.getConnection();
			psmt = con.prepareStatement(selectInOutcomeList);
			// ���������͸� ������ �������� �����Ѵ�.(������ ġ�°�)
			// executeQuery(); �������� �����ؼ� ����� �����ö� ����ϴ� ������
			rs = psmt.executeQuery();
			if (rs == null) {
				MainController.callAlert("select ���� : select�� ������ �ֽ��ϴ�.");
				return 0;
			}
			while (rs.next()) {
				result = rs.getInt(1);
			} // end of while
		} catch (SQLException e) {
			MainController.callAlert("���� ���� : �����ͺ��̽� ���Կ� ������ �ֽ��ϴ�.");
		} finally {
			// �ڿ���ü�� �ݾƾ��Ѵ�.
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
		return result;
	}

}