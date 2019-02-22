package Controller;

import java.net.URL;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.ResourceBundle;

import Model.UserInformation;
import Model.UserInformationDAO;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class LoginController implements Initializable {
	UserInformation userInformation;// ���������� ���� �����
	private ArrayList<UserInformation> userDBList = new ArrayList<>();// ������� ������ ���� ����Ʈ
	public Stage LoginStage;
	@FXML
	private TextField LuserID;// �α���ȭ���� �Ƶ��ؽ�Ʈ�ʵ�
	@FXML
	private PasswordField LpassWord;// �α���ȭ���� ����н������ʵ�
	@FXML
	private Button btnRegister;// ����ڵ�Ϲ�ư
	@FXML
	private Button btnLogin;// �α��ι�ư
	@FXML
	private Button btnExit;// �����ư
	@FXML
	private Button btnIDK;// �Ƶ�,��������ư

	public String ioUserID;
	public Stage IOStage;

	ObservableList<String> cmbFavoriteList = FXCollections.observableArrayList();// �޺��׷��� �ı����� ��Ƶа͵�

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		Media media = new Media("file:/c:/media/play.m4a");
		MediaPlayer mp = new MediaPlayer(media);
		mp.play();

		// 4. ����ڵ�Ϲ�ư�� �������� ����ڵ��â�� ��
		btnRegister.setOnAction((e) -> {
			handlebtnRegisterAction();
		});
//5. Login��ư�� �������� ����-���� ���â�� ��
		btnLogin.setOnAction((e) -> {
			handleBtnLoginAction();
		});
//6. Exit��ư�� �������� â�� ����
		btnExit.setOnAction((e) -> {
			handleLabelExitAction();
		});
//7. ID, ��������ư �������� �Ƶ�������â�� ��
		btnIDK.setOnAction((e) -> {
			handlebtnIDKAction();
		});

	}// end of initaliz

//4. ����ڵ�Ϲ�ư�� �������� ����ڵ��â�� ��
	private void handlebtnRegisterAction() {
		try {
			// â�� ���� ����
			Stage IOStage = new Stage();
			FXMLLoader loader = new FXMLLoader(getClass().getResource("../View/UserRegister.fxml"));
			Parent root = loader.load();
			Scene scene = new Scene(root);
			IOStage.initModality(Modality.WINDOW_MODAL);
			IOStage.initOwner(LoginStage);
			IOStage.setScene(scene);

			IOStage.setTitle("User Register");
			IOStage.setResizable(false);
			IOStage.show();

			// â�ȿ��� Action
			Button URjujang = (Button) root.lookup("#urBtnJujang");// �����ư����
			Button URcancel = (Button) root.lookup("#urBtnCancel");// ��ҹ�ư����

			TextField userID = (TextField) root.lookup("#userID");// ����ھƵ� �ؽ�Ʈ�ʵ忬��
			TextField passWord = (TextField) root.lookup("#passWord");// ��й�ȣ �ؽ�Ʈ�ʵ忬��

			TextField cmbFavoriteAnswer = (TextField) root.lookup("#cmbFavoriteAnswer");// �޺��ڽ��亯 �ؽ�Ʈ�ʵ忬��
			TextField phoneNumber = (TextField) root.lookup("#phoneNumber");// ��ȭ��ȣ �ؽ�Ʈ�ʵ忬��

			// �޺��ڽ�
			ComboBox<String> cmbFavoriteGroup = (ComboBox) root.lookup("#cmbFavoriteGroup");// �޺��ڽ��׷쿬��
			cmbFavoriteList.addAll("���� ���� ��°���?", "���� �����ϴ� ĳ���ʹ�?", "���� �����ϴ� ����?", "���� �����ϴ� ������?", "���ְ� �����ϴ� ���� �帣��?",
					"�츮�ƺ��̸���?");// �׷�ȿ� �ı���
			cmbFavoriteGroup.setItems(cmbFavoriteList);// �޺��ڽ��� �ı����� �ְڴ�

			// �����ư�������� �������� �ϵ�
			URjujang.setOnAction((e) -> {
				userInformation = new UserInformation(userID.getText(), passWord.getText(),
						cmbFavoriteGroup.getSelectionModel().getSelectedItem().toString(), cmbFavoriteAnswer.getText(),
						phoneNumber.getText()); // ������ ����������� �ִ´�
				UserInformationDAO.insertUserInformation(userInformation);// ����������� ����Ÿ���̽��� �ִ´�
				userID.clear();
				passWord.clear();
				cmbFavoriteGroup.getSelectionModel().clearSelection();
				cmbFavoriteAnswer.clear();
				phoneNumber.clear();

			});

			URcancel.setOnAction((e) -> {
				IOStage.close();
			}); // ��Ҵ����� â�ݱ�

		} catch (Exception e) {
			callAlert("ȭ����ȯ����:ȭ����ȯ�� ������ �ֽ��ϴ�. ����ٶ�.");
		}
	}

//5. Login��ư�� �������� ����-���� ���â�� ��

	private void handleBtnLoginAction() {

		// pass��� ��Ʈ��Ÿ���� �����
		String pass = null;
		// ������� ����������
		for (UserInformation userInformation : UserInformationDAO.getUserInformation()) {

			// ���� �α����ؽ�Ʈ�ʵ�� ���������� ���̵�� ����?
			if (userInformation.getUserid().equals(LuserID.getText())) {
				// ������ ����� ������ ������� ����
				ioUserID = userInformation.getUserid();
				pass = userInformation.getPassword();
			}
		} // end of for

		// ���� ����� ��������� �α����н����尡 ����������
		if (!(pass.equals(LpassWord.getText()))) {
			// �̷��˸�â�� ���.
			callAlert("�α��ν���: ���̵�,�н����尡 ���� �ʽ��ϴ�.");
			LuserID.clear();// �α��ξƵ� ������
			LpassWord.clear();// �α��κ�� ������
		} else {
			Parent root;
			FXMLLoader loader;
			try {
				// â�� ���� ����
				Stage IOStage = new Stage();
				loader = new FXMLLoader(getClass().getResource("../View/IncomeOutcome.fxml"));
				root = loader.load();
				MainController mainController = loader.getController();
				mainController.mainStage = IOStage;
				mainController.setIouserID(ioUserID);
				Scene scene = new Scene(root);
				IOStage.setScene(scene);
				IOStage.setResizable(false);// ��������
				LoginStage.close();// �α���â����
				IOStage.setTitle(LuserID.getText() + "'s household ledger"); // �α��ξƵ� ��
				IOStage.show();

				// callAlert("ȭ����ȯ����:����ȭ������ ��ȯ�Ǿ����ϴ�.");
			} catch (Exception e) {
				e.printStackTrace();
				callAlert("ȭ����ȯ����:ȭ����ȯ�� ������ �ֽ��ϴ�. ����ٶ�.");
			}
		} // end of else

	}// end of handleBtnLoginAction()

//6. Exit��ư�� �������� â�� ����
	private void handleLabelExitAction() {
		Platform.exit();
	}

//7. ID, ��������ư �������� �Ƶ�������â�� ��
	private void handlebtnIDKAction() {

		try {
			// â�� ���� ����
			Stage IOStage = new Stage();
			FXMLLoader loader = new FXMLLoader(getClass().getResource("../View/IDon'tKnow.fxml"));
			Parent root = loader.load();
			Scene scene = new Scene(root);
			IOStage.initModality(Modality.WINDOW_MODAL);
			IOStage.initOwner(LoginStage);
			IOStage.setResizable(false);
			IOStage.setScene(scene);
			IOStage.setTitle("I don't know");
			IOStage.show();

			// â�ȿ��� Action
			Button IDKok = (Button) root.lookup("#idkBtnOk");// Ȯ�ι�ư����
			IDKok.setOnAction((e) -> {
				TextField idkTxtPN = (TextField) root.lookup("#idkTxtPN");// ��ȭ��ȣ�ؽ�Ʈ�ʵ忬��

				// ������� ����������
				for (UserInformation userInformation : UserInformationDAO.getUserInformation()) {

					// ���� ��ȭ��ȣ�ؽ�Ʈ�ʵ�� ���������� ��ȭ��ȣ�� ����?
					if (userInformation.getPhonenumber().equals(idkTxtPN.getText())) {
						// ������ ����� ������ ������� ����
						callAlert("�˸�â : ���̵�- " + userInformation.getUserid() + " ��й�ȣ- "
								+ userInformation.getPassword());
						idkTxtPN.clear();// ��ȭ��ȣ������
					} else {
						// callAlert("����:��ϵ������� ��ȭ��ȣ�Դϴ�");
					}
				} // end of for
			});

			Button IDKcancel = (Button) root.lookup("#idkBtnCancel");// ��ҹ�ư����

			// ��Ҵ����� â�ݱ�
			IDKcancel.setOnAction((e) -> {
				IOStage.close();
			});
		} catch (Exception e) {

			callAlert("ȭ����ȯ����:ȭ����ȯ�� ������ �ֽ��ϴ�. ����ٶ�.");
		}
	}

// ��Ÿ: �˸�â(�߰���: �����ٰ�) ����: "��������: ��������� �Է����꼼��"
	private void callAlert(String contentText) {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Notice");
		alert.setHeaderText(contentText.substring(0, contentText.lastIndexOf(":")));
		alert.setContentText(contentText.substring(contentText.lastIndexOf(":") + 1));
		alert.showAndWait();
	}

}
